package com.vanderbilt.isis.chew.adapters;

import java.util.ArrayList;
import com.vanderbilt.isis.chew.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vanderbilt.isis.chew.recipes.Ingredient;

public class IngredientsAdapter extends ArrayAdapter<Ingredient> {
	
	private final Context context;
	private final ArrayList<Ingredient> values;
	
	public IngredientsAdapter(Context context, ArrayList<Ingredient> values){
		
		super(context, R.layout.step_row_woi, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.step_row_woi, parent, false);
		
		TextView tv = (TextView) rowView.findViewById(R.id.step);
		tv.setText(values.get(position).getLongerDescription());
		
		return rowView;
	}
}
