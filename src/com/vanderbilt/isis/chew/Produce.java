package com.vanderbilt.isis.chew;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.dboperations.CustomHandler.InsertDataHandler;
import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.CashVoucher;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
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
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class Produce extends Activity {

	ArrayList<String> voucherNameArray = new ArrayList<String>();
	Map<String, Double> incrementsMap = new HashMap<String, Double>();
	SharedPreferences preferences;
	String voucherNameChosen = "";
	String produceName = "";
	String month_name = "";
	Integer selectedQuantity = 0;
	Map<String, CashVoucher> cashVouchers;
	LinearLayout ll;

	public final String TAG = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.produce);

		cashVouchers = Utils.getCashVouchers(Produce.this);
		initializeLayout();

		// "-", "1/4", "1/2", "3/4"
		incrementsMap.put("-", 1.00);
		incrementsMap.put("1/4", 0.25);
		incrementsMap.put("1/2", 0.5);
		incrementsMap.put("3/4", 0.75);

		populateNameVoucherArray();

	}

	private void initializeLayout() {

		ll = (LinearLayout) findViewById(R.id.lay1);
		DecimalFormat df = new DecimalFormat("0.00");

		for (Map.Entry<String, CashVoucher> entry : cashVouchers.entrySet()) {
			LinearLayout l = new LinearLayout(this);
			l.setTag(entry.getKey());
			l.setOrientation(LinearLayout.VERTICAL);
			LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			l.setLayoutParams(lparams);
			l.setPadding(10, 0, 0, 20);
			TextView tv = new TextView(this);
			tv.setText(entry.getKey());
			TextView tv1 = new TextView(this);
			
			double spent = entry.getValue().getAmountSpent(Produce.this);
			double left = entry.getValue().getAmountAllowed() - spent;
			
			tv1.setText(getString(R.string.left) + " " + df.format(left));
			tv1.setTag(1);
			TextView tv2 = new TextView(this);
			tv2.setText(getString(R.string.spent) + " " + df.format(spent));
			tv2.setTag(2);
			l.addView(tv);
			l.addView(tv1);
			l.addView(tv2);
			ll.addView(l);
		}
	}

	public void calcPackagedFresh(View view) {
		callScanner();

	}

	public void calcPackagedFrozen(View view) {
		callScanner();
	}

	private void callScanner() {
		(new IntentIntegrator(this)).initiateScan();
	}

	public void calcPricePerItem(View view) {
		enterPrice("");
	}

	public void onActivityResult(int request, int result, Intent i) {
		IntentResult scan = IntentIntegrator.parseActivityResult(request,
				result, i);

		if (scan != null && result == RESULT_OK) {
			Log.d("TEST", "called");
			String b = scan.getContents();
			String barcode = Utils.removeZeros(b);

			// resolver = getContentResolver();
			CursorLoader loader = null;

			String[] resultColumns = new String[] { ChewContract.Store._ID,
					ChewContract.Store.FOOD_NAME,
					ChewContract.Store.FOOD_CATEGORY,
					ChewContract.Store.FOOD_TYPE };

			String store = Utils.getStore(Produce.this);

			// it could be either in one store or in both stores
			String where = ChewContract.Store.BARCODE + "='" + barcode + "'"
					+ " AND " + ChewContract.Store.STORE + "='" + store + "'"
					+ " OR " + ChewContract.Store.BARCODE + "='" + barcode
					+ "'" + " AND " + ChewContract.Store.STORE + "='"
					+ Utils.ALL_STORES + "'";

			loader = new CursorLoader(Produce.this,
					ChewContract.Store.CONTENT_URI, resultColumns, where, null,
					null);
			loader.registerListener(10, new MyOnLoadCompleteListener());
			loader.startLoading();
		} else if (result == RESULT_CANCELED) {
			// Handle cancel
			Log.i(TAG, "Cancelled");
		}

	}

	private class MyOnLoadCompleteListener implements
			OnLoadCompleteListener<Cursor> {

		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {

			Log.d("ONLOADCOMPLETE", "onLoadComplete called");

			// also check if it is CASH
			if (cursor != null && cursor.moveToFirst()) {
				String tagDescription = cursor.getString(1);
				final String category = cursor.getString(2);
				final String foodTypeName = cursor.getString(3);
				Log.d("ONLOADCOMPLETE", tagDescription);
				Log.d("ONLOADCOMPLETE", category);
				Log.d("ONLOADCOMPLETE", foodTypeName);
				enterPrice(tagDescription);
			}
		}
	}

	public void calcPricePerPound(View view) {

		AlertDialog.Builder alert = new AlertDialog.Builder(Produce.this);
		alert.setTitle(getString(R.string.calc_price));

		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = li.inflate(R.layout.produce_wheel, null);
		alert.setView(dialogView);

		Spinner spinnercategory = (Spinner) dialogView
				.findViewById(R.id.viewSpin);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Produce.this,
				android.R.layout.simple_spinner_item, voucherNameArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnercategory.setAdapter(adapter);

		spinnercategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				voucherNameChosen = parent.getSelectedItem().toString();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		final WheelView poundsWheel = (WheelView) dialogView
				.findViewById(R.id.pounds);
		final WheelView incrementsWheel = (WheelView) dialogView
				.findViewById(R.id.increments);

		final Integer poundNums[] = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9,
				10 };
		final ArrayWheelAdapter<Integer> poundsWheelAdapter = new ArrayWheelAdapter<Integer>(
				this, poundNums);
		poundsWheel.setViewAdapter(poundsWheelAdapter);

		// have a map to map strings to doubles: 0.0, 0.25, 0.5, 0.75
		final String incrementNums[] = new String[] { "-", "1/4", "1/2", "3/4" };
		final ArrayWheelAdapter<String> incrWheelAdapter = new ArrayWheelAdapter<String>(
				this, incrementNums);
		incrementsWheel.setViewAdapter(incrWheelAdapter);

		final EditText produceEntered = (EditText) dialogView
				.findViewById(R.id.produceName);
		final EditText priceEntered = (EditText) dialogView
				.findViewById(R.id.priceEntered);

		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				String produce = produceEntered.getText().toString();
				double price = Double.parseDouble(priceEntered.getText()
						.toString());
				Log.d("SELECTED", voucherNameChosen);
				Log.d("PRODUCEEENTERED", produce);
				Log.d("PRICEENTERED", price + "");

				Integer pound = poundNums[poundsWheel.getCurrentItem()];
				String incrKey = incrementNums[incrementsWheel.getCurrentItem()];
				double incr = incrementsMap.get(incrKey);
				price = price * incr * pound;

				Log.d(TAG, price + "");

				DecimalFormat df = new DecimalFormat("0.00");

				double totalSpentForPerson = cashVouchers.get(voucherNameChosen).getAmountSpent(Produce.this);
				double totalAllowedForPerson = cashVouchers.get(voucherNameChosen).getAmountAllowed();

				Log.d("TOTALSPENTFORPERSON", totalSpentForPerson + "");
				Log.d("TOTALALLOWEDFORPERSON", totalAllowedForPerson + "");

				if (totalSpentForPerson + price <= totalAllowedForPerson) {

					InsertDataHandler myInsertHandler = new InsertDataHandler(
							Produce.this);

					ContentValues newValues = new ContentValues();

					newValues.put(ChewContract.ProduceChosen.PRODUCE_NAME,
							produce);
					newValues.put(ChewContract.ProduceChosen.COST,
							df.format(price));
					newValues.put(ChewContract.ProduceChosen.MONTH, Utils.getMonth());
					newValues.put(ChewContract.ProduceChosen.VOUCHER_CODE,
							voucherNameChosen.split(" - ")[0]);
					newValues.put(ChewContract.ProductsChosen.MEMBER_NAME,
							voucherNameChosen.split(" - ")[1]);

					myInsertHandler.startInsert(0, 0,
							ChewContract.ProduceChosen.CONTENT_URI, newValues);
					
					totalSpentForPerson = totalSpentForPerson + price;
					
					LinearLayout l = (LinearLayout) ll.findViewWithTag(voucherNameChosen);
					TextView left = (TextView) l.findViewWithTag(1);
					left.setText(getString(R.string.left) + " " + df.format((totalAllowedForPerson - totalSpentForPerson)));
					TextView spent = (TextView) l.findViewWithTag(2);
					spent.setText(getString(R.string.spent) + " " + df.format(totalSpentForPerson));

				} else {
					Log.d("PRODUCE", "over the limit");
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							Produce.this);
					// set title
					alertDialogBuilder.setTitle(getString(R.string.cost_limit)
							+ " " + voucherNameChosen + "!");

					// set dialog message
					alertDialogBuilder.setCancelable(false).setPositiveButton(
							getString(R.string.ok), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
			}

		});

		alert.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Log.d(TAG, poundNums[poundsWheel.getCurrentItem()] + "");
						String s = incrementNums[incrementsWheel
								.getCurrentItem()];
						Log.d(TAG, s);
						double test = incrementsMap.get(s);
						Log.d(TAG, test + "");
					}
				});

		AlertDialog alertdialog = alert.create();
		alertdialog.show();
	}

	private void enterPrice(String produceName) {

		AlertDialog.Builder alert = new AlertDialog.Builder(Produce.this);
		alert.setTitle(getString(R.string.enter_price));

		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = li.inflate(R.layout.enter_price_dialog, null);
		alert.setView(dialogView);

		Spinner spinnercategory = (Spinner) dialogView
				.findViewById(R.id.viewSpin);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Produce.this,
				android.R.layout.simple_spinner_item, voucherNameArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnercategory.setAdapter(adapter);

		ArrayList<Integer> test = new ArrayList<Integer>();
		test.add(1);
		test.add(2);
		test.add(3);
		test.add(4);
		test.add(5);

		Spinner quantity = (Spinner) dialogView
				.findViewById(R.id.quantity_selected);
		ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<Integer>(
				Produce.this, android.R.layout.simple_spinner_item, test);
		quantityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		quantity.setAdapter(quantityAdapter);

		spinnercategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				voucherNameChosen = parent.getSelectedItem().toString();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		quantity.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {

				selectedQuantity = (Integer) parent.getSelectedItem();
				Log.d(TAG, selectedQuantity + "");
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		final EditText produceEntered = (EditText) dialogView
				.findViewById(R.id.produceEntered);
		produceEntered.setText(produceName);
		final EditText priceEntered = (EditText) dialogView
				.findViewById(R.id.priceEntered);

		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				String produce = produceEntered.getText().toString();
				double price = Double.parseDouble(priceEntered.getText()
						.toString());
				Log.d("SELECTED", voucherNameChosen);
				Log.d("PRODUCEEENTERED", produce);
				Log.d("PRICEENTERED", price + "");
				Log.d("QUANTITYSELECTED", selectedQuantity + "");
				price = price * selectedQuantity;
				Log.d("AFTERMULTIPLYING", price + "");

				DecimalFormat df = new DecimalFormat("0.00");

				double totalSpentForPerson = cashVouchers.get(voucherNameChosen).getAmountSpent(Produce.this);
				double totalAllowedForPerson = cashVouchers.get(voucherNameChosen).getAmountAllowed();
				
				Log.d("TOTALSPENTFORPERSON", totalSpentForPerson + "");
				Log.d("TOTALALLOWEDFORPERSON", totalAllowedForPerson + "");

				if (totalSpentForPerson + price <= totalAllowedForPerson) {

					InsertDataHandler myInsertHandler = new InsertDataHandler(
							Produce.this);

					ContentValues newValues = new ContentValues();

					newValues.put(ChewContract.ProduceChosen.PRODUCE_NAME,
							produce);
					newValues.put(ChewContract.ProduceChosen.COST,
							df.format(price));
					newValues.put(ChewContract.ProduceChosen.MONTH, Utils.getMonth());
					newValues.put(ChewContract.ProduceChosen.VOUCHER_CODE,
							voucherNameChosen.split(" - ")[0]);
					newValues.put(ChewContract.ProduceChosen.MEMBER_NAME,
							voucherNameChosen.split(" - ")[1]);

					myInsertHandler.startInsert(0, 0,
							ChewContract.ProduceChosen.CONTENT_URI, newValues);
					
					totalSpentForPerson = totalSpentForPerson + price;

					LinearLayout l = (LinearLayout) ll.findViewWithTag(voucherNameChosen);
					TextView left = (TextView) l.findViewWithTag(1);
					left.setText(getString(R.string.left) + " "+ df.format((totalAllowedForPerson - totalSpentForPerson)));
					TextView spent = (TextView) l.findViewWithTag(2);
					spent.setText(getString(R.string.spent) + " "+ df.format(totalSpentForPerson));

				} else {
					Log.d("PRODUCE", "over the limit");
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							Produce.this);
					// set title
					alertDialogBuilder.setTitle(getString(R.string.cost_limit)
							+ " " + voucherNameChosen + "!");
					alertDialogBuilder.setCancelable(false).setPositiveButton(
							getString(R.string.ok), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
			}

		});

		alert.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		AlertDialog alertdialog = alert.create();
		alertdialog.show();
	}

	public void populateNameVoucherArray() {

		voucherNameArray.addAll(cashVouchers.keySet());
	}

	public void getMemberNames(ArrayList<String> a) {

		Set<String> memberNamesSet = preferences.getStringSet("memberNames",
				null);

		if (memberNamesSet != null) {
			a.addAll(memberNamesSet);
		}
	}

	public double getTotalAllowed() {

		double allowed = 0;
		for (Map.Entry<String, CashVoucher> entry : cashVouchers.entrySet()) {

			allowed = allowed + entry.getValue().getAmountAllowed();
		}
		return allowed;
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Log.d("Produce", "onResume called");
		cashVouchers = Utils.getCashVouchers(Produce.this);
	}
}