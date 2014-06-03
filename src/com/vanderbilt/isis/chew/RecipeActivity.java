package com.vanderbilt.isis.chew;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.adapters.IngredientsAdapter;
import com.vanderbilt.isis.chew.adapters.IngredientsStepsAdapter;
import com.vanderbilt.isis.chew.adapters.StepsAdapter;
import com.vanderbilt.isis.chew.db.ChewContentProvider;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.recipes.Ingredient;
import com.vanderbilt.isis.chew.recipes.Recipe;
import com.vanderbilt.isis.chew.recipes.Step;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
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

public class RecipeActivity extends Activity {

	String TAG = getClass().getSimpleName();
	ImageView imageView;
	TextView titleView;
	TextView setFavorite;
	ListView stepsLV;
	Bitmap mainImage;
	Recipe recipe;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_view);

		View header = getLayoutInflater().inflate(R.layout.recipe_header, null);

		imageView = (ImageView) header.findViewById(R.id.recipeImage);
		titleView = (TextView) header.findViewById(R.id.recipeTitle);
		setFavorite = (TextView) header.findViewById(R.id.setFavorite);
		LinearLayout ll = (LinearLayout) header.findViewById(R.id.l2);
		stepsLV = (ListView) findViewById(R.id.stepsListView);
		stepsLV.addHeaderView(header);

		Bundle data = getIntent().getExtras();
		// int recipe_id = data.getInt("recipe_id");
		recipe = (Recipe) data.getParcelable("recipe");
		if(recipe.isFavorite()){
			Log.d(TAG, "favorite");
			setFavorite.setText("Unset as Favorite");
		}

		// Log.d(TAG, recipe_id+"");
		Log.d(TAG, recipe.getTitle());

		// ArrayList<Ingredient> ingredients = getIngredients(recipe.getId());
		recipe.setIngredients(getIngredients(recipe.getId()));

		/*
		 * for(Ingredient i : ingredients){ Log.d(TAG,
		 * i.getLongerDescription()); }
		 * 
		 * ArrayList<Step> steps = getSteps(recipe.getId());
		 */
		recipe.setSteps(getSteps(recipe.getId()));

		/*
		 * for(Step s : steps){ Log.d(TAG, s.getStep()); if(s.getImage() ==
		 * null){ Log.d(TAG, "Image null"); } }
		 */

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

		/*
		 * IngredientsAdapter ingredientsAdapter = new
		 * IngredientsAdapter(RecipeActivity.this, recipe.getIngredients());
		 * ingredientsLV.setAdapter(ingredientsAdapter);
		 */

		StepsAdapter stepsAdapter = new StepsAdapter(RecipeActivity.this,
				recipe.getSteps());
		stepsLV.setAdapter(stepsAdapter);

		/*
		 * IngredientsStepsAdapter adapter = new
		 * IngredientsStepsAdapter(RecipeActivity.this, recipe.getIngredients(),
		 * recipe.getSteps()); ingredientsLV.setAdapter(adapter);
		 */

	}

	private ArrayList<Ingredient> getIngredients(int id) {

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
		
		ContentValues updateValues = new ContentValues();
		int rowsUpdate = 0;
		
		updateValues.put(
				ChewContract.Recipes.FAVORITE, 1);
		
		String where = ChewContract.ProductsChosen._ID + "=" + recipe.getId();

		rowsUpdate = getContentResolver().update(
				ChewContract.Recipes.CONTENT_URI,
				updateValues, where, null);
		
		if(rowsUpdate == 1){
			Toast.makeText(getApplicationContext(), "Recipe Set as Favorite",
					   Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), "There was a problem",
					   Toast.LENGTH_SHORT).show();
		}
	}
	
	public void addToShoppingList(View v) {

		ContentValues[] cvs = new ContentValues[recipe.getIngredients().size()];
		int count = 0;
		for (Ingredient i : recipe.getIngredients()) {

			ContentValues cv = new ContentValues();
			cv.put(ChewContract.ShoppingItems.RECIPE_NAME, recipe.getTitle());
			cv.put(ChewContract.ShoppingItems.INGREDIENT, i.getIngredient());
			cvs[count] = cv;
			count++;
		}

		//getContentResolver().bulkInsert(ChewContract.ShoppingItems.CONTENT_URI, cvs);
		new InsertTask().execute(cvs);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/** **/
		mainImage.recycle();
		mainImage = null;
	}

	private class InsertTask extends AsyncTask<ContentValues[], Void, Integer>{

		@Override
		protected Integer doInBackground(ContentValues[]... params) {
			
			return getContentResolver().bulkInsert(ChewContract.ShoppingItems.CONTENT_URI, params[0]);
		}
		
		protected void onPostExecute(final Integer numInserted) {
			
			if(numInserted > 0){
			Toast.makeText(getApplicationContext(), "Ingredients were added to your shopping list",
					   Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "There was a problem",
						   Toast.LENGTH_SHORT).show();
			}
		}
	}
}
