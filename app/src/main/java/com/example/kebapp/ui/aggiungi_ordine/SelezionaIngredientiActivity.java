package com.example.kebapp.ui.aggiungi_ordine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kebapp.Ingrediente;
import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class SelezionaIngredientiActivity extends AppCompatActivity
{
    ArrayList<CardView> cards;

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

        cards = new ArrayList<>();
        for (int i=0; i < listaIngredienti.size(); i++)
        {
            newCard = new CardView(getApplicationContext());
            getLayoutInflater().inflate(R.layout.card_ingrediente, newCard);

            newTextView = newCard.findViewById(R.id.textViewNomeIngrediente);
            newTextView.setText(listaIngredienti.get(i).nome);

            newTextView = newCard.findViewById(R.id.textViewPrezzoIngrediente);
            NumberFormat formatter = new DecimalFormat("#0.00");
            newTextView.setText(formatter.format(listaIngredienti.get(i).prezzo) + "€");

            linearLayoutIngredienti.addView(newCard);

            cards.add(newCard);

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

        ((EditText)findViewById(R.id.editTextCerca)).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String ricerca = s.toString().toLowerCase();

                if(!ricerca.isEmpty())
                {
                    String nome;
                    for (int i = 0; i < cards.size(); i++)
                    {
                        nome = ((TextView)cards.get(i).findViewById(R.id.textViewNomeIngrediente)).getText().toString();

                        if(!nome.toLowerCase().contains(ricerca))
                        {
                            cards.get(i).setVisibility(View.GONE);
                        }
                        else
                        {
                            cards.get(i).setVisibility(View.VISIBLE);
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < cards.size(); i++)
                    {
                        cards.get(i).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        findViewById(R.id.imageViewCancelCerca).setOnClickListener(item->
        {
            ((EditText)findViewById(R.id.editTextCerca)).setText("");

            for (int i = 0; i < cards.size(); i++)
            {
                cards.get(i).setVisibility(View.VISIBLE);
            }
        });

    }
}