package com.wikitude.virtualhome;

/**
 * Created by Radhika on 8/1/2015.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.TextView;

public class GalleryGridAdapter extends ArrayAdapter<GalleryItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<GalleryItem> data = new ArrayList<GalleryItem>();

    public GalleryGridAdapter(Context context, int layoutResourceId, ArrayList<GalleryItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ImageHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.gridtext);
            holder.image = (ImageView) row.findViewById(R.id.gridimage);
            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }


        GalleryItem item = data.get(position);
        holder.imageTitle.setText(item.getGalleryItemTitle());
        //change here to get from location (see speed)

        holder.image.setImageBitmap(item.getGalleryItemImage());
        return row;
    }

    static class ImageHolder {
        TextView imageTitle;
        ImageView image;

        //Add price if needed
    }
}