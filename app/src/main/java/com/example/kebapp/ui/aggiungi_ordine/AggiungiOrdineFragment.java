package com.example.kebapp.ui.aggiungi_ordine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kebapp.FireStoreController;
import com.example.kebapp.Ingrediente;
import com.example.kebapp.Ordine;
import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
        FireStoreController database = new FireStoreController();

        // download elenco prodotti
        ArrayList<Prodotto> listaProdotti = database.getProdotti();

        // download elenco ingredienti
        adapter.addListaIngredienti(database.getIngredienti());


        NumberFormat formatter = new DecimalFormat("#0.00");

        // observer che viene eseguito quando viene chiamato notifyItemRangeChanged dall'adapter
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount)
            {
                ((TextView)getView().findViewById(R.id.textViewTotale)).setText(formatter.format(adapter.getTotale()) + '€');
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
                                ((TextView)getView().findViewById(R.id.textViewTotale)).setText(formatter.format(adapter.getTotale()) + '€');
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
                                ((TextView)getView().findViewById(R.id.textViewTotale)).setText(formatter.format(adapter.getTotale()) + '€');
                            }
                        }
                );


        // onClickListener del pulsante per l'aggiunta di un prodotto
        getView().findViewById(R.id.buttonAggiungiProdotto).setOnClickListener(item ->
        {
            // faccio partire SelezionaProdottoActivity passandogli l'elenco dei prodotti
            Intent intent = new Intent(getActivity(), SelezionaProdottoActivity.class);
            intent.putExtra("listaProdotti", listaProdotti);
            prodottoLauncher.launch(intent);
        });

        // onClickListener del pulsante per l'invio dell'ordine
        getView().findViewById(R.id.buttonAggiungiOrdine).setOnClickListener(item ->
        {
            String nome = ((EditText)getView().findViewById(R.id.textInputNome)).getText().toString();
            String indirizzo = ((EditText)getView().findViewById(R.id.textInputIndirizzo)).getText().toString();
            String orario = ((EditText)getView().findViewById(R.id.textInputOrarioRichiesto)).getText().toString();
            String note = ((EditText)getView().findViewById(R.id.textInputNote)).getText().toString();
            String numero = ((EditText)getView().findViewById(R.id.textInputNumero)).getText().toString();
            int countProdotti = adapter.getItemCount();

            if(!nome.isEmpty() && !indirizzo.isEmpty() && !orario.isEmpty() && !numero.isEmpty() && countProdotti != 0)
            {
                Ordine ordine = new Ordine(nome, indirizzo, orario, note, numero, (ArrayList<Prodotto>) adapter.getmData(), adapter.getTotale());

                database.putOrdine(ordine);

                ((EditText)getView().findViewById(R.id.textInputNome)).setText("");
                ((EditText)getView().findViewById(R.id.textInputIndirizzo)).setText("");
                ((EditText)getView().findViewById(R.id.textInputOrarioRichiesto)).setText("");
                ((EditText)getView().findViewById(R.id.textInputNote)).setText("");
                ((EditText)getView().findViewById(R.id.textInputNumero)).setText("");
                adapter.clear();

                Toast.makeText(getContext(), "Ordine inserito con successo", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getContext(), "Completare tutti i campi prima di aggiungere l'ordine", Toast.LENGTH_LONG).show();
            }

        });
    }
}