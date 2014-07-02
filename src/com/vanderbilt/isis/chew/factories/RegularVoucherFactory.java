package com.vanderbilt.isis.chew.factories;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.R;
import com.vanderbilt.isis.chew.dboperations.CustomHandler;
import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.RegularVoucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class RegularVoucherFactory extends VoucherFactory {

	//static Logger logger = LoggerFactory.getLogger(RegularVoucherFactory.class);
	
	@Override
	public Voucher createVoucher(VoucherCode vCode, String month, String name, String used) {
		logger.trace("createVoucher()");

		if (vCode.equals(VoucherCode.A)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.A))));
		} else if (vCode.equals(VoucherCode.A2)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.A2))));
		} else if (vCode.equals(VoucherCode.B)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.B))));
		} else if (vCode.equals(VoucherCode.B2)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.B2))));
		} else if (vCode.equals(VoucherCode.E)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.E))));
		} else if (vCode.equals(VoucherCode.E2)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.E2))));
		} else if (vCode.equals(VoucherCode.G)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.G))));
		} else if (vCode.equals(VoucherCode.G2)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.G2))));
		} else if (vCode.equals(VoucherCode.K)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.K))));
		} else if (vCode.equals(VoucherCode.K2)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.K2))));
		} else if (vCode.equals(VoucherCode.L)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.L))));
		} else if (vCode.equals(VoucherCode.L2)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.L2))));
		} else if (vCode.equals(VoucherCode.P)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.P))));
		} else if (vCode.equals(VoucherCode.P2)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.P2))));
		} else if (vCode.equals(VoucherCode.T)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.T))));
		} else if (vCode.equals(VoucherCode.T2)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.T2))));
		} else if (vCode.equals(VoucherCode.PC)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.PC))));
		} else if (vCode.equals(VoucherCode.PC1)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.PC1))));
		} else if (vCode.equals(VoucherCode.PPW)) {
			return new RegularVoucher(vCode, month, name, used,
					new ArrayList<String>(Arrays.asList(getContext()
							.getResources().getStringArray(R.array.PPW))));
		} else {
			return null;
		}
	}

}
