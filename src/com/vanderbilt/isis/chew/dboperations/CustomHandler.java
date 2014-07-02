package com.vanderbilt.isis.chew.dboperations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.db.ChewDBHelper;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

public abstract class CustomHandler extends AsyncQueryHandler {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    public CustomHandler(Context context) {
            super(context.getContentResolver());

            logger.trace("CustomHandler()");

        }
    
    public static class InsertDataHandler extends CustomHandler{
        
    
   
        public InsertDataHandler(Context context) {
            super(context);

            logger.trace("InsertDataHandler()");

        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Log.d("async", "insert completed");
                logger.debug("async {}", "insert completed");

                logger.trace("onInsertComplete()");
       
        }
    }
    
    public static class UpdateDataHandler extends CustomHandler{
        
        
    	   
        public UpdateDataHandler(Context context) {
            super(context);
            logger.trace("UpdateDataHandler.onInsertComplete()");
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
        	    logger.trace("UpdateDataHandler.onUpdateComplete()");
                Log.d("async", "update completed");
                logger.debug("async {}", "update completed");

        }
    }
}
