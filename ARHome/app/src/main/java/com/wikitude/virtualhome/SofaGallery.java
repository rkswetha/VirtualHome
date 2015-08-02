package com.wikitude.virtualhome;

/**
 * Created by Radhika on 8/1/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;


public class SofaGallery extends Activity {

    private GridView gridView;
    private GalleryGridAdapter gridAdapter;
    ArrayList<GalleryItem> galleryImages;
    java.util.Date date= new java.util.Date();
    Timestamp startTime;
    Timestamp endTime;
    long sTime;
    long eTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Inside gallery");
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_gallery_sofa);

        setContentView(R.layout.activity_sofa_gallery);

       /* gridView = (GridView) findViewById(R.id.gridViewSofa);
        gridAdapter = new GalleryGridAdapter(this, R.layout.grid_item_sofa, getImages());
        gridView.setAdapter(gridAdapter); */

        System.out.println("just outside listener");

        startTime = new Timestamp(date.getTime());
        Log.i("VirtualHome-Gallery","start time:"+startTime);
        sTime=startTime.getTime();
        new GalleryAsynTask().execute();






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


    private class GalleryAsynTask extends AsyncTask<String, String, String>{

        private String[] names;
        private String[] descriptions;
        private String[] prices;
        //private String[] imageLocations;
        private URL[] imageLocations;
        Bitmap[] images;


        protected String doInBackground(String... arg0){
            String jsonContent = null;
            InputStream inputStream = null;
            BufferedReader reader = null;



            try {
                AssetManager aMan = getAssets();
                String[] filelist = aMan.list("");
                //Log.i("VirtualHome1","before list");
                //Log.i("VirtualHome1", filelist.length+"---");
                for(String file:filelist)
                {
                    Log.i("VirtualHome1",file);
                }

                inputStream = getAssets().open("galleryObj.json");
                int size = inputStream.available();
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), size);

                StringBuilder theStringBuilder = new StringBuilder(size);
                String line = null;

                while((line = reader.readLine()) != null)
                {
                    theStringBuilder.append(line+"\n");
                }
                jsonContent = theStringBuilder.toString();
                Log.i("VirtualHome-size", Integer.toString(jsonContent.length()));
                Log.i("VirtualHome", jsonContent);


            }catch(IOException e)
            {
                Log.i("VirtualHome", "raised exception");
                e.printStackTrace();
            }finally {
                Log.i("VirtualHome", " at finally");
                try{if(inputStream != null)inputStream.close();
                    if(reader != null)reader.close();}
                catch(Exception e){}
            }

            JSONObject jsonObject;
            try{
                Log.i("MyAsyncTask- ", "Retrieve jsonContent");

                jsonObject = new JSONObject(jsonContent);

                JSONArray queryArray = jsonObject.getJSONArray("results");
                int resultSize = queryArray.length();

                names = new String[resultSize];
                descriptions = new String[resultSize];
                prices = new String[resultSize];
                imageLocations= new URL[resultSize];
                images=new Bitmap[resultSize];

                galleryImages = new ArrayList<GalleryItem>();



                for(int i=0; i< queryArray.length(); i++)
                {
                    JSONObject jsonAttributes = queryArray.getJSONObject(i);

                    names[i] = jsonAttributes.getString("name");
                    descriptions[i]= jsonAttributes.getString("description");
                    prices[i]= jsonAttributes.getString("price");
                    imageLocations[i]=new URL(jsonAttributes.getString("url"));
                    images[i] = BitmapFactory.decodeStream(imageLocations[i].openConnection().getInputStream());

                    Log.i("Name", jsonAttributes.getString("name"));
                    Log.i("decription", jsonAttributes.getString("description"));
                    Log.i("price", jsonAttributes.getString("price"));
                    Log.i("url", jsonAttributes.getString("url"));


                    galleryImages.add(new GalleryItem(images[i], names[i], descriptions[i], imageLocations[i].toString()));


                }




            }
            catch (JSONException ex) {
                Log.i("VirtualHome-Gallery", " caught JSON exception");
                ex.printStackTrace();
                return null;
            }
            catch(MalformedURLException ex1)
            {
                Log.i("VirtualHome-Gallery", " caught malformedException ");
                ex1.printStackTrace();
                return null;
            }
            catch(IOException ioe)
            {
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


            endTime = new Timestamp(date.getTime());
            Log.i("VirtualHome-Gallery","end time:"+endTime);
            eTime=endTime.getTime();
            Log.i("VirtualHome-Gallery","Total time taken for gallery load"+(eTime-sTime));



            gridView = (GridView) findViewById(R.id.gridViewSofa);
            gridAdapter = new GalleryGridAdapter(SofaGallery.this, R.layout.grid_item_sofa,galleryImages);
            gridView.setAdapter(gridAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    System.out.println("Inside listener");
                    GalleryItem item = (GalleryItem) parent.getItemAtPosition(position);
                    System.out.println("after image creation");

                    //Create intent
                    Intent intent = new Intent(SofaGallery.this, ProductView.class);
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
                    startActivity(intent);
                }
            });

        }
    }






}
