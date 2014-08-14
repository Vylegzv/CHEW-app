package com.vanderbilt.isis.chew.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import android.content.Context;

import com.vanderbilt.isis.chew.dboperations.CustomHandler;
import com.vanderbilt.isis.chew.vouchers.Month;
import com.vanderbilt.isis.chew.vouchers.Voucher;
import com.vanderbilt.isis.chew.vouchers.VoucherCode;
import com.vanderbilt.isis.chew.vouchers.VoucherStatus;

public abstract class VoucherFactory extends Application {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	abstract Voucher createVoucher(VoucherCode vCode, Month month, String name, VoucherStatus used);
	
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
	
	public static void nullContext(){
		mContext = null;
	}
}
