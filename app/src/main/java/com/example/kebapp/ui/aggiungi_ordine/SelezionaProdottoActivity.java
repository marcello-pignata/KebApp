package com.example.kebapp.ui.aggiungi_ordine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

import java.util.ArrayList;

public class SelezionaProdottoActivity extends AppCompatActivity {

    private final String TAG = "SelezionaProdottoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleziona_prodotto);

        ArrayList<Prodotto> listaProdotti = (ArrayList<Prodotto>)getIntent().getSerializableExtra("listaProdotti");

        for (int i=0; i<listaProdotti.size(); i++)
        {
            Log.d(TAG, listaProdotti.get(i).nome + " [" + listaProdotti.get(i).descrizione + "]");
        }

        LinearLayout cards = findViewById(R.id.cards);
        CardView newCard;
        TextView newTextView;

        for (int i=0; i < listaProdotti.size(); i++)
        {
            newCard = new CardView(getApplicationContext());
            getLayoutInflater().inflate(R.layout.card_prodotto, newCard);

            newTextView = newCard.findViewById(R.id.textViewNomeIngrediente);
            newTextView.setText(listaProdotti.get(i).nome);

            newTextView = newCard.findViewById(R.id.textViewDescrizione);
            newTextView.setText(listaProdotti.get(i).descrizione);

            newTextView = newCard.findViewById(R.id.textViewPrezzoIngrediente);
            newTextView.setText(listaProdotti.get(i).prezzo + "€");

            EditText editTextQuantita = newCard.findViewById(R.id.editTextQuantita);


            newCard.findViewById(R.id.buttonIncQuantita).setOnClickListener(item ->
            {
                if(editTextQuantita.getText().toString().matches(""))
                {
                    editTextQuantita.setText("1");
                }
                else
                {
                    editTextQuantita.setText(String.valueOf(Integer.parseInt(editTextQuantita.getText().toString())+1));
                }
            });

            newCard.findViewById(R.id.buttonDecQuantita).setOnClickListener(item ->
            {
                if(editTextQuantita.getText().toString().matches(""))
                {
                    editTextQuantita.setText("1");
                }
                else
                {
                    editTextQuantita.setText(String.valueOf(Integer.parseInt(editTextQuantita.getText().toString())-1));
                }
            });

            int finalI = i;
            newCard.findViewById(R.id.buttonSeleziona).setOnClickListener(item ->
            {
                if(!editTextQuantita.getText().toString().matches("") && !editTextQuantita.getText().toString().equals("0"))
                {
                    Prodotto prodotto = listaProdotti.get(finalI);
                    prodotto.quantita = Integer.parseInt(editTextQuantita.getText().toString());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("prodotto", prodotto);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });

            cards.addView(newCard);
        }
    }
}