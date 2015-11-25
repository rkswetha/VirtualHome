package com.wikitude.virtualhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProductGalleryTabPage extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tabpage_main);

        GalleryButtonsFragment gbf = new GalleryButtonsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.your_placeholder, gbf).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gallery_tab, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        System.out.println("Something clicked");

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editpreferences) {

            Log.i("VirtualHome-Gallery", "Invoke Edit preference for returning user");
            Intent intent = new Intent(this, Preferences.class);
            intent.putExtra("newUserFlag","false");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
