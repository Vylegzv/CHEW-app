package com.vanderbilt.isis.chew.stores;

public class Walmart extends Store {
	
	private Walmart(){
		super();
		storeName = "Walmart";
	}
	
	public static Store instance(){
		
		if(uniqueInstance == null)
			uniqueInstance = new Walmart();
		
		return uniqueInstance;
	}
	
	public String getStoreName(){
		return storeName;
	}
}
