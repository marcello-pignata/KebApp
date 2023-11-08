package com.example.kebapp.ui.ordini;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kebapp.R;

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
        String[] ordini = {"Ordine 1", "Ordine 2", "Ordine 3", "Ordine 4", "Ordine 5"};

        LinearLayout cards = getView().findViewById(R.id.cards);
        CardView newCard;
        TextView newTextView;

        for (int i=0; i < ordini.length; i++)
        {
            newCard = new CardView(getContext());
            getLayoutInflater().inflate(R.layout.card_ordine, newCard);
            newTextView = newCard.findViewById(R.id.textViewOrdine);

            newTextView.setText(ordini[i]);

            cards.addView(newCard);
        }
    }
}