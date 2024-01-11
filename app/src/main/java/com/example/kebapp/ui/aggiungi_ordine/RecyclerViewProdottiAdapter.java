package com.example.kebapp.ui.aggiungi_ordine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kebapp.Ingrediente;
import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewProdottiAdapter extends RecyclerView.Adapter<RecyclerViewProdottiAdapter.ViewHolder> implements Serializable
{

    private List<Prodotto> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ActivityResultLauncher<Intent> aggiunteLauncher;

    // tag usato per debugging
    private final String TAG = "RecyclerViewProdottiAdapter";

    // lista di tutti gli ingredienti disponibili
    private ArrayList<Ingrediente> listaIngredienti = new ArrayList<>();

    RecyclerViewProdottiAdapter(Context context)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
    }

    // metodo get per la lista di tutti i prodotti inseriti
    public List<Prodotto> getmData()
    {
        return this.mData;
    }

    // metodo usato per calcolare il prezzo totale di tutti i prodotti con le relative aggiunte
    public double getTotale()
    {
        double totale = 0;

        for (int i = 0; i < mData.size(); i++)
        {
            totale += mData.get(i).prezzo * mData.get(i).quantita;

            for (int j = 0; j < mData.get(i).aggiunte.size(); j++)
            {
                totale += mData.get(i).aggiunte.get(j).prezzo * mData.get(i).quantita;
            }
        }

        return totale;
    }

    public void clear()
    {
        int size = mData.size();
        mData.clear();
        notifyItemRangeChanged(0, size);
    }

    // metodo usato per ottenere l'ActivityResultLauncher creato da AggiungiOrdineFragment
    public void addLauncher (ActivityResultLauncher<Intent> aggiunteLauncher)
    {
        this.aggiunteLauncher = aggiunteLauncher;
    }

    // metodo usato per ottenere l'elenco degli ingredienti scaricato da AggiungiOrdineFragment
    public void addListaIngredienti (ArrayList<Ingrediente> listaIngredienti)
    {
        this.listaIngredienti = listaIngredienti;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.item_prodotto, parent, false);
        return new ViewHolder(view);
    }

    // aggiunta di un nuovo prodotto nella recycler view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // ottengo il prodotto da aggiungere
        Prodotto prodotto = mData.get(position);

        //calcolo il prezzo del prodotto + le relative aggiunte
        double prezzoProdotto = prodotto.prezzo;
        for (int i = 0; i < prodotto.aggiunte.size(); i++)
        {
            prezzoProdotto += prodotto.aggiunte.get(i).prezzo;
        }

        // imposto le textview per il nome e la quantità con i corrispettivi dati
        NumberFormat formatter = new DecimalFormat("#0.00");

        holder.textViewNome.setText(prodotto.nome);
        holder.textViewPrezzoProdotto.setText(formatter.format(prezzoProdotto) + "€");
        holder.textViewQuantita.setText("x" + prodotto.quantita);

        // se sono presenti aggiunte le scrivo una per una nella textViewNome
        for (int i = 0; i < prodotto.aggiunte.size(); i++)
        {
            holder.textViewNome.setText(holder.textViewNome.getText() + "\n\t\t+ " + prodotto.aggiunte.get(i).nome);
        }
        holder.textViewNome.setText(holder.textViewNome.getText() + "\n");

        // setOnClickListener del pulsante per l'aggiunta di ingredienti
        holder.buttonAggiungiIngredienti.setOnClickListener(item ->
        {
            // faccio partire SelezionaIngredienteActivity passandogli:
            //      - la lista delle aggiunte già presenti
            //      - la lista di tutti gli ingredienti
            //      - l'ID del prodotto che stiamo modificando
            Activity origin = (Activity)holder.itemView.getContext();
            Intent intent = new Intent(origin, SelezionaIngredientiActivity.class);
            intent.putExtra("listaAggiunte", prodotto.aggiunte);
            intent.putExtra("listaIngredienti", listaIngredienti);
            intent.putExtra("idProdotto", position);
            aggiunteLauncher.launch(intent);

        });

        // setOnClickListener del pulsante per la rimozione del prodotto
        holder.buttonRimuoviProdotto.setOnClickListener(item ->
        {
            // rimuovo il prodotto dall'ArrayList e dalla recycler view
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
        });
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public void addItem(Prodotto item)
    {
        mData.add(item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable
    {
        TextView textViewNome;
        TextView textViewQuantita;
        TextView textViewPrezzoProdotto;
        Button buttonAggiungiIngredienti;
        Button buttonRimuoviProdotto;

        ViewHolder(View itemView)
        {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNomeUtente);
            textViewQuantita = itemView.findViewById(R.id.textViewQuantita);
            textViewPrezzoProdotto = itemView.findViewById(R.id.textViewPrezzoProdotto);
            buttonAggiungiIngredienti = itemView.findViewById(R.id.buttonAggiungiIngredienti);
            buttonRimuoviProdotto = itemView.findViewById(R.id.buttonRimuoviProdotto);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Prodotto getItem(int id)
    {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}