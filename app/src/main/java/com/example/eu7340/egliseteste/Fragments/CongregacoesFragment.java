package com.example.eu7340.egliseteste.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.eu7340.egliseteste.AppCongregacao;
import com.example.eu7340.egliseteste.DB.ConfiguracaoDAO;
import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Views.CongregacaoListView;
import com.example.eu7340.egliseteste.DB.CongregacaoDAO;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.SiteCongregacao;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class CongregacoesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_congregacoes, container, false);

        CarregaCongregacoes carrega_congregacoes_task = new CarregaCongregacoes(view);
        carrega_congregacoes_task.execute();

        return view;
    }
    public static CongregacoesFragment newInstance() {
        CongregacoesFragment congregacoesFragment = new CongregacoesFragment();

        return congregacoesFragment;
    }

    private class CarregaCongregacoes extends AsyncTask<Object, Object, LinearLayout> {

        private final View view;

        public CarregaCongregacoes(View view) {
            this.view = view;
        }

        public void ver_site_congregacao(Congregacao congregacao){
            Gson gson = new Gson();
            String congregacao_json = gson.toJson(congregacao);

            Intent intent = new Intent(view.getContext(), SiteCongregacao.class);

            intent.putExtra("congregacao_app", congregacao_json);

            startActivity(intent);
        }

        public void entrar_app_igreja(Congregacao congregacao){
            Gson gson = new Gson();
            String congregacao_json = gson.toJson(congregacao);

            Intent intent = new Intent(view.getContext(), AppCongregacao.class);

            intent.putExtra("json", congregacao_json);

            startActivity(intent);
        }

        public LinearLayout doInBackground(Object... objects) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                CongregacaoDAO congregacaoDAO = new CongregacaoDAO(DB.connection);
                List<Congregacao> congregacoes = congregacaoDAO.queryForAll();

                for (int x = 0; x < congregacoes.size(); x++) {

                    ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO(DB.connection);
                    QueryBuilder<Configuracao, Integer> _filtro = configuracaoDAO.queryBuilder();
                    _filtro.where().like("id_igreja", congregacoes.get(x).getId());
                    PreparedQuery<Configuracao> preparedQuery = _filtro.prepare();
                    List<Configuracao> configuracoes = configuracaoDAO.query(preparedQuery);

                    final Congregacao congregacao = congregacoes.get(x);
                    CongregacaoListView congregacao_list = new CongregacaoListView(getContext(), null, congregacao, (configuracoes != null && configuracoes.size() == 1 ? configuracoes.get(0) : null));
                    linearLayout.addView(congregacao_list);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                }

                return linearLayout;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(LinearLayout linearLayout) {
            ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
            scrollView.addView(linearLayout);
        }
    }
}
