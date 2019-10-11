package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Noticia;
import com.example.eu7340.egliseteste.NoticiaActivity;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class NoticiaListView extends LinearLayout {

    private MyJSONObject noticia;
    private View view;

    public NoticiaListView(Context context, AttributeSet attrs, MyJSONObject noticia) {
        super(context, attrs);
        this.noticia = noticia;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.noticia_list, this);

        TextView nome = view.findViewById(R.id.noticia_nome);
        nome.setText(noticia.getString("nome"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String txt_data = "";
        if(!noticia.getString("updated_at").isEmpty() && !noticia.getString("updated_at").equals("null")){
            txt_data = "Atualizada em " + noticia.getString("updated_at");
        }else{
            txt_data = "Publicada em " + noticia.getString("created_at");
        }

        TextView data = view.findViewById(R.id.noticia_data);
        data.setText(txt_data);

        byte[] decodedString = Base64.decode(noticia.getString("foto"), Base64.DEFAULT);
        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = view.findViewById(R.id.noticia_imagem);
        imageView.setImageBitmap(foto_);

        Button bt = view.findViewById(R.id.noticia_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_noticia(noticia);
            }
        });
    }

    public void detalhes_noticia(MyJSONObject noticia){
        Gson gson = new Gson();
        String noticia_json = gson.toJson(noticia);

        Intent intent = new Intent(getContext(), NoticiaActivity.class);

        intent.putExtra("noticia_detalhe", noticia_json);

        getContext().startActivity(intent);
    }

}
