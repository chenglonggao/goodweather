package com.gcl.goodweather.db;

import java.util.ArrayList;
import java.util.List;

import com.gcl.goodweather.domain.City;
import com.gcl.goodweather.domain.County;
import com.gcl.goodweather.domain.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库访问类
 * ？？？没有采用单例模式，看看有没有什么bug
 * @author gcl
 */
public class GoodWeatherDB {

	public GoodWeatherOpenHelper helper;
	public SQLiteDatabase db;

	public GoodWeatherDB(Context context) {
		helper = new GoodWeatherOpenHelper(context);//问题1：不同的helper对象生成的数据库是否是一个对象？？？
		db = helper.getWritableDatabase();
	}

	
	
	/**
	 *  从数据库中下载province的数据
	 *  return provinceList
	 */
	public List<Province> loadProvinceFromDB() {

		List<Province> provinceList = new ArrayList<Province>();
		Cursor cursor = db.query("provinceTable", null, null, null, null, null,
				null);

		while (cursor.moveToNext()){
			int provinceId = cursor.getInt(cursor.getColumnIndex("provinceId"));//数据库系统自动生成的
			String provinceCode = cursor.getString(cursor.getColumnIndex("provinceCode"));
			String provinceName = cursor.getString(cursor.getColumnIndex("provinceName"));

			Province provinceObj = new Province();
			provinceObj.setprovinceId(provinceId);
			provinceObj.setprovinceCode(provinceCode);
			provinceObj.setprovinceName(provinceName);

			provinceList.add(provinceObj);

		}

		return provinceList;
	}

	/**
	 * 往数据库中写入province的数据
	 */
	public void writeProvinceToDb(Province province){
		String provinceCode = province.getprovinceCode();
		String provinceName = province.getprovinceName();
		
		
		ContentValues values = new ContentValues();
		values.put("provinceCode", provinceCode);
		values.put("provinceName", provinceName);
		
		db.insert("provinceTable", null, values);
		
	}
	
	
	
	
	/**
	 *  从数据库中下载city的数据
	 *  return cityList
	 */
	public List<City> loadCityFromDB(String provinceCode) {
		String arg = String.valueOf(provinceCode);
		List<City> cityList = new ArrayList<City>();
		Cursor cursor = db.query("cityTable", null, "provinceCode = ?", new String[]{arg}, null, null,
				null);//表示查询某一省份下的所有城市
		
		while (cursor.moveToNext()){
			int cityId = cursor.getInt(cursor.getColumnIndex("cityId"));
			String cityCode = cursor.getString(cursor.getColumnIndex("cityCode"));
			String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
			
			City cityObj = new City();
			cityObj.setCityId(cityId);
			cityObj.setCityCode(cityCode);
			cityObj.setCityName(cityName);
			
			cityList.add(cityObj);
			
		}
		
		return cityList;
	}
	
	/**
	 * 往数据库中写入city的数据
	 */
	public void writeCityToDb(City city){
		String cityCode = city.getCityCode();
		String cityName = city.getCityName();
		String provinceCode = city.getProvinceCode();
		
		
		ContentValues values = new ContentValues();
		values.put("cityCode", cityCode);
		values.put("cityName", cityName);
		values.put("provinceCode", provinceCode);
		
		db.insert("cityTable", null, values);
		
	}
	
	
	
	
	
	/**
	 *  从数据库中下载County的数据
	 *  return CountyList
	 */
	public List<County> loadCountyFromDB(String cityCode) {
		
		List<County> CountyList = new ArrayList<County>();
		Cursor cursor = db.query("countyTable", null, "cityCode = ?", new String[]{cityCode}, null, null,
				null);
		
		while (cursor.moveToNext()){
			int countyId = cursor.getInt(cursor.getColumnIndex("countyId"));
			String countyCode = cursor.getString(cursor.getColumnIndex("countyCode"));
			String countyName = cursor.getString(cursor.getColumnIndex("countyName"));
			
			County countyObj = new County();
			countyObj.setCountyId(countyId);
			countyObj.setCityCode(cityCode);
			countyObj.setCountyCode(countyCode);
			countyObj.setCountyName(countyName);
			
			CountyList.add(countyObj);
			
		} 
		
		return CountyList;
	}
	
	/**
	 * 往数据库中写入county的数据
	 */
	public void writeCountyToDb(County county){
		String countyCode = county.getCountyCode();
		String cityCode = county.getCityCode();
		String countyName = county.getCountyName();
		
		
		ContentValues values = new ContentValues();
		values.put("countyCode", countyCode);
		values.put("countyName", countyName);
		values.put("cityCode", cityCode);
		
		db.insert("countyTable", null, values);
	}
 
}
