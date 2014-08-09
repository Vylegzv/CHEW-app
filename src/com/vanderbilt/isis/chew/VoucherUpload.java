package com.vanderbilt.isis.chew;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.notificationmsg.ConfigurationActivity;
import com.vanderbilt.isis.chew.utils.Utils;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class VoucherUpload extends Activity{

	private static final Logger logger = LoggerFactory.getLogger(VoucherUpload.class);
	
	String TAG = getClass().getSimpleName();
	
	ListView lv;
	ArrayAdapter<String> adapter;
	EditText nameET;
	String month;
	String ethnicity;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.trace("onCreate()");
        logger.info("Opened Upload Vouchers");
        setContentView(R.layout.voucher_upload);
       
        initialize();
	}
	
	private void initialize(){
		logger.trace("initialize()");
        lv = (ListView) findViewById(R.id.voucherCodesLV);
        View header = getLayoutInflater().inflate(R.layout.voucher_upload_header, null);
        View footer = getLayoutInflater().inflate(R.layout.voucher_upload_footer, null);
		lv = (ListView) findViewById(R.id.voucherCodesLV);
		lv.addHeaderView(header);
		lv.addFooterView(footer);
		
        String[] voucherCodes = getResources().getStringArray(R.array.vouchercodes_array);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, voucherCodes);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);
       
        nameET = (EditText) header.findViewById(R.id.name);
        
        Spinner months = (Spinner) header.findViewById(R.id.month_spinner);
		ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(VoucherUpload.this,
				android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.months));
		monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		months.setAdapter(monthsAdapter);
		
        Spinner ethnicities = (Spinner) header.findViewById(R.id.ethnicity_spinner);
		ArrayAdapter<String> ethnicitiesAdapter = new ArrayAdapter<String>(VoucherUpload.this,
				android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ethnicities));
		ethnicitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ethnicities.setAdapter(ethnicitiesAdapter);
		
		months.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				month = parent.getSelectedItem().toString();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		ethnicities.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				ethnicity = parent.getSelectedItem().toString();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void upload(View v) {
		logger.trace("upload()");
		hideKeyboard();
		
		Log.d(TAG, "upload called");
		logger.debug("upload called");
		String name = nameET.getText().toString();
		
        SparseBooleanArray checked = lv.getCheckedItemPositions();
        ArrayList<String> selectedVouchers = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            
        	// - 1 because checked starts at 1 nut adapter at 0
            int position = checked.keyAt(i) - 1;
            if (checked.valueAt(i)){
            	Log.d(TAG, adapter.getItem(position));
            	logger.debug(" {}", adapter.getItem(position));
            	selectedVouchers.add(adapter.getItem(position));
            }
        }
        
		ContentValues[] cvs = new ContentValues[selectedVouchers.size()];
		for (int i = 0; i < selectedVouchers.size(); i++) {

			ContentValues cv = new ContentValues();
			cv.put(ChewContract.FamilyVouchers.NAME, name);
			cv.put(ChewContract.FamilyVouchers.VOUCHER_CODE, selectedVouchers.get(i).toString());
			cv.put(ChewContract.FamilyVouchers.VOUCHER_MONTH, month);
			cv.put(ChewContract.FamilyVouchers.ETHNICITY, ethnicity);
			cv.put(ChewContract.FamilyVouchers.USED, Utils.NOTUSED);
			cvs[i] = cv;
		}

		new InsertTask().execute(cvs);
	}
	
	private void hideKeyboard() {
		logger.trace("hideKeyboard()");
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(lv.getWindowToken(), 0);
	}
	
	private class InsertTask extends AsyncTask<ContentValues[], Void, Integer>{

		@Override
		protected Integer doInBackground(ContentValues[]... params) {
			logger.trace("InsertTask.doInBackground()");
			return getContentResolver().bulkInsert(ChewContract.FamilyVouchers.CONTENT_URI, params[0]);
		}
		
		protected void onPostExecute(final Integer numInserted) {
			logger.trace("InsertTask.onPostExecute()");
			if(numInserted > 0){
			Toast.makeText(getApplicationContext(), getString(R.string.vouchers_uploaded_msg),
					   Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), getString(R.string.problem),
						   Toast.LENGTH_SHORT).show();
			}
			
			nameET.setText("");        
	        lv.clearChoices();
			
		}
	}
	
	/*******Pankaj Chand's Functions*/
	
	//Options Menu
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		logger.trace("onCreateOptionsMenu()");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			Intent intent = new Intent(VoucherUpload.this, ConfigurationActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
