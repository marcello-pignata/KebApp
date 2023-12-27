package com.example.kebapp.ui.ordini;

import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kebapp.MainActivity;
import com.example.kebapp.Ordine;
import com.example.kebapp.R;
import com.example.kebapp.Utente;

import java.util.ArrayList;

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

        // inizializzo e faccio partire OrdiniUpdaterThread in background
        updater = new OrdiniUpdaterThread();
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
                        RefreshOrdini(updater.updatedOrdini);
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
            RefreshOrdini(updater.updatedOrdini);
        }

        // onClickListener del pulsante "Aggiorna"
        buttonAggiorna.setOnClickListener(item ->
        {
            // aggiorno la pagina degli ordini, utilizzando la più recente lista di ordini presente su OrdiniUpdaterThread
            RefreshOrdini(updater.updatedOrdini);
        });
    }

    // funzione che mostra nel fragment degli ordini gli ordini più recenti
    public void RefreshOrdini(ArrayList<Ordine> ordini)
    {
        // inizializzo il riferimento al layout contenente tutti gli ordini
        LinearLayout layout = getView().findViewById(R.id.linearLayoutOrdini);
        
        // elimino tutti gli ordini già presenti nel layout
        layout.removeAllViews();

        // itero per ogni ordine da aggiungere al layout
        for (int i=0; i < ordini.size(); i++)
        {
            // inizializzo per comodità un riferimento all'oggetto Ordine dell'ordine che sto creando
            Ordine ordine = ordini.get(i);
            
            // inizializzo la nuova CardView e le applico il layout definito in card_ordine.xml
            CardView newCard = new CardView(getContext());
            getLayoutInflater().inflate(R.layout.card_ordine, newCard);

            // imposto i testi delle varie TextView di card_ordine con i dati dell'oggetto Ordine
            ((TextView)newCard.findViewById(R.id.textViewNomeOrdine)).setText(ordine.nome);
            ((TextView)newCard.findViewById(R.id.textViewOrarioRichiesto)).setText(ordine.orarioRichiesto);
            ((TextView)newCard.findViewById(R.id.textViewIndirizzoOrdine)).setText(ordine.indirizzo);
            ((TextView)newCard.findViewById(R.id.textViewTotale)).setText(String.valueOf(ordine.totale) + '€');

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
                    newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_consegnato));
                    break;

                // ORDINE IN CONSEGNA
                case 1:
                    // imposto il background della TextView al drawable "status_in_consegna.xml" (giallo)
                    newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_in_consegna));
                    break;

                // ORDINE IN PREPARAZIONE
                case 0:
                    // imposto il background della TextView al drawable "status_in_preparazione.xml" (rosso)
                    newTextView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.status_in_preparazione));
                    break;
            }

            // mi salvo in una variabile l'ID dell'ordine per passarlo all'OnClickListener
            final String IDOrdine = ordine.ID;

            // onClickListener dell'intera card dell'ordine
            // ha lo scopo di aprire il menù di interazione con il singolo ordine
            newCard.setOnClickListener(item ->
            {
                // rendo visibile la view "greyout" presente in MainActivity, così da scurire lo sfondo dietro i popup che aprirò
                getActivity().findViewById(R.id.greyout).setVisibility(View.VISIBLE);

                // inizializzo una view con il layout definito in card_ordine_popup_main.xml
                View popupViewMain = getLayoutInflater().inflate(R.layout.card_ordine_popup_main, null);

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
                        getActivity().findViewById(R.id.greyout).setVisibility(View.INVISIBLE);
                    }
                });

                // mostro a schermo la window appena creata
                popupWindowMain.showAtLocation(new View(getContext()), Gravity.CENTER, 0, 0);

                // onClickListener del pulsante "Imposta stato"
                popupViewMain.findViewById(R.id.buttonImpostaStato).setOnClickListener(lambda1 ->
                {
                    // creo e mostro a schermo una window popup con il layout definito in card_ordine_popup_imposta_stato.xml
                    // e le imposto il corretto ID dell'ordine
                    View popupViewStato = getLayoutInflater().inflate(R.layout.card_ordine_popup_imposta_stato, null);
                    ((TextView)popupViewStato.findViewById(R.id.textViewID)).setText("Ordine #" + IDOrdine);
                    PopupWindow popupWindowStato = new PopupWindow(popupViewStato, -2, -2, true);
                    popupWindowStato.showAtLocation(new View(getContext()), Gravity.CENTER, 0, 0);

                    // onClickListener del pulsante "In preparazione"
                    popupViewStato.findViewById(R.id.buttonInPreparazione).setOnClickListener(lambda2 ->
                    {
                        // comunico a OrdiniUpdaterThread di impostare lo stato dell'ordine a "in preparazione" sul server
                        updater.impostaStatoOrdine(IDOrdine, 0);

                        // chiudo tutte le window popup aperte
                        popupWindowStato.dismiss();
                        popupWindowMain.dismiss();

                        // avviso l'utente del successo dell'operazione
                        Toast.makeText(getContext(), "Stato impostato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
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
                        ApriSelezioneFattorino(IDOrdine, openedWindows);
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
                        Toast.makeText(getContext(), "Stato impostato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
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
                    ApriSelezioneFattorino(IDOrdine, openedWindows);
                });

                // onClickListener del pulsante "Elimina"
                popupViewMain.findViewById(R.id.buttonElimina).setOnClickListener(lambda6 ->
                {
                    // creo e mostro a schermo una window popup con il layout definito in card_ordine_popup_elimina.xml
                    // e le imposto il corretto ID dell'ordine
                    View popupViewElimina = getLayoutInflater().inflate(R.layout.card_ordine_popup_elimina, null);
                    ((TextView)popupViewElimina.findViewById(R.id.textViewID)).setText("Ordine #" + IDOrdine);
                    PopupWindow popupWindowElimina = new PopupWindow(popupViewElimina, -2, -2, true);
                    popupWindowElimina.showAtLocation(new View(getContext()), Gravity.CENTER, 0, 0);

                    // onClickListener del pulsante "Si" (conferma dell'eliminazione)
                    popupViewElimina.findViewById(R.id.buttonSi).setOnClickListener(lambda7 ->
                    {
                        // comunico a OrdiniUpdaterThread di eliminare l'ordine sul server
                        updater.eliminaOrdine(IDOrdine);

                        // chiudo tutte le window popup aperte
                        popupWindowElimina.dismiss();
                        popupWindowMain.dismiss();

                        // avviso l'utente del successo dell'operazione
                        Toast.makeText(getContext(), "Ordine eliminato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
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
    }

    void ApriSelezioneFattorino(String IDOrdine, ArrayList<PopupWindow> openedWindows)
    {
        // creo e mostro a schermo una window popup con il layout definito in card_ordine_popup_seleziona_fattorino.xml
        // e le imposto il corretto ID dell'ordine
        View popupViewSelezionaFattorino = getLayoutInflater().inflate(R.layout.card_ordine_popup_seleziona_fattorino, null);
        ((TextView)popupViewSelezionaFattorino.findViewById(R.id.textViewID)).setText("Ordine #" + IDOrdine);
        PopupWindow popupWindowSelezionaFattorino = new PopupWindow(popupViewSelezionaFattorino, -2, -2, true);
        popupWindowSelezionaFattorino.showAtLocation(new View(getContext()), Gravity.CENTER, 0, 0);

        // prendo dalla MainActivity la lista di tutti i fattorini presenti nel server
        ArrayList<Utente> fattorini = ((MainActivity)getActivity()).fattorini;

        // creo un'ArrayList di stringhe con solo i nomi dei fattorini
        ArrayList<String> fattoriniString = new ArrayList<>();
        fattoriniString.add("---"); // primo item dello spinner, equivale a "nessun fattorino selezionato"
        for (int j = 0; j < fattorini.size(); j++)
        {
            fattoriniString.add(fattorini.get(j).nome);
        }

        // imposto i valori selezionabili dallo spinner, usando l'ArrayList appena creata
        Spinner spinner = popupViewSelezionaFattorino.findViewById(R.id.spinnerFattorini);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, fattoriniString);
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

                // comunico a OrdiniUpdaterThread di impostare lo stato dell'ordine a "consegnato" sul server
                updater.impostaStatoOrdine(IDOrdine, 1);

                // comunico a OrdiniUpdaterThread di impostare il fattorino selezionato all'ordine sul server
                updater.impostaFattorinoOrdine(IDOrdine, userID);

                // chiudo tutte le window popup aperte
                popupWindowSelezionaFattorino.dismiss();
                for (int i = 0; i < openedWindows.size(); i++)
                {
                    openedWindows.get(i).dismiss();
                }

                // avviso l'utente del successo dell'operazione
                Toast.makeText(getContext(), "Fattorino assegnato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
            }
            else // se l'utente non ha selezionato un fattorino (ossia ha selezionato "---")...
            {
                // avviso l'utente dell'errore
                Toast.makeText(getContext(), "Seleziona un fattorino", Toast.LENGTH_LONG).show();
            }
        });
    }
}