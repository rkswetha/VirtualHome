package com.wikitude.virtualhome;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GalleryGridAdapter extends ArrayAdapter<GalleryItem> {
    private DisplayImageOptions options;


    private Context context;
    private int layoutResourceId;
    private ArrayList<GalleryItem> data = new ArrayList<GalleryItem>();


    public GalleryGridAdapter(Context context, int layoutResourceId, ArrayList<GalleryItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

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
        ImageLoader.getInstance().displayImage(item.getGalleryItemLocation(), holder.image, options);

        return row;
    }

    static class ImageHolder {
        TextView imageTitle;
        ImageView image;
        //Add price if needed
    }
}