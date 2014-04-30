package com.uis.comm.example.fragmenttabs;

import java.util.Iterator;

import com.uis.comm.example.fragmenttabs.data.City_DB;
import com.uis.comm.example.fragmenttabs.data.HttpWrapper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements
		OnFragmentInteractionListener {

	private FragmentTransaction transaction;

	private Fragment f1;
	private Fragment f2;

	private Fragment f3;

	private Fragment f4;

	private Fragment f5;

	private ImageButton currentImgBtn = null;

	private SparseArray<ImgBtnImageData> ImgBtnMap = new SparseArray<ImgBtnImageData>();

	private LocationManager lm = null;

	/**
	 * location manager ÇéÊìæÇ∑ÇÈ
	 * 
	 * @return
	 */
	public LocationManager getLm() {
		return lm;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lm = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

		transaction = getFragmentManager().beginTransaction();

		f1 = MainFragment.newInstance("MainFragment", "Main");
		f2 = SencondFragment.newInstance("SecondFragment", "Second");
		f3 = ThirdFragment.newInstance("ThirdFragment", "Third");
		f4 = FourthFragment.newInstance("FourthFragment", "Fourth");
		f5 = FifthFragment.newInstance("FifthFragment", "Fifth");

		transaction.add(R.id.mainFrameLayout, f1);

		transaction.commit();
		currentImgBtn = (ImageButton) findViewById(R.id.imgBtn1);
		initImageButtonsData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		((SencondFragment) f2).updateLocation();
		((TextView) findViewById(R.id.json)).setText("ç¸êVíÜ......");
		((TextView) findViewById(R.id.json)).setTextColor(Color.RED);
	}

	@Override
	public void onFragmentInteraction(Fragment f, Bundle b) {
		// Log satrt
		Log.d("MainActivity", "Fragement " + f.getClass().getName()
				+ " Action happen");
		Iterator<String> it = b.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			Log.d("MainActivity", "[" + key + " : " + b.get(key).toString()
					+ "]");
		}
		// Log end

		if (f == f2) {
			int id = b.getInt("id");

			Button btn = (Button) findViewById(id);
			String con = b.getString("content");
			if (null != btn) {

				con = con.replaceAll("\\s", "");

				int index = con.indexOf("\",\"types\":[\"locality\",");

				if (index > 0 && index < con.length()) {
					String subCon = con.substring(0, index);

					index = subCon.lastIndexOf("\"");

					final String city = subCon.substring(index + 1);

					((TextView) this.findViewById(R.id.sublocality))
							.setText(city);

					final String code = City_DB.CITES_ADDR_CODE.get(city);

					if (null != code) {
						HttpWrapper.getHttpResponse(
								"http://www.weather.com.cn/data/cityinfo/"
										+ code + ".html", new MyCallBack() {

									@Override
									public void call(Bundle b) {
										String weatherInfo = b
												.getString("content");

										int index1 = weatherInfo
												.indexOf("temp1");
										weatherInfo = weatherInfo
												.substring(index1 + 8);
										final String hightTemp = weatherInfo
												.substring(0, weatherInfo
														.indexOf("\""));

										index1 = weatherInfo.indexOf("temp2");
										weatherInfo = weatherInfo
												.substring(index1 + 8);
										final String lowTemp = weatherInfo
												.substring(0, weatherInfo
														.indexOf("\""));

										index1 = weatherInfo
												.indexOf("weather\":");
										weatherInfo = weatherInfo
												.substring(index1 + 10);
										final String weather = weatherInfo
												.substring(0, weatherInfo
														.indexOf("\""));

										Looper l = MainActivity.this
												.getMainLooper();

										Handler h = new Handler(l);

										h.post(new Runnable() {

											@Override
											public void run() {
												((TextView) MainActivity.this
														.findViewById(R.id.route))
														.setText(weather
																+ "\r\n"
																+ "ç≈çÇüÉâ∑ÅF"
																+ hightTemp
																+ "\r\n"
																+ "ç≈í·üÉâ∑"
																+ lowTemp);

												((TextView) findViewById(R.id.json))
														.setText("ç¸êVê¨å˜......");
												((TextView) findViewById(R.id.json))
														.setTextColor(Color.BLACK);
											}

										});
									}
								});

					}
				}

			}
		}
	}

	public void onBtnClick(View v) {
		Fragment f = null;

		switch (v.getId()) {
		case R.id.imgBtn1:
			f = f1;

			break;

		case R.id.imgBtn2:
			f = f2;
			break;
		case R.id.imgBtn3:
			f = f3;
			break;
		case R.id.imgBtn4:
			f = f4;
			break;
		case R.id.imgBtn5:
			f = f5;
			break;
		default:
			Log.d("MainActivity",
					"UNKNOWN View has been CLICK. [ID : " + v.getId() + "]");
			break;
		}
		if (null != f) {

			currentImgBtn.setImageDrawable(ImgBtnMap.get(currentImgBtn.getId())
					.getNor_img_id());
			currentImgBtn = (ImageButton) v;
			currentImgBtn.setImageDrawable(ImgBtnMap.get(currentImgBtn.getId())
					.getPress_img_id());

			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainFrameLayout, f);
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.addToBackStack(null);

			transaction.commit();

		}

	}

	private void initImageButtonsData() {
		ImgBtnMap.put(R.id.imgBtn1, new ImgBtnImageData(R.drawable.home,
				R.drawable.home_press));
		ImgBtnMap.put(R.id.imgBtn2, new ImgBtnImageData(R.drawable.cover,
				R.drawable.cover_press));
		ImgBtnMap.put(R.id.imgBtn3, new ImgBtnImageData(R.drawable.database,
				R.drawable.database_press));
		ImgBtnMap.put(R.id.imgBtn4, new ImgBtnImageData(R.drawable.customers,
				R.drawable.customers_press));
		ImgBtnMap.put(R.id.imgBtn5, new ImgBtnImageData(R.drawable.settings,
				R.drawable.settings_press));
	}

	final class ImgBtnImageData {

		public ImgBtnImageData(int res1, int res2) {
			nor_img_id = MainActivity.this.getResources().getDrawable(res1);
			press_img_id = MainActivity.this.getResources().getDrawable(res2);
		}

		public Drawable getNor_img_id() {
			return nor_img_id;
		}

		public Drawable getPress_img_id() {
			return press_img_id;
		}

		private Drawable nor_img_id;

		private Drawable press_img_id;
	}
}
