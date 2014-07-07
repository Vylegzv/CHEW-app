package com.vanderbilt.isis.chew.vouchers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.stores.Walmart;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class RegularVoucher extends Voucher {
	
	//static Logger logger = LoggerFactory.getLogger(RegularVoucher.class);
	
	private ArrayList<String> descriptions;

	public RegularVoucher(VoucherCode vCode, String month, String name, String used, ArrayList<String> ds) {
		super(vCode, month, name, used);
		logger.trace("RegularVoucher()");
		descriptions = ds;
	}

	public ArrayList<String> getDescriptions(){
		logger.trace("getDescriptions()");
		return descriptions;
	}
	
	@Override
	public String getDescription() {
		logger.trace("getDescription()");
		// TODO Auto-generated method stub
		return null;
	}

	
}
