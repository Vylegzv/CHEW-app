package com.vanderbilt.isis.chew.factories;

import com.vanderbilt.isis.chew.vouchers.CashVoucher;
import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class CashVoucherFactory extends VoucherFactory{

	@Override
	public Voucher createVoucher(VoucherCode vCode, String month, String name, String used) {
		
		if(vCode.equals(VoucherCode.CV6)){
			return new CashVoucher(vCode, month, name, 6.0, used);
		}else if(vCode.equals(VoucherCode.CV10)){
			return new CashVoucher(vCode, month, name, 10.0, used);
		}else{
			return null;
		}
	}

}
