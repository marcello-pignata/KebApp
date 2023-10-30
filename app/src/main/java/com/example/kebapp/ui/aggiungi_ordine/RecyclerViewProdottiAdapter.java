package com.example.kebapp.ui.aggiungi_ordine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kebapp.R;

import java.util.List;

public class RecyclerViewProdottiAdapter extends RecyclerView.Adapter<RecyclerViewProdottiAdapter.ViewHolder>
{

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    RecyclerViewProdottiAdapter(Context context, List<String> data)
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
        String nome = mData.get(position);
        holder.myTextView.setText(nome);
    }

    // total number of rows
    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public void addItem(String item)
    {
        mData.add(item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView myTextView;

        ViewHolder(View itemView)
        {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textViewNome);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id)
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