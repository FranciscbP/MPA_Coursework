package com.example.restbook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Restaurants extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    TextView rest1;
    TextView rest2;
    TextView rest3;

    LinearLayout rest1Btn;
    LinearLayout rest2Btn;
    LinearLayout rest3Btn;

    LinearLayout reservationBtnLayout;
    LinearLayout userBtnLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_restaurants);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        reservationBtnLayout = findViewById(R.id.reservationsBtnLayout);
        userBtnLayout = findViewById(R.id.userBtnLayout);

        rest1 = findViewById(R.id.restaurant1Text);
        rest2 = findViewById(R.id.restaurant2Text);
        rest3 = findViewById(R.id.restaurant3Text);

        rest1Btn = findViewById(R.id.restaurant1);
        rest2Btn = findViewById(R.id.restaurant2);
        rest3Btn = findViewById(R.id.restaurant3);

        reservationBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goReservationsPage(v);
            }
        });

        userBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUserPage(v);
            }
        });

        String[] restaurantNames = {"1","2","3"};

        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            Integer counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurantNames[counter] = document.getString("Name");;
                                counter++;
                            }

                            rest1.setText(restaurantNames[0]);
                            rest2.setText(restaurantNames[1]);
                            rest3.setText(restaurantNames[2]);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        rest1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRestaurant1Info(v);
            }
        });

        rest2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRestaurant2Info(v);
            }
        });

        rest3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRestaurant3Info(v);
            }
        });
    }

    public void goRestaurant1Info(View View)
    {
        Intent goRestaurantInfo = new Intent(this, RestaurantInfo.class);
        goRestaurantInfo.putExtra("restaurantId", "1");
        startActivity(goRestaurantInfo);
        overridePendingTransition(0, 0);
    }

    public void goRestaurant2Info(View View)
    {
        Intent goRestaurantInfo = new Intent(this, RestaurantInfo.class);
        goRestaurantInfo.putExtra("restaurantId", "2");
        startActivity(goRestaurantInfo);
        overridePendingTransition(0, 0);
    }

    public void goRestaurant3Info(View View)
    {
        Intent goRestaurantInfo = new Intent(this, RestaurantInfo.class);
        goRestaurantInfo.putExtra("restaurantId", "3");
        startActivity(goRestaurantInfo);
        overridePendingTransition(0, 0);
    }

    public void goReservationsPage(View view)
    {
        Intent goReservations = new Intent(this, Reservations.class);
        startActivity(goReservations);
        overridePendingTransition(0, 0);
    }

    public void goUserPage(View view)
    {
        Intent goUser = new Intent(this, UserPage.class);
        startActivity(goUser);
        overridePendingTransition(0, 0);
    }

}