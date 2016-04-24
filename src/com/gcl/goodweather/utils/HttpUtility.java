package com.gcl.goodweather.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * convient class which enclose the operation of requesting the server
 * 
 * @author gcl
 * 
 */
public class HttpUtility {
	/**
	 * the operation of sending request to the server
	 */
	public static void sendHttpRequest(final String path, final HttpCallbackListener hcl) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(path);
					connection = (HttpURLConnection) url.openConnection();
					
					if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {//If there isn't the two lines codes, it will throw EOFException.I get this from the stack overflow site.
						connection.setRequestProperty("Connection", "close");
					}
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setReadTimeout(8000);
					int code = connection.getResponseCode();		
			 
					if (200 == code) {
						InputStream is = connection.getInputStream();
						String response = Utility.handleStream(is);
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
	}
}
