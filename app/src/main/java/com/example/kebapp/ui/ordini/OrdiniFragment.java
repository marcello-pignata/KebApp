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

import com.example.kebapp.Ordine;
import com.example.kebapp.Prodotto;
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
        //String[] ordini = {"Ordine 1", "Ordine 2", "Ordine 3", "Ordine 4", "Ordine 5"};

        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        LinearLayout cards = getView().findViewById(R.id.cards);
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


            newTextView = newCard.findViewById(R.id.textViewID);
            newTextView.setText('#' + String.valueOf(ordini.get(i).ID));

            newTextView = newCard.findViewById(R.id.textViewIndirizzoOrdine);
            newTextView.setText(ordini.get(i).indirizzo);

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
                case -1:
                    newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_cancellato));
                    break;
            }

            cards.addView(newCard);
        }
    }
}