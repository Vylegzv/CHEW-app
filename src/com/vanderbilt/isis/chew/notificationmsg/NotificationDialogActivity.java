package com.vanderbilt.isis.chew.notificationmsg;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class NotificationDialogActivity extends Activity {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationDialogActivity.class);
	
	public final static String TAG = "NotificationDialogActivity"; 
	long notificationID;
	String fullText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		
		logger.trace("onCreate()");
	    Intent intent = getIntent();
	    
	    notificationID = intent.getLongExtra("notificationID", -1);
        fullText = intent.getStringExtra("fullText");
        
        setContentView(R.layout.notification_dialog);
        
        int icon = R.drawable.ic_launcher;
        
        new AlertDialog.Builder(this)
    	.setMessage(fullText)
    	.setTitle("CHILDREN EATING WELL")
    	.setIcon(icon)
    	.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				logger.trace("onCreate().AlertDialog.Builder.setNegativeButton().onclick()");
				finish();
			}
		})
		.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Log.d(TAG, "starting Continue onClick()");
				logger.trace("onCreate().AlertDialog.Builder.setPositiveButton().onclick()");
				logger.debug("starting Continue onClick()");
				
				if(notificationID != -1) {
					Log.d(TAG, "Querying ContentProvider");
					logger.debug("Querying ContentProvider");
					ContentResolver cr = getContentResolver();
					String [] columnNames = {ChewAppLibrary.NOTIFICATION_ACTION};
					
					String where = ChewAppLibrary.NOTIFICATION_ID + " =?";
					String [] selectionArgs = {String.valueOf(notificationID)};
					Cursor cursor = cr.query(ChewAppLibrary.CONTENT_URI, columnNames, where, selectionArgs, null);
					
					if(cursor == null) {
						Log.e(TAG, "ERROR: CURSOR IS NULL");
						logger.error("ERROR: CURSOR IS NULL");
						finish();   //Cannot do anything with a null (empty) cursor
					}
					
					if(cursor.moveToFirst()) {
						String action = cursor.getString(0) ; 
						if(action.equals("NoAction")) {
							Log.d(TAG, "Action is NOACTION");
							logger.debug("Action is NOACTION");
							finish();
							return;
						}
                        
						
						String packageName = "com.vanderbilt.isis.chew";
						String className = action;
						Class actionClass;
						try {
							Log.e(TAG, "Trying to start the respective Activity " + className);
							logger.debug("Trying to start the respective Activity {}", className);
							actionClass = Class.forName(packageName + "." + className);
							Intent activityIntent = new Intent(getApplicationContext(), actionClass);
							activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
							startActivity(activityIntent);
						} catch (ClassNotFoundException e) {
							
							Log.e(TAG, "ERROR: EXCEPTION THROWN");
							logger.error("ERROR: EXCEPTION THROWN");
							e.printStackTrace();
						}
						
					}
					else {
						Log.e(TAG, "ERROR: CURSOR IS EMPTY");
						logger.error("ERROR: CURSOR IS EMPTY");
					}
					if(cursor != null) {
					    cursor.close();
				    }
				}
				else {
				    Log.e(TAG, "ERROR: NOTIFICATION_ID is equal to -1");
				    logger.error("ERROR: NOTIFICATION_ID is equal to -1");
				}
			}
		})
		.setOnCancelListener(new DialogInterface.OnCancelListener(){
			
			@Override
			public void onCancel(DialogInterface dialog) {
				logger.trace("onCreate().alertDialog.Builder.setOnCancelListener().onCancel()");
				finish();
			}
		}).show();
        
	}
	
}
