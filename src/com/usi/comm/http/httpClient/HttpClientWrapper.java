package com.usi.comm.http.httpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;

import com.uis.comm.example.fragmenttabs.MyCallBack;
import com.usi.comm.log.Log;
import com.usi.comm.log.LogFactory;

public class HttpClientWrapper {

	private static final Log<HttpClientWrapper> innerLog = new LogFactory()
			.getLog(HttpClientWrapper.class);

	private static MyCallBack callBack;

	public static void sendHttpRequestByGet(final String host,
			final String path, final String query, final MyCallBack cb)
			throws URISyntaxException, ClientProtocolException, IOException {
		// public URI(String scheme, String userInfo, String host, int port,
		// String path, String query,String fragment)
		final URI uri = new URI("http", null, host, -1,
				path.startsWith("/") ? path : "/" + path, query, null);

		innerLog.d("request url : " + uri);

		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpClient hc = new DefaultHttpClient();

				callBack = cb;

				HttpGet hg = new HttpGet(uri);

				try {
					hc.execute(hg, responseHandler);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

	public static boolean abortRequest() {
		return false;

	}

	private static ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

		@Override
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {

			int status = response.getStatusLine().getStatusCode();

			switch (status) {
			case HttpStatus.SC_OK:// 200 OK
				HttpEntity entity = response.getEntity();

				String content =entity != null ?  EntityUtils.toString(entity):null;

				innerLog.d("http response entity : " + content);

				Bundle b = new Bundle();
				b.putString("content", content );
				callBack.call(b);
				return content;
			case HttpStatus.SC_BAD_REQUEST:// /400 bad request
			case HttpStatus.SC_UNAUTHORIZED:// 401 Unauthorized
			case HttpStatus.SC_FORBIDDEN:// 403 Forbidden
			case HttpStatus.SC_NOT_FOUND:// 404 Not Found
			case HttpStatus.SC_INTERNAL_SERVER_ERROR:// 500 Internal Server
														// Error
			case HttpStatus.SC_BAD_GATEWAY:// 502: // Bad Gateway
			case HttpStatus.SC_SERVICE_UNAVAILABLE:// 503 Service
													// Unavailable
			case HttpStatus.SC_GATEWAY_TIMEOUT:// 504 Gateway Timeout
				android.util.Log.w("HttpClientWrapper",
						"http response error, response code : " + status);

				break;
			}
			// debug log
			innerLog.d("http response error, response code : " + status);

			android.util.Log.w("HttpClientWrapper",
					"http response error, response code : UNKNOW");

			return null;

		}

	};
}
