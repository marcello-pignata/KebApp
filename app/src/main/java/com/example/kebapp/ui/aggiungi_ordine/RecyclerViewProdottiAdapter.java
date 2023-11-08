package com.example.kebapp.ui.aggiungi_ordine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kebapp.Prodotto;
import com.example.kebapp.R;

import java.util.List;

public class RecyclerViewProdottiAdapter extends RecyclerView.Adapter<RecyclerViewProdottiAdapter.ViewHolder>
{

    private List<Prodotto> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    RecyclerViewProdottiAdapter(Context context, List<Prodotto> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
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
    }

    // total number of rows
    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public void addItem(Prodotto item)
    {
        mData.add(item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textViewNome;
        TextView textViewQuantita;

        ViewHolder(View itemView)
        {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewQuantita = itemView.findViewById(R.id.textViewQuantita);
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