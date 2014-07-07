package com.vanderbilt.isis.chew.adapters;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vanderbilt.isis.chew.R;
import com.vanderbilt.isis.chew.VoucherDescription;
import com.vanderbilt.isis.chew.recipes.Recipe;

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


public class GridViewAdapter extends ArrayAdapter<Recipe> {
	
	private static final Logger logger = LoggerFactory.getLogger(GridViewAdapter.class);
	
	Context context;
	int layoutResourceId;
	ArrayList<Recipe> data = new ArrayList<Recipe>();

	public GridViewAdapter(Context context, int layoutResourceId,
			ArrayList<Recipe> data) {
		super(context, layoutResourceId, data);
		logger.trace("GridViewAdapter()");
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		Log.d("DATA COUNT3", data.size()+"");
		logger.debug("DATA COUNT3 {}", data.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//logger.trace("getView()");
        View itemView = convertView;
        ViewHolder holder = null;

        if (itemView == null)
        {
            final LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.imageTitle = (TextView) itemView.findViewById(R.id.text);
            holder.image = (ImageView) itemView.findViewById(R.id.image);
            itemView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) itemView.getTag();
        }

        Recipe item = getItem(position);
        String recipeImage = item.getImage();
        int path = context.getResources().getIdentifier(recipeImage, "drawable", "com.vanderbilt.isis.chew");
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), path);
        holder.image.setImageBitmap(bitmap);
        holder.imageTitle.setText(item.getTitle());
        
        return itemView;

	}

	static class ViewHolder {
		TextView imageTitle;
		ImageView image;
	}
}
