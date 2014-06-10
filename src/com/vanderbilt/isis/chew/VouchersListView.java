package com.vanderbilt.isis.chew;

import com.vanderbilt.isis.chew.db.ChewContract;

import android.app.AlertDialog;
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

public class VouchersListView extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter dataAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;
	String name;
	String voucherCode;
	AlertDialog.Builder deleteOptionsDialog;
	int deleteQuantity = 0;
	int deleteCount = 0;
	String where = "";
	String productName = "";
	String month_name = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadermanager = getLoaderManager();

		int[] uiBindTo = { R.id.name };
		dataAdapter = new SimpleCursorAdapter(VouchersListView.this,
				R.layout.display_vouchers_row, null,
				new String[] { ChewContract.FamilyVouchers.NAME },
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

				String name = c.getString(1);
				Log.d("NAME", name);
				Intent intent = new Intent(VouchersListView.this, Profile.class);
				intent.putExtra("name", name);
				startActivity(intent);

			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		String[] projection = new String[] {
				ChewContract.FamilyVouchers._ID,
				ChewContract.FamilyVouchers.NAME };

		String where = null;

		CursorLoader loader = new CursorLoader(VouchersListView.this,
				ChewContract.CONTENT_URI_DISTINCT_NAMES, projection,
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
