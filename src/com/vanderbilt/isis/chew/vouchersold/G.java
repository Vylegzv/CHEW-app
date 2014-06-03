package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class G extends RegularVoucher {

	public G(Month month) {
		
		super(month);
		voucherCode = VoucherCode.G;
		voucherType = "Fully Breastfeeding Woman";
	}

	@Override
	public ArrayList<String> getDescription() {
		
		ArrayList<String> description = new ArrayList<String>();
		
		description.add("Three (3) gallons of milk (no whole milk)");
		description.add("Sixteen (16) oz store brand cheese (8 or 16 oz pkg)");
		description.add("One (1) dozen large white grade A eggs");
		description.add("One (1) 11.5-12 oz container of frozen juice or 46-48 oz container of juice");
		description.add("16 oz dried beans/peas or four 15-16 oz cans beans");
		description.add("Thirty-six (36) oz of cereal (buy 11 oz or larger)");
		description.add("One (1) 16-18 oz jar peanut butter ");
		description.add("16 oz Whole Wheat Bread/Whole Grain Product");
		
		return description;
	}
	
	

}


















