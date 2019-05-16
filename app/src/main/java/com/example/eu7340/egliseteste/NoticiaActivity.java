package com.example.eu7340.egliseteste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Noticia;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NoticiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        getSupportActionBar().setTitle("Notícia");

        Gson gson = new Gson();
        Noticia noticia = gson.fromJson(getIntent().getStringExtra("noticia_detalhe"), Noticia.class);

        TextView nome = findViewById(R.id.noticia_nome);
        nome.setText(noticia.getNome());

        TextView descricao = findViewById(R.id.noticia_descricao);
        descricao.setText(noticia.getDescricao());

        String txt_data = "";
        if(noticia.getUpdated_at() != null && noticia.getUpdated_at().compareTo(noticia.getCreated_at()) == 0){
            txt_data = "Atualizada em " + noticia.getUpdated_at();
        }else{
            txt_data = "Publicada em " + noticia.getCreated_at();
        }

        TextView data = findViewById(R.id.noticia_data);
        data.setText(txt_data);

        CarregaFotoNoticia carregaFotoNoticia_task = new CarregaFotoNoticia();
        carregaFotoNoticia_task.execute(noticia);
    }

    private class CarregaFotoNoticia extends AsyncTask<Noticia, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Noticia... params) {
            try {
                URL url = new URL("http://eglise.com.br/storage/" + (params[0].getFoto() != null ? "noticias/" + params[0].getFoto() : "no-news.jpg"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (MalformedURLException ex){
                ex.printStackTrace();
            }catch (IOException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = findViewById(R.id.noticia_imagem);
            imageView.setImageBitmap(result);
        }
    }
}
