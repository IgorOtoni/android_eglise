package com.example.eu7340.egliseteste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Evento;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        getSupportActionBar().setTitle("Evento");

        Gson gson = new Gson();
        Evento evento = gson.fromJson(getIntent().getStringExtra("evento_detalhe"), Evento.class);

        TextView nome = findViewById(R.id.evento_nome);
        nome.setText(evento.getNome());

        TextView horario_inicio = findViewById(R.id.evento_horario_inicio);
        horario_inicio.setText("Início: " + evento.getDados_horario_inicio());

        TextView horario_fim = findViewById(R.id.evento_horario_fim);
        horario_fim.setText("Término: " + evento.getDados_horario_fim());

        TextView local = findViewById(R.id.evento_local);
        local.setText("Local: " + evento.getDados_local());

        TextView descricao = findViewById(R.id.evento_descricao);
        descricao.setText(evento.getDescricao());

        CarregaFotoEvento carregaFotoEvento_task = new CarregaFotoEvento();
        carregaFotoEvento_task.execute(evento);
    }

    private class CarregaFotoEvento extends AsyncTask<Evento, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Evento... params) {
            try {
                URL url = new URL("http://eglise.com.br/storage/" + (params[0].getFoto() != null ? "timeline/" + params[0].getFoto() : "no-news.jpg"));
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
            ImageView imageView = findViewById(R.id.evento_imagem);
            imageView.setImageBitmap(result);
        }
    }
}
