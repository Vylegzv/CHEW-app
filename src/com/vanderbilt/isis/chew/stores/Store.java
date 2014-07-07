package com.vanderbilt.isis.chew.stores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.recipes.Step;

public abstract class Store {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected static Store uniqueInstance;

	private boolean atStore;
	protected String storeName;
	private int storeId;

	protected Store() {
		logger.trace("Store()");
	}

	public static Store getInstance() {
		return uniqueInstance;
	}
	
	public void setStoreId(int id){
		logger.trace("setStoreId()");
		storeId = id;
	}
	
	public int getStoreId(){
		logger.trace("getStoreId()");
		return storeId;
	}

	public void setAtStore(boolean b){
		logger.trace("setAtStore()");
		atStore = b;
	}
	
	public boolean isAtStore(){
		logger.trace("isAtStore()");
		return atStore;
	}
	
	public String getStoreName(){
		logger.trace("getStoreName()");
		return storeName;
	}
}
