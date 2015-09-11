package com.wikitude.virtualhome;

/*
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;


public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.ActionBar;


public class Home extends Activity {

    public static final String PREFERENCES_Gallery_FILE_NAME = "VHGalleryPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        boolean exitedStatus = settings.getBoolean("exitedStatus", true);
        Log.i("Home","After Entry:"+exitedStatus);





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

        //Setting exited status
        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("exitedStatus", true);
        editor.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/


        switch (item.getItemId()) {
            case R.id.action_camera:
                // OPen camera
                Toast.makeText(getApplicationContext(), "Gallery clicked", Toast.LENGTH_SHORT).show();
                showGallery();
                return true;
            case R.id.action_preference:
                // OPen login page
                Toast.makeText(getApplicationContext(), "Preferences", Toast.LENGTH_SHORT).show();
                openLogin();
                return true;

            case R.id.action_settings:

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


    public void showGallery() {
        //Intent intent = new Intent(this, Gallery_Furniture.class);
        System.out.println("Clicked gallery");
        Log.d("VirtualHome1", "Clicked gallery");

        //Intent intent = new Intent(this, SofaGallery_new.class);
        Intent intent = new Intent(this, SofaGallery.class);
        startActivity(intent);
    }

}