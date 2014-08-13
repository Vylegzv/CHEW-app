package com.vanderbilt.isis.chew;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vanderbilt.isis.chew.adapters.MainListViewAdapter;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.model.MainListRowItem;
import com.vanderbilt.isis.chew.notificationmsg.ChewAppLibrary;
import com.vanderbilt.isis.chew.notificationmsg.ConfigurationActivity;
import com.vanderbilt.isis.chew.notificationmsg.NotificationHistoryActivity;
import com.vanderbilt.isis.chew.notificationmsg.SetAlarmService;
import com.vanderbilt.isis.chew.utils.Utils;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.Loader.OnLoadCompleteListener;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.vanderbilt.isis.chew.scanner.*;


public class MainActivity extends Activity implements OnItemClickListener {

	private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
	
	public final String TAG = getClass().getSimpleName();

	public static final int SCAN = 4;//4;//0;
	public static final int CHOOSE = 5;//5;//1;
	public static final int PRODUCE = 6;//6;//2;
	public static final int SHOPPING = 7;//7;//3;
	public static final int DONE = 8;//8;//4;
	public static final int FAV_RECIPES = 1;//5;
	public static final int RECIPES = 0;//6;
	public static final int SHOPLIST = 2;//7;
	public static final int TUTORIAL = 9;//8;
	public static final int UPLOAD = 10;//9;
	public static final int EDIT = 11;//10;
    public static final int HISTORY = 3;//11;
    
	public String[] titles;
	public String[] descriptions;
	public TypedArray images;

	ListView listView;
	List<MainListRowItem> rowItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.trace("onCreate()");
		setContentView(R.layout.main);
		
		logger.info("Home Page has been opened");

		/**********Pankaj Chand's Code BEGIN********/
		//setting the default values from the preferences.xml file in the res/xml folder
				PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
				
				Log.e(TAG, "Main Activity oncreate()");

				//Only EXECUTE ONCE

				SharedPreferences preferencesDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				SharedPreferences preferences = getSharedPreferences("preferences", Context.MODE_MULTI_PROCESS);
				////
				boolean isFirstRun = preferences.getBoolean("FIRSTRUN", true);
				if(!isFirstRun) {
					Log.e(TAG, "isFirstRun is False, MAIN ACTIVITY STARTED BUT NOT DOING INSTALLATION CODE");
				}
				else if (isFirstRun == true)
				{
					Log.e(TAG, "isFirstRun is TRUE, DOING INSTALLATION");
					// Code to run once
					{{
						//if(preferences.getString("pref_language", "DEFAULT").equals("DEFAULT")) {
							preferences.edit().putString("pref_language", preferencesDefault.getString("pref_language", "ENGLISH")).apply();
						//}
						//if(preferences.getString("pref_time", "DEFAULT").equals("DEFAULT")) {
							preferences.edit().putString("pref_time", preferencesDefault.getString("pref_time", "10")).apply();
						//}
						
						Log.e(TAG, "Preferences Language is " + preferences.getString("pref_language", "ERROR"));
						Log.e(TAG, "Preferences Time is " + preferences.getString("pref_time", "ERROR"));
						/****************************/
						/****************************/
						//Calculation of TargetWeekNumber and Today
						Calendar cToday = Calendar.getInstance(Locale.US);
						cToday.setTimeInMillis(System.currentTimeMillis());
						Log.e(TAG, "SUPER ATTENTION TODAY IS: " + cToday.toString());
						
						////////begin JavaRanch 1
						int weekday = cToday.get(Calendar.DAY_OF_WEEK);  
						int days = Calendar.SUNDAY - weekday;  
						if (days < 0)  
						{  
						    // this will usually be the case since Calendar.SUNDAY is the smallest  
						    days += 7;  
						}  
				        ////////end JavaRanch 1	
						
						 //Put in a separate function
						Calendar cTargetDay = Calendar.getInstance(Locale.US);
						cTargetDay.setTimeInMillis(System.currentTimeMillis());
						////////begin JR 2
						cTargetDay.add(Calendar.DAY_OF_YEAR, days);
						///////end JR 2
						
					    ////cStartDay.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					    cTargetDay.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ChewAppLibrary.DEFAULT_CONF_TIME));
				        cTargetDay.set(Calendar.MINUTE,0);
				        cTargetDay.set(Calendar.SECOND,0);
				        Log.e(TAG, "SUPER ATTENTION Target Day IS: " + cTargetDay.toString());
				        
						if (cTargetDay.getTimeInMillis() < cToday.getTimeInMillis()) {
					    	 Log.e(TAG, "cTargetDay Time in Milliseconds is negative, so taking cTargetDay of next week");
						     cTargetDay.add(Calendar.DATE,7);
					     }
						
						int targetWeek = cTargetDay.get(Calendar.WEEK_OF_YEAR);
						Log.e(TAG, "SUPER ATTENTION TARGETWEEK IS: " + targetWeek);
						
						//preferences.edit().putInt("pref_cToday", cToday).apply();
						preferences.edit().putInt("pref_targetWeek", targetWeek).apply();
						////////END OF EXECUTE ONLY ONCE
				
					}}
					Log.e(TAG, "Making FirstRun as False so it will never run again");
				    SharedPreferences.Editor editor = preferences.edit();
				    editor.putBoolean("FIRSTRUN", false);
				    editor.apply();
				}
		/**********Pankaj Chand's Code END********/
				
		/*****Pankaj Application Object Code BEGIN****/		
				Intent service = new Intent(this/*context*/, SetAlarmService.class);
				service.putExtra("calledOn", "APPLICATION_OBJECT");
		        this/*context*/.startService(service);
		/*****Pankaj Application Object Code END****/
		
		titles = getResources().getStringArray(R.array.main_titles_array);
		descriptions = getResources().getStringArray(R.array.main_descriptions_array);
		images = getResources().obtainTypedArray(R.array.main_images_array);
		
		rowItems = new ArrayList<MainListRowItem>();
		for (int i = 0; i < titles.length; i++) {
			MainListRowItem item = new MainListRowItem(images.getResourceId(i, -1), titles[i],
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
		logger.trace("onItemClick()");
		Intent intent = null;

		switch (position) {

		case SCAN:
			logger.info("Clicked SCAN on Homepage");
			scan();
			break;

		case CHOOSE:
			logger.info("Clicked CHOOSE A Family Member on Homepage");
			intent = new Intent(MainActivity.this, MembersListView.class);
			startActivity(intent);
			break;

		case PRODUCE:
			logger.info("Clicked PRODUCE - CALCULATOR on Homepage");
			// first check if they are using cash vouchers
			//nn
			intent = new Intent(MainActivity.this, Produce.class);
			startActivity(intent);
			break;

		case SHOPPING:
			logger.info("Clicked Start SHOPPING on Homepage");
			showChooseStoresD();
			break;
			
		case DONE:			
			logger.info("Clicked DONE Shopping on Homepage");
			done();
			break;
			
		case FAV_RECIPES:			
			logger.info("Clicked FAVORITE_RECIPES on Homepage");
			intent = new Intent(MainActivity.this, RecipesActivity.class);
			intent.putExtra("isFavorite", true);
			startActivity(intent);
			break;

		case RECIPES:
			logger.info("Clicked RECIPES - Yummy Gallery on Homepage");
			intent = new Intent(MainActivity.this, RecipesActivity.class);
			intent.putExtra("isFavorite", false);
			startActivity(intent);
			break;

		case SHOPLIST:
			logger.info("Clicked SHOPLIST on Homepage");
			intent = new Intent(MainActivity.this, ShoppingList.class);
			startActivity(intent);
			break;
			
		case TUTORIAL:
			logger.info("Clicked Video TUTORIAL on Homepage");
			break;

		case UPLOAD:
			logger.info("Clicked UPLOAD Vouchers on Homepage");
            //askForPassword();
			intent = new Intent(MainActivity.this, VoucherUpload.class);
			startActivity(intent);
			break;
			
		case EDIT:
			logger.info("Clicked EDIT Vouchers on Homepage");
			intent = new Intent(MainActivity.this, DeleteVouchers.class);
			startActivity(intent);
			break;

        case HISTORY:
			logger.info("Clicked NOTIFICATION HISTORY on Homepage");
			intent = new Intent(MainActivity.this, NotificationHistoryActivity.class);
			startActivity(intent);
			break;

		default:

		}
	}
	
	private void askForPassword(){
		logger.trace("askForPassword()");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
		alertDialogBuilder.setTitle(getString(R.string.enter_pwd));
		
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = li.inflate(R.layout.enter_password, null);
		alertDialogBuilder.setView(dialogView);
		
		final EditText pwdEntered = (EditText) dialogView
				.findViewById(R.id.pwd);
		
		alertDialogBuilder
				// set dialog message
				.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						String pwd = pwdEntered.getText().toString();
						if(pwd.equals(Utils.getPwd())){
							
							Intent intent = new Intent(MainActivity.this, VoucherUpload.class);
							startActivity(intent);
						}else{
							
							Toast.makeText(getApplicationContext(), getString(R.string.wrong_pwd),
									   Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public void scan() {
        logger.trace("scan()");
		if(Utils.isShopping(MainActivity.this)){
			(new IntentIntegrator(this)).initiateScan();
		}else{
			showChooseStoresD();
		}
	}

	public void onActivityResult(int request, int result, Intent i) {
		logger.trace("onActivityResult()");
		IntentResult scan = IntentIntegrator.parseActivityResult(request,
				result, i);

		if (scan != null && result == RESULT_OK) {
			Log.d(TAG, scan.getContents());
			logger.debug(" {}", scan.getContents());
			String b = scan.getContents();
			String barcode = Utils.removeZeros(b);
			Log.d(TAG, barcode);
			logger.debug(" {}", barcode);

			String[] projection = { ChewContract.Store._ID,
					ChewContract.Store.FOOD_NAME,
					ChewContract.Store.FOOD_CATEGORY,
					ChewContract.Store.VOUCHERS_ID, ChewContract.Store.SIZE,
					ChewContract.Store.SIZE_TYPE, ChewContract.Store.FOOD_TYPE };

			String selection = ChewContract.Store.BARCODE + "='" + barcode
					+ "'";

			CursorLoader loader = new CursorLoader(MainActivity.this,
					ChewContract.Store.CONTENT_URI, projection, selection,
					null, null);
			loader.registerListener(50, new MyOnLoadCompleteListener2());
			loader.startLoading(); 
		}
	}

	private void showChooseStoresD() {
		logger.trace("showChooseStoresD()");
		logger.info("Choosing between Stores like Walmart, Kroger, etc.");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
		final CharSequence[] stores = getResources().getStringArray(R.array.stores_array);
		alertDialogBuilder.setTitle(getString(R.string.which_store));
		alertDialogBuilder
				.setSingleChoiceItems(stores, 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								logger.trace("alertDialogBuilder.setSingleChoiceItems().onClick()");
								selectedStore = which;
							}
						})

				// set dialog message
				.setPositiveButton(getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								logger.trace("alertDialogBuilder.setPositiveButton().onClick()");
								logger.info(
										"Continuing to Store {} with id {}",
										selectedStore, id);
								// not the same as 'which' above
								logger.debug(
										"Which value = {}, Selected value = {}",
										id, selectedStore);
								Log.d(TAG, "Which value=" + id);
								Log.d(TAG, "Selected value=" + selectedStore);

								if (Utils.setStore(MainActivity.this,
										stores[selectedStore].toString()))
									MainActivity.this.getVouchers();
								else {
									logger.debug("error saving store in shared preferences");
									Log.d(TAG,
											"error saving store in shared preferences");
								}
							}
						})

				.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								logger.trace("alertDialogBuilder.setNegativeButton().onClick()");
								logger.info("Cancelled choice of Store id {}", id);
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void getVouchers() {
		logger.trace("getVouchers()");
		CursorLoader loader = null;
		String month = Utils.getMonth();

		String[] resultColumns = new String[] {
				ChewContract.FamilyVouchers._ID,
				ChewContract.FamilyVouchers.VOUCHER_CODE,
				ChewContract.FamilyVouchers.NAME};

		String where = ChewContract.FamilyVouchers.VOUCHER_MONTH + "='" + month
				+ "'" + " AND " + ChewContract.FamilyVouchers.USED + "='" + getString(R.string.not_used) + "'";;

		loader = new CursorLoader(MainActivity.this,
				ChewContract.FamilyVouchers.CONTENT_URI, resultColumns, where,
				null, null);
		loader.registerListener(1, new MyOnLoadCompleteListener());
		loader.startLoading();
	}

	ArrayList<String> selected;
	int selectedStore;

	private void showChooseVouchersD(final CharSequence[] voucherCodes) {
		logger.trace("showChooseVouchersD()");
		//boolean[] selections = new boolean[voucherCodes.length];
		selected = new ArrayList<String>();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
		alertDialogBuilder.setTitle(getString(R.string.which_vouchers));
		alertDialogBuilder
				.setMultiChoiceItems(voucherCodes, null,
						new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						logger.trace("showChooseVouchersD().alertDialogBuilder.setSingleMultiChoiceItems().onClick()");
						if (isChecked) {
							selected.add(voucherCodes[which].toString());
						}
					}
				}).setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						logger.trace("showChooseVouchersD().alertDialogBuilder.setPositiveButton().onClick()");
						Set<String> vouchersUsed = new HashSet<String>();
						
						for (String item : selected) {
							logger.info("Selected voucher {}", item);
							Log.d("Selected", item);
							logger.debug("Selected item {}", item);
							vouchersUsed.add(item);	
						} // save the vouchers
					
						if(Utils.setVouchers(MainActivity.this, vouchersUsed)){
							Utils.setShoppingStatus(MainActivity.this, true);
							Toast.makeText(getApplicationContext(), getString(R.string.ready_to_shop),
									   Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), getString(R.string.problem),
									   Toast.LENGTH_SHORT).show();
						}
					}
				}).setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int id) {
				logger.trace("showChooseVouchersD().alertDialogBuilder.setNegativeButton().onClick()");
				logger.info("Cancelled choice of vouchers");
				
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}
	
	private void done(){
		logger.trace("done()");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
		alertDialogBuilder.setTitle(getString(R.string.sure_done_shop_title))
		.setMessage(getString(R.string.sure_done_shop_msg))
		.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						logger.info("YES, Done Shopping");
 
						String where = ChewContract.FamilyVouchers.USED
								+ "='"
								+ getString(R.string.in_use)
								+ "'";
						
						ContentValues updateValues = new ContentValues();
						updateValues.put(ChewContract.FamilyVouchers.USED, getString(R.string.used)); 
						int rowsUpdate = getContentResolver().update(
								ChewContract.FamilyVouchers.CONTENT_URI,
								updateValues, where, null);
                        logger.debug("ROWSUPDATE {}", rowsUpdate);
						Utils.setShoppingStatus(MainActivity.this, false);
						Utils.showToast(MainActivity.this, getString(R.string.confirm_done_shop));
					}
				}).setNegativeButton(getString(R.string.no),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int id) {
				logger.info("Cancelled, Not Done Shopping as yet");
				
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
		
	}

	private class MyOnLoadCompleteListener implements
			OnLoadCompleteListener<Cursor> {
		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
			logger.trace("MyOnLoadCompleteListener.onLoadComplete()");
			Log.d(TAG, "on load complete called");
			Log.d(TAG, cursor.getCount() + " rows");
			logger.debug("on load complete called {} rows", cursor.getCount());
			CharSequence[] vcodes = new CharSequence[cursor.getCount()];

			while (cursor != null && cursor.moveToNext()) {
				Log.d(TAG, cursor.getString(1));
				Log.d(TAG, cursor.getString(2));
				logger.debug("{} {} ", cursor.getString(1), cursor.getString(2));
				vcodes[cursor.getPosition()] = cursor.getString(1) + " - " + cursor.getString(2);
			}

			showChooseVouchersD(vcodes);
		}
	}
	
	private class MyOnLoadCompleteListener2 implements OnLoadCompleteListener<Cursor>{

		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
			logger.trace("MyOnLoadCompleteListener2.onLoadComplete()");
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
				logger.debug("food_name {} food_category {} and ", food_name, food_category);
				logger.debug("vouchersID {} size {} and ", vouchersID, size);
				logger.debug("size_type {} food_type {} ", size_type, food_type);
				// set title
				alertDialogBuilder.setTitle(food_name);
	 
				// set dialog message
				alertDialogBuilder
						.setMessage(getString(R.string.want_to_get))
						.setCancelable(false)
						.setPositiveButton(getString(R.string.yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										logger.info(
												"Yes, Get Item for Family Member {} with",
												"");
										logger.info(
												"food_name {}, food_category {} and ",
												food_name, food_category);
										logger.info(
												"vouchersID {}, size {} and ",
												vouchersID, size);
										logger.info(
												"size_type {}, food_type {}",
												size_type, food_type);

										// if it is a CASH item, go to Produce
										// activity
										// also check if they are using cash vouchers 
										//nn
										if (food_category.equals("CASH")) {

											Intent intent = new Intent(
													MainActivity.this,
													Produce.class);
											intent.putExtra("food_name",
													food_name);

											startActivity(intent);
											
										} else {

											Intent intent = new Intent(
													MainActivity.this,
													GetProducts.class);
											intent.putExtra("member_name", "");
											intent.putExtra("food_name",
													food_name);
											intent.putExtra("food_category",
													food_category);
											intent.putExtra("vouchersID",
													vouchersID);
											intent.putExtra("size", size);
											intent.putExtra("size_type",
													size_type);
											intent.putExtra("food_type",
													food_type);

											startActivity(intent);
										}
									}
								})
						.setNegativeButton(getString(R.string.no),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										logger.info(
												"No, Do Not Get Item for Family Member {} with ",
												"");
										logger.info(
												"food_name {}, food_category {} and ",
												food_name, food_category);
										logger.info(
												"vouchersID {}, size {} and ",
												vouchersID, size);
										logger.info(
												"size_type {}, food_type {}",
												size_type, food_type);
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			} else {

				logger.info("Scanned an WIC unapproved Item");

				// set title
				alertDialogBuilder.setTitle(getString(R.string.cannot_get));

				// set dialog message
				alertDialogBuilder
						.setMessage(getString(R.string.select_wic_appr))
						.setCancelable(false)
						.setPositiveButton(getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
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
	
	public void onStop() {
		super.onStop();
		logger.trace("onStop()");
		Log.d(TAG, "onStop called");
		logger.debug("onStop called");
		images.recycle();
	}
	
	/*******Pankaj Chand's Functions*/
	
	//Options Menu
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		logger.trace("onCreateOptionsMenu()");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		logger.trace("onOptionsItemSelected()");
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == R.id.action_notification) {
			Intent intent = new Intent(MainActivity.this, ConfigurationActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
