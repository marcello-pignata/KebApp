package com.example.kebapp.ui.aggiungi_ordine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

import java.util.ArrayList;

public class SelezionaProdottoActivity extends AppCompatActivity
{
    // tag usato per debugging
    private final String TAG = "SelezionaProdottoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleziona_prodotto);

        // ottengo la lista dei prodotti disponibili passata da AggiungiOrdineFragment
        ArrayList<Prodotto> listaProdotti = (ArrayList<Prodotto>)getIntent().getSerializableExtra("listaProdotti");

        // per ogni prodotto disponibile creo una nuova card da aggiungere all'activity
        CardView newCard;
        TextView newTextView;
        for (int i=0; i < listaProdotti.size(); i++)
        {
            // inizializzo la nuova card e le assegno il layout definito in card_prodotto.xml
            newCard = new CardView(getApplicationContext());
            getLayoutInflater().inflate(R.layout.card_prodotto, newCard);

            // imposto il testo della text view per il nome del prodotto
            newTextView = newCard.findViewById(R.id.textViewNomeProdotto);
            newTextView.setText(listaProdotti.get(i).nome);

            // imposto il testo della text view per la descrizione del prodotto
            newTextView = newCard.findViewById(R.id.textViewDescrizione);
            newTextView.setText(listaProdotti.get(i).descrizione);

            // imposto il testo della text view per il prezzo del prodotto
            newTextView = newCard.findViewById(R.id.textViewPrezzoProdotto);
            newTextView.setText(listaProdotti.get(i).prezzo + "€");

            // inizializzo l'edit text per la quantità selezionata del prodotto
            EditText editTextQuantita = newCard.findViewById(R.id.editTextQuantita);

            // setOnClickListener del pulsante di incremento della quantità
            newCard.findViewById(R.id.buttonIncQuantita).setOnClickListener(item ->
            {
                // se la edit text è vuota imposto la quantità a 1
                if(editTextQuantita.getText().toString().matches(""))
                {
                    editTextQuantita.setText("1");
                }
                // altrimenti incremento di 1 il valore già presente
                else
                {
                    editTextQuantita.setText(String.valueOf(Integer.parseInt(editTextQuantita.getText().toString())+1));
                }
            });

            // setOnClickListener del pulsante di decremento della quantità
            newCard.findViewById(R.id.buttonDecQuantita).setOnClickListener(item ->
            {
                // se la edit text è vuota imposto la quantità a 1
                if(editTextQuantita.getText().toString().matches(""))
                {
                    editTextQuantita.setText("1");
                }
                // altrimenti incremento di 1 il valore già presente
                else
                {
                    editTextQuantita.setText(String.valueOf(Integer.parseInt(editTextQuantita.getText().toString())-1));
                }
            });

            // setOnClickListener del pulsante di aggiunta del prodotto
            int finalI = i;
            newCard.findViewById(R.id.buttonSeleziona).setOnClickListener(item ->
            {
                // controllo che la quantità selezionata sia almeno 1
                if(!editTextQuantita.getText().toString().matches("") && !editTextQuantita.getText().toString().equals("0"))
                {
                    // inizializzo l'oggetto prodotto che ritornerò a AggiungiOrdineFragment, con la corretta quantità
                    Prodotto prodotto = listaProdotti.get(finalI);
                    prodotto.quantita = Integer.parseInt(editTextQuantita.getText().toString());

                    // termino l'esecuzione dell'activity, ritornando come result il prodotto selezionato
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("prodotto", prodotto);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });

            // aggiungo la nuova card al layout
            ((LinearLayout)findViewById(R.id.linearLayoutOrdini)).addView(newCard);
        }
    }
}