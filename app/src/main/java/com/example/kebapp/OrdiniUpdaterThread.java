package com.example.kebapp;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class OrdiniUpdaterThread extends Thread
{
    private static final String TAG = "OrdiniUpdaterThread";
    public static final int REFRESH_RATE = 3000;

    private FireStoreController database;
    public ArrayList<Ordine> updatedOrdini;

    private MainActivity mainActivity;

    public OrdiniUpdaterThread(MainActivity mainActivity)
    {
        updatedOrdini = new ArrayList<>();
        database = new FireStoreController();
        this.mainActivity = mainActivity;
    }
    @Override
    synchronized public void run()
    {
        while(true)
        {
            try
            {
                wait(REFRESH_RATE);

                if(mainActivity.isNetworkAvailable())
                {
                    try
                    {
                        FileOutputStream fos = mainActivity.openFileOutput("ordini.ser", mainActivity.MODE_PRIVATE);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(updatedOrdini);
                        os.close();
                        fos.close();
                    }
                    catch (IOException i)
                    {
                        i.printStackTrace();
                    }

                    updatedOrdini = database.getOrdini();
                }
                else
                {
                    try
                    {
                        FileInputStream fis = mainActivity.openFileInput("ordini.ser");
                        ObjectInputStream is = new ObjectInputStream(fis);
                        updatedOrdini = (ArrayList<Ordine>) is.readObject();
                        is.close();
                        fis.close();
                    }
                    catch (IOException i)
                    {
                        i.printStackTrace();
                        return;
                    }
                }
            }
            catch (Exception e){}
        }
    }
    public void eliminaOrdine(String ID)
    {
        database.deleteOrdine(ID);
    }

    public void impostaStatoOrdine(String ID, int status)
    {
        database.setOrdineStatus(ID, status);

        if(status == 0)
        {
            database.setOrdineFattorino(ID, "");
        }
    }

    public void impostaFattorinoOrdine(String ID, String UserID)
    {
        database.setOrdineFattorino(ID, UserID);
    }

    public void impostaPioggiaOrdine(String ID, boolean pioggia)
    {
        database.SetOrdinePioggia(ID, pioggia);
    }
}