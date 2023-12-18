package com.example.kebapp.ui.ordini;

import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kebapp.FireStoreHandler;
import com.example.kebapp.Ordine;
import com.example.kebapp.R;

import java.util.ArrayList;

public class OrdiniFragment extends Fragment
{
    private final String TAG = "OrdiniFragment";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ordini, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        FireStoreHandler database = new FireStoreHandler();
        ArrayList<Ordine> ordini = database.getOrdini();

        getView().findViewById(R.id.buttonAggiorna).setOnClickListener(item ->
        {
            LinearLayout cards = getView().findViewById(R.id.cards);
            cards.removeAllViews();

            CardView newCard;
            TextView newTextView;

            for (int i=0; i < ordini.size(); i++)
            {
                newCard = new CardView(getContext());
                getLayoutInflater().inflate(R.layout.card_ordine, newCard);

                newTextView = newCard.findViewById(R.id.textViewNomeOrdine);
                newTextView.setText(ordini.get(i).nome);

                newTextView = newCard.findViewById(R.id.textViewOrarioRichiesto);
                newTextView.setText(ordini.get(i).orarioRichiesto);

                newTextView = newCard.findViewById(R.id.textViewIndirizzoOrdine);
                newTextView.setText(ordini.get(i).indirizzo);

                newTextView = newCard.findViewById(R.id.textViewTotale);
                newTextView.setText(String.valueOf(ordini.get(i).totale) + '€');

                newTextView = newCard.findViewById(R.id.textViewProdotti);
                String newText = "";
                for (int j = 0; j < ordini.get(i).prodotti.size(); j++)
                {
                    newText += ordini.get(i).prodotti.get(j).quantita + " " + ordini.get(i).prodotti.get(j).nome;

                    for (int k = 0; k < ordini.get(i).prodotti.get(j).aggiunte.size(); k++)
                    {
                        newText += "\n\t\t+ " + ordini.get(i).prodotti.get(j).aggiunte.get(k).nome;
                    }


                    newText += '\n';
                }
                newTextView.setText(newText);

                newTextView = newCard.findViewById(R.id.textViewColoreStatus);
                switch(ordini.get(i).status)
                {
                    case 2:
                        newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_consegnato));
                        break;
                    case 1:
                        newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_in_consegna));
                        break;
                    case 0:
                        newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_in_preparazione));
                        break;
                }

                cards.addView(newCard);
            }
        });


    }
}