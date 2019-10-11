package com.example.eu7340.egliseteste;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.FrequenciaDAO;
import com.example.eu7340.egliseteste.DB.MembroComunidadeDAO;
import com.example.eu7340.egliseteste.DB.MembroDAO;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Frequencia;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.Models.Reuniao;
import com.example.eu7340.egliseteste.Views.FrequenciaListView;
import com.google.gson.Gson;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequenciaActivity extends AppCompatActivity {

    private Comunidade comunidade;
    private Reuniao reuniao;

    private LinearLayout frequencias_;
    private TextView data;

    private CarregaFrequencias carregaFrequencias_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequencia);

        getSupportActionBar().setTitle("Frequência");

        Gson gson = new Gson();
        comunidade = gson.fromJson(getIntent().getStringExtra("comunidade_frequencia"), Comunidade.class);
        reuniao = gson.fromJson(getIntent().getStringExtra("reuniao_frequencia"), Reuniao.class);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        data = findViewById(R.id.frequencia_data);
        data.setText(sdf.format(reuniao.getInicio()));

        frequencias_ = findViewById(R.id.presenca_layout);

        carregaFrequencias_task = new CarregaFrequencias(this);
        carregaFrequencias_task.execute();
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaFrequencias_task != null) carregaFrequencias_task.cancel(true);
    }

    private class CarregaFrequencias extends AsyncTask<Object, Void, List<FrequenciaListView>> {

        private Context context;

        public CarregaFrequencias(Context context){
            this.context = context;
        }

        @Override
        protected List<FrequenciaListView> doInBackground(Object... params) {
            try {
                List<FrequenciaListView> frequencias_list = new ArrayList<>();

                MembroComunidadeDAO membroComunidadeDAO = new MembroComunidadeDAO(DB.connection);
                GenericRawResults<MembroComunidade> membro_da_comunidade_ = membroComunidadeDAO.queryRaw("select tbl_membros_comunidades.* from tbl_membros_comunidades inner join tbl_membros on tbl_membros_comunidades.id_membro = tbl_membros.id where tbl_membros.ativo = true and tbl_membros_comunidades.ativo = true and id_comunidade = " + comunidade.getId(), membroComunidadeDAO.getRawRowMapper());
                List<MembroComunidade> membro_da_comunidade = new ArrayList<>();
                if(membro_da_comunidade_ != null) membro_da_comunidade.addAll(membro_da_comunidade_.getResults());

                FrequenciaDAO frequenciaDAO = new FrequenciaDAO(DB.connection);
                MembroDAO membroDAO = new MembroDAO(DB.connection);

                for(int x = 0; x < membro_da_comunidade.size(); x++){
                    Membro membro_frequencia = membroDAO.queryForId(membro_da_comunidade.get(x).getMembro().getId());

                    Map<String, Object> campos = new HashMap<>();
                    campos.put("id_reuniao", reuniao.getId());
                    campos.put("id_membro_comunidade", membro_da_comunidade.get(x).getId());
                    List<Frequencia> resp = frequenciaDAO.queryForFieldValues(campos);

                    Frequencia frequencia;

                    if(resp != null && !resp.isEmpty()){
                        frequencia = resp.get(0);
                    }else{
                        frequencia = new Frequencia();
                        frequencia.setId(-1);
                        frequencia.setMembroComunidade(membro_da_comunidade.get(x));
                        frequencia.setReuniao(reuniao);
                        frequencia.setAusente(false);
                        int id_frequencia = frequenciaDAO.create(frequencia);
                        frequencia.setId(id_frequencia);
                    }

                    /*FrequenciaListView frequencia_list = new FrequenciaListView(context, null, frequencia, membro_frequencia);
                    frequencias_list.add(frequencia_list);*/
                }

                return frequencias_list;
            }catch (SQLException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<FrequenciaListView> frequencias) {
            if(frequencias != null) for(int x = 0; x < frequencias.size(); x++) frequencias_.addView(frequencias.get(x));
        }
    }
}
