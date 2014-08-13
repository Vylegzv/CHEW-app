package com.vanderbilt.isis.chew;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;

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
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.utils.Utils;

public class InCartCash extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final Logger logger = LoggerFactory
			.getLogger(InCartCash.class);

	public final String TAG = getClass().getSimpleName();

	private SimpleCursorAdapter mAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;
	String name;
	AlertDialog.Builder deleteOptionsDialog;
	String where = "";
	String produceName = "";
	String month_name = "";
	String voucherCode;
	Set<String> vCodesInUse;

	// TextView nameTV;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		// logger.info(" {}", );
		// View header = getLayoutInflater().inflate(R.layout.in_cart_header,
		// null);
		// nameTV = (TextView) header.findViewById(R.id.name);

		name = "";
		Bundle getName = getIntent().getExtras();
		if (getName != null) {
			name = getName.getString("name");
			vCodesInUse = Utils.getInUseVoucherCodes(InCartCash.this, name);
			logger.info("Opened Cash Voucher Selections for person {}", name);
		}

		final ListView listview = getListView();
		// listview.addHeaderView(header);

		// int[] uiBindTo = { R.id.producePrice, R.id.produceName,
		// R.id.voucherCode };

		mAdapter = new MySimpleCursorAdapterCash(InCartCash.this,
				R.layout.in_cart_cash, null, new String[] {
						ChewContract.ProduceChosen.COST,
						ChewContract.ProduceChosen.PRODUCE_NAME,
						ChewContract.ProduceChosen.VOUCHER_CODE, }, null, 0);

		setListAdapter(mAdapter);

		loadermanager = getLoaderManager();
		loadermanager.initLoader(1, null, this);

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// logger.info(" {}", );

				final Cursor c = ((SimpleCursorAdapter) parent.getAdapter())
						.getCursor();

				// HeaderViewListAdapter hlva = (HeaderViewListAdapter)
				// listview.getAdapter();
				// SimpleCursorAdapter scAdapter = (SimpleCursorAdapter)
				// hlva.getWrappedAdapter();
				// final Cursor c = scAdapter.getCursor();

				c.moveToPosition(position);
				Log.d("CLICK", c.getString(0) + " clicked");
				Log.d("CLICK", c.getString(1) + " clicked");
				Log.d("CLICK", c.getString(2) + " clicked");
				Log.d("CLICK", c.getString(3) + " clicked");

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						InCartCash.this);

				// set title
				alertDialogBuilder.setTitle(getString(R.string.delete_item));

				// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton(getString(R.string.yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// logger.info(" {}", );
										month_name = Utils.getMonth();
										produceName = c.getString(2);
										voucherCode = c.getString(3);
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
												+ ChewContract.ProductsChosen.VOUCHER_CODE
												+ "='"
												+ voucherCode
												+ "'"
												+ " AND "
												+ ChewContract.ProduceChosen.MONTH
												+ "='" + month_name + "'";

										int numDeleted = getContentResolver()
												.delete(ChewContract.ProduceChosen.CONTENT_URI,
														where, null);
										Utils.assertDeleted(
												getApplicationContext(),
												numDeleted);

										// mAdapter.notifyDataSetChanged();
										loadermanager.restartLoader(2, null,
												InCartCash.this);

									}
								})
						.setNegativeButton(getString(R.string.no),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// logger.info(" {}", );
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		logger.trace("onCreateLoader()");
		Log.d(TAG, "onCreateLoader called");
		logger.debug("onCreateLoader called");
		String[] projection = new String[] { ChewContract.ProduceChosen._ID,
				ChewContract.ProduceChosen.COST,
				ChewContract.ProduceChosen.PRODUCE_NAME,
				ChewContract.ProduceChosen.VOUCHER_CODE };

		String month_name = Utils.getMonth();
		logger.debug("Month name: {} and Name: {}", month_name, name);
		Log.d(TAG, month_name);
		Log.d(TAG, name);

		String where = ChewContract.ProduceChosen.MONTH + "='" + month_name
				+ "'" + " AND " + ChewContract.ProduceChosen.MEMBER_NAME + "='"
				+ name + "'";

		String sortOrder = ChewContract.ProduceChosen.PRODUCE_NAME + " ASC";

		CursorLoader loader = new CursorLoader(InCartCash.this,
				ChewContract.ProduceChosen.CONTENT_URI, projection, where,
				null, sortOrder);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		logger.trace("onLoadFinished()");
		mAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		logger.trace("onLoaderReset()");
		mAdapter.changeCursor(null);
	}

	private class MySimpleCursorAdapterCash extends SimpleCursorAdapter {

		public MySimpleCursorAdapterCash(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, 0);
			logger.trace("MySimpleCursorAdapter.MySimpleCursorAdapter()");
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			logger.trace("MySimpleCursorAdapterCash.newView()");
			logger.debug("NEWVIEW {}", "called");
			ViewHolder holder = new ViewHolder();
			View v = null;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			String vCode = cursor.getString(cursor
					.getColumnIndex(ChewContract.ProduceChosen.VOUCHER_CODE));
			// make sure these vouchers are in use before displaying them
			if (vCodesInUse != null && vCodesInUse.contains(vCode)) {

				v = inflater.inflate(R.layout.in_cart_cash, null);
				holder.textViewName = (TextView) v
						.findViewById(R.id.produceName);
				holder.textViewVCode = (TextView) v
						.findViewById(R.id.voucherCode);
				holder.textViewPrice = (TextView) v
						.findViewById(R.id.producePrice);

				v.setTag(holder);
			}
			return v;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			logger.trace("MySimpleCursorAdapterCash.bindView()");
			logger.debug("BINDVIEW {}", "called");

			// view may be null if no selections have been made
			if (view != null) {

				ViewHolder holder = (ViewHolder) view.getTag();

				String vCode = cursor
						.getString(cursor
								.getColumnIndex(ChewContract.ProduceChosen.VOUCHER_CODE));
				// make sure these vouchers are in use before displaying them
				if (vCodesInUse.contains(vCode)) {

					holder.textViewVCode
							.setText(cursor.getString(cursor
									.getColumnIndex(ChewContract.ProduceChosen.VOUCHER_CODE)));
					holder.textViewName
							.setText(cursor.getString(cursor
									.getColumnIndex(ChewContract.ProduceChosen.PRODUCE_NAME)));

					holder.textViewPrice
							.setText(cursor.getString(cursor
									.getColumnIndex(ChewContract.ProduceChosen.COST)));

				}
			}
		}
	}

	int[] uiBindTo = { R.id.producePrice, R.id.produceName, R.id.voucherCode };

	public static class ViewHolder {
		public TextView textViewVCode;
		public TextView textViewName;
		public TextView textViewPrice;
	}
}
