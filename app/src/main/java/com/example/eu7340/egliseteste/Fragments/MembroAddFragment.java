package com.example.eu7340.egliseteste.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.eu7340.egliseteste.ComunidadeCRUDActivity;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.MembroComunidadeDAO;
import com.example.eu7340.egliseteste.DB.MembroDAO;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.FrequenciaListView;
import com.google.gson.Gson;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembroAddFragment extends Fragment {

    private Comunidade comunidade;

    private AutoCompleteTextView membros;
    private Button bt;

    private Membro membro_selecionado;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_membro_add, container, false);

        Gson gson = new Gson();
        this.comunidade = gson.fromJson(getActivity().getIntent().getStringExtra("comunidade_app"), Comunidade.class);

        membros = view.findViewById(R.id.comunidade_membro);

        CarregarMembros carregarMembros_task = new CarregarMembros(getContext());
        carregarMembros_task.execute();

        bt = view.findViewById(R.id.membroadd_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.setText("Aguarde...");
                bt.setEnabled(false);
                AdicionarMembro adicionarMembro_task = new AdicionarMembro(getContext());
                adicionarMembro_task.execute();
            }
        });

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
    }

    public static MembroAddFragment newInstance() {
        return new MembroAddFragment();
    }

    private class AdicionarMembro extends AsyncTask<Object, Void, Boolean> {

        private Context context;

        public AdicionarMembro(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {

                MembroComunidadeDAO membroComunidadeDAO = new MembroComunidadeDAO(DB.connection);
                Map<String, Object> campos = new HashMap<>();
                campos.put("id_comunidade", comunidade.getId());
                campos.put("id_membro", membro_selecionado.getId());
                List<MembroComunidade> resultado = membroComunidadeDAO.queryForFieldValuesArgs(campos);
                if(resultado != null && !resultado.isEmpty()){
                    MembroComunidade membroComunidade = resultado.get(0);
                    membroComunidade.setAtivo(true);
                    membroComunidadeDAO.update(membroComunidade);
                }else{
                    MembroComunidade membroComunidade = new MembroComunidade();
                    membroComunidade.setAtivo(true);
                    membroComunidade.setComunidade(comunidade);
                    membroComunidade.setMembro(membro_selecionado);
                    membroComunidadeDAO.create(membroComunidade);
                }

                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if(resultado){
                Toast.makeText(getContext(), "Membro adicionado.",
                        Toast.LENGTH_LONG).show();

                ComunidadeCRUDActivity activity = (ComunidadeCRUDActivity) getActivity();
                activity.getSupportFragmentManager().popBackStack();
            }else{
                Toast.makeText(getContext(), "Membro não selecionado corretamente.",
                        Toast.LENGTH_LONG).show();
            }
            bt.setText("Adicionar membro");
            bt.setEnabled(true);
            /*List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
            activity.openFragment(fragments.get(fragments.size() - 1));*/
        }
    }

    private class CarregarMembros extends AsyncTask<Object, Void, ArrayAdapter<Membro>> {

        private Context context;

        public CarregarMembros(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayAdapter<Membro> doInBackground(Object... params) {
            try {
                MembroDAO membroDAO = new MembroDAO(DB.connection);
                GenericRawResults<Membro> membro_ = membroDAO.queryRaw("select tbl_membros.* from tbl_membros left join tbl_membros_comunidades on tbl_membros_comunidades.id_membro = tbl_membros.id where tbl_membros.ativo = true and tbl_membros_comunidades.ativo = false and id_comunidade = " + comunidade.getId(), membroDAO.getRawRowMapper());
                List<Membro> _membros_ = new ArrayList<>();
                if (membro_ != null) _membros_.addAll(membro_.getResults());

                /*Membro[] membros = null;
                membros = _membros_.toArray(membros);*/

                ArrayAdapter<Membro> membros_aa = new ArrayAdapter<Membro>(getActivity(), android.R.layout.simple_dropdown_item_1line, _membros_);
                return membros_aa;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<Membro> membros_aa) {
            if (membros_aa != null){
                membros.setAdapter(membros_aa);
                membros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        membro_selecionado = (Membro) membros.getAdapter().getItem(position);
                    }
                });
                /*membros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        membro_selecionado = (Membro) membros.getAdapter().getItem(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        membro_selecionado = null;
                    }
                });*/
            }
        }
    }

}
