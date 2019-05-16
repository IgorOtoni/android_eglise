package com.example.eu7340.egliseteste.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.PublicacaoDAO;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Publicacao;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.PublicacaoListView;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class PublicacoesFragment extends Fragment {

    private Congregacao congregacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_publicacoes, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);

        CarregaPublicacoes carregaPublicacoes_task = new CarregaPublicacoes(view);
        carregaPublicacoes_task.execute(congregacao);

        return view;
    }
    public static PublicacoesFragment newInstance() {
        return new PublicacoesFragment();
    }

    private class CarregaPublicacoes extends AsyncTask<Congregacao, Object, LinearLayout> {

        private final View view;

        public CarregaPublicacoes(View view) {
            this.view = view;
        }

        public LinearLayout doInBackground(Congregacao... objects) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                PublicacaoDAO publicacaoDAO = new PublicacaoDAO(DB.connection);
                QueryBuilder<Publicacao, Integer> _filtro = publicacaoDAO.queryBuilder();
                _filtro.where().like("id_igreja", objects[0].getId());
                PreparedQuery<Publicacao> preparedQuery = _filtro.prepare();
                List<Publicacao> publicacoes = publicacaoDAO.query(preparedQuery);
                for (int x = 0; x < publicacoes.size(); x++) {
                    final Publicacao publicacao = publicacoes.get(x);
                    PublicacaoListView publicacao_view = new PublicacaoListView(getContext(), null, publicacao);
                    linearLayout.addView(publicacao_view);
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
