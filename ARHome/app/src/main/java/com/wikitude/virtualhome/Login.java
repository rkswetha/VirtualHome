package com.wikitude.virtualhome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ActionBar;

import com.wikitude.tools.activities.MediaPlayerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class Login extends Activity {
    private String email;
    private String pwd;
    private boolean newUserFlag;
    JSONObject userDetailsJson;
    private boolean creationNotSuccess=false;
    private boolean LoginSuccess=false;


    public static final String PREFERENCES_Gallery_FILE_NAME = "VHGalleryPreferences";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.button_submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                openPreferences();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void openPreferences() {
        //Check the email error and password empty- If new user;
        //Check  if new user

        CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox_newuser);
        //boolean chstate=checkbox.isChecked();
        newUserFlag = checkbox.isChecked();
        EditText emailT = (EditText) findViewById(R.id.text_email);
        //String email = emailT.getText().toString();
        email = emailT.getText().toString();


        EditText pwdT = (EditText) findViewById(R.id.text_password);
        //String pwd = pwdT.getText().toString();
        pwd = pwdT.getText().toString();

        //Toast.makeText(getApplicationContext(), "Email: "+email +"Pass: " + pwd , Toast.LENGTH_SHORT).show();

        if (email.matches("")) {
            Toast.makeText(getApplicationContext(), "Email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.matches("")) {
            Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newUserFlag) {
            //Check email validity
            boolean emailVal = isValidEmailAddress(email);

            if (!emailVal) {
                Toast.makeText(getApplicationContext(), "Not a valid email id", Toast.LENGTH_SHORT).show();
                return;
            } else {
                createUserJson();
                //Include call to add the user.
            }

        } else {
            //Include call to DB to check if correct Password and email
            //Include call to DB to check if correct Password and email
            createUserJson();
        }

    /*    Intent intent = new Intent(this, Preferences.class);
        //For now.
        //TODO: Put this on postExecute???
        startActivity(intent);*/
    }


    public boolean isValidEmailAddress(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailPattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    public void createUserJson() {
        userDetailsJson = new JSONObject();
        try {
           /* userDetailsJson.put("email", email);
            userDetailsJson.put("password", pwd);
            userDetailsJson.put("newUserFlag", newUserFlag);*/

            Log.i("Login","create json user");

            //Using the below so as to match the format on server side: Needs to be changed
            userDetailsJson.put("name", "adad");
            userDetailsJson.put("email", email);
            userDetailsJson.put("password",pwd);


        } catch (JSONException e) {
            Log.i("VirtualHome-Login", "Exception in Json creation");
            e.printStackTrace();
        }

        //Post to the server

        //Depending on the flow.(whether new user or not)

        if (newUserFlag) {
            //Call the new user creation rest api
            new PostAsynTask().execute();
        }
        else
        {
            //Call the user validation rest api
        }




    }




    private class PostAsynTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... arg0) {
            HttpURLConnection urlConnection = null;

            //String url1= "http://ec2-54-219-182-125.us-west-1.compute.amazonaws.com:8080/api/v1/users";
              String url1= "http://ec2-54-193-107-243.us-west-1.compute.amazonaws.com:8080/api/v5/users";

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
                out.write(userDetailsJson.toString());
                out.flush();
                out.close();


                String responseMessage=urlConnection.getResponseMessage();
                int HttpResult =urlConnection.getResponseCode();
                System.out.println("CODE: "+HttpResult);


                //TODO: check for created or check for validated. If not throw pop up as error.
                System.out.println(responseMessage);

//TODO: Check for re entry condition here
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {

               Log.i("Login", "---Failed : HTTP error code : "
                            + urlConnection.getResponseCode());
                    creationNotSuccess=true;

                    /*throw new RuntimeException("Failed : HTTP error code : "
                            + urlConnection.getResponseCode());*/
                }
                else {
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

                    //Parsing the returned json
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
            Log.i("VirtualHome-Login", "onPostExecute-Create User");

            //TODO: check the creation success and then navigate
            if(newUserFlag) {
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Preferences.class);
                intent.putExtra("newUserFlag","true");
                startActivity(intent);
            }


        }
    }

    private class LoginAsynTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... arg0) {
            HttpURLConnection urlConnection = null;

            //String url1= "http://ec2-54-219-182-125.us-west-1.compute.amazonaws.com:8080/api/v1/users";
            String url1= "http://ec2-54-215-226-210.us-west-1.compute.amazonaws.com:8080/api/v4/login";

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
                out.write(userDetailsJson.toString());
                out.flush();
                out.close();


                String responseMessage=urlConnection.getResponseMessage();
                int HttpResult =urlConnection.getResponseCode();
                System.out.println("CODE: "+HttpResult);


                //TODO: check for created or check for validated. If not throw pop up as error.
                System.out.println(responseMessage);

                //TODO: this should be different status code
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {

                    Log.i("Login", "---Failed : HTTP error code : "
                            + urlConnection.getResponseCode());
                    LoginSuccess=false;

                    /*throw new RuntimeException("Failed : HTTP error code : "
                            + urlConnection.getResponseCode());*/


                }
                else {
                    //It is verified:
                    LoginSuccess=true;

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (urlConnection.getInputStream())));

                    String serverOutput;
                    StringBuilder serverSB = new StringBuilder();
                    System.out.println("Output from Server .... \n");
                    while ((serverOutput = br.readLine()) != null) {

                        System.out.println(serverOutput);
                        serverSB.append(serverOutput + "\n");

                    }

                    //Parsing the returned json
                    JSONObject jsonObject = null;
                    String userID = null;
                    String gender= null;
                    String family= null;
                    String profession= null;
                    String gardening= null;
                    String interiorDesign= null;
                    String cooking= null;
                    String painting= null;
                    String music= null;
                    String reading= null;


                    try {
                        Log.i("LoginAsync ", "Retrieve jsonContent");

                        //System.out.println("inside : " + serverSB);

                        jsonObject = new JSONObject(serverSB.toString());
                        //Assuming only 1 jsonObject is returned.
                        if (jsonObject != null) {
                            //Obtaining the data
                            userID = jsonObject.getString("user_id");
                            gender = jsonObject.getString("gender");
                            family = jsonObject.getString("family");
                            profession = jsonObject.getString("profession");
                            interiorDesign = jsonObject.getString("interiorDesign");
                            cooking = jsonObject.getString("cooking");
                            painting = jsonObject.getString("painting");
                            reading = jsonObject.getString("reading");
                            music = jsonObject.getString("music");


                            //Printing the whole json data obtained:
                            Log.i("VirtualHome", jsonObject.toString());

                            //Saving the userID in shared preferences:

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

                            //saving all the preferences
                            editor.putInt("gender", Integer.parseInt(gender)) ;
                            editor.putInt("family", Integer.parseInt(family));
                            editor.putInt("profession", Integer.parseInt(profession));
                            editor.putBoolean("gardening", Boolean.parseBoolean(gardening));
                            editor.putBoolean("interiorDesign", Boolean.parseBoolean(interiorDesign));
                            editor.putBoolean("cooking", Boolean.parseBoolean(cooking));
                            editor.putBoolean("painting", Boolean.parseBoolean(painting));
                            editor.putBoolean("reading", Boolean.parseBoolean(reading));
                            editor.putBoolean("music", Boolean.parseBoolean(music));



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
            Log.i("VirtualHome-Login", "onPostExecute");


            if(!LoginSuccess)
            {
                Toast.makeText(getApplicationContext(), "Invalid credentials!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //If returning user, navigating the to the gallery
                Intent intent = new Intent(Login.this, SofaGallery.class);
                startActivity(intent);
            }

        }
    }





}

