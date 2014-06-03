package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class L2 extends RegularVoucher {

	public L2(Month month) {
		
		super(month);
		voucherCode = VoucherCode.L2;
		voucherType = "Partially Breastfeeding Woman";
	}

	@Override
	public ArrayList<String> getDescription() {
		
		ArrayList<String> description = new ArrayList<String>();

		description.add("Two (2) gallons of milk (reduced fat, low fat, " +
				"fat free, or sweet acidophilus)");
		description.add("One (1) dozen large white grade A eggs ");
		description.add("Two (2) 11.5-12 oz containers of frozen juice " +
				"or Two (2) 46-48 oz containers of juice");
		description.add("Sixteen (16) oz dried beans/peas or Four (4) " +
				"cans 15-16oz canned beans");
		description.add("One (1) 16-18 oz jar peanut butter");
		description.add("16 oz Whole Wheat Bread/Whole Grain Product");
		
		return description;
	}	
}














