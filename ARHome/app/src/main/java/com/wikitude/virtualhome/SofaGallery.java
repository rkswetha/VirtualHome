 package com.wikitude.virtualhome;

/**
 * Created by Radhika on 8/1/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


public class SofaGallery extends Activity {

    public static final String PREFERENCES_Gallery_FILE_NAME = "VHGalleryPreferences";

    private GridView gridView;
    private GalleryGridAdapter gridAdapter;
    ArrayList<GalleryItem> galleryImages;
    //java.util.Date date= new java.util.Date();
    Timestamp startTime;
    Timestamp endTime;
    long sTime;
    long eTime;
    String morePictures;
    boolean GalleryJsonStreamSuccess;
    boolean networkError;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Inside gallery");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sofa_gallery);
        System.out.println("just outside listener");

        startTime = new Timestamp( new Date().getTime());
        Log.i("VirtualHome-Gallery","start time:"+startTime);
        sTime=startTime.getTime();


        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        long lastEntryTime = settings.getLong("lastEntryTime", sTime);


        //Setting the current time

        //Getting difference
        //Checking if the window has been updated 60 mins back or if the application has exited (TO DISCUSS)
        //Getting application status:
        boolean exitedStatus = settings.getBoolean("exitedStatus", true);

        long timeDifference = sTime-lastEntryTime;

        //editor.apply();
        //TODO: Maybe use apply initially
        editor.commit();

        //Check exited status of another page (Home)????????????

        //1000>59*60 --> Reducing to 2 mins
        if(exitedStatus || (timeDifference/1000>(2*60)))
        {
            //Updating the value of entry time to reflect time of server update;
            editor.putLong("lastEntryTime", sTime);

            Log.i("Gallery", "Updating from server");
            //Setting the exitedStatus to false for further trials:
            editor.putBoolean("exitedStatus", false);
            editor.commit();
            new GalleryAsynTask().execute();
        }
        else
        {
            //Read from local
            Log.i("Gallery", "Updating from LOCAL");
            new GalleryFromSDAsynTask().execute();
        }



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
        System.out.println("Something clicked");

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.i("VirtualHome-Gallery", "External device mounted");

            return true;
        }
        Log.i("VirtualHome-Gallery", "External device NOT mounted");
        return false;

    }


    protected void onDestroy()
    {
        super.onDestroy();
    }

    protected void onPause()
    {
        super.onPause();
        Log.i("Gallery","PAUSED");

    }

    protected void onStop()
    {
        super.onStop();
        Log.i("Gallery","STOPPED");

    }


    protected void onResume()
    {
        super.onResume();
        Log.i("Gallery","RESUMED");

    }


    private class GalleryAsynTask extends AsyncTask<String, String, String> {

        private String[] names;
        private String[] descriptions;
        private String[] prices;

        private String[] imageLocations;
        Bitmap[] images;

        private void createJSONProductFile(String jsonStr) {

            try{
            boolean externalMounted= isExternalStorageWritable();
            File myDir=null;
            if(externalMounted)
            {
                String root = Environment.getExternalStorageDirectory().toString();

                Log.i("VirtualHome-Gallery", "Sofa gallery Directory---" + root);

                myDir = new File(root + "/VirtualHome/Ikea");

                if (!myDir.exists()) {
                    if (myDir.mkdirs()) {
                        System.out.println("Directory is created!");
                    } else {
                        System.out.println("Failed to create directory!");
                    }
                }
            }

            File file ;
            FileOutputStream out;
              if(externalMounted && myDir!=null) {
                file = new File(myDir, "sofa.json");
                filePath = file.getAbsolutePath();
                FileWriter filewr = new FileWriter(filePath);
                filewr.write(jsonStr);
                filewr.flush();
                filewr.close();
            }
          }
         catch(IOException ioe)
        {
            Log.i("VirtualHome-Gallery", " caught IO exception");
            ioe.printStackTrace();
        }
        }

        protected String doInBackground(String... arg0) {
            InputStream inputStream = null;
            BufferedReader reader = null;
            StringBuilder serverSB = new StringBuilder();



                //Get the json from a GET Rest end point
                //**********************************************************
                HttpURLConnection urlConnection = null;
                Log.i("Login","inside gallery");
                String url1= "http://ec2-52-11-109-4.us-west-2.compute.amazonaws.com:8080/api/v5/products/sofa";

                StringBuilder sb = new StringBuilder();
                try {

                    URL url = new URL(url1);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(false);
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.connect();

                    String responseMessage=urlConnection.getResponseMessage();
                    int HttpResult =urlConnection.getResponseCode();
                    System.out.println("CODE: "+HttpResult);
                   int len= urlConnection.getContentLength();
                    Log.i("gallery:","content length: "+len+"");

                    //TODO: check for created or check for validated. If not throw pop up as error.
                    System.out.println(responseMessage);

                    //TODO: this should be different status code
                    if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK ) {

                        Log.i("Login", "---Failed : HTTP error code : "
                                + urlConnection.getResponseCode());
                        GalleryJsonStreamSuccess=false;

                    /*throw new RuntimeException("Failed : HTTP error code : "
                            + urlConnection.getResponseCode());*/
                    }
                    else {
                        //It is verified:
                        GalleryJsonStreamSuccess = true;

                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                (urlConnection.getInputStream())));

                        String serverOutput;

                        System.out.println("Output from Server .... \n");

                        //To ensure the body is not zero
                        int count = 0;
                        while ((serverOutput = br.readLine()) != null) {
                            count++;
                            System.out.println(serverOutput);
                            //serverSB.append(serverOutput + "\n");
                            serverSB.append(serverOutput);

                        }
                        System.out.println("Full server output: "+ serverSB.toString());
                        Log.i("gallery", "count:" + count);

                        if (count == 0) {
                            GalleryJsonStreamSuccess = false;

                        } else {
                            GalleryJsonStreamSuccess =true;

                            //Parsing the returned json
                            JSONObject jsonObject = null;

                            try {
                                Log.i("LoginAsync ", "Retrieve jsonContent");

                                //System.out.println("inside : " + serverSB);

                                jsonObject = new JSONObject(serverSB.toString());
                                System.out.println("Full json object: "+jsonObject.toString());

                                JSONArray queryArray = jsonObject.getJSONArray("results");
                                int resultSize = queryArray.length();

                                Log.i("gallery","resultSize:"+resultSize);
                                Log.i("gallery","queryArray:"+queryArray);

                                //write to Sofa.json in SD Card
                                createJSONProductFile(jsonObject.toString());

                                 }catch(JSONException e)
                            {
                                Log.i("VirtualHome-Gallery", " caught JSON exception");
                                e.printStackTrace();
                                return null;
                            }
                        }
                    }}
                    catch (MalformedURLException e) {
                        networkError =true;
                        GalleryJsonStreamSuccess = true;
                        e.printStackTrace();
                    } catch (IOException e) {
                        networkError =true;
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null)
                            urlConnection.disconnect();
                    }


                //***********************************************************


            JSONObject jsonObject;
            try {
                Log.i("MyAsyncTask- ", "Retrieve jsonContent");

                //Changing the logic from reading the file to populating the data from the existing json.
                jsonObject = new JSONObject(serverSB.toString());

                JSONArray queryArray = jsonObject.getJSONArray("results");
                int resultSize = queryArray.length();

                names = new String[resultSize];
                descriptions = new String[resultSize];
                prices = new String[resultSize];
                //imageLocations = new URL[resultSize];
                imageLocations = new String[resultSize];
                images = new Bitmap[resultSize];

                galleryImages = new ArrayList<GalleryItem>();



                //*************************** File Save*******************

                boolean externalMounted= isExternalStorageWritable();
                File myDir=null;
                if(externalMounted)
                {
                    String root = Environment.getExternalStorageDirectory().toString();

                    Log.i("VirtualHome-Gallery", "Root Directory---" + root);

                     myDir = new File(root + "/VirtualHome/Ikea/Sofa");

                    if (!myDir.exists()) {
                        if (myDir.mkdirs()) {
                            System.out.println("Directory is created!");
                        } else {
                            System.out.println("Failed to create directory!");
                        }
                    }


                }

                File file ;
                FileOutputStream out;
                String filePath;

                for (int i = 0; i < queryArray.length(); i++) {
                    JSONObject jsonAttributes = queryArray.getJSONObject(i);

                    names[i] = jsonAttributes.getString("name");
                    descriptions[i] = jsonAttributes.getString("description");
                    prices[i] = jsonAttributes.getString("price");
                    //imageLocations[i] = new URL(jsonAttributes.getString("url"));

                    images[i] = BitmapFactory.decodeStream(new URL(jsonAttributes.getString("url")).openConnection().getInputStream());


                    //*************************** File Save*******************

                    if(externalMounted && myDir!=null) {
                        file = new File(myDir, names[i] + ".jpg");
                        filePath=file.getAbsolutePath();
                        //Log.i("file path",filePath );
                        try {
                            out = new FileOutputStream(file, false);
                            images[i].compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();

                            //Changing the file name:
                            imageLocations[i] = filePath;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    else
                    {
                        imageLocations[i] = jsonAttributes.getString("url");
                    }


                    galleryImages.add(new GalleryItem(images[i], names[i], descriptions[i], imageLocations[i].toString()));


                }



            } catch (JSONException ex) {
                Log.i("VirtualHome-Gallery", " caught JSON exception");
                ex.printStackTrace();
                return null;
            } catch (MalformedURLException ex1) {
                Log.i("VirtualHome-Gallery", " caught malformedException ");
                ex1.printStackTrace();
                return null;
            } catch (IOException ioe) {
                Log.i("VirtualHome-Gallery", " caught IO exception ");
                ioe.printStackTrace();
                return null;
            }

            // Read from image URL and creates a BitMap for each image.
            //createImages();

            return serverSB.toString();
        }


        protected void onPostExecute(String result) {

            Log.i("VirtualHome", "onPostExecute");


            endTime = new Timestamp(new Date().getTime());
            Log.i("VirtualHome-Gallery", "end time:" + endTime);
            eTime = endTime.getTime();
            Log.i("VirtualHome-Gallery", "start time:2" + sTime);
            Log.i("VirtualHome-Gallery", "end time:2" + eTime);
            Log.i("VirtualHome-Gallery", "Total time taken for gallery load2 " + (eTime - sTime));


            gridView = (GridView) findViewById(R.id.gridViewSofa);
            gridAdapter = new GalleryGridAdapter(SofaGallery.this, R.layout.grid_item_sofa, galleryImages);
            gridView.setAdapter(gridAdapter);


            //getting the extra:
            morePictures = getIntent().getStringExtra("additionalProduct");
            Log.i("Sofa Gallery", "morePictures " + morePictures);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    System.out.println("Inside listener");
                    GalleryItem item = (GalleryItem) parent.getItemAtPosition(position);
                    System.out.println("after image creation");

                    //Log.i("Sofa Gallery", "morePictures " + morePictures);
                    if(morePictures!=null) {
                        Log.i("Sofa Gallery", "inside not null");
                        if (morePictures.trim().equals("yes")) {
                            //This is called if additional images have to be added to the AR screen
                            Log.i("Sofa Gallery", "inside morePicture");
                            Intent intent1 = new Intent();
                            intent1.putExtra("location", item.getGalleryItemLocation());
                            intent1.putExtra("title", item.getGalleryItemTitle());
                            intent1.putExtra("description", item.getGalleryItemDescription());
                            setResult(2, intent1);
                            Log.i("Sofa Gallery", "inside morePicture-end");
                            finish();
                        }
                    }
                    else {
                        //This is called if it is the initial image to be chosen.
                        Log.i("Sofa Gallery", "inside first picture choice");

                        //Create intent
                        Intent intent = new Intent(SofaGallery.this, ProductView.class);
                        intent.putExtra("title", item.getGalleryItemTitle());

                        //Compress --- Commenting it out
                   /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    item.getGalleryItemImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    intent.putExtra("BMP", bytes);*/


                        //decription:
                        intent.putExtra("description", item.getGalleryItemDescription());

                        //location

                        intent.putExtra("location", item.getGalleryItemLocation());

                        //Start details activity
                        startActivity(intent);
                    }
                }
            });
        }


    }

    private class GalleryFromSDAsynTask extends AsyncTask<String, String, String> {

        private String[] names;
        private String[] descriptions;
        private String[] prices;

        private String[] imageLocations;
        //For data saving and giving url:
        //private URL[] imageLocations;
        Bitmap[] images;


        //Bitmap bitmap = BitmapFactory.decodeFile(location);


        protected String doInBackground(String... arg0) {
            String jsonContent = null;
            InputStream inputStream = null;
            BufferedReader reader = null;


            try {


                //Opening file in SD card
                boolean externalMounted= isExternalStorageWritable();
                File myDir=null;
                if(externalMounted)
                {
                    String root = Environment.getExternalStorageDirectory().toString();
                    myDir = new File(root + "/VirtualHome/Ikea");

                    if (!myDir.exists()) {
                        Log.i("Gallery", "ERROR : file does not exits!");
                        //Throw exception
                    }
                }

                File file ;
                if(externalMounted && myDir!=null) {
                    file = new File(myDir, "sofa.json");
                    filePath = file.getAbsolutePath();




                    //inputStream = getAssets().open("galleryObj.json");
                inputStream = new FileInputStream(filePath);
                    //TODO: Throw exception
                int size = inputStream.available();
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), size);

                StringBuilder theStringBuilder = new StringBuilder(size);
                String line = null;

                while ((line = reader.readLine()) != null) {
                    theStringBuilder.append(line + "\n");
                }
                jsonContent = theStringBuilder.toString();
                //Log.i("VirtualHome-size", Integer.toString(jsonContent.length()));
                //Log.i("VirtualHome", jsonContent);


            }} catch (IOException e) {
                Log.i("VirtualHome", "raised exception");
                e.printStackTrace();
            } finally {
                Log.i("VirtualHome", " at finally");
                try {
                    if (inputStream != null) inputStream.close();
                    if (reader != null) reader.close();
                } catch (Exception e) {
                }
            }

            JSONObject jsonObject;
            try {
                Log.i("MyAsyncTask- ", "Retrieve jsonContent");

                jsonObject = new JSONObject(jsonContent);

                JSONArray queryArray = jsonObject.getJSONArray("results");
                int resultSize = queryArray.length();

                names = new String[resultSize];
                descriptions = new String[resultSize];
                prices = new String[resultSize];
                //imageLocations = new URL[resultSize];
                imageLocations = new String[resultSize];
                images = new Bitmap[resultSize];

                galleryImages = new ArrayList<GalleryItem>();

                boolean externalMounted = isExternalStorageWritable();
                File myDir = null;
                if (externalMounted) {
                    String root = Environment.getExternalStorageDirectory().toString();
                    myDir = new File(root + "/VirtualHome/Ikea/Sofa");

                    if (!myDir.exists()) {
                        if (myDir.mkdirs()) {
                            System.out.println("Directory is created!");
                        } else {
                            System.out.println("Failed to create directory!");
                        }
                    }


                }

                File file;

                String filePath;

                for (int i = 0; i < queryArray.length(); i++) {
                    JSONObject jsonAttributes = queryArray.getJSONObject(i);

                    names[i] = jsonAttributes.getString("name");
                    descriptions[i] = jsonAttributes.getString("description");
                    prices[i] = jsonAttributes.getString("price");
                    //imageLocations[i] = new URL(jsonAttributes.getString("url"));
                    // images[i] = BitmapFactory.decodeStream(new URL(jsonAttributes.getString("url")).openConnection().getInputStream());

                    //CHECK to get the names programatically
                    if (externalMounted && myDir != null) {
                        file = new File(myDir, names[i] + ".jpg");
                        filePath = file.getAbsolutePath();
                        //Log.i("file path", filePath);
                        images[i] = BitmapFactory.decodeFile(filePath);
                        imageLocations[i] = filePath;
                    } else {
                        images[i] = BitmapFactory.decodeStream(new URL(jsonAttributes.getString("url")).openConnection().getInputStream());
                        imageLocations[i] = jsonAttributes.getString("url");
                    }



                    galleryImages.add(new GalleryItem(images[i], names[i], descriptions[i], imageLocations[i].toString()));


                }


            } catch (JSONException ex) {
                Log.i("VirtualHome-Gallery", " caught JSON exception");
                ex.printStackTrace();
                return null;
            } catch (MalformedURLException ex1) {
                Log.i("VirtualHome-Gallery", " caught malformedException ");
                ex1.printStackTrace();
                return null;
            } catch (IOException ioe) {
                Log.i("VirtualHome-Gallery", " caught IO exception ");
                ioe.printStackTrace();
                return null;
            }

            // Read from image URL and creates a BitMap for each image.
            //createImages();

            return jsonContent;
        }


        protected void onPostExecute(String result) {
            Log.i("VirtualHome", "onPostExecute");


            endTime = new Timestamp(new Date().getTime());
            Log.i("VirtualHome-Gallery", "end time:" + endTime);
            eTime = endTime.getTime();
            Log.i("VirtualHome-Gallery", "start time:2" + sTime);
            Log.i("VirtualHome-Gallery", "end time:2" + eTime);
            Log.i("VirtualHome-Gallery", "Total time taken for gallery load2 " + (eTime - sTime));


            gridView = (GridView) findViewById(R.id.gridViewSofa);
            gridAdapter = new GalleryGridAdapter(SofaGallery.this, R.layout.grid_item_sofa, galleryImages);
            gridView.setAdapter(gridAdapter);


            //getting the extra:
            morePictures = getIntent().getStringExtra("additionalProduct");
            Log.i("Sofa Gallery", "morePictures " + morePictures);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    System.out.println("Inside listener");
                    GalleryItem item = (GalleryItem) parent.getItemAtPosition(position);
                    System.out.println("after image creation");

                    //Log.i("Sofa Gallery", "morePictures " + morePictures);
                    if(morePictures!=null) {
                        Log.i("Sofa Gallery", "inside not null");
                        if (morePictures.trim().equals("yes")) {
                            //This is called if additional images have to be added to the AR screen
                            Log.i("Sofa Gallery", "inside morePicture");
                            Intent intent1 = new Intent();
                            intent1.putExtra("location", item.getGalleryItemLocation());
                            intent1.putExtra("title", item.getGalleryItemTitle());
                            intent1.putExtra("description", item.getGalleryItemDescription());
                            setResult(2, intent1);
                            Log.i("Sofa Gallery", "inside morePicture-end");
                            finish();
                        }
                    }
                    else {
                        //This is called if it is the initial image to be chosen.
                        Log.i("Sofa Gallery", "inside first picture choice");

                        //Create intent
                        Intent intent = new Intent(SofaGallery.this, ProductView.class);
                        intent.putExtra("title", item.getGalleryItemTitle());

                        //Compress --- Commenting it out
                   /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    item.getGalleryItemImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    intent.putExtra("BMP", bytes);*/


                        //decription:
                        intent.putExtra("description", item.getGalleryItemDescription());

                        //location

                        intent.putExtra("location", item.getGalleryItemLocation());

                        //Start details activity
                        startActivity(intent);
                    }
                }
            });

        }


 }


}
