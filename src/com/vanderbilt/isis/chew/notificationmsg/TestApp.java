package com.vanderbilt.isis.chew.notificationmsg;

import com.vanderbilt.isis.chew.R;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class TestApp extends Application {

	public String chosenLanguage = null;
    public String chosenTime = null;
    
	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("APPLICATION OBJECt", "APPLICATION STARTING");
		
		//setting the default values from the preferences.xml file in the res/xml folder
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);	
		
		Intent service = new Intent(this/*context*/, SetAlarmService.class);
		service.putExtra("calledOn", "APPLICATION_OBJECT");
        this/*context*/.startService(service);
	}
}
