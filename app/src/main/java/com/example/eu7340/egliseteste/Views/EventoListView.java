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

import com.example.eu7340.egliseteste.EventoActivity;
import com.example.eu7340.egliseteste.Models.Evento;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventoListView extends LinearLayout {

    private Evento evento;
    private View view;

    public EventoListView(Context context, AttributeSet attrs, Evento evento) {
        super(context, attrs);
        this.evento = evento;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.evento_list, this);

        TextView nome = view.findViewById(R.id.evento_nome);
        nome.setText(evento.getNome());

        String txt_data_inicio = "Início: " + evento.getDados_horario_inicio();
        String txt_data_fim = "Término: " + evento.getDados_horario_fim();
        String txt_local = evento.getDados_local();

        TextView data_inicio = view.findViewById(R.id.evento_data_inicio);
        data_inicio.setText(txt_data_inicio);

        TextView data_fim = view.findViewById(R.id.evento_data_fim);
        data_fim.setText(txt_data_fim);

        TextView local = view.findViewById(R.id.evento_local);
        local.setText(txt_local);

        CarregaLogoEvento carregaLogoEvento_task = new CarregaLogoEvento();
        carregaLogoEvento_task.execute(evento);

        ImageButton bt = view.findViewById(R.id.evento_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_evento(evento);
            }
        });
    }

    public void detalhes_evento(Evento evento){
        Gson gson = new Gson();
        String evento_json = gson.toJson(evento);

        Intent intent = new Intent(getContext(), EventoActivity.class);

        intent.putExtra("evento_detalhe", evento_json);

        getContext().startActivity(intent);
    }

    private class CarregaLogoEvento extends AsyncTask<Evento, Void, Bitmap> {
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
            ImageView imageView = view.findViewById(R.id.evento_imagem);
            imageView.setImageBitmap(result);
        }
    }

}
