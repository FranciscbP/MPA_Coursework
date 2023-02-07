package com.example.restbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Reservations extends AppCompatActivity {

    LinearLayout restaurantsBtnLayout;
    LinearLayout userBtnLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_reservations);

        restaurantsBtnLayout = findViewById(R.id.restaurantBtnLayout);
        userBtnLayout = findViewById(R.id.userBtnLayout);

        restaurantsBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRestaurantsPage(v);
            }
        });

        userBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUserPage(v);
            }
        });
    }

    public void goRestaurantsPage(View view)
    {
        Intent goRestaurants = new Intent(this, Restaurants.class);
        startActivity(goRestaurants);
        overridePendingTransition(0, 0);
    }

    public void goUserPage(View view)
    {
        Intent goUser = new Intent(this, UserPage.class);
        startActivity(goUser);
        overridePendingTransition(0, 0);
    }
}