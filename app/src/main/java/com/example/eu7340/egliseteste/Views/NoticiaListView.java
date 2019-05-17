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
import com.example.eu7340.egliseteste.Models.Noticia;
import com.example.eu7340.egliseteste.NoticiaActivity;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NoticiaListView extends LinearLayout {

    private Noticia noticia;
    private View view;

    public NoticiaListView(Context context, AttributeSet attrs, Noticia noticia) {
        super(context, attrs);
        this.noticia = noticia;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.noticia_list, this);

        TextView nome = view.findViewById(R.id.noticia_nome);
        nome.setText(noticia.getNome());

        String txt_data = "";
        if(noticia.getUpdated_at() != null && noticia.getUpdated_at().compareTo(noticia.getCreated_at()) == 0){
            txt_data = "Atualizada em " + noticia.getUpdated_at();
        }else{
            txt_data = "Publicada em " + noticia.getCreated_at();
        }

        TextView data = view.findViewById(R.id.noticia_data);
        data.setText(txt_data);

        CarregaFotoNoticia carregaFotoNoticia_task = new CarregaFotoNoticia();
        carregaFotoNoticia_task.execute(noticia);

        ImageButton bt = view.findViewById(R.id.noticia_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_noticia(noticia);
            }
        });
    }

    public void detalhes_noticia(Noticia noticia){
        Gson gson = new Gson();
        String noticia_json = gson.toJson(noticia);

        Intent intent = new Intent(getContext(), NoticiaActivity.class);

        intent.putExtra("noticia_detalhe", noticia_json);

        getContext().startActivity(intent);
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
            ImageView imageView = view.findViewById(R.id.noticia_imagem);
            imageView.setImageBitmap(result);
        }
    }

}
