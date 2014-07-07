package com.vanderbilt.isis.chew.recipes;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.model.MainListRowItem;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
	
	private static final Logger logger = LoggerFactory.getLogger(Recipe.class);

	private int id;
	//private Bitmap image;
	// have to use String instead of Bitmap because of Parcelable
	private String image;
	private String title;
	private boolean isFavorite;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Step> steps;
	
	public Recipe(){
		logger.trace("Recipe())");
		ingredients = new ArrayList<Ingredient>();
		steps = new ArrayList<Step>();
	}

	public Recipe(int id, String image, String title, boolean isFavorite) {
		logger.trace("Recipe(int id, String image, String title, boolean isFavorite)");
		this.id = id;
		this.image = image;
		this.title = title;
		this.isFavorite = isFavorite;
	}

	public int getId() {
		logger.trace("getId()");
		return id;
	}

	public void setId(int id) {
		logger.trace("setId()");
		this.id = id;
	}
	
	/*public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}*/
	
	public String getImage() {
		logger.trace("getImage()");
		return image;
	}

	public void setImage(String image) {
		logger.trace("setImage()");
		this.image = image;
	}

	public String getTitle() {
		logger.trace("getTitle()");
		return title;
	}

	public void setTitle(String title) {
		logger.trace("setTitle()");
		this.title = title;
	}
	
	public boolean isFavorite() {
		logger.trace("isFavorite()");
		return isFavorite;
	}

	public void setFavorite(boolean f) {
		logger.trace("setFavorite()");
		this.isFavorite = f;
	}
	
	public ArrayList<Ingredient> getIngredients(){
		logger.trace("getIngredients()");
		return ingredients;
	}
	
	public void setIngredients(ArrayList<Ingredient> ingredients){
		logger.trace("setIngredients()");
		this.ingredients = ingredients;
	}
	
	public ArrayList<Step> getSteps(){
		logger.trace("getSteps()");
		return steps;
	}
	
	public void setSteps(ArrayList<Step> steps){
		logger.trace("setSteps()");
		this.steps = steps;
	}

	/** parcel part **/
	public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){

		@Override
		public Recipe createFromParcel(Parcel source) {
			logger.trace("createFromParcel()");
			return new Recipe(source);
		}

		@Override
		public Recipe[] newArray(int size) {
			logger.trace("newArray()");
			return new Recipe[size];
		}
	
	};
	
	public Recipe(Parcel source){
		
		this(); // to avoid null pointer exception
		logger.trace("Recipe(Parcel source)");
		readFromParcel(source);
	}
	
	public void readFromParcel(Parcel source){
		logger.trace("readFromParcel()");
		
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
		logger.trace("describeContents()");
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		logger.trace("writeToParcel()");
		dest.writeInt(id);
		//dest.writeParcelable(image, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(image);
		dest.writeString(title);
		dest.writeInt(isFavorite ? 0 : 1); // since there is no writeBoolean........
		dest.writeTypedList(ingredients);
		dest.writeTypedList(steps);
	}
}
