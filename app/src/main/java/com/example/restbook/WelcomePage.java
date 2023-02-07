package com.example.restbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WelcomePage extends AppCompatActivity {

    EditText emailTxt;
    EditText givenNameTxt;
    EditText surnameTxt;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_welcome_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailTxt = findViewById(R.id.emailText);
        emailTxt.setEnabled(false);
        emailTxt.setInputType(InputType.TYPE_NULL);

        givenNameTxt = findViewById(R.id.givenNameText);
        surnameTxt = findViewById(R.id.surnameText);
    }

    @Override
    protected void onStart() {
        super.onStart();

        user = mAuth.getCurrentUser();
        emailTxt.setText(user.getEmail());
    }

    public void saveUserName(View view)
    {
        user = mAuth.getCurrentUser();
        String userId = user.getUid();
        String userGivenName = givenNameTxt.getText().toString();
        String userSurname = surnameTxt.getText().toString();

        if (TextUtils.isEmpty(userGivenName))
        {
            givenNameTxt.setError("Given Name Cannot be Empty!");
            givenNameTxt.requestFocus();
        }
        else if (TextUtils.isEmpty(userSurname))
        {
            surnameTxt.setError("Surname Cannot be Empty!");
            surnameTxt.requestFocus();
        }
        else
        {
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("GivenName", userGivenName);
            userDetails.put("Surname", userSurname);

            db.collection("Users").document(userId)
                    .set(userDetails)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(WelcomePage.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent goWelcome = new Intent(WelcomePage.this, Restaurants.class);
                            startActivity(goWelcome);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(WelcomePage.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}