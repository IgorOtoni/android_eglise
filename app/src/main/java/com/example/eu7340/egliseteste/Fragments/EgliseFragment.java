package com.example.eu7340.egliseteste.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eu7340.egliseteste.R;

public class EgliseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eglise, container, false);

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
    }

    public static EgliseFragment newInstance() {
        return new EgliseFragment();
    }
}
