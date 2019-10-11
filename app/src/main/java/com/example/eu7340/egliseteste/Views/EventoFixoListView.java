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

import com.example.eu7340.egliseteste.EventoFixoActivity;
import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventoFixoListView extends LinearLayout {

    private MyJSONObject eventofixo;
    private View view;

    public EventoFixoListView(Context context, AttributeSet attrs, MyJSONObject eventofixo) {
        super(context, attrs);
        this.eventofixo = eventofixo;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.eventofixo_list, this);

        TextView nome = view.findViewById(R.id.eventofixo_nome);
        nome.setText(eventofixo.getString("nome"));

        TextView dados = view.findViewById(R.id.eventofixo_dados);
        dados.setText(eventofixo.getString("dados_horario_local"));

        byte[] decodedString = Base64.decode(eventofixo.getString("foto"), Base64.DEFAULT);
        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = view.findViewById(R.id.eventofixo_imagem);
        imageView.setImageBitmap(foto_);

        Button bt = view.findViewById(R.id.eventofixo_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_eventofixo(eventofixo);
            }
        });
    }

    public void detalhes_eventofixo(MyJSONObject eventofixo){
        Gson gson = new Gson();
        String eventofixo_json = gson.toJson(eventofixo);

        Intent intent = new Intent(getContext(), EventoFixoActivity.class);

        intent.putExtra("eventofixo_detalhe", eventofixo_json);

        getContext().startActivity(intent);
    }

}
