package com.vanderbilt.isis.chew.factories;

import com.vanderbilt.isis.chew.vouchers.Month;
import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.CashVoucher;
import com.vanderbilt.isis.chew.vouchers.RegularVoucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public abstract class VoucherFactory {
	
	abstract Voucher createVoucher(VoucherCode vCode, String month, String name);
}
