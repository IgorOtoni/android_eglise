package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.FrequenciaDAO;
import com.example.eu7340.egliseteste.DB.MembroComunidadeDAO;
import com.example.eu7340.egliseteste.DB.ReuniaoDAO;
import com.example.eu7340.egliseteste.FrequenciaActivity;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Frequencia;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.Models.Reuniao;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.ReuniaoActivity;
import com.example.eu7340.egliseteste.ReuniaoEditActivity;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReuniaoListView extends LinearLayout {

    private Comunidade comunidade;
    private Reuniao reuniao;
    private View view;

    private TextView nome;
    private TextView inicio;
    private TextView fim;
    private TextView descricao;
    private ImageButton bt_rem;
    private ImageButton bt_edit;
    private ImageButton bt_presenca;
    private ImageButton bt;

    public ReuniaoListView(Context context, AttributeSet attrs, Comunidade comunidade, Reuniao reuniao) {
        super(context, attrs);
        this.comunidade = comunidade;
        this.reuniao = reuniao;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.reuniao_list, this);

        nome = view.findViewById(R.id.reuniao_local);
        nome.setText("Local: " + reuniao.getBairro() + ", " + reuniao.getRua() + ", " + reuniao.getNum());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        inicio = view.findViewById(R.id.reuniao_inicio);
        inicio.setText(sdf.format(reuniao.getInicio()));

        inicio = view.findViewById(R.id.reuniao_fim);
        if(reuniao.getFim() != null) inicio.setText(sdf.format(reuniao.getFim()));
        else inicio.setText("Não definido.");

        descricao = view.findViewById(R.id.reuniao_descricao);
        descricao.setText(reuniao.getDescricao());

        bt_rem = view.findViewById(R.id.reuniaorem_bt);
        bt_rem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remover_reuniao(comunidade, reuniao);
            }
        });

        bt_edit = view.findViewById(R.id.reuniaoedit_bt);
        bt_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editar_reuniao(reuniao);
            }
        });

        bt_presenca = view.findViewById(R.id.reuniao_presenca_bt);
        bt_presenca.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lancar_presenca(comunidade, reuniao);
            }
        });

        bt = view.findViewById(R.id.reuniao_bt);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detalhes_reuniao(reuniao);
            }
        });

        VerificaLideranca verificaLideranca_task = new VerificaLideranca();
        verificaLideranca_task.execute();
    }

    private class RemoveReuniao extends AsyncTask<Object, Object, Boolean>{
        @Override
        protected Boolean doInBackground(Object... params) {
            try {

                FrequenciaDAO frequenciaDAO = new FrequenciaDAO(DB.connection);
                //List<Frequencia> frequencias = frequenciaDAO.queryForEq("id_reuniao", reuniao.getId());
                frequenciaDAO.queryRaw("delete from tb_frequencias where id_reuniao = " + reuniao.getId(), frequenciaDAO.getRawRowMapper());

                ReuniaoDAO reuniaDAO = new ReuniaoDAO(DB.connection);
                reuniaDAO.delete(reuniao);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean resultado) { }
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
            if(!bt_rem.isEnabled()) bt_rem.setBackgroundColor(Color.WHITE);
            bt_edit.setEnabled(lider);
            if(!bt_edit.isEnabled()) bt_edit.setBackgroundColor(Color.WHITE);
        }
    }

    public void remover_reuniao(Comunidade comunidade, Reuniao reuniao){
        this.setVisibility(GONE);

        Toast.makeText(getContext(), "Reunião removida com sucesso!",
                Toast.LENGTH_LONG).show();
    }

    public void lancar_presenca(Comunidade comunidade, Reuniao reuniao){
        Gson gson = new Gson();
        String membro_json = gson.toJson(comunidade);
        String funcao_json = gson.toJson(reuniao);

        Intent intent = new Intent(getContext(), FrequenciaActivity.class);

        intent.putExtra("comunidade_frequencia", membro_json);
        intent.putExtra("reuniao_frequencia", funcao_json);

        getContext().startActivity(intent);
    }

    public void detalhes_reuniao(Reuniao reuniao){
        Gson gson = new Gson();
        String reuniao_json = gson.toJson(reuniao);

        Intent intent = new Intent(getContext(), ReuniaoActivity.class);

        intent.putExtra("reuniao_detalhe", reuniao_json);

        getContext().startActivity(intent);
    }

    public void editar_reuniao(Reuniao reuniao){
        Gson gson = new Gson();
        String funcao_json = gson.toJson(reuniao);

        Intent intent = new Intent(getContext(), ReuniaoEditActivity.class);

        intent.putExtra("reuniao_edit", funcao_json);

        getContext().startActivity(intent);
    }

}
