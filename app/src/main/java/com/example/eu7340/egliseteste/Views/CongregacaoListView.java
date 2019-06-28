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

import com.example.eu7340.egliseteste.AppCongregacaoActivity;
import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CongregacaoListView extends LinearLayout {

    private Congregacao congregacao;
    private Configuracao configuracao;
    private View view;

    private CarregaLogoCongregacao carregaLogoCongregacao_task;

    public CongregacaoListView(Context context, AttributeSet attrs, Congregacao congregacao, Configuracao configuracao) {
        super(context, attrs);
        this.congregacao = congregacao;
        this.configuracao = configuracao;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.congregacao_list, this);

        TextView nome = view.findViewById(R.id.congregacao_nome);
        nome.setText(congregacao.getNome());

        carregaLogoCongregacao_task = new CarregaLogoCongregacao();
        carregaLogoCongregacao_task.execute(congregacao);

        ImageButton bt = view.findViewById(R.id.congregacao_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar_app_igreja(congregacao, configuracao);
            }
        });

        if(!congregacao.isStatus() || configuracao == null){
            bt.setEnabled(false);
            bt.setVisibility(INVISIBLE);
        }
    }

    public void entrar_app_igreja(Congregacao congregacao, Configuracao configuracao){
        Gson gson = new Gson();
        String congregacao_json = gson.toJson(congregacao);
        String configuracao_json = gson.toJson(configuracao);

        Intent intent = new Intent(getContext(), AppCongregacaoActivity.class);

        intent.putExtra("congregacao_app", congregacao_json);
        intent.putExtra("configuracao_app", configuracao_json);

        getContext().startActivity(intent);
    }

    private class CarregaLogoCongregacao extends AsyncTask<Congregacao, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Congregacao... params) {
            try {
                URL url = new URL("http://eglise.com.br/storage/" + (params[0].getLogo() != null ? "igrejas/" + params[0].getLogo() : "no-logo.jpg"));
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
            ImageView imageView = view.findViewById(R.id.congregacao_logo);
            imageView.setImageBitmap(result);
        }
    }

}
