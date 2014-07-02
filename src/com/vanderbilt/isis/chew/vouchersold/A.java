package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class A extends RegularVoucher {

	public A(Month month) {

		super(month);
		logger.trace("A()");
		voucherCode = VoucherCode.A;
		voucherType = "Prenatal";
	}

	@Override
	public ArrayList<String> getDescription() {
		logger.trace("getDescription()");
		ArrayList<String> description = new ArrayList<String>();

		description
				.add("Two (2) gallons of milk (reduced fat, low fat, fat free, or sweet acidophilus");
		description
				.add("Choose 3 (any combination) of: 14-16 oz tofu, quart buttermilk, 12 oz can " +
						"evaporated milk or choose one (1) 9.6 container nonfat dry milk");
		description.add("Sixteen (16) oz store brand cheese (8 or 16 oz pkg)");
		description.add("One (1) 11.5-12 oz container of frozen juice or 46-48 oz container of juice");
		description.add("Thirty-six (36) oz of cereal (buy 11 oz or larger)");

		return description;
	}

}























