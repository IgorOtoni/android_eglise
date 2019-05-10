package com.example.eu7340.egliseteste;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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

            intent.putExtra("json", congregacao_json);

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
                    final Congregacao congregacao = congregacoes.get(x);
                    final LinearLayout _linearLayout = new LinearLayout(view.getContext());
                    final LinearLayout _linearLayout_ = new LinearLayout(view.getContext());
                    _linearLayout_.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    final EditText nm_etapa = new EditText(view.getContext());
                    nm_etapa.setEnabled(false);
                    nm_etapa.setText(congregacoes.get(x).getNome());
                    final ImageView line = new ImageView(view.getContext());
                    line.setImageResource(R.drawable.line);
                    line.setScaleType(ImageView.ScaleType.FIT_XY);
                    line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    final ImageButton bt_site = new ImageButton(view.getContext());
                    bt_site.setImageResource(R.drawable.pesquisar);
                    bt_site.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ver_site_congregacao(congregacao);
                        }
                    });
                    bt_site.setEnabled(congregacoes.get(x).isStatus());
                    final ImageButton bt_congregacao = new ImageButton(view.getContext());
                    bt_congregacao.setImageResource(R.drawable.cad);
                    bt_congregacao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            entrar_app_igreja(congregacao);
                        }
                    });
                    _linearLayout.addView(bt_site);
                    _linearLayout.addView(bt_congregacao);
                    _linearLayout.addView(nm_etapa);
                    _linearLayout_.addView(line);
                    linearLayout.addView(_linearLayout);
                    linearLayout.addView(_linearLayout_);
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
