package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class L extends RegularVoucher{

	public L(Month month) {
		
		super(month);
		voucherCode = VoucherCode.L;
		voucherType = "Partially Breastfeeding Woman";
	}

	@Override
	public ArrayList<String> getDescription() {
		
		ArrayList<String> description = new ArrayList<String>();
		
		description.add("Two (2) gallons of milk (reduced fat, low fat," +
				" fat free, or sweet acidophilus)");
		description.add("Choose 3 (any combination) of: 14-16 oz tofu, " +
				"quart buttermilk, 12 oz can evaporated milk or choose " +
				"One (1) 9.6 oz container nonfat dry milk");
		description.add("Sixteen (16) oz store brand cheese (8 or 16 oz pkg)");
		description.add("One (1) 11.5-12 oz container of frozen juice or 46-48 oz container of juice");
		description.add("Thirty-six (36) oz of cereal (buy 11 oz or larger)");
		
		return description;
	}
}





















