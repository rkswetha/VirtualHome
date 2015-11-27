package com.wikitude.virtualhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProductView extends Activity {

    String markerPresent = null;
    String location = null;
    String productID = null;
    String userIDStr = null;
    public static final String PREFERENCES_Gallery_FILE_NAME = "VHGalleryPreferences";
    String ConstantURL = URLAPIConstant.URL;
    JSONObject userPrefJson;
    boolean galleryJsonStreamSuccess;
    String[] imageLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside details creation");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);


        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        productID = getIntent().getStringExtra("productid");
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

        //Get the user id: If logged in user then update user transactions else

        //getting no userID status:
        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        userIDStr = settings.getString("user_id", "-1");
        Log.i("Pref", "UserID: " + userIDStr);



        //update transactions only for the logged in user
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userIDStr.equals("-1")) {
                    openARView();
                    imageLocations = new String[0];
                }
                else
                    getUserTransactions();
            }
        });

    }


    public void getUserTransactions() {

        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        int userID = Integer.parseInt(userIDStr);
        userPrefJson = new JSONObject();
        try {

            Log.i("VirtualHome-ProductView", "create json user transaction");
            userPrefJson.put("user_id", userID);
            userPrefJson.put("product_id", productID);


        } catch (JSONException e) {
            Log.i("VirtualHome-ProductView", "Exception in Json creation");
            e.printStackTrace();
        }

        Log.i("VirtualHome-ProductView", "JSON User preference input:" + userPrefJson.toString());

        new UserTransactionAsynTask().execute();

    }


    private class UserTransactionAsynTask extends AsyncTask<String, String, String> {

        JSONArray queryArray;

        protected String doInBackground(String... arg0) {
            HttpURLConnection urlConnection = null;
            Log.i("VirtualHome-ProductView", "Inside user transactions");
            //int userID = Integer.parseInt(userIDStr);
            String url1 = ConstantURL + "usertransactions/"+userIDStr;

            StringBuilder sb = new StringBuilder();
            try {

                URL url = new URL(url1);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                //urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(userPrefJson.toString());
                out.flush();
                out.close();

                String responseMessage = urlConnection.getResponseMessage();
                int HttpResult = urlConnection.getResponseCode();
                System.out.println("CODE: " + HttpResult);

                //TODO: check for created or check for validated. If not throw pop up as error.
                System.out.println(responseMessage);

                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                    Log.i("VirtualHome:AR", "---Failed : HTTP error code : "
                            + urlConnection.getResponseCode());
                    galleryJsonStreamSuccess = false;

                } else {
                    //It is verified:
                    galleryJsonStreamSuccess = true;
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (urlConnection.getInputStream())));

                    String serverOutput;
                    StringBuilder serverSB = new StringBuilder();
                    System.out.println("Output from Server .... \n");
                    while ((serverOutput = br.readLine()) != null) {

                        System.out.println(serverOutput);
                        serverSB.append(serverOutput + "\n");

                    }

                    JSONObject jsonObject = null;

                    try {
                        Log.i("VirtualHome:AR ", "Retrieve jsonContent");

                        jsonObject = new JSONObject(serverSB.toString());
                        System.out.println("Full json object: " + jsonObject.toString());

                        queryArray = jsonObject.getJSONArray("results");
                        int resultSize = queryArray.length();
                        int count = jsonObject.getInt("total_results_count");
                        Log.i("VirtualHome:AR", "queryArray:" + queryArray);
                        Log.i("VirtualHome:AR", "queryArray size:" + resultSize);
                        if (count ==0) {
                            //If count ==0 send an empty array-- First time registration user
                            imageLocations = new String[count];
                        }
                         else if (count > 0) {
                            imageLocations = new String[count];
                            for (int i = 0; i < queryArray.length(); i++) {
                                JSONObject jsonAttributes = queryArray.getJSONObject(i);
                                imageLocations[i] = jsonAttributes.getString("url");
                            }
                        }

                    } catch (JSONException e) {
                        Log.i("VirtualHome:AR", " caught JSON exception");
                        e.printStackTrace();
                        return null;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return "";

        }

        protected void onPostExecute(String result) {
            Log.i("VirtualHome:AR", "onPostExecute-Getting POST reply: result"+result);

            if (!galleryJsonStreamSuccess) {
                Toast.makeText(getApplicationContext(), "Updating transactions failed", Toast.LENGTH_SHORT).show();
            } else {

                for (int i = 0; i < imageLocations.length; i++) {
                    Log.i("VirtualHome-ProductView", "onPostExecute:URL obtained"+imageLocations[i]);
                }
                openARView();

            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_product_view, menu);
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

        //If null
        //final String[] images = imageLocations;
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
                            theIntent.putExtra("productid",productID);
                            theIntent.putExtra("PastTransaction",imageLocations );
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
                            theIntent.putExtra("productid",productID);
                            theIntent.putExtra("PastTransaction",imageLocations );
                           // Log.i("images intent", imageLocations[0]);
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