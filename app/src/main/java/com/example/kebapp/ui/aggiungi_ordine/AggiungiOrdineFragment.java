package com.example.kebapp.ui.aggiungi_ordine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kebapp.Ingrediente;
import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

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
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewProdotti);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerViewProdottiAdapter adapter = new RecyclerViewProdottiAdapter(getContext(), prodotti);

        ActivityResultLauncher<Intent> aggiunteLauncher = registerForActivityResult
                (
                        new ActivityResultContracts.StartActivityForResult(),
                        result ->
                        {
                            if (result.getResultCode() == Activity.RESULT_OK)
                            {
                                Intent data = result.getData();
                                ArrayList<Ingrediente> listaAggiunte = (ArrayList<Ingrediente>) data.getSerializableExtra("listaAggiunte");
                                int idProdotto = data.getIntExtra("idProdotto", -1);

                                adapter.getItem(idProdotto).aggiunte = listaAggiunte;
                                recyclerView.setAdapter(adapter);
                            }
                        }
                );

        adapter.addLauncher(aggiunteLauncher);

        listaProdotti.add(new Prodotto("Marinara", 4., 0, "Pomodoro"));
        listaProdotti.add(new Prodotto("Margherita", 5., 0, "Pomodoro, mozzarella"));
        listaProdotti.add(new Prodotto("Bufala", 6.5, 0, "Pomodoro, mozzarella di bufala"));
        listaProdotti.add(new Prodotto("Prosciutto e funghi", 7., 0, "Pomodoro, mozzarella, prosciutto cotto, funghi"));
        listaProdotti.add(new Prodotto("Americana", 7., 0, "Pomodoro, mozzarella, wurstel, patatine"));

        ActivityResultLauncher<Intent> prodottoLauncher = registerForActivityResult
                (
                    new ActivityResultContracts.StartActivityForResult(),
                        result ->
                        {
                            if (result.getResultCode() == Activity.RESULT_OK)
                            {
                                Intent data = result.getData();
                                Prodotto prodotto = (Prodotto) data.getSerializableExtra("prodotto");
                                adapter.addItem(prodotto);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                );


        getView().findViewById(R.id.buttonAggiungiProdotto).setOnClickListener(item ->
        {
            Intent intent = new Intent(getActivity(), SelezionaProdottoActivity.class);
            intent.putExtra("listaProdotti", listaProdotti);
            prodottoLauncher.launch(intent);
        });
    }
}