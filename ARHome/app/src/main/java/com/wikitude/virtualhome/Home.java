package com.wikitude.virtualhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class Home extends Activity {

    public static final String PREFERENCES_Gallery_FILE_NAME = "VHGalleryPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.home_actions, menu);
        return true;*/

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("Home","DESTROY");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_camera:
                // OPen camera
                //Toast.makeText(getApplicationContext(), "Gallery clicked", Toast.LENGTH_SHORT).show();
                showGallery();
                return true;
            case R.id.action_login:
                // OPen login page
                //Toast.makeText(getApplicationContext(), "Preferences", Toast.LENGTH_SHORT).show();
                openLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }

    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        //System.out.println("Clicked login");
        Log.d("VirtualHome1", "Clicked login");
    }


    public void openPreference() {
        Intent intent = new Intent(this, Preferences.class);
        startActivity(intent);
        //System.out.println("Clicked login");
        Log.d("VirtualHome1", "Clicked Preference");
    }


    public void showGallery() {
        //Intent intent = new Intent(this, Gallery_Furniture.class);
        System.out.println("Clicked gallery");
        Log.d("VirtualHome1", "Clicked gallery");

        //Intent intent = new Intent(this, SofaGallery_new.class);
        Intent intent = new Intent(this, ProductGalleryTabPage.class);
        startActivity(intent);
    }

}
