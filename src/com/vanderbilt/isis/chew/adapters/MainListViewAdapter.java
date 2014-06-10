package com.vanderbilt.isis.chew.adapters;

import java.util.List;

import com.vanderbilt.isis.chew.R;
import com.vanderbilt.isis.chew.model.MainListRowItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainListViewAdapter extends ArrayAdapter<MainListRowItem> {

	Context context;

	public MainListViewAdapter(Context context, int resourceId,
			List<MainListRowItem> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView imageView;
		TextView txtTitle;
		TextView txtDesc;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MainListRowItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.main_list_row, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
			holder.txtDesc = (TextView) convertView.findViewById(R.id.subtitle);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.list_image);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.txtDesc.setText(rowItem.getDesc());
		holder.txtTitle.setText(rowItem.getTitle());
		holder.imageView.setImageResource(rowItem.getImageId());

		return convertView;
	}
}
