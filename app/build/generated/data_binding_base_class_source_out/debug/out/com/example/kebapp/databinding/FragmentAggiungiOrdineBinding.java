// Generated by view binder compiler. Do not edit!
package com.example.kebapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.kebapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentAggiungiOrdineBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button buttonAggiungiOrdine;

  @NonNull
  public final Button buttonAggiungiProdotto;

  @NonNull
  public final ConstraintLayout constraintLayoutAggiungiOrdine;

  @NonNull
  public final LinearLayout linearLayoutAggiungiOrdine;

  @NonNull
  public final LinearLayout linearLayoutProdotti;

  @NonNull
  public final RecyclerView recyclerViewProdotti;

  @NonNull
  public final ScrollView scrollViewAggiungiOrdine;

  @NonNull
  public final TextInputEditText textInputIndirizzo;

  @NonNull
  public final TextInputLayout textInputLayoutIndirizzo;

  @NonNull
  public final TextInputLayout textInputLayoutNome;

  @NonNull
  public final TextInputLayout textInputLayoutNumero;

  @NonNull
  public final TextInputEditText textInputNome;

  @NonNull
  public final TextInputEditText textInputNumero;

  @NonNull
  public final TextView textViewIndirizzo;

  @NonNull
  public final TextView textViewNome;

  @NonNull
  public final TextView textViewNumero;

  @NonNull
  public final TextView textViewProdotti;

  @NonNull
  public final TextView textViewTotale;

  private FragmentAggiungiOrdineBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button buttonAggiungiOrdine, @NonNull Button buttonAggiungiProdotto,
      @NonNull ConstraintLayout constraintLayoutAggiungiOrdine,
      @NonNull LinearLayout linearLayoutAggiungiOrdine, @NonNull LinearLayout linearLayoutProdotti,
      @NonNull RecyclerView recyclerViewProdotti, @NonNull ScrollView scrollViewAggiungiOrdine,
      @NonNull TextInputEditText textInputIndirizzo,
      @NonNull TextInputLayout textInputLayoutIndirizzo,
      @NonNull TextInputLayout textInputLayoutNome, @NonNull TextInputLayout textInputLayoutNumero,
      @NonNull TextInputEditText textInputNome, @NonNull TextInputEditText textInputNumero,
      @NonNull TextView textViewIndirizzo, @NonNull TextView textViewNome,
      @NonNull TextView textViewNumero, @NonNull TextView textViewProdotti,
      @NonNull TextView textViewTotale) {
    this.rootView = rootView;
    this.buttonAggiungiOrdine = buttonAggiungiOrdine;
    this.buttonAggiungiProdotto = buttonAggiungiProdotto;
    this.constraintLayoutAggiungiOrdine = constraintLayoutAggiungiOrdine;
    this.linearLayoutAggiungiOrdine = linearLayoutAggiungiOrdine;
    this.linearLayoutProdotti = linearLayoutProdotti;
    this.recyclerViewProdotti = recyclerViewProdotti;
    this.scrollViewAggiungiOrdine = scrollViewAggiungiOrdine;
    this.textInputIndirizzo = textInputIndirizzo;
    this.textInputLayoutIndirizzo = textInputLayoutIndirizzo;
    this.textInputLayoutNome = textInputLayoutNome;
    this.textInputLayoutNumero = textInputLayoutNumero;
    this.textInputNome = textInputNome;
    this.textInputNumero = textInputNumero;
    this.textViewIndirizzo = textViewIndirizzo;
    this.textViewNome = textViewNome;
    this.textViewNumero = textViewNumero;
    this.textViewProdotti = textViewProdotti;
    this.textViewTotale = textViewTotale;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentAggiungiOrdineBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentAggiungiOrdineBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_aggiungi_ordine, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentAggiungiOrdineBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonAggiungiOrdine;
      Button buttonAggiungiOrdine = ViewBindings.findChildViewById(rootView, id);
      if (buttonAggiungiOrdine == null) {
        break missingId;
      }

      id = R.id.buttonAggiungiProdotto;
      Button buttonAggiungiProdotto = ViewBindings.findChildViewById(rootView, id);
      if (buttonAggiungiProdotto == null) {
        break missingId;
      }

      ConstraintLayout constraintLayoutAggiungiOrdine = (ConstraintLayout) rootView;

      id = R.id.linearLayoutAggiungiOrdine;
      LinearLayout linearLayoutAggiungiOrdine = ViewBindings.findChildViewById(rootView, id);
      if (linearLayoutAggiungiOrdine == null) {
        break missingId;
      }

      id = R.id.linearLayoutProdotti;
      LinearLayout linearLayoutProdotti = ViewBindings.findChildViewById(rootView, id);
      if (linearLayoutProdotti == null) {
        break missingId;
      }

      id = R.id.recyclerViewProdotti;
      RecyclerView recyclerViewProdotti = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewProdotti == null) {
        break missingId;
      }

      id = R.id.scrollViewAggiungiOrdine;
      ScrollView scrollViewAggiungiOrdine = ViewBindings.findChildViewById(rootView, id);
      if (scrollViewAggiungiOrdine == null) {
        break missingId;
      }

      id = R.id.textInputIndirizzo;
      TextInputEditText textInputIndirizzo = ViewBindings.findChildViewById(rootView, id);
      if (textInputIndirizzo == null) {
        break missingId;
      }

      id = R.id.textInputLayoutIndirizzo;
      TextInputLayout textInputLayoutIndirizzo = ViewBindings.findChildViewById(rootView, id);
      if (textInputLayoutIndirizzo == null) {
        break missingId;
      }

      id = R.id.textInputLayoutNome;
      TextInputLayout textInputLayoutNome = ViewBindings.findChildViewById(rootView, id);
      if (textInputLayoutNome == null) {
        break missingId;
      }

      id = R.id.textInputLayoutNumero;
      TextInputLayout textInputLayoutNumero = ViewBindings.findChildViewById(rootView, id);
      if (textInputLayoutNumero == null) {
        break missingId;
      }

      id = R.id.textInputNome;
      TextInputEditText textInputNome = ViewBindings.findChildViewById(rootView, id);
      if (textInputNome == null) {
        break missingId;
      }

      id = R.id.textInputNumero;
      TextInputEditText textInputNumero = ViewBindings.findChildViewById(rootView, id);
      if (textInputNumero == null) {
        break missingId;
      }

      id = R.id.textViewIndirizzo;
      TextView textViewIndirizzo = ViewBindings.findChildViewById(rootView, id);
      if (textViewIndirizzo == null) {
        break missingId;
      }

      id = R.id.textViewNome;
      TextView textViewNome = ViewBindings.findChildViewById(rootView, id);
      if (textViewNome == null) {
        break missingId;
      }

      id = R.id.textViewNumero;
      TextView textViewNumero = ViewBindings.findChildViewById(rootView, id);
      if (textViewNumero == null) {
        break missingId;
      }

      id = R.id.textViewProdotti;
      TextView textViewProdotti = ViewBindings.findChildViewById(rootView, id);
      if (textViewProdotti == null) {
        break missingId;
      }

      id = R.id.textViewTotale;
      TextView textViewTotale = ViewBindings.findChildViewById(rootView, id);
      if (textViewTotale == null) {
        break missingId;
      }

      return new FragmentAggiungiOrdineBinding((ConstraintLayout) rootView, buttonAggiungiOrdine,
          buttonAggiungiProdotto, constraintLayoutAggiungiOrdine, linearLayoutAggiungiOrdine,
          linearLayoutProdotti, recyclerViewProdotti, scrollViewAggiungiOrdine, textInputIndirizzo,
          textInputLayoutIndirizzo, textInputLayoutNome, textInputLayoutNumero, textInputNome,
          textInputNumero, textViewIndirizzo, textViewNome, textViewNumero, textViewProdotti,
          textViewTotale);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
