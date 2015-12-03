package com.wikitude.virtualhome;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class LivingRoomGalleryFragment extends Fragment {

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
    String ConstantURL = URLAPIConstant.URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // caching initialization
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .build();
        ImageLoader.getInstance().init(config);

        View v = inflater.inflate(R.layout.activity_sofa_gallery, container, false);
        System.out.println("just outside listener");

        startTime = new Timestamp( new Date().getTime());
        Log.i("VirtualHome-Gallery","start time:"+startTime);
        sTime=startTime.getTime();

        SharedPreferences settings = this.getActivity().getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        long lastEntryTime = settings.getLong("lastEntryTime", sTime);

        //Updating the value of entry time to reflect time of server update;
        editor.putLong("lastEntryTime", sTime);

        Log.i("Gallery", "Updating from server");

        new GalleryAsynTask().execute();
        setHasOptionsMenu(true);
        ActionBar actionbar = getActivity().getActionBar();
        actionbar.setTitle("Living Room Gallery");
        this.setRetainInstance(true);
        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_product_gallery, menu);
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


    public void onDestroy()
    {
        super.onDestroy();
    }

    public void onPause()
    {
        super.onPause();
        Log.i("Gallery","PAUSED");

    }

    public void onStop()
    {
        super.onStop();
        Log.i("Gallery","STOPPED");

    }


    public void onResume()
    {
        super.onResume();
        Log.i("Gallery","RESUMED");

    }


    private class GalleryAsynTask extends AsyncTask<String, String, String> {

        private String[] productID;
        private String[] names;
        private String[] descriptions;
        private String[] prices;

        private String[] imageLocations;
        Bitmap[] images;
        int resultSize;
        private void createJSONProductFile(String jsonStr) {

            try{
                boolean externalMounted= isExternalStorageWritable();
                File myDir=null;
                if(externalMounted)
                {
                    String root = Environment.getExternalStorageDirectory().toString();

                    Log.i("VirtualHome-Gallery", "Living Room gallery Directory---" + root);

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
                    file = new File(myDir, "livingroom.json");
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
            String url1= ConstantURL+"products/livingroom";
            //String url1= "http://ec2-52-11-109-4.us-west-2.compute.amazonaws.com:8080/api/v8/products/livingroom";

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

                            jsonObject = new JSONObject(serverSB.toString());
                            System.out.println("Full json object: "+jsonObject.toString());

                            JSONArray queryArray = jsonObject.getJSONArray("results");
                            int resultSize = queryArray.length();

                            Log.i("gallery","resultSize:"+resultSize);
                            Log.i("gallery","queryArray:"+queryArray);

                            //write to livingroom.json in SD Card
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
                resultSize = queryArray.length();

                productID = new String[resultSize];
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

                    myDir = new File(root + "/VirtualHome/Ikea/LivingRoom");

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

                    productID[i] = jsonAttributes.getString("productid");
                    names[i] = jsonAttributes.getString("name");
                    descriptions[i] = jsonAttributes.getString("description");
                    prices[i] = jsonAttributes.getString("price");
                    imageLocations[i] = jsonAttributes.getString("url");
                    galleryImages.add(new GalleryItem( names[i], descriptions[i], imageLocations[i],productID[i]));


                }



            } catch (JSONException ex) {
                Log.i("VirtualHome-Gallery", " caught JSON exception");
                ex.printStackTrace();
                return null;
            }

            return serverSB.toString();
        }


        protected void onPostExecute(String result) {

            Log.i("VirtualHome", "onPostExecute");


            if(networkError) {
                Toast.makeText(getActivity(), "Network Error!!", Toast.LENGTH_SHORT).show();
                networkError = false;
            }

            endTime = new Timestamp(new Date().getTime());
            Log.i("VirtualHome-Gallery", "end time:" + endTime);
            eTime = endTime.getTime();
            Log.i("VirtualHome-Gallery", "start time:2" + sTime);
            Log.i("VirtualHome-Gallery", "end time:2" + eTime);
            Log.i("VirtualHome-Gallery", "Total time taken for gallery load2 " + (eTime - sTime));

            if (resultSize>0){
            gridView = (GridView) getView().findViewById(R.id.gridViewSofa);
            gridAdapter = new GalleryGridAdapter(getActivity(), R.layout.grid_item_sofa, galleryImages);
            gridView.setAdapter(gridAdapter);

            //getting the extra:
            morePictures = getActivity().getIntent().getStringExtra("additionalProduct");
            Log.i("Living Room Gallery", "morePictures " + morePictures);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    System.out.println("Inside listener");
                    GalleryItem item = (GalleryItem) parent.getItemAtPosition(position);
                    System.out.println("after image creation");

                    if (morePictures != null) {
                        Log.i("Living Room Gallery", "inside not null");

                        if (morePictures.trim().equals("yes")) {

                            //This is called if additional images have to be added to the AR screen
                            Log.i("Living Room Gallery", "inside morePicture");
                            Intent intent1 = new Intent();
                            intent1.putExtra("location", item.getGalleryItemLocation());
                            intent1.putExtra("title", item.getGalleryItemTitle());
                            intent1.putExtra("description", item.getGalleryItemDescription());
                            getActivity().setResult(2, intent1);
                            Log.i("Living Room Gallery", "inside morePicture-end");
                            getActivity().finish();
                        }
                    } else {
                        //This is called if it is the initial image to be chosen.
                        Log.i("Living Room Gallery", "inside first picture choice");

                        //Create intent
                        Intent intent = new Intent(getActivity(), ProductView.class);
                        intent.putExtra("title", item.getGalleryItemTitle());
                        intent.putExtra("description", item.getGalleryItemDescription());
                        intent.putExtra("location", item.getGalleryItemLocation());
                        intent.putExtra("productid", item.getGalleryItemProductID());
                        startActivity(intent);
                    }
                }
            });
            } else {
                Toast.makeText(getActivity(), "Cannot load Living Room product gallery at this time", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        }


    }



}
