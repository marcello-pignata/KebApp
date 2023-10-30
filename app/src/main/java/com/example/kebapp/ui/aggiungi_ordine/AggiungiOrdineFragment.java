package com.example.kebapp.ui.aggiungi_ordine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kebapp.R;

import java.util.ArrayList;

public class AggiungiOrdineFragment extends Fragment
{
    private final String TAG = "AggiungiOrdineFragment";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_aggiungi_ordine, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //Inizializzo recyclerView
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewProdotti);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Inizializzo adapter
        ArrayList<String> prodotti = new ArrayList<>();
        RecyclerViewProdottiAdapter adapter = new RecyclerViewProdottiAdapter(getContext(), prodotti);

        getView().findViewById(R.id.buttonAggiungiProdotto).setOnClickListener(item ->
        {
            //Aggiungo elemento all'adapter
            adapter.addItem("Nuovo prodotto " + (adapter.getItemCount()+1));
            recyclerView.setAdapter(adapter);
        });
    }
}