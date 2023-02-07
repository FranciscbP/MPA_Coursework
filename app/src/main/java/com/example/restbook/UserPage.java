package com.example.restbook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserPage extends AppCompatActivity {

    LinearLayout restaurantsBtnLayout;
    LinearLayout reservationsBtnLayout;

    EditText emailTxt;
    EditText givenNameTxt;
    EditText surnameTxt;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    String givenName = "";
    String surname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_user_page);

        restaurantsBtnLayout = findViewById(R.id.restaurantBtnLayout);
        reservationsBtnLayout = findViewById(R.id.reservationsBtnLayout);

        restaurantsBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRestaurantsPage(v);
            }
        });

        reservationsBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goReservationPage(v);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailTxt = findViewById(R.id.emailText);
        emailTxt.setEnabled(false);
        emailTxt.setInputType(InputType.TYPE_NULL);

        givenNameTxt = findViewById(R.id.givenNameText);
        givenNameTxt.setEnabled(false);
        givenNameTxt.setInputType(InputType.TYPE_NULL);

        surnameTxt = findViewById(R.id.surnameText);
        surnameTxt.setEnabled(false);
        surnameTxt.setInputType(InputType.TYPE_NULL);

        user = mAuth.getCurrentUser();
        emailTxt.setText(user.getEmail());

        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        givenName = document.getString("GivenName");
                        surname = document.getString("Surname");

                        givenNameTxt.setText(givenName);
                        surnameTxt.setText(surname);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();

    }

    public void goRestaurantsPage(View view)
    {
        Intent goRestaurants = new Intent(this, Restaurants.class);
        startActivity(goRestaurants);
        overridePendingTransition(0, 0);
    }

    public void goReservationPage(View view)
    {
        Intent goReservations = new Intent(this, Reservations.class);
        startActivity(goReservations);
        overridePendingTransition(0, 0);
    }

    public void changePassword(View view)
    {

        String emailAddress = user.getEmail();

        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserPage.this, "Reset Password Email Sent!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signOut(View view)
    {
        mAuth.signOut();
        Intent goSignIn = new Intent(this, SignIn.class);
        startActivity(goSignIn);
    }
}