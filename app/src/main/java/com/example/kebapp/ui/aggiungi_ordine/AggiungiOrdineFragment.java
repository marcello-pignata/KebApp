package com.example.kebapp.ui.aggiungi_ordine;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kebapp.Prodotto;
import com.example.kebapp.R;
import com.example.kebapp.SelezionaProdottoActivity;

import java.util.ArrayList;

public class AggiungiOrdineFragment extends Fragment
{
    private final String TAG = "AggiungiOrdineFragment";

    ArrayList<Prodotto> listaProdotti = new ArrayList<>();

    ArrayList<Prodotto> prodotti = new ArrayList<>();

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

        listaProdotti.add(new Prodotto("Marinara", 4., 0, "Pomodoro"));
        listaProdotti.add(new Prodotto("Margherita", 5., 0, "Pomodoro, mozzarella"));
        listaProdotti.add(new Prodotto("Bufala", 6.5, 0, "Pomodoro, mozzarella di bufala"));
        listaProdotti.add(new Prodotto("Prosciutto e funghi", 7., 0, "Pomodoro, mozzarella, prosciutto cotto, funghi"));
        listaProdotti.add(new Prodotto("Americana", 7.5, 0, "Pomodoro, mozzarella, wurstel, patatine"));


        getView().findViewById(R.id.buttonAggiungiProdotto).setOnClickListener(item ->
        {
            int REQUEST_CODE = 1;
            Intent intent = new Intent(getActivity(), SelezionaProdottoActivity.class);
            intent.putExtra("listaProdotti", listaProdotti);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewProdotti);
        RecyclerViewProdottiAdapter adapter = new RecyclerViewProdottiAdapter(getContext(), prodotti);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK)
            {
                Prodotto result = (Prodotto) data.getSerializableExtra("result");
                adapter.addItem(result);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}