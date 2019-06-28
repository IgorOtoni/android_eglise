package com.example.eu7340.egliseteste;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.Noticia;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

public class MembroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membro);

        getSupportActionBar().setTitle("Membro");

        Gson gson = new Gson();
        Membro membro = gson.fromJson(getIntent().getStringExtra("membro_detalhe"), Membro.class);
        Funcao funcao = gson.fromJson(getIntent().getStringExtra("funcao_detalhe"), Funcao.class);

        TextView nome = findViewById(R.id.membro_nome);
        if(membro.getNome() != null) nome.setText(membro.getNome());
        else nome.setText("Não informado.");

        TextView funcao_ = findViewById(R.id.membro_funcao);
        if(funcao != null) funcao_.setText(funcao.getNome());
        else funcao_.setText("Não informado.");

        TextView telefone = findViewById(R.id.membro_telefone);
        if(membro.getTelefone() != null) telefone.setText(membro.getTelefone());
        else telefone.setText("Não informado.");

        TextView email = findViewById(R.id.membro_email);
        if(membro.getEmail() != null) email.setText(membro.getEmail());
        else email.setText("Não informado.");

        TextView cep = findViewById(R.id.membro_cep);
        if(membro.getCep() != null) cep.setText(membro.getCep());
        else cep.setText("Não informado.");

        TextView bairro = findViewById(R.id.membro_bairro);
        if(membro.getBairro() != null) bairro.setText(membro.getBairro());
        else bairro.setText("Não informado.");

        TextView rua = findViewById(R.id.membro_rua);
        if(membro.getRua() != null) rua.setText(membro.getRua());
        else rua.setText("Não informado.");

        TextView num = findViewById(R.id.membro_num);
        if(membro.getNum() != null) num.setText(membro.getNum());
        else num.setText("Não informado.");

        TextView complemento = findViewById(R.id.membro_comp);
        if(membro.getComplemento() != null) complemento.setText(membro.getComplemento());
        else complemento.setText("Não informado.");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        TextView nascimento = findViewById(R.id.membro_nascimento);
        if(membro.getDt_nascimento() != null) nascimento.setText(sdf.format(membro.getDt_nascimento()));
        else nascimento.setText("Não informado.");
    }
}
