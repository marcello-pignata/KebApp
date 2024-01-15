package com.example.kebapp.ui.ordini;

import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kebapp.MainActivity;
import com.example.kebapp.Ordine;
import com.example.kebapp.OrdiniUpdaterThread;
import com.example.kebapp.R;
import com.example.kebapp.Utente;
import com.google.logging.type.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrdiniFragment extends Fragment
{
    // tag usato per debugging
    private final String TAG = "OrdiniFragment";

    // thread utilizzato per interrogare costantemente il database,
    // così da avere una lista sempre aggiornata degli ordini.
    // Funge anche da interfaccia tra il FragmentOrdini e il FireStoreController
    private OrdiniUpdaterThread updater;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ((MainActivity)getActivity()).updater = new OrdiniUpdaterThread((MainActivity) getActivity());

        updater = ((MainActivity)getActivity()).updater;

        updater.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ordini, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

        // inizializzo i riferimenti al pulsante "Aggiorna" e alla gif del caricamento
        View buttonAggiorna = getView().findViewById(R.id.buttonAggiorna);
        View gifLoading = getView().findViewById(R.id.gifLoading);

        // se è la prima volta che il fragment degli ordini viene aperto dall'avvio dell'app...
        if(((MainActivity)getActivity()).coldStart)
        {
            // segnalo all'activity che il fragment degli ordini è stato aperto almeno una volta
            ((MainActivity)getActivity()).coldStart = false;

            // faccio partire un timer
            buttonAggiorna.postDelayed(new Runnable()
            {
                public void run()
                {
                    // al termine del timer nascondo la gif del caricamento e mostro il pulsante "Aggiorna"
                    gifLoading.setVisibility(View.GONE);
                    buttonAggiorna.setVisibility(View.VISIBLE);

                    try
                    {
                        // aggiorno la pagina degli ordini, utilizzando la più recente lista di ordini presente su OrdiniUpdaterThread
                        ((MainActivity)getActivity()).RefreshOrdini(getView(), updater.updatedOrdini, true);
                    }
                    catch(Exception e){}
                }
            }, updater.REFRESH_RATE+500); // il timer ha la stessa durata del refresh rate
                                                   // di OrdiniUpdaterThread, più mezzo secondo aggiunto
                                                   // per sicurezza
        }
        // se il fragment degli ordini è già stato aperto almeno una volta dall'avvio dell'app
        // (e quindi sono già presenti degli ordini scaricati da OrdiniUpdaterThread)...
        else
        {
            // nascondo la gif del caricamento e mostro il pulsante "Aggiorna"
            gifLoading.setVisibility(View.GONE);
            buttonAggiorna.setVisibility(View.VISIBLE);

            // aggiorno la pagina degli ordini, utilizzando la più recente lista di ordini presente su OrdiniUpdaterThread
            ((MainActivity)getActivity()).RefreshOrdini(getView(), updater.updatedOrdini, true);
        }

        // onClickListener del pulsante "Aggiorna"
        buttonAggiorna.setOnClickListener(item ->
        {
            // aggiorno la pagina degli ordini, utilizzando la più recente lista di ordini presente su OrdiniUpdaterThread
            ((MainActivity)getActivity()).RefreshOrdini(getView(), updater.updatedOrdini, true);
        });
    }


}