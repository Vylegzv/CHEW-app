package com.vanderbilt.isis.chew;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.db.ChewContract;
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

//Choose Family Member

//Then when you choose a person name, you go to Profile.java
public class MembersListView extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final Logger logger = LoggerFactory.getLogger(MembersListView.class);
	
	private SimpleCursorAdapter dataAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		logger.info("Opened Family Member List - Choose a Family Member");
		loadermanager = getLoaderManager();

		int[] uiBindTo = { R.id.name };
		dataAdapter = new SimpleCursorAdapter(MembersListView.this,
				R.layout.display_names_row, null,
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
				logger.debug("CLICK {} clicked", c.getString(1));
				String name = c.getString(1);
				logger.info("Selected Family Member {} who is {} in the list", name, position );
				Log.d("NAME", name);
				logger.debug("NAME {}", name);
				Intent intent = new Intent(MembersListView.this, Profile.class);
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
				ChewContract.FamilyVouchers.NAME };

		String where = null;

		CursorLoader loader = new CursorLoader(MembersListView.this,
				ChewContract.CONTENT_URI_DISTINCT_NAMES, projection,
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
}
