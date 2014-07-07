package com.vanderbilt.isis.chew.vouchers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;

import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.stores.Walmart;

public class Entry {

	private static final Logger logger = LoggerFactory.getLogger(Entry.class);
	
	private int option1;
	private int option2;
	private int option3;
	private int option4;
	private int op1_comb_quantity;
	
	public Entry(int o1, int o2, int o3, int o4, int o1_c){
		logger.trace("Entry()");
		option1 = o1;
		option2 = o2;
		option3 = o3;
		option4 = o4;
		op1_comb_quantity =  o1_c;
	}
	
    public static Entry fromCursor(Cursor curEntries) {
    	logger.trace("fromCursor()");
        int o1 = Integer.parseInt(curEntries.getString(curEntries.getColumnIndex(ChewContract.Voucher.OPTION1)));
        int o2 = Integer.parseInt(curEntries.getString(curEntries.getColumnIndex(ChewContract.Voucher.OPTION2)));
        int o3 = Integer.parseInt(curEntries.getString(curEntries.getColumnIndex(ChewContract.Voucher.OPTION3)));
        int o4 = Integer.parseInt(curEntries.getString(curEntries.getColumnIndex(ChewContract.Voucher.OPTION4)));
        int o1_c = Integer.parseInt(curEntries.getString(curEntries.getColumnIndex(ChewContract.Voucher.OP1_COMB_QUANTITY)));

        return new Entry(o1, o2, o3, o4, o1_c);
    }
}

