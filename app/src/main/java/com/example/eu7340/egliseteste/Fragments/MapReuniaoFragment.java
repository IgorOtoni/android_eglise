package com.example.eu7340.egliseteste.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eu7340.egliseteste.Models.Reuniao;
import com.example.eu7340.egliseteste.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class MapReuniaoFragment extends Fragment {

    private Reuniao reuniao;

    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Gson gson = new Gson();
        reuniao = gson.fromJson(getActivity().getIntent().getStringExtra("reuniao_detalhe"), Reuniao.class);

        mapFragment = null;

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    try {
                        Geocoder geocoder = new Geocoder(getContext());
                        List<Address> resultados = geocoder.getFromLocationName(reuniao.getCep().replace(".", "").replace("-",""), 1);
                        LatLng latLng = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Reunião"));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16.0f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
    }

    public static MapReuniaoFragment newInstance() {
        return new MapReuniaoFragment();
    }
}
