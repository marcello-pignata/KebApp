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

        ordini.add(new Ordine(
                1,
                "Devis",
                "via delle Robinie 15, Seveso (MB)",
                "20:00",
                "18:47",
                new ArrayList<Prodotto>(),
                23.5,
                false,
                Ordine.Status.CONSEGNATO));
        ordini.add(new Ordine(
                2,
                "Marcello",
                "via Venezia 13, Meda (MB)",
                "19:30",
                "18:12",
                new ArrayList<Prodotto>(),
                12.,
                true,
                Ordine.Status.CANCELLATO));
        ordini.add(new Ordine(
                3,
                "Riccardo",
                "via XXV Aprile 7, Cesano Maderno (MB)",
                "ASAP",
                "19:38",
                new ArrayList<Prodotto>(),
                14.5,
                true,
                Ordine.Status.IN_PREPARAZIONE));
        ordini.add(new Ordine(
                4,
                "Marco",
                "via Marco Polo 93, Meda (MB)",
                "20:30",
                "18:24",
                new ArrayList<Prodotto>(),
                47.5,
                false,
                Ordine.Status.IN_CONSEGNA));

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
                case CONSEGNATO:
                    newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_consegnato));
                    break;
                case IN_CONSEGNA:
                    newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_in_consegna));
                    break;
                case IN_PREPARAZIONE:
                    newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_in_preparazione));
                    break;
                case CANCELLATO:
                    newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_cancellato));
                    break;
            }

            cards.addView(newCard);
        }

    }
}