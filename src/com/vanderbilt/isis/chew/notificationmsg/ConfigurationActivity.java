package com.vanderbilt.isis.chew.notificationmsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ConfigurationActivity extends Activity {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationActivity.class);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit(); 
		
	}
	
	
}
