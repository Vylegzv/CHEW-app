package com.vanderbilt.isis.chew.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import android.content.Context;

import com.vanderbilt.isis.chew.dboperations.CustomHandler;
import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;

public abstract class VoucherFactory extends Application {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	abstract Voucher createVoucher(VoucherCode vCode, String month, String name, String used);
	
	private static Context mContext;
	
	@Override
	public void onCreate(){
		super.onCreate();
		logger.trace("onCreate()");
		mContext = this;
	}
	
	public static Context getContext(){
		return mContext;
	}
}
