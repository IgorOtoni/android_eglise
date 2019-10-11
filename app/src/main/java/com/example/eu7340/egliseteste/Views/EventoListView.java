package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.EventoActivity;
import com.example.eu7340.egliseteste.Models.Evento;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class EventoListView extends LinearLayout {

    private MyJSONObject evento;
    private View view;

    public EventoListView(Context context, AttributeSet attrs, MyJSONObject evento) {
        super(context, attrs);
        this.evento = evento;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.evento_list, this);

        TextView nome = view.findViewById(R.id.evento_nome);
        nome.setText(evento.getString("nome"));

        String txt_data_inicio = "Início: " + evento.getString("dados_horario_inicio");
        //String txt_data_fim = "Término: " + evento.getString("dados_horario_fim");
        String txt_local = evento.getString("dados_local");

        TextView data_inicio = view.findViewById(R.id.evento_data_inicio);
        data_inicio.setText(txt_data_inicio);

        /*TextView data_fim = view.findViewById(R.id.evento_data_fim);
        data_fim.setText(txt_data_fim);*/

        TextView local = view.findViewById(R.id.evento_local);
        local.setText(txt_local);

        byte[] decodedString = Base64.decode(evento.getString("foto"), Base64.DEFAULT);
        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = view.findViewById(R.id.evento_imagem);
        imageView.setImageBitmap(foto_);

        Button bt = view.findViewById(R.id.evento_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_evento(evento);
            }
        });
    }

    public void detalhes_evento(MyJSONObject evento){
        Gson gson = new Gson();
        String evento_json = gson.toJson(evento);

        Intent intent = new Intent(getContext(), EventoActivity.class);

        intent.putExtra("evento_detalhe", evento_json);

        getContext().startActivity(intent);
    }

}
