package com.vanderbilt.isis.chew.vouchersold;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;


public abstract class CashVoucher {

	protected VoucherCode voucherCode;
	protected Month voucherMonth;
	protected double amountAllowed;
	protected double amountSpent;
	
	public CashVoucher(VoucherCode code, Month month){
		
		voucherCode = code;
		voucherMonth = month;
		amountSpent = 0.0;
	}
	
	/** Get amount allowed - fixed value **/
	public double getAmountAllowed(){
		
		return amountAllowed;
	}
	
	public Month getMonth(){
		
		return voucherMonth;
	}
	
	/** Getter/setter for amount spent **/
	public double getAmountSpent(){
		
		return amountSpent;
	}
	
	public void setAmountSpent(double s){
		
		this.amountSpent = s;
	}
	
	public void addPrice(double p){
		
		this.amountSpent += p;
	}
	
	public boolean isExpired(){
		
	    int m = Calendar.getInstance().get(Calendar.MONTH);
		
		return !voucherMonth.equals(m);
				
	}
	
	public abstract String getDescription();
}
