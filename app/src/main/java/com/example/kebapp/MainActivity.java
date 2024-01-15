package com.example.kebapp;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.kebapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";
    public boolean coldStart;
    public FirebaseAuth authenticator;
    public ArrayList<Utente> utente;

    public ArrayList<Utente> fattorini;
    public OrdiniUpdaterThread updater;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        coldStart = true;

        authenticator = FirebaseAuth.getInstance();
        FireStoreController database = new FireStoreController();
        utente = database.getUtente(authenticator.getCurrentUser().getUid(), authenticator.getCurrentUser().getEmail());
        fattorini = database.getFattorini();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // funzione che mostra nel fragment degli ordini gli ordini più recenti
    @SuppressLint("SetTextI18n")
    public void RefreshOrdini(View currentView, ArrayList<Ordine> ordini)
    {
        if(!isNetworkAvailable())
        {
            currentView.findViewById(R.id.textViewOffline).setVisibility(View.VISIBLE);
        }
        else
        {
            currentView.findViewById(R.id.textViewOffline).setVisibility(View.GONE);
        }


        // ordini gli ordini in base allo stato
        Collections.sort(ordini, Comparator.comparing(Ordine::getStatus));

        // inizializzo il riferimento al layout contenente tutti gli ordini
        LinearLayout layout = currentView.findViewById(R.id.linearLayoutOrdini);

        // elimino tutti gli ordini già presenti nel layout
        layout.removeAllViews();

        // Hashmap che verrà passata al processo per l'interrogazione della API
        // contiene l'oggetto e un riferimento alla relativa ImageView di ogni ordine
        Map<Ordine, ImageView> riferimentiPerAPI = new HashMap<Ordine, ImageView>();

        // itero per ogni ordine da aggiungere al layout
        for (int i=0; i < ordini.size(); i++)
        {
            // inizializzo per comodità un riferimento all'oggetto Ordine dell'ordine che sto creando
            Ordine ordine = ordini.get(i);

            // inizializzo la nuova CardView e le applico il layout definito in card_ordine.xml
            CardView newCard = new CardView(this);
            getLayoutInflater().inflate(R.layout.card_ordine, newCard);

            // imposto i testi delle varie TextView di card_ordine con i dati dell'oggetto Ordine
            NumberFormat formatter = new DecimalFormat("#0.00");

            ((TextView)newCard.findViewById(R.id.textViewNomeOrdine)).setText(ordine.nome);
            ((TextView)newCard.findViewById(R.id.textViewOrarioRichiesto)).setText(ordine.orarioRichiesto);
            ((TextView)newCard.findViewById(R.id.textViewIndirizzoOrdine)).setText(ordine.indirizzo);
            ((TextView)newCard.findViewById(R.id.textViewNumeroCellulare)).setText(ordine.numero);
            ((TextView)newCard.findViewById(R.id.textViewNote)).setText(ordine.note);
            ((TextView)newCard.findViewById(R.id.textViewTotale)).setText(formatter.format(ordine.totale) + '€');

            if(ordine.pioggia)
            {
                ((ImageView)newCard.findViewById(R.id.imageViewAPI)).setImageResource(R.drawable.rainy);
            }
            else
            {
                ((ImageView)newCard.findViewById(R.id.imageViewAPI)).setImageResource(R.drawable.sun);
            }

            if(!ordine.IDFattorino.equals(""))
            {
                //for (int j = 0; j < ((MainActivity)getActivity()).fattorini.size(); j++)
                for (int j = 0; j < fattorini.size(); j++)
                {
                    //if( ((MainActivity)getActivity()).fattorini.get(j).userID.equals(ordine.IDFattorino) )
                    if( fattorini.get(j).userID.equals(ordine.IDFattorino) )
                    {
                        ((TextView)newCard.findViewById(R.id.textViewFattorino)).setText("Fattorino: " +
                                //((MainActivity)getActivity()).fattorini.get(j).nome);
                                fattorini.get(j).nome);
                        break;
                    }
                }
            }
            else
            {
                ((TextView)newCard.findViewById(R.id.textViewFattorino)).setText("");
            }

            // con i seguenti cicli for ottengo la stringa composta da tutti i prodotti dell'ordine in questione e dalle rispettive aggiunte
            String newText = ""; // inizializzo una stringa vuota
            for (int j = 0; j < ordine.prodotti.size(); j++) // per ogni prodotto...
            {
                // aggiungo al testo la sua quantità e il suo nome
                newText += ordine.prodotti.get(j).quantita + " " + ordine.prodotti.get(j).nome;

                for (int k = 0; k < ordine.prodotti.get(j).aggiunte.size(); k++) // per ogni aggiunta...
                {
                    // aggiungo al testo il suo nome
                    newText += "\n\t\t+ " + ordine.prodotti.get(j).aggiunte.get(k).nome;
                }
                newText += '\n';
            }
            // aggiungo il testo ottenuto alla CardView dell'ordine
            ((TextView)newCard.findViewById(R.id.textViewProdotti)).setText(newText);

            // inizializzo un riferimento alla TextView utilizzata per mostrare il colore indicante dello stato dell'ordine
            TextView newTextView = newCard.findViewById(R.id.textViewColoreStatus);
            switch(ordine.status) // in base allo stato dell'ordine imposto un colore diverso (prendendolo dai drawable)
            {
                // ORDINE CONSEGNATO
                case 2:
                    // imposto il background della TextView al drawable "status_consegnato.xml" (verde)
                    newTextView.setBackground(AppCompatResources.getDrawable(this, R.drawable.status_consegnato));
                    break;

                // ORDINE IN CONSEGNA
                case 1:
                    // imposto il background della TextView al drawable "status_in_consegna.xml" (giallo)
                    newTextView.setBackground(AppCompatResources.getDrawable(this, R.drawable.status_in_consegna));
                    break;

                // ORDINE IN PREPARAZIONE
                case 0:
                    // imposto il background della TextView al drawable "status_in_preparazione.xml" (rosso)
                    newTextView.setBackground(AppCompatResources.getDrawable(this, R.drawable.status_in_preparazione));
                    break;
            }


            riferimentiPerAPI.put(ordine, newCard.findViewById(R.id.imageViewAPI));



            // mi salvo in una variabile l'ID dell'ordine per passarlo all'OnClickListener
            final String IDOrdine = ordine.ID;

            // onClickListener dell'intera card dell'ordine
            // ha lo scopo di aprire il menù di interazione con il singolo ordine
            newCard.setOnClickListener(item ->
            {
                // rendo visibile la view "greyout" presente in MainActivity, così da scurire lo sfondo dietro i popup che aprirò
                findViewById(R.id.greyout).setVisibility(View.VISIBLE);

                View popupViewMain;
                if (utente.get(0).fattorino)
                {
                    popupViewMain = getLayoutInflater().inflate(R.layout.card_ordine_popup_main_fattorino, null);
                }
                else
                {
                    popupViewMain = getLayoutInflater().inflate(R.layout.card_ordine_popup_main, null);
                }

                // imposto il corretto ID dell'ordine nel layout della view
                ((TextView)popupViewMain.findViewById(R.id.textViewID)).setText("Ordine #" + IDOrdine);

                // inizializzo una window contenente la view che ho appena creato
                PopupWindow popupWindowMain = new PopupWindow(popupViewMain, -2, -2, true);

                // OnDismissListener della window appena creata
                // (viene chiamata quando viene chiusa la window, ad esempio premendo al di fuori di essa)
                popupWindowMain.setOnDismissListener(new PopupWindow.OnDismissListener()
                {
                    @Override
                    public void onDismiss()
                    {
                        // rendo invisibile la view "greyout" presente in MainActivity, così da far tornare chiaro lo sfondo
                        findViewById(R.id.greyout).setVisibility(View.INVISIBLE);
                    }
                });

                // mostro a schermo la window appena creata
                popupWindowMain.showAtLocation(new View(this), Gravity.CENTER, 0, 0);


                if (utente.get(0).fattorino)
                {
                    popupViewMain.findViewById(R.id.buttonAssegnaATeStesso).setOnClickListener(lambda1 ->
                    {
                        if(ordine.status == 0)
                        {
                            // comunico a OrdiniUpdaterThread di impostare lo stato dell'ordine a "in consegna" sul server
                            updater.impostaStatoOrdine(IDOrdine, 1);
                        }

                        // comunico a OrdiniUpdaterThread di impostare il fattorino selezionato all'ordine sul server
                        updater.impostaFattorinoOrdine(IDOrdine, utente.get(0).userID);

                        // chiudo tutte le window popup aperte
                        popupWindowMain.dismiss();

                        // avviso l'utente del successo dell'operazione
                        Toast.makeText(this, "Fattorino assegnato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
                    });
                }

                // onClickListener del pulsante "Imposta stato"
                popupViewMain.findViewById(R.id.buttonImpostaStato).setOnClickListener(lambda1 ->
                {
                    // creo e mostro a schermo una window popup con il layout definito in card_ordine_popup_imposta_stato.xml
                    // e le imposto il corretto ID dell'ordine
                    View popupViewStato = getLayoutInflater().inflate(R.layout.card_ordine_popup_imposta_stato, null);
                    ((TextView)popupViewStato.findViewById(R.id.textViewID)).setText("Ordine #" + IDOrdine);
                    PopupWindow popupWindowStato = new PopupWindow(popupViewStato, -2, -2, true);
                    popupWindowStato.showAtLocation(new View(this), Gravity.CENTER, 0, 0);

                    // onClickListener del pulsante "In preparazione"
                    popupViewStato.findViewById(R.id.buttonInPreparazione).setOnClickListener(lambda2 ->
                    {
                        // comunico a OrdiniUpdaterThread di impostare lo stato dell'ordine a "in preparazione" sul server
                        updater.impostaStatoOrdine(IDOrdine, 0);

                        // chiudo tutte le window popup aperte
                        popupWindowStato.dismiss();
                        popupWindowMain.dismiss();

                        // avviso l'utente del successo dell'operazione
                        Toast.makeText(this, "Stato impostato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
                    });

                    // onClickListener del pulsante "In consegna"
                    popupViewStato.findViewById(R.id.buttonInConsegna).setOnClickListener(lambda3 ->
                    {
                        // creo una lista contenente tutte le window aperte
                        ArrayList<PopupWindow> openedWindows = new ArrayList<>();
                        openedWindows.add(popupWindowStato);
                        openedWindows.add(popupWindowMain);

                        // richiamo il metodo che si occupa di visualizzare la window popup per la selezione del fattorino
                        // e gli passo la lista di tutte le window popup che dovrà chiudere al suo termine
                        ApriSelezioneFattorino(IDOrdine, ordine.status, openedWindows);
                    });

                    // onClickListener del pulsante "Consegnato"
                    popupViewStato.findViewById(R.id.buttonConsegnato).setOnClickListener(lambda4 ->
                    {
                        // comunico a OrdiniUpdaterThread di impostare lo stato dell'ordine a "consegnato" sul server
                        updater.impostaStatoOrdine(IDOrdine, 2);

                        // chiudo tutte le window popup aperte
                        popupWindowStato.dismiss();
                        popupWindowMain.dismiss();

                        // avviso l'utente del successo dell'operazione
                        Toast.makeText(this, "Stato impostato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
                    });
                });

                // onClickListener del pulsante "Assegna fattorino"
                popupViewMain.findViewById(R.id.buttonAssegnaFattorino).setOnClickListener(lambda5 ->
                {
                    // creo una lista contenente tutte le window aperte
                    ArrayList<PopupWindow> openedWindows = new ArrayList<>();
                    openedWindows.add(popupWindowMain);

                    // richiamo il metodo che si occupa di visualizzare la window popup per la selezione del fattorino
                    // e gli passo la lista di tutte le window popup che dovrà chiudere al suo termine
                    ApriSelezioneFattorino(IDOrdine, ordine.status, openedWindows);
                });

                // onClickListener del pulsante "Elimina"
                popupViewMain.findViewById(R.id.buttonElimina).setOnClickListener(lambda6 ->
                {
                    // creo e mostro a schermo una window popup con il layout definito in card_ordine_popup_elimina.xml
                    // e le imposto il corretto ID dell'ordine
                    View popupViewElimina = getLayoutInflater().inflate(R.layout.card_ordine_popup_elimina, null);
                    ((TextView)popupViewElimina.findViewById(R.id.textViewID)).setText("Ordine #" + IDOrdine);
                    PopupWindow popupWindowElimina = new PopupWindow(popupViewElimina, -2, -2, true);
                    popupWindowElimina.showAtLocation(new View(this), Gravity.CENTER, 0, 0);

                    // onClickListener del pulsante "Si" (conferma dell'eliminazione)
                    popupViewElimina.findViewById(R.id.buttonSi).setOnClickListener(lambda7 ->
                    {
                        // comunico a OrdiniUpdaterThread di eliminare l'ordine sul server
                        updater.eliminaOrdine(IDOrdine);

                        // chiudo tutte le window popup aperte
                        popupWindowElimina.dismiss();
                        popupWindowMain.dismiss();

                        // avviso l'utente del successo dell'operazione
                        Toast.makeText(this, "Ordine eliminato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
                    });

                    // onClickListener del pulsante "No" (annullamento dell'eliminazione)
                    popupViewElimina.findViewById(R.id.buttonNo).setOnClickListener(lambda8 ->
                    {
                        // chiudo la window popup di conferma dell'eliminazione
                        popupWindowElimina.dismiss();
                    });
                });
            });

            // aggiungo la nuova card appena creata al layout contenente tutti gli ordini
            layout.addView(newCard);
        }

        // processo in background che interroga la API per il meteo per ogni ordine visualizzato
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
        {
            for (Map.Entry<Ordine, ImageView> set : riferimentiPerAPI.entrySet())
            {
                HttpURLConnection urlConnection = null;

                try {

                    String indirizzo = set.getKey().indirizzo.replace(" ", "%20");

                    URL url = new URL("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                            + indirizzo +
                            "/today?unitGroup=metric&elements=precip%2Cpreciptype&include=current%2Cobs%2Cremote%2Cfcst&key=BS8QBEEBXEQ5BZMUTUVB4QYSN&contentType=csv");
                    urlConnection = (HttpURLConnection) url.openConnection();

                    int code = urlConnection.getResponseCode();
                    if (code != 200) {
                        throw new IOException("Invalid response from server: " + code);
                    }

                    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = rd.readLine();
                    line = rd.readLine();

                    double prec;
                    if (line.equals(","))
                    {
                        prec = 0;
                    } else {
                        prec = Double.parseDouble(line.split(",")[0]);
                    }

                    Log.d(TAG, set.getKey() + " --> " + prec);


                    if (prec >= 0.1)
                    {
                        if (!set.getKey().pioggia && (set.getKey().status == 0))
                        {
                            set.getValue().setImageResource(R.drawable.rainy);
                            updater.impostaPioggiaOrdine(set.getKey().ID, true);
                        }
                    } else
                    {
                        if (set.getKey().pioggia && (set.getKey().status == 0))
                        {
                            set.getValue().setImageResource(R.drawable.sun);
                            updater.impostaPioggiaOrdine(set.getKey().ID, false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                }
            }
        });
    }

    void ApriSelezioneFattorino(String IDOrdine, int statusOrdine, ArrayList<PopupWindow> openedWindows)
    {
        // creo e mostro a schermo una window popup con il layout definito in card_ordine_popup_seleziona_fattorino.xml
        // e le imposto il corretto ID dell'ordine
        View popupViewSelezionaFattorino = getLayoutInflater().inflate(R.layout.card_ordine_popup_seleziona_fattorino, null);
        ((TextView)popupViewSelezionaFattorino.findViewById(R.id.textViewID)).setText("Ordine #" + IDOrdine);
        PopupWindow popupWindowSelezionaFattorino = new PopupWindow(popupViewSelezionaFattorino, -2, -2, true);
        popupWindowSelezionaFattorino.showAtLocation(new View(this), Gravity.CENTER, 0, 0);

        // prendo dalla MainActivity la lista di tutti i fattorini presenti nel server
        //ArrayList<Utente> fattorini = ((MainActivity)getActivity()).fattorini;

        // creo un'ArrayList di stringhe con solo i nomi dei fattorini
        ArrayList<String> fattoriniString = new ArrayList<>();
        fattoriniString.add("---"); // primo item dello spinner, equivale a "nessun fattorino selezionato"
        for (int j = 0; j < fattorini.size(); j++)
        {
            fattoriniString.add(fattorini.get(j).nome);
        }

        // imposto i valori selezionabili dallo spinner, usando l'ArrayList appena creata
        Spinner spinner = popupViewSelezionaFattorino.findViewById(R.id.spinnerFattorini);
        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, fattoriniString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // onClickListener del pulsante "Seleziona"
        popupViewSelezionaFattorino.findViewById(R.id.buttonSeleziona).setOnClickListener(lambda ->
        {
            // ottengo l'indice del fattorino selezionato
            // (siccome lo spinner contiene un elemento in più rispetto alla lista di tutti i fattorini,
            // ossia il "---" all'inizio, mi basta decrementare di 1 l'indice selezionato dallo spinner
            // per ottenere l'indice corretto del fattorino nell'ArrayList fattorini)
            int indexFattorino = spinner.getSelectedItemPosition()-1;

            if (indexFattorino >= 0) // se l'utente ha selezionato un fattorino...
            {
                // ottengo lo userID del fattorino selezionato
                String userID = fattorini.get(indexFattorino).userID;

                if(statusOrdine == 0)
                {
                    // comunico a OrdiniUpdaterThread di impostare lo stato dell'ordine a "in consegna" sul server
                    updater.impostaStatoOrdine(IDOrdine, 1);
                }

                // comunico a OrdiniUpdaterThread di impostare il fattorino selezionato all'ordine sul server
                updater.impostaFattorinoOrdine(IDOrdine, userID);

                // chiudo tutte le window popup aperte
                popupWindowSelezionaFattorino.dismiss();
                for (int i = 0; i < openedWindows.size(); i++)
                {
                    openedWindows.get(i).dismiss();
                }

                // avviso l'utente del successo dell'operazione
                Toast.makeText(this, "Fattorino assegnato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
            }
            else // se l'utente non ha selezionato un fattorino (ossia ha selezionato "---")...
            {
                // avviso l'utente dell'errore
                Toast.makeText(this, "Seleziona un fattorino", Toast.LENGTH_LONG).show();
            }
        });
    }
}