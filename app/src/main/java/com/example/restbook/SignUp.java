package com.example.restbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    EditText emailTxt;
    EditText passwordTxt;
    EditText confirmPasswordTxt;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.emailText);
        passwordTxt = findViewById(R.id.passwordText);
        confirmPasswordTxt = findViewById(R.id.confirmPasswordText);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void signUpUser(View view)
    {
        String email = emailTxt.getText().toString();
        String passwd = passwordTxt.getText().toString();
        String confirmPasswd = confirmPasswordTxt.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            emailTxt.setError("Email Cannot be Empty!");
            emailTxt.requestFocus();
        }
        else if(TextUtils.isEmpty(passwd))
        {
            passwordTxt.setError("Password Cannot be Empty!");
            passwordTxt.requestFocus();
        }
        else if(TextUtils.isEmpty(confirmPasswd))
        {
            confirmPasswordTxt.setError("Password Confirmation Cannot be Empty!");
            confirmPasswordTxt.requestFocus();
        }
        else if(!isEmailValid(email))
        {
            emailTxt.setError("Email Is not Valid!");
            emailTxt.requestFocus();
        }
        else if(passwd.length() < 6)
        {
            passwordTxt.setError("Please Insert a stronger Password!");
            passwordTxt.requestFocus();
        }
        else
        {
            if(confirmPasswd.equals(passwd))
            {
                mAuth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "User Created!", Toast.LENGTH_SHORT).show();

                            Intent goWelcome = new Intent(SignUp.this, WelcomePage.class);
                            startActivity(goWelcome);
                        }
                        else
                        {
                            Toast.makeText(SignUp.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                confirmPasswordTxt.setError("Passwords Do Not Match!");
                confirmPasswordTxt.requestFocus();
            }
        }
    }

    public void goToSignInPage(View view)
    {
        Intent goSignIn = new Intent(this, SignIn.class);
        startActivity(goSignIn);
    }
}