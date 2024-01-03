package com.example.kebapp.ui.aggiungi_ordine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kebapp.Ingrediente;
import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

import java.util.ArrayList;

public class SelezionaIngredientiActivity extends AppCompatActivity
{
    ArrayList<Ingrediente> listaAggiunte;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleziona_ingredienti);

        ArrayList<Ingrediente> listaIngredienti = (ArrayList<Ingrediente>)getIntent().getSerializableExtra("listaIngredienti");
        ArrayList<Ingrediente> listaAggiunte = (ArrayList<Ingrediente>)getIntent().getSerializableExtra("listaAggiunte");
        int idProdotto = getIntent().getIntExtra("idProdotto", -1);

        LinearLayout linearLayoutIngredienti = findViewById(R.id.linearLayoutIngredienti);
        CardView newCard;
        TextView newTextView;

        findViewById(R.id.buttonAggiungiAggiunte).setOnClickListener(item ->
        {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("listaAggiunte", listaAggiunte);
            resultIntent.putExtra("idProdotto", idProdotto);
            setResult(RESULT_OK, resultIntent);
            finish();
        });


        for (int i=0; i < listaIngredienti.size(); i++)
        {
            newCard = new CardView(getApplicationContext());
            getLayoutInflater().inflate(R.layout.card_ingrediente, newCard);

            newTextView = newCard.findViewById(R.id.textViewNomeIngrediente);
            newTextView.setText(listaIngredienti.get(i).nome);

            newTextView = newCard.findViewById(R.id.textViewPrezzoIngrediente);
            newTextView.setText(listaIngredienti.get(i).prezzo + "€");

            linearLayoutIngredienti.addView(newCard);

            int finalI = i;

            CheckBox newCheckBox = newCard.findViewById(R.id.checkBoxSelezionaIngrediente);

            for (int j = 0; j < listaAggiunte.size(); j++)
            {
                if(listaAggiunte.get(j).nome.equals(listaIngredienti.get(finalI).nome))
                {
                    newCheckBox.setChecked(true);
                }
            }

            newCheckBox.setOnClickListener(item ->
            {
                if (newCheckBox.isChecked())
                {
                    listaAggiunte.add(listaIngredienti.get(finalI));
                }
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
        }

    }
}