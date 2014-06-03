package com.vanderbilt.isis.chew.vouchersold;

import java.util.ArrayList;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class B extends RegularVoucher {

	public B(Month month) {

		super(month);
		voucherCode = VoucherCode.B;
		voucherType = "Postpartum";
	}

	@Override
	public ArrayList<String> getDescription() {

		ArrayList<String> description = new ArrayList<String>();

		description.add("One (1) gallon of milk (reduced fat, low fat, fat free, or sweet acidophilus)");
		description.add("Choose one of these: One (1) quart lowfat buttermilk, or One (1) 12 oz can evaporated milk or 14-16 oz tofu");
		description.add("One (1) dozen large white grade A eggs");
		description.add("One (1) 11.5-12 oz container of frozen juice or 46-48 oz container of juice");
		description.add("Thirty-six (36) oz of cereal (buy 11 oz or larger)");

		return description;
	}
}









