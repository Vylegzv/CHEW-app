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
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.A)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.A2)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.A2)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.B)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.B)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.B2)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.B2)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.E)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.E)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.E2)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.E2)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.G)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.G)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.G2)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.G2)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.K)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.K)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.K2)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.K2)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.L)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.L)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.L2)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.L2)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.P)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.P)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.P2)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.P2)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.T)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.T)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.T2)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.T2)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.PC)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.PC)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.PC1)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.PC1)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else if (vCode.equals(VoucherCode.PPW)) {
			ArrayList<String> descr = new ArrayList<String>(Arrays.asList(getContext()
					.getResources().getStringArray(R.array.PPW)));
			nullContext(); // to avoid memory leaks
			return new RegularVoucher(vCode, month, name, used, descr);
		} else {
			return null;
		}
	}

}
