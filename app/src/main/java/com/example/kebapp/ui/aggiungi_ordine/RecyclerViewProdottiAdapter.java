package com.example.kebapp.ui.aggiungi_ordine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewProdottiAdapter extends RecyclerView.Adapter<RecyclerViewProdottiAdapter.ViewHolder> implements Serializable
{

    private List<Prodotto> mData;
    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;
    ActivityResultLauncher<Intent> aggiunteLauncher;
    private final String TAG = "RecyclerViewProdottiAdapter";

    ArrayList<Ingrediente> listaIngredienti = new ArrayList<>();

    RecyclerViewProdottiAdapter(Context context, List<Prodotto> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.aggiunteLauncher = aggiunteLauncher;

        listaIngredienti.add(new Ingrediente("Doppio pomodoro", 1.));
        listaIngredienti.add(new Ingrediente("Doppia mozzarella", 1.));
        listaIngredienti.add(new Ingrediente("Mozzarella di bufala", 2.));
        listaIngredienti.add(new Ingrediente("Patatine", 1.));
        listaIngredienti.add(new Ingrediente("Salsiccia", 2.));
        listaIngredienti.add(new Ingrediente("Rucola", 0.5));
    }

    public void addLauncher (ActivityResultLauncher<Intent> aggiunteLauncher)
    {
        this.aggiunteLauncher = aggiunteLauncher;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.item_prodotto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Prodotto prodotto = mData.get(position);
        holder.textViewNome.setText(prodotto.nome);
        holder.textViewQuantita.setText("x" + prodotto.quantita);

        for (int i = 0; i < prodotto.aggiunte.size(); i++)
        {
            holder.textViewNome.setText(holder.textViewNome.getText() + "\n\t\t+ " + prodotto.aggiunte.get(i).nome);
        }
        holder.textViewNome.setText(holder.textViewNome.getText() + "\n");

        holder.buttonAggiungiIngredienti.setOnClickListener(item ->
        {
            Activity origin = (Activity)holder.itemView.getContext();

            Intent intent = new Intent(origin, SelezionaIngredientiActivity.class);
            intent.putExtra("listaAggiunte", prodotto.aggiunte);
            intent.putExtra("listaIngredienti", listaIngredienti);
            intent.putExtra("idProdotto", position);

            aggiunteLauncher.launch(intent);

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

        Button buttonAggiungiIngredienti;

        ViewHolder(View itemView)
        {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewQuantita = itemView.findViewById(R.id.textViewQuantita);
            buttonAggiungiIngredienti = itemView.findViewById(R.id.buttonAggiungiIngredienti);
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