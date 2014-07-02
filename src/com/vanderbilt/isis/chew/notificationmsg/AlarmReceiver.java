package com.vanderbilt.isis.chew.notificationmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

	private static final Logger logger = LoggerFactory.getLogger(AlarmReceiver.class);
	
	public final static String TAG = "ALARMRECEIVER";  
	public final static String ACTION_SEND_NOTIFICATION = "com.vanderbilt.isis.chew.notificationmsg.ACTION_SEND_NOTIFICATION";
	
	long _id;
	long alarmID;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		logger.trace("onReceive()");		
		_id = intent.getLongExtra("_id", -1);
		alarmID = intent.getLongExtra("alarmID", -1);
		 
		Intent service = new Intent(context, SendNotificationService.class);
		Log.d(TAG, "Received alarm for _id " + Long.toString(_id) +" and alarmID " + Long.toString(alarmID));
		logger.debug(" Received alarm for _id {} and alarmID {}", Long.toString(_id), Long.toString(alarmID));
		service.putExtra("_id", _id);
		service.putExtra("alarmID", alarmID);
		
        context.startService(service);
	}

}
