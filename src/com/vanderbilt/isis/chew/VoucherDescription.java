package com.vanderbilt.isis.chew;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VoucherDescription extends Activity {

	private static final Logger logger = LoggerFactory.getLogger(VoucherDescription.class);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		setContentView(R.layout.voucher_description);

		Bundle getVoucher = getIntent().getExtras();
		if (getVoucher != null) {
			
			String voucher = getVoucher.getString("voucher");
			
			ListView listView1 = (ListView) findViewById(R.id.descriptionLV);

			int arrayID = getResources().getIdentifier(voucher, "array", getPackageName());
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, getResources().getStringArray(arrayID));

			listView1.setAdapter(adapter);
		}
	}
}
