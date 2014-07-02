package com.vanderbilt.isis.chew.notificationmsg;

import java.util.Calendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SetAlarmService extends IntentService {

	private static final Logger logger = LoggerFactory.getLogger(SetAlarmService.class);
	
	public static final String TAG = "SETALARMSERVICE"; 
			
	AlarmManager alarmManager;

	public SetAlarmService () {
		super("SetAlarmService");
		logger.trace("SetAlarmService()");
	}

	@Override
	public void onCreate() {
	    super.onCreate();
	    logger.trace("onCreate()");
	    alarmManager = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
	    
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		logger.trace("onHandleIntent()");
		String calledOn = intent.getStringExtra("calledOn");
		if(calledOn.equals("BOOT_COMPLETE")) {
			Log.d(TAG, "SetAlarmService called after device is re-booted");
			logger.debug("SetAlarmService called after device is re-booted");
			ContentResolver cr = getContentResolver();
	        ContentValues values = new ContentValues();

		    values.put(ChewAppLibrary.NOTIFICATION_ALARMID, -1);
	        int updatedRows = 0;
	        String stop = "FALSE";
			String where = ChewAppLibrary.NOTIFICATION_STOP + "= ?";
		    String [] selectionArgs = {stop};
	        updatedRows = cr.update(ChewAppLibrary.CONTENT_URI, values, where, selectionArgs);
	        Log.d(TAG, "UPDATED ROWS = " + updatedRows);
	        logger.debug("UPDATED ROWS = {}", updatedRows);
		}
		else if(calledOn.equals("CHANGED_CHOSEN_TIME")) {

			String [] columnNames = {ChewAppLibrary.NOTIFICATION_ID, ChewAppLibrary.NOTIFICATION_ALARMID, ChewAppLibrary.NOTIFICATION_STOP, ChewAppLibrary.NOTIFICATION_ACTION};

		    ContentResolver cr = getContentResolver();
		    Cursor cursor = cr.query(ChewAppLibrary.CONTENT_URI, columnNames, null, null, null);
		    
		    if(cursor.moveToFirst()) {
		    	do {
			    	int alarmID = (int)cursor.getLong(1);
			    	
			    	if(alarmID != -1 && (cursor.getString(2)).equals("FALSE")) {
			    		
					    String ALARM_ACTION = AlarmReceiver.ACTION_SEND_NOTIFICATION;
					  	PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmID, new Intent(ALARM_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
						pendingIntent.cancel();
						alarmManager.cancel(pendingIntent);
			    		Log.d(TAG, "Canceling Alarm for Notification " + cursor.getLong(0));
			    		logger.debug("Canceling Alarm for Notification {}", cursor.getLong(0));
			    	}
			    }while (cursor.moveToNext());
		    }
		    
		    ContentValues values = new ContentValues();

		    values.put(ChewAppLibrary.NOTIFICATION_ALARMID, -1);
	        int updatedRows = 0;
	        String stop = "FALSE";
	        //To not reset alarms which have already fired
			String where = ChewAppLibrary.NOTIFICATION_STOP + "=?";
		    String [] selectionArgs = {stop};
	        updatedRows = cr.update(ChewAppLibrary.CONTENT_URI, values, where, selectionArgs);
	        Log.d(TAG, "UPDATED ROWS = " + updatedRows);
	        logger.debug("UPDATED ROWS = {}", updatedRows);
	        if(cursor != null) {
			    cursor.close();
		    }
	        
		}
		
		
		//START ALARMS
		{
			//variables whose values will be got from the DB in the While loop
			long _id = -1;
			long scheduledAlarmTime = -1; //Will need this to store the calculated time for setting the alarm
			long alarmID = -1;
			
			int dbWeek = -1;
			int dbDay = -1;
			
			String stop = "FALSE";
			
			String ALARM_ACTION = AlarmReceiver.ACTION_SEND_NOTIFICATION;
			
								    
		    String [] columnNames = {ChewAppLibrary.NOTIFICATION_ID, ChewAppLibrary.NOTIFICATION_TIME,
		                             ChewAppLibrary.NOTIFICATION_ALARMID, ChewAppLibrary.NOTIFICATION_STOP,
		                             ChewAppLibrary.NOTIFICATION_WEEK, ChewAppLibrary.NOTIFICATION_DAY};
		    ContentResolver cr = getContentResolver();
		    Cursor cursor = cr.query(ChewAppLibrary.CONTENT_URI, columnNames, null, null, null);
		    
		    int columnForAlarmID = 2;
            
		    if(cursor.moveToFirst()) {
		    	do {
			    	if(cursor.getLong(columnForAlarmID) != -1) {
			    		Log.d(TAG, "Alarm for Notification " + cursor.getLong(0) + " already set, Not setting again.");
			    		logger.debug("Alarm for Notification {} already set, Not setting again.", cursor.getLong(0));
			    		continue;
			    	}
			    	
			    	Intent intentAlarm = new Intent(ALARM_ACTION);
			    	
				    for(int columnIndex = 0; columnIndex < 6; columnIndex++) {
				    	
				    	switch(columnIndex) {
				    	    case 0:
				    	    	_id = cursor.getLong(columnIndex);
				    	    	intentAlarm.putExtra("_id", cursor.getLong(columnIndex));
				    		    break;
				    	    case 1:
				    	    	scheduledAlarmTime = cursor.getLong(columnIndex);
					    		break;
				    	    case 2:
				    	    	alarmID = cursor.getLong(columnIndex);
				    	    	intentAlarm.putExtra("alarmID", _id);
					    		break;
				    	    case 3:
				    	    	stop = cursor.getString(columnIndex);
					    		break;
					    	case 4:
					    		dbWeek = (int)cursor.getLong(columnIndex);
					    		break;
					    	case 5:
					    		dbDay = (int)cursor.getLong(columnIndex);
					    		break;
					    	default:
					    		Log.e(TAG, "Error: Gone past the number of columns in the cursor");
					    		logger.error("Error: Gone past the number of columns in the cursor");
				    	}
				    }
				    
				  //For getting the Preferences - Language and Notification Time
			    	SharedPreferences preferences = getSharedPreferences("preferences", Context.MODE_MULTI_PROCESS);
			    	String chosenTime = preferences.getString("pref_time", "CHANGE TO 10");
			    	int targetWeek = preferences.getInt("pref_targetWeek", 0);
			    	
				    Calendar cTargetDay = Calendar.getInstance(Locale.US);
				    cTargetDay.setTimeInMillis(System.currentTimeMillis());
				    cTargetDay.set(Calendar.DAY_OF_WEEK, dbDay);
				    cTargetDay.set(Calendar.WEEK_OF_YEAR, targetWeek + (dbWeek - 1));
				    Log.d(TAG, "SUPER ATTENTION, DBWEEK IS : " + dbWeek);
				    logger.debug("SUPER ATTENTION, DBWEEK IS :  {}", dbWeek);
			        cTargetDay.set(Calendar.HOUR_OF_DAY,(int) Long.parseLong(chosenTime));
			        cTargetDay.set(Calendar.MINUTE,0);
			        cTargetDay.set(Calendar.SECOND,0);
				    			    
			        
			        scheduledAlarmTime = cTargetDay.getTimeInMillis();
				    
					PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)_id, intentAlarm, 0);
					
	                 
					if(stop.equals("FALSE")) {
						//Alarm is not shown yet, and not stopped.
						alarmManager.set(AlarmManager.RTC_WAKEUP, scheduledAlarmTime , pendingIntent);
	            		Log.d(TAG, "IMPORTANT WORKING SET ALARM " + _id + " for milliseconds " + scheduledAlarmTime );
	            		logger.debug("IMPORTANT WORKING SET ALARM {} for milliseconds {}", _id, scheduledAlarmTime);
	                    ContentValues values = new ContentValues();
					     //Put the updated value into ContentValues object for each column name
					     values.put(ChewAppLibrary.NOTIFICATION_ALARMID, _id);
				         int updatedRows = -1;
						 String where = ChewAppLibrary.NOTIFICATION_ID + " =?";
					     String [] selectionArgs = {String.valueOf(_id)};

				         updatedRows = cr.update(ChewAppLibrary.CONTENT_URI, values, where, selectionArgs);
				         Log.d(TAG, "UPDATED ROWS = " + updatedRows);
				         logger.debug("UPDATED ROWS = {}", updatedRows);
				         
			            Log.d(TAG, "Set Alarm with alarmTime " + (System.currentTimeMillis() + scheduledAlarmTime) + " and alarmID " + _id);
			            Log.d(TAG, "SUPER ATTENTION: ALARM SET FOR " + cTargetDay.toString());
			            logger.debug("Set Alarm with alarmTime {} and alarmID {}", (System.currentTimeMillis() + scheduledAlarmTime), _id);
			            logger.debug("SUPER ATTENTION: ALARM SET FOR {}", cTargetDay.toString());
					}
					else {
						Log.d(TAG, "Did NOT set Alarm with alarmTime " + scheduledAlarmTime + " and alarmID " + alarmID);
						logger.debug("Did NOT set Alarm with alarmTime {} and alarmID {}", scheduledAlarmTime, alarmID);
					}
			    } while (cursor.moveToNext());//end of DO-WHILE
	
		    }  
            		    	
		    Log.d(TAG, "Finished setting alarms for all entries in database");
		    logger.debug("Finished setting alarms for all entries in database");
		    if(cursor != null) {
			    cursor.close();
		    }

		}
	} 

}
