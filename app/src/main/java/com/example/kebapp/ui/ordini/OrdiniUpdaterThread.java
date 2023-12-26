package com.example.kebapp.ui.ordini;

import com.example.kebapp.FireStoreHandler;
import com.example.kebapp.Ordine;

import java.util.ArrayList;

public class OrdiniUpdaterThread extends Thread
{
    private static final String TAG = "OrdiniUpdaterThread";
    public static final int REFRESH_RATE = 3000;

    private FireStoreHandler database;

    ArrayList<Ordine> updatedOrdini;

    OrdiniUpdaterThread()
    {
        updatedOrdini = new ArrayList<>();
        database = new FireStoreHandler();
    }
    @Override
    synchronized public void run()
    {
        while(true)
        {
            try
            {
                wait(REFRESH_RATE);
                updatedOrdini = database.getOrdini();
            }
            catch (Exception e)
            {
                throw new RuntimeException();
            }
        }
    }

    public void eliminaOrdine(String ID)
    {
        database.deleteOrdine(ID);
    }

    public void impostaStatoOrdine(String ID, int status)
    {
        database.setOrdineStatus(ID, status);
    }
}