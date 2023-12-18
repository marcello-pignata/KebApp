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
import android.widget.EditText;
import android.widget.TextView;

import com.example.kebapp.FireStoreHandler;
import com.example.kebapp.Ingrediente;
import com.example.kebapp.Ordine;
import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

import java.util.ArrayList;

public class AggiungiOrdineFragment extends Fragment
{
    // tag usato per debugging
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
        // inizializzazione layout della recycler view
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewProdotti);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // inizializzazione adapter per la recycler view
        RecyclerViewProdottiAdapter adapter = new RecyclerViewProdottiAdapter(getContext());

        // collegamento con database FireStore
        FireStoreHandler database = new FireStoreHandler();

        // download elenco prodotti
        Log.d(TAG, "CHIAMO GETPRODOTTI()");
        ArrayList<Prodotto> listaProdotti = database.getProdotti();
        Log.d(TAG, "1° listaProdotti.size() = " + listaProdotti.size());

        // download elenco ingredienti
        adapter.addListaIngredienti(database.getIngredienti());

        // observer che viene eseguito quando viene chiamato notifyItemRangeChanged dall'adapter
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount)
            {
                ((TextView)getView().findViewById(R.id.textViewTotale)).setText(String.valueOf(adapter.getTotale()) + '€');
            }
        });

        // inizializzazione launcher per SelezionaIngredientiActivity
        ActivityResultLauncher<Intent> aggiunteLauncher = registerForActivityResult
                (
                        new ActivityResultContracts.StartActivityForResult(),
                        result ->
                        {
                            if (result.getResultCode() == Activity.RESULT_OK)
                            {
                                // ricezioni dati da SelezionaIngredientiActivity
                                Intent data = result.getData();
                                ArrayList<Ingrediente> listaAggiunte = (ArrayList<Ingrediente>) data.getSerializableExtra("listaAggiunte");
                                int idProdotto = data.getIntExtra("idProdotto", -1);

                                // aggiunta dati ricevuti alla recycler view
                                adapter.getItem(idProdotto).aggiunte = listaAggiunte;
                                recyclerView.setAdapter(adapter);
                                ((TextView)getView().findViewById(R.id.textViewTotale)).setText(String.valueOf(adapter.getTotale()) + '€');
                            }
                        }
                );
        adapter.addLauncher(aggiunteLauncher);

        // inizializzazione launcher per SelezionaProdottoActivity
        ActivityResultLauncher<Intent> prodottoLauncher = registerForActivityResult
                (
                    new ActivityResultContracts.StartActivityForResult(),
                        result ->
                        {
                            if (result.getResultCode() == Activity.RESULT_OK)
                            {
                                // ricezioni dati da SelezionaProdottoActivity
                                Intent data = result.getData();
                                Prodotto prodotto = (Prodotto) data.getSerializableExtra("prodotto");

                                // aggiunta dati ricevuti alla recycler view
                                adapter.addItem(prodotto);
                                recyclerView.setAdapter(adapter);
                                ((TextView)getView().findViewById(R.id.textViewTotale)).setText(String.valueOf(adapter.getTotale()) + '€');
                            }
                        }
                );


        // onClickListener del pulsante per l'aggiunta di un prodotto
        getView().findViewById(R.id.buttonAggiungiProdotto).setOnClickListener(item ->
        {
            Log.d(TAG, "2° listaProdotti.size() = " + listaProdotti.size());

            // faccio partire SelezionaProdottoActivity passandogli l'elenco dei prodotti
            Intent intent = new Intent(getActivity(), SelezionaProdottoActivity.class);
            intent.putExtra("listaProdotti", listaProdotti);
            prodottoLauncher.launch(intent);
        });

        // onClickListener del pulsante per l'invio dell'ordine
        getView().findViewById(R.id.buttonAggiungiOrdine).setOnClickListener(item ->
        {
            Ordine ordine = new Ordine(
                    ((EditText)getView().findViewById(R.id.textInputNome)).getText().toString(),
                    ((EditText)getView().findViewById(R.id.textInputIndirizzo)).getText().toString(),
                    ((EditText)getView().findViewById(R.id.textInputOrarioRichiesto)).getText().toString(),
                    ((EditText)getView().findViewById(R.id.textInputNote)).getText().toString(),
                    ((EditText)getView().findViewById(R.id.textInputNumero)).getText().toString(),
                    (ArrayList<Prodotto>) adapter.getmData(),
                    adapter.getTotale()
            );
            database.putOrdine(ordine);
        });
    }
}