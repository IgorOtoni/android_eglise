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
import com.example.eu7340.egliseteste.DB.GaleriaDAO;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Galeria;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.GaleriaListView;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class GaleriasFragment extends Fragment {

    private Congregacao congregacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_galerias, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);

        CarregaGalerias carregaGalerias_task = new CarregaGalerias(view);
        carregaGalerias_task.execute(congregacao);

        return view;
    }
    public static GaleriasFragment newInstance() {
        return new GaleriasFragment();
    }

    private class CarregaGalerias extends AsyncTask<Congregacao, Object, LinearLayout> {

        private final View view;

        public CarregaGalerias(View view) {
            this.view = view;
        }

        public LinearLayout doInBackground(Congregacao... objects) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                GaleriaDAO galeriaDAO = new GaleriaDAO(DB.connection);
                QueryBuilder<Galeria, Integer> _filtro = galeriaDAO.queryBuilder();
                _filtro.where().like("id_igreja", objects[0].getId());
                PreparedQuery<Galeria> preparedQuery = _filtro.prepare();
                List<Galeria> galerias = galeriaDAO.query(preparedQuery);
                for (int x = 0; x < galerias.size(); x++) {
                    final Galeria galeria = galerias.get(x);
                    GaleriaListView galeria_view = new GaleriaListView(getContext(), null, galeria);
                    linearLayout.addView(galeria_view);
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
