package com.example.kebapp;

import java.io.Serializable;

public class Prodotto implements Serializable {
    public String nome;
    public double prezzo;
    public int quantita;
    public String descrizione;

    public Prodotto(String nome, double prezzo, int quantita, String descrizione)
    {
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.descrizione = descrizione;
    }
}