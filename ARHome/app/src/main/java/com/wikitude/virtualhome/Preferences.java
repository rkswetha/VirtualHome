package com.wikitude.virtualhome;


/*import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Preferences extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
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
}*/


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Preferences extends Activity {


    Spinner spinner1,spinner2,spinner3;
    CheckBox checkbox1,checkbox2,checkbox3,checkbox4,checkbox5,checkbox6;

    String family = null;
    String gender = null;
    String profession = null;
    boolean chstate1,chstate2,chstate3,chstate4,chstate5,chstate6;
    JSONObject userPrefJson;
    String userEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //Obtaining the email id
        userEmail = getIntent().getStringExtra("email");


        // Spinner element

        spinner1 = (Spinner) findViewById(R.id.FamilyType);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.familytypelist, R.layout.spinner_style_preferences );
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);

        spinner2 = (Spinner) findViewById(R.id.SexType1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.sextypelist, R.layout.spinner_style_preferences );
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);


        spinner3 = (Spinner) findViewById(R.id.ProfessionType);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.professiontypelist, R.layout.spinner_style_preferences );
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter3);



        Button button = (Button) findViewById(R.id.button_submit_preferences);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                onSubmit();
            }
        });






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

        String result = family + " " + gender + " " + profession + " " + "1:" + chstate1 + "2:" + chstate2 + "3:" + chstate3 + "4:" + chstate4 + "5:" + chstate5 + "6:" + chstate6;


        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

        //Add code to insert data into preferences table in database.

        //Create preferences json
        createUserJson();


        //Launching the gallery after data insertion
        launchGallery();


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
            userPrefJson.put("email", userEmail);

            userPrefJson.put("gender", gender);
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
        postToServer();


    }

    public void postToServer()
    {
        //Maybe make it as a seperate thread
        //For now, checking if the json is populated correctly


        String jsonData = userPrefJson.toString();
        Log.i("VirtualHome userJson", jsonData);

        //Server code



        //on server Confirmation add in shared preferences maybe(?)


    }



}

