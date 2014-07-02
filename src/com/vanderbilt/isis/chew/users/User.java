package com.vanderbilt.isis.chew.users;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.stores.Walmart;
import com.vanderbilt.isis.chew.vouchersold.CashVoucher;
import com.vanderbilt.isis.chew.vouchersold.RegularVoucher;

public class User {

	private static final Logger logger = LoggerFactory.getLogger(User.class);
	
	private String userName;
	private String userEthnicity;

	/** Composition over Inheritance */
	ArrayList<RegularVoucher> regularVouchers;
	CashVoucher cashVoucher;

	/** Constructors **/
	public User() {
		logger.trace("User()");
	};

	public User(String name, String ethnicity) {
		logger.trace("User(String name, String ethnicity)");
		this.userName = name;
		this.userEthnicity = ethnicity;
		
	}
	
	/** Setter and getter methods **/
	public void setName(String name){
		logger.trace("setName()");
		this.userName = name;
	}
	
	public void setEthnicity(String ethnicity){
		logger.trace("setEthnicity()");
		this.userEthnicity = ethnicity;
	}
	
	public String getName(){
		logger.trace("getName()");
		return this.userName;
	}
	
	public String getEthnicity(){
		logger.trace("getEthnicity()");
		return this.userEthnicity;
	}

	/** Get Vouchers **/
	public void setRegularVouchers(ArrayList<RegularVoucher> regularVouchers) {
		logger.trace("setRegularVouchers()");
		this.regularVouchers = regularVouchers;
	}

	public void setCashVoucher(CashVoucher cashVoucher) {
		logger.trace("setCashVoucher()");
		this.cashVoucher = cashVoucher;
	}
	
	/** Cash Voucher methods **/
	public double getCashValue(){
		logger.trace("getCashValue()");
		return cashVoucher.getAmountAllowed();
	}
	
	public String getCashVoucherDescr(){
		logger.trace("getCashVoucherDescr()");
		return cashVoucher.getDescription();
	}
	
	/** Regular Voucher methods **/
	// to display in a listview
	public ArrayList<String> getRegularVoucherDescr(){
		logger.trace("getRegularVoucherDescr()");
		return null;
	}

}
