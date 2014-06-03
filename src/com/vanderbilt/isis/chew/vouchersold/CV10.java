package com.vanderbilt.isis.chew.vouchersold;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class CV10 extends CashVoucher {

	private static final double AMT = 10.0;
	
	public CV10(VoucherCode code, Month month) {
		
		super(code, month);
		amountAllowed = AMT;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
