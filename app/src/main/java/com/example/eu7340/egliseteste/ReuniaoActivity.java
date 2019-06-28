package com.example.eu7340.egliseteste;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Fragments.MapReuniaoFragment;
import com.example.eu7340.egliseteste.Models.Reuniao;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

public class ReuniaoActivity extends AppCompatActivity {

    private Reuniao reuniao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao);

        getSupportActionBar().setTitle("Reunião");

        Gson gson = new Gson();
        reuniao = gson.fromJson(getIntent().getStringExtra("reuniao_detalhe"), Reuniao.class);

        TextView cep = findViewById(R.id.reuniao_cep);
        if(reuniao.getCep() != null) cep.setText(reuniao.getCep());
        else cep.setText("Não informado.");

        TextView bairro = findViewById(R.id.reuniao_bairro);
        if(reuniao.getBairro() != null) bairro.setText(reuniao.getBairro());
        else bairro.setText("Não informado.");

        TextView rua = findViewById(R.id.reuniao_rua);
        if(reuniao.getRua() != null) rua.setText(reuniao.getRua());
        else rua.setText("Não informado.");

        TextView num = findViewById(R.id.reuniao_num);
        if(reuniao.getNum() != null) num.setText(reuniao.getNum());
        else num.setText("Não informado.");

        TextView complemento = findViewById(R.id.reuniao_comp);
        if(reuniao.getComplemento() != null) complemento.setText(reuniao.getComplemento());
        else complemento.setText("Não informado.");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        TextView inicio = findViewById(R.id.reuniao_dth_inicio);
        if(reuniao.getInicio() != null) inicio.setText(sdf.format(reuniao.getInicio()));
        else inicio.setText("Não informado.");

        TextView fim = findViewById(R.id.reuniao_dth_fim);
        if(reuniao.getFim() != null) fim.setText(sdf.format(reuniao.getFim()));
        else fim.setText("Não informado.");

        TextView descricao = findViewById(R.id.reuniao_descricao);
        if(reuniao.getDescricao() != null) descricao.setText(reuniao.getDescricao());
        else descricao.setText("Não informado.");

        TextView observacao = findViewById(R.id.reuniao_observacao);
        if(reuniao.getObservacao() != null) observacao.setText(reuniao.getObservacao());
        else observacao.setText("Não informado.");

        if(reuniao.getCep() != null) openFragment(MapReuniaoFragment.newInstance());
        else findViewById(R.id.map_container).setVisibility(View.GONE);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onBackPressed(){
        finish();
    }
}
