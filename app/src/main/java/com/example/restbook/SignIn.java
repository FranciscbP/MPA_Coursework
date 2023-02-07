package com.example.restbook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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

public class SignIn extends AppCompatActivity {

    EditText emailTxt;
    EditText passwordTxt;

    FirebaseAuth mAuth;

    String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.emailText);
        passwordTxt = findViewById(R.id.passwordText);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void signInUser(View view)
    {
        String email = emailTxt.getText().toString();
        String passwd = passwordTxt.getText().toString();

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
        else if(!isEmailValid(email))
        {
            emailTxt.setError("Email Is not Valid!");
            emailTxt.requestFocus();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        Toast.makeText(SignIn.this, "User Sign In!", Toast.LENGTH_SHORT).show();

                        Intent goSignUp = new Intent(SignIn.this, MainActivity.class);
                        startActivity(goSignUp);
                    }
                    else
                    {
                        Toast.makeText(SignIn.this, "User Not Sign In!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void forgotPass(View view)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Insert your Email");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();

                if(isEmailValid(m_Text))
                {
                    mAuth.sendPasswordResetEmail(m_Text)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignIn.this, "Reset Password Email Sent!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SignIn.this, "Please Insert a Valid Email!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void goToSignUpPage(View view)
    {
        Intent goSignUp = new Intent(this, SignUp.class);
        startActivity(goSignUp);
    }
}