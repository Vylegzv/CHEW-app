package com.vanderbilt.isis.chew.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.model.CheckBoxRowModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.vanderbilt.isis.chew.R;

public class InteractiveArrayAdapter extends ArrayAdapter<CheckBoxRowModel> {

	private ArrayList<CheckBoxRowModel> list;
	private final Activity context;
	private final String productType;
	String name;
	String voucherCode;
	int ch = 0;
	double sizeNum;
	AlertDialog.Builder alertDialogBuilder2;
	AlertDialog.Builder noMoreDialog;
	AlertDialog.Builder otherOptionDialog;
	boolean combinationAllowed = false;
	boolean combinationItemsBoughtBefore = false;
	int substitute1 = 0;
	int substitute2 = 0;
	String foodTypeName1 = "";
	String foodTypeName2 = "";
	View view = null;
	int quantityAllowed = 0, op1CombQuant = 0;
	boolean firstCursor = false;
	boolean alreadyBoughtOtherOption = false;
	boolean deleteOld = false;
	String otherOptionType = "";
	String month_name = "";
	double minSizeAllowed;
	double maxSizeAllowed;
	String sizeType = "";
	// boolean specialCase;
	// double tagSize;
	String tagDescription;
	int listPosition = -1;

	public InteractiveArrayAdapter(Activity context,
			ArrayList<CheckBoxRowModel> list, String productType, double sizeNum) {
		super(context, R.layout.row_checkbox, list);
		this.context = context;
		this.list = new ArrayList<CheckBoxRowModel>();
		this.list.addAll(list);
		this.productType = productType;
		this.sizeNum = sizeNum;
	}

	static class ViewHolder {
		protected TextView name;
		protected TextView voucherCode;
		protected TextView quantity;
		protected TextView quantityNumber;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.row_checkbox, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.voucherCode = (TextView) view
					.findViewById(R.id.voucherCode);
			viewHolder.quantity = (TextView) view.findViewById(R.id.quanity);
			viewHolder.quantityNumber = (TextView) view
					.findViewById(R.id.quantity_number);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							CheckBoxRowModel element = (CheckBoxRowModel) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());

							if (buttonView.isChecked()) {
								listPosition = position;
								Log.d("CHECKBOX:", "checked");
								name = list.get(position).getPersonName();
								voucherCode = list.get(position)
										.getVoucherCode();
								Log.d("CHECKBOX NAME:", name);
								Log.d("CHECKBOX CODE:", voucherCode);
								CheckBoxRowModel model = list.get(listPosition);
								model.setSpecialCase(false);
								combinationAllowed = false;
								combinationItemsBoughtBefore = false;
								deleteOld = true;
								// minSizeAllowed = 0.0;
								maxSizeAllowed = 0.0;

								// see if works
								view.setTag(viewHolder);
								Log.d("SEEVH", view.getTag().toString());

								CursorLoader loader = null;

								String[] resultColumns = new String[] {
										ChewContract.Voucher.OPTION1,
										ChewContract.Voucher.OPTION2,
										ChewContract.Voucher.OPTION3,
										ChewContract.Voucher.OPTION4,
										ChewContract.FoodTypeLookup._ID,
										ChewContract.Voucher.OP1_COMB_QUANTITY };

								String where = ChewContract.FoodTypeLookup.FOOD_TYPE_NAME
										+ "='"
										+ productType
										+ "'"
										+ " AND "
										+ ChewContract.Voucher.CODE
										+ "='"
										+ list.get(position).getVoucherCode()
										+ "'";

								loader = new CursorLoader(
										context,
										ChewContract.CONTENT_URI_FOODTYPELOOKUP_VOUCHER_JOIN,
										resultColumns, where, null, null);
								loader.registerListener(50,
										new MyOnLoadCompleteListener3());
								loader.startLoading();

							} else {
								Log.d("CHECKBOX:", "unchecked");
								CheckBoxRowModel m = list.get(position);
								m.setSelected(false);
								m.setQuantityNumber(0 + "");
								// viewHolder.checkbox.setChecked(list.get(position).isSelected());
								viewHolder.quantityNumber.setText(list.get(
										position).getQuantityNumber());
							}
						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText("(" + list.get(position).getPersonName() + ")");
		holder.voucherCode.setText(list.get(position).getVoucherCode());
		holder.checkbox.setChecked(list.get(position).isSelected());
		return view;
	}

	private class MyOnLoadCompleteListener3 implements
			OnLoadCompleteListener<Cursor> {
		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor c1) {

			Log.d("Listener3", "called");
			
			String where = "";

			String op1 = "", op2 = "", op3 = "", op4 = "", fID = "";
			String[] options = new String[4];
			String whichOp = "";
			quantityAllowed = 0;
			op1CombQuant = 0;

			otherOptionType = "";
			alreadyBoughtOtherOption = false;

			// if cursor is empty, then it doesn't belong to this voucher, so
			// display dialog
			firstWhile: while (c1 != null && c1.moveToNext()) {

				firstCursor = true;

				op1 = c1.getString(0);
				op2 = c1.getString(1);
				op3 = c1.getString(2);
				op4 = c1.getString(3);
				fID = c1.getString(4);
				op1CombQuant = Integer.parseInt(c1.getString(5));

				options[0] = c1.getString(0); // option1
				options[1] = c1.getString(1); // option2
				options[2] = c1.getString(2); // option3
				options[3] = c1.getString(3); // option4

				Log.d("CURSOR1", "option1: " + op1 + ", option2: " + op2
						+ ", option3 " + op3 + ", option4 " + op4 + ", fID: " + fID + ", op2Quant: "
						+ op1CombQuant);

				// if we are buying the first option and option1 quantity = 3
				if (Integer.parseInt(options[0]) == Integer.parseInt(fID)
						&& op1CombQuant == 3) {
					Log.d("COMBINATION", "true");
					combinationAllowed = true;
				}

				// if we are buying second option and option1 quantity = 3, note
				// that to delete all options 2 in case
				// they want to replace those
				if (Integer.parseInt(options[1]) == Integer.parseInt(fID)
						&& op1CombQuant == 3) {
					combinationItemsBoughtBefore = true;
				}

				Log.d("Combination Before", combinationItemsBoughtBefore + "");

				// if combinationAllowed = false
				// if(!combinationAllowed){
				
				// Iterate through possible other options
				for (int i = 0; i < options.length; ++i) {
					// which option doesn't equal fID and which option is not -1
					Log.d("options" + i, options[i]);
					if (Integer.parseInt(options[i]) != Integer.parseInt(fID)
							&& Integer.parseInt(options[i]) != -1) {
						// get their food type
						Log.d("INIF", "inif");
						where = ChewContract.VoucherFood.FOOD_TYPE_ID + "='"
								+ options[i] + "'" + " AND "
								+ ChewContract.ProductsChosen.MEMBER_NAME
								+ "='" + name + "'" + " AND "
								+ ChewContract.ProductsChosen.VOUCHER_CODE
								+ "='" + voucherCode + "'";

						Cursor c10 = context
								.getContentResolver()
								.query(ChewContract.CONTENT_URI_VOUCHERFOOD_JOIN_PRODUCTSCHOSEN,
										new String[] {
												ChewContract.VoucherFood.FOOD_TYPE,
												ChewContract.VoucherFood.QUANTITY,
												ChewContract.VoucherFood.MAX_SIZE,
												ChewContract.VoucherFood.MIN_SIZE,
												ChewContract.VoucherFood.SIZE_TYPE,
												ChewContract.VoucherFood.SUBSTITUTE },
										where, null, null);

						if (c10.moveToNext()) {
							int otherOpSub = Integer.parseInt(c10.getString(5));
							if (otherOpSub != -1) {
								otherOptionType = "buttermilk and/or evaporated milk and/or tofu";
							} else {
								otherOptionType = c10.getString(0);
							}
							Log.d("CURSOR10", "otherOptionType: "
									+ otherOptionType);
							Log.d("CURSOR11", "cannot buy " + productType + ""
									+ " because already bought "
									+ otherOptionType);
							alreadyBoughtOtherOption = true;
							Log.d("ALREADYBOUTHOTHEROPTION",
									alreadyBoughtOtherOption + "");
							break firstWhile;
						} else {
							Log.d("CURSOR10", "empty");
						}
					}
				}

			}

			// do some if and call cursor 2
			if (firstCursor) {

				CursorLoader loader1 = null;
				String[] resultColumns = new String[] {
						ChewContract.VoucherFood.QUANTITY,
						ChewContract.VoucherFood.MAX_SIZE,
						ChewContract.VoucherFood.MIN_SIZE,
						ChewContract.VoucherFood.SIZE_TYPE,
						ChewContract.VoucherFood.SUBSTITUTE };

				where = ChewContract.VoucherFood.FOOD_TYPE_ID + "='" + fID
						+ "'";

				loader1 = new CursorLoader(context,
						ChewContract.VoucherFood.CONTENT_URI, resultColumns,
						where, null, null);
				loader1.registerListener(50, new MyOnLoadCompleteListener4());
				loader1.startLoading();
			}else{
				Log.d("First Cursor", "EMPTY");
			}

		}
	}

	private class MyOnLoadCompleteListener4 implements
			OnLoadCompleteListener<Cursor> {
		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor c2) {

			String max = "";
			String where = "";

			if (c2 != null && c2.moveToNext()) {
				quantityAllowed = Integer.parseInt(c2.getString(0));
				maxSizeAllowed = Double.parseDouble(c2.getString(1));
				minSizeAllowed = Double.parseDouble(c2.getString(2));
				sizeType = c2.getString(3);
				substitute1 = Integer.parseInt(c2.getString(4));
				Log.d("CURSOR2", "quantityAllowed: " + quantityAllowed
						+ ", max: " + maxSizeAllowed + ", min "
						+ minSizeAllowed + ", sizeType: " + 0 + ", sizeType: "
						+ sizeType + ", substitute " + substitute1);
				Log.d("SUBSTITUTE1", substitute1 + "");
			}

			

			if (!alreadyBoughtOtherOption) {

				Calendar cal = Calendar.getInstance();
				SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
				month_name = month_date.format(cal.getTime());

				Log.d("*** MONTH", month_name);

				CursorLoader loader2 = null;

				if (!combinationAllowed) {

					where = ChewContract.ProductsChosen.MONTH + "='"
							+ month_name + "'" + " AND "
							+ ChewContract.ProductsChosen.MEMBER_NAME + "='"
							+ name + "'" + " AND "
							+ ChewContract.ProductsChosen.VOUCHER_CODE + "='"
							+ voucherCode + "'" + " AND "
							+ ChewContract.ProductsChosen.PRODUCT_TYPE + "='"
							+ productType + "'";
				} else {
					where = ChewContract.ProductsChosen.MONTH + "='"
							+ month_name + "'" + " AND "
							+ ChewContract.ProductsChosen.MEMBER_NAME + "='"
							+ name + "'" + " AND "
							+ ChewContract.ProductsChosen.VOUCHER_CODE + "='"
							+ voucherCode + "'" + " AND "
							+ ChewContract.ProductsChosen.COMBINATION + "=" + 1
							+ "";
				}

				String[] resultColumns = new String[] {
						ChewContract.ProductsChosen.QUANTITY,
						ChewContract.ProductsChosen.SIZE_NUM,
						ChewContract.ProductsChosen.SIZE_TYPE };

				loader2 = new CursorLoader(context,
						ChewContract.ProductsChosen.CONTENT_URI, resultColumns,
						where, null, null);
				loader2.registerListener(50, new MyOnLoadCompleteListener5());
				loader2.startLoading();

			} else {

				// tell that you already bought enough
				// set title
				otherOptionDialog = new AlertDialog.Builder(context);
				otherOptionDialog.setTitle("You cannot buy " + productType
						+ " because you already bought " + otherOptionType);

				// set dialog message
				otherOptionDialog
						.setMessage(
								"Do you want to replace it? All choices of "
										+ otherOptionType.toUpperCase()
										+ " will be DELETED.")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										// MainActivity.this.finish();
										/*
										 * CheckBoxRowModel m =
										 * list.get(listPosition);
										 * Log.d("CHECKPROBLEM",
										 * list.get(listPosition)+"");
										 * m.setSelected(false); ViewHolder h =
										 * (ViewHolder) view.getTag();
										 * h.checkbox
										 * .setChecked(list.get(listPosition
										 * ).isSelected());
										 * Log.d("CHECKPROBLEM",
										 * list.get(listPosition
										 * ).isSelected()+"");
										 */

										// delete old
										deleteOld = true;
										CheckBoxRowModel m = list
												.get(listPosition);
										// Log.d("CHECKPROBLEM",
										// list.get(listPosition)+"");
										m.setDeleteOld(true);
										Log.d("DELETEOLD", m.isDeleteOld() + "");
										if (combinationItemsBoughtBefore) {
											m.setCombinationItemsBoughtBefore(true);
											Log.d("COMBINATIONBEFORE",
													m.isCombinationItemsBoughtBefore()
															+ "");
										} else {
											m.setOtherOptionType(otherOptionType);
											Log.d("OTHERTYPE",
													m.getOtherOptionType());
										}
										/*
										 * Calendar cal=Calendar.getInstance();
										 * SimpleDateFormat month_date = new
										 * SimpleDateFormat("MMMMMMMMM");
										 * month_name =
										 * month_date.format(cal.getTime());
										 * 
										 * Log.d("DELETE", otherOptionType);
										 * Log.d("DELETE", name);
										 * Log.d("DELETE", month_name);
										 * 
										 * String where = "";
										 * 
										 * if(!combinationItemsBoughtBefore){
										 * where =
										 * OverallContentProvider.ProductsChosen
										 * .PRODUCT_TYPE + "='" +
										 * otherOptionType + "'" + " AND " +
										 * OverallContentProvider
										 * .ProductsChosen.NAME + "='" + name +
										 * "'" + " AND " +
										 * OverallContentProvider
										 * .ProductsChosen.MONTH + "='" +
										 * month_name + "'"; }else{ where =
										 * OverallContentProvider
										 * .ProductsChosen.COMBINATION_ITEM +
										 * "=" + 1 + " AND " +
										 * OverallContentProvider
										 * .ProductsChosen.NAME + "='" + name +
										 * "'" + " AND " +
										 * OverallContentProvider
										 * .ProductsChosen.MONTH + "='" +
										 * month_name + "'"; } int numDeleted =
										 * context.getContentResolver().delete(
										 * OverallContentProvider
										 * .ProductsChosen.CONTENT_URI8, where,
										 * null);
										 */

										// insert new
										CursorLoader loader2 = null;
										String where = "";
										if (!combinationAllowed) {

											where = ChewContract.ProductsChosen.MONTH
													+ "='"
													+ month_name
													+ "'"
													+ " AND "
													+ ChewContract.ProductsChosen.MEMBER_NAME
													+ "='"
													+ name
													+ "'"
													+ " AND "
													+ ChewContract.ProductsChosen.VOUCHER_CODE
													+ "='"
													+ voucherCode
													+ "'"
													+ " AND "
													+ ChewContract.ProductsChosen.PRODUCT_TYPE
													+ "='" + productType + "'";
										} else {
											where = ChewContract.ProductsChosen.MONTH
													+ "='"
													+ month_name
													+ "'"
													+ " AND "
													+ ChewContract.ProductsChosen.MEMBER_NAME
													+ "='"
													+ name
													+ "'"
													+ " AND "
													+ ChewContract.ProductsChosen.VOUCHER_CODE
													+ "='"
													+ voucherCode
													+ "'"
													+ " AND "
													+ ChewContract.ProductsChosen.COMBINATION
													+ "=" + 1 + "";
										}

										String[] resultColumns = new String[] {
												ChewContract.ProductsChosen.QUANTITY,
												ChewContract.ProductsChosen.SIZE_NUM,
												ChewContract.ProductsChosen.SIZE_TYPE };

										loader2 = new CursorLoader(
												context,
												ChewContract.ProductsChosen.CONTENT_URI,
												resultColumns, where, null,
												null);
										loader2.registerListener(50,
												new MyOnLoadCompleteListener5());
										loader2.startLoading();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = otherOptionDialog.create();

				// show it
				alertDialog.show();
			}

		}
	}

	private class MyOnLoadCompleteListener5 implements
			OnLoadCompleteListener<Cursor> {
		@Override
		public void onLoadComplete(Loader<Cursor> loader, Cursor c3) {

			String sizeT = "";
			double sizeNChosen = 0.0;
			int quant = 0;
			int quantOverall = 0;

			while (c3.moveToNext()) {
				quant = Integer.parseInt(c3.getString(0));
				quantOverall = quantOverall + quant;
				sizeNChosen = sizeNChosen + Double.parseDouble(c3.getString(1));
				sizeT = c3.getString(2);

				Log.d("CURSOR3", "quant: " + quant + ", quantOverall: "
						+ quantOverall + ", sizeNChosen " + sizeNChosen
						+ ", sizeT: " + sizeT);
			}

			if (combinationAllowed) {
				quantityAllowed = 3;
				Log.d("COMBINATIONALLOWED", "true");
				CheckBoxRowModel model = list.get(listPosition);
				model.setCombinationItem(true);
				Log.d("COMBINATIONALLOWED", "true");
				Log.d("QUANTITYALLOWED", "=3");
			}

			int quantRunning = 0;
			double sizeDiff = 0.0;
			if (quantityAllowed == -1) {

				CheckBoxRowModel model = list.get(listPosition);
				model.setSpecialCase(true);
				Log.d("SET SPECIALCASE", model.isSpecialCase() + "");

				// sizeDiff = minSizeAllowed - sizeNChosen;
				sizeDiff = maxSizeAllowed - sizeNChosen;
				Log.d("LIFEDIFF", maxSizeAllowed + "-" + sizeNChosen + "="
						+ sizeDiff);
				if (sizeDiff > 0) {
					quantRunning = (int) (sizeDiff / sizeNum);
					Log.d("DIVISION", sizeDiff + "/" + sizeNum + "="
							+ quantRunning);
				}
			} else {

				quantRunning = quantityAllowed - quantOverall;
				Log.d("QUANT SUBSTR", quantityAllowed + "-" + quantOverall
						+ "=" + quantRunning);
			}

			if (quantRunning >= 1) {

				final String[] choices = new String[quantRunning];
				for (int i = 0; i < quantRunning; ++i) {

					choices[i] = i + 1 + "";
				}

				// show another dialog with allowed quantities
				alertDialogBuilder2 = new AlertDialog.Builder(context)
						.setTitle("Please Select Quantity")
						.setSingleChoiceItems(choices, -1,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										ch = Integer.parseInt(choices[which]);
									}
								})
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										/*
										 * // later before inserting it, check
										 * if this produt is already there, and
										 * just update String name =
										 * model.getPersonName(); int quant =
										 * Integer
										 * .parseInt(model.getQuantityNumber());
										 */

										// first check if this item was already
										// chosen
										/*
										 * Log.d("TEST1", viewHolder.quantity
										 * .getText() .toString() + ch + " " +
										 * viewHolder
										 * .name.getText().toString());
										 * viewHolder
										 * .quantityNumber.setText(ch+"");
										 */

										Log.d("ID", id + "");
										CheckBoxRowModel model = list
												.get(listPosition);
										Log.d("PROBLEM", listPosition + "");
										model.setQuantityNumber(ch + "");
										Log.d("SET NUMBER",
												model.getQuantityNumber());

										ViewHolder h = (ViewHolder) view
												.getTag();
										Log.d("TEST1", h.quantity.getText()
												.toString()
												+ ch
												+ " "
												+ h.name.getText().toString());
										h.quantityNumber.setText(ch + "");

										/*
										 * ViewHolder h = (ViewHolder)
										 * view.getTag(); Log.d("TEST1",
										 * h.quantity .getText() .toString() +
										 * ch + " " +
										 * h.name.getText().toString());
										 * h.quantityNumber.setText(ch+"");
										 * 
										 * view.setTag(viewHolder);
										 * viewHolder.checkbox
										 * .setTag(list.get(position));
										 * viewHolder.quantity.getTag();
										 */

									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										CheckBoxRowModel model = list
												.get(listPosition);
										model.setSelected(false);
										Log.d("SET NUMBER",
												model.getQuantityNumber());

										ViewHolder holder = (ViewHolder) view
												.getTag();
										holder.checkbox.setChecked(list.get(
												listPosition).isSelected());
									}
								});

				AlertDialog alertDialog2 = alertDialogBuilder2.create();

				// show it
				alertDialog2.show();
			} else {

				// tell that you already bought enough
				// set title
				noMoreDialog = new AlertDialog.Builder(context);
				noMoreDialog.setTitle("You cannot get anymore of "
						+ productType + " for this voucher");

				// set dialog message
				noMoreDialog
				// .setMessage("Please select a WIC approved item.")
						.setCancelable(false).setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										// MainActivity.this.finish();
										CheckBoxRowModel m = list
												.get(listPosition);
										Log.d("CHECKPROBLEM",
												list.get(listPosition) + "");
										m.setSelected(false);
										ViewHolder h = (ViewHolder) view
												.getTag();
										h.checkbox.setChecked(list.get(
												listPosition).isSelected());
										Log.d("CHECKPROBLEM",
												list.get(listPosition)
														.isSelected() + "");
									}
								});

				// create alert dialog
				AlertDialog alertDialog = noMoreDialog.create();

				// show it
				alertDialog.show();

			}
		}
	}
}
