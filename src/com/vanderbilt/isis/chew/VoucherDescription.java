package com.vanderbilt.isis.chew;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VoucherDescription extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
