package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class B2 extends RegularVoucher {

	public B2(Month month) {
		
		super(month);
		voucherCode = VoucherCode.B2;
		voucherType = "PostPartum";
	}

	@Override
	public ArrayList<String> getDescription() {
		
		ArrayList<String> description = new ArrayList<String>();
		
		description.add("Two (2) gallons of milk (reduced fat, " +
				"low fat, fat free, or sweet acidophilus)");
		description.add("Sixteen (16) oz store brand cheese (8 or 16 oz pkg)");
		description.add("One (1) dozen large white grade A eggs");
		description.add("One (1) 11.5-12 oz container of frozen juice or 46-48 oz container of juice");
		description.add("Sixteen (16) oz dried beans/peas or Four (4) cans 15-16 oz canned beans " +
				"or One (1) 16-18 oz jar peanut butter");
		
		return description;
	}	
}













