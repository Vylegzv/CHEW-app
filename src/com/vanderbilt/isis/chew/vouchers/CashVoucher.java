package com.vanderbilt.isis.chew.vouchers;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class CashVoucher extends Voucher{

	private double amountAllowed;
	//private double amountSpent;
	
	public CashVoucher(VoucherCode vCode, String month, String name, double allowed, String used) {
		super(vCode, month, name, used);
		amountAllowed = allowed;
	}

	/*private double getAllowed(VoucherCode vCode){
		
		if(vCode.equals(VoucherCode.CV6)){
			return 6.0;
		}else if(vCode.equals(VoucherCode.CV10)){
			return 10.0;
		}else{
			return 0.0;
		}
	}*/
	
	public double getAmountAllowed(){		
		return amountAllowed;
	}
	
	/** Getter/setter for amount spent **/
	public double getAmountSpent(Context context){	
		
		String where = ChewContract.ProduceChosen.MONTH + "='"
				+ Utils.getMonth() + "'" + " AND "
				+ ChewContract.ProduceChosen.VOUCHER_CODE + "='"
				+ voucherCode + "'" + " AND "
				+ ChewContract.ProduceChosen.MEMBER_NAME + "='"
				+ memberName + "'";

		Cursor c = context.getContentResolver().query(
				ChewContract.ProduceChosen.CONTENT_URI,
				new String[] { ChewContract.ProduceChosen.COST },
				where, null, null);

		double spent = 0.0;
		while (c.moveToNext()) {
			spent = spent
					+ Double.parseDouble(c.getString(0));
		}
		Log.d("CashVoucher", spent+"");
		return spent;
	}
	
	/*public void setAmountSpent(double s){	
		this.amountSpent = s;
	}
	
	public void addPrice(double p){	
		this.amountSpent += p;
	}*/
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
