package com.vanderbilt.isis.chew.recipes;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

	private int id;
	//private Bitmap image;
	// have to use String instead of Bitmap because of Parcelable
	private String image;
	private String title;
	private boolean isFavorite;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Step> steps;
	
	public Recipe(){
		
		ingredients = new ArrayList<Ingredient>();
		steps = new ArrayList<Step>();
	}

	public Recipe(int id, String image, String title, boolean isFavorite) {
		
		this.id = id;
		this.image = image;
		this.title = title;
		this.isFavorite = isFavorite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/*public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}*/
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean f) {
		this.isFavorite = f;
	}
	
	public ArrayList<Ingredient> getIngredients(){
		return ingredients;
	}
	
	public void setIngredients(ArrayList<Ingredient> ingredients){
		this.ingredients = ingredients;
	}
	
	public ArrayList<Step> getSteps(){
		return steps;
	}
	
	public void setSteps(ArrayList<Step> steps){
		this.steps = steps;
	}

	/** parcel part **/
	public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){

		@Override
		public Recipe createFromParcel(Parcel source) {
			return new Recipe(source);
		}

		@Override
		public Recipe[] newArray(int size) {
			return new Recipe[size];
		}
	
	};
	
	public Recipe(Parcel source){
		
		this(); // to avoid null pointer exception
		readFromParcel(source);
	}
	
	public void readFromParcel(Parcel source){
		
		id = source.readInt();
		//image = source.readParcelable(Bitmap.class.getClassLoader());
		image = source.readString();
		title = source.readString();
		isFavorite = source.readInt() == 0; // since there is no readBoolean........
		source.readTypedList(ingredients, Ingredient.CREATOR);
		source.readTypedList(steps, Step.CREATOR);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(id);
		//dest.writeParcelable(image, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(image);
		dest.writeString(title);
		dest.writeInt(isFavorite ? 0 : 1); // since there is no writeBoolean........
		dest.writeTypedList(ingredients);
		dest.writeTypedList(steps);
	}
}
