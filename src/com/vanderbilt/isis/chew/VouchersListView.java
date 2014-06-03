package com.vanderbilt.isis.chew;

import com.vanderbilt.isis.chew.db.ChewContract;

import android.app.AlertDialog;
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

public class VouchersListView extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private MySimpleCursorAdapter dataAdapter;
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

		dataAdapter = new MySimpleCursorAdapter(VouchersListView.this,
				R.layout.display_vouchers, null,
				new String[] { ChewContract.FamilyVouchers.NAME },
				null, 0);

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
				//String vCode = c.getString(2);
				//String used = c.getString(3);
				Log.d("NAME", name);
				//Log.d("VCODE", vCode);
				//Log.d("USED", used);
				Intent intent = new Intent(VouchersListView.this, Profile.class);
				intent.putExtra("name", name);
				//intent.putExtra("voucherCode", vCode);
				//intent.putExtra("used", used);
				startActivity(intent);

			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		// mAdapter.swapCursor(cursor);
		dataAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		// mAdapter.swapCursor(null);
		dataAdapter.changeCursor(null);
	}

	private class MySimpleCursorAdapter extends SimpleCursorAdapter {

		private int mSelectedPosition;
		Cursor items;
		private Context context;
		private int layout;
		private LayoutInflater mInflater;

		public MySimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, 0);
			this.context = context;
			this.layout = layout;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View v = inflater.inflate(R.layout.display_vouchers_row, null);
			holder.textViewName = (TextView) v.findViewById(R.id.name);

			v.setTag(holder);

			return v;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {

			ViewHolder holder = (ViewHolder) view.getTag();

			holder.textViewName
					.setText(cursor.getString(cursor
							.getColumnIndex(ChewContract.FamilyVouchers.NAME)));
		}
	}

	public static class ViewHolder {
		public TextView textViewName;

	}
}
