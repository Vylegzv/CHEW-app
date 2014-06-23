package com.vanderbilt.isis.chew.vouchers;

import java.util.Calendar;

import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.Month;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public abstract class Voucher {

	protected VoucherCode voucherCode;
	protected String voucherMonth;
	protected String memberName;
	protected String used;
	
	// TODO: use factory to get the right voucher from String like A2
	public Voucher(VoucherCode vCode, String month, String name, String u){
		
		voucherCode = vCode;
		voucherMonth = month;
		memberName = name;
		used = u;
	}
	
	public VoucherCode getCode(){
		return voucherCode;
	}
	
	public String getMonth(){	
		return voucherMonth;
	}
	
	public String getName(){
		return memberName;
	}
	
	public boolean isUsed(){
		
		return used.equals(Utils.USED) ? true : false;
	}
	
	public boolean isExpired(){
		
	    int m = Calendar.getInstance().get(Calendar.MONTH);
		return !voucherMonth.equals(m);
				
	}
	
	public abstract String getDescription();
}
