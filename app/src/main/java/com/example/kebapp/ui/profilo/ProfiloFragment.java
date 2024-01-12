package com.example.kebapp.ui.profilo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kebapp.LoginActivity;
import com.example.kebapp.MainActivity;
import com.example.kebapp.R;

public class ProfiloFragment extends Fragment
{
    private final String TAG = "ProfiloFragment";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profilo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        String userID = ((MainActivity)getActivity()).utente.get(0).userID;
        String nome = ((MainActivity)getActivity()).utente.get(0).nome;
        String email = ((MainActivity)getActivity()).utente.get(0).email;
        String numero = ((MainActivity)getActivity()).utente.get(0).numero;
        boolean fattorino = ((MainActivity)getActivity()).utente.get(0).fattorino;

        ((TextView)getView().findViewById(R.id.textViewID)).setText(userID);
        ((TextView)getView().findViewById(R.id.textViewNomeUtente)).setText(nome);
        ((TextView)getView().findViewById(R.id.textViewEmailUtente)).setText(email);
        ((TextView)getView().findViewById(R.id.textViewNumeroTelefono)).setText(numero);
        ((CheckBox)getView().findViewById(R.id.checkBoxFattorino)).setChecked(fattorino);

        getView().findViewById(R.id.buttonLogout).setOnClickListener(item ->
        {
            ((MainActivity)getActivity()).authenticator.signOut();

            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}