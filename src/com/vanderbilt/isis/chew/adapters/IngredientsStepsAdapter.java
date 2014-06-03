package com.vanderbilt.isis.chew.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.vanderbilt.isis.chew.recipes.Ingredient;
import com.vanderbilt.isis.chew.recipes.Step;
import com.vanderbilt.isis.chew.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IngredientsStepsAdapter extends BaseAdapter {

	private ArrayList<Ingredient> ingredients;
	private ArrayList<Step> steps;
	LayoutInflater inflater;

	public IngredientsStepsAdapter(Context context,
			ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {

		this.ingredients = ingredients;
		this.steps = steps;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return ingredients.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return ingredients.get(position) != null ? 0 : 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View curView = convertView;

		if (curView == null) {
			curView = inflater.inflate(R.layout.step_row_woi, parent, false);
		}

		TextView tv = (TextView) curView.findViewById(R.id.step);

		if (ingredients.get(position) != null) {

			tv.setText(ingredients.get(position).getLongerDescription());
		} else {
			
			tv.setText(steps.get(position).getStep());
		}

		return curView;
	}

}
