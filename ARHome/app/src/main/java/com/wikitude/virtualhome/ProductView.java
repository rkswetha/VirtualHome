package com.wikitude.virtualhome;

/*
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ProductView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
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
}
*/

import android.app.Activity;
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

public class ProductView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside details creation");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);



        //Uncompressing:
        //added final
        //final byte[] bytes = getIntent().getByteArrayExtra("BMP");
        byte[] bytes = getIntent().getByteArrayExtra("BMP");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");


        TextView titleTextView = (TextView) findViewById(R.id.galleryItemTitle);
        //TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.galleryItemImage);
        //ImageView imageView = (ImageView) findViewById(R.id.item);
        imageView.setImageBitmap(bitmap);

        //description:
        TextView descriptionTextView = (TextView) findViewById(R.id.galleryItemDescription);
        descriptionTextView.setText(description);

        //location:


        //On text click:
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //Send the compressed image for NOW to the AR

               /* Intent intent = new Intent(Augented.this, ProductView.class);
                intent.putExtra("title", title);
                intent.putExtra("BMP", bytes);*/



                //As requested by swetha, sending the address.
                //Intent intent = new Intent(Augented.this, ProductView.class);
                String location = getIntent().getStringExtra("location");

                Toast.makeText(getApplicationContext(), "The AR View will be opened!", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "The location is:" + location, Toast.LENGTH_SHORT).show();

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
        Toast.makeText(getApplicationContext(), "The AR View will be opened!", Toast.LENGTH_SHORT).show();
    }
}