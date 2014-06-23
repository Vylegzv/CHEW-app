package com.vanderbilt.isis.chew.vouchers;

import java.util.ArrayList;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class RegularVoucher extends Voucher {
	
	private ArrayList<String> descriptions;

	public RegularVoucher(VoucherCode vCode, String month, String name, String used, ArrayList<String> ds) {
		super(vCode, month, name, used);
		descriptions = ds;
	}

	public ArrayList<String> getDescriptions(){
		return descriptions;
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
