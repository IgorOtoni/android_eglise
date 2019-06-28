package com.example.eu7340.egliseteste;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.eu7340.egliseteste.Fragments.ComunidadeCRUDHomeFragment;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.google.gson.Gson;

import java.util.List;

public class ComunidadeCRUDActivity extends AppCompatActivity {

    private static Fragment last_fragment;

    private Comunidade comunidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunidade_crud);

        Gson gson = new Gson();
        this.comunidade = gson.fromJson(getIntent().getStringExtra("comunidade_app"), Comunidade.class);

        getSupportActionBar().setTitle("Comunidade: " + this.comunidade.getNome());

        if(last_fragment == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_layout, ComunidadeCRUDHomeFragment.newInstance(), "comunidade_home");
            transaction.commit();
        }else if(last_fragment instanceof ComunidadeCRUDHomeFragment){
            openFragment(last_fragment);
        } else {
            openFragment(last_fragment);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        last_fragment = fragments.get(fragments.size() - 1);
    }

    public void openFragment(Fragment fragment, String backFlag) {
        last_fragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_layout, fragment);
        transaction.addToBackStack(backFlag);
        transaction.commit();
    }

    public void openFragment(Fragment fragment) {
        last_fragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_layout, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
