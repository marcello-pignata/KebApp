package com.example.kebapp;

import java.util.ArrayList;

public class Ordine
{
    public Ordine() {}

    public String ID;

    public String nome;
    public String indirizzo;
    public String orarioRichiesto;
    public String orarioInserito;
    public String note;
    public String numero;
    public String IDFattorino;
    public ArrayList<Prodotto> prodotti;
    public double totale;
    public int status;

    public Ordine(
            String ID,
            String nome,
            String indirizzo,
            String orarioRichiesto,
            String orarioInserito,
            String note,
            String numero,
            String IDFattorino,
            ArrayList<Prodotto> prodotti,
            double totale,
            int status)
    {
        this.ID = ID;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orarioRichiesto = orarioRichiesto;
        this.orarioInserito = orarioInserito;
        this.note = note;
        this.numero = numero;
        this.IDFattorino = IDFattorino;
        this.prodotti = prodotti;
        this.totale = totale;
        this.status = status;
    }

    public Ordine(
            String nome,
            String indirizzo,
            String orarioRichiesto,
            String note,
            String numero,
            ArrayList<Prodotto> prodotti,
            double totale
    )
    {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orarioRichiesto = orarioRichiesto;
        this.note = note;
        this.numero = numero;
        this.prodotti = prodotti;
        this.totale = totale;
    }
}
