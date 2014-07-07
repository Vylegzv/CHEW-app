package com.vanderbilt.isis.chew.notificationmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.R;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	
	private static final Logger logger = LoggerFactory.getLogger(SettingsFragment.class);
	
	public final static String TAG = "SettingsFragment";
	
	private String conf_Language;
	private String conf_Time;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		//Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		logger.trace("onResume()");
		SharedPreferences preferenceCheck = getActivity().getSharedPreferences("preferences", Context.MODE_MULTI_PROCESS);
		
		//These 2 lines are not required
	    conf_Language = preferenceCheck.getString("pref_language", "CHANGE TO ENGLISH");
		conf_Time = preferenceCheck.getString("pref_time", "CHANGE TO 10");
		
		Log.d(TAG, "PREFERENCE: chosenLanguage was " + conf_Language);
		Log.d(TAG, "PREFERENCE: chosenTime was " + conf_Time);
		logger.debug("PREFERENCE: chosenLanguage was {} and chosenTime was {}", conf_Language, conf_Time);
		// Register this OnSharedPreferenceChangeListener
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		// Unregister this OnSharedPreferenceChangeListener
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		logger.trace("onPause()");
		super.onPause();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,	String key) {
		logger.trace("onSharedPreferenceChanged()");
		if(key.equals("pref_language")) {
			Log.d(TAG, "PREFERENCE: chosenLanguage was " + conf_Language);
			logger.debug("PREFERENCE: chosenLanguage was {}", conf_Language);
			String chosenLanguage = sharedPreferences.getString("pref_language", "CHANGE TO ENGLISH");
			Log.d(TAG, "PREFERENCE: chosenLanguage is now " + chosenLanguage);
			logger.debug("PREFERENCE: chosenLanguage is now {}", chosenLanguage);

			SharedPreferences preferences = getActivity().getSharedPreferences("preferences", Context.MODE_MULTI_PROCESS);
			preferences.edit().putString("pref_language", chosenLanguage).apply();
			
			sharedPreferences.edit().putString("pref_language", chosenLanguage).apply();
			
            Preference connectionPref = findPreference(key);
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
            
		}
		if(key.equals("pref_time")) {
			Log.d(TAG, "PREFERENCE: chosenTime was " + conf_Time);
			logger.debug("PREFERENCE: chosenTime was {}", conf_Time);
			String chosenTime = sharedPreferences.getString("pref_time", "CHANGE TO 10");
			Log.d(TAG, "PREFERENCE: chosenTime is now " + chosenTime);
			logger.debug("PREFERENCE: chosenTime is now {}", chosenTime);
			
			SharedPreferences preferences = getActivity().getSharedPreferences("preferences", Context.MODE_MULTI_PROCESS);
			preferences.edit().putString("pref_time", chosenTime).apply();

			sharedPreferences.edit().putString("pref_time", chosenTime).apply();
			
			Preference connectionPref = findPreference(key);
			if (connectionPref instanceof ListPreference) {
		        ListPreference listPref = (ListPreference) connectionPref;
		        connectionPref.setSummary(listPref.getEntry());
		    }
            
	        //Call setAlarmService to cancel and re-set the alarms based on the new chosenTime
	        Intent setAlarmService = new Intent(getActivity(), SetAlarmService.class);
			//To signal SetAlarmService that device has been booted, and to reset all alarmIDs so as to set alarms again 
	        setAlarmService.putExtra("calledOn", "CHANGED_CHOSEN_TIME");
	        getActivity().startService(setAlarmService);
		}
	} 

}
