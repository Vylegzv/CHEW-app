package com.vanderbilt.isis.chew.vouchers;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import com.vanderbilt.isis.chew.R;
import com.vanderbilt.isis.chew.stores.Walmart;
import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.Month;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public abstract class Voucher {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected VoucherCode voucherCode;
	protected Month voucherMonth;
	protected String memberName;
	protected VoucherStatus status;
	
	// TODO: use factory to get the right voucher from String like A2
	public Voucher(VoucherCode vCode, Month month, String name, VoucherStatus s){
		logger.trace("Voucher()");
		voucherCode = vCode;
		voucherMonth = month;
		memberName = name;
		status = s;
	}
	
	public VoucherCode getCode(){
		logger.trace("getCode()");
		return voucherCode;
	}
	
	public Month getMonth(){
		logger.trace("getMonth()");
		return voucherMonth;
	}
	
	public String getName(){
		logger.trace("getName()");
		return memberName;
	}
	
	public boolean isUsed(Context c){
		logger.trace("isUsed()");
		return status.equals(VoucherStatus.Used);
	}
	
	public boolean isExpired(){
		logger.trace("isExpired()");
	    Month curMonth = Utils.getMonth();
		return !voucherMonth.equals(curMonth);
				
	}
	
	public abstract String getDescription();
}
