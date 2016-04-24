package com.gcl.goodweather.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.gcl.goodweather.db.GoodWeatherDB;
import com.gcl.goodweather.domain.City;
import com.gcl.goodweather.domain.County;
import com.gcl.goodweather.domain.Province;
import com.gcl.goodweather.domain.Weather;

/**
 * Convience class which contains methods which can convert stream to String, 
 * parse the json data
 */
public class Utility {

	/**
	 * convient method which converts the inputStream to String
	 */
	public static String handleStream(InputStream is) {
		String str;
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			while (null != (str = br.readLine())) {
				sb.append(str);
			}
			
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result = sb.toString();
		return result;
	}

	/**
	 * write the provinces data to the database
	 * @return 	true: the data has been written successfully;false: the response is empty or httprequest failed; 
	 */
	public static boolean parseHttpResponseOfP(String response, GoodWeatherDB db) {
		if (!TextUtils.isEmpty(response)) {// �жϷ�������Ӧ�Ƿ�Ϊ��

			List<Province> provinceList = new ArrayList<Province>();
			Province provinceObj;
			String[] provinces = response.split(",");
			for (int i = 0; i < provinces.length; i++) {
				String[] provinceContent = provinces[i].split("\\u007C");//��|���ߵ�ת���ַ�

				provinceObj = new Province();
				provinceObj.setprovinceCode(provinceContent[0]);
				provinceObj.setprovinceName(provinceContent[1]);
				
				db.writeProvinceToDb(provinceObj);
				provinceList.add(provinceObj);
			}
			return true;
		}
		return false;// ����ķ��ؽ��Ϊnull;
	}
	
	
	
	
	/**
	 * write the cities data to the database
	 * @return 	true: the data has been written successfully;false: the response is empty or httprequest failed; 
	 */
	public static boolean parseHttpResponseOfCity(String response, String provinceCode, GoodWeatherDB db) {
		if (!TextUtils.isEmpty(response)) {// �жϷ�������Ӧ�Ƿ�Ϊ��
			
			List<City> cityList = new ArrayList<City>();
			City cityObj;
			String[] cities = response.split(",");
			
			//If there is one data in the cities[]
			for (int i = 0; i < cities.length; i++) {
				String[] cityContent = cities[i].split("\\u007C");//��|���ߵ�ת���ַ�
				
				cityObj = new City();
				cityObj.setCityCode(cityContent[0]);
				cityObj.setCityName(cityContent[1]);
				cityObj.setProvinceCode(provinceCode);//��һ��provinceCode���ݣ�������ʾĳһʡ�����г���


				db.writeCityToDb(cityObj);
				cityList.add(cityObj);
			}
			return true;
		}
		return false;// ����ķ��ؽ��Ϊnull��
	}
	
	
	
	/**
	 * write the counties data to the database
	 * @return 	true: the data has been written successfully;false: the response is empty or httprequest failed; 
	 * Note that the cities like Beijing, Tianjing, Shanghai don't have counties.
	 */
	public static boolean parseHttpResponseOfCounty(String response, String cityCode, GoodWeatherDB db) {
		if (!TextUtils.isEmpty(response)) {// �жϷ�������Ӧ�Ƿ�Ϊ��
			
			List<County> countyList = new ArrayList<County>();
			County countyObj;
			String[] counties = response.split(",");
			for (int i = 0; i < counties.length; i++) {
				String[] countyContent = counties[i].split("\\u007C");//��|���ߵ�ת���ַ�
				
				countyObj = new County();
				countyObj.setCountyCode(countyContent[0]);
				countyObj.setCountyName(countyContent[1]);
				countyObj.setCityCode(cityCode);
				
				
				db.writeCountyToDb(countyObj);
				countyList.add(countyObj);
			}
			return true;
		}
		return false;// when the response is empty or null;
	}
	
	
	
	/**
	 * Convient method which is used to parse the JSON data,and store the parse result to the sharedPreferences file.
	 * @param response the data returned by requesting the server
	 * @return	true the parsing is successful,false the parsing is failed.
	 */
	public static boolean parseJsonData(String response, Context context) {
		
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONObject weatherInfoObj = jsonObj.getJSONObject("weatherinfo");
			
			String weatherCode = weatherInfoObj.getString("cityid");
			String weatherContent = weatherInfoObj.getString("weather");
			String temp1 = weatherInfoObj.getString("temp1");
			String temp2 = weatherInfoObj.getString("temp2");
			String ptime = weatherInfoObj.getString("ptime");
			String placeName = weatherInfoObj.getString("city");
			
			Date currentDate = new Date();
			SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy.MM.dd");
			String currentTime = simpleFormat.format(currentDate);

			String[] times = currentTime.split("\\.");
			String year = times[0];
			String month = times[1];
			String day = times[2];
			String dateText = year + "��" + month + "��" + day + "��";

			
			
			SharedPreferences mySP = context.getSharedPreferences("weatherData", Context.MODE_PRIVATE);
			Editor editor = mySP.edit();
			editor.putString("weatherCode", weatherCode);
			editor.putString("weatherContent", weatherContent);
			editor.putString("temp1", temp1);
			editor.putString("temp2", temp2);
			editor.putString("ptime", ptime);
			editor.putString("placeName", placeName);
			editor.putString("dateText", dateText);
			
			editor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
			return false;//represents that the parsing is failed.
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
}
