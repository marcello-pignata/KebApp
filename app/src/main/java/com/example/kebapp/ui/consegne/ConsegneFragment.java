package com.example.kebapp.ui.consegne;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        if (((MainActivity)getActivity()).utente.get(0).fattorino)
        {
            ArrayList<Ordine> consegne = ((MainActivity)getActivity()).updater.updatedOrdini;

            for (int i = 0; i < consegne.size(); i++)
            {
                if (!consegne.get(i).IDFattorino.equals(((MainActivity)getActivity()).authenticator.getCurrentUser().getUid()))
                {
                    consegne.remove(consegne.get(i));
                }
            }

            if(!consegne.isEmpty())
            {
                ((MainActivity)getActivity()).RefreshOrdini(getView(), consegne);
            }
            else
            {
                ((TextView)getView().findViewById(R.id.textViewAvvisi)).setText("Non sono presenti consegne a tuo nome");
            }
        }
        else
        {
            ((TextView)getView().findViewById(R.id.textViewAvvisi)).setText("Questa pagina è visualizzabile soltanto dai fattorini");
        }

    }
}