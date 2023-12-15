package com.example.kebapp.ui.aggiungi_ordine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kebapp.Ingrediente;
import com.example.kebapp.R;

import java.util.ArrayList;

public class SelezionaIngredientiActivity extends AppCompatActivity
{
    // tag usato per debugging
    private final String TAG = "SelezionaIngredientiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleziona_ingredienti);

        // ottengo i dati passati da RecyclerViewProdottiAdapter
        ArrayList<Ingrediente> listaIngredienti = (ArrayList<Ingrediente>)getIntent().getSerializableExtra("listaIngredienti");
        ArrayList<Ingrediente> listaAggiunte = (ArrayList<Ingrediente>)getIntent().getSerializableExtra("listaAggiunte");
        int idProdotto = getIntent().getIntExtra("idProdotto", -1);

        // per ogni ingrediente disponibile creo una nuova card da aggiungere all'activity
        CardView newCard;
        TextView newTextView;
        for (int i=0; i < listaIngredienti.size(); i++)
        {
            // inizializzo la nuova card e le assegno il layout definito in card_ingrediente.xml
            newCard = new CardView(getApplicationContext());
            getLayoutInflater().inflate(R.layout.card_ingrediente, newCard);

            // imposto il testo della text view per il nome dell'ingrediente
            newTextView = newCard.findViewById(R.id.textViewNomeProdotto);
            newTextView.setText(listaIngredienti.get(i).nome);

            // imposto il testo della text view per il prezzo dell'ingrediente
            newTextView = newCard.findViewById(R.id.textViewPrezzoProdotto);
            newTextView.setText(listaIngredienti.get(i).prezzo + "€");

            // inizializzo la checkbox della card
            CheckBox newCheckBox = newCard.findViewById(R.id.checkBoxSelezionaIngrediente);

            // se tra la lista delle aggiunte già esistenti è presente l'ingrediente di questa card
            // allora imposto la sua checkbox come già spuntata
            for (int j = 0; j < listaAggiunte.size(); j++)
            {
                if(listaAggiunte.get(j).nome.equals(listaIngredienti.get(i).nome))
                {
                    newCheckBox.setChecked(true);
                }
            }

            // setOnClickListener della checkbox della card
            int finalI = i;
            newCheckBox.setOnClickListener(item ->
            {
                // se la checkbox viene spuntata allora aggiungo il corrispondente ingrediente a listaAggiunte
                if (newCheckBox.isChecked())
                {
                    listaAggiunte.add(listaIngredienti.get(finalI));
                }
                // se la checkbox viene deselezionata allora rimuovo il corrispondente ingrediente da listaAggiunte
                else
                {
                    for (int j = 0; j < listaAggiunte.size(); j++)
                    {
                        if(listaAggiunte.get(j).nome.equals(listaIngredienti.get(finalI).nome))
                        {
                            listaAggiunte.remove(j);
                        }
                    }
                }
            });

            // aggiungo la nuova card al layout
            ((LinearLayout)findViewById(R.id.linearLayoutIngredienti)).addView(newCard);
        }

        // setOnClickListener del bottone per aggiungere gli ingredienti
        findViewById(R.id.buttonAggiungiAggiunte).setOnClickListener(item ->
        {
            // termino l'esecuzione dell'activity, ritornando come result:
            //      - l'elenco delle aggiunte modificato
            //      - l'ID del prodotto che vogliamo modificare
            Intent resultIntent = new Intent();
            resultIntent.putExtra("listaAggiunte", listaAggiunte);
            resultIntent.putExtra("idProdotto", idProdotto);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}