package com.vanderbilt.isis.chew.vouchers;

import java.util.ArrayList;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class RegularVoucher extends Voucher {
	
	private ArrayList<Entry> entries;

	public RegularVoucher(VoucherCode vCode, String month, String name) {
		super(vCode, month, name);
	}

	public ArrayList<Entry> getEntries(){
		return entries;
	}
	
	public void setEntries(ArrayList<Entry> entries){
		this.entries = entries;
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
