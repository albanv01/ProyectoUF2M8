package com.example.incivisme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LlistarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llistar, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference base = FirebaseDatabase.getInstance("https://incivisme2-ae9db-default-rtdb.firebaseio.com/").getReference();

        DatabaseReference users = base.child("users");
        DatabaseReference uid = users.child(auth.getUid());
        DatabaseReference opinions = uid.child("opinions");


        FirebaseListOptions<Opinion> options = new FirebaseListOptions.Builder<Opinion>()
                .setQuery(opinions, Opinion.class)
                .setLayout(R.layout.list_opiniones)
                .setLifecycleOwner(this)
                .build();


        FirebaseListAdapter<Opinion> adapter = new FirebaseListAdapter<Opinion>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Opinion model, int position) {
                TextView txtNom = v.findViewById(R.id.txtNom);
                TextView txtDescripcio = v.findViewById(R.id.txtValoracio);
                TextView txtAdreca = v.findViewById(R.id.txtDireccio);

                txtNom.setText(model.getNombre());

                    txtDescripcio.setText(model.getOpinion());

                txtAdreca.setText(model.getDireccio());
            }


        };

        ListView lvIncidencies = view.findViewById(R.id.lvOpiniones);
        lvIncidencies.setAdapter(adapter);


        return view;
    }
}
