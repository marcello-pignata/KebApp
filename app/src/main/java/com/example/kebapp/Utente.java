package com.example.kebapp;

import java.io.Serializable;

public class Utente implements Serializable
{
    public String userID, nome, email, numero;
    public boolean fattorino;

    Utente(String userID, String nome, String email, String numero, boolean fattorino)
    {
        this.userID = userID;
        this.nome = nome;
        this.email = email;
        this.fattorino = fattorino;
        this.numero = numero;
    }

    Utente(String userID, String nome)
    {
        this.userID = userID;
        this.nome = nome;
    }
}
