package com.vanderbilt.isis.chew.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class ChewContract {

	public static final String AUTHORITY = "com.vanderbilt.isis.chew.provider";
	
	public static final Uri AUTHORITY_URI = Uri.parse("content://"+AUTHORITY);
	
	public static final Uri CONTENT_URI_JOIN = 
			Uri.parse("content://"+AUTHORITY + "/voucherCategoriesFamilyVouchersJoin");
	
	public static final Uri CONTENT_URI_FOODTYPELOOKUP_VOUCHER_JOIN = 
			Uri.parse("content://"+AUTHORITY +"/foodtypeLookupVoucherJoin");
	
	public static final Uri CONTENT_URI_VOUCHERFOOD_JOIN_PRODUCTSCHOSEN =
			Uri.parse("content://"+AUTHORITY + "/voucherFoodProductsChosenJoin");
	
	public static final Uri CONTENT_URI_SUM_PRODUCE =
			Uri.parse("content://"+AUTHORITY + "/sumProduce");
	
	public static final Uri CONTENT_URI_DISTINCT_NAMES =
			Uri.parse("content://"+AUTHORITY + "/distinctNames");
	
	/** Stores **/
	public static final class Store implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "store");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.store";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.store";
		
		/** Table name **/
		public static final String TABLE = "Store";
		
		/** Column names **/
		public static final String FOOD_NAME = "food_name";
		public static final String BARCODE = "barcode";
		public static final String FOOD_CATEGORY = "food_category";
		public static final String REMOVE = "remove";
		public static final String VOUCHERS_ID = "vouchers_id";
		public static final String SIZE = "size";
		public static final String SIZE_TYPE = "size_type";
		public static final String FOOD_TYPE = "food_type";
		//public static final String WALMART = "walmart";
		//public static final String KROGER = "kroger";
		public static final String STORE = "store";
	}
	
	/** FoodTypeLookup **/
	public static final class FoodTypeLookup implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "foodtypelookup");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.foodtypelookup";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.foodtypelookup";
		
		/** Table name **/
		public static final String TABLE = "FoodTypeLookup";
		
		/** Column names **/
		public static final String FOOD_TYPE_NAME = "food_type_name";		
		
	}
	
	/** FamilyVouchers **/
	public static final class FamilyVouchers implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "familyvouchers");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.familyvouchers";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.familyvouchers";
		
		/** Table name **/
		public static final String TABLE = "FamilyVouchers";
		
		/** Column names **/
		public static final String NAME = "name";
		public static final String VOUCHER_CODE = "voucher_code";
		public static final String VOUCHER_MONTH = "voucher_month";
		public static final String ETHNICITY = "ethnicity";
		public static final String USED = "used";
	}
	
	/** Voucher **/
	public static final class Voucher implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "voucher");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.voucher";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.voucher";
		
		/** Table name **/
		public static final String TABLE = "Voucher";
		
		/** Column names **/
		public static final String CODE = "code";
		public static final String OPTION1 = "option1";
		public static final String OPTION2 = "option2";
		public static final String OPTION3 = "option3";
		public static final String OPTION4 = "option4";
		public static final String OP1_COMB_QUANTITY = "op1_comb_quantity";	
	}
	
	
	/** VouchersIDsToCategories **/
	public static final class VouchersIDsToCategories implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "vouchersidstocategories");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.vouchersidstocategories";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.vouchersidstocategories";
		
		/** Table name **/
		public static final String TABLE = "VouchersIDsToCategories";
		
		/** Column names **/
		public static final String VOUCHERS_ID = "vouchers_id";
		public static final String VOUCHER_CODE = "voucher_code";
		
	}
	
	/** VoucherFood **/
	public static final class VoucherFood implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "voucherfood");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.voucherfood";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.voucherfood";
		
		/** Table name **/
		public static final String TABLE = "VoucherFood";
		
		/** Column names **/
		public static final String FOOD_TYPE_ID = "food_type_id";
		public static final String FOOD_TYPE = "food_type";
		public static final String QUANTITY = "quantity";
		public static final String MIN_SIZE = "min_size";
		public static final String MAX_SIZE = "max_size";
		public static final String SIZE_TYPE = "size_type";
		public static final String SUBSTITUTE = "substitute";
		
	}
	
	/** ProductsChosen **/
	public static final class ProductsChosen implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "productschosen");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.productschosen";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.productschosen";
		
		/** Table name **/
		public static final String TABLE = "ProductsChosen";
		
		/** Column names **/
		public static final String PRODUCT_NAME = "product_name";
		public static final String PRODUCT_TYPE = "product_type";
		public static final String PRODUCT_CATEGORY = "product_category";
		public static final String SIZE_NUM = "size_num";
		public static final String SIZE_TYPE = "size_type";
		public static final String QUANTITY = "quantity";
		public static final String COUNT = "count";
		//public static final String COST = "cost";
		public static final String MONTH = "month";
		public static final String MEMBER_NAME = "member_name";
		public static final String VOUCHER_CODE = "voucher_code";
		public static final String COMBINATION = "combination";
	}
	
	/** ProductsChosen **/
	public static final class ProduceChosen implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "producechosen");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.producechosen";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.producechosen";
		
		/** Table name **/
		public static final String TABLE = "ProduceChosen";
		
		/** Column names **/
		public static final String PRODUCE_NAME = "produce_name";
		public static final String COST = "cost";
		public static final String MONTH = "month";
		public static final String MEMBER_NAME = "member_name";
		public static final String VOUCHER_CODE = "voucher_code";
	}
	
	/** Recipes **/
	public static final class Recipes implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "recipes");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.recipes";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.recipes";
		
		/** Table name **/
		public static final String TABLE = "Recipe";
		
		/** Column names **/
		public static final String SHORT_TITLE = "short_title";
		public static final String LONG_TITLE = "long_title";
		public static final String RECIPE_IMAGE = "recipe_image";
		public static final String FAVORITE = "favorite";
		
		
	}
	
	/** Ingredients **/
	public static final class Ingredients implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "ingredients");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.ingredients";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.ingredients";
		
		/** Table name **/
		public static final String TABLE = "RecipeIngredients";
		
		/** Column names **/
		public static final String RECIPE_ID = "recipe_id";
		public static final String INGREDIENT = "ingredient";
		public static final String LONG_DESCRIPTION = "longer_description";
		
	}
	
	/** Steps **/
	public static final class Steps implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "steps");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.steps";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.steps";
		
		/** Table name **/
		public static final String TABLE = "RecipeSteps";
		
		/** Column names **/
		public static final String RECIPE_ID = "recipe_id";
		public static final String STEP = "step";
		public static final String STEP_IMAGE = "step_image";
		
	}
	
	/** ShoppingList **/
	public static final class ShoppingItems implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "shoppingitems");
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.vanderbilt.isis.chew.shoppingitmes";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.vanderbilt.isis.chew.shoppingitems";
		
		/** Table name **/
		public static final String TABLE = "ShoppingList";
		
		/** Column names **/
		public static final String RECIPE_NAME = "recipe_name";
		public static final String INGREDIENT = "ingredient";
		
	}
}

























