package com.example.eu7340.egliseteste.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapSiteFragment extends Fragment {

    private MyJSONObject congregacao;

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

        congregacao = Sessao.ultima_congregacao;

        mapFragment = null;

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    try {
                        Geocoder geocoder = new Geocoder(getContext());
                        List<Address> resultados = geocoder.getFromLocationName(congregacao.getString("cep").replace(".", "").replace("-",""), 1);
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

    public static MapSiteFragment newInstance() {
        return new MapSiteFragment();
    }
}
