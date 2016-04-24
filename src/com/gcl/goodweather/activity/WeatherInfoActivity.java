package com.gcl.goodweather.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gcl.goodweather.R;
import com.gcl.goodweather.alarm.AutoUpdateService;
import com.gcl.goodweather.utils.HttpCallbackListener;
import com.gcl.goodweather.utils.HttpUtility;
import com.gcl.goodweather.utils.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which uses to display the information of the weather
 */
@SuppressLint("SimpleDateFormat") public class WeatherInfoActivity extends Activity implements OnClickListener{

	/**
	 * The place name of the weatherInfo
	 */
	private TextView place_name;
	/**
	 * click this you can return to the previous activity
	 */
	private ImageView back_button;
	/**
	 * click this you can update the weather information
	 */
	private ImageView update_button;
	/**
	 * the time of updating the weather information
	 */
	private  TextView update_time;
	/**
	 * the current date
	 */
	private TextView date_text;
	/**
	 * the weather information of the selected place
	 */
	private TextView weatherInfo;
	/**
	 * the low temperature of the selected place
	 */
	private TextView tmp_low;
	/**
	 * the high temperature of the selected place
	 */
	private TextView tmp_high;
	/**
	 * the layout of the view which display the detail of the weather
	 */
	private LinearLayout weatherInfo_ll;
	
	/**
	 * determine whether the request for the weather is successful
	 */
	public static final int COUNTY_CODE = 1;
	public static final int WEATHER_CODE = 2;
	
//	private String placeName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_activity_layout);

		// get the importent views of the layout file
		place_name = (TextView) findViewById(R.id.place_name);
		back_button = (ImageView) findViewById(R.id.back_button);
		update_button = (ImageView) findViewById(R.id.update_button);
		update_time = (TextView) findViewById(R.id.update_time);
		date_text = (TextView) findViewById(R.id.date_text);
		weatherInfo = (TextView) findViewById(R.id.weatherInfo);
		tmp_low = (TextView) findViewById(R.id.tmp_low);
		tmp_high = (TextView) findViewById(R.id.tmp_high);
		weatherInfo_ll = (LinearLayout) findViewById(R.id.weatherInfo_ll);
		
		back_button.setOnClickListener(this);
		update_button.setOnClickListener(this);

		weatherInfo_ll.setVisibility(View.INVISIBLE);
		
		SharedPreferences sp = this.getSharedPreferences("weatherData",MODE_PRIVATE);// find a callback method when
		boolean countySelected = sp.getBoolean("countySelected", false);//through this value to determine where there is data in the sp file.
		String weatherContent = sp.getString("weatherContent", "");
		
		
		
		if(!countySelected && !TextUtils.isEmpty(weatherContent)){//display the weather when not clicking the item
			displayWeatherInfo();//show the weather data which exists in the sp file without requesting the server
			
		} else {
			Intent receiveIntent = getIntent();
			String countyCode = receiveIntent.getStringExtra("countyCode");
			update_time.setText("同步中……");
			queryWeatherCode(countyCode);
			
			Intent startServiceIntent = new Intent(this, AutoUpdateService.class);
			this.startService(startServiceIntent);
		}
	}

	
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		queryFromServer(address, COUNTY_CODE);
	}


	private void queryWeatherInfo(String weatherCode){
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address, WEATHER_CODE);
	}

	private void queryFromServer(String address, final int type){
		
		HttpUtility.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void requestSuccessfully(String response) {
				if (!TextUtils.isEmpty(response)) {
					switch (type) {
					case COUNTY_CODE:
						String[] values = response.split("\\u007C");
						String weatherCode = values[1];
						queryWeatherInfo(weatherCode);
						break;
					case WEATHER_CODE:
						Utility.parseJsonData(response, WeatherInfoActivity.this);
						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								displayWeatherInfo();
								
							}
						});
						break;
					}
				}
			}

			@Override
			public void requestFailed() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						update_time.setText("同步失败");
					}
				});
			}
		});
	}



	/**
	 * display the weather information in the ui
	 */
	public void displayWeatherInfo() {
		
		SharedPreferences sp = this.getSharedPreferences("weatherData",MODE_PRIVATE);// find a callback method when
		String weatherContent = sp.getString("weatherContent", "");
		String temp1 = sp.getString("temp1", "");
		String temp2 = sp.getString("temp2", "");
		String ptime = sp.getString("ptime", "");
		String placeName = sp.getString("placeName", "");
		String dateText = sp.getString("dateText", "");;
		
		Editor editor = sp.edit();
		editor.putBoolean("countySelected", false);//this
		editor.commit();

		tmp_low.setText(temp1);
		tmp_high.setText(temp2);
		update_time.setText("今天" + ptime + "发布");
		weatherInfo.setText(weatherContent);
		place_name.setText(placeName);
		date_text.setText(dateText);

		weatherInfo_ll.setVisibility(View.VISIBLE);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			Intent chooseAreaIntent = new Intent(this, ChooseAreaActivity.class);
			chooseAreaIntent.putExtra("isFromWeatherActivity", true);
			
			startActivity(chooseAreaIntent);
			WeatherInfoActivity.this.finish();
			break;
		case R.id.update_button:
			SharedPreferences sp = this.getSharedPreferences("weatherData",MODE_PRIVATE);// find a callback method when
			String weatherCode = sp.getString("weatherCode", "");
			if(!TextUtils.isEmpty(weatherCode)) {
				update_time.setText("同步中……");
				queryWeatherInfo(weatherCode);
			}
			break;

		}
	}
}
