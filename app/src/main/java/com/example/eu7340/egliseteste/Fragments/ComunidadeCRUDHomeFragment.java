package com.example.eu7340.egliseteste.Fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.ComunidadeCRUDActivity;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.MembroComunidadeDAO;
import com.example.eu7340.egliseteste.DB.MembroDAO;
import com.example.eu7340.egliseteste.DB.ReuniaoDAO;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.Models.Reuniao;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.gson.Gson;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComunidadeCRUDHomeFragment extends Fragment {

    private Comunidade comunidade;

    private LinearLayout reuniao_layout;
    private LinearLayout aniversariantes_layout;

    private TextView local;
    private TextView data_horario;
    private TextView descricao;

    private ImageButton membercad_bt;
    private ImageButton members_bt;
    private ImageButton reuniaocad_bt;
    private ImageButton reunioes_bt;

    private CarregaHomeComunidade carregaMenusCongregacao_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comunidade_crudhome, container, false);

        Gson gson = new Gson();
        this.comunidade = gson.fromJson(getActivity().getIntent().getStringExtra("comunidade_app"), Comunidade.class);

        reuniao_layout = view.findViewById(R.id.proxima_reuniao_layout);
        reuniao_layout.setVisibility(View.GONE);
        aniversariantes_layout = view.findViewById(R.id.aniversarintes_layout);
        aniversariantes_layout.setVisibility(View.GONE);

        local = view.findViewById(R.id.proxima_reuniao_local);
        data_horario = view.findViewById(R.id.proxima_reuniao_data);
        descricao = view.findViewById(R.id.proxima_reuniao_descricao);

        membercad_bt = view.findViewById(R.id.membercad_bt);
        membercad_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ((ComunidadeCRUDActivity) getActivity()).openFragment(MembroAddFragment.newInstance(), "comunidade_home");
            }
        });

        members_bt = view.findViewById(R.id.members_bt);
        members_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ((ComunidadeCRUDActivity) getActivity()).openFragment(MembrosFragment.newInstance(), "comunidade_home");
            }
        });

        reuniaocad_bt = view.findViewById(R.id.reuniaocad_bt);
        reuniaocad_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ((ComunidadeCRUDActivity) getActivity()).openFragment(ReuniaoCadFragment.newInstance(), "comunidade_home");
            }
        });

        reunioes_bt = view.findViewById(R.id.reunioes_bt);
        reunioes_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ((ComunidadeCRUDActivity) getActivity()).openFragment(ReunioesFragment.newInstance(), "comunidade_home");
            }
        });

        carregaMenusCongregacao_task = new CarregaHomeComunidade();
        carregaMenusCongregacao_task.execute(comunidade);

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        carregaMenusCongregacao_task.cancel(true);
    }

    public static ComunidadeCRUDHomeFragment newInstance() {
        return new ComunidadeCRUDHomeFragment();
    }

    private class DadosHome{
        public Reuniao ultima_reuniao;
        public List<Membro> membros_aniversariantes;
        public boolean lider;
    }

    private class CarregaHomeComunidade extends AsyncTask<Comunidade, Void, DadosHome> {
        @Override
        protected DadosHome doInBackground(Comunidade... params) {
            try {
                DadosHome dados = new DadosHome();

                Date agora = new Date(System.currentTimeMillis());
                java.util.Date _agora_ = new java.util.Date(System.currentTimeMillis());

                ReuniaoDAO reuniaoDAO = new ReuniaoDAO(DB.connection);
                QueryBuilder<Reuniao, Integer> _filtro = reuniaoDAO.queryBuilder();
                _filtro.where().like("id_comunidade", params[0].getId())
                .and().ge("inicio", _agora_);
                PreparedQuery<Reuniao> preparedQuery = _filtro.orderBy("inicio", true).prepare();
                List<Reuniao> _resultado = reuniaoDAO.query(preparedQuery);

                if(_resultado != null && !_resultado.isEmpty()) dados.ultima_reuniao = _resultado.get(0);

                String agora_ = agora.toString();
                agora_ = agora_.substring(5);

                MembroDAO membroDAO = new MembroDAO(DB.connection);
                GenericRawResults<Membro> membros_aniversariantes_ = membroDAO.queryRaw("select * from tbl_membros where dt_nascimento like '%" + agora_ + "%' and ativo = true", membroDAO.getRawRowMapper());
                List<Membro> membros_aniversariantes = new ArrayList<>();
                if(membros_aniversariantes_ != null) membros_aniversariantes.addAll(membros_aniversariantes_.getResults());

                MembroComunidadeDAO membroComunidadeDAO = new MembroComunidadeDAO(DB.connection);

                Map<String, Object> campos_ = new HashMap<>();
                campos_.put("id_comunidade", params[0].getId());
                campos_.put("id_membro", Sessao.ultimo_membro.getId());
                List<MembroComunidade> membroComunidade = membroComunidadeDAO.queryForFieldValues(campos_);
                if(membroComunidade != null && membroComunidade.size() == 1) dados.lider = membroComunidade.get(0).isLider();
                else dados.lider = false;

                dados.membros_aniversariantes = new ArrayList<>();
                for(int x = 0; x < membros_aniversariantes.size(); x++){
                    Map<String, Object> campos = new HashMap<>();
                    campos.put("id_membro", membros_aniversariantes.get(x).getId());
                    campos.put("id_comunidade", params[0].getId());
                    List<MembroComunidade> membro_aniversariante = membroComunidadeDAO.queryForFieldValues(campos);
                    if(membro_aniversariante != null && membro_aniversariante.size() == 1) dados.membros_aniversariantes.add(membros_aniversariantes.get(x));
                }

                return dados;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(DadosHome dados) {
            if(dados.ultima_reuniao != null) {
                reuniao_layout.setVisibility(View.VISIBLE);
                String local_ = local.getText() + " Bairro: " + dados.ultima_reuniao.getBairro()
                        + ", Rua: " + dados.ultima_reuniao.getRua()
                        + ", Num: " + dados.ultima_reuniao.getNum();
                if (dados.ultima_reuniao.getComplemento() != null)
                    local_ = local_ + ", " + dados.ultima_reuniao.getComplemento();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                String data_horario_ = data_horario.getText() + " " + sdf.format(dados.ultima_reuniao.getInicio());

                String descricao_ = descricao.getText() + " " + dados.ultima_reuniao.getDescricao();

                local.setText(local_);
                data_horario.setText(data_horario_);
                descricao.setText(descricao_);
            }else{
                reuniao_layout.setVisibility(View.GONE);
            }

            if(dados.membros_aniversariantes != null && !dados.membros_aniversariantes.isEmpty()){
                aniversariantes_layout.setVisibility(View.VISIBLE);
                for(int x = 0; x < dados.membros_aniversariantes.size(); x++){
                    TextView texto_nome = new TextView(getContext());
                    texto_nome.setText(dados.membros_aniversariantes.get(x).getNome());
                    texto_nome.setTextSize(20);
                    texto_nome.setTextColor(Color.WHITE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(5, 5, 5, 5);
                    texto_nome.setLayoutParams(lp);
                    aniversariantes_layout.addView(texto_nome);
                }
            }else{
                aniversariantes_layout.setVisibility(View.GONE);
            }

            membercad_bt.setEnabled(dados.lider);
            if(!membercad_bt.isEnabled()){
                membercad_bt.setBackgroundColor(Color.WHITE);
            }
            reuniaocad_bt.setEnabled(dados.lider);
            if(!reuniaocad_bt.isEnabled()){
                reuniaocad_bt.setBackgroundColor(Color.WHITE);
            }
        }
    }
}
