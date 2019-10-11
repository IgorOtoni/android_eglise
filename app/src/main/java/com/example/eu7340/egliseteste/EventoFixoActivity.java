package com.example.eu7340.egliseteste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventoFixoActivity extends AppCompatActivity {

    private MyJSONObject eventofixo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventofixo);

        getSupportActionBar().setTitle("Evento fixo");

        Gson gson = new Gson();
        eventofixo = gson.fromJson(getIntent().getStringExtra("eventofixo_detalhe"), MyJSONObject.class);

        TextView nome = findViewById(R.id.eventofixo_nome);
        nome.setText(eventofixo.getString("nome"));

        TextView dados = findViewById(R.id.eventofixo_dados);
        dados.setText(eventofixo.getString("dados_horario_local"));

        TextView descricao = findViewById(R.id.eventofixo_descricao);
        descricao.setText(eventofixo.getString("descricao"));

        byte[] decodedString = Base64.decode(eventofixo.getString("foto"), Base64.DEFAULT);
        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = findViewById(R.id.eventofixo_imagem);
        imageView.setImageBitmap(foto_);
    }
}
