package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.example.eu7340.egliseteste.GaleriaActivity;
import com.example.eu7340.egliseteste.GaleriaActivity;
import com.example.eu7340.egliseteste.Models.Galeria;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONArray;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class GaleriaListView extends LinearLayout {

    private MyJSONObject galeria;
    private MyJSONArray fotos;
    private View view;

    public GaleriaListView(Context context, AttributeSet attrs, MyJSONObject galeria, MyJSONArray fotos) {
        super(context, attrs);
        this.galeria = galeria;
        this.fotos = fotos;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.galeria_list, this);

        TextView nome = view.findViewById(R.id.galeria_nome);
        nome.setText(galeria.getString("nome"));

        TextView data = view.findViewById(R.id.galeria_data);
        data.setText("Data: " + galeria.getString("data"));

        Button bt = view.findViewById(R.id.galeria_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_galeria(galeria, fotos);
            }
        });
    }

    public void detalhes_galeria(MyJSONObject galeria, MyJSONArray fotos){
        Gson gson = new Gson();
        String galeria_json = gson.toJson(galeria);
        String fotos_json = gson.toJson(fotos);

        Intent intent = new Intent(getContext(), GaleriaActivity.class);

        intent.putExtra("galeria_detalhe", galeria_json);
        intent.putExtra("fotos_detalhe", fotos_json);

        getContext().startActivity(intent);
    }

}
