package com.vanderbilt.isis.chew.vouchers;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.util.SparseArray;

import com.vanderbilt.isis.chew.R;

public enum Month {

	January(0), February(1), March(2), April(3), May(4), June(5), July(6), August(
			7), September(8), October(9), November(10), December(11);

	private static final Logger logger = LoggerFactory.getLogger(Month.class);
	// private static final Logger logger =
	// LoggerFactory.getLogger(Month.class.getName());

	private int monthNum;

	private Month(int monthNum) {

		this.monthNum = monthNum;
	}

	public int getMonthNum() {
		logger.trace("getMonthNum()");
		return this.monthNum;
	}

	public boolean equals(Month m) {
		logger.trace("equals()");
		return this.monthNum == m.monthNum;
	}

	public String toString(Context c) {

		switch (this) {

		case January:
			return c.getString(R.string.january);
		case February:
			return c.getString(R.string.february);
		case March:
			return c.getString(R.string.march);		
		case April:
			return c.getString(R.string.april);
		case May:
			return c.getString(R.string.may);
		case June:
			return c.getString(R.string.june);
		case July:
			return c.getString(R.string.july);
		case August:
			return c.getString(R.string.august);
		case September:
			return c.getString(R.string.september);
		case October:
			return c.getString(R.string.october);
		case November:
			return c.getString(R.string.november);
		case December:
			return c.getString(R.string.december);
		default:
			return "";
		}
	}

	// Lookup table
	private static final SparseArray<Month> lookup = new SparseArray<Month>();

	// Populate the lookup table on loading time
	static {
		for (Month m : EnumSet.allOf(Month.class))
			lookup.put(m.getMonthNum(), m);
	}

	// reverse lookup
	public static Month getMonth(int monthNum) {
		return lookup.get(monthNum);
	}
	
	public static Month getMonth(Context c, String month) {

		if (month.equalsIgnoreCase(c.getString(R.string.january))) {
			return January;
		} else if (month.equalsIgnoreCase(c.getString(R.string.february))) {
			return February;
		} else if (month.equalsIgnoreCase(c.getString(R.string.march))) {
			return March;
		} else if (month.equalsIgnoreCase(c.getString(R.string.april))) {
			return April;
		} else if (month.equalsIgnoreCase(c.getString(R.string.may))) {
			return May;
		} else if (month.equalsIgnoreCase(c.getString(R.string.june))) {
			return June;
		} else if (month.equalsIgnoreCase(c.getString(R.string.july))) {
			return July;
		} else if (month.equalsIgnoreCase(c.getString(R.string.august))) {
			return August;
		} else if (month.equalsIgnoreCase(c.getString(R.string.september))) {
			return September;
		} else if (month.equalsIgnoreCase(c.getString(R.string.october))) {
			return October;
		} else if (month.equalsIgnoreCase(c.getString(R.string.november))) {
			return November;
		} else if (month.equalsIgnoreCase(c.getString(R.string.december))) {
			return December;
		} else {
			return null;
		}
	}

//	public static int getMonthNum(Context c, String month) {
//
//		if (month.equalsIgnoreCase(c.getString(R.string.january))) {
//			return January.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.february))) {
//			return February.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.march))) {
//			return March.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.april))) {
//			return April.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.may))) {
//			return May.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.june))) {
//			return June.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.july))) {
//			return July.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.august))) {
//			return August.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.september))) {
//			return September.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.october))) {
//			return October.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.november))) {
//			return November.getMonthNum();
//		} else if (month.equalsIgnoreCase(c.getString(R.string.december))) {
//			return December.getMonthNum();
//		} else {
//			return -1;
//		}
//	}
}
