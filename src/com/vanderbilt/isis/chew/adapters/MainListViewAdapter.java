package com.vanderbilt.isis.chew.adapters;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.R;
import com.vanderbilt.isis.chew.model.MainListRowItem;
import com.vanderbilt.isis.chew.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainListViewAdapter extends ArrayAdapter<MainListRowItem> {

	private static final Logger logger = LoggerFactory
			.getLogger(MainListViewAdapter.class);

	Context context;

	public MainListViewAdapter(Context context, int resourceId,
			List<MainListRowItem> items) {
		super(context, resourceId, items);
		logger.trace("MainListViewAdapter()");
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView imageView;
		TextView txtTitle;
		TextView txtDesc;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// logger.trace("getView()");
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

		// use this to specify image decoding options
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// setting this property to true while decoding avoids memory
		// allocation, returning null for the bitmap, but setting
		// outWidth, outHeight, and outMimeType. This allows to read
		// the dimensions and type of the image data prior to
		// construction and memory allocation of the bitmap
		options.inJustDecodeBounds = true;
		
		int id = rowItem.getImageId();
		Bitmap decodedBitmap = Utils.decodeImage(context, id);

		holder.imageView.setImageBitmap(decodedBitmap);

		return convertView;
	}
}
