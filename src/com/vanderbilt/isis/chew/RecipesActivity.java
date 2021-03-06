package com.vanderbilt.isis.chew;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.adapters.GridViewAdapter;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.recipes.Recipe;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
//Yummy Gallery and Favorite Recipes
//Yummy Gallery is superset of Favorite

public class RecipesActivity extends Activity implements OnItemClickListener {

	private static final Logger logger = LoggerFactory
			.getLogger(RecipesActivity.class);

	String TAG = getClass().getSimpleName();

	private GridView gridView;
	private GridViewAdapter customGridAdapter;
	ArrayList<Recipe> data = new ArrayList<Recipe>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		setContentView(R.layout.recipe_grid_layout);

		Log.d(TAG, "on create called");
		logger.debug("onCreate called");

		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setOnItemClickListener(this);

		Bundle getStuff = getIntent().getExtras();
		boolean isShowFavorite = false;
		if (getStuff != null) {
			isShowFavorite = getStuff.getBoolean("isFavorite");
		}
		
		if(isShowFavorite)
			getActionBar().setTitle(getString(R.string.favor_snack_act));

		getData(isShowFavorite);

	}

	private void getData(boolean isShowFavorite) {
		logger.trace("getData()");
		CursorLoader loader = null;

		String[] resultColumns = new String[] { ChewContract.Recipes._ID,
				ChewContract.Recipes.SHORT_TITLE,
				ChewContract.Recipes.RECIPE_IMAGE,
				ChewContract.Recipes.FAVORITE };

		String where = null;
		if (isShowFavorite)
			where = ChewContract.Recipes.FAVORITE + "=" + 1;

		loader = new CursorLoader(RecipesActivity.this,
				ChewContract.Recipes.CONTENT_URI, resultColumns, where, null,
				null);
		loader.registerListener(50, new MyOnLoadCompleteListener());
		loader.startLoading();

	}

	private class MyOnLoadCompleteListener implements
			OnLoadCompleteListener<Cursor> {

		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
			logger.trace("MyOnLoadCompleteListener.onLoadComplete()");
			int id = 0;
			String recipeName = "";
			String recipeImage = "";
			boolean isFavorite = false;

			Log.d("DEBUG", "onLoadComplete called");
			logger.debug("onLoadComplete caled");

			if (cursor != null) {

				while (cursor.moveToNext()) {

					// Log.d("DEBUG", "cursor moved");

					id = Integer.parseInt(cursor.getString(0));
					int recipeResourceId = RecipesActivity.this.getResources()
							.getIdentifier(cursor.getString(1), "string",
									"com.vanderbilt.isis.chew");
					recipeName = getString(recipeResourceId);
					recipeImage = cursor.getString(2);
					isFavorite = Integer.valueOf(cursor.getString(3)) == 1;

					Log.d("DEBUG", id + "");
					Log.d("DEBUG", recipeName);
					Log.d("DEBUG", recipeImage);
					Log.d("DEBUG", cursor.getString(3));
					Log.d("DEBUG", isFavorite + "");

					data.add(new Recipe(id, recipeImage, recipeName, isFavorite));
				}
			}

			// Log.d("DATA COUNT2", data.size() + "");

			customGridAdapter = new GridViewAdapter(RecipesActivity.this,
					R.layout.recipe_grid_row, data);
			gridView.setAdapter(customGridAdapter);
		}
	}

	@Override
	public void onItemClick(final AdapterView<?> arg0, final View view,
			final int position, final long id) {
		logger.trace("onItemClick()");
		Intent intent = new Intent(RecipesActivity.this, RecipeActivity.class);
		intent.putExtra("recipe", data.get(position));
		startActivity(intent);
	}

}
