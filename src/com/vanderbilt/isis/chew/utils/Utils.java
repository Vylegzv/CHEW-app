package com.vanderbilt.isis.chew.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.vanderbilt.isis.chew.R;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.factories.CashVoucherFactory;
import com.vanderbilt.isis.chew.factories.RegularVoucherFactory;
import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	public static final int WALMART = 1;
	public static final int KROGER = 2;
	public static final int ALL_STORES = 3;
	public static final String STOREKEY = "store";
	public static final String VOUCHERS = "vouchers";
	public static final String NOTUSED = "not used";
	public static final String INUSE = "in use";
	public static final String USED = "used";
	public static final String SHOPKEY = "shopping";
	private static final String PWD = "password";

	/**
	 * private constructor to avoid this class from being instantiated
	 */
	private Utils() {

	}
	
	public static String getPwd(){
		return PWD;
	}

	public static boolean setShoppingStatus(Context context, boolean shopping) {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putBoolean(SHOPKEY, shopping);

		return editor.commit();

	}

	public static boolean isShopping(Context context) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(SHOPKEY, false);
	}

	public static boolean setStore(Context context, String store) {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();

		if (store.equals("Walmart"))
			editor.putInt(STOREKEY, WALMART);
		else if (store.equals("Kroger"))
			editor.putInt(STOREKEY, KROGER);

		return editor.commit();

	}

	public static String getStore(Context context) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String store = String.valueOf(prefs.getInt(STOREKEY, 0));
		return store;
	}

	/**
	 * 
	 * @param context
	 * @param vouchersUsed
	 *            - in the format "VoucherCode - Name"
	 * @return
	 */
	public static boolean setVouchers(Context context, Set<String> vouchersUsed) {

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		for (String voucher : vouchersUsed) {

			String selection = ChewContract.FamilyVouchers.VOUCHER_CODE + "='"
					+ voucher.split(" - ")[0] + "'" + " AND "
					+ ChewContract.FamilyVouchers.NAME + "='"
					+ voucher.split(" - ")[1] + "'";
			ops.add(ContentProviderOperation
					.newUpdate(ChewContract.FamilyVouchers.CONTENT_URI)
					.withSelection(selection, null)
					.withValue(ChewContract.FamilyVouchers.USED, Utils.INUSE)
					.build());
		}

		try {
			Log.d("Batch Update", "in try");
			context.getContentResolver()
					.applyBatch(ChewContract.AUTHORITY, ops);
		} catch (RemoteException e) {

		} catch (OperationApplicationException e) {

		}

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putStringSet(VOUCHERS, vouchersUsed);
		return editor.commit();
	}

	public static Set<String> getInUseVouchersForMember(Context context,
			String memberName) {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Set<String> vouchers = preferences.getStringSet(Utils.VOUCHERS, null);

		// debug
		if (vouchers != null) {

			if (memberName.isEmpty()) {

				for (String v : vouchers) {

					String vCode = v.split(" - ")[0];
					String name = v.split(" - ")[1];

					Log.d("Voucher Used", vCode + ", " + name);
				}

				return vouchers;
			} else {

				Log.d("Utils", "get vouchers for member");
				Set<String> memberVouchers = new HashSet<String>();
				for (String v : vouchers) {

					String vCode = v.split(" - ")[0];
					String name = v.split(" - ")[1];

					if (name.equals(memberName))
						memberVouchers.add(v);

					Log.d("Voucher Used", vCode + ", " + name);
				}

				return memberVouchers;
			}

		} else {
			Log.d("Voucher Used", "null");
		}

		return null;
	}

	public static Set<Voucher> getRegVouchersForMember(Context context,
			String memberName) {

		Set<Voucher> vouchers = new HashSet<Voucher>();

		String[] projection = new String[] {
				ChewContract.FamilyVouchers.VOUCHER_CODE,
				ChewContract.FamilyVouchers.USED };

		String month = Utils.getMonth();

		String selection = ChewContract.FamilyVouchers.NAME + "='" + memberName
				+ "'" + " AND " + ChewContract.ProductsChosen.MONTH + "='"
				+ month + "'";

		Cursor cursor = context.getContentResolver().query(
				ChewContract.FamilyVouchers.CONTENT_URI, projection, selection,
				null, null);

		while (cursor != null && cursor.moveToNext()) {

			VoucherCode vCode = VoucherCode.getVoucherCodeFromValue(cursor
					.getString(0));
			String used = cursor.getString(1);

			Voucher voucher = new RegularVoucherFactory().createVoucher(vCode,
					month, memberName, used);
			vouchers.add(voucher);
		}

		return vouchers;
	}

	/**
	 * 
	 * @param context
	 * @return Map<"vcode - name", CashVoucher>
	 */
	public static Map<String, Voucher> getCashVouchers(Context context) {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Set<String> vouchers = preferences.getStringSet(Utils.VOUCHERS, null);
		Map<String, Voucher> cashVouchers = new HashMap<String, Voucher>();

		// debug
		if (vouchers != null) {
			for (String v : vouchers) {

				String voucher = v.split(" - ")[0];
				String name = v.split(" - ")[1];

				VoucherCode vcode = VoucherCode
						.getVoucherCodeFromValue(voucher);

				if (VoucherCode.isCashCode(voucher)) {
					Voucher cashVoucher = new CashVoucherFactory()
							.createVoucher(vcode, getMonth(), name, Utils.INUSE);
					cashVouchers.put(v, cashVoucher);
				}
			}

		} else {
			Log.d("Voucher Used", "null");
		}

		return cashVouchers;
	}

	public static String getMonth() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
		return month_date.format(cal.getTime());
	}

	public static String removeZeros(String s) {

		s = s.replaceFirst("^0+(?!$)", "");
		s = s.substring(0, s.length() - 1);
		return s;
	}

	public static void assertDeleted(Context context, int num) {

		if (num > 0) {
			Toast.makeText(context,
					context.getString(R.string.deleted_success_msg),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, context.getString(R.string.problem),
					Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * public static Set<String> getCashVouchers(Context context){
	 * 
	 * SharedPreferences preferences = PreferenceManager
	 * .getDefaultSharedPreferences(context); Set<String> vouchers =
	 * preferences.getStringSet(Utils.VOUCHERS, null); Set<String> cashVouchers
	 * = new HashSet<String>();
	 * 
	 * // debug if(vouchers != null){ for(String v : vouchers){
	 * 
	 * if(v.contains(VoucherCode.CV6.getCode()) ||
	 * v.contains(VoucherCode.CV10.getCode())) cashVouchers.add(v); }
	 * 
	 * }else{ Log.d("Voucher Used", "null"); }
	 * 
	 * return cashVouchers; }
	 */

}
