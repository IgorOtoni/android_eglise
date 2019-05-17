package com.example.eu7340.egliseteste.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.SermaoDAO;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.SermaoListView;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class SermoesFragment extends Fragment {
    private Congregacao congregacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sermoes, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);

        SermoesFragment.CarregaSermoes carregaSermoes_task = new SermoesFragment.CarregaSermoes(view);
        carregaSermoes_task.execute(congregacao);

        return view;
    }
    public static SermoesFragment newInstance() {
        return new SermoesFragment();
    }

    private class CarregaSermoes extends AsyncTask<Congregacao, Object, LinearLayout> {

        private final View view;

        public CarregaSermoes(View view) {
            this.view = view;
        }

        public LinearLayout doInBackground(Congregacao... objects) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                SermaoDAO sermaoDAO = new SermaoDAO(DB.connection);
                QueryBuilder<Sermao, Integer> _filtro = sermaoDAO.queryBuilder();
                _filtro.where().like("id_igreja", objects[0].getId());
                PreparedQuery<Sermao> preparedQuery = _filtro.prepare();
                List<Sermao> publicacoes = sermaoDAO.query(preparedQuery);
                for (int x = 0; x < publicacoes.size(); x++) {
                    final Sermao sermao = publicacoes.get(x);
                    SermaoListView sermao_view = new SermaoListView(getContext(), null, sermao);
                    linearLayout.addView(sermao_view);
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
