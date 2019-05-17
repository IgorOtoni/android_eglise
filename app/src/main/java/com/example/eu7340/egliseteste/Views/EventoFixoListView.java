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

import com.example.eu7340.egliseteste.AppCongregacao;
import com.example.eu7340.egliseteste.EventoFixoActivity;
import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventoFixoListView extends LinearLayout {

    private EventoFixo eventofixo;
    private View view;

    public EventoFixoListView(Context context, AttributeSet attrs, EventoFixo eventofixo) {
        super(context, attrs);
        this.eventofixo = eventofixo;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.eventofixo_list, this);

        TextView nome = view.findViewById(R.id.eventofixo_nome);
        nome.setText(eventofixo.getNome());

        TextView dados = view.findViewById(R.id.eventofixo_dados);
        dados.setText(eventofixo.getDados_horario_local());
        
        CarregaFotoEventoFixo carregaFotoEventoFixo_task = new CarregaFotoEventoFixo();
        carregaFotoEventoFixo_task.execute(eventofixo);

        ImageButton bt = view.findViewById(R.id.eventofixo_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_eventofixo(eventofixo);
            }
        });
    }

    public void detalhes_eventofixo(EventoFixo eventofixo){
        Gson gson = new Gson();
        String eventofixo_json = gson.toJson(eventofixo);

        Intent intent = new Intent(getContext(), EventoFixoActivity.class);

        intent.putExtra("eventofixo_detalhe", eventofixo_json);

        getContext().startActivity(intent);
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
            ImageView imageView = view.findViewById(R.id.eventofixo_imagem);
            imageView.setImageBitmap(result);
        }
    }

}
