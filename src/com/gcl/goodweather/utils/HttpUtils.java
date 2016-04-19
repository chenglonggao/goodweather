package com.gcl.goodweather.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * convient class which enclose the operation of requesting the server
 * 
 * @author gcl
 * 
 */
public class HttpUtils {


	/**
	 * the operation of sending request to the server
	 */
	public static void sendHttpRequest(final String path, final HttpCallbackListener hcl) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("TAG", "进入网络请求 ");
				HttpURLConnection connection = null;
				try {
					URL url = new URL(path);
					connection = (HttpURLConnection) url.openConnection();
					
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setReadTimeout(8000);
					int code = connection.getResponseCode();		
					
					Log.i("TAG", "网络请求返回码code= " + code);
					
					if (200 == code) {
						InputStream is = connection.getInputStream();
						String response = Utils.handleStream(is);


						hcl.requestSuccessfully(response);//invoke this method when the request the server successfully.
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					
					hcl.requestFailed();//invoke this method when the request the server unsuccessfully.
				} finally {
					if(connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();

		// 考虑怎么处理表明查询失败！！！！！到时可以用一个接口方法，来反馈到界面上

	}

}
