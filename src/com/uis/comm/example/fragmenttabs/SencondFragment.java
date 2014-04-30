package com.uis.comm.example.fragmenttabs;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.uis.comm.example.fragmenttabs.data.City;
import com.uis.comm.example.fragmenttabs.data.HttpWrapper;
import com.usi.comm.http.httpClient.HttpClientWrapper;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link SencondFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class SencondFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private static SencondFragment self;

	private Location nowLocation;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment BlankFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SencondFragment newInstance(String param1, String param2) {
		if (null == self) {
			self = new SencondFragment();
		}

		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		self.setArguments(args);
		return self;
	}

	public SencondFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}

		nowLocation = ((MainActivity) this.getActivity()).getLm()
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.sencod_fragment, container, false);
		return v;

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	public void updateLocation() {
		getWeathInfo();
		((MainActivity) this.getActivity()).getLm().requestSingleUpdate(
				LocationManager.GPS_PROVIDER, new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						Toast.makeText(
								(MainActivity) SencondFragment.this
										.getActivity(), "onStatusChanged", 0)
								.show();
					}

					@Override
					public void onProviderEnabled(String provider) {
						Toast.makeText(
								(MainActivity) SencondFragment.this
										.getActivity(), "Provider Enabled", 0)
								.show();
					}

					@Override
					public void onProviderDisabled(String provider) {
						Toast.makeText(
								(MainActivity) SencondFragment.this
										.getActivity(), "Provider Disabled", 0)
								.show();
					}

					@Override
					public void onLocationChanged(Location location) {
						Toast.makeText(
								(MainActivity) SencondFragment.this
										.getActivity(), "Location Changed", 0)
								.show();
						
						nowLocation= location;
						Log.d("nowLocation : ", nowLocation.toString());
						getWeathInfo();
					}
				}, null);
	}

	private void getWeathInfo() {
		City c = new City();

		if(null == nowLocation)
			nowLocation = ((MainActivity) this.getActivity()).getLm()
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(null == nowLocation) return;
		c.setLocation(nowLocation);

		String queryStr = c.getLocation().getLongitude() + ","
				+ c.getLocation().getLatitude();

		String httpUrl = "http://maps.google.com/maps/api/geocode/json?latlng="
				+ queryStr + "&language=zh-CN&sensor=false";
//
//		HttpWrapper.getHttpResponse(httpUrl, myCB);
		
		
		
		try {
			HttpClientWrapper.sendHttpRequestByGet("maps.google.com", "/maps/api/geocode/json", "latlng="+ queryStr + "&language=zh-CN&sensor=false", myCB);
		} catch (ClientProtocolException e) {
			Log.e("","ClientProtocolException"+e.getLocalizedMessage());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			Log.e("","URISyntaxException" + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("","IOException" + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}
	
	private MyCallBack myCB = new MyCallBack() {

		@Override
		public void call(final Bundle b) {
			Looper l = SencondFragment.this.getActivity().getMainLooper();

			Handler h = new Handler(l);

			h.post(new Runnable() {

				@Override
				public void run() {
					b.putInt("id", R.id.refresh);

					if (null != nowLocation)
						((MainActivity) SencondFragment.this.getActivity())
								.onFragmentInteraction(
										SencondFragment.this, b);
				}
			});

		}
	};
}
