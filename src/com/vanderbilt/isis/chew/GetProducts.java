package com.vanderbilt.isis.chew;

import java.util.ArrayList;
import java.util.Set;
import com.vanderbilt.isis.chew.adapters.InteractiveArrayAdapter;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.model.CheckBoxRowModel;
import com.vanderbilt.isis.chew.utils.Utils;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GetProducts extends ListActivity {

	ArrayList<CheckBoxRowModel> list = new ArrayList<CheckBoxRowModel>();
	ArrayAdapter<CheckBoxRowModel> adapter;
	TextView quantityN;
	Button ok;
	ListView listview;
	Set<String> vouchers;

	int vouchersID = -1;
	double sizeNo = -1.0;
	String foodName = "", foodCategory = "", foodType = "", sizeType = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		listview = (ListView) findViewById(R.id.getProductsListView);

		listview = getListView();
		LayoutInflater inflater = getLayoutInflater();
		View header = inflater.inflate(R.layout.get_products_header,
				(ViewGroup) findViewById(R.id.header_layout_root));
		listview.addHeaderView(header, null, false);
		View footer = inflater.inflate(R.layout.get_products_footer,
				(ViewGroup) findViewById(R.id.footer_layout_root));
		listview.addFooterView(footer, null, false);

		quantityN = (TextView) findViewById(R.id.quantity_number);
		ok = (Button) findViewById(R.id.button_ok);

		Bundle getStuff = getIntent().getExtras();
		String memberName = "";
		if (getStuff != null) {
			memberName = getStuff.getString("member_name");
			foodName = getStuff.getString("food_name");
			foodCategory = getStuff.getString("food_category");
			vouchersID = getStuff.getInt("vouchersID");
			sizeNo = getStuff.getDouble("size");
			sizeType = getStuff.getString("size_type");
			foodType = getStuff.getString("food_type");
		}
		
		Log.d("Getting name", memberName);
		vouchers = Utils.getInUseVouchersForMember(GetProducts.this, memberName);
		Log.d("GetProducts", vouchers.size()+"");
		
		Log.d("GETPRODUCTS", "test: " + vouchersID + " " + foodType);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (int i = 0; i < list.size(); i++) {
					CheckBoxRowModel model = list.get(i);
					Log.d("LIST ITEM", list.get(i).toString());
					if (model.isSelected()) {
						Log.d("SELECTED", model.getPersonName());
						Log.d("SELECTED", model.getVoucherCode());
						Log.d("SELECTED", model.getQuantityNumber() + "");
						Log.d("^^^VOUCHER^^^", model.getVoucherCode());

						String month_name = Utils.getMonth();
						String name = model.getPersonName();
						String vCode = model.getVoucherCode();
						int quant = Integer.parseInt(model.getQuantityNumber());

						// delete old stuff, if need to replace it with new
						// stuff
						Log.d("***DELETEOLD***", model.isDeleteOld() + "");
						Log.d("***COMBBEFORE***",
								model.isCombinationItemsBoughtBefore() + "");
						Log.d("***OTHEROPTIONTYPE***",
								model.getOtherOptionType());
						Log.d("***TESTNAME***", model.getPersonName());
						if (model.isDeleteOld()) {
							String where = "";

							if (!model.isCombinationItemsBoughtBefore()) {
								where = ChewContract.ProductsChosen.PRODUCT_TYPE
										+ "='"
										+ model.getOtherOptionType()
										+ "'"
										+ " AND "
										+ ChewContract.ProductsChosen.MEMBER_NAME
										+ "='"
										+ name
										+ "'"
										+ " AND "
										+ ChewContract.ProductsChosen.VOUCHER_CODE
										+ "='"
										+ vCode
										+ "'"
										+ " AND "
										+ ChewContract.ProductsChosen.MONTH
										+ "='" + month_name + "'";
							} else {
								where = ChewContract.ProductsChosen.COMBINATION
										+ "="
										+ 1
										+ " AND "
										+ ChewContract.ProductsChosen.MEMBER_NAME
										+ "='" + name + "'" + " AND "
										+ ChewContract.ProductsChosen.VOUCHER_CODE
										+ "='" + vCode + "'" + " AND "
										+ ChewContract.ProductsChosen.MONTH
										+ "='" + month_name + "'";
							}
							int numDeleted = getContentResolver().delete(
									ChewContract.ProductsChosen.CONTENT_URI,
									where, null);
							Log.d("****NUMDELETED****", numDeleted + "");
						}

						String[] resultColumns = new String[] {
								ChewContract.ProductsChosen._ID,
								ChewContract.ProductsChosen.QUANTITY,
								ChewContract.ProductsChosen.SIZE_NUM,
								ChewContract.ProductsChosen.COUNT };

						String where = ChewContract.ProductsChosen.PRODUCT_NAME
								+ "='" + foodName + "'" + " AND "
								+ ChewContract.ProductsChosen.MEMBER_NAME
								+ "='" + model.getPersonName() + "'" + " AND "
								+ ChewContract.ProductsChosen.VOUCHER_CODE
								+ "='" + model.getVoucherCode() + "'" + " AND "
								+ ChewContract.ProductsChosen.MONTH + "='"
								+ month_name + "'";

						Cursor cur = getContentResolver().query(
								ChewContract.ProductsChosen.CONTENT_URI,
								resultColumns, where, null, null);

						// later before inserting it, check if this produt is
						// already there, and just update

						Log.d("***QUANT***", quant + "");

						int count = quant;
						double size = sizeNo;

						if (model.isSpecialCase()) {
							Log.d("MODELSPECIALCASE", model.isSpecialCase()
									+ "");
							size = quant * sizeNo;
							quant = 0;
						}

						int isCombination = 0;
						if (model.isCombinationItem()) {
							isCombination = 1;
						}

						Log.d("***TAGSIZE***", sizeNo + "");
						Log.d("***SIZE***", size + "");
						Log.d("***ISCOMBINATION***", isCombination + "");

						// if row exists, update it
						if (cur.moveToFirst()) {

							int id = Integer.parseInt(cur.getString(0));
							int q = Integer.parseInt(cur.getString(1));
							double s = Double.parseDouble(cur.getString(2));
							int c = Integer.parseInt(cur.getString(3));

							ContentValues updateValues = new ContentValues();
							int rowsUpdate = 0;
							if (model.isSpecialCase()) {
								updateValues.put(
										ChewContract.ProductsChosen.SIZE_NUM, s
												+ size); // update ounces or wtv
								updateValues.put(
										ChewContract.ProductsChosen.QUANTITY,
										quant); // 0
								updateValues.put(
										ChewContract.ProductsChosen.COUNT, c
												+ count); // update count
							} else {
								updateValues.put(
										ChewContract.ProductsChosen.QUANTITY, q
												+ quant); // update quantity
								updateValues.put(
										ChewContract.ProductsChosen.COUNT, c
												+ count); // update count
							}

							where = ChewContract.ProductsChosen._ID + "=" + id;

							rowsUpdate = getContentResolver().update(
									ChewContract.ProductsChosen.CONTENT_URI,
									updateValues, where, null);
							Log.d("ROWSUPDATE", rowsUpdate + "");
							// else if row doesn't exist
						} else {

							ContentValues newValues = new ContentValues();

							// Assign values for each row.
							newValues.put(
									ChewContract.ProductsChosen.PRODUCT_NAME,
									foodName);
							newValues.put(
									ChewContract.ProductsChosen.PRODUCT_TYPE,
									foodType);
							newValues
									.put(ChewContract.ProductsChosen.PRODUCT_CATEGORY,
											foodCategory);
							newValues.put(ChewContract.ProductsChosen.SIZE_NUM,
									size);
							newValues.put(
									ChewContract.ProductsChosen.SIZE_TYPE,
									sizeType);
							newValues.put(ChewContract.ProductsChosen.QUANTITY,
									quant);
							newValues.put(ChewContract.ProductsChosen.COUNT,
									count);
							newValues.put(ChewContract.ProductsChosen.MONTH,
									month_name);
							newValues.put(
									ChewContract.ProductsChosen.MEMBER_NAME,
									name);
							newValues.put(
									ChewContract.ProductsChosen.VOUCHER_CODE,
									vCode);
							newValues.put(
									ChewContract.ProductsChosen.COMBINATION,
									isCombination);

							// Get the Content Resolver
							ContentResolver cr = getContentResolver();

							// Insert the row into your table
							cr.insert(
									ChewContract.ProductsChosen.CONTENT_URI,
									newValues);

							Log.d("CODE", vCode);
							Log.d("INSERTED", "inserted");
						}
					}
				}
				GetProducts.this.finish();
			}
		});
		whoCanGetIt(vouchersID);

	}

	private void whoCanGetIt(int vouchersID) {

		Log.d("WHOCANGETIT", vouchersID+"");
		
		// worked
		CursorLoader loader = null;

		String[] resultColumns = new String[] { ChewContract.FamilyVouchers.NAME, ChewContract.FamilyVouchers.VOUCHER_CODE };
		String where = ChewContract.VouchersIDsToCategories.VOUCHERS_ID + "="
				+ vouchersID + "";

		loader = new CursorLoader(GetProducts.this,
				ChewContract.CONTENT_URI_JOIN, resultColumns, where, null, null);
		loader.registerListener(50, new MyOnLoadCompleteListener());
		loader.startLoading();

	}

	private class MyOnLoadCompleteListener implements
			OnLoadCompleteListener<Cursor> {
		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {

			Log.d("ONLOADCOMPLETE", "onLoadComplete called");
			String vcode = "", name = "", quantity = "";

			Log.d("ONLOADCOMPLETE", cursor.getCount() + "");

			while (cursor.moveToNext()) {
				name = cursor.getString(0);
				vcode = cursor.getString(1);
				// /quantity = cursor.getString(2);
				// categoryID = cursor.getString(2);
				Log.d("FROMVOUCHERSTABLE", vcode + "-" + name + "-" + quantity);
				
				for(String v : vouchers){
					
					String vCode = v.split(" - ")[0];
					String n = v.split(" - ")[1];
					if(vCode.equals(vcode) && n.equals(name))
						list.add(get(name, vcode, ""));
				}
			}

			// if list was populated
			if (!list.isEmpty()) {

				adapter = new InteractiveArrayAdapter(GetProducts.this, list,
						foodType, sizeNo);
				listview.setAdapter(adapter);
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						GetProducts.this);

				// set title
				alertDialogBuilder
						.setTitle(getString(R.string.no_matching_voucher));

				// set dialog message
				alertDialogBuilder
						.setMessage(getString(R.string.choose_voucher_matching_product))
						.setCancelable(false)
						.setPositiveButton(getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										GetProducts.this.finish();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item, Toast.LENGTH_LONG).show();
	}

	private CheckBoxRowModel get(String n, String v, String q) {
		return new CheckBoxRowModel(n, v, q);
	}
}
