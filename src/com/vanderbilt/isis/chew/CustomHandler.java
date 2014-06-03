package com.vanderbilt.isis.chew;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CursorTreeAdapter;
import android.widget.SimpleCursorAdapter;

abstract class CustomHandler extends AsyncQueryHandler {
    
    public CustomHandler(Context context) {
            super(context.getContentResolver());        
        }
    
    public static class InsertDataHandler extends CustomHandler{
        
    
   
        public InsertDataHandler(Context context) {
            super(context);
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Log.d("async", "insert completed");
       
        }
    }
    
    public static class UpdateDataHandler extends CustomHandler{
        
        
    	   
        public UpdateDataHandler(Context context) {
            super(context);
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
                Log.d("async", "update completed");
        }
    }
    
    /*public static class CursorC1Handler extends CustomHandler{
        private Cursor myCursor = null; 

        public CursorC1Handler(Context context) {
            super(context);
            }  
        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor){
            myCursor = cursor;
            Log.d("async"," properly assigned the cursor");
            
        }
        
        public  Cursor getCursor(){
            return myCursor;
        }
    } */ 
}
