package com.vanderbilt.isis.chew.stores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.recipes.Step;

public class Kroger extends Store {

	private static final Logger loggerKroger = LoggerFactory.getLogger(Kroger.class);
	
	private Kroger(){
		super();
		logger.trace("Kroger()");
		storeName = "Kroger";
	}
	
	public static Store instance(){
		loggerKroger.trace("instance()");
		if(uniqueInstance == null)
			uniqueInstance = new Kroger();
		
		return uniqueInstance;
	}
	
	public String getStoreName(){
		logger.trace("getStoreName()");
		return storeName;
	}
}
