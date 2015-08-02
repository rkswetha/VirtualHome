package com.wikitude.virtualhome;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/*
public class SofaGallery_Old extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sofa_gallery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sofa_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
*/


import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class SofaGallery_Old extends Activity {

    private GridView gridView;
    private GalleryGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Inside gallery");
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_gallery_sofa);
        setContentView(R.layout.activity_sofa_gallery);

        gridView = (GridView) findViewById(R.id.gridViewSofa);
        gridAdapter = new GalleryGridAdapter(this, R.layout.grid_item_sofa, getImages());
        gridView.setAdapter(gridAdapter);



        System.out.println("just outside listener");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                System.out.println("Inside listener");
                GalleryItem item = (GalleryItem) parent.getItemAtPosition(position);
                System.out.println("after image creation");

              /*  //Create intent
                Intent intent = new Intent(SofaGallery_Old.this, ProductView.class);
                intent.putExtra("title", item.getGalleryItemTitle());

                //Compress
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                item.getGalleryItemImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("BMP", bytes);


                //decription:
                intent.putExtra("description", item.getGalleryItemDescription());

                //location

                intent.putExtra("location", item.getGalleryItemLocation());

                //Start details activity
                startActivity(intent);*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.activity_sofa_gallery, menu);
        getMenuInflater().inflate(R.menu.menu_sofa_gallery, menu);
        //activity_sofa_gallery
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        System.out.println("Something clicked");

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private ArrayList<GalleryItem> getImages() {
        //Need to alter the method of getting images
        final ArrayList<GalleryItem> imageslist = new ArrayList<>();
        Resources res = getResources();
        String[] names= res.getStringArray(R.array.sofanamelist);

        String[] descriptions = res.getStringArray(R.array.sofadescriptionlist);
        String[] locations = res.getStringArray(R.array.sofa_images);
        TypedArray images = res.obtainTypedArray(R.array.sofa_images);
        for (int i = 0; i < images.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(res, images.getResourceId(i, -1));
            String name = names[i];
            String description = descriptions[i];
            // System.out.println(description);

            // locations
            String location = locations[i];
            // System.out.println(location);

            // imageslist.add(new GalleryItem(bitmap, "Image#" + i));
            //imageslist.add(new GalleryItem(bitmap, name, description));

            imageslist.add(new GalleryItem(bitmap, name, description,location));


        }
        return imageslist;

        //recycle typed array
    }
}