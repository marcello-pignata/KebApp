package com.example.kebapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Prodotto implements Serializable {
    public String nome;
    public double prezzo;
    public int quantita;
    public String descrizione;

    public ArrayList<Ingrediente> aggiunte;

    public Prodotto(String nome, double prezzo, int quantita, String descrizione)
    {
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.descrizione = descrizione;
        aggiunte = new ArrayList<Ingrediente>();
    }

    public ArrayList<String> getAggiunteString()
    {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < aggiunte.size(); i++)
        {
            list.add(aggiunte.get(i).nome);
        }

        return list;
    }
}