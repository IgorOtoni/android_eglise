package com.example.eu7340.egliseteste.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.FuncaoDAO;
import com.example.eu7340.egliseteste.DB.MembroComunidadeDAO;
import com.example.eu7340.egliseteste.DB.MembroDAO;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.MembroListView;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.gson.Gson;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembrosFragment extends Fragment {

    private Comunidade comunidade;

    private ScrollView scrollView;

    private CarregaMembrosComunidade carregaMembrosComunidade_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_membros, container, false);

        Gson gson = new Gson();
        this.comunidade = gson.fromJson(getActivity().getIntent().getStringExtra("comunidade_app"), Comunidade.class);

        scrollView = view.findViewById(R.id.scrollView);

        /*carregaMembrosComunidade_task = new CarregaMembrosComunidade(view);
        carregaMembrosComunidade_task.execute(comunidade);*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(carregaMembrosComunidade_task != null) carregaMembrosComunidade_task.cancel(true);
        carregaMembrosComunidade_task = new CarregaMembrosComunidade(getView());
        carregaMembrosComunidade_task.execute(comunidade);
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaMembrosComunidade_task != null) carregaMembrosComunidade_task.cancel(true);
    }

    public static MembrosFragment newInstance() {
        MembrosFragment membrosFragment = new MembrosFragment();

        return membrosFragment;
    }

    private class CarregaMembrosComunidade extends AsyncTask<Comunidade, Object, LinearLayout> {

        private final View view;

        public CarregaMembrosComunidade(View view) {
            this.view = view;
        }

        public LinearLayout doInBackground(Comunidade... params) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                MembroComunidadeDAO membroComunidadeDAO = new MembroComunidadeDAO(DB.connection);
                //List<MembroComunidade> membro_da_comunidade = membroComunidadeDAO.queryForEq("id_comunidade", params[0].getId());
                GenericRawResults<MembroComunidade> membro_da_comunidade_ = membroComunidadeDAO.queryRaw("select tbl_membros_comunidades.* from tbl_membros_comunidades inner join tbl_membros on tbl_membros_comunidades.id_membro = tbl_membros.id where tbl_membros.ativo = true and tbl_membros_comunidades.ativo = true and id_comunidade = " + params[0].getId(), membroComunidadeDAO.getRawRowMapper());
                List<MembroComunidade> membro_da_comunidade = new ArrayList<>();
                if(membro_da_comunidade_ != null) membro_da_comunidade.addAll(membro_da_comunidade_.getResults());

                MembroDAO membroDAO = new MembroDAO(DB.connection);
                FuncaoDAO funcaoDAO = new FuncaoDAO(DB.connection);
                for (int x = 0; x < membro_da_comunidade.size(); x++) {
                    Membro membro = membroDAO.queryForId(membro_da_comunidade.get(x).getMembro().getId());
                    Funcao funcao = null;
                    if(membro.getFuncao() != null) funcao = funcaoDAO.queryForId(membro.getFuncao().getId());

                    MembroListView membro_list = new MembroListView(getContext(), null, comunidade, membro, funcao);
                    linearLayout.addView(membro_list);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                }

                return linearLayout;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(LinearLayout linearLayout) {
            scrollView.removeAllViews();
            scrollView.addView(linearLayout);
        }
    }
}
