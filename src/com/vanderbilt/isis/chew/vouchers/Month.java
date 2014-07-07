package com.vanderbilt.isis.chew.vouchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.stores.Walmart;

public enum Month {

	January(0),
	February(1),
	March(2),
	April(3),
	May(4),
	June(5),
	July(6),
	August(7),
	September(8),
	October(9),
	November(10),
	December(11);

	private static final Logger logger = LoggerFactory.getLogger(Month.class);
	//private static final Logger logger = LoggerFactory.getLogger(Month.class.getName());
	
	private int value;
	
	private Month(int value){
		
		this.value = value;
	}
	
	public int getValue(){
		logger.trace("getValue()");
		return this.value;
	}
	
	public boolean equals(Month m){
		logger.trace("equals()");
		return this.value == m.value;
	}
	
	public String toString(){
		logger.trace("toString()");
		switch(value){
		
		case 0:
			return "January";
		case 1:
			return "February";
		case 2:
			return "March";
		case 3:
			return "April";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "October";
		case 10:
			return "November";
		case 11:
			return "December";
		default:
			return "";
		}
	}
}
