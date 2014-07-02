package com.vanderbilt.isis.chew.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.factories.CashVoucherFactory;

public class MainListRowItem {

	private static final Logger logger = LoggerFactory.getLogger(MainListRowItem.class);
	
	private int imageId;
	private String title;
	private String subtitle;

	public MainListRowItem(int imageId, String title, String subtitle) {
		logger.trace("MainListRowItem()");
		this.imageId = imageId;
		this.title = title;
		this.subtitle = subtitle;
	}

	public int getImageId() {
		logger.trace("getImageId()");
		return imageId;
	}

	public void setImageId(int imageId) {
		logger.trace("setImageId()");
		this.imageId = imageId;
	}

	public String getDesc() {
		logger.trace("getDesc()");
		return subtitle;
	}

	public void setDesc(String desc) {
		logger.trace("setDesc()");
		this.subtitle = desc;
	}

	public String getTitle() {
		logger.trace("getTitle()");
		return title;
	}

	public void setTitle(String title) {
		logger.trace("setTitle()");
		this.title = title;
	}

	@Override
	public String toString() {
		logger.trace("toString()");
		return title + "\n" + subtitle;
	}
}
