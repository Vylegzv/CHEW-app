package com.vanderbilt.isis.chew.notificationmsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class NotificationHistoryActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener {
	//IMPORTANT
	//May require to extends FragmentActivity, import android.v4.support for everything, and use 
	//getSupportLoaderManager().initLoader(ChewAppLibrary.NOTIFICATION_LOADER, null, this);
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationHistoryActivity.class);
	
	public static final String TAG = "NOTIFICATION_HISTORY";
	
	private ListView notificationListView;


	SimpleCursorAdapter mAdapter;
	

	Cursor cursor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		setContentView(R.layout.notification_history);
		Log.d(TAG, "oncreate()");
		logger.debug("oncreate()");
		notificationListView = (ListView) findViewById(R.id.notification_listView1);

		notificationListView.setOnItemClickListener(this);

		String[] from = {ChewAppLibrary.NOTIFICATION_ID, "notification_text"};

	    int []to = new int [] {R.id.textView101, R.id.textView102};
        mAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.listview_notification, 
        		null, from, to, 0);
		
		
        
		getLoaderManager().initLoader(ChewAppLibrary.NOTIFICATION_LOADER, null, this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		logger.trace("onResume()");
		getLoaderManager().restartLoader(ChewAppLibrary.NOTIFICATION_LOADER, null, this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		logger.trace("onStart()");
		getLoaderManager().restartLoader(ChewAppLibrary.NOTIFICATION_LOADER, null, this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		logger.trace("onItemClick()");
        if(cursor == null) {
			Log.d(TAG, "ERROR: Cursor is empty");
			logger.debug("ERROR: Cursor is empty");
			return;   //Cannot do anything with a null (empty) cursor
		}
		
		if(cursor.moveToFirst()) {
			int counter = 1;
			do {
				Log.d(TAG, "position is " + position + " and _id is " + (int)cursor.getLong(0));
				logger.debug("position is {} and _id is {}", position, (int)cursor.getLong(0));
				if(position+1 == counter) {
					Log.d(TAG, "Inside IF BLOCK");
					logger.debug("Inside IF BLOCK");
					
				    String action = cursor.getString(2);
				    
					String packageName = "com.vanderbilt.isis.chew";
					String className = action;
					Class actionClass;
					try {
						Log.d(TAG, "Trying to start the respective Activity " + className);
						logger.debug("Trying to start the respective Activity {}", className);
						actionClass = Class.forName(packageName + "." + className);
						Intent activityIntent = new Intent(getApplicationContext(), actionClass);
						activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
						startActivity(activityIntent);
					} catch (ClassNotFoundException e) {
						
						Log.e(TAG, "ERROR:EXCEPTION THROWN");
						logger.error("ERROR: ClassNotFoundException THROWN for {}", className);
						e.printStackTrace();
					}
					
				}	
				counter++;	
			}while (cursor.moveToNext());
		       	
		   
		}
	}

	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		logger.trace("onCreateLoader()");
		SharedPreferences preferences = getSharedPreferences("preferences", Context.MODE_MULTI_PROCESS);
		String chosenLanguage = preferences.getString("pref_language", "CHANGE TO ENGLISH");
		
		
		String [] projection;
		
		if(chosenLanguage.equals("ENGLISH")) {
			Log.d(TAG, "Language ENGLISH");
			logger.debug("Language ENGLISH");
			projection = new String[] {ChewAppLibrary.NOTIFICATION_ID,
					ChewAppLibrary.NOTIFICATION_FULLTEXT + " as 'notification_text'", ChewAppLibrary.NOTIFICATION_ACTION};
		}
		
		else if(chosenLanguage.equals("SPANISH")) {
			Log.d(TAG, "Language SPANISH");
			logger.debug("Language SPANISH");
			projection = new String[] {ChewAppLibrary.NOTIFICATION_ID,
					ChewAppLibrary.NOTIFICATION_ESFULLTEXT + " as 'notification_text'", ChewAppLibrary.NOTIFICATION_ACTION};
		}
		else {
			Log.e(TAG, "ERROR: Language DEFAULT");
			logger.error("ERROR: Language DEFAULT");
			projection = new String[] {ChewAppLibrary.NOTIFICATION_ID, 
					ChewAppLibrary.NOTIFICATION_FULLTEXT + " as 'notification_text'", ChewAppLibrary.NOTIFICATION_ACTION};
		}

		String stop = "TRUE";
		String where = ChewAppLibrary.NOTIFICATION_STOP + "=?";
	    String [] selectionArgs = {stop};
		
		CursorLoader cLoader = new CursorLoader(this, ChewAppLibrary.CONTENT_URI, projection, where, selectionArgs, null);
		
		cursor = cLoader.loadInBackground();
		if(cursor != null) {
    		Log.d(TAG, "Swapping cursor");
    		logger.debug("Swapping cursor");
	        mAdapter.swapCursor(cursor);
    	}
		else if(cursor == null) {
			Log.d(TAG, "cursor is null");
			logger.debug("cursor is null");
		}
    	 
    	notificationListView.setAdapter(mAdapter);
		
		if(cLoader == null) {
			Log.d(TAG, "cLoader is null");
			logger.debug("cLoader is null");
		}
		return cLoader;
		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		logger.trace("onLoadFinished()");
		Log.d(TAG, "in onLoadFinished()");
		logger.debug("in onLoadFinished()");
    	if(data != null) {
    		Log.d(TAG, "Swapping cursor");
    		logger.debug("Swapping cursor");
	        mAdapter.swapCursor(data);
    	}
    	if(data == null) {
			Log.d(TAG, "data is null");
			logger.debug("data is null");
		}
    	
    	notificationListView.setAdapter(mAdapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		logger.trace("onLoaderReset()");
		notificationListView.setAdapter(null);
		Log.d(TAG, "in onLoaderReset()");
		logger.debug("in onLoaderReset()");
	}

	
	//Options Menu
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		logger.trace("onCreateOptionsMenu()");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		logger.trace("onOptionsItemSelected()");
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == R.id.action_notification) {
			Intent intent = new Intent(NotificationHistoryActivity.this, ConfigurationActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
