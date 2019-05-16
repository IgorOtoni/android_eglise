package com.example.eu7340.egliseteste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Publicacao;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PublicacaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacao);

        getSupportActionBar().setTitle("Publicação");

        Gson gson = new Gson();
        Publicacao publicacao = gson.fromJson(getIntent().getStringExtra("publicacao_detalhe"), Publicacao.class);

        TextView nome = findViewById(R.id.publicacao_nome);
        nome.setText(publicacao.getNome());

        /*String txt_data = "";
        if(publicacao.getUpdated_at() != null && publicacao.getUpdated_at().compareTo(publicacao.getCreated_at()) == 0){
            txt_data = "Atualizada em " + publicacao.getUpdated_at();
        }else{
            txt_data = "Publicada em " + publicacao.getCreated_at();
        }

        TextView data = findViewById(R.id.publicacao_data);
        data.setText(txt_data);*/

        WebView webView = (WebView) findViewById(R.id.publicacao_html);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String html = publicacao.getHtml();
        String css = "<style>body {width: 300px !important;} h1, h2, h3, h4, h5, h6, p, ul, ol, li, img {width: 100% !important;}</style>";
        /*html = html.replaceAll("\\t","");
        html = html.replaceAll("\\r","");
        html = html.replaceAll("\\n","");*/
        webView.loadData("<html>" + css + "<body>" + html + "</body></html>", "text/html", "utf-8");
        
    }
}
