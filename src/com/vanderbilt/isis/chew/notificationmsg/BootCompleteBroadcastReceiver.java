package com.vanderbilt.isis.chew.notificationmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


public class BootCompleteBroadcastReceiver extends BroadcastReceiver {
	
	private static final Logger logger = LoggerFactory.getLogger(BootCompleteBroadcastReceiver.class);

	private final static String TAG = "BootCompleteBroadcastReceiver";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		logger.trace("onReceive()");
		Log.d(TAG, "Received BOOT_COMPLETE intent");
		logger.debug("Received BOOT_COMPLETE intent");
		Intent service = new Intent(context, SetAlarmService.class);
        service.putExtra("calledOn", "BOOT_COMPLETE");
        context.startService(service);
	}

}
