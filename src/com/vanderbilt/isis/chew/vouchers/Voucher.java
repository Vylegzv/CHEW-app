package com.vanderbilt.isis.chew.vouchers;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.stores.Walmart;
import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.Month;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public abstract class Voucher {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected VoucherCode voucherCode;
	protected String voucherMonth;
	protected String memberName;
	protected String used;
	
	// TODO: use factory to get the right voucher from String like A2
	public Voucher(VoucherCode vCode, String month, String name, String u){
		logger.trace("Voucher()");
		voucherCode = vCode;
		voucherMonth = month;
		memberName = name;
		used = u;
	}
	
	public VoucherCode getCode(){
		logger.trace("getCode()");
		return voucherCode;
	}
	
	public String getMonth(){
		logger.trace("getMonth()");
		return voucherMonth;
	}
	
	public String getName(){
		logger.trace("getName()");
		return memberName;
	}
	
	public boolean isUsed(){
		logger.trace("isUsed()");
		return used.equals(Utils.USED) ? true : false;
	}
	
	public boolean isExpired(){
		logger.trace("isExpired()");
	    int m = Calendar.getInstance().get(Calendar.MONTH);
		return !voucherMonth.equals(m);
				
	}
	
	public abstract String getDescription();
}
