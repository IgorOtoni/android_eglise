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
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SermaoActivity extends AppCompatActivity {

    private MyJSONObject sermao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermao);

        getSupportActionBar().setTitle("Sermão");

        Gson gson = new Gson();
        sermao = gson.fromJson(getIntent().getStringExtra("sermao_detalhe"), MyJSONObject.class);

        TextView nome = findViewById(R.id.sermao_nome);
        nome.setText(sermao.getString("nome"));

        String txt_data = "";
        if(!sermao.getString("updated_at").isEmpty() && !sermao.getString("updated_at").equals("null")){
            txt_data = "Atualizado em " + sermao.getString("updated_at");
        }else{
            txt_data = "Publicado em " + sermao.getString("created_at");
        }

        TextView data = findViewById(R.id.sermao_data);
        data.setText(txt_data);

        TextView descricao = findViewById(R.id.sermao_descricao);
        descricao.setText(sermao.getString("descricao"));

        WebView webView = (WebView) findViewById(R.id.sermao_video);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(sermao.getString("link"));

        Button bt = findViewById(R.id.sermao_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sermao.getString("link"))));
            }
        });
    }
}
