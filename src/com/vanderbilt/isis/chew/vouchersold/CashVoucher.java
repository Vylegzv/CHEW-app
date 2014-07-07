package com.vanderbilt.isis.chew.vouchersold;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.vouchers.RegularVoucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;


public abstract class CashVoucher {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected VoucherCode voucherCode;
	protected Month voucherMonth;
	protected double amountAllowed;
	protected double amountSpent;
	
	public CashVoucher(VoucherCode code, Month month){
		logger.trace("CashVoucher()");
		voucherCode = code;
		voucherMonth = month;
		amountSpent = 0.0;
	}
	
	/** Get amount allowed - fixed value **/
	public double getAmountAllowed(){
		logger.trace("getAmountAllowed()");
		
		return amountAllowed;
	}
	
	public Month getMonth(){
		logger.trace("getMonth()");
		return voucherMonth;
	}
	
	/** Getter/setter for amount spent **/
	public double getAmountSpent(){
		logger.trace("getAmountSpent()");
		return amountSpent;
	}
	
	public void setAmountSpent(double s){
		logger.trace("setAmountSpent()");
		this.amountSpent = s;
	}
	
	public void addPrice(double p){
		logger.trace("addPrice()");
		this.amountSpent += p;
	}
	
	public boolean isExpired(){
		logger.trace("isExpired()");
	    int m = Calendar.getInstance().get(Calendar.MONTH);
		
		return !voucherMonth.equals(m);
				
	}
	
	public abstract String getDescription();
}
