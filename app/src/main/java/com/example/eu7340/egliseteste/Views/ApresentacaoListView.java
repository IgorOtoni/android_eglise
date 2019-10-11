package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApresentacaoListView extends LinearLayout {

    private MyJSONObject membro;
    private MyJSONObject funcao;
    private View view;

    public ApresentacaoListView(Context context, AttributeSet attrs, MyJSONObject membro, MyJSONObject funcao) {
        super(context, attrs);
        this.membro = membro;
        this.funcao = funcao;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.apresentacao_list, this);

        TextView nome = view.findViewById(R.id.apresentacao_nome);
        nome.setText(membro.getString("nome"));

        TextView funcao_ = view.findViewById(R.id.apresentacao_funcao);
        funcao_.setText(funcao.getString("nome"));

        TextView descricao = view.findViewById(R.id.apresentacao_descricao);
        descricao.setText(membro.getString("descricao"));

        byte[] decodedString = Base64.decode(membro.getString("foto"), Base64.DEFAULT);
        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = view.findViewById(R.id.apresentacao_foto);
        imageView.setImageBitmap(foto_);
    }

}
