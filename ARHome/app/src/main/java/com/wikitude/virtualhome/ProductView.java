package com.wikitude.virtualhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class ProductView extends Activity {

    String markerPresent = null;
    String location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside details creation");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);


        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        //Trying to get access the image from the storage

        //location:
        location = getIntent().getStringExtra("location");

        //CACHING

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();




        TextView titleTextView = (TextView) findViewById(R.id.galleryItemTitle);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.galleryItemImage);

        ImageLoader.getInstance().displayImage(location, imageView, options);

        //description:
        TextView descriptionTextView = (TextView) findViewById(R.id.galleryItemDescription);
        descriptionTextView.setText(description);



        //On text click:
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openARView();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_view, menu);
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


    public void openARView()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Virtual Home");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you have marker with you?")
                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        markerPresent = "YES";
                        /*Toast.makeText(getApplicationContext(),
                                "You clicked on YES", Toast.LENGTH_SHORT)
                                .show(); */
                        try {

                            Intent theIntent = new Intent (getApplication(), AugmentedActivity.class);
                            theIntent.putExtra("MarkerPresent", "YES");
                            theIntent.putExtra("ImagePath", location);
                            startActivity(theIntent);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "\n className not defined/accessible",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        markerPresent = "NO";
                        /*Toast.makeText(getApplicationContext(),
                                "You clicked on NO", Toast.LENGTH_SHORT)
                                .show();*/
                        dialog.cancel();
                        try{
                            Intent theIntent = new Intent (getApplication(), AugmentedActivity.class);
                            theIntent.putExtra("MarkerPresent", "NO");
                            theIntent.putExtra("ImagePath", location);
                            startActivity(theIntent);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "\n className not defined/accessible",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}