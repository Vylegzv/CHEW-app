package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class G2 extends RegularVoucher{

	public G2(Month month) {
		
		super(month);
		logger.trace("G2()");
		voucherCode = VoucherCode.G2;
		voucherType = "Fully Breastfeeding Woman";	
	}

	@Override
	public ArrayList<String> getDescription() {
		logger.trace("getDescription()");
		ArrayList<String> description = new ArrayList<String>();
		
		description.add("Two (2) gallons of milk (no whole milk)");
		description.add("Choose one of these: One (1) quart lowfat buttermilk, One (1) 12 oz can evaporated milk or 14-16 oz tofu");
		description.add("Sixteen (16) oz store brand cheese (8 or 16 oz pkg)");
		description.add("One (1) dozen large white grade A eggs");
		description.add("Two (2) 11.5-12 oz containers of frozen juice or Two (2) 46-48 oz containers of juice");
		description.add("Thirty (30) oz of fish (6-5oz or 5-6 oz tuna or 2-14.75 oz Salmon or 8-3.75 oz Sardines)");
		
		return description;
	}	
}


















