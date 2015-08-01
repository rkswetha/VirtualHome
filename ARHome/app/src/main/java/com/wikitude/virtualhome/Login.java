package com.wikitude.virtualhome;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/*
public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
}
*/


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

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends Activity {
    private String email;
    private String pwd;
    private boolean newUserFlag;
    JSONObject userDetailsJson;


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

    public void openPreferences()
    {
        //Check the email error and password empty- If new user;
        //Check  if new user

        CheckBox checkbox= (CheckBox) findViewById(R.id.checkbox_newuser);
        //boolean chstate=checkbox.isChecked();
        newUserFlag=checkbox.isChecked();
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

        if(newUserFlag) {
            //Check email validity
            boolean emailVal = isValidEmailAddress(email);

            if (!emailVal) {
                Toast.makeText(getApplicationContext(), "Not a valid email id", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                createUserJson();
                //Include call to add the user.
            }

        }

        else
        {
            //Include call to DB to check if correct Password and email
            //Include call to DB to check if correct Password and email
            createUserJson();
        }

        Intent intent = new Intent(this, Preferences.class);
        //For now.
        intent.putExtra("email",email);
        startActivity(intent);
    }


    public boolean isValidEmailAddress(String email)
    {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailPattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }



    public void createUserJson()
    {
        userDetailsJson=new JSONObject();
        try {
            userDetailsJson.put("email", email);
            userDetailsJson.put("password", pwd);
            userDetailsJson.put("newUserFlag", newUserFlag);
        }
        catch(JSONException e) {
            Log.i("VirtualHome-Login","Exception in Json creation");
            e.printStackTrace();
        }

        //Post to the server
        postToServer();


    }

    public void postToServer()
    {
        //Maybe make it as a seperate thread
        //For now, checking if the json is populated correctly


        String jsonData = userDetailsJson.toString();
        Log.i("VirtualHome userJson",jsonData);

        //Server code



        //on server Confirmation add in shared preferences


    }

}
