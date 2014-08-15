package com.vanderbilt.isis.chew.vouchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.stores.Walmart;
import com.vanderbilt.isis.chew.utils.Utils;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public class CashVoucher extends Voucher{

	//static Logger logger = LoggerFactory.getLogger(CashVoucher.class);
	
	private double amountAllowed;
	//private double amountSpent;
	
	public CashVoucher(VoucherCode vCode, Month month, String name, double allowed, VoucherStatus used) {
		super(vCode, month, name, used);
		logger.trace("CashVoucher()");
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
		logger.trace("getAmountAllowed()");
		return amountAllowed;
	}
	
	/** Getter/setter for amount spent **/
	public double getAmountSpent(Context context){
		logger.trace("getAmountSpent()");
		
		String where = ChewContract.ProduceChosen.MONTH + "='"
				+ Utils.getMonth().getMonthNum() + "'" + " AND "
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
			Log.d("CASH VOUCHER", "cursor is null");
			spent = spent
					+ Double.parseDouble(c.getString(0));
		}
		Log.d("CashVoucher", spent+"");
		logger.debug("CashVoucher {}", spent);
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
		logger.trace("getDescription()");
		// TODO Auto-generated method stub
		return null;
	}

}
