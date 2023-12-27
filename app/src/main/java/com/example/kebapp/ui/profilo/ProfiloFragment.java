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
        String userID = ((MainActivity)getActivity()).user.get(0).userID;
        String nome = ((MainActivity)getActivity()).user.get(0).nome;
        String email = ((MainActivity)getActivity()).user.get(0).email;
        boolean fattorino = ((MainActivity)getActivity()).user.get(0).fattorino;

        ((TextView)getView().findViewById(R.id.textViewUserID)).setText(userID);
        ((TextView)getView().findViewById(R.id.textViewNomeUtente)).setText(nome);
        ((TextView)getView().findViewById(R.id.textViewEmailUtente)).setText(email);
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