package com.example.incivisme;

import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NotificarFragment extends Fragment {
    ProgressBar mLoading;
    private TextInputEditText txtNom;
    private TextInputEditText txtCognom;
    private TextInputEditText txtDireccio;
    private TextInputEditText txtValoracio;

    private Button buttonOpinio;
    private SharedViewModel model;


    public NotificarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificar, container, false);

        mLoading = view.findViewById(R.id.loading);

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        txtNom =  view.findViewById(R.id.txtNom);
        txtCognom = view.findViewById(R.id.txtCognom);
        txtDireccio = view.findViewById(R.id.txtDireccio);
        txtValoracio = view.findViewById(R.id.txtValoracio);
        buttonOpinio = view.findViewById(R.id.button_opinio);





        model.getCurrentAddress().observe(getViewLifecycleOwner(), address -> {
            txtDireccio.setText(getString(R.string.address_text,
                    address, System.currentTimeMillis()));
        });
        model.getCurrentLatLng().observe(getViewLifecycleOwner(), latlng -> {
            txtNom.setText(String.valueOf(latlng.latitude));
            txtCognom.setText(String.valueOf(latlng.longitude));
        });


        model.getProgressBar().observe(getViewLifecycleOwner(), visible -> {
            if(visible)
                mLoading.setVisibility(ProgressBar.VISIBLE);
            else
                mLoading.setVisibility(ProgressBar.INVISIBLE);
        });

        model.switchTrackingLocation();


        buttonOpinio.setOnClickListener(button -> {
            Opinion opinion = new Opinion();
            opinion.setDireccio(txtDireccio.getText().toString());
            opinion.setNombre(txtNom.getText().toString());
            opinion.setApellido(txtCognom.getText().toString());
            opinion.setOpinion(txtValoracio.getText().toString());

            FirebaseAuth auth = FirebaseAuth.getInstance();
            DatabaseReference base = FirebaseDatabase.getInstance("https://incivisme2-ae9db-default-rtdb.firebaseio.com/").getReference();



            DatabaseReference users = base.child("users");
            DatabaseReference uid = users.child(auth.getUid());
            DatabaseReference incidencies = uid.child("opinions");

            DatabaseReference reference = incidencies.push();
            reference.setValue(opinion);
        });

        return view;
    }




}
