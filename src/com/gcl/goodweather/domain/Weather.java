package com.gcl.goodweather.domain;

/**
 *class which contains the information of the weather 
 */
public class Weather {
	
	/**
	 * The idCode of the place about which we want to get its weather information
	 */
	private String countyCode;
	/**
	 * The weather information of the current county
	 */
	private String weatherInfo;
	/**
	 * the low temperature of the weather
	 */
	private String temp1;
	/**
	 * the high temperature of the weather
	 */
	private String temp2;
	/**
	 * The time of posting weather information.
	 */
	private String ptime;
	
	
	
	
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyId) {
		this.countyCode = countyId;
	}
	public String getWeatherInfo() {
		return weatherInfo;
	}
	public void setWeatherInfo(String weatherInfo) {
		this.weatherInfo = weatherInfo;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	public String getTemp2() {
		return temp2;
	}
	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}
	public String getPtime() {
		return ptime;
	}
	public void setPtime(String ptime) {
		this.ptime = ptime;
	}
	
	
	

}
