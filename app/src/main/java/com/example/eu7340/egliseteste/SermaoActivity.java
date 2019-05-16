package com.example.eu7340.egliseteste;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Sermao;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SermaoActivity extends AppCompatActivity {

    private Sermao sermao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermao);

        getSupportActionBar().setTitle("Sermão");

        Gson gson = new Gson();
        sermao = gson.fromJson(getIntent().getStringExtra("sermao_detalhe"), Sermao.class);

        TextView nome = findViewById(R.id.sermao_nome);
        nome.setText(sermao.getNome());

        String txt_data = "";
        if(sermao.getUpdated_at() != null && sermao.getUpdated_at().compareTo(sermao.getCreated_at()) == 0){
            txt_data = "Atualizado em " + sermao.getUpdated_at();
        }else{
            txt_data = "Publicado em " + sermao.getCreated_at();
        }

        TextView data = findViewById(R.id.sermao_data);
        data.setText(txt_data);

        TextView descricao = findViewById(R.id.sermao_descricao);
        descricao.setText(sermao.getDescricao());

        WebView webView = (WebView) findViewById(R.id.sermao_video);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(sermao.getLink());

        Button bt = findViewById(R.id.sermao_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sermao.getLink())));
            }
        });
    }
}
