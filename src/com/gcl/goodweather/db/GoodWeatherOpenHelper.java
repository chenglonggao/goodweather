package com.gcl.goodweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 生成该应用数据库的帮助类
 * 
 * @author gcl
 */
public class GoodWeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * 我的数据库的名称
	 */
	public static final String DATABASE_NAME = "goodWeather.db";
	
	
	
	/**
	 * 创建province表的SQL语句
	 */
	public static final String CREATE_PROCINCE_TABLE = "create table provinceTable("
			+ "provinceId   	integer primary key	autoincrement,"
			+ "provinceCode   	text				not null,"
			+ "provinceName 	text				not null)";

	/**
	 * 创建city表的SQL语句
	 */
	public static final String CREATE_CITY_TABLE = "create table cityTable("
			+ "provinceCode text 				not null,"
			+ "cityId   	integer primary key	autoincrement,"
			+ "cityCode 	text					not null," 
			+ "cityName 	text				not null)";

	/**
	 * 创建county表的SQL语句
	 */
	public static final String CREATE_COUNTY_TABLE = "create table countyTable("
			+ "cityCode 	text					not null,"
			+ "countyId   	integer primary key	autoincrement,"
			+ "countyCode 	text					not null,"
			+ "countyName 	text				not null)";

	public GoodWeatherOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	/*
	 * 数据库第一次被创建时，在该数据库中创建省、市、县的table
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROCINCE_TABLE);
		db.execSQL(CREATE_CITY_TABLE);
		db.execSQL(CREATE_COUNTY_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
