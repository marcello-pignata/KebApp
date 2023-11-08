package com.example.kebapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity
{
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextInputLayout textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        TextInputLayout textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUsername.setErrorIconDrawable(0);
        textInputLayoutPassword.setErrorIconDrawable(0);


        // ONCLICK PULSANTE LOGIN
        findViewById(R.id.buttonLogin).setOnClickListener(item ->
        {
            String username = textInputLayoutUsername.getEditText().getText().toString();
            String password = textInputLayoutPassword.getEditText().getText().toString();

            Log.d(TAG, "Login: {" + username + ", " + password + "}");

            /*
            textInputLayoutUsername.setError("username non valido");
            textInputLayoutPassword.setError("password errata");
            */
        });
    }
}