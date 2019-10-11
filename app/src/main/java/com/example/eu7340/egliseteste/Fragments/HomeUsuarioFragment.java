package com.example.eu7340.egliseteste.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.DB.ComunidadeDAO;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.MembroComunidadeDAO;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.Models.Perfil;
import com.example.eu7340.egliseteste.Models.Usuario;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.ComunidadeListView;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeUsuarioFragment extends Fragment {

    private Congregacao congregacao;
    private Configuracao configuracao;
    private Usuario usuario;
    private Perfil perfil;
    private Membro membro;

    private LinearLayout main_layout;
    private TextView titulo_comunidade_lider;
    private LinearLayout comunidade_lider_area;
    private TextView titulo_comunidade;
    private LinearLayout comunidade_area;
    private LinearLayout dizimo_area;
    private TextView msg_erro_membro;

    private CarregarHome carregarHome_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_usuario, container, false);

        main_layout = view.findViewById(R.id.main_layout);

        titulo_comunidade_lider = view.findViewById(R.id.membro_comunidades_lider_titulo);
        comunidade_lider_area = view.findViewById(R.id.membro_comunidades_lider);
        titulo_comunidade = view.findViewById(R.id.membro_comunidades_titulo);
        comunidade_area = view.findViewById(R.id.membro_comunidades);
        dizimo_area = view.findViewById(R.id.membro_comunidades);
        msg_erro_membro = view.findViewById(R.id.msg_erro);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);
        this.configuracao = gson.fromJson(getActivity().getIntent().getStringExtra("configuracao_app"), Configuracao.class);
        this.usuario = gson.fromJson(getActivity().getIntent().getStringExtra("usuario_app"), Usuario.class);
        this.perfil = gson.fromJson(getActivity().getIntent().getStringExtra("perfil_app"), Perfil.class);
        this.membro = gson.fromJson(getActivity().getIntent().getStringExtra("membro_app"), Membro.class);

        if(membro != null) {
            msg_erro_membro.setVisibility(View.GONE);
            carregarHome_task = new CarregarHome();
            carregarHome_task.execute(membro);
        }else{
            titulo_comunidade_lider.setVisibility(View.GONE);
            comunidade_lider_area.setVisibility(View.GONE);
            titulo_comunidade.setVisibility(View.GONE);
            comunidade_area.setVisibility(View.GONE);
            dizimo_area.setVisibility(View.GONE);
        }

        return view;
    }

    public void onDestroyView(){
        super.onDestroyView();

        carregarHome_task.cancel(true);
    }

    public static HomeUsuarioFragment newInstance() {
        return new HomeUsuarioFragment();
    }

    private class DadosHome{
        public List<Comunidade> comunidades_lider;
        public List<Comunidade> comunidades;
    }

    private class CarregarHome extends AsyncTask<Membro, Void, DadosHome> {
        @Override
        protected DadosHome doInBackground(Membro... params) {
            try {
                DadosHome dadosHome = new DadosHome();

                List<Comunidade> comunidades_lider = new ArrayList<>();
                List<Comunidade> comunidades = new ArrayList<>();
                //List<Dizimo> dizimos = new ArrayList<>(); ======================= Implementar

                ComunidadeDAO comunidadeDAO = new ComunidadeDAO(DB.connection);

                MembroComunidadeDAO membroComunidadeDAO = new MembroComunidadeDAO(DB.connection);
                Map<String, Object> campos_lider = new HashMap<>();
                campos_lider.put("id_membro", params[0].getId());
                campos_lider.put("lider", true);
                List<MembroComunidade> comunidades_lider_ = membroComunidadeDAO.queryForFieldValues(campos_lider);

                for(int x = 0; x < comunidades_lider_.size(); x++){
                    comunidades_lider.add(comunidadeDAO.queryForId(comunidades_lider_.get(x).getComunidade().getId()));
                }

                Map<String, Object> campos = new HashMap<>();
                campos.put("id_membro", params[0].getId());
                campos.put("lider", false);
                List<MembroComunidade> comunidades_ = membroComunidadeDAO.queryForFieldValues(campos);

                for(int x = 0; x < comunidades_.size(); x++){
                    comunidades.add(comunidadeDAO.queryForId(comunidades_.get(x).getComunidade().getId()));
                }

                dadosHome.comunidades_lider = comunidades_lider;
                dadosHome.comunidades = comunidades;

                return dadosHome;
            }catch (SQLException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(DadosHome dados) {
            if(dados.comunidades_lider == null || dados.comunidades_lider.isEmpty()){
                titulo_comunidade_lider.setVisibility(View.GONE);
            }else{
                for(int x = 0; x < dados.comunidades_lider.size(); x++){
                    /*ComunidadeListView comunidadeListView = new ComunidadeListView(getContext(), null, dados.comunidades_lider.get(x));
                    comunidade_lider_area.addView(comunidadeListView);*/
                }
            }

            if(dados.comunidades == null || dados.comunidades.isEmpty()){
                titulo_comunidade.setVisibility(View.GONE);
            }else{
                for(int x = 0; x < dados.comunidades.size(); x++){
                    /*ComunidadeListView comunidadeListView = new ComunidadeListView(getContext(), null, dados.comunidades.get(x));
                    comunidade_area.addView(comunidadeListView);*/
                }
            }
        }
    }
}
