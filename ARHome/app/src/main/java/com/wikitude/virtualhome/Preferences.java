package com.wikitude.virtualhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
    String ConstantURL = URLAPIConstant.URL;

    boolean networkError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getting no userID status:
        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();


        userID = settings.getString("user_id", "-1");
        emailID=settings.getString("email", "-1");

        Log.i("Pref", "UserIDStatus: " + userID);
        Log.i("Preference","newUserFlag before getString: "+ newUserFlag);
        //Obtaining the new user flag status flg status
        newUserFlag = getIntent().getStringExtra("newUserFlag");
        Log.i("Preference","newUserFlag after getString: "+ newUserFlag);

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

        /*
        In this new method, with the delinking of the login and preferences- there is no http call from server, while loading this page.
        As ideally, during the login, the preferences would have been sent from the server and updated in the local(shared preferences)
         */
        if (newUserFlag != null) {
            if (newUserFlag.equals("true")) {
                //Log.i("new user FLAG",newUserFlag);

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

                //Log.i("pref:", "inside new user");

                String gender = settings.getString("gender", null);
                String family = settings.getString("family", null);
                String profession = settings.getString("profession", null);
                boolean gardening = settings.getBoolean("gardening", false);
                boolean interiorDesign = settings.getBoolean("interiorDesign", false);
                boolean cooking = settings.getBoolean("cooking", false);
                boolean painting = settings.getBoolean("painting", false);
                boolean reading = settings.getBoolean("reading", false);
                boolean music = settings.getBoolean("music", false);

                Log.i("pref:", "inside returning user");

                // Log.i("pref:", "gender"+gender+" family"+family+"profession"+profession);


                if (gender != null && family != null && profession != null) {
                    // Spinner element
                    spinner1 = (Spinner) findViewById(R.id.FamilyType);
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                            R.array.familytypelist, R.layout.spinner_style_preferences);
                    // Specify the layout to use when the list of choices appears
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner1.setAdapter(adapter1);
                    if (!family.equals(null)) {
                        int spinnerPosition = adapter1.getPosition(family);
                        spinner1.setSelection(spinnerPosition);
                    }


                    spinner2 = (Spinner) findViewById(R.id.SexType1);
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                            R.array.sextypelist, R.layout.spinner_style_preferences);
                    // Specify the layout to use when the list of choices appears
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner2.setAdapter(adapter2);
                    if (!gender.equals(null)) {
                        int spinnerPosition = adapter2.getPosition(gender);
                        spinner2.setSelection(spinnerPosition);
                    }


                    spinner3 = (Spinner) findViewById(R.id.ProfessionType);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                            R.array.professiontypelist, R.layout.spinner_style_preferences);
                    // Specify the layout to use when the list of choices appears
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner3.setAdapter(adapter3);
                    if (!profession.equals(null)) {
                        int spinnerPosition = adapter3.getPosition(profession);
                        spinner3.setSelection(spinnerPosition);
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
       }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_preferences, menu);
            return true;
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

        String result = family + " " + gender + " " + profession + " " + "1:" + chstate1 + "2:" + chstate2 + "3:" + chstate3 + "4:" + chstate4 + "5:" + chstate5 + "6:" + chstate6 ;
        Log.i("pref", result);

        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


        //Create preferences json and call the server.
        createUserJson();

        //Save the features to local
        SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();


        editor.putString("gender", gender);
        editor.putString("family", family);
        editor.putString("profession", profession);
        editor.putBoolean("gardening", chstate1);
        editor.putBoolean("interiorDesign", chstate2);
        editor.putBoolean("cooking", chstate3);
        editor.putBoolean("painting", chstate4);
        editor.putBoolean("reading", chstate5);
        editor.putBoolean("music", chstate6);
        editor.commit();

    }


    public void launchGallery()
    {
        Log.d("VirtualHome1", "Clicked gallery");
        Intent intent = new Intent(this, ProductGalleryTabPage.class);
        startActivity(intent);
    }



    public void createUserJson()
    {
        userPrefJson=new JSONObject();
        try {

            userPrefJson.put("user_id",Integer.parseInt(userID));
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

        new PreferenceAsynTask().execute();

    }



    private class PreferenceAsynTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... arg0) {

            //check if the json is populated successfully.
            String jsonData = userPrefJson.toString();
            Log.i("VirtualHome userJson", jsonData);
            HttpURLConnection urlConnection = null;
            String url1=null;
            int expectedResponseCode=0;

            if(newUserFlag==null || newUserFlag.equals("false") )
            {
                url1 = ConstantURL+"userpreferences"+"/"+userID;
                expectedResponseCode=HttpURLConnection.HTTP_OK;
            }
            else  if(newUserFlag.equals("true")) {
                url1 = ConstantURL+"userpreferences";
                expectedResponseCode=HttpURLConnection.HTTP_CREATED;
            }
            //String url1 = "http://192.168.0.14:8080/api/v5/userpreferences";
            Log.i("Preference","URL: "+url1);
            StringBuilder sb = new StringBuilder();
            try {

                URL url = new URL(url1);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                //urlConnection.setRequestMethod("POST");
                System.out.println("New User Flag before put/post: " + newUserFlag);
                //21.10.2015:
                if(newUserFlag==null || newUserFlag.equals("false")) {
                    urlConnection.setRequestMethod("PUT");
                }
                else  if(newUserFlag.equals("true"))
                {
                    urlConnection.setRequestMethod("POST");
                    newUserFlag = "false";
                }


                System.out.println("put/post: " +  urlConnection.getRequestMethod().toString());
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
                System.out.println("CODE:Expected " + expectedResponseCode);
                System.out.println("CODE: " + HttpResult);


                //TODO: check for created or check for validated. If not throw pop up as error.
                System.out.println(responseMessage);

                //TODO: Check for re entry condition here
                if (HttpResult != expectedResponseCode) {

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


                }

            } catch (MalformedURLException e) {

                e.printStackTrace();
                networkError =true;
            } catch (IOException e) {

                e.printStackTrace();
                networkError =true;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return "";

        }


        protected void onPostExecute(String result) {
            Log.i("VirtualHome-Preferences", "onPostExecute-Create User Preferences");

            if(networkError) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                networkError = false;
            }

            if(preferenceCreationNotSuccess){

                Toast.makeText(getApplicationContext(), "Preferences was not set properly!", Toast.LENGTH_SHORT).show();
            }

            launchGallery();


        }

    }
}
