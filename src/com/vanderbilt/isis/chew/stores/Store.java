package com.vanderbilt.isis.chew.stores;

public abstract class Store {

	protected static Store uniqueInstance;

	private boolean atStore;
	protected String storeName;
	private int storeId;

	protected Store() {
	}

	public static Store getInstance() {
		return uniqueInstance;
	}
	
	public void setStoreId(int id){
		storeId = id;
	}
	
	public int getStoreId(){
		return storeId;
	}

	public void setAtStore(boolean b){
		atStore = b;
	}
	
	public boolean isAtStore(){
		return atStore;
	}
	
	public String getStoreName(){
		return storeName;
	}
}
