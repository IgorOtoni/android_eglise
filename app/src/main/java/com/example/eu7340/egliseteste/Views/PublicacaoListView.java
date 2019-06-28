package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Publicacao;
import com.example.eu7340.egliseteste.PublicacaoActivity;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class PublicacaoListView extends LinearLayout {

    private Publicacao publicacao;
    private View view;

    public PublicacaoListView(Context context, AttributeSet attrs, Publicacao publicacao) {
        super(context, attrs);
        this.publicacao = publicacao;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.publicacao_list, this);

        TextView nome = view.findViewById(R.id.publicacao_nome);
        nome.setText(publicacao.getNome());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String txt_data = "";
        if(publicacao.getUpdated_at() != null && publicacao.getUpdated_at().compareTo(publicacao.getCreated_at()) == 0){
            txt_data = "Atualizada em " + sdf.format(publicacao.getUpdated_at());
        }else{
            txt_data = "Publicada em " + sdf.format(publicacao.getCreated_at());
        }

        TextView data = view.findViewById(R.id.publicacao_data);
        data.setText(txt_data);

        ImageButton bt = view.findViewById(R.id.publicacao_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_publicacao(publicacao);
            }
        });
    }

    public void detalhes_publicacao(Publicacao publicacao){
        Gson gson = new Gson();
        String publicacao_json = gson.toJson(publicacao);

        Intent intent = new Intent(getContext(), PublicacaoActivity.class);

        intent.putExtra("publicacao_detalhe", publicacao_json);

        getContext().startActivity(intent);
    }

}
