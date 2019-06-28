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
import android.widget.Toast;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.MembroComunidadeDAO;
import com.example.eu7340.egliseteste.Fragments.ComunidadeCRUDHomeFragment;
import com.example.eu7340.egliseteste.MembroActivity;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.Models.Noticia;
import com.example.eu7340.egliseteste.NoticiaActivity;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembroListView extends LinearLayout {

    private Comunidade comunidade;
    private Membro membro;
    private Funcao funcao;
    private View view;

    private TextView nome;
    private TextView funcao_;
    private ImageButton bt_rem;
    private ImageButton bt;

    public MembroListView(Context context, AttributeSet attrs, Comunidade comunidade, Membro membro, Funcao funcao) {
        super(context, attrs);
        this.comunidade = comunidade;
        this.membro = membro;
        this.funcao = funcao;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.membro_list, this);

        nome = view.findViewById(R.id.membro_nome);
        nome.setText(membro.getNome());

        funcao_ = view.findViewById(R.id.membro_funcao);
        if(funcao != null) funcao_.setText("Função: " + funcao.getNome());
        else funcao_.setVisibility(INVISIBLE);

        bt_rem = view.findViewById(R.id.membrorem_bt);
        bt_rem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remover_membro(comunidade, membro);
            }
        });

        bt = view.findViewById(R.id.membro_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_membro(membro, funcao);
            }
        });

        VerificaLideranca verificaLideranca_task = new VerificaLideranca();
        verificaLideranca_task.execute();
    }

    private class VerificaLideranca extends AsyncTask<Object, Object, Boolean>{
        @Override
        protected Boolean doInBackground(Object... params) {
            try {

                MembroComunidadeDAO membroComunidadeDAO = new MembroComunidadeDAO(DB.connection);
                Map<String, Object> campos_ = new HashMap<>();
                campos_.put("id_comunidade", comunidade.getId());
                campos_.put("id_membro", Sessao.ultimo_membro.getId());
                List<MembroComunidade> membroComunidade = membroComunidadeDAO.queryForFieldValues(campos_);
                if(membroComunidade != null && membroComunidade.size() == 1) return membroComunidade.get(0).isLider();
                else return false;

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean lider) {
            bt_rem.setEnabled(lider);
        }
    }

    public void remover_membro(Comunidade comunidade, Membro membro){

        DesativaMembro desativaMembro_task = new DesativaMembro();
        desativaMembro_task.execute();

        Toast.makeText(getContext(), membro.getNome() + " foi removido da comunidade " + comunidade.getNome() + ".",
                Toast.LENGTH_LONG).show();

        this.setVisibility(GONE);
    }

    private class DesativaMembro extends AsyncTask<Object, Object, Boolean>{
        @Override
        protected Boolean doInBackground(Object... params) {
            try {

                MembroComunidadeDAO membroComunidadeDAO = new MembroComunidadeDAO(DB.connection);
                Map<String, Object> campos_ = new HashMap<>();
                campos_.put("id_comunidade", comunidade.getId());
                campos_.put("id_membro", membro.getId());
                List<MembroComunidade> membroComunidade = membroComunidadeDAO.queryForFieldValues(campos_);
                if(membroComunidade != null && !membroComunidade.isEmpty()){
                    membroComunidade.get(0).setAtivo(false);
                    membroComunidadeDAO.update(membroComunidade.get(0));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean lider) {

        }
    }

    public void detalhes_membro(Membro membro, Funcao funcao){
        Gson gson = new Gson();
        String membro_json = gson.toJson(membro);
        String funcao_json = gson.toJson(funcao);

        Intent intent = new Intent(getContext(), MembroActivity.class);

        intent.putExtra("membro_detalhe", membro_json);
        intent.putExtra("funcao_detalhe", funcao_json);

        getContext().startActivity(intent);
    }

}
