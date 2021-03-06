package com.example.incivisme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    FragmentManager fm = getSupportFragmentManager();

    final Fragment fragment1 = new NotificarFragment();
    final Fragment fragment2 = new LlistarFragment();
    final Fragment fragment3 = new MapaFragment();
    FusedLocationProviderClient mFusedLocationClient;

    Fragment active = fragment1;

    private SharedViewModel model;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                return true;
            case R.id.navigation_llistat:
                fm.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                return true;
            case R.id.navigation_mapa:
                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction()
                .add(R.id.fragment_seleccionat, fragment1, "1")
                .hide(fragment2)
                .commit();

        fm.beginTransaction()
                .add(R.id.fragment_seleccionat, fragment2, "2")
                .hide(fragment2)
                .commit();

        fm.beginTransaction()
                .add(R.id.fragment_seleccionat, fragment3, "3")
                .hide(fragment3)
                .commit();

        nav.setSelectedItemId(R.id.navigation_home);

        model = ViewModelProviders.of(this).get(SharedViewModel.class);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        model.setFusedLocationClient(mFusedLocationClient);

        model.getCheckPermission().observe(this, s -> checkPermission());
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            // Choose authentication providers
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    )
                            )
                            .build(),
                    0);
        }
    }

    void checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            model.startTrackingLocation(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // Si es concedeix perm??s, obt?? la ubicaci??,
                // d'una altra manera, mostra un Toast

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    model.startTrackingLocation(false);
                } else {
                    Toast.makeText(this,
                            "Perm??s denegat",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}