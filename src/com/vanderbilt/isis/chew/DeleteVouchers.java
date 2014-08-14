package com.vanderbilt.isis.chew;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.vanderbilt.isis.chew.InCartRegular.ViewHolder;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.notificationmsg.ConfigurationActivity;
import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.Month;
import com.vanderbilt.isis.chew.vouchers.VoucherStatus;

public class DeleteVouchers extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final Logger logger = LoggerFactory
			.getLogger(DeleteVouchers.class);

	private SimpleCursorAdapter dataAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		logger.info("Opened the Edit Vouchers Page");
		loadermanager = getLoaderManager();

		dataAdapter = new MySimpleCursorAdapter(DeleteVouchers.this,
				R.layout.edit_vouchers_listview, null, new String[] {
						ChewContract.FamilyVouchers.VOUCHER_CODE,
						ChewContract.FamilyVouchers.USED,
						ChewContract.FamilyVouchers.NAME,
						ChewContract.FamilyVouchers.VOUCHER_MONTH,
						ChewContract.FamilyVouchers.ETHNICITY }, null, 0);

		setListAdapter(dataAdapter);
		loadermanager.initLoader(1, null, this);

		ListView listview = getListView();
		listview.setDivider(getResources().getDrawable(R.color.background));
		listview.setDividerHeight(20);

	}

	public void delete(View view) {
		// Log.d("DELETE", "called");
		final int position = getListView().getPositionForView(view);
		logger.trace("delete()");
		logger.debug("DELETE {}", position);
		Log.d("DELETE", position + "");

		RelativeLayout parentRow = (RelativeLayout) view.getParent();

		LinearLayout ll1 = (LinearLayout) parentRow.getChildAt(0);
		final TextView vCodeTV = (TextView) ll1.getChildAt(1);

		LinearLayout ll2 = (LinearLayout) parentRow.getChildAt(1);
		final TextView statusTV = (TextView) ll2.getChildAt(1);

		LinearLayout ll3 = (LinearLayout) parentRow.getChildAt(2);
		final TextView nameTV = (TextView) ll3.getChildAt(1);

		LinearLayout ll4 = (LinearLayout) parentRow.getChildAt(3);
		final TextView monthTV = (TextView) ll4.getChildAt(1);

		LinearLayout ll5 = (LinearLayout) parentRow.getChildAt(4);
		final TextView ethnicityTV = (TextView) ll5.getChildAt(1);

		logger.debug("Edit Vouchers {}, {}, {}, {}", vCodeTV.getText()
				.toString(), nameTV.getText().toString(), monthTV.getText()
				.toString(), ethnicityTV.getText().toString());
		Log.d("Edit", vCodeTV.getText().toString());
		Log.d("Edit", statusTV.getText().toString());
		Log.d("Edit", nameTV.getText().toString());
		Log.d("Edit", monthTV.getText().toString());
		Log.d("Edit", ethnicityTV.getText().toString());

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				DeleteVouchers.this);
		alertDialogBuilder.setTitle(getString(R.string.sure_want_delete));

		alertDialogBuilder
		// set dialog message
				.setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								String where = ChewContract.FamilyVouchers.VOUCHER_CODE
										+ "='"
										+ vCodeTV.getText().toString()
										+ "'"
										+ " AND "
										+ ChewContract.FamilyVouchers.USED
										+ "='"
										+ VoucherStatus.getVoucherStatus(
												DeleteVouchers.this, statusTV
														.getText().toString()).getValue()
										+ "'"
										+ " AND "
										+ ChewContract.FamilyVouchers.NAME
										+ "='"
										+ nameTV.getText().toString()
										+ "'"
										+ " AND "
										+ ChewContract.FamilyVouchers.VOUCHER_MONTH
										+ "='"
										+ Month.getMonth(DeleteVouchers.this,
												monthTV.getText().toString())
												.getMonthNum()
										+ "'"
										+ " AND "
										+ ChewContract.FamilyVouchers.ETHNICITY
										+ "='"
										+ ethnicityTV.getText().toString()
										+ "'";

								getContentResolver()
										.delete(ChewContract.FamilyVouchers.CONTENT_URI,
												where, null);

								loadermanager.restartLoader(1, null,
										DeleteVouchers.this);
							}
						}).setNegativeButton(getString(R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		logger.trace("onCreateLoader()");
		String[] projection = new String[] { ChewContract.FamilyVouchers._ID,
				ChewContract.FamilyVouchers.VOUCHER_CODE,
				ChewContract.FamilyVouchers.USED,
				ChewContract.FamilyVouchers.NAME,
				ChewContract.FamilyVouchers.VOUCHER_MONTH,
				ChewContract.FamilyVouchers.ETHNICITY };

		String where = null;

		CursorLoader loader = new CursorLoader(DeleteVouchers.this,
				ChewContract.FamilyVouchers.CONTENT_URI, projection, where,
				null, null);
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

			v = inflater.inflate(R.layout.edit_vouchers_row, null);
			holder.textViewVCode = (TextView) v.findViewById(R.id.voucherCode);
			holder.textViewVStatus = (TextView) v
					.findViewById(R.id.voucher_status);
			holder.textViewName = (TextView) v.findViewById(R.id.name);
			holder.textViewMonth = (TextView) v.findViewById(R.id.month);
			holder.textViewEthnicity = (TextView) v
					.findViewById(R.id.ethnicity);

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
			holder.textViewVStatus.setText(VoucherStatus
					.getVoucherStatus(value).toString(DeleteVouchers.this));
			holder.textViewName.setText(cursor.getString(cursor
					.getColumnIndex(ChewContract.FamilyVouchers.NAME)));
			int monthNum = Integer
					.parseInt(cursor.getString(cursor
							.getColumnIndex(ChewContract.FamilyVouchers.VOUCHER_MONTH)));
			holder.textViewMonth.setText(Month.getMonth(monthNum).toString(
					DeleteVouchers.this));
			holder.textViewEthnicity.setText(cursor.getString(cursor
					.getColumnIndex(ChewContract.FamilyVouchers.ETHNICITY)));
		}
	}

	public static class ViewHolder {
		public TextView textViewVCode;
		public TextView textViewVStatus;
		public TextView textViewName;
		public TextView textViewMonth;
		public TextView textViewEthnicity;
	}

	/******* Pankaj Chand's Functions */

	// Options Menu

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
		} else if (id == R.id.action_notification) {
			Intent intent = new Intent(DeleteVouchers.this,
					ConfigurationActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
