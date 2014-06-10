package com.vanderbilt.isis.chew;

import java.util.ArrayList;

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

public class RecipesActivity extends Activity implements OnItemClickListener {

	String TAG = getClass().getSimpleName();
	
	private GridView gridView;
	private GridViewAdapter customGridAdapter;
	ArrayList<Recipe> data = new ArrayList<Recipe>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_grid_layout);
		
		Log.d(TAG, "on create called");

		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setOnItemClickListener(this);
		
		Bundle getStuff = getIntent().getExtras();
		boolean isShowFavorite = false;
		if (getStuff != null) {
			isShowFavorite = getStuff.getBoolean("isFavorite");
		}

		getData(isShowFavorite);

	}

	private void getData(boolean isShowFavorite) {

		CursorLoader loader = null;

		String[] resultColumns = new String[] { ChewContract.Recipes._ID,
				ChewContract.Recipes.SHORT_TITLE,
				ChewContract.Recipes.RECIPE_IMAGE,
				ChewContract.Recipes.FAVORITE };
		
		String where = null;
		if(isShowFavorite)
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

			int id = 0;
			String recipeName = "";
			String recipeImage = "";
			boolean isFavorite = false;

			Log.d("DEBUG", "onLoadComplete called");

			if (cursor != null) {

				while (cursor.moveToNext()) {

					//Log.d("DEBUG", "cursor moved");

					id = Integer.parseInt(cursor.getString(0));
					recipeName = cursor.getString(1);
					recipeImage = cursor.getString(2);
					isFavorite = Integer.valueOf(cursor.getString(3)) == 1;

					/*Log.d("DEBUG", id + "");
					Log.d("DEBUG", recipeName);
					Log.d("DEBUG", recipeImage);
					Log.d("DEBUG", cursor.getString(3));
					Log.d("DEBUG", isFavorite+"");*/

					//int path = RecipesActivity.this.getResources()
					//		.getIdentifier(recipeImage, "drawable",
					//				"com.vanderbilt.isis.chew");
					//Log.d("Path", path + "");
					//Bitmap bitmap = BitmapFactory.decodeResource(
					//		RecipesActivity.this.getResources(), path);

					// data.add(new Recipe(id, bitmap, recipeName));
					data.add(new Recipe(id, recipeImage, recipeName, isFavorite));
				}
			}

			//Log.d("DATA COUNT2", data.size() + "");

			customGridAdapter = new GridViewAdapter(RecipesActivity.this,
					R.layout.recipe_grid_row, data);
			gridView.setAdapter(customGridAdapter);
		}
	}

	@Override
	public void onItemClick(final AdapterView<?> arg0, final View view,
			final int position, final long id) {

		Intent intent = new Intent(RecipesActivity.this, RecipeActivity.class);
		intent.putExtra("recipe", data.get(position));
		startActivity(intent);
	}

}
