package com.vanderbilt.isis.chew.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.dboperations.CustomHandler;
import com.vanderbilt.isis.chew.vouchers.CashVoucher;
import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class CashVoucherFactory extends VoucherFactory{

	//static Logger logger = LoggerFactory.getLogger(CashVoucherFactory.class);
	
	@Override
	public Voucher createVoucher(VoucherCode vCode, String month, String name, String used) {
		logger.trace("createVoucher()");
		if(vCode.equals(VoucherCode.CV8)){
			return new CashVoucher(vCode, month, name, 8.0, used);
		}else if(vCode.equals(VoucherCode.CV10)){
			return new CashVoucher(vCode, month, name, 10.0, used);
		}else{
			return null;
		}
	}

}
