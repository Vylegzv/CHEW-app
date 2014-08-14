package com.vanderbilt.isis.chew;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.VoucherStatus;

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

		dataAdapter = new MySimpleCursorAdapter(VouchersListView.this,
				R.layout.display_vouchers_row, null,
				new String[] { ChewContract.FamilyVouchers.VOUCHER_CODE, ChewContract.FamilyVouchers.USED },
				null, 0);

		setListAdapter(dataAdapter);
		loadermanager.initLoader(1, null, this);

		ListView listview = getListView();
		listview.setDivider(getResources().getDrawable(R.color.background));
		listview.setDividerHeight(20);

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
				+ "='" + Utils.getMonth().getMonthNum() + "'";

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
	
	private class MySimpleCursorAdapter extends SimpleCursorAdapter {

		public MySimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, 0);
			logger.trace("MySimpleCursorAdapter.MySimpleCursorAdapter()");
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			logger.trace("MySimpleCursorAdapter.newView()");
			Log.d("NEWVIEW", "called");
			logger.debug("NEWVIEW {}", "called");
			ViewHolder holder = new ViewHolder();
			View v = null;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.display_vouchers_row, null);
			holder.textViewVCode = (TextView) v.findViewById(R.id.voucher);
			holder.textViewVStatus = (TextView) v
					.findViewById(R.id.used);

			v.setTag(holder);
			return v;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			logger.trace("MySimpleCursorAdapter.bindView()");
			Log.d("BINDVIEW", "called");
			logger.debug("BINDVIEW {}", "called");

			ViewHolder holder = (ViewHolder) view.getTag();

			holder.textViewVCode.setText(cursor.getString(cursor
					.getColumnIndex(ChewContract.FamilyVouchers.VOUCHER_CODE)));
			int value = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(ChewContract.FamilyVouchers.USED)));
			holder.textViewVStatus.setText(VoucherStatus.getVoucherStatus(value).toString(VouchersListView.this));

		}
	}

	public static class ViewHolder {
		public TextView textViewVCode;
		public TextView textViewVStatus;
	}
}
