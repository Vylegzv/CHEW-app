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
  
	//		db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (1, 'Program Week', 1, " + 0 + ", 'Welcome to CHEW Week 1', 'Welcome to Week 1 of the Children Eating Well (CHEW) program!', -1, 'RecipesActivity', 1, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 1', 'Bienvenida a la semana 1 del Programa de los niños comer bien (CHEW)!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
	//		db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (2, 'Program Week', 2, " + 0 + ", 'Welcome to CHEW Week 2', 'Thanks for being part of the CHEW program! You are now starting Week 2.', -1, 'MainActivity', 2, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 2', 'Gracias por ser parte del programa CHEW! Ahora está empezando la Semana 2.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
	//		db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (3, 'Program Week', 3, " + 0 + ", 'Welcome to CHEW Week 3', 'Healthy is the way to be! Welcome to CHEW program Week 3!', -1, 'NoAction', 3, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 3', 'Saludable es la manera de ser! Bienvenido a CHEW programa de la Semana 3!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
	//		db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (4, 'Program Week', 4, " + 0 + ", 'Welcome to CHEW Week 4', 'Now starting CHEW program Week 4! Have you used the WIC shopping tool yet?', -1, 'NoAction', 4, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 4', 'Ahora al iniciar el programa CHEW Semana 4! ¿Ha utilizado la herramienta de compras de WIC todavía?', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
	//		db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (5, 'WIC Shopping Tool', 1, " + 0 + ", 'CHEW WIC Vouchers', 'Ready to use your WIC vouchers? Use your WIC shopping tools to help make shopping a breeze!', -1, 'Produce',  1, " + Calendar.MONDAY + ", 'FALSE', 'CHEW WIC Vales', 'Listo para usar sus cupones de WIC? Utilice las herramientas de compras de WIC para ayudar a hacer las compras una brisa!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
	//		db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (6, 'WIC Shopping Tool', 2, " + 0 + ", 'CHEW Cash Voucher Calculator', 'The cash voucher calculator can help you add up the costs of fruits and vegetables you want to buy.', -1, 'MembersListView',  1, " + Calendar.TUESDAY + ", 'FALSE', 'CHEW Cash Voucher Calculadora', 'La calculadora de bono en efectivo puede ayudar a realizar la suma de los costos de las frutas y verduras que usted desea comprar.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");
			
			
		
			/********/

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (1, 'Program Week', 1, " + 0 + ", 'Welcome to CHEW Week 1!', 'Welcome to Week 1 of the Children Eating Well (CHEW) program!', -1, 'NoAction', 1, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 1', 'Bienvenida a la Semana #1 del programa los Niños Comiendo Bien (CHEW siglas en inglés)!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (2, 'Program Week', 2, " + 0 + ", 'Welcome to CHEW Week 2!', 'Thanks for being part of the CHEW program! You are now starting Week 2.', -1, 'NoAction', 2, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 2', 'Gracias por ser parte del programa de CHEW! Ahora está comenzando la Semana #2.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (3, 'Program Week', 3, " + 0 + ", 'Welcome to CHEW Week 3!', 'Healthy is the way to be! Welcome to CHEW program Week 3!', -1, 'NoAction', 3, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 3', 'Es bueno ser saludables! Bienvenida a la Semana #3 del programa CHEW!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (4, 'Program Week', 4, " + 0 + ", 'Welcome to CHEW Week 4!', 'Now starting CHEW program Week 4! Have you used the WIC shopping tool yet?', -1, 'NoAction', 4, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 4', 'Ahora comenzamos la Semana #4 del program CHEW! Ya ha usado la herramienta de compras de WIC?', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (5, 'Program Week', 5, " + 0 + ", 'Welcome to CHEW Week 5!', 'Thanks for being part of the CHEW program! You are now starting Week 5.', -1, 'NoAction', 5, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 5', 'Gracias por ser parte del programa de CHEW! Ahora está comenzando la Semana #5.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (6, 'Program Week', 6, " + 0 + ", 'Welcome to CHEW Week 6!', 'Welcome to Week 6 of the Children Eating Well (CHEW) program!', -1, 'NoAction', 6, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 6', 'Bienvenida a la Semana #6 del programa los Niños Comiendo Bien (CHEW siglas en inglés)!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (7, 'Program Week', 7, " + 0 + ", 'Welcome to CHEW Week 7!', 'We are halfway there! Now starting Week 7 of the CHEW program.', -1, 'NoAction', 7, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 7', 'Estamos en la mitad del camino! Ahora comenzando la Semana #7 del programa CHEW.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (8, 'Program Week', 8, " + 0 + ", 'Welcome to CHEW Week 8!', 'Week 8 - It is not too late! Try a new healthy snack today!', -1, 'NoAction', 8, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 8', 'Semana #8 - Nunca es tarde! Pruebe un nuevo snack saludable hoy!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (9, 'Program Week', 9, " + 0 + ", 'Welcome to CHEW Week 9!', 'Thanks for being part of the CHEW program! You are now starting Week 9.', -1, 'NoAction', 9, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 9', 'Gracias por ser parte del programa de CHEW! Ahora está comenzando la Semana# 9', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (10, 'Program Week', 10, " + 0 + ", 'Welcome to CHEW Week 10!', 'Welcome to Week 10 of the Children Eating Well (CHEW) program!', -1, 'NoAction', 10, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 10', 'Bienvenida a la Semana #10 del programa los Niños Comiendo Bien (CHEW siglas en inglés)!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (11, 'Program Week', 11, " + 0 + ", 'Welcome to CHEW Week 11!', 'Now starting CHEW program Week 11! Have you checked out the Yummy Snack Gallery lately?', -1, 'NoAction', 11, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 11', 'Ahora, comenzando la Semana #11 del program CHEW! Ha checado ultimamente la galería de Snacks Deliciosos?', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (12, 'Program Week', 12, " + 0 + ", 'Welcome to CHEW Week 12!', 'You did it! You made it to the last week of the CHEW program! ', -1, 'NoAction', 12, " + Calendar.SUNDAY + ", 'FALSE', 'Bienvenido a CHEW Semana 12', 'Lo logró! Usted participó hasta la ultima semana del programa de CHEW!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (13, 'WIC Shopping Tool', 1, " + 0 + ", 'Ready to use your WIC vouchers?', 'Ready to use your WIC vouchers? Use your WIC shopping tools to help make shopping a breeze!', -1, 'MainActivity', 1, " + Calendar.SUNDAY + ", 'FALSE', 'Está lista par usar sus cupones de WIC?', 'Está lista par usar sus cupones de WIC? Use la herramienta de compras de WIC para hacer sus compras fáciles!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (14, 'WIC Shopping Tool', 2, " + 0 + ", 'Cash Voucher Calculator!', 'The cash voucher calculator can help you add up the costs of fruits and vegetables you want to buy.', -1, 'Produce', 2, " + Calendar.SUNDAY + ", 'FALSE', 'Cash Vale Calculadora', 'El calculador para los cupones en efectivo puede ayudarle a sumar el costo de las frutas y verduras que quiere comprar.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (15, 'WIC Shopping Tool', 3, " + 0 + ", 'Manage your Family WIC Vouchers!', 'Your WIC shopping tools can help you manage all the WIC vouchers for your family.', -1, 'MembersListView', 4, " + Calendar.SUNDAY + ", 'FALSE', 'Gestione vales de WIC de su Familia', 'La herramienta de compras de WIC puede ayudarle a manejar todos los cupones de WIC para su familia.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (16, 'WIC Shopping Tool', 4, " + 0 + ", 'Use your WIC Shopping Tools!', 'Remember to use your WIC shopping tools when you are ready to buy WIC items at the store.', -1, 'MainActivity', 6, " + Calendar.SUNDAY + ", 'FALSE', 'Utilice sus WIC Herramientas de compra', 'Recuerde usar la herramienta de compras de WIC cuando esté lista para comprar los productos de WIC en la tienda.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (17, 'WIC Shopping Tool', 5, " + 0 + ", 'WIC Participants In Your Family!', 'You can shop for all WIC participants in the family using your WIC shopping tools.', -1, 'MembersListView', 8, " + Calendar.SUNDAY + ", 'FALSE', 'Los participantes de WIC en su familia', 'Puede comprar para todos los participantes de WIC en su familia usando la herramienta de compras de WIC.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (18, 'WIC Shopping Tool', 6, " + 0 + ", 'Need help with your WIC vouchers?', 'Need help with your WIC vouchers? Your WIC shopping tools can help you choose the right items.', -1, 'MainActivity', 10, " + Calendar.SUNDAY + ", 'FALSE', 'Necesita ayuda con sus cupones de WIC?', 'Necesita ayuda con sus cupones de WIC? La herramienta de compras de WIC puede ayudarle a escoger los productos correctos.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (19, 'Snack Gallery', 1, " + 0 + ", 'Just a few minutes until snack time?', 'Just a few minutes until snack time? Check out the Yummy Snack Gallery for quick and tasty ideas!', -1, 'RecipesActivity', 1, " + Calendar.TUESDAY + ", 'FALSE', 'A pocos minutos del tiempo para un snack?', 'A pocos minutos del tiempo para un snack? Cheque la Galería de Snacks Deliciosos para ideas rápidas y deliciosas!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (20, 'Snack Gallery', 2, " + 0 + ", 'Snack Gallery!', 'Have your little chef help make one of the easy, delicious, kid-friendly snacks in Yummy Snack Gallery!', -1, 'RecipesActivity', 2, " + Calendar.TUESDAY + ", 'FALSE', 'Galería de Snacks!', 'Invite a su pequeño cocinero que le ayude a hacer uno de los snacks deliciosos, fáciles, y agradables para los niños en la Galería de Snacks Deliciosos!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (21, 'Snack Gallery', 3, " + 0 + ", 'Check out our Yummy Snack Gallery.', 'Check out our Yummy Snack Gallery for tasty and easy-to-make snacks!', -1, 'RecipesActivity', 3, " + Calendar.SUNDAY + ", 'FALSE', 'Cheque nuestra Galería de Snacks Deliciosos', 'Cheque nuestra Galería de Snacks Deliciosos por snacks sabrosos y fáciles de hacer!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (22, 'Beverages', 1, " + 0 + ", 'Thirsty? Drink Only Water!', 'Thirst is your body’s way of saying it needs water, not juice, soda, sports drinks, or anything sweet!', -1, 'MainActivity', 1, " + Calendar.SATURDAY + ", 'FALSE', 'Tienes sed? Beba sólo agua!', 'Le da sed porque necesita agua--no jugo, sodas, bebidas deportivas, o cualquier cosa dulce!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (23, 'Beverages', 2, " + 0 + ", 'Thirsty? Drink Only Water!', 'If your child isn’t used to drinking water, add slices of fresh fruit for some color and flavor!', -1, 'MainActivity', 2, " + Calendar.SATURDAY + ", 'FALSE', 'Tienes sed? Beba sólo agua!', 'Si sus niños no están acostumbrados a tomar agua, aumente unas rodajas de fruta fresca para color y sabor!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (24, 'Beverages', 3, " + 0 + ", 'Whole Milk or Skim Milk?', 'Whole milk is for toddlers under two. For older kids and adults, gradually switch to 2% then 1% and then skim. Try mixing them for an easier switch!', -1, 'MainActivity', 3, " + Calendar.SATURDAY + ", 'FALSE', 'Leche entera o descremada?', 'La leche entera es para niños menores de 2 años. Para niños mayores y adultos, gradualmente cambie a 2%, después a 1%, y luego a descremada. Puede mezclarlas para una cambio más fácil!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (25, 'Beverages', 4, " + 0 + ", 'Eat Fruit, Not Juice!', 'It’s better to eat fruit than drink it! Even 100% juice should be saved for a special treat.', -1, 'MainActivity', 4, " + Calendar.SATURDAY + ", 'FALSE', 'Coma frutas, No Jugo', 'Es mejor comer fruta que tomarla! Incluso el jugo 100% natural debe ser guardado para ocaciones especiales.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (26, 'Beverages', 5, " + 0 + ", 'Promote Healthy Teeth And Healthy Growth!', 'Promote healthy teeth and healthy growth by cutting out sodas and sugary drinks for your child. Be smarter and drink water!', -1, 'MainActivity', 5, " + Calendar.SATURDAY + ", 'FALSE', 'Promueva los dientes saludables y el crecimiento saludable', 'Promueva los dientes saludables y el crecimiento saludable cortando las sodas y las bebidas azucaradas para sus niños. Sea inteligente y tome agua!!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (27, 'Beverages', 6, " + 0 + ", 'Think Before You Drink!', 'Think before you drink—serve water or low-fat milk instead of soda, which is linked to excess weight gain and tooth decay.', -1, 'MainActivity', 6, " + Calendar.SATURDAY + ", 'FALSE', 'Piense Antes de Tomar', 'Piense antes de tomar--sirva agua o leche baja en grasa en lugar de soda, la cual está contribuye al aumento de peso excesivo y caries dentales.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (28, 'Kid Friendly Snacking', 1, " + 0 + ", 'Fresh Fruits and Vegetables!', 'Fresh fruits and vegetables make a great snack for both kids and parents!', -1, 'MainActivity', 1, " + Calendar.WEDNESDAY + ", 'FALSE', 'Frutas y verduras frescas', 'Frutas y verduras frescas son buenos snacks para ambos niños y padres!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (29, 'Kid Friendly Snacking', 2, " + 0 + ", 'Fruit as an Afternoon Snack!', 'Let healthy foods fill the gaps! If your child misses fruit at lunch, offer it as an afternoon snack.', -1, 'MainActivity', 2, " + Calendar.WEDNESDAY + ", 'FALSE', 'Frutas como la merienda', 'Deje que las comidas saludables llenen los espacios! Si sus niños no comen fruta en el almuerzo, ofrézcales como un snack en la tarde.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (30, 'Kid Friendly Snacking', 3, " + 0 + ", 'Kid-Friendly Snacking!', 'Your little one has a small stomach. Be sure to provide 3 meals and 2-3 healthy snacks each day.', -1, 'MainActivity', 3, " + Calendar.WEDNESDAY + ", 'FALSE', 'Snacks Agradables para los Niños', 'Los niños pequeños tienen estómagos pequeños. Asegúrese de darles 3 comidas y 2 o 3 snacks saludables cada dia.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (31, 'Kid Friendly Snacking', 4, " + 0 + ", 'Make Every Bite Count!', 'Make every bite count! Offer your child fruits, vegetables, and low-fat dairy products as snacks in between meals.', -1, 'MainActivity', 4, " + Calendar.WEDNESDAY + ", 'FALSE', 'Haga que cada momento cuente', 'Haga que cada momento cuente! Ofrezca a sus niños frutas, verduras, y productos lácteos bajos en grasa como snacks entre comidas.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (32, 'Kid Friendly Snacking', 5, " + 0 + ", 'Fresh Fruits and Vegetables!', 'Keep fruits and vegetables where your little one can see them, like on the table or eye-level in the refrigerator!', -1, 'MainActivity', 5, " + Calendar.WEDNESDAY + ", 'FALSE', 'Frutas y verduras frescas', 'Mantenga frutas y verduras donde los niños puedan verlas, como en la mesa o a la altura de sus ojos en el refrigerador.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (33, 'Kid Friendly Snacking', 6, " + 0 + ", 'Fresh Fruits and Vegetables!', 'Offer your kids fruits and vegetables from each color of the rainbow - to get a variety of vitamins, minerals, and nutrients!', -1, 'MainActivity', 6, " + Calendar.WEDNESDAY + ", 'FALSE', 'Frutas y verduras frescas', 'Ofrezca a sus niños frutas y verduras de todos los colores del arco iris - para tener una variedad de vitaminas, minerales, y nutrientes!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (34, 'New Foods', 1, " + 0 + ", 'New Foods - Stay Positive!', 'It may take more than 12 tries for your child to like a new food. Stay positive and keep motivating them to try a bite!', -1, 'MainActivity', 1, " + Calendar.THURSDAY + ", 'FALSE', 'Comidas Nuevas - Manténgase Positiva', 'Podría tomar más de 12 intentos para que a sus niños les guste una comida nueva. Manténgase positiva y siga motivándoles para que prueben un bocado!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (35, 'New Foods', 2, " + 0 + ", 'Picky eater? - Be Creative!', 'Picky eater? Try arranging the food in a fun and creative way!', -1, 'MainActivity', 2, " + Calendar.THURSDAY + ", 'FALSE', 'Los niños son exigentes para comer?', 'Los niños son exigentes para comer? Trate de servir la comida de manera creativa y divertida!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (36, 'New Foods', 3, " + 0 + ", 'Let your kids be Produce Pickers!', 'Let your kids be - Produce Pickers - choosing fruits and vegetables at the store to try when they get home.', -1, 'MainActivity', 3, " + Calendar.THURSDAY + ", 'FALSE', 'Deje que sus niños escojan verduras y frutas', 'Deje que sus niños escojan algunas verduras y frutas en la tienda para que las prueben cuando lleguen a casa.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (37, 'Role Models', 1, " + 0 + ", 'Keep things positive!', 'Keep things positive! Ask older children and other family members not to make yucky faces or negative comments about new or unfamiliar foods.', -1, 'MainActivity', 1, " + Calendar.FRIDAY + ", 'FALSE', 'Mantenga las cosas positivas!', 'Mantenga las cosas positivas! Pida a los niños mayores y a otros miembros de la familia a no hacer caras de disgusto o comentarios negativos sobre las comidas nuevas o no muy conocidas.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (38, 'Role Models', 2, " + 0 + ", 'Be a great role model!', 'Be a great role model! Let your child see you enjoying fruits, vegetables, water, and low-fat dairy every day.', -1, 'MainActivity', 2, " + Calendar.FRIDAY + ", 'FALSE', 'Sea un buen ejemplo!', 'Sea un buen ejemplo! Deje que sus niños le vean disfrutando de las frutas, verduras, agua, y productos lácteos bajos en grasa cada día.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (39, 'Role Models', 3, " + 0 + ", 'Be a great role model!', 'Show your child how to make healthy choices when you are on the go. Put oranges, bananas, or other fruits in your bag for quick snacks.', -1, 'MainActivity', 3, " + Calendar.FRIDAY + ", 'FALSE', 'Sea un buen ejemplo!', 'Muestre a sus niños cómo tomar decisiones saludables cuando están en el carro. Ponga naranjas, plátanos, u otras frutas en su bolsa para tener snacks rápidos a la mano.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (40, 'Health Benefits', 1, " + 0 + ", 'Eyes need Vitamin A to see well!', 'Eyes need Vitamin A to see well! Get it from: Carrots * Red Leaf Lettuce * Broccoli * Spinach * Dark Leafy Greens', -1, 'MainActivity', 1, " + Calendar.MONDAY + ", 'FALSE', 'Los ojos necesitan Vitamina A para ver bien', 'Los ojos necesitan Vitamina A para ver bien! Obténgala de: Zanahorias * Lechuga de Hoja Roja * Brócoli * Espinaca * Verduras de Hojas Oscuras', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (41, 'Health Benefits', 2, " + 0 + ", 'Vitamin C strengthens skin, blood vessels, and bones', 'Vitamin C strengthens skin, blood vessels, and bones! Get it from: Tomatoes * Oranges * Kiwi * Broccoli * Spinach * Strawberries * Cauliflower * Bell Peppers', -1, 'MainActivity', 2, " + Calendar.MONDAY + ", 'FALSE', 'La Vitamina C refuerza la piel, los vasos sanguíneos, y los huesos', 'La Vitamina C refuerza la piel, los vasos sanguíneos, y los huesos! Obténgala de: Tomates * Naranjas * Kiwi * Brócoli * Espinaca * Fresas * Coliflor * Chile Campana o Pimiento Verde', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (42, 'Health Benefits', 3, " + 0 + ", 'Fiber helps the tummy do its work', 'Fiber helps the tummy do its work and helps you go regularly! Get it from: Apples * Carrots * Broccoli * Pears * Spinach', -1, 'MainActivity', 3, " + Calendar.MONDAY + ", 'FALSE', 'La Fibra ayuda al estómago para que haga su trabajo', 'La Fibra ayuda al estómago para que haga su trabajo y ayuda a ir al baño regularmente! Obténgala de: Manzanas * Zanahorias * Brócoli * Peras * Espinaca', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

//Duplicate


			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (43, 'WIC Shopping Tool', 1, " + 0 + ", 'Ready to use your WIC vouchers?', 'Ready to use your WIC vouchers? Use your WIC shopping tools to help make shopping a breeze!', -1, 'MainActivity', 12, " + Calendar.SUNDAY + ", 'FALSE', 'Está lista par usar sus cupones de WIC?', 'Está lista par usar sus cupones de WIC? Use la herramienta de compras de WIC para hacer sus compras fáciles!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (44, 'Snack Gallery', 1, " + 0 + ", 'Just a few minutes until snack time?', 'Just a few minutes until snack time? Check out the Yummy Snack Gallery for quick and tasty ideas!', -1, 'RecipesActivity', 5, " + Calendar.SUNDAY + ", 'FALSE', 'A pocos minutos del tiempo para un snack?', 'A pocos minutos del tiempo para un snack? Cheque la Galería de Snacks Deliciosos para ideas rápidas y deliciosas!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (45, 'Snack Gallery', 2, " + 0 + ", 'Snack Gallery!', 'Have your little chef help make one of the easy, delicious, kid-friendly snacks in Yummy Snack Gallery!', -1, 'RecipesActivity', 7, " + Calendar.SUNDAY + ", 'FALSE', 'Galería de Snacks!', 'Invite a su pequeño cocinero que le ayude a hacer uno de los snacks deliciosos, fáciles, y agradables para los niños en la Galería de Snacks Deliciosos!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (46, 'Snack Gallery', 3, " + 0 + ", 'Check out our Yummy Snack Gallery.', 'Check out our Yummy Snack Gallery for tasty and easy-to-make snacks!', -1, 'RecipesActivity', 9, " + Calendar.SUNDAY + ", 'FALSE', 'Cheque nuestra Galería de Snacks Deliciosos', 'Cheque nuestra Galería de Snacks Deliciosos por snacks sabrosos y fáciles de hacer!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (47, 'Snack Gallery', 1, " + 0 + ", 'Just a few minutes until snack time?', 'Just a few minutes until snack time? Check out the Yummy Snack Gallery for quick and tasty ideas!', -1, 'RecipesActivity', 11, " + Calendar.SUNDAY + ", 'FALSE', 'A pocos minutos del tiempo para un snack?', 'A pocos minutos del tiempo para un snack? Cheque la Galería de Snacks Deliciosos para ideas rápidas y deliciosas!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (48, 'Beverages', 2, " + 0 + ", 'Thirsty? Drink Only Water!', 'If your child isn’t used to drinking water, add slices of fresh fruit for some color and flavor!', -1, 'MainActivity', 7, " + Calendar.SATURDAY + ", 'FALSE', 'Tienes sed? Beba sólo agua!', 'Si sus niños no están acostumbrados a tomar agua, aumente unas rodajas de fruta fresca para color y sabor!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (49, 'Beverages', 1, " + 0 + ", 'Thirsty? Drink Only Water!', 'Thirst is your body’s way of saying it needs water, not juice, soda, sports drinks, or anything sweet!', -1, 'MainActivity', 8, " + Calendar.SATURDAY + ", 'FALSE', 'Tienes sed? Beba sólo agua!', 'Le da sed porque necesita agua--no jugo, sodas, bebidas deportivas, o cualquier cosa dulce!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (50, 'Beverages', 4, " + 0 + ", 'Eat Fruit, Not Juice!', 'It’s better to eat fruit than drink it! Even 100% juice should be saved for a special treat.', -1, 'MainActivity', 9, " + Calendar.SATURDAY + ", 'FALSE', 'Coma frutas, No Jugo', 'Es mejor comer fruta que tomarla! Incluso el jugo 100% natural debe ser guardado para ocaciones especiales.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (51, 'Beverages', 3, " + 0 + ", 'Whole Milk or Skim Milk?', 'Whole milk is for toddlers under two. For older kids and adults, gradually switch to 2% then 1% and then skim. Try mixing them for an easier switch!', -1, 'MainActivity', 10, " + Calendar.SATURDAY + ", 'FALSE', 'Leche entera o descremada?', 'La leche entera es para niños menores de 2 años. Para niños mayores y adultos, gradualmente cambie a 2%, después a 1%, y luego a descremada. Puede mezclarlas para una cambio más fácil!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (52, 'Beverages', 5, " + 0 + ", 'Promote Healthy Teeth And Healthy Growth!', 'Promote healthy teeth and healthy growth by cutting out sodas and sugary drinks for your child. Be smarter and drink water!', -1, 'MainActivity', 11, " + Calendar.SATURDAY + ", 'FALSE', 'Promueva los dientes saludables y el crecimiento saludable', 'Promueva los dientes saludables y el crecimiento saludable cortando las sodas y las bebidas azucaradas para sus niños. Sea inteligente y tome agua!!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (53, 'Beverages', 6, " + 0 + ", 'Think Before You Drink!', 'Think before you drink—serve water or low-fat milk instead of soda, which is linked to excess weight gain and tooth decay.', -1, 'MainActivity', 12, " + Calendar.SATURDAY + ", 'FALSE', 'Piense Antes de Tomar', 'Piense antes de tomar--sirva agua o leche baja en grasa en lugar de soda, la cual está contribuye al aumento de peso excesivo y caries dentales.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (54, 'Kid Friendly Snacking', 1, " + 0 + ", 'Fresh Fruits and Vegetables!', 'Fresh fruits and vegetables make a great snack for both kids and parents!', -1, 'MainActivity', 7, " + Calendar.WEDNESDAY + ", 'FALSE', 'Frutas y verduras frescas', 'Frutas y verduras frescas son buenos snacks para ambos niños y padres!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (55, 'Kid Friendly Snacking', 2, " + 0 + ", 'Fruit as an Afternoon Snack!', 'Let healthy foods fill the gaps! If your child misses fruit at lunch, offer it as an afternoon snack.', -1, 'MainActivity', 8, " + Calendar.WEDNESDAY + ", 'FALSE', 'Frutas como la merienda', 'Deje que las comidas saludables llenen los espacios! Si sus niños no comen fruta en el almuerzo, ofrézcales como un snack en la tarde.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (56, 'Kid Friendly Snacking', 3, " + 0 + ", 'Kid-Friendly Snacking!', 'Your little one has a small stomach. Be sure to provide 3 meals and 2-3 healthy snacks each day.', -1, 'MainActivity', 9, " + Calendar.WEDNESDAY + ", 'FALSE', 'Snacks Agradables para los Niños', 'Los niños pequeños tienen estómagos pequeños. Asegúrese de darles 3 comidas y 2 o 3 snacks saludables cada dia.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (57, 'Kid Friendly Snacking', 4, " + 0 + ", 'Make Every Bite Count!', 'Make every bite count! Offer your child fruits, vegetables, and low-fat dairy products as snacks in between meals.', -1, 'MainActivity', 10, " + Calendar.WEDNESDAY + ", 'FALSE', 'Haga que cada momento cuente', 'Haga que cada momento cuente! Ofrezca a sus niños frutas, verduras, y productos lácteos bajos en grasa como snacks entre comidas.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (58, 'Kid Friendly Snacking', 5, " + 0 + ", 'Fresh Fruits and Vegetables!', 'Keep fruits and vegetables where your little one can see them, like on the table or eye-level in the refrigerator!', -1, 'MainActivity', 11, " + Calendar.WEDNESDAY + ", 'FALSE', 'Frutas y verduras frescas', 'Mantenga frutas y verduras donde los niños puedan verlas, como en la mesa o a la altura de sus ojos en el refrigerador.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (59, 'Kid Friendly Snacking', 6, " + 0 + ", 'Fresh Fruits and Vegetables!', 'Offer your kids fruits and vegetables from each color of the rainbow - to get a variety of vitamins, minerals, and nutrients!', -1, 'MainActivity', 12, " + Calendar.WEDNESDAY + ", 'FALSE', 'Frutas y verduras frescas', 'Ofrezca a sus niños frutas y verduras de todos los colores del arco iris - para tener una variedad de vitaminas, minerales, y nutrientes!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (60, 'New Foods', 2, " + 0 + ", 'Picky eater? - Be Creative!', 'Picky eater? Try arranging the food in a fun and creative way!', -1, 'MainActivity', 4, " + Calendar.THURSDAY + ", 'FALSE', 'Los niños son exigentes para comer?', 'Los niños son exigentes para comer? Trate de servir la comida de manera creativa y divertida!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (61, 'New Foods', 1, " + 0 + ", 'New Foods - Stay Positive!', 'It may take more than 12 tries for your child to like a new food. Stay positive and keep motivating them to try a bite!', -1, 'MainActivity', 6, " + Calendar.THURSDAY + ", 'FALSE', 'Comidas Nuevas - Manténgase Positiva', 'Podría tomar más de 12 intentos para que a sus niños les guste una comida nueva. Manténgase positiva y siga motivándoles para que prueben un bocado!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (62, 'New Foods', 3, " + 0 + ", 'Let your kids be Produce Pickers!', 'Let your kids be - Produce Pickers - choosing fruits and vegetables at the store to try when they get home.', -1, 'MainActivity', 7, " + Calendar.THURSDAY + ", 'FALSE', 'Deje que sus niños escojan verduras y frutas', 'Deje que sus niños escojan algunas verduras y frutas en la tienda para que las prueben cuando lleguen a casa.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (63, 'New Foods', 2, " + 0 + ", 'Picky eater? - Be Creative!', 'Picky eater? Try arranging the food in a fun and creative way!', -1, 'MainActivity', 8, " + Calendar.THURSDAY + ", 'FALSE', 'Los niños son exigentes para comer?', 'Los niños son exigentes para comer? Trate de servir la comida de manera creativa y divertida!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (64, 'New Foods', 1, " + 0 + ", 'New Foods - Stay Positive!', 'It may take more than 12 tries for your child to like a new food. Stay positive and keep motivating them to try a bite!', -1, 'MainActivity', 10, " + Calendar.THURSDAY + ", 'FALSE', 'Comidas Nuevas - Manténgase Positiva', 'Podría tomar más de 12 intentos para que a sus niños les guste una comida nueva. Manténgase positiva y siga motivándoles para que prueben un bocado!', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (65, 'New Foods', 3, " + 0 + ", 'Let your kids be Produce Pickers!', 'Let your kids be - Produce Pickers - choosing fruits and vegetables at the store to try when they get home.', -1, 'MainActivity', 12, " + Calendar.THURSDAY + ", 'FALSE', 'Deje que sus niños escojan verduras y frutas', 'Deje que sus niños escojan algunas verduras y frutas en la tienda para que las prueben cuando lleguen a casa.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (66, 'Role Models', 1, " + 0 + ", 'Keep things positive!', 'Keep things positive! Ask older children and other family members not to make yucky faces or negative comments about new or unfamiliar foods.', -1, 'MainActivity', 4, " + Calendar.FRIDAY + ", 'FALSE', 'Mantenga las cosas positivas!', 'Mantenga las cosas positivas! Pida a los niños mayores y a otros miembros de la familia a no hacer caras de disgusto o comentarios negativos sobre las comidas nuevas o no muy conocidas.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (67, 'Role Models', 2, " + 0 + ", 'Be a great role model!', 'Be a great role model! Let your child see you enjoying fruits, vegetables, water, and low-fat dairy every day.', -1, 'MainActivity', 6, " + Calendar.FRIDAY + ", 'FALSE', 'Sea un buen ejemplo!', 'Sea un buen ejemplo! Deje que sus niños le vean disfrutando de las frutas, verduras, agua, y productos lácteos bajos en grasa cada día.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (68, 'Role Models', 3, " + 0 + ", 'Be a great role model!', 'Show your child how to make healthy choices when you are on the go. Put oranges, bananas, or other fruits in your bag for quick snacks.', -1, 'MainActivity', 7, " + Calendar.FRIDAY + ", 'FALSE', 'Sea un buen ejemplo!', 'Muestre a sus niños cómo tomar decisiones saludables cuando están en el carro. Ponga naranjas, plátanos, u otras frutas en su bolsa para tener snacks rápidos a la mano.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (69, 'Role Models', 1, " + 0 + ", 'Keep things positive!', 'Keep things positive! Ask older children and other family members not to make yucky faces or negative comments about new or unfamiliar foods.', -1, 'MainActivity', 9, " + Calendar.FRIDAY + ", 'FALSE', 'Mantenga las cosas positivas!', 'Mantenga las cosas positivas! Pida a los niños mayores y a otros miembros de la familia a no hacer caras de disgusto o comentarios negativos sobre las comidas nuevas o no muy conocidas.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (70, 'Role Models', 2, " + 0 + ", 'Be a great role model!', 'Be a great role model! Let your child see you enjoying fruits, vegetables, water, and low-fat dairy every day.', -1, 'MainActivity', 11, " + Calendar.FRIDAY + ", 'FALSE', 'Sea un buen ejemplo!', 'Sea un buen ejemplo! Deje que sus niños le vean disfrutando de las frutas, verduras, agua, y productos lácteos bajos en grasa cada día.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (71, 'Role Models', 3, " + 0 + ", 'Be a great role model!', 'Show your child how to make healthy choices when you are on the go. Put oranges, bananas, or other fruits in your bag for quick snacks.', -1, 'MainActivity', 12, " + Calendar.FRIDAY + ", 'FALSE', 'Sea un buen ejemplo!', 'Muestre a sus niños cómo tomar decisiones saludables cuando están en el carro. Ponga naranjas, plátanos, u otras frutas en su bolsa para tener snacks rápidos a la mano.', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (72, 'Health Benefits', 1, " + 0 + ", 'Eyes need Vitamin A to see well!', 'Eyes need Vitamin A to see well! Get it from: Carrots * Red Leaf Lettuce * Broccoli * Spinach * Dark Leafy Greens', -1, 'MainActivity', 5, " + Calendar.MONDAY + ", 'FALSE', 'Los ojos necesitan Vitamina A para ver bien', 'Los ojos necesitan Vitamina A para ver bien! Obténgala de: Zanahorias * Lechuga de Hoja Roja * Brócoli * Espinaca * Verduras de Hojas Oscuras', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (73, 'Health Benefits', 2, " + 0 + ", 'Vitamin C strengthens skin, blood vessels, and bones', 'Vitamin C strengthens skin, blood vessels, and bones! Get it from: Tomatoes * Oranges * Kiwi * Broccoli * Spinach * Strawberries * Cauliflower * Bell Peppers', -1, 'MainActivity', 7, " + Calendar.MONDAY + ", 'FALSE', 'La Vitamina C refuerza la piel, los vasos sanguíneos, y los huesos', 'La Vitamina C refuerza la piel, los vasos sanguíneos, y los huesos! Obténgala de: Tomates * Naranjas * Kiwi * Brócoli * Espinaca * Fresas * Coliflor * Chile Campana o Pimiento Verde', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (74, 'Health Benefits', 3, " + 0 + ", 'Fiber helps the tummy do its work', 'Fiber helps the tummy do its work and helps you go regularly! Get it from: Apples * Carrots * Broccoli * Pears * Spinach', -1, 'MainActivity', 8, " + Calendar.MONDAY + ", 'FALSE', 'La Fibra ayuda al estómago para que haga su trabajo', 'La Fibra ayuda al estómago para que haga su trabajo y ayuda a ir al baño regularmente! Obténgala de: Manzanas * Zanahorias * Brócoli * Peras * Espinaca', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (75, 'Health Benefits', 1, " + 0 + ", 'Eyes need Vitamin A to see well!', 'Eyes need Vitamin A to see well! Get it from: Carrots * Red Leaf Lettuce * Broccoli * Spinach * Dark Leafy Greens', -1, 'MainActivity', 11, " + Calendar.MONDAY + ", 'FALSE', 'Los ojos necesitan Vitamina A para ver bien', 'Los ojos necesitan Vitamina A para ver bien! Obténgala de: Zanahorias * Lechuga de Hoja Roja * Brócoli * Espinaca * Verduras de Hojas Oscuras', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");

			db.execSQL("Insert into " + NOTIFICATION_TABLE + " values (76, 'Health Benefits', 2, " + 0 + ", 'Vitamin C strengthens skin, blood vessels, and bones', 'Vitamin C strengthens skin, blood vessels, and bones! Get it from: Tomatoes * Oranges * Kiwi * Broccoli * Spinach * Strawberries * Cauliflower * Bell Peppers', -1, 'MainActivity', 12, " + Calendar.MONDAY + ", 'FALSE', 'La Vitamina C refuerza la piel, los vasos sanguíneos, y los huesos', 'La Vitamina C refuerza la piel, los vasos sanguíneos, y los huesos! Obténgala de: Tomates * Naranjas * Kiwi * Brócoli * Espinaca * Fresas * Coliflor * Chile Campana o Pimiento Verde', 'ENGLISH', " + ChewAppLibrary.DEFAULT_CONF_TIME + ")");


			/********/
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
