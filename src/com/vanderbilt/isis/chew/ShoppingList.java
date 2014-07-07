package com.vanderbilt.isis.chew;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.db.ChewContract;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
//ShoppingList is unrelated to the vouchers and only hasa the items from Recipes which they have added to the shiopping list

public class ShoppingList extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final Logger logger = LoggerFactory.getLogger(ShoppingList.class);
	
	private MySimpleCursorAdapter mAdapter;
	LoaderManager loadermanager;
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		logger.info("Opened the Shopping List");
		
		loadermanager = getLoaderManager();

		mAdapter = new MySimpleCursorAdapter(ShoppingList.this,
				R.layout.shopping_list_listview, null, new String[] {
						ChewContract.ShoppingItems.RECIPE_NAME,
						ChewContract.ShoppingItems.INGREDIENT }, null, 0);
		
		View header = getLayoutInflater().inflate(R.layout.shopping_list_header, null);
		ListView lv = getListView();
		lv.addHeaderView(header);

		setListAdapter(mAdapter);
		loadermanager.initLoader(1, null, this);
	}
	
	public void clearShopList(View v) {
		logger.trace("clearShopList()");
		logger.info("Cleared the Shopping List");
		Log.d("Shopping", "clear called");
		logger.debug("clear called");
		ContentValues updateValues = new ContentValues();
		updateValues.put(ChewContract.ShoppingItems.SHOW, 0);

		new UpdateTask().execute(updateValues);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		logger.trace("onCreateLoader()");
		String[] projection = new String[] { ChewContract.ShoppingItems._ID,
				ChewContract.ShoppingItems.RECIPE_NAME,
				ChewContract.ShoppingItems.INGREDIENT, };

		String where = ChewContract.ShoppingItems.SHOW + "=" + 1 + "";;

		String sortOrder = null;

		CursorLoader loader = new CursorLoader(ShoppingList.this,
				ChewContract.ShoppingItems.CONTENT_URI, projection, where,
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

	public static class ViewHolder {

		public TextView ingredientTV;
		public TextView separator;
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
			logger.debug("NEWVIEW called");

			ViewHolder holder = new ViewHolder();
			View v = null;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.shopping_list_row, null);
			holder.separator = (TextView) v.findViewById(R.id.separator);
			holder.ingredientTV = (TextView) v
					.findViewById(R.id.ingredientName);

			v.setTag(holder);

			return v;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			logger.trace("MySimpleCursorAdapter.bindView()");
			Log.d("BINDVIEW", "called");
			logger.debug("BINDVIEW called");
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
	
	private class UpdateTask extends AsyncTask<ContentValues, Void, Integer> {

		@Override
		protected Integer doInBackground(ContentValues... params) {
			logger.trace("UpdateTask.doInBackground()");
			String where = ChewContract.ShoppingItems.SHOW + "=" + 1 + "";

			return getContentResolver()
					.update(ChewContract.ShoppingItems.CONTENT_URI,
							params[0],
							where,
							null);
			
		}

		protected void onPostExecute(final Integer numUpdated) {
			logger.trace("UpdateTask.onPostExecute()");
			if (numUpdated == 0) {
				Toast.makeText(getApplicationContext(), getString(R.string.problem),
						Toast.LENGTH_SHORT).show();
			} else {
				loadermanager.restartLoader(1, null, ShoppingList.this);
			}
		}
	}
}
