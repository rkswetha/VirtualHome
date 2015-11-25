package com.wikitude.virtualhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Login extends Activity {
    private String email;
    private String pwd;
    private boolean newUserFlag;
    JSONObject userDetailsJson;
    private boolean creationNotSuccess=false;
    private boolean networkError=false;
    private boolean LoginSuccess=false;
	String ConstantURL = URLAPIConstant.URL;


    public static final String PREFERENCES_Gallery_FILE_NAME = "VHGalleryPreferences";
    public static final String VIRTUALHOME_FB_LOGIN = "VirtualHomeFBLogin";

    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        handleOnClickSubmitButton();

        handleLoginFromFacebook();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void handleOnClickSubmitButton() {

        Button button = (Button) findViewById(R.id.button_submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                openPreferences();
            }
        });
    }

    public void handleLoginFromFacebook() {

        Log.d("VirtualHome-Login", "handleLoginFromFacebook called");

        // Generate key hash for FB app
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.wikitude.virtualhome",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //Place holder for saving FB latest login
        SharedPreferences.Editor editor = getSharedPreferences(VIRTUALHOME_FB_LOGIN, MODE_PRIVATE).edit();
        editor.putString("email", "test@test.com");
        editor.commit();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("VirtualHome-Login", "Facebook login onSuccess");

                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                // Get the email that the FB user has logged in with.
                                String fbEmail = object.optString("email");
                                Log.v("FB Email logged in: ", fbEmail);

                                SharedPreferences prefs = getSharedPreferences(VIRTUALHOME_FB_LOGIN, MODE_PRIVATE);
                                String prefEmail = prefs.getString("email", "0");
                                Log.v("Shared pref Email: ", prefEmail);

                                // Check if its returning FB user
                                if(prefEmail.equals(fbEmail))
                                {
                                    Log.v("FB Login", "Returning user");
                                    // Assign the login values needed for creating User JSON
                                    email = fbEmail;
                                    pwd = "testFbPassword";
                                    newUserFlag = false;

                                    createUserJson();
                                }
                                else // Check for NEW FB user
                                {
                                    Log.v("FB Login", "New user");

                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("email", fbEmail);
                                    editor.commit();
                                    // Assign the login values needed for creating User JSON
                                    email = fbEmail;
                                    pwd = "testFbPassword";
                                    newUserFlag = true;

                                    createUserJson();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.i("VirtualHome-Login", "Facebook login cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("VirtualHome-Login", "Facebook login Error");
            }
        });

    }

    public void openPreferences() {
        //Check the email error and password empty- If new user;
        //Check  if new user

        CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox_newuser);
        //boolean chstate=checkbox.isChecked();
        newUserFlag = checkbox.isChecked();
        Log.i("VirtualHome-Login","new user flag: "+newUserFlag);
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
            createUserJson();
        }

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

            Log.i("VirtualHome-Login","create json user");

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
            new LoginAsynTask().execute();
        }

    }



    //create a new USER
    private class PostAsynTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... arg0) {
            HttpURLConnection urlConnection = null;

            String url1= ConstantURL+"users";

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
                    creationNotSuccess=false;
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

                networkError =true;
                e.printStackTrace();

            } catch (IOException e) {

                networkError=true;
                e.printStackTrace();
            } finally {

                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return "";

        }




        protected void onPostExecute(String result) {
            Log.i("VirtualHome-Login", "onPostExecute-Create User");

            if(networkError)
            {
                Toast.makeText(getApplicationContext(), "Network Error: New User registration failed!!", Toast.LENGTH_SHORT).show();
                networkError = false;
            }
            else if(creationNotSuccess)
            {
                Toast.makeText(getApplicationContext(), "Email exist: New User registration failed!!", Toast.LENGTH_SHORT).show();
                creationNotSuccess = false;
            }
            else if(newUserFlag ) {   //ODO: check the creation success and then navigate
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Preferences.class);
                intent.putExtra("newUserFlag","true");
                startActivity(intent);
            }


        }
    }

//Check Login credentials for a returning user.

    private class LoginAsynTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... arg0) {
            HttpURLConnection urlConnection = null;
            Log.i("Login","inside returning user aync");

            String url1= ConstantURL+"login";

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
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK ) {

                    Log.i("Login", "---Failed : HTTP error code : "
                            + urlConnection.getResponseCode());
                    LoginSuccess=false;

                    /*throw new RuntimeException("Failed : HTTP error code : "
                            + urlConnection.getResponseCode());*/
                }
                else {
                    //It is verified:
                    LoginSuccess = true;

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (urlConnection.getInputStream())));

                    String serverOutput;
                    StringBuilder serverSB = new StringBuilder();
                    System.out.println("Output from Server .... \n");

                    //To ensure the body is not zero
                    int count = 0;
                    while ((serverOutput = br.readLine()) != null) {
                        count++;
                        System.out.println(serverOutput);
                        serverSB.append(serverOutput + "\n");

                    }

                    if (count == 0) {
                        LoginSuccess = false;

                    }
                    else
                    {
                        //Parsing the returned json
                        JSONObject jsonObject = null;
                        String userID = null;
                        String gender = null;
                        String family = null;
                        String profession = null;
                        String gardening = null;
                        String interiorDesign = null;
                        String cooking = null;
                        String painting = null;
                        String music = null;
                        String reading = null;


                        try {
                            Log.i("LoginAsync ", "Retrieve jsonContent");

                            //System.out.println("inside : " + serverSB);

                            jsonObject = new JSONObject(serverSB.toString());
                            //Assuming only 1 jsonObject is returned.
                            if (jsonObject != null) {
                                //Obtaining the data
                                userID = jsonObject.getString("user_id");
                                gender = jsonObject.getString("sex");
                                family = jsonObject.getString("family");
                                profession = jsonObject.getString("profession");
                                interiorDesign = jsonObject.getString("interiorDesign");
                                cooking = jsonObject.getString("cooking");
                                painting = jsonObject.getString("painting");
                                reading = jsonObject.getString("reading");
                                music = jsonObject.getString("music");
                                gardening = jsonObject.getString("gardening");

                                Log.i("VirtualHome- individual", gender + " " + family + " " + profession + " " + interiorDesign + " " + cooking + " " + painting + " " + reading + " " + music);


                                //Printing the whole json data obtained:
                                Log.i("VirtualHome", jsonObject.toString());

                                //Saving the userID in shared preferences:
                                Log.i("Login ", "User ID: " + userID);

                                //Save email id, password and user id.
                                SharedPreferences settings = getSharedPreferences(PREFERENCES_Gallery_FILE_NAME, 0);

                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("user_id", userID);
                                editor.putString("email", email);
                                editor.putString("password", pwd);

                                //saving all the preferences
                                editor.putString("gender", gender);
                                editor.putString("family", family);
                                editor.putString("profession", profession);
                                editor.putBoolean("gardening", Boolean.parseBoolean(gardening));
                                editor.putBoolean("interiorDesign", Boolean.parseBoolean(interiorDesign));
                                editor.putBoolean("cooking", Boolean.parseBoolean(cooking));
                                editor.putBoolean("painting", Boolean.parseBoolean(painting));
                                editor.putBoolean("reading", Boolean.parseBoolean(reading));
                                editor.putBoolean("music", Boolean.parseBoolean(music));
                                editor.commit();

                       /*     //For confirmation
                            System.out.println("=========================================");
                            System.out.println(settings.getString("user_id", "0"));
                            System.out.println(settings.getString("email", "0"));
                            System.out.println(settings.getString("password", "0"));

                            System.out.println(settings.getInt("gender", -1));
                            System.out.println(settings.getInt("family", -1));
                            System.out.println(settings.getInt("profession", -1));


                            System.out.println(settings.getBoolean("gardening", false));
                            System.out.println(settings.getBoolean("interiorDesign", false));
                            System.out.println(settings.getBoolean("cooking", false));
                            System.out.println(settings.getBoolean("painting", false));
                            System.out.println(settings.getBoolean("reading", false));
                            System.out.println(settings.getBoolean("music", false));
*/
                            }


                        } catch (JSONException ex) {
                            Log.i("VirtualHome-Gallery", " caught JSON exception");
                            ex.printStackTrace();
                            return null;
                        }

                    }

                }

            } catch (MalformedURLException e) {
                networkError =true;
                e.printStackTrace();
            } catch (IOException e) {
                networkError =true;
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return "";

        }


        protected void onPostExecute(String result) {
            Log.i("VirtualHome-Login", "onPostExecute");

            if(networkError)
            {
                Toast.makeText(getApplicationContext(), "Network Error: User login failed!!", Toast.LENGTH_SHORT).show();
                networkError = false;
            }
            else if(!LoginSuccess)
            {
                Toast.makeText(getApplicationContext(), "Login failed: Invalid credentials!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //If returning user, navigating the to the gallery
                Intent intent = new Intent(Login.this, ProductGalleryTabPage.class);
                //intent.putExtra("newUserFlag","false");
                startActivity(intent);
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.(Needed for Facebook APIS)
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.(Needed for Facebook APIS)
        AppEventsLogger.deactivateApp(this);
    }

}
