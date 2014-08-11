package com.vanderbilt.isis.chew.recipes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vanderbilt.isis.chew.db.ChewContract;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
	
	private static final Logger logger = LoggerFactory.getLogger(Step.class);

	//static Bitmap bitmap = null;
	
	//private Bitmap image;
	private String image;
	private String step;
	
	public Step(String i, String s){
		super();
		logger.trace("Step()");
		image = i;
		step = s;
	}
	
	public String getImage(){
		logger.trace("getImage()");
		return image;
	}
	
	public void setImage(String i){
		logger.trace("setImage()");
		image = i;
	}
	
	public String getStep(){
		logger.trace("getStep()");
		return step;
	}
	
	public void setStep(String s){
		logger.trace("setStep()");
		step = s;
	}
	
    public static Step fromCursor(Context c, Cursor curSteps) {
    	logger.trace("fromCursor()");
    	
    	int stepResourceId = c.getResources()
				.getIdentifier(curSteps.getString(curSteps.getColumnIndex(ChewContract.Steps.STEP)), "string",
						"com.vanderbilt.isis.chew");
    	
        String image_name = curSteps.getString(curSteps.getColumnIndex(ChewContract.Steps.STEP_IMAGE));
        String step = c.getString(stepResourceId);

        return new Step(image_name, step);
    }
    
    public boolean hasImage(){
    	logger.trace("hasImage()");
    	return getImage() != null;
    }

	/** Parcelable stuff **/
	public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>(){

		@Override
		public Step createFromParcel(Parcel source) {
			logger.trace("createFromParcel()");
			return new Step(source);
		}

		@Override
		public Step[] newArray(int size) {
			logger.trace("newArray()");
			return new Step[size];
		}
		
	};
	
	public Step(Parcel source){
		logger.trace("Step()");
		readFromParcel(source);
	}
	
	public void readFromParcel(Parcel source){
		logger.trace("readFromParcel()");
		image = source.readParcelable(Bitmap.class.getClassLoader());
		step = source.readString();
	}
	
	@Override
	public int describeContents() {
		logger.trace("describeContents()");
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		logger.trace("writeToParcel()");
		
		//dest.writeParcelable(image, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(image);
		dest.writeString(step);
	}
}
