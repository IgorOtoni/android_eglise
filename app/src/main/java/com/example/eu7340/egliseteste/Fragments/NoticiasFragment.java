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
import com.example.eu7340.egliseteste.DB.NoticiaDAO;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Noticia;
import com.example.eu7340.egliseteste.Views.NoticiaListView;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class NoticiasFragment extends Fragment {

    private Congregacao congregacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);

        CarregaNoticias carregaNoticias_task = new CarregaNoticias(view);
        carregaNoticias_task.execute(congregacao);

        return view;
    }
    public static NoticiasFragment newInstance() {
        return new NoticiasFragment();
    }

    private class CarregaNoticias extends AsyncTask<Congregacao, Object, LinearLayout> {

        private final View view;

        public CarregaNoticias(View view) {
            this.view = view;
        }

        public LinearLayout doInBackground(Congregacao... objects) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                NoticiaDAO noticiaDAO = new NoticiaDAO(DB.connection);
                QueryBuilder<Noticia, Integer> _filtro = noticiaDAO.queryBuilder();
                _filtro.where().like("id_igreja", objects[0].getId());
                PreparedQuery<Noticia> preparedQuery = _filtro.prepare();
                List<Noticia> publicacoes = noticiaDAO.query(preparedQuery);
                for (int x = 0; x < publicacoes.size(); x++) {
                    final Noticia noticia = publicacoes.get(x);
                    NoticiaListView noticia_view = new NoticiaListView(getContext(), null, noticia);
                    linearLayout.addView(noticia_view);
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
