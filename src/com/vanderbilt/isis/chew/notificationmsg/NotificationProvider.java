package com.vanderbilt.isis.chew.notificationmsg;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class NotificationProvider extends ContentProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationProvider.class);
	
	public static final String TAG = "NOTIFICATIONPROVIDER"; 
	

	public static final String AUTHORITY = "com.vanderbilt.isis.chew.notificationmsg.NotificationProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notifications");
    

    private static final int NO_MATCH = -1;
    private static final int ALL_ROWS = 1;
    private static final int SINGLE_ROW = 2;
    
    private static final UriMatcher uriMatcher;
    
    
    
    static {
    	uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    	uriMatcher.addURI(/*"com.pankaj.testapp.NotificationProvider"*/AUTHORITY, "notifications", ALL_ROWS);
    	uriMatcher.addURI(/*"com.pankaj.chewApp.NotificationProvider"*/AUTHORITY, "notifications/#", SINGLE_ROW);
    }
    
    
    //Table Notification
    public static final String NOTIFICATION_ID = "_id";
    public static final int NOTIFICATION_ID_COL = 0;
    
    public static final String NOTIFICATION_CATEGORY = "category";
    public static final int NOTIFICATION_CATEGORY_COL = 1;
    
    public static final String NOTIFICATION_MSG = "msg_number";
    public static final int NOTIFICATION_MSG_COL = 2;
    
    public static final String NOTIFICATION_TIME = "notification_time";
    public static final int NOTIFICATION_TIME_COL = 3;
    
    public static final String NOTIFICATION_ALERTMESSAGE = "alert_message";
    public static final int NOTIFICATION_ALERTMESSAGE_COL = 4;
    
    public static final String NOTIFICATION_FULLTEXT = "full_text";
    public static final int NOTIFICATION_FULLTEXT_COL = 5;
    
    public static final String NOTIFICATION_ALARMID = "alarm_id";
    public static final int NOTIFICATION_ALARMID_COL = 6;
    
    public static final String NOTIFICATION_ACTION = "action";
    public static final int NOTIFICATION_ACTION_COL = 7;
    
    public static final String NOTIFICATION_WEEK = "week";
    public static final int NOTIFICATION_WEEK_COL = 8;
    
    public static final String NOTIFICATION_DAY = "day";
    public static final int NOTIFICATION_DAY_COL = 9;
    
    public static final String NOTIFICATION_STOP = "stop";
    public static final int NOTIFICATION_STOP_COL = 10;
    
    public static final String NOTIFICATION_ESALERTMESSAGE = "esalert_message";
    public static final int NOTIFICATION_ESALERTMESSAGE_COL = 11;
    
    public static final String NOTIFICATION_ESFULLTEXT = "esfull_text";
    public static final int NOTIFICATION_ESFULLTEXT_COL = 12;
    
    public static final String NOTIFICATION_LANGUAGE = "language";
    public static final int NOTIFICATION_LANGUAGE_COL = 13;
    
    public static final String NOTIFICATION_CONFTIME = "conf_time";
    public static final int NOTIFICATION_CONFTIME_COL = 14;
    
 
    private NotificationDatabaseHelper notificationDBHelper; 
    
    @Override
	public boolean onCreate() {

    	notificationDBHelper = new NotificationDatabaseHelper(getContext(), NotificationDatabaseHelper.DATABASE_NAME, 
    			                                                 null, NotificationDatabaseHelper.DATABASE_VERSION);
    	logger.trace("onCreate()");
		return true;
	}
    

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		logger.trace("query()");
		SQLiteDatabase db = notificationDBHelper.getWritableDatabase();
		
		String groupBy = null;
		String having = null;

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		queryBuilder.setTables(NotificationDatabaseHelper.NOTIFICATION_TABLE);
		
		
		switch (uriMatcher.match(uri)) {
		    case SINGLE_ROW :
		    String rowID = uri.getPathSegments().get(1);
		    queryBuilder.appendWhere(NOTIFICATION_ID + "=" + rowID);
		    default: break;
		}
		
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
		
		return cursor;

	}

	@Override
	public String getType(Uri uri) {
		logger.trace("getType()");
		switch(uriMatcher.match(uri)) {
		    case ALL_ROWS: return "vnd.android.cursor.dir/vnd.vanderbilt.chew.notifications";
		    case SINGLE_ROW: return "vnd.android.cursor.item/vnd.vanderbilt.chew.notifications";
		    default: throw new IllegalArgumentException("Unsupported URI: " + uri);	
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		logger.trace("insert()");
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		logger.trace("delete()");
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,	String[] selectionArgs) {
		logger.trace("update()");
		SQLiteDatabase db = notificationDBHelper.getWritableDatabase();
		int updateCount = 0;

		switch (uriMatcher.match(uri)) {
		    case ALL_ROWS:
		    	updateCount = db.update(NotificationDatabaseHelper.NOTIFICATION_TABLE, values, selection, selectionArgs);
		    	Log.d(TAG, "ATTENTION: UPDATE ALL_ROWS ");
		    	logger.debug("ATTENTION: UPDATE ALL_ROWS ");
		    	break;

		    case SINGLE_ROW :
		        String rowID = uri.getPathSegments().get(1);
		        selection = NOTIFICATION_ID + "=" + rowID  + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
		        Log.d(TAG, "ATTENTION: UPDATE SINGLE_ROW");
		        logger.debug("ATTENTION: UPDATE SINGLE_ROW");

				updateCount = db.update(NotificationDatabaseHelper.NOTIFICATION_TABLE, values, selection, selectionArgs);
				break;
		    default:
				updateCount = db.update(NotificationDatabaseHelper.NOTIFICATION_TABLE, values, selection, selectionArgs);
		    	Log.d(TAG, "ATTENTION: UPDATE DEFAULT");
		    	logger.debug("ATTENTION: UPDATE DEFAULT");
			    break;
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;

	}

	/**
	 *DATABASEHELPER 
	 */
	private static class NotificationDatabaseHelper extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME = "chewNotificationDatabase"; 
		private static final int DATABASE_VERSION = 1;
		
		//Table Names
		private static final String NOTIFICATION_TABLE = "Notification";
		
		//SQL statements to create the new database
		private static final String NOTIFICATION_TABLE_CREATE = "create table " + NOTIFICATION_TABLE + " (" + 
		        NOTIFICATION_ID + " integer primary key autoincrement, " +
				NOTIFICATION_CATEGORY + " text, "             + 
				NOTIFICATION_MSG + " integer, "               +
				NOTIFICATION_TIME + " long, "                 +
				NOTIFICATION_ALERTMESSAGE + " text, "         +
				NOTIFICATION_FULLTEXT + " text, "             +
				NOTIFICATION_ALARMID + " integer, "           +
				NOTIFICATION_ACTION + " text, "               +
				NOTIFICATION_WEEK + " integer, "              +
				NOTIFICATION_DAY + " integer, "                  +
				NOTIFICATION_STOP + " text, "                 +
				NOTIFICATION_ESALERTMESSAGE + " text, "       +
				NOTIFICATION_ESFULLTEXT + " text, "           +
				NOTIFICATION_LANGUAGE + " text, "             +
				NOTIFICATION_CONFTIME + " text "              +
			    ");";

		public NotificationDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super (context, name, factory, version);
			logger.trace("NotificationDatabaseHelper()");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			logger.trace("NotificationDatabaseHelper.onCreate()");
			db.execSQL(NOTIFICATION_TABLE_CREATE);
		
			Log.d(TAG, "CREATING DATABASE");
			logger.debug("CREATING DATABASE");
  
			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (1, 'Program Week', 1, " + 0 + ", 'Welcome to CHEW Week 1', 'Welcome to Week 1 of the Children Eating Well (CHEW) program!', -1, 'RecipesActivity', 1, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 1', 'Bienvenida a la semana 1 del Programa de los niños comer bien (CHEW)!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (2, 'Program Week', 2, " + 0 + ", 'Welcome to CHEW Week 2', 'Thanks for being part of the CHEW program! You are now starting Week 2.', -1, 'MainActivity', 2, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 2', 'Gracias por ser parte del programa CHEW! Ahora está empezando la Semana 2.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (3, 'Program Week', 3, " + 0 + ", 'Welcome to CHEW Week 3', 'Healthy is the way to be! Welcome to CHEW program Week 3!', -1, 'NoAction', 3, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 3', 'Saludable es la manera de ser! Bienvenido a CHEW programa de la Semana 3!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (4, 'Program Week', 4, " + 0 + ", 'Welcome to CHEW Week 4', 'Now starting CHEW program Week 4! Have you used the WIC shopping tool yet?', -1, 'NoAction', 4, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 4', 'Ahora al iniciar el programa CHEW Semana 4! ¿Ha utilizado la herramienta de compras de WIC todavía?', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (5, 'WIC Shopping Tool', 1, " + 0 + ", 'CHEW WIC Vouchers', 'Ready to use your WIC vouchers? Use your WIC shopping tools to help make shopping a breeze!', -1, 'Produce',  1, " + Calendar.MONDAY + ", 'FALSE', 'CHEW WIC Vales', 'Listo para usar sus cupones de WIC? Utilice las herramientas de compras de WIC para ayudar a hacer las compras una brisa!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (6, 'WIC Shopping Tool', 2, " + 0 + ", 'CHEW Cash Voucher Calculator', 'The cash voucher calculator can help you add up the costs of fruits and vegetables you want to buy.', -1, 'MembersListView',  1, " + Calendar.TUESDAY + ", 'FALSE', 'CHEW Cash Voucher Calculadora', 'La calculadora de bono en efectivo puede ayudar a realizar la suma de los costos de las frutas y verduras que usted desea comprar.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
			
			
			
		}

		//Called to upgrade the database when there is a version mismatch
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			logger.trace("NotificationDatabaseHelper.onUpgrade()");
			Log.w("CHEW DB Adapter", "Upgrading from version " + oldVersion + " to " + newVersion + 
					", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE);
			onCreate(db);
		}
	}
}
