package com.example.kebapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FireStoreController {
    private final String TAG = "FireStoreHandler";
    private FirebaseFirestore database;

    public FireStoreController() {
        database = FirebaseFirestore.getInstance();
    }

    public ArrayList<Prodotto> getProdotti()
    {
        ArrayList<Prodotto> result = new ArrayList<>();

        database.collection("prodotti").get().addOnCompleteListener(
            new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    Map<String, Object> data;

                    String nome, descrizione;
                    double prezzo;

                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            data = document.getData();

                            nome = (String)data.get("nome");
                            descrizione = (String)data.get("descrizione");

                            try
                            {
                                prezzo = new Double((long)data.get("prezzo"));
                            }
                            catch (Exception e)
                            {
                                prezzo = (double)data.get("prezzo");
                            }
                            result.add(new Prodotto(nome,prezzo,0,descrizione));
                        }
                    }
                    else
                    {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });

        return result;
    }

    public ArrayList<Ingrediente> getIngredienti()
    {
        ArrayList<Ingrediente> result = new ArrayList<>();

        database.collection("ingredienti").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        Map<String, Object> data;

                        String nome;
                        double prezzo;

                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                data = document.getData();

                                nome = (String)data.get("nome");

                                try
                                {
                                    prezzo = new Double((long)data.get("prezzo"));
                                }
                                catch (Exception e)
                                {
                                    prezzo = (double)data.get("prezzo");
                                }


                                result.add(new Ingrediente(nome,prezzo));
                            }
                        }
                        else
                        {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return result;
    }

    synchronized public ArrayList<Ordine> getOrdini()
    {
        ArrayList<Ordine> result = new ArrayList<>();

        database.collection("ordini").get().addOnCompleteListener(
            new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    Map<String, Object> data;

                    int status;
                    double totale;
                    String ID, nome, indirizzo, orarioRichiesto, orarioInserito, note, numero, IDFattorino;
                    ArrayList<Prodotto> prodotti;

                    ArrayList<Map<String, Object>> prodottiArray;

                    String nomeProdotto;
                    int quantitaProdotto;
                    ArrayList<String> aggiunteProdottoString;


                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            data = document.getData();
                            ID = document.getId();

                            status = (int)(long)data.get("status");
                            indirizzo = (String)data.get("indirizzo");
                            nome = (String)data.get("nome");
                            note = (String)data.get("note");
                            numero = (String)data.get("numero");
                            orarioRichiesto = (String)data.get("orario_richiesto");
                            orarioInserito = ((Timestamp)data.get("orario_inserito")).toString();
                            IDFattorino = (String)data.get("IDfattorino");

                            try
                            {
                                totale = new Double((long)data.get("totale"));
                            }
                            catch (Exception e)
                            {
                                totale = (double)data.get("totale");
                            }

                            prodotti = new ArrayList<>();
                            prodottiArray = (ArrayList<Map<String, Object>>)data.get("prodotti");

                            for (int i = 0; i < prodottiArray.size(); i++)
                            {
                                nomeProdotto = (String)prodottiArray.get(i).get("nome");
                                quantitaProdotto = (int)(long)prodottiArray.get(i).get("quantita");
                                aggiunteProdottoString = (ArrayList<String>)prodottiArray.get(i).get("aggiunte");

                                prodotti.add(new Prodotto(nomeProdotto, quantitaProdotto, aggiunteProdottoString));
                            }

                            result.add(new Ordine(ID, nome, indirizzo, orarioRichiesto, orarioInserito, note, numero, IDFattorino, prodotti, totale, status));
                        }
                    }
                    else
                    {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });

        return result;
    }

    public void putOrdine(Ordine ordine)
    {
        Map<String, Object> ordineMap = new HashMap<>();
        ordineMap.put("indirizzo", ordine.indirizzo);
        ordineMap.put("nome", ordine.nome);
        ordineMap.put("note", ordine.note);
        ordineMap.put("numero", ordine.numero);
        ordineMap.put("orario_inserito", new Timestamp(new Date()));
        ordineMap.put("orario_richiesto", ordine.orarioRichiesto);
        ordineMap.put("status", 0);
        ordineMap.put("totale", ordine.totale);
        ordineMap.put("IDfattorino", "");

        ArrayList<Map> prodottiList = new ArrayList<>();

        Prodotto currentProdotto;
        Map<String, Object> prodottoMap;
        for (int i = 0; i < ordine.prodotti.size(); i++)
        {
            currentProdotto = ordine.prodotti.get(i);

            prodottoMap = new HashMap<>();
            prodottoMap.put("nome", currentProdotto.nome);
            prodottoMap.put("quantita", currentProdotto.quantita);
            prodottoMap.put("aggiunte", currentProdotto.getAggiunteString());

            prodottiList.add(prodottoMap);
        }

        ordineMap.put("prodotti", prodottiList);

        database.collection("ordini").add(ordineMap);
    }

    public void deleteOrdine(String ID)
    {
        database.collection("ordini").document(ID).delete();
    }

    public void setOrdineStatus(String ID, int status)
    {
        database.collection("ordini").document(ID).update("status", status);
    }

    public void setFattorinoOrdine(String ID, String UserID)
    {
        database.collection("ordini").document(ID).update("IDfattorino", UserID);

    }

    public ArrayList<Utente> getUtente(String userID, String email)
    {
        ArrayList<Utente> result = new ArrayList<>();

        database.collection("utenti").document(userID).get().addOnCompleteListener(
            new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    Map<String, Object> data = task.getResult().getData();
                    result.add(new Utente(userID, (String)data.get("nome"), email, (String)data.get("numero"), (boolean)data.get("fattorino")));
                }
            });

        return result;
    }

    public ArrayList<Utente> getFattorini()
    {
        ArrayList<Utente> result = new ArrayList<>();

        database.collection("utenti").whereEqualTo("fattorino", true). get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        String UserID, nome;

                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                UserID = document.getId();
                                nome = (String)document.getData().get("nome");

                                result.add(new Utente(UserID, nome));
                            }
                        }
                        else
                        {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return result;
    }
}
