package com.uis.comm.example.fragmenttabs.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.uis.comm.example.fragmenttabs.MyCallBack;

import android.os.Bundle;
import android.util.Log;

public class HttpWrapper {


	/**
	 * Temp
	 * 
	 * @param httpUrl
	 * @return
	 */
	public static String getHttpResponse(String httpUrl) {

		try {
			Log.d("HttpWrapper", httpUrl);
			URL url = new URL(httpUrl);
			URLConnection connection = url.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			StringBuffer sb = new StringBuffer();
			String s = null;
			while ((s = reader.readLine()) != null) {
				sb.append(s);
			}
			
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void getHttpResponse(final String httpUrl,final MyCallBack cb) {
		
		Log.d("HttpWrapper",httpUrl);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String s = getHttpResponse(httpUrl);
				
				Bundle b = new Bundle();
				b.putString("content",s);
				cb.call(b);
			}
		}).start();
		
	}
}
