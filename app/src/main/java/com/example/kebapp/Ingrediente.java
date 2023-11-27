package com.example.kebapp;

import java.io.Serializable;

public class Ingrediente  implements Serializable
{
    public String nome;

    public double prezzo;

    public Ingrediente(String nome, double prezzo)
    {
        this.nome = nome;
        this.prezzo = prezzo;
    }
}
