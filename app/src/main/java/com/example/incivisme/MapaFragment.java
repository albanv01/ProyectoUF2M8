package com.example.incivisme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    // below are the latitude and longitude
    // of 4 different locations.
    LatLng punto1 = new LatLng(41.389996599694406, 2.1352847162868778);
    LatLng punto2 = new LatLng(41.3978520048405, 2.1483309816120335);
    LatLng punto3 = new LatLng(41.362752884556315, 2.1395762509333105);
    LatLng punto4 = new LatLng(41.38479472864416, 2.1645560329064213);
    LatLng punto5 = new LatLng(41.38119745632779, 2.13339243922842);
    LatLng punto6 = new LatLng(41.37580117473925, 2.047093256735496);
    LatLng punto7 = new LatLng(41.36980478119577, 2.0570815880425473);
    LatLng punto8 = new LatLng(41.36800575534873, 2.0774577839089323);
    LatLng punto9 = new LatLng(41.43079228248801, 2.1593621006267543);
    LatLng punto10 = new LatLng(41.73142475940853, 1.8268225202151263);
    LatLng punto11 = new LatLng(41.41704013932239, 2.0161956088627764);

    private ArrayList<LatLng> locationArrayList;
    private Object BitmapDescriptor;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // in below line we are initializing our array list.
        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.
        locationArrayList.add(punto1);
        locationArrayList.add(punto2);
        locationArrayList.add(punto3);
        locationArrayList.add(punto4);
        locationArrayList.add(punto5);
        locationArrayList.add(punto6);
        locationArrayList.add(punto7);
        locationArrayList.add(punto8);
        locationArrayList.add(punto9);
        locationArrayList.add(punto10);
        locationArrayList.add(punto11);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference base = FirebaseDatabase.getInstance("https://incivisme-c4af7-default-rtdb.firebaseio.com/").getReference();

        DatabaseReference users = base.child("users");
        DatabaseReference uid = users.child(auth.getUid());
        DatabaseReference incidencies = uid.child("opinions");

        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);


        mapFragment.getMapAsync(map -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return ;
            }
            map.setMyLocationEnabled(true);
            MutableLiveData<LatLng> currentLatLng = model.getCurrentLatLng();
            LifecycleOwner owner = getViewLifecycleOwner();
            currentLatLng.observe(owner, latLng -> {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                map.animateCamera(cameraUpdate);
                currentLatLng.removeObservers(owner);
            });

            incidencies.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Opinion opinion = dataSnapshot.getValue(Opinion.class);

                    LatLng aux = new LatLng(
                            Double.parseDouble(opinion.getNombre()),
                            Double.parseDouble(opinion.getDireccio())
                    );

                    map.addMarker(new MarkerOptions()
                            .position(aux)
                            .title(opinion.getProblema())
                            .icon(BitmapDescriptorFactory.defaultMarker
                                    (BitmapDescriptorFactory.HUE_GREEN))
                            .snippet(opinion.getDireccio())).showInfoWindow();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        });

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < locationArrayList.size(); i++) {

            // below line is use to add marker to each location of our array list.
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Punt de Recollida").icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_delete_outline_24)));

            // below lin is use to zoom our camera on map.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    }





