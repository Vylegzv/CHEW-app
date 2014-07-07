package com.vanderbilt.isis.chew;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.utils.Utils;

public class VouchersListView extends ListActivity implements
LoaderManager.LoaderCallbacks<Cursor> {

	private static final Logger logger = LoggerFactory.getLogger(VouchersListView.class);
	
	private SimpleCursorAdapter dataAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;
	String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		

		Bundle getName = getIntent().getExtras();
		if (getName != null) {
			name = getName.getString("name");
			logger.info("Opening the VouchersListView to see the Vouchers for Family Member - {}", name);
		}

		loadermanager = getLoaderManager();

		int[] uiBindTo = { R.id.voucher, R.id.used };
		dataAdapter = new SimpleCursorAdapter(VouchersListView.this,
				R.layout.display_vouchers_row, null,
				new String[] { ChewContract.FamilyVouchers.VOUCHER_CODE, ChewContract.FamilyVouchers.USED },
				uiBindTo, 0);

		setListAdapter(dataAdapter);
		loadermanager.initLoader(1, null, this);

		ListView listview = getListView();

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final Cursor c = ((SimpleCursorAdapter) parent.getAdapter())
						.getCursor();
				c.moveToPosition(position);
				Log.d("CLICK", c.getString(1) + " clicked");
				logger.debug("CLICK {} clicked", c.getString(1));
				
				String voucher = c.getString(1);
				String used = c.getString(2);
				Log.d("Voucher", voucher);
				logger.debug("Voucher {} ", voucher);
				logger.info("Clicked on Voucher {} with status {} to see the items available to Family Member - {}", voucher, used, name);
				Intent intent = new Intent(VouchersListView.this, VoucherDescription.class);
				intent.putExtra("voucher", voucher);
				intent.putExtra("used", used);
				intent.putExtra("name", name);
				startActivity(intent);

			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		logger.trace("onCreateLoader()");
		String[] projection = new String[] {
				ChewContract.FamilyVouchers._ID,
				ChewContract.FamilyVouchers.VOUCHER_CODE,
				ChewContract.FamilyVouchers.USED };

		String where = ChewContract.FamilyVouchers.NAME + "='" + name + "'" + " AND "
				+ ChewContract.FamilyVouchers.VOUCHER_MONTH
				+ "='" + Utils.getMonth() + "'";

		CursorLoader loader = new CursorLoader(VouchersListView.this,
				ChewContract.FamilyVouchers.CONTENT_URI, projection,
				where, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		logger.trace("onLoadFinished()");
		dataAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		logger.trace("onLoaderReset()");
		dataAdapter.changeCursor(null);
	}
}
