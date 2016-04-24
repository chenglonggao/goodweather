package com.gcl.goodweather.alarm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gcl.goodweather.activity.WeatherInfoActivity;
import com.gcl.goodweather.utils.HttpCallbackListener;
import com.gcl.goodweather.utils.HttpUtility;
import com.gcl.goodweather.utils.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//This is where we handle our own operations
				updateWeather();
				
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		Intent i = new Intent(this, UpdateAlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		long anHour = 1000 * 60 * 60 * 1;
		long triggerAtMillis = SystemClock.elapsedRealtime() + anHour;
		
		
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	private void updateWeather(){
		SharedPreferences sp = getApplicationContext().getSharedPreferences("weatherData",MODE_PRIVATE);
		String weatherCode = sp.getString("weatherCode", "");
		
		if(!TextUtils.isEmpty(weatherCode)){
			String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
			
			HttpUtility.sendHttpRequest(address, new HttpCallbackListener() {
				
				@Override
				public void requestSuccessfully(String response) {
					if (!TextUtils.isEmpty(response)) {
						Date currentTime = new Date();
						SimpleDateFormat simpleFormat = new SimpleDateFormat("hh:mm.ss");
						String time = simpleFormat.format(currentTime);
						Log.i("TAG", "定时更新返回的天气信息 = " + response + "  时间： " + time);
						
						Utility.parseJsonData(response, getApplicationContext());
					}
					
				}
				
				@Override
				public void requestFailed() {
					//there isn't runonUiThread,because this method belongs to Activity class.
					
				}
			});
		}
	}
	

}
