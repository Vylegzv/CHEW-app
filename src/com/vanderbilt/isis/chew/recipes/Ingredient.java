package com.vanderbilt.isis.chew.recipes;

import com.vanderbilt.isis.chew.db.ChewContract;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

	private String ingredient;
	private String longerDescription;
	
	public Ingredient(String i, String l){
		super();
		ingredient = i;
		longerDescription = l;
	}
	
	public String getIngredient(){
		
		return ingredient;
	}
	
	public void setIngredient(String i){
		
		ingredient = i;
	}
	
	public String getLongerDescription(){
		
		return longerDescription;
	}
	
	public void setLongerDescription(String l){
		
		longerDescription = l;
	}
	
    public static Ingredient fromCursor(Cursor curIngs) {
    	
        String i = curIngs.getString(curIngs.getColumnIndex(ChewContract.Ingredients.INGREDIENT));
        String d = curIngs.getString(curIngs.getColumnIndex(ChewContract.Ingredients.LONG_DESCRIPTION));

        return new Ingredient(i, d);
    }

	/** Parcelable stuff **/
	public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>(){

		@Override
		public Ingredient createFromParcel(Parcel source) {
			return new Ingredient(source);
		}

		@Override
		public Ingredient[] newArray(int size) {
			return new Ingredient[size];
		}	
	};
	
	public Ingredient(Parcel source){
		readFromParcel(source);
	}
	
	public void readFromParcel(Parcel source){
		
		ingredient = source.readString();
		longerDescription = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(ingredient);
		dest.writeString(longerDescription);
	}
}
