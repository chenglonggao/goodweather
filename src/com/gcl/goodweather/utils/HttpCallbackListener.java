package com.gcl.goodweather.utils;

/**
 * you can use the different impletations of this Interface to handle the response of 
 * whether the request of the different areaLevel(like province, city, county) is successful;
 */
public interface HttpCallbackListener {
	
	/**
	 * invoke this method when the request the server successfully.
	 */
	public void requestSuccessfully(String response);
	
	
	/**
	 * invoke this method when the request the server unsuccessfully.
	 */
	public void requestFailed();

}
