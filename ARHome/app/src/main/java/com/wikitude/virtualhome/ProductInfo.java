package com.wikitude.virtualhome;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Log;

public class ProductInfo extends Activity {

    TextView productName_TV,dimension_TV,price_TV, description_TV;
    String productName, dimension, price, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        productName_TV = (TextView)findViewById(R.id.productNameEditText);
        dimension_TV = (TextView)findViewById(R.id.dimensionEditText);
        price_TV = (TextView)findViewById(R.id.priceEditText);
        description_TV = (TextView)findViewById(R.id.descriptionEditText);
    }

    @Override
    protected void onStart() {

        super.onStart();

        Log.i("ProductInfo-", "OnStart called");

        productName = getIntent().getStringExtra("ProductName");
        dimension = getIntent().getStringExtra("Dimension");
        price = getIntent().getStringExtra("Price");
        description = getIntent().getStringExtra("Description");

        setTitle(productName);
        productName_TV.setText(productName);
        dimension_TV.setText(dimension);
        price_TV.setText(price);
        description_TV.setText(description);

    }

}
