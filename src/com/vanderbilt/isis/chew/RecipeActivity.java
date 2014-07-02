package com.vanderbilt.isis.chew;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.adapters.StepsAdapter;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.recipes.Ingredient;
import com.vanderbilt.isis.chew.recipes.Recipe;
import com.vanderbilt.isis.chew.recipes.Step;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//Comes here if you choose any one recipe in RecipesActivity.java (i.e. in Yummy Gallery or Favorite Recipes)
public class RecipeActivity extends Activity {

	private static final Logger logger = LoggerFactory.getLogger(RecipeActivity.class);
	
	String TAG = getClass().getSimpleName();
	ImageView imageView;
	TextView titleView;
	TextView setFavoriteView;
	ListView stepsLV;
	Bitmap mainImage;
	Recipe recipe;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_view);
		logger.trace("onCreate()");
		Log.d(TAG, "on create called");
		logger.debug("onCreate Called");
		View header = getLayoutInflater().inflate(R.layout.recipe_header, null);

		imageView = (ImageView) header.findViewById(R.id.recipeImage);
		titleView = (TextView) header.findViewById(R.id.recipeTitle);
		setFavoriteView = (TextView) header.findViewById(R.id.setFavorite);
		LinearLayout ll = (LinearLayout) header.findViewById(R.id.l2);
		stepsLV = (ListView) findViewById(R.id.stepsListView);
		stepsLV.addHeaderView(header);

		Bundle data = getIntent().getExtras();
		recipe = (Recipe) data.getParcelable("recipe");
		if(recipe.isFavorite()){
			Log.d(TAG, "favorite");
			logger.debug("favorite");
			setFavoriteView.setText(getString(R.string.unset_favorite));
		}

		Log.d(TAG, recipe.getTitle());
		logger.debug(" {}", recipe.getTitle());

		recipe.setIngredients(getIngredients(recipe.getId()));
		recipe.setSteps(getSteps(recipe.getId()));

		titleView.setText(recipe.getTitle());

		int path = getResources().getIdentifier(recipe.getImage(), "drawable",
				"com.vanderbilt.isis.chew");
		mainImage = BitmapFactory.decodeResource(getResources(), path);
		imageView.setImageBitmap(mainImage);

		TextView[] txt = new TextView[recipe.getIngredients().size()];
		for (int i = 0; i < txt.length; i++) {

			txt[i] = new TextView(RecipeActivity.this);
			txt[i].setText(recipe.getIngredients().get(i)
					.getLongerDescription());
			txt[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			txt[i].setTextSize(13);
			txt[i].setTextColor(getResources().getColor(R.color.textcolor));
			txt[i].setTypeface(null, Typeface.BOLD);
			ll.addView(txt[i]);

		}

		StepsAdapter stepsAdapter = new StepsAdapter(RecipeActivity.this,
				recipe.getSteps());
		stepsLV.setAdapter(stepsAdapter);

	}

	private ArrayList<Ingredient> getIngredients(int id) {
		logger.trace("getIngredients()");
		String selection = ChewContract.Ingredients.RECIPE_ID + "=?";
		String selectionArgs[] = { id + "" };
		Cursor curIngrs = getContentResolver().query(
				ChewContract.Ingredients.CONTENT_URI, null, selection,
				selectionArgs, null);

		ArrayList<Ingredient> ings = new ArrayList<Ingredient>();

		if (curIngrs != null) {
			while (curIngrs.moveToNext())
				ings.add(Ingredient.fromCursor(curIngrs));
			curIngrs.close();
		}
		return ings;
	}

	private ArrayList<Step> getSteps(int id) {
		logger.trace("getSteps()");
		String selection = ChewContract.Steps.RECIPE_ID + "=?";
		String selectionArgs[] = { id + "" };
		Cursor curSteps = getContentResolver().query(
				ChewContract.Steps.CONTENT_URI, null, selection, selectionArgs,
				null);

		ArrayList<Step> steps = new ArrayList<Step>();

		if (curSteps != null) {
			while (curSteps.moveToNext())
				steps.add(Step.fromCursor(RecipeActivity.this, curSteps));
			curSteps.close();
		}
		return steps;
	}

	public void setFavorite(View v) {
		logger.trace("setFavourite()");
		ContentValues updateValues = new ContentValues();
		int rowsUpdate = 0;
			
		boolean setFavorite = true;
		if(recipe.isFavorite())
			setFavorite = false;
		
		updateValues.put(
				ChewContract.Recipes.FAVORITE, setFavorite);
		
		String where = ChewContract.ProductsChosen._ID + "=" + recipe.getId();

		rowsUpdate = getContentResolver().update(
				ChewContract.Recipes.CONTENT_URI,
				updateValues, where, null);
		
		if(rowsUpdate == 1){
			
			recipe.setFavorite(setFavorite);
			if(recipe.isFavorite()){
				setFavoriteView.setText(getString(R.string.unset_favorite));
				Toast.makeText(getApplicationContext(), getString(R.string.set_favorite_toast),
					   Toast.LENGTH_SHORT).show();
			}else{
				setFavoriteView.setText(getString(R.string.set_favorite));
				Toast.makeText(getApplicationContext(), getString(R.string.unset_favorite_toast),
						   Toast.LENGTH_SHORT).show();
		    }
		}else{
			Toast.makeText(getApplicationContext(), getString(R.string.problem),
					   Toast.LENGTH_SHORT).show();
		}
	}
	
	public void addToShoppingList(View v) {
		logger.trace("addToShoppingList()");
		ContentValues[] cvs = new ContentValues[recipe.getIngredients().size()];
		int count = 0;
		for (Ingredient i : recipe.getIngredients()) {

			ContentValues cv = new ContentValues();
			cv.put(ChewContract.ShoppingItems.RECIPE_NAME, recipe.getTitle());
			cv.put(ChewContract.ShoppingItems.INGREDIENT, i.getIngredient());
			cvs[count] = cv;
			count++;
		}

		new InsertTask().execute(cvs);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		logger.trace("onDestroy()");
		mainImage.recycle();
		mainImage = null;
	}

	private class InsertTask extends AsyncTask<ContentValues[], Void, Integer>{

		@Override
		protected Integer doInBackground(ContentValues[]... params) {
			logger.trace("InsertTask.doInBackground()");
			return getContentResolver().bulkInsert(ChewContract.ShoppingItems.CONTENT_URI, params[0]);
		}
		
		protected void onPostExecute(final Integer numInserted) {
			logger.trace("InsertTask.onPostExecute()");
			if(numInserted > 0){
			Toast.makeText(getApplicationContext(), getString(R.string.ingredients_added_toast),
					   Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), getString(R.string.problem),
						   Toast.LENGTH_SHORT).show();
			}
		}
	}
}
