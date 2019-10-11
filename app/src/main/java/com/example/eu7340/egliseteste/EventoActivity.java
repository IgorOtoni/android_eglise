package com.example.eu7340.egliseteste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Evento;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventoActivity extends AppCompatActivity {

    private MyJSONObject evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        getSupportActionBar().setTitle("Evento");

        Gson gson = new Gson();
        evento = gson.fromJson(getIntent().getStringExtra("evento_detalhe"), MyJSONObject.class);

        TextView nome = findViewById(R.id.evento_nome);
        nome.setText(evento.getString("nome"));

        TextView horario_inicio = findViewById(R.id.evento_horario_inicio);
        horario_inicio.setText("Início: " + evento.getString("dados_horario_inicio"));

        TextView horario_fim = findViewById(R.id.evento_horario_fim);
        horario_fim.setText("Término: " + evento.getString("dados_horario_fim"));

        TextView local = findViewById(R.id.evento_local);
        local.setText("Local: " + evento.getString("dados_local"));

        TextView descricao = findViewById(R.id.evento_descricao);
        descricao.setText(evento.getString("descricao"));

        byte[] decodedString = Base64.decode(evento.getString("foto"), Base64.DEFAULT);
        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = findViewById(R.id.evento_imagem);
        imageView.setImageBitmap(foto_);
    }
}
