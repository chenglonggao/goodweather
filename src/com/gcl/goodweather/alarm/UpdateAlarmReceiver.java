package com.gcl.goodweather.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateAlarmReceiver extends BroadcastReceiver {

	/*
	 * start the service when receive the broadcast; 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startServiceIntent = new Intent(context, AutoUpdateService.class);
		context.startService(startServiceIntent);
	}
}
