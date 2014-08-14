package com.vanderbilt.isis.chew.vouchers;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.R;

import android.content.Context;
import android.util.SparseArray;

public enum VoucherStatus {

	Unused(0), Inuse(1), Used(2);

	private static final Logger logger = LoggerFactory
			.getLogger(VoucherStatus.class);

	private int value;

	private VoucherStatus(int value) {

		this.value = value;
	}

	public int getValue() {
		logger.trace("getValue()");
		return this.value;
	}

	public boolean equals(VoucherStatus s) {
		logger.trace("equals()");
		return this.value == s.value;
	}

	public String toString(Context c) {
		
		switch (this) {

		case Unused:
			return c.getString(R.string.not_used);
		case Inuse:
			return c.getString(R.string.in_use);
		case Used:
			return c.getString(R.string.used);
		default:
			return "";
		}
	}

	// Lookup table
	private static final SparseArray<VoucherStatus> lookup = new SparseArray<VoucherStatus>();

	// Populate the lookup table on loading time
	static {
		for (VoucherStatus s : EnumSet.allOf(VoucherStatus.class))
			lookup.put(s.getValue(), s);
	}

	// reverse lookup
	public static VoucherStatus getVoucherStatus(int value) {
		return lookup.get(value);
	}
	
	public static VoucherStatus getVoucherStatus(Context c, String status) {

		if (status.equals(c.getString(R.string.not_used))) {
			return Unused;
		} else if (status.equals(c.getString(R.string.in_use))) {
			return Inuse;
		} else if (status.equals(c.getString(R.string.used))) {
			return Used;
		} else {
			return null;
		}
	}

//	public static int getValueFromStatus(Context c, String status) {
//
//		if (status.equals(c.getString(R.string.not_used))) {
//			return Unused.getValue();
//		} else if (status.equals(c.getString(R.string.in_use))) {
//			return Inuse.getValue();
//		} else if (status.equals(c.getString(R.string.used))) {
//			return Used.getValue();
//		} else {
//			return -1;
//		}
//	}
}
