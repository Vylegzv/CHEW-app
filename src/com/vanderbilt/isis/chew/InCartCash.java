package com.vanderbilt.isis.chew;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
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
import com.vanderbilt.isis.chew.InCartRegular.ViewHolder;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.utils.Utils;

public class InCartCash extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	public final String TAG = getClass().getSimpleName();

	private SimpleCursorAdapter mAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;
	String name;
	AlertDialog.Builder deleteOptionsDialog;
	String where = "";
	String produceName = "";
	String month_name = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.in_cart_listview);

		name = "";
		Bundle getName = getIntent().getExtras();
		if (getName != null) {
			name = getName.getString("name");
		}

		Log.d("NAME", name);

		int[] uiBindTo = { R.id.producePrice, R.id.produceName };

		mAdapter = new SimpleCursorAdapter(InCartCash.this,
				R.layout.in_cart_cash, null, new String[] {
						ChewContract.ProduceChosen.COST,
						ChewContract.ProduceChosen.PRODUCE_NAME }, uiBindTo, 0);

		setListAdapter(mAdapter);
		
		loadermanager = getLoaderManager();
		loadermanager.initLoader(1, null, this);

		ListView listview = getListView();

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final Cursor c = ((SimpleCursorAdapter) parent.getAdapter())
						.getCursor();
				c.moveToPosition(position);
				Log.d("CLICK", c.getString(0) + " clicked");
				Log.d("CLICK", c.getString(1) + " clicked");
				Log.d("CLICK", c.getString(2) + " clicked");

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						InCartCash.this);

				// set title
				alertDialogBuilder.setTitle("Do You Want To Delete This Item?");

				// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										month_name = Utils.getMonth();

										Log.d("PRICE", c.getString(0));
										Log.d("PRODUCE NAME", c.getString(1));
										
										where = "";
										produceName = c.getString(1);
											where = ChewContract.ProduceChosen.PRODUCE_NAME
													+ "='"
													+ produceName
													+ "'"
													+ " AND "
													+ ChewContract.ProduceChosen.MEMBER_NAME
													+ "='"
													+ name
													+ "'"
													+ " AND "
													// +
													// ChewContract.ProductsChosen.VOUCHER_CODE
													// + "='"
													// + voucherCode
													// + "'"
													// + " AND "
													+ ChewContract.ProduceChosen.MONTH
													+ "='" + month_name + "'";

											int numDeleted = getContentResolver()
													.delete(ChewContract.ProductsChosen.CONTENT_URI,
															where, null);

										// mAdapter.notifyDataSetChanged();
										loadermanager.restartLoader(2, null,
												InCartCash.this);

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		Log.d(TAG, "onCreateLoader called");
		
		String[] projection = new String[] { ChewContract.ProduceChosen._ID,
				ChewContract.ProduceChosen.COST,
				ChewContract.ProduceChosen.PRODUCE_NAME };

		String month_name = Utils.getMonth();
		
		Log.d(TAG, month_name);
		Log.d(TAG, name);

		String where = ChewContract.ProduceChosen.MONTH + "='" + month_name
				+ "'" + " AND " + ChewContract.ProduceChosen.MEMBER_NAME
				+ "='" + name + "'";

		String sortOrder = ChewContract.ProduceChosen.PRODUCE_NAME
				+ " ASC";

		CursorLoader loader = new CursorLoader(InCartCash.this,
				ChewContract.ProduceChosen.CONTENT_URI, projection, where,
				null, sortOrder);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d(TAG, "onLoadFinished called");
		Log.d(TAG, "cursor count: " + cursor.getCount()+"");
		mAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}

	public static class ViewHolder {
		public TextView textViewName;
		public TextView textViewPrice;
	}
}
