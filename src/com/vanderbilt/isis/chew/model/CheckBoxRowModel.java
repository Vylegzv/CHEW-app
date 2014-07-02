package com.vanderbilt.isis.chew.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.factories.CashVoucherFactory;

public class CheckBoxRowModel {

	private static final Logger logger = LoggerFactory.getLogger(CheckBoxRowModel.class);
	
	private String personName;
	private String voucherCode;
	private String quantityNumber;
	private String otherOptionType;
	private boolean specialCase;
	private boolean combinationItem;
	private boolean deleteOld;
	private boolean combinationItemsBoughtBefore;

	private boolean selected;

	public CheckBoxRowModel(String personName, String voucherCode,
			String quantityNumber) {
		logger.trace("CheckBoxRowModel()");
		this.personName = personName;
		this.voucherCode = voucherCode;
		this.quantityNumber = quantityNumber;
		selected = false;
		specialCase = false;
		combinationItem = false;
		deleteOld = false;
		combinationItemsBoughtBefore = false;
		otherOptionType = "";
	}

	public String getPersonName() {
		logger.trace("getPersonName()");
		return personName;
	}

	public void setPersonName(String personName) {
		logger.trace("setPersonName()");
		this.personName = personName;
	}

	public String getVoucherCode() {
		logger.trace("getVoucherCode()");
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		logger.trace("setVoucherCode()");
		this.voucherCode = voucherCode;
	}

	public String getQuantityNumber() {
		logger.trace("getQuantityNumber()");
		return quantityNumber;
	}
	
	public String getOtherOptionType() {
		logger.trace("getOtherOptionType()");
		return otherOptionType;
	}

	public void setOtherOptionType(String otherOptionType) {
		logger.trace("setOtherOptionType()");
		this.otherOptionType = otherOptionType;
	}

	public void setQuantityNumber(String quantityNumber) {
		logger.trace("setQuantityNumber()");
		this.quantityNumber = quantityNumber;
	}

	public boolean isSelected() {
		logger.trace("isSelected()");
		return selected;
	}

	public void setSelected(boolean selected) {
		logger.trace("setSelected()");
		this.selected = selected;
	}
	
	public boolean isSpecialCase() {
		logger.trace("isSpecialCase()");
		return specialCase;
	}

	public void setSpecialCase(boolean specialCase) {
		logger.trace("setSpecialCase()");
		this.specialCase = specialCase;
	}
	
	public boolean isCombinationItem() {
		logger.trace("isCombinationItem()");
		return combinationItem;
	}

	public void setCombinationItem(boolean combinationItem) {
		logger.trace("setCombinationItem()");
		this.combinationItem = combinationItem ;
	}
	
	public boolean isDeleteOld() {
		logger.trace("isDeleteOld()");
		return deleteOld;
	}

	public void setDeleteOld(boolean deleteOld) {
		logger.trace("setDeleteOld()");
		this.deleteOld = deleteOld ;
	}
	
	public boolean isCombinationItemsBoughtBefore() {
		logger.trace("isCombinationItemsBoughtBefore()");
		return combinationItemsBoughtBefore;
	}

	public void setCombinationItemsBoughtBefore(boolean combinationItemsBoughtBefore) {
		logger.trace("setCombinationItemsBoughtBefore()");
		this.combinationItemsBoughtBefore = combinationItemsBoughtBefore;
	}

}
