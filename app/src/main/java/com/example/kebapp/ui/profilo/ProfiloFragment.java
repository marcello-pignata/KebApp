package com.example.kebapp.ui.profilo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        getView().findViewById(R.id.buttonProfilo).setOnClickListener(item ->
        {
            Log.d(TAG, "click");
        });
    }
}