package com.vanderbilt.isis.chew;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.vanderbilt.isis.chew.db.ChewContract;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class InCartRegular extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private MySimpleCursorAdapter mAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;
	String name;
	AlertDialog.Builder deleteOptionsDialog;
	int deleteQuantity = 0;
	int deleteCount = 0;
	String where = "";
	String productName = "";
	//String voucherCode = "";
	String month_name = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.testlistview);

		name = "";
		//voucherCode = "";
		Bundle getName = getIntent().getExtras();
		if (getName != null) {
			name = getName.getString("name");
			//voucherCode = getName.getString("voucherCode");
		}

		Log.d("NAME", name);
		//Log.d("VCODE", voucherCode);

		loadermanager = getLoaderManager();

		int[] uiBindTo = { android.R.id.text1, android.R.id.text2 };

		/*
		 * mAdapter = new SimpleCursorAdapter(InCart.this,
		 * android.R.layout.simple_list_item_2, null, new String[] {
		 * OverallContentProvider.ProductsChosen.PRODUCT_NAME,
		 * OverallContentProvider.ProductsChosen.QUANTITY}, uiBindTo, 0);
		 */

		mAdapter = new MySimpleCursorAdapter(InCartRegular.this,
				R.layout.in_cart_listview, null, new String[] {
						ChewContract.ProductsChosen.PRODUCT_NAME,
						ChewContract.ProductsChosen.QUANTITY }, null, 0);

		setListAdapter(mAdapter);
		loadermanager.initLoader(1, null, this);

		ListView listview = getListView();

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final Cursor c = ((SimpleCursorAdapter) parent.getAdapter())
						.getCursor();
				c.moveToPosition(position);
				Log.d("CLICK", c.getString(0) + " clicked");
				Log.d("CLICK", c.getString(1) + " clicked");
				Log.d("CLICK", c.getString(2) + " clicked");

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						InCartRegular.this);

				// set title
				alertDialogBuilder.setTitle("Do You Want To Delete This Item?");

				// set dialog message
				alertDialogBuilder
						// .setMessage("Please connect to Internet")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										Calendar cal = Calendar.getInstance();
										SimpleDateFormat month_date = new SimpleDateFormat(
												"MMMM");
										month_name = month_date.format(cal
												.getTime());

										Log.d("C", c.getString(0));
										Log.d("C", c.getString(1));
										Log.d("QUANT", c.getString(2));
										Log.d("OZ", c.getString(3));
										Log.d("Count", c.getString(4));
										//Log.d("Cost", c.getString(6));

										where = "";
										productName = c.getString(1);
										final int quantity = Integer.parseInt(c
												.getString(2));
										final double ounces = Double
												.parseDouble(c.getString(3));
										final int count = Integer.parseInt(c
												.getString(4));
										deleteQuantity = 0;
										deleteCount = 0;

										// if quantity is 1 or -1, just delete
										// it
										if (quantity == 1 || quantity == -1) {
											where = ChewContract.ProductsChosen.PRODUCT_NAME
													+ "='"
													+ productName
													+ "'"
													+ " AND "
													+ ChewContract.ProductsChosen.MEMBER_NAME
													+ "='"
													+ name
													+ "'"
													+ " AND "
													//+ ChewContract.ProductsChosen.VOUCHER_CODE
													//+ "='"
													//+ voucherCode
													//+ "'"
													//+ " AND "
													+ ChewContract.ProductsChosen.MONTH
													+ "='" + month_name + "'";

											int numDeleted = getContentResolver()
													.delete(ChewContract.ProductsChosen.CONTENT_URI,
															where, null);
											// if quantity is more than 1, ask
											// them how many they want to delete
										} else if (quantity > 1) {

											final String[] choices = new String[quantity];
											for (int i = 0; i < quantity; ++i) {

												choices[i] = i + 1 + "";
											}

											deleteOptionsDialog = new AlertDialog.Builder(
													InCartRegular.this)
													.setTitle(
															"Please Select Quantity to Delete")
													.setSingleChoiceItems(
															choices,
															-1,
															new DialogInterface.OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {

																	deleteQuantity = Integer
																			.parseInt(choices[which]);
																}
															})
													.setPositiveButton(
															"Yes",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {

																	Log.d("deleteQuantity",
																			deleteQuantity
																					+ "");
																	// if
																	// quantity
																	// to delete
																	// =
																	// quantity
																	// bought,
																	// delete
																	if (deleteQuantity == quantity) {

																		where = ChewContract.ProductsChosen.PRODUCT_NAME
																				+ "='"
																				+ productName
																				+ "'"
																				+ " AND "
																				+ ChewContract.ProductsChosen.MEMBER_NAME
																				+ "='"
																				+ name
																				+ "'"
																				+ " AND "
																				//+ ChewContract.ProductsChosen.VOUCHER_CODE
																				//+ "='"
																				//+ voucherCode
																				//+ "'"
																				//+ " AND "
																				+ ChewContract.ProductsChosen.MONTH
																				+ "='"
																				+ month_name
																				+ "'";
																		;
																		int numDeleted = getContentResolver()
																				.delete(ChewContract.ProductsChosen.CONTENT_URI,
																						where,
																						null);
																		loadermanager
																				.restartLoader(
																						1,
																						null,
																						InCartRegular.this);
																		// else
																		// update
																		// row
																		// in
																		// database
																	} else if (deleteQuantity != 0
																			&& deleteQuantity < quantity) {
																		Log.d("INSIDIE ELSE",
																				"inside else");
																		ContentValues updateValues = new ContentValues();
																		int rowsUpdate = 0;
																		updateValues
																				.put(ChewContract.ProductsChosen.QUANTITY,
																						quantity
																								- deleteQuantity);
																		updateValues
																				.put(ChewContract.ProductsChosen.COUNT,
																						quantity
																								- deleteQuantity);

																		where = ChewContract.ProductsChosen.PRODUCT_NAME
																				+ "='"
																				+ productName
																				+ "'"
																				+ " AND "
																				+ ChewContract.ProductsChosen.MEMBER_NAME
																				+ "='"
																				+ name
																				+ "'"
																				+ " AND "
																				//+ ChewContract.ProductsChosen.VOUCHER_CODE
																				//+ "='"
																				//+ voucherCode
																				//+ "'"
																				//+ " AND "
																				+ ChewContract.ProductsChosen.MONTH
																				+ "='"
																				+ month_name
																				+ "'";

																		rowsUpdate = getContentResolver()
																				.update(ChewContract.ProductsChosen.CONTENT_URI,
																						updateValues,
																						where,
																						null);
																		Log.d("ROWSUPDATE",
																				rowsUpdate
																						+ "");

																		loadermanager
																				.restartLoader(
																						1,
																						null,
																						InCartRegular.this);
																	}

																}
															})
													.setNegativeButton(
															"No",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {
																	dialog.cancel();
																}
															});

											AlertDialog alertDialog2 = deleteOptionsDialog
													.create();

											// show it
											alertDialog2.show();
											// if "quantity" is 0, which means
											// we are dealing with oz
										} else {

											// if count is 1, just delete it
											if (count == 1) {
												where = ChewContract.ProductsChosen.PRODUCT_NAME
														+ "='"
														+ productName
														+ "'"
														+ " AND "
														+ ChewContract.ProductsChosen.MEMBER_NAME
														+ "='"
														+ name
														+ "'"
														+ " AND "
														//+ ChewContract.ProductsChosen.VOUCHER_CODE
														//+ "='"
														//+ voucherCode
														//+ "'"
														//+ " AND "
														+ ChewContract.ProductsChosen.MONTH
														+ "='"
														+ month_name
														+ "'";

												int numDeleted = getContentResolver()
														.delete(ChewContract.ProductsChosen.CONTENT_URI,
																where, null);
												// if quantity is more than 1,
												// ask them how many they want
												// to delete
											} else if (count > 1) {

												final String[] choices = new String[count];
												for (int i = 0; i < count; ++i) {

													choices[i] = i + 1 + "";
												}

												deleteOptionsDialog = new AlertDialog.Builder(
														InCartRegular.this)
														.setTitle(
																"Please Select Quantity to Delete")
														.setSingleChoiceItems(
																choices,
																-1,
																new DialogInterface.OnClickListener() {
																	@Override
																	public void onClick(
																			DialogInterface dialog,
																			int which) {

																		deleteCount = Integer
																				.parseInt(choices[which]);
																	}
																})
														.setPositiveButton(
																"Yes",
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int id) {

																		Log.d("deleteQuantity",
																				deleteQuantity
																						+ "");
																		// if
																		// quantity
																		// to
																		// delete
																		// =
																		// quantity
																		// bought,
																		// delete
																		if (deleteCount == count) {

																			where = ChewContract.ProductsChosen.PRODUCT_NAME
																					+ "='"
																					+ productName
																					+ "'"
																					+ " AND "
																					+ ChewContract.ProductsChosen.MEMBER_NAME
																					+ "='"
																					+ name
																					+ "'"
																					+ " AND "
																					//+ ChewContract.ProductsChosen.VOUCHER_CODE
																					//+ "='"
																					//+ voucherCode
																					//+ "'"
																					//+ " AND "
																					+ ChewContract.ProductsChosen.MONTH
																					+ "='"
																					+ month_name
																					+ "'";
																			;
																			int numDeleted = getContentResolver()
																					.delete(ChewContract.ProductsChosen.CONTENT_URI,
																							where,
																							null);
																			loadermanager
																					.restartLoader(
																							1,
																							null,
																							InCartRegular.this);
																			// else
																			// update
																			// row
																			// in
																			// database
																		} else if (deleteCount != 0
																				&& deleteCount < count) {
																			Log.d("INSIDIE ELSE",
																					"inside else");

																			ContentValues updateValues = new ContentValues();
																			int rowsUpdate = 0;
																			Log.d("NEWOZ",
																					ounces
																							/ (deleteCount + 1.0)
																							+ "");

																			updateValues
																					.put(ChewContract.ProductsChosen.SIZE_NUM,
																							ounces
																									/ (deleteCount + 1.0));
																			updateValues
																					.put(ChewContract.ProductsChosen.COUNT,
																							count
																									- deleteCount);

																			where = ChewContract.ProductsChosen.PRODUCT_NAME
																					+ "='"
																					+ productName
																					+ "'"
																					+ " AND "
																					+ ChewContract.ProductsChosen.MEMBER_NAME
																					+ "='"
																					+ name
																					+ "'"
																					+ " AND "
																					//+ ChewContract.ProductsChosen.VOUCHER_CODE
																					//+ "='"
																					//+ voucherCode
																					//+ "'"
																					//+ " AND "
																					+ ChewContract.ProductsChosen.MONTH
																					+ "='"
																					+ month_name
																					+ "'";

																			rowsUpdate = getContentResolver()
																					.update(ChewContract.ProductsChosen.CONTENT_URI,
																							updateValues,
																							where,
																							null);
																			Log.d("ROWSUPDATE",
																					rowsUpdate
																							+ "");

																			loadermanager
																					.restartLoader(
																							1,
																							null,
																							InCartRegular.this);
																		}

																	}
																})
														.setNegativeButton(
																"No",
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int id) {
																		dialog.cancel();
																	}
																});

												AlertDialog alertDialog2 = deleteOptionsDialog
														.create();

												// show it
												alertDialog2.show();
											}
										}

										/*
										 * String where =
										 * OverallContentProvider.
										 * ProductsChosen.PRODUCT_NAME + "='" +
										 * c.getString(1) + "'" + " AND " +
										 * OverallContentProvider
										 * .ProductsChosen.NAME + "='" + name +
										 * "'"; ; int numDeleted =
										 * getContentResolver().delete(
										 * OverallContentProvider
										 * .ProductsChosen.CONTENT_URI8, where,
										 * null); // c.requery(); //
										 * mAdapter.notifyDataSetChanged();
										 * loadermanager.restartLoader(1, null,
										 * InCart.this);
										 */

										// mAdapter.notifyDataSetChanged();
										loadermanager.restartLoader(2, null,
												InCartRegular.this);

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		});
	}

	/*
	 * public OnItemClickListener listener = new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { // TODO Auto-generated method stub Log.d("CLICK",
	 * mAdapter.getItem(position)+""); } };
	 */

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		String[] projection = new String[] { ChewContract.ProductsChosen._ID,
				ChewContract.ProductsChosen.PRODUCT_NAME,
				ChewContract.ProductsChosen.QUANTITY,
				ChewContract.ProductsChosen.SIZE_NUM,
				ChewContract.ProductsChosen.COUNT,
				ChewContract.ProductsChosen.PRODUCT_CATEGORY,
				ChewContract.ProductsChosen.VOUCHER_CODE };

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
		String month_name = month_date.format(cal.getTime());
		Log.d("MONTH INCART", month_name);

		/*String where = ChewContract.ProductsChosen.MONTH + "='" + month_name
				+ "'" + " AND " + ChewContract.ProductsChosen.MEMBER_NAME
				+ "='" + name + "'" + " AND "
				+ ChewContract.ProductsChosen.VOUCHER_CODE + "='" + voucherCode
				+ "'" + " AND " + ChewContract.ProductsChosen.VOUCHER_CODE
				+ "='" + voucherCode + "'";*/
		
		String where = ChewContract.ProductsChosen.MONTH + "='" + month_name
				+ "'" + " AND " + ChewContract.ProductsChosen.MEMBER_NAME
				+ "='" + name + "'";

		String sortOrder = ChewContract.ProductsChosen.PRODUCT_CATEGORY
				+ " ASC";

		CursorLoader loader = new CursorLoader(InCartRegular.this,
				ChewContract.ProductsChosen.CONTENT_URI, projection, where,
				null, sortOrder);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		// mAdapter.swapCursor(cursor);
		mAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		// mAdapter.swapCursor(null);
		mAdapter.changeCursor(null);
	}

	private class MySimpleCursorAdapter extends SimpleCursorAdapter {

		private static final int STATE_UNKNOWN = 0;
		private static final int STATE_SECTIONED_CELL = 1;
		private static final int STATE_REGULAR_CELL = 2;
		// private static final int STATE_QUANTITY_CELL = 2;
		// private static final int STATE_OUNCES_CELL = 3;

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

			// Log.d("test",
			// cursor.getString(cursor.getColumnIndex(ChewContract.ProductsChosen.COST)));

			ViewHolder holder = new ViewHolder();
			View v = null;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// if dealing with quantity
			if ((Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(ChewContract.ProductsChosen.QUANTITY))) > 0)
			// && (Double
			// .parseDouble(cursor.getString(cursor
			// .getColumnIndex(ChewContract.ProductsChosen.COST))) == -1.0)
			) {

				v = inflater.inflate(R.layout.in_cart_quantity, null);
				holder.separator = (TextView) v.findViewById(R.id.separator);
				holder.textViewOther = (TextView) v
						.findViewById(R.id.productQuantity);
				holder.textViewName = (TextView) v
						.findViewById(R.id.productName1);
				// dealing with ounces
			} else if (Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(ChewContract.ProductsChosen.QUANTITY))) == 0) {

				v = inflater.inflate(R.layout.in_cart_ounces, null);
				holder.separator = (TextView) v.findViewById(R.id.separator);
				holder.textViewOther = (TextView) v
						.findViewById(R.id.productOunces);
				holder.insertOz = (TextView) v.findViewById(R.id.insertOz);
				holder.textViewName = (TextView) v
						.findViewById(R.id.productName2);
				// dealing with produce
			} else {

				Log.d("IN ELSE", "COST");
				/*
				 * v = inflater.inflate(R.layout.in_cart_cost, null);
				 * holder.separator = (TextView) v.findViewById(R.id.separator);
				 * holder.textViewOther = (TextView) v
				 * .findViewById(R.id.productCost); holder.insertDollar =
				 * (TextView) v .findViewById(R.id.insertDollar);
				 * holder.textViewName = (TextView) v
				 * .findViewById(R.id.productName3);
				 */
			}
			v.setTag(holder);

			return v;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {

			Log.d("BINDVIEW", "called");

			ViewHolder holder = (ViewHolder) view.getTag();

			boolean needSeparator = false;
			final int position = cursor.getPosition();
			final String category = cursor
					.getString(cursor
							.getColumnIndex(ChewContract.ProductsChosen.PRODUCT_CATEGORY));

			if (position == 0) {
				needSeparator = true;
			} else {
				cursor.moveToPosition(position - 1);
				String category1 = cursor
						.getString(cursor
								.getColumnIndex(ChewContract.ProductsChosen.PRODUCT_CATEGORY));
				if (!category1.contains(category)) {
					needSeparator = true;
				} else {
					needSeparator = false;
				}
				cursor.moveToPosition(position);
			}

			// if dealing with quantity
			if ((Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(ChewContract.ProductsChosen.QUANTITY))) > 0)
			// && (Double
			// .parseDouble(cursor.getString(cursor
			// .getColumnIndex(ChewContract.ProductsChosen.COST))) == -1.0)
			) {

				if (needSeparator) {
					String cat = cursor
							.getString(cursor
									.getColumnIndex(ChewContract.ProductsChosen.PRODUCT_CATEGORY));
					if (cat.contains("FRUIT_VEG")) {
						cat = "FRUITS & VEGETABLES";
					}
					holder.separator.setText(cat);
					holder.separator.setTextColor(getResources().getColor(
							R.color.greySeparator));
					holder.separator.setVisibility(View.VISIBLE);
				} else {
					holder.separator.setVisibility(View.GONE);
				}

				holder.textViewName
						.setText(cursor.getString(cursor
								.getColumnIndex(ChewContract.ProductsChosen.PRODUCT_NAME)));

				holder.textViewOther.setText(cursor.getString(cursor
						.getColumnIndex(ChewContract.ProductsChosen.QUANTITY)));
				// dealing with ounces
			} else if (Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(ChewContract.ProductsChosen.QUANTITY))) == 0) {

				if (needSeparator) {
					String cat = cursor
							.getString(cursor
									.getColumnIndex(ChewContract.ProductsChosen.PRODUCT_CATEGORY));
					if (cat.contains("FRUIT_VEG")) {
						cat = "FRUITS & VEGETABLES";
					}
					holder.separator.setText(cat);
					holder.separator.setTextColor(getResources().getColor(
							R.color.greySeparator));
					holder.separator.setVisibility(View.VISIBLE);
				} else {
					holder.separator.setVisibility(View.GONE);
				}

				holder.textViewName
						.setText(cursor.getString(cursor
								.getColumnIndex(ChewContract.ProductsChosen.PRODUCT_NAME)));

				holder.textViewOther.setText(cursor.getString(cursor
						.getColumnIndex(ChewContract.ProductsChosen.SIZE_NUM)));

			} else {
				Log.d("DEALING with PRODICE", "");

				// dealing with produce
				if (needSeparator) {
					String cat = cursor
							.getString(cursor
									.getColumnIndex(ChewContract.ProductsChosen.PRODUCT_CATEGORY));
					if (cat.contains("CASH")) {
						cat = "CASH VOUCHER";
					}
					holder.separator.setText(cat);
					holder.separator.setTextColor(getResources().getColor(
							R.color.greySeparator));
					holder.separator.setVisibility(View.VISIBLE);
				} else {
					holder.separator.setVisibility(View.GONE);
				}

				holder.textViewName
						.setText(cursor.getString(cursor
								.getColumnIndex(ChewContract.ProductsChosen.PRODUCT_NAME)));

				// holder.textViewOther
				// .setText(cursor.getString(cursor
				// .getColumnIndex(ChewContract.ProductsChosen.COST)));
			}
		}

		/*
		 * @Override public void bindView(View view, Context context, Cursor
		 * cursor) {
		 * 
		 * Log.d("BINDVIEW", "called");
		 * 
		 * ViewHolder holder = (ViewHolder) view.getTag();
		 * 
		 * boolean needSeparator = false; final int position =
		 * cursor.getPosition(); final String category = cursor.getString(cursor
		 * .
		 * getColumnIndex(OverallContentProvider.ProductsChosen.PRODUCT_CATEGORY
		 * ));
		 * 
		 * if(position == 0){ needSeparator = true; }else{
		 * cursor.moveToPosition(position - 1); String category1 =
		 * cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider.ProductsChosen
		 * .PRODUCT_CATEGORY)); if(!category1.contains(category)){ needSeparator
		 * = true; }else{ needSeparator = false; }
		 * cursor.moveToPosition(position); }
		 * 
		 * // if dealing with quantity if
		 * (Integer.parseInt(cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider.ProductsChosen.QUANTITY))) >
		 * 0) {
		 * 
		 * if(needSeparator){ String cat = cursor.getString(cursor
		 * .getColumnIndex
		 * (OverallContentProvider.ProductsChosen.PRODUCT_CATEGORY));
		 * if(cat.contains("FRUIT_VEG")){ cat = "FRUITS & VEGETABLES"; }
		 * holder.separator.setText(cat);
		 * holder.separator.setTextColor(getResources
		 * ().getColor(R.color.greySeparator));
		 * holder.separator.setVisibility(View.VISIBLE); }else{
		 * holder.separator.setVisibility(View.GONE); }
		 * 
		 * holder.textViewName .setText(cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider
		 * .ProductsChosen.PRODUCT_NAME)));
		 * 
		 * holder.textViewOther.setText(cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider .ProductsChosen.QUANTITY)));
		 * // dealing with ounces } else
		 * if(Integer.parseInt(cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider.ProductsChosen.QUANTITY))) ==
		 * 0){
		 * 
		 * if(needSeparator){ String cat = cursor.getString(cursor
		 * .getColumnIndex
		 * (OverallContentProvider.ProductsChosen.PRODUCT_CATEGORY));
		 * if(cat.contains("FRUIT_VEG")){ cat = "FRUITS & VEGETABLES"; }
		 * holder.separator.setText(cat);
		 * holder.separator.setTextColor(getResources
		 * ().getColor(R.color.greySeparator));
		 * holder.separator.setVisibility(View.VISIBLE); }else{
		 * holder.separator.setVisibility(View.GONE); }
		 * 
		 * holder.textViewName .setText(cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider
		 * .ProductsChosen.PRODUCT_NAME)));
		 * 
		 * holder.textViewOther.setText(cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider
		 * .ProductsChosen.SIZE_NUMBER)));
		 * 
		 * }else{ // dealing with produce if(needSeparator){ String cat =
		 * cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider.ProductsChosen
		 * .PRODUCT_CATEGORY)); if(cat.contains("CASH")){ cat = "CASH VOUCHER";
		 * } holder.separator.setText(cat);
		 * holder.separator.setTextColor(getResources
		 * ().getColor(R.color.greySeparator));
		 * holder.separator.setVisibility(View.VISIBLE); }else{
		 * holder.separator.setVisibility(View.GONE); }
		 * 
		 * holder.textViewName .setText(cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider
		 * .ProductsChosen.PRODUCT_NAME)));
		 * 
		 * holder.textViewOther.setText(cursor.getString(cursor
		 * .getColumnIndex(OverallContentProvider .ProductsChosen.COST))); } }
		 */
	}

	public static class ViewHolder {
		public TextView textViewName;
		public TextView textViewOther;
		public TextView insertOz;
		public TextView insertDollar;
		public TextView separator;
	}
}
