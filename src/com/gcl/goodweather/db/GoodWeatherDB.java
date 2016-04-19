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
 * ���ݿ������
 * ������û�в��õ���ģʽ��������û��ʲôbug
 * @author gcl
 */
public class GoodWeatherDB {

	public GoodWeatherOpenHelper helper;
	public SQLiteDatabase db;

	public GoodWeatherDB(Context context) {
		helper = new GoodWeatherOpenHelper(context);//����1����ͬ��helper�������ɵ����ݿ��Ƿ���һ�����󣿣���
		db = helper.getWritableDatabase();
	}

	
	
	/**
	 *  �����ݿ�������province������
	 *  return provinceList
	 */
	public List<Province> loadProvinceFromDB() {

		List<Province> provinceList = new ArrayList<Province>();
		Cursor cursor = db.query("provinceTable", null, null, null, null, null,
				null);

		while (cursor.moveToNext()){
			int provinceId = cursor.getInt(cursor.getColumnIndex("provinceId"));//���ݿ�ϵͳ�Զ����ɵ�
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
	 * �����ݿ���д��province������
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
	 *  �����ݿ�������city������
	 *  return cityList
	 */
	public List<City> loadCityFromDB(String provinceCode) {
		String arg = String.valueOf(provinceCode);
		List<City> cityList = new ArrayList<City>();
		Cursor cursor = db.query("cityTable", null, "provinceCode = ?", new String[]{arg}, null, null,
				null);//��ʾ��ѯĳһʡ���µ����г���
		
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
	 * �����ݿ���д��city������
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
	 *  �����ݿ�������County������
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
	 * �����ݿ���д��county������
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
