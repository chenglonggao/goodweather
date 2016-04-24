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
	}

	
	
	/**
	 *  �����ݿ�������province������
	 *  return provinceList
	 */
	public List<Province> loadProvinceFromDB() {
		db = helper.getWritableDatabase();
		
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
		
		cursor.close();
		db.close();
		return provinceList;
	}

	/**
	 * �����ݿ���д��province������
	 */
	public void writeProvinceToDb(Province province){
		db = helper.getWritableDatabase();
		
		String provinceCode = province.getprovinceCode();
		String provinceName = province.getprovinceName();
		
		
		ContentValues values = new ContentValues();
		values.put("provinceCode", provinceCode);
		values.put("provinceName", provinceName);
		
		db.insert("provinceTable", null, values);
		db.close();
	}
	
	
	
	
	/**
	 *  �����ݿ�������city������
	 *  return cityList
	 */
	public List<City> loadCityFromDB(String provinceCode) {
		db = helper.getWritableDatabase();
		
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
		
		cursor.close();
		db.close();
		return cityList;
	}
	
	/**
	 * �����ݿ���д��city������
	 */
	public void writeCityToDb(City city){
		db = helper.getWritableDatabase();
		
		String cityCode = city.getCityCode();
		String cityName = city.getCityName();
		String provinceCode = city.getProvinceCode();
		
		
		ContentValues values = new ContentValues();
		values.put("cityCode", cityCode);
		values.put("cityName", cityName);
		values.put("provinceCode", provinceCode);
		
		db.insert("cityTable", null, values);
		db.close();
	}
	
	
	
	
	
	/**
	 *  �����ݿ�������County������
	 *  return CountyList
	 */
	public List<County> loadCountyFromDB(String cityCode) {
		db = helper.getWritableDatabase();
		
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
		
		cursor.close();
		db.close();
		return CountyList;
	}
	
	/**
	 * �����ݿ���д��county������
	 */
	public void writeCountyToDb(County county){
		db = helper.getWritableDatabase();
		
		String countyCode = county.getCountyCode();
		String cityCode = county.getCityCode();
		String countyName = county.getCountyName();
		
		
		ContentValues values = new ContentValues();
		values.put("countyCode", countyCode);
		values.put("countyName", countyName);
		values.put("cityCode", cityCode);
		
		db.insert("countyTable", null, values);
		db.close();
	}
 
}
