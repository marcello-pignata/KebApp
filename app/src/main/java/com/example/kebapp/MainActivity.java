package com.example.kebapp;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.kebapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";
    public boolean coldStart;
    public FirebaseAuth authenticator;
    public ArrayList<Utente> utente;

    public ArrayList<Utente> fattorini;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        coldStart = true;

        authenticator = FirebaseAuth.getInstance();
        FireStoreController database = new FireStoreController();
        utente = database.getUtente(authenticator.getCurrentUser().getUid(), authenticator.getCurrentUser().getEmail());
        fattorini = database.getFattorini();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
        if(!utente.get(0).fattorino)
        {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            navView.getMenu().clear();
            navView.inflateMenu(R.menu.bottom_nav_menu_no_consegne);
        }
        */

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}