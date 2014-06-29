package com.vanderbilt.isis.chew.factories;

import android.app.Application;
import android.content.Context;

import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public abstract class VoucherFactory extends Application {
	
	abstract Voucher createVoucher(VoucherCode vCode, String month, String name, String used);
	
	private static Context mContext;
	
	@Override
	public void onCreate(){
		super.onCreate();
		mContext = this;
	}
	
	public static Context getContext(){
		return mContext;
	}
	
	public static void nullContext(){
		mContext = null;
	}
}
