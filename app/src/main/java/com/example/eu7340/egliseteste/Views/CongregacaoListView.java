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

import com.example.eu7340.egliseteste.AppCongregacaoActivity;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONArray;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.example.eu7340.egliseteste.utils.Sessao;

public class CongregacaoListView extends LinearLayout {

    private MyJSONObject congregacao;
    private MyJSONObject configuracao;
    private MyJSONArray menus;
    private View view;
    private TextView nome;
    private ImageView logo;
    private Button bt;

    public CongregacaoListView(Context context, AttributeSet attrs, MyJSONObject congregacao, MyJSONObject configuracao, MyJSONArray menus) {
        super(context, attrs);
        this.congregacao = congregacao;
        this.configuracao = configuracao;
        this.menus = menus;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.sites_list, this);

        nome = view.findViewById(R.id.congregacao_nome);
        nome.setText(congregacao.getString("nome"));

        bt = view.findViewById(R.id.congregacao_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar_app_igreja(congregacao, configuracao, menus);
            }
        });

        if(congregacao.getInt("status") != 1 || configuracao == null){
            bt.setEnabled(false);
            bt.setVisibility(INVISIBLE);
        }

        logo = view.findViewById(R.id.congregacao_logo);

        byte[] decodedString = Base64.decode(congregacao.getString("logo"), Base64.DEFAULT);
        Bitmap logo_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        logo.setImageBitmap(logo_);
    }

    public void entrar_app_igreja(MyJSONObject congregacao, MyJSONObject configuracao, MyJSONArray menus){
        Sessao.ultima_congregacao = congregacao;
        Sessao.ultima_configuracao = configuracao;
        Sessao.ultimos_menus = menus;

        Intent intent = new Intent(getContext(), AppCongregacaoActivity.class);

        getContext().startActivity(intent);
    }

}
