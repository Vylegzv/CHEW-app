package com.vanderbilt.isis.chew.stores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.recipes.Step;

public class Walmart extends Store {
	
	private static final Logger loggerWalmart = LoggerFactory.getLogger(Walmart.class);
	
	private Walmart(){
		super();
		logger.trace("Walmart()");
		storeName = "Walmart";
	}
	
	public static Store instance(){
		loggerWalmart.trace("instance()");
		if(uniqueInstance == null)
			uniqueInstance = new Walmart();
		
		return uniqueInstance;
	}
	
	public String getStoreName(){
		logger.trace("getStoreName()");
		return storeName;
	}
}
