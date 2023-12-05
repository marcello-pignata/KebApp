package com.example.kebapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FireStoreHandler {
    private final String TAG = "FireStoreHandler";
    private FirebaseFirestore database;

    public FireStoreHandler() {
        database = FirebaseFirestore.getInstance();
    }

    public ArrayList<Prodotto> getProdotti()
    {
        ArrayList<Prodotto> result = new ArrayList<>();

        database.collection("prodotti").get().addOnCompleteListener(
            new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    Map<String, Object> data;

                    String nome, descrizione;
                    double prezzo;

                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            data = document.getData();

                            nome = (String)data.get("nome");
                            descrizione = (String)data.get("descrizione");

                            try
                            {
                                prezzo = new Double((long)data.get("prezzo"));
                            }
                            catch (Exception e)
                            {
                                prezzo = (double)data.get("prezzo");
                            }


                            result.add(new Prodotto(nome,prezzo,0,descrizione));
                        }
                    }
                    else
                    {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });

        return result;
    }

    public ArrayList<Ingrediente> getIngredienti()
    {
        ArrayList<Ingrediente> result = new ArrayList<>();

        database.collection("ingredienti").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        Map<String, Object> data;

                        String nome;
                        double prezzo;

                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                data = document.getData();

                                nome = (String)data.get("nome");

                                try
                                {
                                    prezzo = new Double((long)data.get("prezzo"));
                                }
                                catch (Exception e)
                                {
                                    prezzo = (double)data.get("prezzo");
                                }


                                result.add(new Ingrediente(nome,prezzo));
                            }
                        }
                        else
                        {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return result;
    }
}
