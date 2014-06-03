package com.vanderbilt.isis.chew.factories;

import com.vanderbilt.isis.chew.vouchers.Month;
import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.CashVoucher;
import com.vanderbilt.isis.chew.vouchers.RegularVoucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class RegularVoucherFactory extends VoucherFactory{

	@Override
	Voucher createVoucher(VoucherCode vCode, String month, String name) {
		
		if(vCode.equals(VoucherCode.CV10) || vCode.equals(VoucherCode.CV6)){
			//return new CashVoucher(vCode, month, name);
		}else{
			//return new RegularVoucher(vCode, month, name);
		}
		return null;
	}
	
}
