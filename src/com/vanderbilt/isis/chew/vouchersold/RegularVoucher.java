package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;
import java.util.Calendar;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public abstract class RegularVoucher {
	
	protected VoucherCode voucherCode;
	protected Month voucherMonth;
	protected String voucherType;
	
	// TODO: use factory to get the right voucher from String like A2.
	public RegularVoucher(Month month){
		
		voucherMonth = month;
	}
	
	public Month getMonth(){
		
		return voucherMonth;
	}
	
	public String getType(){
		
		return voucherType;
	}
	
	public boolean isExpired(){
		
	    int m = Calendar.getInstance().get(Calendar.MONTH);
		
		return !voucherMonth.equals(m);
				
	}
	
	public abstract ArrayList<String> getDescription();
	
	// maybe use iterator pattern to iterate?
	
	// need to create voucher entry class
	// and put entires (with food, size) into arraylist
}
