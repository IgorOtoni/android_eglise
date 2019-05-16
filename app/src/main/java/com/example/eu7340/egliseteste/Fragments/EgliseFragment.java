package com.example.eu7340.egliseteste.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eu7340.egliseteste.R;

public class EgliseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eglise, container, false);
    }
    public static EgliseFragment newInstance() {
        return new EgliseFragment();
    }
}
