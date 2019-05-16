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
import com.example.eu7340.egliseteste.DB.EventoDAO;
import com.example.eu7340.egliseteste.Views.EventoListView;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Evento;
import com.example.eu7340.egliseteste.R;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class EventosFragment extends Fragment {
    private Congregacao congregacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);

        EventosFragment.CarregaEventoss carregaEventoss_task = new EventosFragment.CarregaEventoss(view);
        carregaEventoss_task.execute(congregacao);

        return view;
    }
    public static EventosFragment newInstance() {
        return new EventosFragment();
    }

    private class CarregaEventoss extends AsyncTask<Congregacao, Object, LinearLayout> {

        private final View view;

        public CarregaEventoss(View view) {
            this.view = view;
        }

        public LinearLayout doInBackground(Congregacao... objects) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                EventoDAO eventoDAO = new EventoDAO(DB.connection);
                QueryBuilder<Evento, Integer> _filtro = eventoDAO.queryBuilder();
                _filtro.where().like("id_igreja", objects[0].getId());
                PreparedQuery<Evento> preparedQuery = _filtro.prepare();
                List<Evento> eventos = eventoDAO.query(preparedQuery);
                for (int x = 0; x < eventos.size(); x++) {
                    final Evento evento = eventos.get(x);
                    EventoListView evento_view = new EventoListView(getContext(), null, evento);
                    /*final LinearLayout _linearLayout = new LinearLayout(view.getContext());
                    final LinearLayout _linearLayout_ = new LinearLayout(view.getContext());
                    _linearLayout_.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    final EditText nm_etapa = new EditText(view.getContext());
                    nm_etapa.setEnabled(false);
                    nm_etapa.setText(publicacoes.get(x).getNome());
                    final ImageView line = new ImageView(view.getContext());
                    line.setImageResource(R.drawable.line);
                    line.setScaleType(ImageView.ScaleType.FIT_XY);
                    line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    final ImageButton bt_site = new ImageButton(view.getContext());
                    bt_site.setImageResource(R.drawable.pesquisar);
                    bt_site.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ver_evento(evento);
                        }
                    });
                    _linearLayout.addView(bt_site);
                    _linearLayout.addView(nm_etapa);
                    _linearLayout_.addView(line);
                    linearLayout.addView(_linearLayout);
                    linearLayout.addView(_linearLayout_);*/
                    linearLayout.addView(evento_view);
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
