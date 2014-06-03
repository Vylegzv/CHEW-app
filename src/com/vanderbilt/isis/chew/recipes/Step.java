package com.vanderbilt.isis.chew.recipes;

import com.vanderbilt.isis.chew.db.ChewContract;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Step implements Parcelable {

	//static Bitmap bitmap = null;
	
	//private Bitmap image;
	private String image;
	private String step;
	
	public Step(String i, String s){
		super();
		image = i;
		step = s;
	}
	
	public String getImage(){
		
		return image;
	}
	
	public void setImage(String i){
		
		image = i;
	}
	
	public String getStep(){
		
		return step;
	}
	
	public void setStep(String s){
		
		step = s;
	}
	
    public static Step fromCursor(Context context, Cursor curSteps) {
    	
        String image_name = curSteps.getString(curSteps.getColumnIndex(ChewContract.Steps.STEP_IMAGE));
        String step = curSteps.getString(curSteps.getColumnIndex(ChewContract.Steps.STEP));
        
        //Log.d("IMAGE STEP", image_name);
        
        /*if(!step.equals("")){
        	int path = context.getResources().getIdentifier(image_name, "drawable", "com.vanderbilt.isis.chew");
        	bitmap = BitmapFactory.decodeResource(context.getResources(), path);
        } */      

        return new Step(image_name, step);
    }
    
    public boolean hasImage(){
    	return getImage() != null;
    }

	/** Parcelable stuff **/
	public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>(){

		@Override
		public Step createFromParcel(Parcel source) {
			return new Step(source);
		}

		@Override
		public Step[] newArray(int size) {
			return new Step[size];
		}
		
	};
	
	public Step(Parcel source){
		readFromParcel(source);
	}
	
	public void readFromParcel(Parcel source){
		
		image = source.readParcelable(Bitmap.class.getClassLoader());
		step = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		//dest.writeParcelable(image, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(image);
		dest.writeString(step);
	}
}
