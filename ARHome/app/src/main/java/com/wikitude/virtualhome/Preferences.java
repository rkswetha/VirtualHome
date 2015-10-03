package com.wikitude.virtualhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Preferences extends Activity {


    Spinner spinner1,spinner2,spinner3;
    CheckBox checkbox1,checkbox2,checkbox3,checkbox4,checkbox5,checkbox6;

    String family = null;
    String gender = null;
    String profession = null;
    boolean chstate1,chstate2,chstate3,chstate4,chstate5,chstate6;
    JSONObject userPrefJson;
    String newUserFlag = null;
    boolean noUserID=false;
    public static final String PREFERENCES_Gallery_FILE_NAME = "VHGalleryPreferences";
    String userID =null;
    String emailID=null;
    private boolean preferenceCreationNotSuccess=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getting no userID status:
        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();


        userID = settings.getString("user_id", "-1");
        emailID=settings.getString("email","-1");
       /* //For checking:
        userID = "-1";*/


        Log.i("Pref", "UserIDStatus: " + userID);

        //Obtaining the new user flag status flg status
        newUserFlag = getIntent().getStringExtra("newUserFlag");

        //Just for checking:
        newUserFlag="false";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button button = (Button) findViewById(R.id.button_submit_preferences);

        checkbox1= (CheckBox) findViewById(R.id.checkbox_hobby1); //gardening

        checkbox2= (CheckBox) findViewById(R.id.checkbox_hobby2); //interiorDesign

        checkbox3= (CheckBox) findViewById(R.id.checkbox_hobby3); //cooking

        checkbox4= (CheckBox) findViewById(R.id.checkbox_hobby4); //painting

        checkbox5= (CheckBox) findViewById(R.id.checkbox_hobby5); //reading

        checkbox6= (CheckBox) findViewById(R.id.checkbox_hobby6); //music


        //getting the preferences
//Disabling the button if no user has logged
        if (userID.equals("-1")) {
            noUserID = true;
            Toast.makeText(getApplicationContext(), "You have not logged in! Please login to set preferences", Toast.LENGTH_SHORT).show();
            button.setEnabled(false);
            //to confirm
            Log.i("button disabled: ", button.isEnabled() + "");

            //Making the newUserFlag set as true, so that the the page displays but with the submit button disabled.
            newUserFlag="true";

        }




//Disabling the button if no user has logged
        if (newUserFlag != null) {
            if (newUserFlag.equals("true")) {

                Log.i("pref:", "inside new user");

                // Spinner element
                spinner1 = (Spinner) findViewById(R.id.FamilyType);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                        R.array.familytypelist, R.layout.spinner_style_preferences);
                // Specify the layout to use when the list of choices appears
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner1.setAdapter(adapter1);

                spinner2 = (Spinner) findViewById(R.id.SexType1);
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                        R.array.sextypelist, R.layout.spinner_style_preferences);
                // Specify the layout to use when the list of choices appears
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner2.setAdapter(adapter2);



                spinner3 = (Spinner) findViewById(R.id.ProfessionType);
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                        R.array.professiontypelist, R.layout.spinner_style_preferences);
                // Specify the layout to use when the list of choices appears
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner3.setAdapter(adapter3);


                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                        onSubmit();
                    }
                });


            } else {
                //getting the preferences:

                int gender=settings.getInt("gender",-1);
                int family=settings.getInt("family",-1);
                int profession=settings.getInt("profession",-1);

                boolean gardening=settings.getBoolean("gardening", true);
                boolean interiorDesign=settings.getBoolean("interiorDesign",false);
                boolean cooking=settings.getBoolean("cooking",false);
                boolean painting=settings.getBoolean("painting",false);
                boolean reading=settings.getBoolean("reading",false);
                boolean music=settings.getBoolean("music",false);





                Log.i("pref:", "inside returning user");

                Log.i("pref:", "gender"+gender+" family"+family+"profession"+profession);


                if(gender!=-1&&family!=-1&&profession!=-1)
                {
                    // Spinner element
                    spinner1 = (Spinner) findViewById(R.id.FamilyType);
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                            R.array.familytypelist, R.layout.spinner_style_preferences);
                    // Specify the layout to use when the list of choices appears
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner1.setAdapter(adapter1);
                    spinner1.setSelection(family);

                    spinner2 = (Spinner) findViewById(R.id.SexType1);
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                            R.array.sextypelist, R.layout.spinner_style_preferences);
                    // Specify the layout to use when the list of choices appears
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner2.setAdapter(adapter2);
                    spinner2.setSelection(gender);


                    spinner3 = (Spinner) findViewById(R.id.ProfessionType);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                            R.array.professiontypelist, R.layout.spinner_style_preferences);
                    // Specify the layout to use when the list of choices appears
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner3.setAdapter(adapter3);
                    spinner3.setSelection(profession);

                }

                checkbox1.setChecked(gardening);
                checkbox2.setChecked(interiorDesign);
                checkbox3.setChecked(cooking);
                checkbox4.setChecked(painting);
                checkbox5.setChecked(reading);
                checkbox6.setChecked(music);



                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                        onSubmit();
                    }
                });



            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
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

    public void onSubmit()
    {
        //collect on the data
        spinner1 = (Spinner) findViewById(R.id.FamilyType);
        spinner2 = (Spinner) findViewById(R.id.SexType1);
        spinner3 = (Spinner) findViewById(R.id.ProfessionType);

        family = String.valueOf(spinner1.getSelectedItem());
        gender = String.valueOf(spinner2.getSelectedItem());
        profession = String.valueOf(spinner3.getSelectedItem());

        int familyIndex=spinner1.getSelectedItemPosition();
        int genderIndex=spinner2.getSelectedItemPosition();
        int professionIndex=spinner3.getSelectedItemPosition();

        checkbox1= (CheckBox) findViewById(R.id.checkbox_hobby1); //gardening
        chstate1=checkbox1.isChecked();

        checkbox2= (CheckBox) findViewById(R.id.checkbox_hobby2); //interiorDesign
        chstate2=checkbox2.isChecked();

        checkbox3= (CheckBox) findViewById(R.id.checkbox_hobby3); //cooking
        chstate3=checkbox3.isChecked();

        checkbox4= (CheckBox) findViewById(R.id.checkbox_hobby4); //painting
        chstate4=checkbox4.isChecked();

        checkbox5= (CheckBox) findViewById(R.id.checkbox_hobby5); //reading
        chstate5=checkbox5.isChecked();

        checkbox6= (CheckBox) findViewById(R.id.checkbox_hobby6); //music
        chstate6=checkbox6.isChecked();

        String result = family + " " + gender + " " + profession + " " + "1:" + chstate1 + "2:" + chstate2 + "3:" + chstate3 + "4:" + chstate4 + "5:" + chstate5 + "6:" + chstate6 + "x:" + familyIndex+ "y:" + genderIndex+ "z:" + professionIndex;
        Log.i("pref", result);

        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


        //Create preferences json and call the server.
        createUserJson();

        //Save the features to local
        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();


        editor.putInt("gender", genderIndex) ;
        editor.putInt("family", familyIndex);
        editor.putInt("profession", professionIndex);
        editor.putBoolean("gardening", chstate1);
        editor.putBoolean("interiorDesign", chstate2);
        editor.putBoolean("cooking", chstate3);
        editor.putBoolean("painting", chstate4);
        editor.putBoolean("reading", chstate5);
        editor.putBoolean("music", chstate6);

        editor.commit();



        //Launching the gallery after data insertion
       // launchGallery();


    }


    public void launchGallery()
    {
        Log.d("VirtualHome1", "Clicked gallery");
        //Intent intent = new Intent(this, SofaGallery_Old.class);
        Intent intent = new Intent(this, SofaGallery.class);
        startActivity(intent);
    }



    public void createUserJson()
    {
        userPrefJson=new JSONObject();
        try {

            //userEmail

            userPrefJson.put("user_id",userID);
            userPrefJson.put("email",emailID );
            userPrefJson.put("sex", gender);
            userPrefJson.put("family", family);
            userPrefJson.put("profession", profession);

            userPrefJson.put("gardening", chstate1);
            userPrefJson.put("interiorDesign", chstate2);
            userPrefJson.put("cooking", chstate3);
            userPrefJson.put("painting", chstate4);
            userPrefJson.put("reading", chstate5);
            userPrefJson.put("music", chstate6);

        }
        catch(JSONException e) {
            Log.i("VirtualHome-Login","Exception in Json creation");
            e.printStackTrace();
        }



        //Post to the server
       // postToServer();
        new PreferenceAsynTask().execute();



    }

    public void postToServer()
    {
        //Maybe make it as a seperate thread
        //For now, checking if the json is populated correctly

        if(!preferenceCreationNotSuccess) {
            String jsonData = userPrefJson.toString();
            Log.i("VirtualHome userJson", jsonData);
        }

        //Server code

        //on server Confirmation add in shared preferences maybe(?)


    }



    private class PreferenceAsynTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... arg0) {

            //check if the json is populated successfully.
            String jsonData = userPrefJson.toString();
            Log.i("VirtualHome userJson", jsonData);
            HttpURLConnection urlConnection = null;

            String url1 = "http://ec2-54-193-107-243.us-west-1.compute.amazonaws.com:8080/api/v5/userpreferences";

            StringBuilder sb = new StringBuilder();
            try {

                URL url = new URL(url1);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                //urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonData);
                out.flush();
                out.close();


                String responseMessage = urlConnection.getResponseMessage();
                int HttpResult = urlConnection.getResponseCode();
                System.out.println("CODE: " + HttpResult);


                //TODO: check for created or check for validated. If not throw pop up as error.
                System.out.println(responseMessage);

                //TODO: Check for re entry condition here
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {

                    Log.i("Login", "---Failed : HTTP error code : "
                            + urlConnection.getResponseCode());
                    preferenceCreationNotSuccess = true;

                    /*throw new RuntimeException("Failed : HTTP error code : "
                            + urlConnection.getResponseCode());*/
                } else {
                    //It is created:

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (urlConnection.getInputStream())));

                    String serverOutput;
                    StringBuilder serverSB = new StringBuilder();
                    System.out.println("Output from Server .... \n");
                    while ((serverOutput = br.readLine()) != null) {

                        System.out.println(serverOutput);
                        serverSB.append(serverOutput + "\n");

                    }

                    //Save to the editor the preferences:

                  /*  //Parsing the returned json
                    JSONObject jsonObject = null;
                    String userID = null;
                    try {
                        Log.i("New User Async ", "Retrieve jsonContent");

                        //System.out.println("inside : " + serverSB);

                        jsonObject = new JSONObject(serverSB.toString());
                        //Assuming only 1 jsonObject is returned.
                        if (jsonObject != null) {
                            userID = jsonObject.getString("user_id");

                            //Printing the whole json data obtained:
                            Log.i("VirtualHome", jsonObject.toString());


                            //Saving the userID in shared preferences:
                            //TODO: save username and password in shared Preferences.
                            Log.i("Login ", "User ID: " + userID);


                            //Save email id, password and user id.
                            SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, 0);

                            //Initially for cross checking
                            System.out.println(settings.getString("user_id", "0"));
                            System.out.println(settings.getString("email", "0"));
                            System.out.println(settings.getString("password", "0"));


                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("user_id", userID);
                            editor.putString("email", email);
                            editor.putString("password", pwd);



                            editor.commit();

                            //For confirmation
                            System.out.println(settings.getString("user_id", "0"));
                            System.out.println(settings.getString("email", "0"));
                            System.out.println(settings.getString("password", "0"));

                            //Save the preferences data
                            //Get the data and save the preferences.




                        }


                    } catch (JSONException ex) {
                        Log.i("VirtualHome-Gallery", " caught JSON exception");
                        ex.printStackTrace();
                        return null;
                    }
*/

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
            Log.i("VirtualHome-Preferences", "onPostExecute-Create User Preferences");

            //
            launchGallery();



/*
            //TODO: check the creation success and then navigate


            if(newUserFlag) {
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Preferences.class);
                intent.putExtra("newUserFlag","true");
                startActivity(intent);
            }*/


        }

    }

}

