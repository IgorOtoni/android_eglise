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
import java.util.ArrayList;
import java.util.List;

public class GaleriasFragment extends Fragment {

    private Congregacao congregacao;

    private ScrollView scrollView;

    private CarregaGalerias carregaGalerias_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_galerias, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);

        scrollView = view.findViewById(R.id.scrollView);

        /*carregaGalerias_task = new CarregaGalerias(view);
        carregaGalerias_task.execute(congregacao);*/

        return view;
    }

    public void onResume(){
        super.onResume();

        if(carregaGalerias_task != null) carregaGalerias_task.cancel(true);
        carregaGalerias_task = new CarregaGalerias(getView());
        carregaGalerias_task.execute(congregacao);
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaGalerias_task != null) carregaGalerias_task.cancel(true);
    }

    public static GaleriasFragment newInstance() {
        return new GaleriasFragment();
    }

    private class DadosGalerias{
        public Galeria galeria;
    }

    private class CarregaGalerias extends AsyncTask<Congregacao, Object, List<DadosGalerias>> {

        private final View view;

        public CarregaGalerias(View view) {
            this.view = view;
        }

        public List<DadosGalerias> doInBackground(Congregacao... objects) {
            try {
                List<DadosGalerias> dados_galerias = new ArrayList<>();

                GaleriaDAO galeriaDAO = new GaleriaDAO(DB.connection);
                QueryBuilder<Galeria, Integer> _filtro = galeriaDAO.queryBuilder();
                _filtro.where().like("id_igreja", objects[0].getId());
                PreparedQuery<Galeria> preparedQuery = _filtro.prepare();
                List<Galeria> galerias = galeriaDAO.query(preparedQuery);
                for (int x = 0; x < galerias.size(); x++) {
                    DadosGalerias dado_galeria = new DadosGalerias();
                    dado_galeria.galeria = galerias.get(x);
                    dados_galerias.add(dado_galeria);
                }

                return dados_galerias;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(List<DadosGalerias> dados_galerias) {
            LinearLayout linearLayout = new LinearLayout(view.getContext());

            for (int x = 0; x < dados_galerias.size(); x++) {
                GaleriaListView galeria_view = new GaleriaListView(view.getContext(), null, dados_galerias.get(x).galeria);
                linearLayout.addView(galeria_view);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            }

            scrollView.removeAllViews();
            scrollView.addView(linearLayout);
        }
    }
}
