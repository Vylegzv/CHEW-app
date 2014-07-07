package com.vanderbilt.isis.chew.recipes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.db.ChewContract;
import com.vanderbilt.isis.chew.model.MainListRowItem;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
	
	private static final Logger logger = LoggerFactory.getLogger(Ingredient.class);

	private String ingredient;
	private String longerDescription;
	
	public Ingredient(String i, String l){
		super();
		logger.trace("Ingredient(String i, String l)");
		ingredient = i;
		longerDescription = l;
	}
	
	public String getIngredient(){
		logger.trace("getIngredient()");
		return ingredient;
	}
	
	public void setIngredient(String i){
		logger.trace("setIngredient()");
		ingredient = i;
	}
	
	public String getLongerDescription(){
		logger.trace("getLongerDescription()");
		return longerDescription;
	}
	
	public void setLongerDescription(String l){
		logger.trace("setLongerDescription()");
		longerDescription = l;
	}
	
    public static Ingredient fromCursor(Cursor curIngs) {
    	logger.trace("fromCursor()");
        String i = curIngs.getString(curIngs.getColumnIndex(ChewContract.Ingredients.INGREDIENT));
        String d = curIngs.getString(curIngs.getColumnIndex(ChewContract.Ingredients.LONG_DESCRIPTION));

        return new Ingredient(i, d);
    }

	/** Parcelable stuff **/
	public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>(){
		
		@Override
		public Ingredient createFromParcel(Parcel source) {
			logger.trace("createFromParcel()");
			return new Ingredient(source);
		}

		@Override
		public Ingredient[] newArray(int size) {
			logger.trace("newArray()");
			return new Ingredient[size];
		}	
	};
	
	public Ingredient(Parcel source){
		logger.trace("Ingredient(Parcel source)");
		readFromParcel(source);
	}
	
	public void readFromParcel(Parcel source){
		logger.trace("readFromParcel()");
		
		ingredient = source.readString();
		longerDescription = source.readString();
	}
	
	@Override
	public int describeContents() {
		logger.trace("describeContents()");
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		logger.trace("writeToParcel()");
		dest.writeString(ingredient);
		dest.writeString(longerDescription);
	}
}
