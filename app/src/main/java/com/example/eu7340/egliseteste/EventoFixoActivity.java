package com.example.eu7340.egliseteste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventoFixoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventofixo);

        getSupportActionBar().setTitle("Evento fixo");

        Gson gson = new Gson();
        EventoFixo eventofixo = gson.fromJson(getIntent().getStringExtra("eventofixo_detalhe"), EventoFixo.class);

        TextView nome = findViewById(R.id.eventofixo_nome);
        nome.setText(eventofixo.getNome());

        TextView dados = findViewById(R.id.eventofixo_dados);
        dados.setText(eventofixo.getDados_horario_local());

        TextView descricao = findViewById(R.id.eventofixo_descricao);
        descricao.setText(eventofixo.getDescricao());

        CarregaFotoEventoFixo carregaFotoEventoFixo_task = new CarregaFotoEventoFixo();
        carregaFotoEventoFixo_task.execute(eventofixo);
    }

    private class CarregaFotoEventoFixo extends AsyncTask<EventoFixo, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(EventoFixo... params) {
            try {
                URL url = new URL("http://eglise.com.br/storage/" + (params[0].getFoto() != null ? "eventos/" + params[0].getFoto() : "no-news.jpg"));
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
            ImageView imageView = findViewById(R.id.eventofixo_imagem);
            imageView.setImageBitmap(result);
        }
    }
}
