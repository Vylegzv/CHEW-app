package com.vanderbilt.isis.chew;

import com.vanderbilt.isis.chew.db.ChewContract;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

public class ShoppingList extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private MySimpleCursorAdapter mAdapter;
	LoaderManager loadermanager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadermanager = getLoaderManager();

		int[] uiBindTo = { android.R.id.text1, android.R.id.text2 };

		/*
		 * mAdapter = new SimpleCursorAdapter(InCart.this,
		 * android.R.layout.simple_list_item_2, null, new String[] {
		 * OverallContentProvider.ProductsChosen.PRODUCT_NAME,
		 * OverallContentProvider.ProductsChosen.QUANTITY}, uiBindTo, 0);
		 */

		mAdapter = new MySimpleCursorAdapter(ShoppingList.this,
				R.layout.shopping_list_listview, null, new String[] {
						ChewContract.ShoppingItems.RECIPE_NAME,
						ChewContract.ShoppingItems.INGREDIENT }, null, 0);

		setListAdapter(mAdapter);
		loadermanager.initLoader(1, null, this);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		String[] projection = new String[] { ChewContract.ShoppingItems._ID,
				ChewContract.ShoppingItems.RECIPE_NAME,
				ChewContract.ShoppingItems.INGREDIENT, };

		String where = null;

		String sortOrder = null;

		CursorLoader loader = new CursorLoader(ShoppingList.this,
				ChewContract.ShoppingItems.CONTENT_URI, projection, where,
				null, sortOrder);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		mAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		mAdapter.changeCursor(null);
	}

	public static class ViewHolder {

		public TextView ingredientTV;
		public TextView separator;
	}

	private class MySimpleCursorAdapter extends SimpleCursorAdapter {

		private int[] mCellStates;

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
			this.mCellStates = c == null ? null : new int[c.getCount()];
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

			Log.d("NEWVIEW", "called");

			ViewHolder holder = new ViewHolder();
			View v = null;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.shopping_list, null);
			holder.separator = (TextView) v.findViewById(R.id.separator);
			holder.ingredientTV = (TextView) v
					.findViewById(R.id.ingredientName);

			v.setTag(holder);

			return v;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {

			Log.d("BINDVIEW", "called");

			ViewHolder holder = (ViewHolder) view.getTag();

			boolean needSeparator = false;
			final int position = cursor.getPosition();
			final String recipe = cursor.getString(cursor
					.getColumnIndex(ChewContract.ShoppingItems.RECIPE_NAME));

			// need heading/separator for the beginning of list for sure
			if (position == 0) {
				needSeparator = true;
				// else decide if we need separator or not
			} else {
				cursor.moveToPosition(position - 1);
				String recipe1 = cursor
						.getString(cursor
								.getColumnIndex(ChewContract.ShoppingItems.RECIPE_NAME));
				if (recipe1.compareTo(recipe) != 0) {
					needSeparator = true;
				} else {
					needSeparator = false;
				}
				cursor.moveToPosition(position);
			}

			if (needSeparator) {
				String recipeName = cursor
						.getString(cursor
								.getColumnIndex(ChewContract.ShoppingItems.RECIPE_NAME));

				holder.separator.setText(recipeName);
				holder.separator.setTextColor(getResources().getColor(
						R.color.greySeparator));
				holder.separator.setVisibility(View.VISIBLE);
			} else {
				holder.separator.setVisibility(View.GONE);
			}

			holder.ingredientTV.setText(cursor.getString(cursor
					.getColumnIndex(ChewContract.ShoppingItems.INGREDIENT)));

		}
	}
}
