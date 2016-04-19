package com.gcl.goodweather.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.gcl.goodweather.db.GoodWeatherDB;
import com.gcl.goodweather.domain.City;
import com.gcl.goodweather.domain.County;
import com.gcl.goodweather.domain.Province;

/**
 * ������ 1������streamת����string�ַ���
 * 
 */
public class Utils {

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
				String[] cityContent = counties[i].split("\\u007C");//��|���ߵ�ת���ַ�
				
				countyObj = new County();
				countyObj.setCountyCode(cityContent[0]);
				countyObj.setCountyName(cityContent[1]);
				countyObj.setCityCode(cityCode);
				
				
				db.writeCountyToDb(countyObj);
				countyList.add(countyObj);
			}
			return true;
		}
		return false;// when the response is empty or null;
	}
	
	
}



