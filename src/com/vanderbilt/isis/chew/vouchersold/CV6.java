package com.vanderbilt.isis.chew.vouchersold;

import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class CV6 extends CashVoucher {
	
	private static final double AMT = 6.0;
	
	public CV6(VoucherCode code, Month month) {
		
		super(code, month);
        amountAllowed = AMT;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
