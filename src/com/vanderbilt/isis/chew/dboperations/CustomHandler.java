package com.vanderbilt.isis.chew.dboperations;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

public abstract class CustomHandler extends AsyncQueryHandler {
    
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
}
