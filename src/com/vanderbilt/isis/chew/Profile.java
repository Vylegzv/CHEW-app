package com.vanderbilt.isis.chew;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.utils.Utils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Profile extends Activity {

	public final String TAG = getClass().getSimpleName();
	
	TextView memberName;
	TextView whichVouchers;
	TextView whichMonth;
	String voucherCategoryId;
	String tagDescription;
	String foodTypeName;
	AlertDialog.Builder alertDialogBuilder2;
	AlertDialog.Builder noMoreDialog;
	AlertDialog.Builder otherOptionDialog;
	String name = "";
	String voucherCode = "";
	double tagSize;
	int ch = 0;
	boolean specialCase;
	String category;
	boolean combinationAllowed;
	boolean combinationItemsBoughtBefore;
	int substitute1 = 0;
	int substitute2 = 0;
	String foodTypeName1 = "";
	String foodTypeName2 = "";
	static Cursor myCursor = null;
	double minSizeAllowed;
	String sizeType = "";
	int quantityAllowed = 0, op2Quant = 0;
	boolean firstCursor = false;
	boolean alreadyBoughtOtherOption = false;
	String otherOptionType = "";
	String month_name = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		Bundle getName = getIntent().getExtras();
		if (getName != null) {
			name = getName.getString("name");
		}
		Log.d(TAG, "test: " + name);

		memberName = (TextView) findViewById(R.id.member_name);
		whichVouchers = (TextView) findViewById(R.id.which_vouchers);
		whichMonth = (TextView) findViewById(R.id.which_month);

		populateScreen(name);

		Button whatCanGet = (Button) findViewById(R.id.what_can_get);
		whatCanGet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent(Profile.this, WhatCanGet.class);
				 * intent.putExtra("voucherID", voucherCategoryId);
				 * startActivity(intent);
				 */
			}
		});

		Button shopForMember = (Button) findViewById(R.id.scan);
		shopForMember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				scan();
			}
		});

		Button already_added_regular = (Button) findViewById(R.id.already_added_regular);
		already_added_regular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Profile.this, InCartRegular.class);
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});
		
		Button already_added_cash = (Button) findViewById(R.id.already_added_cash);
		already_added_cash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Profile.this, InCartCash.class);
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});
	}
	
	public void scan() {
		(new IntentIntegrator(this)).initiateScan();
	}

	@SuppressLint("NewApi")
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
			
			//String store = Utils.getStore(Profile.this);
			
			// it could be either in one store or in both stores
			/*String selection = ChewContract.Store.BARCODE + "='" + barcode + "'"
					+ " AND " + ChewContract.Store.STORE + "='" + store + "'"
					+ " OR " + ChewContract.Store.BARCODE + "='" + barcode + "'"
					+ " AND " + ChewContract.Store.STORE + "='" + Utils.ALL_STORES + "'";*/
			
			String selection = ChewContract.Store.BARCODE + "='" + barcode + "'";
			
			CursorLoader loader = new CursorLoader(Profile.this, ChewContract.Store.CONTENT_URI, projection, selection, null, null);
			loader.registerListener(50, new MyOnLoadCompleteListener2());
			loader.startLoading();
		}
	}

	@SuppressLint("NewApi")
	private void populateScreen(String name) {

		memberName.setText(name);
		String month_name = Utils.getMonth();
		whichMonth.setText(month_name);
		
		CursorLoader loader = null;

		String[] resultColumns = new String[] {
				ChewContract.FamilyVouchers.VOUCHER_CODE,
				ChewContract.FamilyVouchers.USED };
		String where = ChewContract.FamilyVouchers.VOUCHER_MONTH + "='" + month_name + "'"
				+ " AND " + ChewContract.FamilyVouchers.NAME + "='" + name + "'";

		loader = new CursorLoader(Profile.this,
				ChewContract.FamilyVouchers.CONTENT_URI, resultColumns, where,
				null, null);
		loader.registerListener(50, new MyOnLoadCompleteListener());
		loader.startLoading();
	}

	private class MyOnLoadCompleteListener implements
			OnLoadCompleteListener<Cursor> {

		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
			
			StringBuffer vouchers = new StringBuffer();
			while (cursor != null && cursor.moveToNext()) {
				String voucher = cursor.getString(0);
				String used = cursor.getString(1);
				
				Log.d(TAG, voucher);
				Log.d(TAG, used);
				
				vouchers.append(voucher);
				vouchers.append(" (");
				vouchers.append(used);
				vouchers.append(")  ");
			}
			
			whichVouchers.setText(vouchers);
		}
	}
		
	private class MyOnLoadCompleteListener2 implements OnLoadCompleteListener<Cursor>{

		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);
			
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
					.setMessage(getString(R.string.want_to_get))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.yes),
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {

							Intent intent = new Intent(Profile.this, GetProducts.class);
							Log.d("Putting name", name);
							intent.putExtra("member_name", name);
							intent.putExtra("food_name", food_name);
							intent.putExtra("food_category", food_category);
							intent.putExtra("vouchersID", vouchersID);
							intent.putExtra("size", size);
							intent.putExtra("size_type", size_type);
							intent.putExtra("food_type", food_type);
							
							startActivity(intent);
						}
					  })
					.setNegativeButton(getString(R.string.no),
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
		}else{
			
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

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		}
	}
}
