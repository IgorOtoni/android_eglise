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

//import com.example.eu7340.egliseteste.GaleriaActivity;
import com.example.eu7340.egliseteste.GaleriaActivity;
import com.example.eu7340.egliseteste.Models.Galeria;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GaleriaListView extends LinearLayout {

    private Galeria galeria;
    private View view;

    public GaleriaListView(Context context, AttributeSet attrs, Galeria galeria) {
        super(context, attrs);
        this.galeria = galeria;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.galeria_list, this);

        TextView nome = view.findViewById(R.id.galeria_nome);
        nome.setText(galeria.getNome());

        TextView data = view.findViewById(R.id.galeria_data);
        data.setText("Data: " + galeria.getData());

        ImageButton bt = view.findViewById(R.id.galeria_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_galeria(galeria);
            }
        });
    }

    public void detalhes_galeria(Galeria galeria){
        Gson gson = new Gson();
        String galeria_json = gson.toJson(galeria);

        Intent intent = new Intent(getContext(), GaleriaActivity.class);

        intent.putExtra("galeria_detalhe", galeria_json);

        getContext().startActivity(intent);
    }

}
