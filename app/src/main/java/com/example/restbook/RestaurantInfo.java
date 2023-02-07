package com.example.restbook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RestaurantInfo extends AppCompatActivity {

    String restName;
    String restDetails;
    String restRating;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    TextView name;
    TextView rating;
    TextView details;

    LinearLayout restaurantsBtnLayout;
    LinearLayout reservationBtnLayout;
    LinearLayout userBtnLayout;

    EditText dateTxt;
    EditText timeTxt;
    EditText guestsTxt;
    Dialog dialog;

    Bundle b;
    String restId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_restaurant_info);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.restaurantNameTxt);
        rating = findViewById(R.id.ratingTxt);
        details = findViewById(R.id.detailsTxt);

        restaurantsBtnLayout = findViewById(R.id.restaurantBtnLayout);
        reservationBtnLayout = findViewById(R.id.reservationsBtnLayout);
        userBtnLayout = findViewById(R.id.userBtnLayout);


        restaurantsBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRestaurantsPage(v);
            }
        });

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


    }

    //Function to display the custom dialog.
   public void showCustomDialog(View view) {
        dialog = new Dialog(this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.custom_modal);

        //Initializing the views of the dialog.
        dateTxt = dialog.findViewById(R.id.editTextDate);
        guestsTxt = dialog.findViewById(R.id.guests);
        timeTxt = dialog.findViewById(R.id.editTextTime);

        dialog.show();
    }

    public void closeDialog(View view)
    {
        dialog.dismiss();
    }

    public static boolean validateJavaDate(String strDate)
    {
        if (strDate.trim().equals(""))
        {
            return true;
        }
        else
        {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("MM/dd/yyyy");
            sdfrmt.setLenient(false);
            try
            {
                Date javaDate = sdfrmt.parse(strDate);
            }
            catch (ParseException e)
            {
                return false;
            }
            return true;
        }
    }

   public void makeReservation(View view)
    {
        String date = dateTxt.getText().toString();
        String time = timeTxt.getText().toString();
        String guests = guestsTxt.getText().toString();

        if (TextUtils.isEmpty(date))
        {
            dateTxt.setError("Date Cannot be Empty!");
            dateTxt.requestFocus();
        }
        else if (TextUtils.isEmpty(time))
        {
            timeTxt.setError("Time Cannot be Empty!");
            timeTxt.requestFocus();
        }
        else if (TextUtils.isEmpty(guests))
        {
            guestsTxt.setError("Number of Guests Cannot be Empty!");
            guestsTxt.requestFocus();
        }
        else if(validateJavaDate(date) == false)
        {
            dateTxt.setError("Date is Invalid!");
            dateTxt.requestFocus();
        }
        else
        {
            addReservation(date,time,guests);
            dialog.dismiss();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        user = mAuth.getCurrentUser();

        b = getIntent().getExtras();
        restId = b.getString("restaurantId");

        DocumentReference docRef = db.collection("Restaurants").document(restId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        restName = document.getString("Name");
                        restDetails = document.getString("Details");
                        restRating = document.getString("Rate");

                        name.setText(restName);
                        rating.setText(restRating + "/5");
                        details.setText(restDetails);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void addReservation(String date, String time, String guests)
    {
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("Guests", guests);
        data.put("Date", date);
        data.put("Time", time);
        data.put("User", user.getUid());
        data.put("Restaurant", restId);

        db.collection("Reservations")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RestaurantInfo.this, "Reservation Successfully Made!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }

    public void goRestaurantsPage(View view)
    {
        Intent goRestaurants = new Intent(this, Restaurants.class);
        startActivity(goRestaurants);
        overridePendingTransition(0, 0);
        finish();
    }

    public void goReservationsPage(View view)
    {
        Intent goReservations = new Intent(this, Reservations.class);
        startActivity(goReservations);
        overridePendingTransition(0, 0);
        finish();
    }

    public void goUserPage(View view)
    {
        Intent goUser = new Intent(this, UserPage.class);
        startActivity(goUser);
        overridePendingTransition(0, 0);
        finish();
    }
}