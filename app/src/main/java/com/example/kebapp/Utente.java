package com.example.kebapp;

import java.io.Serializable;

public class Utente implements Serializable
{
    public String userID, nome, email;
    public boolean fattorino;

    Utente(String userID, String nome, String email, boolean fattorino)
    {
        this.userID = userID;
        this.nome = nome;
        this.email = email;
        this.fattorino = fattorino;
    }

    Utente(String userID, String nome)
    {
        this.userID = userID;
        this.nome = nome;
    }
}
