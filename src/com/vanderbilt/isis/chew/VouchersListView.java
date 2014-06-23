package com.vanderbilt.isis.chew;

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

	private SimpleCursorAdapter dataAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;
	String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle getName = getIntent().getExtras();
		if (getName != null) {
			name = getName.getString("name");
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

				String voucher = c.getString(1);
				String used = c.getString(2);
				Log.d("Voucher", voucher);
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
		dataAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		dataAdapter.changeCursor(null);
	}
}
