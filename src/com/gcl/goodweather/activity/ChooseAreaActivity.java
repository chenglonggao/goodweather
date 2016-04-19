package com.gcl.goodweather.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gcl.goodweather.R;
import com.gcl.goodweather.db.GoodWeatherDB;
import com.gcl.goodweather.domain.City;
import com.gcl.goodweather.domain.County;
import com.gcl.goodweather.domain.Province;
import com.gcl.goodweather.utils.HttpCallbackListener;
import com.gcl.goodweather.utils.HttpUtils;
import com.gcl.goodweather.utils.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	private TextView title_text;
	private ListView list_view;
	private ProgressDialog progressDialog;
	private ArrayAdapter<String> adapter;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	private Province provinceObj;
	private City cityObj;
	private County countyObj;
	private String provinceCode;
	private String cityCode;
	private String provinceName;
	private String cityName;
	public static final String PROVINCE_TITLE = "全国";
	public static final int PROVINCE_LEVEL = 1;
	public static final int CITY_LEVEL = 2;
	public static final int COUNTY_LEVEL = 3;
	
	
	
	
	/**
	 * represent which area(province, city, county) is displayed in the current activity
	 */
	public int currentLevel = PROVINCE_LEVEL;
	
	
	public static final String CHINA_CODE = "0";//
//	public final GoodWeatherDB db = new GoodWeatherDB(this);???为什么在这个位置设置db不行呢？？没有被初始化还是其他原因
	/**
	 * use to restore the data which is displayed in the listview;
	 */
	private List<String> dataList = new ArrayList<String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.area_activity_layout);

		title_text = (TextView) findViewById(R.id.title_text);
		list_view = (ListView) findViewById(R.id.list_view);

		
		adapter = new ArrayAdapter<String>(ChooseAreaActivity.this, android.R.layout.simple_list_item_1, dataList);
			
		//we can set the adapter even if the datalist of it is empty, 
		//then we can add the data to the datalist and invoke the notify() method to update the listview,
		//so that we can display the data on the UI.
		list_view.setAdapter(adapter);
		list_view.setOnItemClickListener(new SelectedItemListener());
		
		displayProvince();//display the data of the province in the ChooseAreaActivity
	}
	

	/**
	 * query the data of the provinces(from database or server), and then display it in the listview
	 */
	private void displayProvince() {
		dataList.clear();
		final GoodWeatherDB db = new GoodWeatherDB(this);
		//when the database is not empty;
		provinceList = db.loadProvinceFromDB();
		title_text.setText(PROVINCE_TITLE);
		
		if (provinceList.size() != 0) {
			for (Iterator<Province> iter = provinceList.iterator(); iter.hasNext();) {
				Province provinceObj = iter.next();
				dataList.add(provinceObj.getprovinceName());

				adapter.notifyDataSetChanged();
				list_view.setSelection(0);
			}
		} else {
			queryAndDisplayFromServer(CHINA_CODE);
		}
	}
	
	
	
	/**
	 * query the data of the city(from the database or server) and display it in the activity;
	 */
	private void displayCity(String provinceCode) {
		dataList.clear();
		final GoodWeatherDB db = new GoodWeatherDB(this);
		
		cityList = db.loadCityFromDB(provinceCode);
		
			if (cityList.size() != 0) {
				for (Iterator<City> iter = cityList.iterator(); iter.hasNext();) {
					cityObj = iter.next();
					
					
					dataList.add(cityObj.getCityName());

					adapter.notifyDataSetChanged();
					list_view.setSelection(0);
				}
			} else {
				queryAndDisplayFromServer(provinceCode);
			}
	}
	
	
	
	/**
	 * query the data of the county(from the database or server) and display it in the activity;
	 */
	private void displayCounty(String cityCode) {
		dataList.clear();
		
		final GoodWeatherDB db = new GoodWeatherDB(this);
		
		countyList = db.loadCountyFromDB(cityCode);
		
		if (countyList.size() != 0) {
			for (Iterator<County> iter = countyList.iterator(); iter.hasNext();) {
				countyObj = iter.next();
				dataList.add(countyObj.getCountyName());
				
				adapter.notifyDataSetChanged();
				list_view.setSelection(0);
			}
		} else {
			queryAndDisplayFromServer(cityCode);
		}
		
	}
	
	/**
	 * called when you press the back key
	 */
	@Override
	public void onBackPressed() {
		if(currentLevel == PROVINCE_LEVEL){
			super.onBackPressed();//return to the home screen
		} else if(currentLevel == CITY_LEVEL) {
			currentLevel = PROVINCE_LEVEL;
			title_text.setText(PROVINCE_TITLE);
			displayProvince();
			
		} else if(currentLevel == COUNTY_LEVEL) {
			currentLevel = CITY_LEVEL;
			title_text.setText(provinceName);
			displayCity(provinceCode);
		}
	}


	/**
	 * if the data has not been written into the database, 
	 * then we should request it from the server and write it into the database,
	 * and then invoke the corresponding display() method;
	 */
	private void queryAndDisplayFromServer(final String areaCode) {
		final GoodWeatherDB db = new GoodWeatherDB(this);
		String address;
		
		if(areaCode == CHINA_CODE) {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city" + areaCode + ".xml";
			Log.i("TAG", "areaCode = " + areaCode);
		}
		
		showProgressDialog();
		//This is a classical mode: use an interface as a args, then handle the result by implementing the method of the interface
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void requestSuccessfully(final String response) {//this method runs in the worker thread
				runOnUiThread(new Runnable() {//跨线程方法：将改变ui界面的操作，由子线程中的操作转换到主线程
					@Override
					public void run() {
						
						if (currentLevel == PROVINCE_LEVEL) {
							Utils.parseHttpResponseOfP(response, db);
							displayProvince();
						} else if (currentLevel == CITY_LEVEL) {
							Utils.parseHttpResponseOfCity(response, areaCode, db);
							displayCity(areaCode);
							
							
						} else if (currentLevel == COUNTY_LEVEL){
							Utils.parseHttpResponseOfCounty(response, areaCode, db);
							displayCounty(areaCode);
							
						}
						
						closeProgressDialog();
					}
				});
			}

			@Override
			public void requestFailed() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "网络请求失败", Toast.LENGTH_LONG).show();
					}
				});
				
			}
		});
	}
	
	
	
	/**
	 * the listener of the items in the listview, when the item is clicked,
	 *  the onItemClick() method will be invoked.
	 */
	private class SelectedItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			
			if(currentLevel == PROVINCE_LEVEL && provinceList != null && provinceList.size() != 0) {//如果点击的是province的条目时
				provinceObj = provinceList.get(position);//得到点击省份的province对象
				provinceCode = provinceObj.getprovinceCode();
				provinceName = provinceObj.getprovinceName();
				title_text.setText(provinceName);
				
				currentLevel = CITY_LEVEL;//将界面调成city级别
				displayCity(provinceCode);
			} else if (currentLevel == CITY_LEVEL && cityList != null && cityList.size() != 0) {
				cityObj = cityList.get(position);
				cityCode = cityObj.getCityCode();
				cityName = cityObj.getCityName();
				title_text.setText(cityName);
				
				currentLevel = COUNTY_LEVEL;
				displayCounty(cityCode);
			}
		}
	}
	
	
	
	
	/**
	 * show the progress in the activity
	 */
	private void showProgressDialog() {
		if(progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载中……");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
		}
		progressDialog.show();
		
	}
	
	
	/**
	 * close the ProgressDialog
	 */
	private void closeProgressDialog() {
		
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	

}
