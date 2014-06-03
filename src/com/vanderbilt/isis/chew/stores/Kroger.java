package com.vanderbilt.isis.chew.stores;

public class Kroger extends Store {

	private Kroger(){
		super();
		storeName = "Kroger";
	}
	
	public static Store instance(){
		
		if(uniqueInstance == null)
			uniqueInstance = new Kroger();
		
		return uniqueInstance;
	}
	
	public String getStoreName(){
		return storeName;
	}
}
