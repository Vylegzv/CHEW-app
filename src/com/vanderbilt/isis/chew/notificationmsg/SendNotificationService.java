package com.vanderbilt.isis.chew.notificationmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class SendNotificationService extends IntentService {
	
	private static final Logger logger = LoggerFactory.getLogger(SendNotificationService.class);
	
    public final static String TAG = "SendNotificationService";
	long _id;
	long alarmID;
	
	
	public SendNotificationService () {
		super("SendNotificationService");
		logger.trace("SendNotificationService ()");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		logger.trace("onHandleIntent()");
		_id = intent.getLongExtra("_id", -1);
		alarmID = intent.getLongExtra("alarmID", -1);
		Log.d(TAG, "Sending Notification for the received alarm for _id " + Long.toString(_id) +" and alarmID " + Long.toString(alarmID));
		logger.debug("Sending Notification for the received alarm for _id {} and alarmID {} ", Long.toString(_id), Long.toString(alarmID));
	
		if(_id == alarmID && _id != -1) {
			ContentResolver cr = getContentResolver();
			
			SharedPreferences preferences = getSharedPreferences("preferences", Context.MODE_MULTI_PROCESS);
			String chosenLanguage = preferences.getString("pref_language", "CHANGE TO ENGLISH");
			
			String[] columnNames;
			if(chosenLanguage.equals("ENGLISH")) {
				columnNames = new String[] {ChewAppLibrary.NOTIFICATION_ID, ChewAppLibrary.NOTIFICATION_ALERTMESSAGE, ChewAppLibrary.NOTIFICATION_FULLTEXT};	
			}
			else if(chosenLanguage.equals("SPANISH")) {
				columnNames = new String[] {ChewAppLibrary.NOTIFICATION_ID, ChewAppLibrary.NOTIFICATION_ESALERTMESSAGE, ChewAppLibrary.NOTIFICATION_ESFULLTEXT};
			}
			else {
				Log.e(TAG, "ERROR: DEFAULT LANGUAGE");
				logger.error("ERROR: DEFAULT LANGUAGE");
				columnNames = new String[] {ChewAppLibrary.NOTIFICATION_ID, ChewAppLibrary.NOTIFICATION_ALERTMESSAGE, ChewAppLibrary.NOTIFICATION_FULLTEXT};
			}
			
			String stop = "FALSE";
			String where = ChewAppLibrary.NOTIFICATION_ALARMID + " =?";
		    String [] selectionArgs = {String.valueOf(alarmID)};
		    Cursor cursor = cr.query(ChewAppLibrary.CONTENT_URI, columnNames, where, selectionArgs, null);
		    
			if(cursor == null) {
				return;   //Cannot do anything with a null (empty) cursor
			}
			
			if(cursor.moveToFirst()) {
				long sID = cursor.getLong(0);
			    String sAlertMessage = cursor.getString(1);
			    String sFullText = cursor.getString(2);
			    
			    sendNewNotification(sID, sAlertMessage, sFullText);
				

				ContentValues values = new ContentValues();

			    values.put(ChewAppLibrary.NOTIFICATION_STOP, "TRUE");
		        cr.update(ChewAppLibrary.CONTENT_URI, values, where, selectionArgs);
			}
			else {
				Log.d(TAG, "CURSOR IS EMPTY");
				logger.debug("CURSOR IS EMPTY");
			}
			
			
			if(cursor != null) {
				cursor.close();
			}
		}
		
	}

	private void sendNewNotification(long notificationID, String alertMessage, String fullText) {
		logger.trace("sendNewNotification()");
		// create the intent for the notification
		Intent notificationIntent = new Intent(this, NotificationDialogActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		notificationIntent.putExtra("notificationID", notificationID);
		notificationIntent.putExtra("fullText", fullText);
		
        // create the pending intent
        PendingIntent pendingIntent = 
                PendingIntent.getActivity(this, (int)notificationID+100, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT/*PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT*/);
        
        // create the variables for the notification
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = alertMessage;
        CharSequence contentTitle = getText(R.string.app_name);
        CharSequence contentText = alertMessage; 
        //contentText should be the actual notification message, but it is too long for the min API Level 11
        
        // create the notification and set its data
        Notification notification = 
                new NotificationCompat.Builder(this)
            .setSmallIcon(icon)
            .setTicker(tickerText)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .build();
        
        // display the notification
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify((int)System.currentTimeMillis(), notification);
    }

	
	@Override
	public void onCreate() {
	    super.onCreate();
	    logger.trace("onCreate");
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	}
	
}
