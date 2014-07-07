package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class A2 extends RegularVoucher {
	
	public A2(Month month) {
		
		super(month);
		logger.trace("A2()");
		voucherCode = VoucherCode.A2;
		voucherType = "Prenatal";
	}

	@Override
	public ArrayList<String> getDescription() {
		logger.trace("getDescription()");
		
		ArrayList<String> description = new ArrayList<String>();
		
		description.add("Two (2) gallons of milk (reduced fat, low fat, fat free, or sweet acidophilus");
		description.add("One (1) dozen large white grade A eggs");
		description.add("Two (2) 11.5-12 oz containers of frozen juice or two (2) 46-48 oz containers of juice");
		description.add("Sixteen (16) oz dried beans or four (4) cans 15-16oz canned beans");
		description.add("One (1) 16-18 oz jar peanut butter");
		description.add("16oz Whole Wheat Bread/Whole Grain Products");
		
		return description;
	}


}
