package com.example.kebapp.ui.ordini;

import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kebapp.MainActivity;
import com.example.kebapp.Ordine;
import com.example.kebapp.R;

import java.util.ArrayList;

public class OrdiniFragment extends Fragment
{
    private final String TAG = "OrdiniFragment";
    private OrdiniUpdaterThread updater;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        View buttonAggiorna = getView().findViewById(R.id.buttonAggiorna);
        View gifLoading = getView().findViewById(R.id.gifLoading);

        if(((MainActivity)getActivity()).coldStart)
        {
            ((MainActivity)getActivity()).coldStart = false;
            buttonAggiorna.postDelayed(new Runnable()
            {
                public void run() {
                    gifLoading.setVisibility(View.GONE);
                    buttonAggiorna.setVisibility(View.VISIBLE);
                    try
                    {
                        RefreshOrdini(updater.updatedOrdini);
                    }
                    catch(Exception e){}
                }
            }, updater.REFRESH_RATE+500);
        }
        else
        {
            gifLoading.setVisibility(View.GONE);
            buttonAggiorna.setVisibility(View.VISIBLE);
            RefreshOrdini(updater.updatedOrdini);
        }

        buttonAggiorna.setOnClickListener(item ->
        {
            RefreshOrdini(updater.updatedOrdini);
        });
    }

    synchronized void RefreshOrdini(ArrayList<Ordine> ordini)
    {
        LinearLayout layout = getView().findViewById(R.id.cards);
        layout.removeAllViews();

        CardView newCard;
        TextView newTextView;
        String newText;

        for (int i=0; i < ordini.size(); i++)
        {
            final String ID = ordini.get(i).ID;
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
            newText = "";
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

            newCard.setOnClickListener(item ->
                {
                    View popupViewMain = getLayoutInflater().inflate(R.layout.card_ordine_popup_main, null);
                    ((TextView)popupViewMain.findViewById(R.id.textViewID)).setText("Ordine #" + ID);
                    PopupWindow popupWindowMain = new PopupWindow(popupViewMain, -2, -2, true);

                    popupViewMain.findViewById(R.id.buttonImpostaStato).setOnClickListener(lambda1 ->
                    {
                        View popupViewStato = getLayoutInflater().inflate(R.layout.card_ordine_popup_imposta_stato, null);
                        ((TextView)popupViewStato.findViewById(R.id.textViewID)).setText("Ordine #" + ID);
                        PopupWindow popupWindowStato = new PopupWindow(popupViewStato, -2, -2, true);

                        popupViewStato.findViewById(R.id.buttonInPreparazione).setOnClickListener(lambda2 ->
                        {
                            updater.impostaStatoOrdine(ID, 0);
                            popupWindowStato.dismiss();
                            popupWindowMain.dismiss();
                            Toast.makeText(getContext(), "Stato impostato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
                        });

                        popupViewStato.findViewById(R.id.buttonInConsegna).setOnClickListener(lambda3 ->
                        {
                            updater.impostaStatoOrdine(ID, 1);
                            // TODO seleziona fattorino

                            popupWindowStato.dismiss();
                            popupWindowMain.dismiss();
                            Toast.makeText(getContext(), "Stato impostato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
                        });

                        popupViewStato.findViewById(R.id.buttonConsegnato).setOnClickListener(lambda4 ->
                        {
                            updater.impostaStatoOrdine(ID, 2);
                            popupWindowStato.dismiss();
                            popupWindowMain.dismiss();
                            Toast.makeText(getContext(), "Stato impostato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
                        });
                        popupWindowStato.showAtLocation(new View(getContext()), Gravity.CENTER, 0, 0);
                    });

                    popupViewMain.findViewById(R.id.buttonAssegnaFattorino).setOnClickListener(lambda5 ->
                    {
                        //TODO assegna a fattorino su Firebase
                    });

                    popupViewMain.findViewById(R.id.buttonElimina).setOnClickListener(lambda6 ->
                    {
                        View popupViewElimina = getLayoutInflater().inflate(R.layout.card_ordine_popup_elimina, null);
                        ((TextView)popupViewElimina.findViewById(R.id.textViewID)).setText("Ordine #" + ID);
                        PopupWindow popupWindowElimina = new PopupWindow(popupViewElimina, -2, -2, true);

                        popupViewElimina.findViewById(R.id.buttonSi).setOnClickListener(lambda2 ->
                        {
                            updater.eliminaOrdine(ID);
                            popupWindowElimina.dismiss();
                            popupWindowMain.dismiss();
                            Toast.makeText(getContext(), "Ordine eliminato, aggiorna la pagina per visualizzare le modfiche", Toast.LENGTH_LONG).show();
                        });

                        popupViewElimina.findViewById(R.id.buttonNo).setOnClickListener(lambda3 ->
                        {
                            popupWindowElimina.dismiss();
                        });

                        popupWindowElimina.showAtLocation(new View(getContext()), Gravity.CENTER, 0, 0);
                    });

                    popupWindowMain.showAtLocation(new View(getContext()), Gravity.CENTER, 0, 0);
                    getActivity().findViewById(R.id.greyout).setVisibility(View.VISIBLE);
                    popupWindowMain.setOnDismissListener(new PopupWindow.OnDismissListener()
                    {
                        @Override
                        public void onDismiss()
                        {
                            getActivity().findViewById(R.id.greyout).setVisibility(View.INVISIBLE);
                        }
                    });
                });

            layout.addView(newCard);
        }
    }
}