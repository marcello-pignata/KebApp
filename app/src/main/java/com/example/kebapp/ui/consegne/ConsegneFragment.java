package com.example.kebapp.ui.consegne;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kebapp.MainActivity;
import com.example.kebapp.Ordine;
import com.example.kebapp.R;

import java.util.ArrayList;

public class ConsegneFragment extends Fragment {


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_consegne, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        if (!((MainActivity)getActivity()).utente.get(0).fattorino)
        {
            ((TextView)getView().findViewById(R.id.textViewAvvisi)).setText("Questa pagina è visualizzabile soltanto dai fattorini");
        }
        else
        {
            getView().findViewById(R.id.textViewAvvisi).setVisibility(View.GONE);
        }

        AggiornaConsegne();

        // onClickListener del pulsante "Aggiorna"
        getView().findViewById(R.id.buttonAggiorna).setOnClickListener(item ->
        {
            AggiornaConsegne();
        });

    }

    private void AggiornaConsegne()
    {
        ArrayList<Ordine> ordini = ((MainActivity)getActivity()).updater.updatedOrdini;
        ArrayList<Ordine> consegne= new ArrayList<>();

        String IDFattorino = ((MainActivity)getActivity()).utente.get(0).userID;

        for (int i = 0; i < ordini.size(); i++)
        {
            if (IDFattorino.equals(ordini.get(i).IDFattorino))
            {
                consegne.add(ordini.get(i));
            }
        }

        if(!consegne.isEmpty())
        {
            getView().findViewById(R.id.textViewAvvisi).setVisibility(View.GONE);
            ((MainActivity)getActivity()).RefreshOrdini(getView(), consegne);
        }
        else
        {
            ((LinearLayout)getView().findViewById(R.id.linearLayoutOrdini)).removeAllViews();
            getView().findViewById(R.id.textViewAvvisi).setVisibility(View.VISIBLE);
            ((TextView)getView().findViewById(R.id.textViewAvvisi)).setText("Non sono presenti ordini a tuo nome");
        }
    }
}