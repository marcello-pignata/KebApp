package com.example.kebapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }
}