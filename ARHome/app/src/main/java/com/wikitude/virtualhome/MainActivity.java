package com.wikitude.virtualhome;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.wikitude.virtualhome.R;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends Activity {

    String markerPresent = null;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void invokeARActivity(View v) {

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
                        Toast.makeText(getApplicationContext(),
                                "You clicked on YES", Toast.LENGTH_SHORT)
                                .show();
                        try {
                            Intent theIntent = new Intent (getApplication(), AugmentedActivity.class);
                            theIntent.putExtra("MarkerPresent", "YES");
                            theIntent.putExtra("ImagePath", "assets/furniture1.png");
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
                        Toast.makeText(getApplicationContext(),
                                "You clicked on NO", Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                        try{
                            Intent theIntent = new Intent (getApplication(), AugmentedActivity.class);
                            theIntent.putExtra("MarkerPresent", "NO");
                            theIntent.putExtra("ImagePath", "assets/furniture1.png");
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
