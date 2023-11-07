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
        TextInputLayout textInputLayoutUsername = findViewById(R.id.username_view);
        TextInputLayout textInputLayoutPassword = findViewById(R.id.password_view);
        textInputLayoutUsername.setErrorIconDrawable(0);
        textInputLayoutPassword.setErrorIconDrawable(0);


        // ONCLICK PULSANTE LOGIN
        findViewById(R.id.login_button).setOnClickListener(item ->
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