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
import com.example.eu7340.egliseteste.DB.EventoFixoDAO;
import com.example.eu7340.egliseteste.Views.EventoFixoListView;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class EventosFixosFragment extends Fragment {
    private Congregacao congregacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos_fixos, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);

        EventosFixosFragment.CarregaEventosFixos carregaEventosFixos_task = new EventosFixosFragment.CarregaEventosFixos(view);
        carregaEventosFixos_task.execute(congregacao);

        return view;
    }
    public static EventosFixosFragment newInstance() {
        return new EventosFixosFragment();
    }

    private class CarregaEventosFixos extends AsyncTask<Congregacao, Object, LinearLayout> {

        private final View view;

        public CarregaEventosFixos(View view) {
            this.view = view;
        }

        public LinearLayout doInBackground(Congregacao... objects) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                EventoFixoDAO eventofixoDAO = new EventoFixoDAO(DB.connection);
                QueryBuilder<EventoFixo, Integer> _filtro = eventofixoDAO.queryBuilder();
                _filtro.where().like("id_igreja", objects[0].getId());
                PreparedQuery<EventoFixo> preparedQuery = _filtro.prepare();
                List<EventoFixo> publicacoes = eventofixoDAO.query(preparedQuery);
                for (int x = 0; x < publicacoes.size(); x++) {
                    final EventoFixo eventofixo = publicacoes.get(x);
                    EventoFixoListView eventofixo_view = new EventoFixoListView(getContext(), null, eventofixo);
                    linearLayout.addView(eventofixo_view);
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
