package com.example.eu7340.egliseteste;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;

public class PublicacoesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_publicacoes, container, false);

        CarregaPublicacoes carregaPublicacoes_task = new CarregaPublicacoes(view);
        carregaPublicacoes_task.execute();

        return view;
    }
    public static PublicacoesFragment newInstance() {
        return new PublicacoesFragment();
    }

    private class CarregaPublicacoes extends AsyncTask<Object, Object, LinearLayout> {

        private final View view;

        public CarregaPublicacoes(View view) {
            this.view = view;
        }

        public void ver_publicacao(Publicacao publicacao){
            Gson gson = new Gson();
            String publicacao_json = gson.toJson(publicacao);

            /*Intent intent = new Intent(view.getContext(), SiteCongregacao.class);

            intent.putExtra("json", publicacao_json);

            startActivity(intent);*/
        }

        public LinearLayout doInBackground(Object... objects) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                PublicacaoDAO publicacaoDAO = new PublicacaoDAO(DB.connection);
                List<Publicacao> publicacoes = publicacaoDAO.queryForAll();
                for (int x = 0; x < publicacoes.size(); x++) {
                    final Publicacao publicacao = publicacoes.get(x);
                    final LinearLayout _linearLayout = new LinearLayout(view.getContext());
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
                            ver_publicacao(publicacao);
                        }
                    });
                    _linearLayout.addView(bt_site);
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
