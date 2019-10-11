package com.example.eu7340.egliseteste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Noticia;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NoticiaActivity extends AppCompatActivity {

    private MyJSONObject noticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        getSupportActionBar().setTitle("Notícia");

        Gson gson = new Gson();
        noticia = gson.fromJson(getIntent().getStringExtra("noticia_detalhe"), MyJSONObject.class);

        TextView nome = findViewById(R.id.noticia_nome);
        nome.setText(noticia.getString("nome"));

        TextView descricao = findViewById(R.id.noticia_descricao);
        descricao.setText(noticia.getString("descricao"));

        String txt_data = "";
        if(!noticia.getString("updated_at").isEmpty() && !noticia.getString("updated_at").equals("null")){
            txt_data = "Atualizada em " + noticia.getString("updated_at");
        }else{
            txt_data = "Publicada em " + noticia.getString("created_at");
        }

        TextView data = findViewById(R.id.noticia_data);
        data.setText(txt_data);

        byte[] decodedString = Base64.decode(noticia.getString("foto"), Base64.DEFAULT);
        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = findViewById(R.id.noticia_imagem);
        imageView.setImageBitmap(foto_);
    }
}
