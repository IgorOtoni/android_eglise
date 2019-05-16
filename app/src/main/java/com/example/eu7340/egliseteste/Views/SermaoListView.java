package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.AppCongregacao;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.SermaoActivity;
import com.google.gson.Gson;

public class SermaoListView extends LinearLayout {

    private Sermao sermao;
    private View view;

    public SermaoListView(Context context, AttributeSet attrs, Sermao sermao) {
        super(context, attrs);
        this.sermao = sermao;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.sermao_list, this);

        TextView nome = view.findViewById(R.id.sermao_nome);
        nome.setText(sermao.getNome());

        String txt_data = "";
        if(sermao.getUpdated_at() != null && sermao.getUpdated_at().compareTo(sermao.getCreated_at()) == 0){
            txt_data = "Atualizado em " + sermao.getUpdated_at();
        }else{
            txt_data = "Publicado em " + sermao.getCreated_at();
        }

        TextView data = view.findViewById(R.id.sermao_data);
        data.setText(txt_data);

        ImageButton bt = view.findViewById(R.id.sermao_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_sermao(sermao);
            }
        });
    }

    public void detalhes_sermao(Sermao sermao){
        Gson gson = new Gson();
        String sermao_json = gson.toJson(sermao);

        Intent intent = new Intent(getContext(), SermaoActivity.class);

        intent.putExtra("sermao_detalhe", sermao_json);

        getContext().startActivity(intent);
    }

}
