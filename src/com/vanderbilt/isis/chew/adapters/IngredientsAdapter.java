package com.vanderbilt.isis.chew.adapters;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vanderbilt.isis.chew.recipes.Ingredient;

public class IngredientsAdapter extends ArrayAdapter<Ingredient> {
	
	private static final Logger logger = LoggerFactory.getLogger(IngredientsAdapter.class);
	
	private final Context context;
	private final ArrayList<Ingredient> values;
	
	public IngredientsAdapter(Context context, ArrayList<Ingredient> values){
		
		super(context, R.layout.step_row_woi, values);
		logger.trace("IngredientsAdapter()");
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		//logger.trace("getView()");
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.step_row_woi, parent, false);
		
		TextView tv = (TextView) rowView.findViewById(R.id.step);
		tv.setText(values.get(position).getLongerDescription());
		
		return rowView;
	}
}
