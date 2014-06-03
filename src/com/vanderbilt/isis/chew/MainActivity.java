package com.vanderbilt.isis.chew;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.model.CheckBoxRowModel;
import com.vanderbilt.isis.chew.utils.Utils;

import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.OperationApplicationException;
import android.content.Loader.OnLoadCompleteListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends Activity implements OnItemClickListener {

	public final String TAG = getClass().getSimpleName();

	public static final int SCAN = 0;
	public static final int CHOOSE = 1;
	public static final int PRODUCE = 2;
	public static final int SHOPPING = 3;
	public static final int DONE = 4;
	public static final int FAV_RECIPES = 5;
	public static final int RECIPES = 6;
	public static final int VIDEOS = 7;
	public static final int SHOPLIST = 8;
	public static final int UPLOAD = 9;

	public static final String[] titles = new String[] { "Scan the Barcode",
			"Choose a Family Member", "Buy Produce", "Start Shopping",
			"Done Shopping", "Favorite Recipes", "Yummy Gallery", 
			"Watch Snack Recipe Videos", "Shopping List", "Voucher Upload" };

	public static final String[] descriptions = new String[] {
			"Shop for all family", "Shop for a particular member",
			"Calculate the prices", "Click here to start using your vouchers",
			"Click here when you are done shopping", "View Your Favorite Recipes!",
			"Easy and quick snack recipes!", "Watch how to fix snacks!", 
			"Ingredients for recipes", "Upload your family vouchers" };

	public static final Integer[] images = { R.drawable.barcode,
			R.drawable.family, R.drawable.produce, R.drawable.shopping_cart,
			R.drawable.done, R.drawable.favorite_recipes, R.drawable.recipes, 
			R.drawable.videos, R.drawable.ic_launcher, R.drawable.upload };

	ListView listView;
	List<MainListRowItem> rowItems;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		rowItems = new ArrayList<MainListRowItem>();
		for (int i = 0; i < titles.length; i++) {
			MainListRowItem item = new MainListRowItem(images[i], titles[i],
					descriptions[i]);
			rowItems.add(item);
		}

		listView = (ListView) findViewById(R.id.list);
		MainListViewAdapter adapter = new MainListViewAdapter(this,
				R.layout.main_list_row, rowItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent intent = null;

		switch (position) {

		case 0:

			scan();
			break;

		case 1:

			intent = new Intent(MainActivity.this, VouchersListView.class);
			startActivity(intent);
			break;

		case PRODUCE:

			intent = new Intent(MainActivity.this, Produce.class);
			startActivity(intent);
			break;

		case SHOPPING:

			Log.d(TAG, "in shopping");
			showChooseStoresD();
			break;
			
		case DONE:
			
			done();
			break;
			
		case FAV_RECIPES:
			
			intent = new Intent(MainActivity.this, RecipesActivity.class);
			intent.putExtra("isFavorite", true);
			startActivity(intent);
			break;

		case RECIPES:

			intent = new Intent(MainActivity.this, RecipesActivity.class);
			intent.putExtra("isFavorite", false);
			startActivity(intent);
			break;

		case SHOPLIST:

			intent = new Intent(MainActivity.this, ShoppingList.class);
			startActivity(intent);
			break;

		case UPLOAD:

			intent = new Intent(MainActivity.this, VoucherUpload.class);
			startActivity(intent);
			break;

		default:

		}

		/*
		 * Toast toast = Toast.makeText(getApplicationContext(), "Item " +
		 * (position + 1) + ": " + rowItems.get(position), Toast.LENGTH_SHORT);
		 * toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		 * toast.show();
		 */
	}

	public void scan() {
		(new IntentIntegrator(this)).initiateScan();
	}

	public void onActivityResult(int request, int result, Intent i) {
		IntentResult scan = IntentIntegrator.parseActivityResult(request,
				result, i);

		if (scan != null && result == RESULT_OK) {
			Log.d(TAG, scan.getContents());
			String b = scan.getContents();
			String barcode = Utils.removeZeros(b);
			Log.d(TAG, barcode);
			
			String[] projection = {ChewContract.Store._ID, ChewContract.Store.FOOD_NAME, ChewContract.Store.FOOD_CATEGORY,
					ChewContract.Store.VOUCHERS_ID, ChewContract.Store.SIZE, ChewContract.Store.SIZE_TYPE,
					ChewContract.Store.FOOD_TYPE};
			
			String store = Utils.getStore(MainActivity.this);
			
			// it could be either in one store or in both stores
			String selection = ChewContract.Store.BARCODE + "='" + barcode + "'"
					+ " AND " + ChewContract.Store.STORE + "='" + store + "'"
					+ " OR " + ChewContract.Store.BARCODE + "='" + barcode + "'"
					+ " AND " + ChewContract.Store.STORE + "='" + Utils.ALL_STORES + "'";
			
			CursorLoader loader = new CursorLoader(MainActivity.this, ChewContract.Store.CONTENT_URI, projection, selection, null, null);
			loader.registerListener(50, new MyOnLoadCompleteListener2());
			loader.startLoading(); 
		}
	}

	private void showChooseStoresD() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
		final CharSequence[] stores = { "Walmart", "Kroger" };
		alertDialogBuilder.setTitle("Which store are you in?");
		alertDialogBuilder
				.setSingleChoiceItems(stores, 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								selectedStore = which;
							}
						})

				// set dialog message
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// not the same as 'which' above
						Log.d(TAG, "Which value=" + id);
						Log.d(TAG, "Selected value=" + selectedStore);
						// Toast.makeText(MainActivity.this, "Select " +
						// stores[selectedStore], Toast.LENGTH_SHORT).show();
						
						if(Utils.setStore(MainActivity.this, stores[selectedStore].toString()))
							MainActivity.this.getVouchers();
						else
							Log.d(TAG, "error saving store in shared preferences");
					}
				})

				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void getVouchers() {

		CursorLoader loader = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM");
		String month = formatter.format(cal.getTime());
		Log.d(TAG, month);

		String[] resultColumns = new String[] {
				ChewContract.FamilyVouchers._ID,
				ChewContract.FamilyVouchers.VOUCHER_CODE,
				ChewContract.FamilyVouchers.NAME};

		String where = ChewContract.FamilyVouchers.VOUCHER_MONTH + "='" + month
				+ "'";

		loader = new CursorLoader(MainActivity.this,
				ChewContract.FamilyVouchers.CONTENT_URI, resultColumns, where,
				null, null);
		loader.registerListener(1, new MyOnLoadCompleteListener());
		loader.startLoading();
	}

	ArrayList<String> selected;
	int selectedStore;

	private void showChooseVouchersD(final CharSequence[] voucherCodes) {

		//boolean[] selections = new boolean[voucherCodes.length];
		selected = new ArrayList<String>();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
		alertDialogBuilder.setTitle("Which vouchers will you be using today?");
		alertDialogBuilder.setMultiChoiceItems(voucherCodes, null,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {

						if (isChecked) {
							selected.add(voucherCodes[which].toString());
						}
					}
				}).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
 
						Set<String> vouchersUsed = new HashSet<String>();
						
						for (String item : selected) {
							Log.d("Selected", item);
							vouchersUsed.add(item);	
						} // save the vouchers
					
						Utils.setVouchers(MainActivity.this, vouchersUsed);
					}
				}).setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int id) {
				
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}
	
	private void done(){
		
		String where = ChewContract.FamilyVouchers.USED
				+ "='"
				+ Utils.INUSE
				+ "'";
		
		ContentValues updateValues = new ContentValues();
		updateValues.put(ChewContract.FamilyVouchers.USED, Utils.USED); 
		int rowsUpdate = getContentResolver().update(
				ChewContract.FamilyVouchers.CONTENT_URI,
				updateValues, where, null);
		Log.d("ROWSUPDATE", rowsUpdate + "");
	}

	private class MyOnLoadCompleteListener implements
			OnLoadCompleteListener<Cursor> {
		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {

			Log.d(TAG, "on load complete called");
			Log.d(TAG, cursor.getCount() + " rows");

			CharSequence[] vcodes = new CharSequence[cursor.getCount()];

			while (cursor != null && cursor.moveToNext()) {
				Log.d(TAG, cursor.getString(1));
				Log.d(TAG, cursor.getString(2));
				vcodes[cursor.getPosition()] = cursor.getString(1) + " - " + cursor.getString(2);
			}

			showChooseVouchersD(vcodes);
		}
	}
	
	private class MyOnLoadCompleteListener2 implements OnLoadCompleteListener<Cursor>{

		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
			
			if (cursor != null && cursor.moveToNext()) {
				
				final String food_name = cursor.getString(1);
				final String food_category = cursor.getString(2);
				final int vouchersID = Integer.parseInt(cursor.getString(3));
				final double size = Double.parseDouble(cursor.getString(4));
				final String size_type = cursor.getString(5);
				final String food_type = cursor.getString(6);
				
				Log.d(TAG, food_name);
				Log.d(TAG, food_category);
				Log.d(TAG, vouchersID+"");
				Log.d(TAG, size+"");
				Log.d(TAG, size_type+"");
				Log.d(TAG, food_type+"");
				
				// set title
				alertDialogBuilder.setTitle(food_name);
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Do you want to get it?")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							//MainActivity.this.finish();
							Intent intent = new Intent(MainActivity.this, GetProducts.class);
							intent.putExtra("member_name", "");
							intent.putExtra("food_name", food_name);
							intent.putExtra("food_category", food_category);
							intent.putExtra("vouchersID", vouchersID);
							intent.putExtra("size", size);
							intent.putExtra("size_type", size_type);
							intent.putExtra("food_type", food_type);
							
							startActivity(intent);
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
		}else{
			
				// set title
				alertDialogBuilder.setTitle("You cannot get this item!");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Please select a WIC approved item at this store.")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										// MainActivity.this.finish();
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		}
	}
}
