package com.vanderbilt.isis.chew.notificationmsg;

import android.net.Uri;

public class ChewAppLibrary {
	
	public static final String AUTHORITY = "com.vanderbilt.isis.chew.notificationmsg.NotificationProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notifications");

    //Default Time for showing daily notifications
    public static final String DEFAULT_CONF_TIME = "10";
    
	//Table Names
	public static final String NOTIFICATION_TABLE = "Notification";
	

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
    
    //ID numbers for Loaders
    public static final int NOTIFICATION_LOADER = 100;
    

}
