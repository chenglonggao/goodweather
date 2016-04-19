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
				Log.i("TAG", "������������ ");
				HttpURLConnection connection = null;
				try {
					URL url = new URL(path);
					connection = (HttpURLConnection) url.openConnection();
					
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setReadTimeout(8000);
					int code = connection.getResponseCode();		
					
					Log.i("TAG", "�������󷵻���code= " + code);
					
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

		// ������ô���������ѯʧ�ܣ�����������ʱ������һ���ӿڷ�������������������

	}

}
