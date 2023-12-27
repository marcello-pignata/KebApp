package com.example.kebapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity
{
    private final String TAG = "LoginActivity";
    FirebaseAuth authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextInputLayout textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        TextInputLayout textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutEmail.setErrorIconDrawable(0);
        textInputLayoutPassword.setErrorIconDrawable(0);

        authenticator = FirebaseAuth.getInstance();

        if(authenticator.getCurrentUser() != null)
        {
            try
            {
                launchMainActivity();
            }
            catch (Exception e){throw new RuntimeException(e);}
        }


        // ONCLICK PULSANTE LOGIN
        findViewById(R.id.buttonLogin).setOnClickListener(item ->
        {
            String email = textInputLayoutEmail.getEditText().getText().toString();
            String password = textInputLayoutPassword.getEditText().getText().toString();

            if(isEmailValid(email))
            {
                if(isPasswordValid(password))
                {
                    authenticator.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    try
                                    {
                                        launchMainActivity();
                                    }
                                    catch (Exception e){throw new RuntimeException(e);}
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
                else
                {
                    //TODO errors not showing
                    textInputLayoutPassword.setError("Inserisci almeno 6 caratteri");
                }
            }
            else
            {
                textInputLayoutEmail.setError("Email non valida");
            }

        });
    }

    synchronized public void launchMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public boolean isEmailValid(String email)
    {
        return email.length() >= 5;
    }

    public boolean isPasswordValid(String password)
    {
        return password.length() >= 6;
    }
}