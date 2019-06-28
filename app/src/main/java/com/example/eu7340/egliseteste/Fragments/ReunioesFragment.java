package com.example.eu7340.egliseteste.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.ReuniaoDAO;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Reuniao;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.ReuniaoListView;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;

public class ReunioesFragment extends Fragment {

    private Comunidade comunidade;

    private ScrollView scrollView;

    private CarregaReunioesComunidade carregaReunioesComunidade_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reunioes, container, false);

        Gson gson = new Gson();
        this.comunidade = gson.fromJson(getActivity().getIntent().getStringExtra("comunidade_app"), Comunidade.class);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        /*carregaReunioesComunidade_task = new CarregaReunioesComunidade(view);
        carregaReunioesComunidade_task.execute(comunidade);*/

        return view;
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaReunioesComunidade_task != null) carregaReunioesComunidade_task.cancel(true);
    }

    public void onResume(){
        super.onResume();
        if(carregaReunioesComunidade_task != null) carregaReunioesComunidade_task.cancel(true);
        carregaReunioesComunidade_task = new CarregaReunioesComunidade(getView());
        carregaReunioesComunidade_task.execute(comunidade);
    }

    public static ReunioesFragment newInstance() {
        ReunioesFragment reunioesFragment = new ReunioesFragment();

        return reunioesFragment;
    }

    private class CarregaReunioesComunidade extends AsyncTask<Comunidade, Object, LinearLayout> {

        private final View view;

        public CarregaReunioesComunidade(View view) {
            this.view = view;
        }

        public LinearLayout doInBackground(Comunidade... params) {
            try {
                LinearLayout linearLayout = new LinearLayout(view.getContext());

                ReuniaoDAO reuniaoDAO = new ReuniaoDAO(DB.connection);
                List<Reuniao> reunioes = reuniaoDAO.queryForEq("id_comunidade", params[0].getId());
                for (int x = 0; x < reunioes.size(); x++) {
                    ReuniaoListView membro_list = new ReuniaoListView(getContext(), null, comunidade, reunioes.get(x));
                    linearLayout.addView(membro_list);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                }

                return linearLayout;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(LinearLayout linearLayout) {
            scrollView.removeAllViews();
            scrollView.addView(linearLayout);
        }
    }
}
