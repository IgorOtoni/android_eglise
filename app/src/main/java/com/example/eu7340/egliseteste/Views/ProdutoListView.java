package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.NoticiaActivity;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

public class ProdutoListView extends LinearLayout {

    private MyJSONObject produto;
    private MyJSONObject categoria;
    private MyJSONObject oferta;
    private View view;

    public ProdutoListView(Context context, AttributeSet attrs, MyJSONObject produto, MyJSONObject categoria, MyJSONObject oferta) {
        super(context, attrs);
        this.produto = produto;
        this.categoria = categoria;
        this.oferta = oferta;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.produto_list, this);

        TextView nome = view.findViewById(R.id.produto_nome);
        nome.setText(produto.getString("nome") + "(" + categoria.getString("nome") + ")");

        TextView preco = view.findViewById(R.id.produto_preco);
        preco.setText("Valor: R$" + produto.getString("valor"));

        TextView descricao = view.findViewById(R.id.produto_descricao);
        descricao.setText(produto.getString("descricao"));

        TextView oferta_ = view.findViewById(R.id.produto_oferta);
        if(oferta.isValid()){
            oferta_.setText("Desconto: " + oferta.getString("desconto") + "%");
        }else{
            oferta_.setVisibility(GONE);
        }

        byte[] decodedString = Base64.decode(produto.getString("icone"), Base64.DEFAULT);
        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = view.findViewById(R.id.produto_icone);
        imageView.setImageBitmap(foto_);

        Button bt = view.findViewById(R.id.produto_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_noticia(produto);
            }
        });
    }

    public void detalhes_noticia(MyJSONObject produto){
        /*Gson gson = new Gson();
        String produto_json = gson.toJson(produto);

        Intent intent = new Intent(getContext(), ProdutoActivity.class);

        intent.putExtra("produto_detalhe", produto_json);
        intent.putExtra("oferta_detalhe", produto_json);
        intent.putExtra("categoria_detalhe", produto_json);

        getContext().startActivity(intent);*/
    }

}
