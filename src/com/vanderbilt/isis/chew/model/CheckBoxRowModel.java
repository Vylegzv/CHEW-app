package com.vanderbilt.isis.chew.model;

public class CheckBoxRowModel {

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
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getQuantityNumber() {
		return quantityNumber;
	}
	
	public String getOtherOptionType() {
		return otherOptionType;
	}

	public void setOtherOptionType(String otherOptionType) {
		this.otherOptionType = otherOptionType;
	}

	public void setQuantityNumber(String quantityNumber) {
		this.quantityNumber = quantityNumber;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isSpecialCase() {
		return specialCase;
	}

	public void setSpecialCase(boolean specialCase) {
		this.specialCase = specialCase;
	}
	
	public boolean isCombinationItem() {
		return combinationItem;
	}

	public void setCombinationItem(boolean combinationItem) {
		this.combinationItem = combinationItem ;
	}
	
	public boolean isDeleteOld() {
		return deleteOld;
	}

	public void setDeleteOld(boolean deleteOld) {
		this.deleteOld = deleteOld ;
	}
	
	public boolean isCombinationItemsBoughtBefore() {
		return combinationItemsBoughtBefore;
	}

	public void setCombinationItemsBoughtBefore(boolean combinationItemsBoughtBefore) {
		this.combinationItemsBoughtBefore = combinationItemsBoughtBefore;
	}

}
