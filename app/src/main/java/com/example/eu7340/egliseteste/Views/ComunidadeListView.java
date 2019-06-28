package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.ComunidadeCRUDActivity;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;

public class ComunidadeListView extends LinearLayout {

    private Comunidade comunidade;
    private View view;

    public ComunidadeListView(Context context, AttributeSet attrs, Comunidade comunidade) {
        super(context, attrs);
        this.comunidade = comunidade;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.comunidade_list, this);

        TextView nome = view.findViewById(R.id.comunidade_nome);
        nome.setText(comunidade.getNome());

        TextView descricao = view.findViewById(R.id.comunidade_descricao);
        descricao.setText(comunidade.getDescricao());

        ImageButton bt = view.findViewById(R.id.comunidade_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_comunidade(comunidade);
            }
        });
    }

    public void detalhes_comunidade(Comunidade comunidade){
        Gson gson = new Gson();
        String comunidade_json = gson.toJson(comunidade);

        Intent intent = new Intent(getContext(), ComunidadeCRUDActivity.class);

        intent.putExtra("comunidade_app", comunidade_json);

        getContext().startActivity(intent);
    }

}
