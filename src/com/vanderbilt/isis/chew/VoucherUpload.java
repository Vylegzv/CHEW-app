package com.vanderbilt.isis.chew;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.recipes.Ingredient;
import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class VoucherUpload extends Activity{

	String TAG = getClass().getSimpleName();
	
	ListView lv;
	ArrayAdapter<String> adapter;
	EditText nameET;
	EditText monthET;
	EditText ethnicityET;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_upload);
       
        initialize();
	}
	
	private void initialize(){
		
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
        monthET = (EditText) header.findViewById(R.id.month);
        ethnicityET = (EditText) header.findViewById(R.id.ethnicity);
	}
	
	public void upload(View v) {
		
		hideKeyboard();
		
		Log.d(TAG, "upload called");
		String name = nameET.getText().toString();
		String month = monthET.getText().toString();
		String ethnicity = ethnicityET.getText().toString();
		
        SparseBooleanArray checked = lv.getCheckedItemPositions();
        ArrayList<String> selectedVouchers = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            
        	// - 1 because checked starts at 1 nut adapter at 0
            int position = checked.keyAt(i) - 1;
            if (checked.valueAt(i)){
            	Log.d(TAG, adapter.getItem(position));
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

		//getContentResolver().bulkInsert(ChewContract.ShoppingItems.CONTENT_URI, cvs);
		new InsertTask().execute(cvs);
	}
	
	private void hideKeyboard() {
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(lv.getWindowToken(), 0);
	}
	
	private class InsertTask extends AsyncTask<ContentValues[], Void, Integer>{

		@Override
		protected Integer doInBackground(ContentValues[]... params) {
			
			return getContentResolver().bulkInsert(ChewContract.FamilyVouchers.CONTENT_URI, params[0]);
		}
		
		protected void onPostExecute(final Integer numInserted) {
			
			if(numInserted > 0){
			Toast.makeText(getApplicationContext(), "Vouchers got uploaded.",
					   Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "There was a problem",
						   Toast.LENGTH_SHORT).show();
			}
			
			nameET.setText("");
			monthET.setText("");
			ethnicityET.setText("");        
	        lv.clearChoices();
			
		}
	}
}
