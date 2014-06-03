package com.vanderbilt.isis.chew.db;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import com.vanderbilt.isis.chew.db.ChewContract.FamilyVouchers;
import com.vanderbilt.isis.chew.db.ChewContract.VouchersIDsToCategories;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ChewContentProvider extends ContentProvider {

	private String TAG = getClass().getSimpleName();
	
	private static final HashMap<String, String> voucherCategoriesMap;
	
	/**
	 * Helper constants to use with UriMatcher
	 */
	private static final int RECIPES = 1;
	private static final int RECIPE_ID = 2;
	private static final int INGREDIENTS = 3;
	private static final int INGREDIENT_ID = 4;
	private static final int STEPS = 5;
	private static final int STEP_ID = 6;
	private static final int SHOPPINGITEMS = 7;
	private static final int SHOPPING_ITEM = 8;
	private static final int STORE = 9;
	private static final int STORE_ID = 10;
	private static final int FOODTYPELOOKUP = 11;
	private static final int FOODTYPELOOKUP_ID = 12;
	private static final int FAMILYVOUCHERS = 13;
	private static final int FAMILYVOUCHERS_ID = 14;
	private static final int VOUCHER = 15;
	private static final int VOUCHER_ID = 16;
	private static final int VOUCHERSIDSTOCATEGORIES = 17;
	private static final int VOUCHERSIDSTOCATEGORIES_ID = 18;
	private static final int VOUCHERFOOD = 19;
	private static final int VOUCHERFOOD_ID = 20;
	private static final int PRODUCTSCHOSEN = 21;
	private static final int PRODUCTSCHOSEN_ID = 22;
	private static final int PRODUCECHOSEN = 23;
	private static final int PRODUCECHOSEN_ID = 24;
	
	// joins
	private static final int ALL_VOUCHER_CATEGORIES_JOIN_FAMILY_VOUCHERS = 25;
	private static final int SINGLE_VOUCHER_CATEGORIES_JOIN_FAMILY_VOUCHERS = 26;
	private static final int ALL_FOODTYPE_LOOKUP_JOIN_VOUCHER = 27;
	private static final int SINGLE_FOODTYPE_LOOKUP_JOIN_VOUCHER = 28;
	private static final int ALL_VOUCHERFOOD_JOIN_PRODUCTSCHOSEN = 29;
	
	// aggregation
	private static final int SUM_PRODUCE = 30;
	
	private static final int DISTINCT_NAMES = 31;

	/**
	 * UriMatcher that will match different URIs
	 */
	private static final UriMatcher uriMatcher;

	/**
	 * Declare an SQLite open helper to manage database creation and versions
	 */
	private ChewDBHelper myDbOpenHelper;
	
	static {
	    //Setup projection maps
		voucherCategoriesMap = new HashMap<String, String>();
		voucherCategoriesMap.put(VouchersIDsToCategories._ID, "VouchersIDsToCategories._ID AS VouchersIDsToCategories_ID");
		voucherCategoriesMap.put(VouchersIDsToCategories.VOUCHERS_ID, "VouchersIDsToCategories.vouchers_id AS VouchersIDsToCategories_vouchers_id");
		//voucherCategoriesMap.put(VouchersIDToCategories.VOUCHER_CATEGORY, VOUCHERS_ID_TO_CATEGORIES_TABLE + ".voucher_category");

		voucherCategoriesMap.put(FamilyVouchers._ID, "FamilyVouchers._ID AS FamilyVouchers_ID");
		voucherCategoriesMap.put(FamilyVouchers.NAME, "FamilyVouchers.name AS FamilyVouchers_name");        
		//voucherCategoriesMap.put(MemberVouchers.VOUCHER_CATEGORY, MEMBER_VOUCHERS_TABLE + ".voucher_category"); 
		voucherCategoriesMap.put(FamilyVouchers.VOUCHER_CODE, "FamilyVouchers.voucher_code AS FamilyVouchers_voucher_code");  
		voucherCategoriesMap.put(FamilyVouchers.VOUCHER_MONTH, "FamilyVouchers.month AS FamilyVouchers_month");
		
		// to force selection from particular table
		voucherCategoriesMap.put("voucher_code", "VouchersIDsToCategories.voucher_code AS VouchersIDsToCategories_voucher_code");
	}
	
	/**
	 * Declare a UriMatcher to match URIs
	 */
	static {

		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ChewContract.AUTHORITY, "recipes", RECIPES);
		uriMatcher.addURI(ChewContract.AUTHORITY, "recipes/#", RECIPE_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "ingredients", INGREDIENTS);
		uriMatcher.addURI(ChewContract.AUTHORITY, "ingredients/#", INGREDIENT_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "steps", STEPS);
		uriMatcher.addURI(ChewContract.AUTHORITY, "steps/#", STEP_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "shoppingitems", SHOPPINGITEMS);
		uriMatcher.addURI(ChewContract.AUTHORITY, "shoppingitems/#", SHOPPING_ITEM);
		uriMatcher.addURI(ChewContract.AUTHORITY, "store", STORE);
		uriMatcher.addURI(ChewContract.AUTHORITY, "store/#", STORE_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "foodtypelookup", FOODTYPELOOKUP);
		uriMatcher.addURI(ChewContract.AUTHORITY, "foodtypelookup/#", FOODTYPELOOKUP_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "familyvouchers", FAMILYVOUCHERS);
		uriMatcher.addURI(ChewContract.AUTHORITY, "familyvouchers/#", FAMILYVOUCHERS_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "voucher", VOUCHER);
		uriMatcher.addURI(ChewContract.AUTHORITY, "voucher/#", VOUCHER_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "vouchersidstocategories", VOUCHERSIDSTOCATEGORIES);
		uriMatcher.addURI(ChewContract.AUTHORITY, "vouchersidstocategories/#", VOUCHERSIDSTOCATEGORIES_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "voucherfood", VOUCHERFOOD);
		uriMatcher.addURI(ChewContract.AUTHORITY, "voucherfood/#", VOUCHERFOOD_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "productschosen", PRODUCTSCHOSEN);
		uriMatcher.addURI(ChewContract.AUTHORITY, "productschosen/#", PRODUCTSCHOSEN_ID);
		uriMatcher.addURI(ChewContract.AUTHORITY, "producechosen", PRODUCECHOSEN);
		uriMatcher.addURI(ChewContract.AUTHORITY, "producechosen/#", PRODUCECHOSEN_ID);
		
		//joins
		uriMatcher.addURI(ChewContract.AUTHORITY, "voucherCategoriesFamilyVouchersJoin", ALL_VOUCHER_CATEGORIES_JOIN_FAMILY_VOUCHERS);
		uriMatcher.addURI(ChewContract.AUTHORITY, "voucherCategoriesFamilyVouchersJoin/#", SINGLE_VOUCHER_CATEGORIES_JOIN_FAMILY_VOUCHERS);
		uriMatcher.addURI(ChewContract.AUTHORITY, "foodtypeLookupVoucherJoin", ALL_FOODTYPE_LOOKUP_JOIN_VOUCHER);
		uriMatcher.addURI(ChewContract.AUTHORITY, "foodtypeLookupVoucherJoin/#", SINGLE_FOODTYPE_LOOKUP_JOIN_VOUCHER);
		uriMatcher.addURI(ChewContract.AUTHORITY, "voucherFoodProductsChosenJoin", ALL_VOUCHERFOOD_JOIN_PRODUCTSCHOSEN);
		uriMatcher.addURI(ChewContract.AUTHORITY, "distinctNames", DISTINCT_NAMES);	
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		String rowId = null;
		String table = null;

		switch (uriMatcher.match(uri)) {

		case FAMILYVOUCHERS_ID:

			table = ChewContract.FamilyVouchers.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.FamilyVouchers._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case FAMILYVOUCHERS:

			table = ChewContract.FamilyVouchers.TABLE;
			break;
		
		case PRODUCTSCHOSEN_ID:

			table = ChewContract.ProductsChosen.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.ProductsChosen._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case PRODUCTSCHOSEN:

			table = ChewContract.ProductsChosen.TABLE;
			break;
			
		case PRODUCECHOSEN_ID:

			table = ChewContract.ProduceChosen.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.ProduceChosen._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case PRODUCECHOSEN:

			table = ChewContract.ProduceChosen.TABLE;
			break;
		
		case RECIPE_ID:

			table = ChewContract.Recipes.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.Recipes._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case RECIPES:

			table = ChewContract.Recipes.TABLE;
			break;
			
		case INGREDIENT_ID:

			table = ChewContract.Ingredients.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.Ingredients._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case INGREDIENTS:

			table = ChewContract.Ingredients.TABLE;
			break;
			
		case STEP_ID:

			table = ChewContract.Steps.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.Steps._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case STEPS:

			table = ChewContract.Steps.TABLE;
			break;
			
		case SHOPPINGITEMS:

			table = ChewContract.ShoppingItems.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.ShoppingItems._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case SHOPPING_ITEM:

			table = ChewContract.ShoppingItems.TABLE;
			break;

		default:

			Log.e(TAG, "Unsupported URI: " + uri);
			return 0;
		}

		/**
		 * To return the number of deleted items, a where clause must be
		 * specified. Pass in 1 to delete all rows.
		 */
		if (selection == null)
			selection = "1";

		int deleteCount = myDbOpenHelper.getWritableDatabase().delete(table,
				selection, selectionArgs);
	    if (deleteCount > 0)
	    	getContext().getContentResolver().notifyChange(uri, null); //uri?

		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		
		switch (uriMatcher.match(uri)) {

		case VOUCHERFOOD_ID:
			return ChewContract.VoucherFood.CONTENT_ITEM_TYPE;
		case VOUCHERFOOD:
			return ChewContract.VoucherFood.CONTENT_TYPE;
		case VOUCHERSIDSTOCATEGORIES_ID:
			return ChewContract.VouchersIDsToCategories.CONTENT_ITEM_TYPE;
		case VOUCHERSIDSTOCATEGORIES:
			return ChewContract.VouchersIDsToCategories.CONTENT_TYPE;
		case VOUCHER_ID:
			return ChewContract.Voucher.CONTENT_ITEM_TYPE;
		case VOUCHER:
			return ChewContract.Voucher.CONTENT_TYPE;
		case FAMILYVOUCHERS_ID:
			return ChewContract.FamilyVouchers.CONTENT_ITEM_TYPE;
		case FAMILYVOUCHERS:
			return ChewContract.FamilyVouchers.CONTENT_TYPE;
		case FOODTYPELOOKUP_ID:
			return ChewContract.FoodTypeLookup.CONTENT_ITEM_TYPE;
		case FOODTYPELOOKUP:
			return ChewContract.FoodTypeLookup.CONTENT_TYPE;
		case STORE_ID:
			return ChewContract.Store.CONTENT_ITEM_TYPE;
		case STORE:
			return ChewContract.Store.CONTENT_TYPE;
		case PRODUCTSCHOSEN_ID:
			return ChewContract.ProductsChosen.CONTENT_ITEM_TYPE;
		case PRODUCTSCHOSEN:
			return ChewContract.ProductsChosen.CONTENT_TYPE;
		case PRODUCECHOSEN_ID:
			return ChewContract.ProduceChosen.CONTENT_ITEM_TYPE;
		case PRODUCECHOSEN:
			return ChewContract.ProduceChosen.CONTENT_TYPE;
		case RECIPE_ID:
			return ChewContract.Recipes.CONTENT_ITEM_TYPE;
		case RECIPES:
			return ChewContract.Recipes.CONTENT_TYPE;
		case INGREDIENT_ID:
			return ChewContract.Ingredients.CONTENT_ITEM_TYPE;
		case INGREDIENTS:
			return ChewContract.Ingredients.CONTENT_TYPE;
		case STEP_ID:
			return ChewContract.Steps.CONTENT_ITEM_TYPE;
		case STEPS:
			return ChewContract.Steps.CONTENT_TYPE;
		case SHOPPINGITEMS:
			return ChewContract.ShoppingItems.CONTENT_ITEM_TYPE;
		case SHOPPING_ITEM:
			return ChewContract.ShoppingItems.CONTENT_TYPE;
		default:
			Log.e(TAG, "Unsupported URI: " + uri);
			return null;

		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		long rowId = 0;
		Uri _uri = null;

		switch (uriMatcher.match(uri)) {

		case FAMILYVOUCHERS:

			rowId = myDbOpenHelper.getWritableDatabase().insert(ChewContract.FamilyVouchers.TABLE,
					null, values);
			// if added successfully
			if (rowId > 0) {
				_uri = ContentUris.withAppendedId(
						ChewContract.FamilyVouchers.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(_uri, null);
			}
			break;
		
		case PRODUCTSCHOSEN:

			rowId = myDbOpenHelper.getWritableDatabase().insert(ChewContract.ProductsChosen.TABLE,
					null, values);
			// if added successfully
			if (rowId > 0) {
				_uri = ContentUris.withAppendedId(
						ChewContract.ProductsChosen.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(_uri, null);
			}
			break;
			
		case PRODUCECHOSEN:

			rowId = myDbOpenHelper.getWritableDatabase().insert(ChewContract.ProduceChosen.TABLE,
					null, values);
			// if added successfully
			if (rowId > 0) {
				_uri = ContentUris.withAppendedId(
						ChewContract.ProduceChosen.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(_uri, null);
			}
			break;
		
		case RECIPES:

			rowId = myDbOpenHelper.getWritableDatabase().insert(ChewContract.Recipes.TABLE,
					null, values);
			// if added successfully
			if (rowId > 0) {
				_uri = ContentUris.withAppendedId(
						ChewContract.Recipes.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(_uri, null);
			}
			break;
			
		case INGREDIENTS:

			rowId = myDbOpenHelper.getWritableDatabase().insert(ChewContract.Ingredients.TABLE,
					null, values);
			// if added successfully
			if (rowId > 0) {
				_uri = ContentUris.withAppendedId(
						ChewContract.Ingredients.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(_uri, null);
			}
			break;
			
		case STEPS:

			rowId = myDbOpenHelper.getWritableDatabase().insert(ChewContract.Steps.TABLE,
					null, values);
			// if added successfully
			if (rowId > 0) {
				_uri = ContentUris.withAppendedId(
						ChewContract.Steps.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(_uri, null);
			}
			break;
			
		case SHOPPINGITEMS:

			rowId = myDbOpenHelper.getWritableDatabase().insert(ChewContract.ShoppingItems.TABLE,
					null, values);
			// if added successfully
			if (rowId > 0) {
				_uri = ContentUris.withAppendedId(
						ChewContract.ShoppingItems.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(_uri, null);
			}
			break;
			
		default:

			Log.e(TAG, "Unsupported URI: " + uri);
			return null;
		}
		
		// Notify any observers of the change in the data set.
	    getContext().getContentResolver().notifyChange(_uri, null);
		
		return _uri;
	}
	

	@Override
	public boolean onCreate() {
		
		myDbOpenHelper = new ChewDBHelper(getContext());
		return ((myDbOpenHelper == null) ? false : true);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		String rowId = null;
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		switch (uriMatcher.match(uri)) {

		case STORE_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.Store.TABLE);
			queryBuilder.appendWhere(ChewContract.Store._ID + "=" + rowId);
			break;

		case STORE:

			queryBuilder.setTables(ChewContract.Store.TABLE);
			break;
		
		case FOODTYPELOOKUP_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.FoodTypeLookup.TABLE);
			queryBuilder.appendWhere(ChewContract.FoodTypeLookup._ID + "=" + rowId);
			break;

		case FOODTYPELOOKUP:

			queryBuilder.setTables(ChewContract.FoodTypeLookup.TABLE);
			break;
		
		case FAMILYVOUCHERS_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.FamilyVouchers.TABLE);
			queryBuilder.appendWhere(ChewContract.FamilyVouchers._ID + "=" + rowId);
			break;

		case FAMILYVOUCHERS:

			queryBuilder.setTables(ChewContract.FamilyVouchers.TABLE);
			break;
		
		case VOUCHER_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.Voucher.TABLE);
			queryBuilder.appendWhere(ChewContract.Voucher._ID + "=" + rowId);
			break;

		case VOUCHER:

			queryBuilder.setTables(ChewContract.Voucher.TABLE);
			break;
		
		case VOUCHERSIDSTOCATEGORIES_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.VouchersIDsToCategories.TABLE);
			queryBuilder.appendWhere(ChewContract.VouchersIDsToCategories._ID + "=" + rowId);
			break;

		case VOUCHERSIDSTOCATEGORIES:

			queryBuilder.setTables(ChewContract.VouchersIDsToCategories.TABLE);
			break;
		
		case VOUCHERFOOD_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.VoucherFood.TABLE);
			queryBuilder.appendWhere(ChewContract.VoucherFood._ID + "=" + rowId);
			break;

		case VOUCHERFOOD:

			queryBuilder.setTables(ChewContract.VoucherFood.TABLE);
			break;
		
		case PRODUCTSCHOSEN_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.ProductsChosen.TABLE);
			queryBuilder.appendWhere(ChewContract.ProductsChosen._ID + "=" + rowId);
			break;

		case PRODUCTSCHOSEN:

			queryBuilder.setTables(ChewContract.ProductsChosen.TABLE);
			break;
			
		case PRODUCECHOSEN_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.ProduceChosen.TABLE);
			queryBuilder.appendWhere(ChewContract.ProduceChosen._ID + "=" + rowId);
			break;

		case PRODUCECHOSEN:

			queryBuilder.setTables(ChewContract.ProduceChosen.TABLE);
			break;
		
		case RECIPE_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.Recipes.TABLE);
			queryBuilder.appendWhere(ChewContract.Recipes._ID + "=" + rowId);
			break;

		case RECIPES:

			queryBuilder.setTables(ChewContract.Recipes.TABLE);
			break;
			
		case INGREDIENT_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.Ingredients.TABLE);
			queryBuilder.appendWhere(ChewContract.Ingredients._ID + "=" + rowId);
			break;

		case INGREDIENTS:

			queryBuilder.setTables(ChewContract.Ingredients.TABLE);
			break;
			
		case STEP_ID:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.Steps.TABLE);
			queryBuilder.appendWhere(ChewContract.Steps._ID + "=" + rowId);
			break;

		case STEPS:

			queryBuilder.setTables(ChewContract.Steps.TABLE);
			break;
			
		case SHOPPING_ITEM:

			rowId = uri.getPathSegments().get(1);
			queryBuilder.setTables(ChewContract.ShoppingItems.TABLE);
			queryBuilder.appendWhere(ChewContract.ShoppingItems._ID + "=" + rowId);
			break;

		case SHOPPINGITEMS:

			queryBuilder.setTables(ChewContract.ShoppingItems.TABLE);
			break;
			
		case SINGLE_VOUCHER_CATEGORIES_JOIN_FAMILY_VOUCHERS:
			queryBuilder.setTables("VouchersIDsToCategories INNER JOIN FamilyVouchers " +
					"ON VouchersIDsToCategories.voucher_code = FamilyVouchers.voucher_code");
			queryBuilder.appendWhere(VouchersIDsToCategories._ID + "=" + uri.getLastPathSegment());
			//cursor = queryBuilder.query(mydb, projection, selection, selectionArgs, groupBy, having, sortOrder);
			break;
			
		case ALL_VOUCHER_CATEGORIES_JOIN_FAMILY_VOUCHERS:
			queryBuilder.setTables("VouchersIDsToCategories INNER JOIN FamilyVouchers " +
				"ON VouchersIDsToCategories.voucher_code = FamilyVouchers.voucher_code");
			//queryBuilder.setTables("VouchersIDToCategories INNER JOIN MemberVouchers " +
					//"ON VouchersIDToCategories.vouchers_id = MemberVouchers._id");
			queryBuilder.setProjectionMap(voucherCategoriesMap);
			queryBuilder.setDistinct(true);
			//cursor = queryBuilder.query(mydb, projection, selection, selectionArgs, groupBy, having, sortOrder);
			break;
			
		case ALL_VOUCHERFOOD_JOIN_PRODUCTSCHOSEN:
			queryBuilder.setTables("(SELECT * " +
					"FROM VoucherFood, ProductsChosen " +
					"WHERE VoucherFood.food_type = ProductsChosen.product_type)");
			break;
			
		case ALL_FOODTYPE_LOOKUP_JOIN_VOUCHER:
			 queryBuilder.setTables("(SELECT * " +
				 		"FROM FoodTypeLookup, Voucher " +
				 		"WHERE FoodTypeLookup._id = Voucher.option1 " +
				 		"OR FoodTypeLookup._id = Voucher.option2 " +
				 		"OR FoodTypeLookup._id = Voucher.option3 " +
				 		"OR FoodTypeLookup._id = Voucher.option4)");
			 break;
			 
		case SUM_PRODUCE:
			Log.d("INCP", "incp");
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
			String month_name = month_date.format(cal.getTime());
			queryBuilder.setTables("SELECT sum(ProduceChosen.cost) FROM ProduceChosen WHERE ProduceChosen.month = '" + month_name + "'"
					+ " AND ProductsChosen.product_category = 'CASH'");
			break;
			
		case DISTINCT_NAMES:
			queryBuilder.setTables("(SELECT DISTINCT _id, name" +
			 		" FROM FamilyVouchers GROUP BY name)");
			break;

		default:

			Log.e(TAG, "Unsupported URI: " + uri);
			return null;
		}

		Cursor cursor = queryBuilder.query(
				myDbOpenHelper.getWritableDatabase(), projection, selection,
				selectionArgs, null, null, sortOrder);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		String rowId = null;
		String table = null;

		switch (uriMatcher.match(uri)) {
		
		case PRODUCTSCHOSEN_ID:

			table = ChewContract.ProductsChosen.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.ProductsChosen._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case PRODUCTSCHOSEN:

			table = ChewContract.ProductsChosen.TABLE;
			break;
			
		case PRODUCECHOSEN_ID:

			table = ChewContract.ProduceChosen.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.ProduceChosen._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case PRODUCECHOSEN:

			table = ChewContract.ProduceChosen.TABLE;
			break;
		
		case RECIPE_ID:

			table = ChewContract.Recipes.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.Recipes._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case RECIPES:

			table = ChewContract.Recipes.TABLE;
			break;
			
		case INGREDIENT_ID:

			table = ChewContract.Ingredients.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.Ingredients._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case INGREDIENTS:

			table = ChewContract.Ingredients.TABLE;
			break;
			
		case STEP_ID:

			table = ChewContract.Steps.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.Steps._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case STEPS:

			table = ChewContract.Steps.TABLE;
			break;
			
		case SHOPPING_ITEM:

			table = ChewContract.ShoppingItems.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.ShoppingItems._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case SHOPPINGITEMS:

			table = ChewContract.ShoppingItems.TABLE;
			break;
			
		case FAMILYVOUCHERS_ID:

			table = ChewContract.FamilyVouchers.TABLE;
			rowId = uri.getPathSegments().get(1);
			selection = ChewContract.FamilyVouchers._ID
					+ "="
					+ rowId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;

		case FAMILYVOUCHERS:

			table = ChewContract.FamilyVouchers.TABLE;
			break;

		default:

			Log.e(TAG, "Unsupported URI: " + uri);
			return 0;
		}

		int updateCount = myDbOpenHelper.getWritableDatabase().update(table,
				values, selection, selectionArgs);

		if (updateCount > 0)
			getContext().getContentResolver().notifyChange(ChewContract.Recipes.CONTENT_URI, null); // NOT RECIPES

		return updateCount;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values){
		
		int numInserted = 0;
		String table = null;
		
		switch(uriMatcher.match(uri)){
		
		case SHOPPINGITEMS:
			table = ChewContract.ShoppingItems.TABLE;
			break;
			
		case FAMILYVOUCHERS:
			table = ChewContract.FamilyVouchers.TABLE;
			break;
		}		
		
		SQLiteDatabase myDB = myDbOpenHelper.getWritableDatabase();
		myDB.beginTransaction();
		
		try{
			for(ContentValues cv : values){
				long newID = myDB.insertOrThrow(table, null, cv);
				if(newID <= 0){
					throw new SQLException("Failed to insert row into " + uri);
				}
			}
			
			myDB.setTransactionSuccessful();
			getContext().getContentResolver().notifyChange(uri, null);
			numInserted = values.length;
		}finally{
			myDB.endTransaction();
		}
		
		return numInserted;
	}
}
