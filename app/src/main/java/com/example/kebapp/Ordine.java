package com.example.kebapp;

import java.util.ArrayList;

public class Ordine
{
    public enum Status
    {
        CANCELLATO,
        IN_PREPARAZIONE,
        IN_CONSEGNA,
        CONSEGNATO
    }

    public int ID;
    public String nome;
    public String indirizzo;
    public String orarioRichiesto;
    public String orarioInvio;
    public ArrayList<Prodotto> prodotti;
    public double totale;
    public boolean pagato;
    public Status status;

    public Ordine( int ID, String nome, String indirizzo, String orarioRichiesto, String orarioInvio, ArrayList<Prodotto> prodotti, double totale, boolean pagato, Status status)
    {
        this.ID = ID;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orarioRichiesto = orarioRichiesto;
        this.orarioInvio = orarioInvio;
        this.prodotti = prodotti;
        this.totale = totale;
        this.pagato = pagato;
        this.status = status;
    }
}
