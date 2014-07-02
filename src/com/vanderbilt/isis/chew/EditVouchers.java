package com.vanderbilt.isis.chew;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.utils.Utils;

public class EditVouchers extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter dataAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadermanager = getLoaderManager();

		int[] uiBindTo = { R.id.voucherCode, R.id.name, R.id.month, R.id.ethnicity };
		dataAdapter = new SimpleCursorAdapter(EditVouchers.this,
				R.layout.edit_vouchers_row, null, new String[] {
						ChewContract.FamilyVouchers.VOUCHER_CODE,
						ChewContract.FamilyVouchers.NAME,
						ChewContract.FamilyVouchers.VOUCHER_MONTH,
						ChewContract.FamilyVouchers.ETHNICITY }, uiBindTo, 0);

		setListAdapter(dataAdapter);
		loadermanager.initLoader(1, null, this);

		ListView listview = getListView();
		listview.setDivider(getResources().getDrawable(R.color.background));
		listview.setDividerHeight(20);

//		listview.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				final Cursor c = ((SimpleCursorAdapter) parent.getAdapter())
//						.getCursor();
//				c.moveToPosition(position);
//				Log.d("CLICK", c.getString(1) + " clicked");
//
//				String voucher = c.getString(1);
//				String used = c.getString(2);
//				Log.d("Voucher", voucher);
//				Intent intent = new Intent(EditVouchers.this,
//						VoucherDescription.class);
//				startActivity(intent);
//
//			}
//		});
	}
	
	public void delete(View view) {
		//Log.d("DELETE", "called");
		final int position = getListView().getPositionForView(view);
		Log.d("DELETE", position+"");
		
		RelativeLayout parentRow = (RelativeLayout) view.getParent();
		
		LinearLayout ll1 = (LinearLayout) parentRow.getChildAt(0);
		final TextView vCodeTV = (TextView) ll1.getChildAt(1);
		
		LinearLayout ll2 = (LinearLayout) parentRow.getChildAt(1);
		final TextView nameTV = (TextView) ll2.getChildAt(1);
		
		LinearLayout ll3 = (LinearLayout) parentRow.getChildAt(2);
		final TextView monthTV = (TextView) ll3.getChildAt(1);
		
		LinearLayout ll4 = (LinearLayout) parentRow.getChildAt(3);
		final TextView ethnicityTV = (TextView) ll4.getChildAt(1);
		
		Log.d("Edit", vCodeTV.getText().toString());
		Log.d("Edit", nameTV.getText().toString());
		Log.d("Edit", monthTV.getText().toString());
		Log.d("Edit", ethnicityTV.getText().toString());
		
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				EditVouchers.this);
		alertDialogBuilder.setTitle(getString(R.string.sure_want_delete));
		
		alertDialogBuilder
				// set dialog message
				.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						String where = ChewContract.FamilyVouchers.VOUCHER_CODE
								+ "='"
								+ vCodeTV.getText().toString()
								+ "'" + " AND "
								+ ChewContract.FamilyVouchers.NAME
								+ "='" + nameTV.getText().toString() + "'" + " AND "
								+ ChewContract.FamilyVouchers.VOUCHER_MONTH
								+ "='" + monthTV.getText().toString() + "'" + " AND "
								+ ChewContract.FamilyVouchers.ETHNICITY
								+ "='" + ethnicityTV.getText().toString() + "'";
						
						getContentResolver().delete(
								ChewContract.FamilyVouchers.CONTENT_URI,
								where, null);
						
						loadermanager
						.restartLoader(
								1,
								null,
								EditVouchers.this);
					}
				})
				.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
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

		String[] projection = new String[] { ChewContract.FamilyVouchers._ID,
				ChewContract.FamilyVouchers.VOUCHER_CODE,
				ChewContract.FamilyVouchers.NAME,
				ChewContract.FamilyVouchers.VOUCHER_MONTH,
				ChewContract.FamilyVouchers.ETHNICITY };

//		String where = ChewContract.FamilyVouchers.NAME + "='" + name + "'"
//				+ " AND " + ChewContract.FamilyVouchers.VOUCHER_MONTH + "='"
//				+ Utils.getMonth() + "'";
		
		String where = null;

		CursorLoader loader = new CursorLoader(EditVouchers.this,
				ChewContract.FamilyVouchers.CONTENT_URI, projection, where,
				null, null);
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
