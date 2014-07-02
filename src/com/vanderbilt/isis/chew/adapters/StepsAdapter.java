package com.vanderbilt.isis.chew.adapters;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.vanderbilt.isis.chew.recipes.Step;

public class StepsAdapter extends ArrayAdapter<Step> {

	private static final Logger logger = LoggerFactory.getLogger(StepsAdapter.class);
	
	private final Context context;
	private final ArrayList<Step> values;

	public StepsAdapter(Context context, ArrayList<Step> values) {

		super(context, R.layout.step_row_woi, values);
		logger.trace("StepsAdapter()");
		this.context = context;
		this.values = values;
	}

	public static final int WITH_IMAGE = 0; // step with image
	public static final int NO_IMAGE = 1; // step without image

	@Override
	public int getItemViewType(int position) {
		logger.trace("getItemViewType()");
		Step s = getItem(position);
		/*
		 * if (s.hasImage()) { Log.d("IMAGE", "not null"); return WITH_IMAGE; }
		 * else { Log.d("IMAGE", "null"); return NO_IMAGE; }
		 */
		// String si = s.getImage();
		// Log.d("IMAGE NAME", si);
		if (s.hasImage())
			return WITH_IMAGE;
		else
			return NO_IMAGE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		logger.trace("getView()");
		final Step step = values.get(position);
		View row = convertView;
		LayoutInflater inflater = null;
		int type = getItemViewType(position);

		if (row == null) {

			if (type == WITH_IMAGE) {
				Log.d("TYPE", "with_image");
				logger.debug("TYPE {}", "with image" );
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.step_row_wi, null);
			} else if (type == NO_IMAGE) {
				Log.d("TYPE", "NO_image");
				logger.debug("TYPE {}", "NO_image");
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.step_row_woi, null);
			}
		}

		if (type == WITH_IMAGE) {

			ImageView imgView = (ImageView) row.findViewById(R.id.step_image);
			TextView txtView = (TextView) row.findViewById(R.id.step);

			int path = context.getResources().getIdentifier(step.getImage(),
					"drawable", "com.vanderbilt.isis.chew");
			Bitmap b = BitmapFactory.decodeResource(context.getResources(),
					path);
			imgView.setImageBitmap(b);

			txtView.setText(values.get(position).getStep());

		} else if (type == NO_IMAGE) {

			TextView txtView = (TextView) row.findViewById(R.id.step1);

			if(txtView != null)
				txtView.setText(values.get(position).getStep());
			else {
				Log.d("StepsAdapter", "txtView is null");
			    logger.debug("StepsAdapter {}", "txtView is null");
			}
		}

		return row;
	}

	public static class ViewHolder {
		public TextView step;
		public ImageView stepImage;
	}
}

/*
 * public class StepsAdapter extends ArrayAdapter<Step> {
 * 
 * private final Context context; private final ArrayList<Step> values;
 * 
 * public StepsAdapter(Context context, ArrayList<Step> values){
 * 
 * super(context, R.layout.step_row, values); this.context = context;
 * this.values = values; }
 * 
 * @Override public View getView(int position, View convertView, ViewGroup
 * parent){
 * 
 * LayoutInflater inflater = (LayoutInflater)
 * context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); View rowView =
 * inflater.inflate(R.layout.step_row, parent, false);
 * 
 * TextView tv = (TextView) rowView.findViewById(R.id.step);
 * tv.setText(values.get(position).getStep());
 * 
 * return rowView; } }
 */
