package com.example.kebapp;

public class Utente
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
}
