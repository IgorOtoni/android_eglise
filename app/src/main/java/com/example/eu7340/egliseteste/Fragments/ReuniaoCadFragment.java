package com.example.eu7340.egliseteste.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.eu7340.egliseteste.ComunidadeCRUDActivity;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.ReuniaoDAO;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Reuniao;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.CustomDatePicker;
import com.example.eu7340.egliseteste.utils.ViaCEP;
import com.example.eu7340.egliseteste.utils.ViaCEPException;
import com.google.gson.Gson;

public class ReuniaoCadFragment extends Fragment {

    private Comunidade comunidade;

    private Reuniao reuniao_nova = new Reuniao();

    private EditText cep;
    private ImageButton bt_cep;
    private EditText bairro;
    private EditText rua;
    private EditText num;
    private EditText complemento;
    private CustomDatePicker dt_inicio;
    private CustomDatePicker dt_fim;
    private TimePicker h_inicio;
    private TimePicker h_fim;
    private EditText descricao;
    private EditText observacao;
    private Button bt;

    private DownloadCEPTask downloadCEPTask_task;
    private CadastrarReuniao cadastrarReuniao_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        reuniao_nova = new Reuniao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reuniao_cad, container, false);

        Gson gson = new Gson();
        this.comunidade = gson.fromJson(getActivity().getIntent().getStringExtra("comunidade_app"), Comunidade.class);

        reuniao_nova.setComunidade(comunidade);

        cep = view.findViewById(R.id.reuniao_cep);

        bairro = view.findViewById(R.id.reuniao_bairro);

        rua = view.findViewById(R.id.reuniao_rua);

        num = view.findViewById(R.id.reuniao_num);

        complemento = view.findViewById(R.id.reuniao_comp);

        dt_inicio = view.findViewById(R.id.reuniao_dt_inicio);

        dt_fim = view.findViewById(R.id.reuniao_dt_fim);

        h_inicio = view.findViewById(R.id.reuniao_hr_inicio);

        h_fim = view.findViewById(R.id.reuniao_hr_fim);

        descricao = view.findViewById(R.id.reuniao_desc);

        observacao = view.findViewById(R.id.reuniao_obs);

        bt_cep = view.findViewById(R.id.reuniaocep_bt);
        bt_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadCEPTask_task = new DownloadCEPTask();
                downloadCEPTask_task.execute(String.valueOf(cep.getText()));
            }
        });

        bt = view.findViewById(R.id.reuniaocad_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.setText("Aguarde...");
                bt.setEnabled(false);
                cadastrarReuniao_task = new CadastrarReuniao();
                cadastrarReuniao_task.execute();
            }
        });

        return view;
    }

    public void onDestroy(){
        super.onDestroy();

        if(downloadCEPTask_task != null) downloadCEPTask_task.cancel(true);
        if(cadastrarReuniao_task != null) cadastrarReuniao_task.cancel(true);
    }

    public static ReuniaoCadFragment newInstance() {
        return new ReuniaoCadFragment();
    }

    private class DownloadCEPTask extends AsyncTask<String, Void, ViaCEP> {
        @Override
        protected ViaCEP doInBackground(String ... ceps) {
            ViaCEP vCep = null;

            try {
                vCep = new ViaCEP(ceps[0].replace(".","").replace("-",""));
            } catch (ViaCEPException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return vCep;
        }

        @Override
        protected void onPostExecute(ViaCEP result) {
            if (result != null) {
                bairro.setText(result.getBairro());
                complemento.setText(result.getComplemento());
                rua.setText(result.getLogradouro());

                reuniao_nova.setCep(result.getCep());
                reuniao_nova.setCidade(result.getLocalidade());
                //txtGia.setText(result.getGia());
                //txtIbge.setText(result.getIbge());
                //txtLocalidade.setText(result.getLocalidade());
                //txtLogradouro.setText(result.getLogradouro());
                //txtUf.setText(result.getUf());
            }else{
                Toast.makeText(getContext(), "CEP inválido!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CadastrarReuniao extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object ... args) {

            try{
                reuniao_nova.setBairro(bairro.getText().toString());
                reuniao_nova.setComplemento(complemento.getText().toString());
                reuniao_nova.setRua(rua.getText().toString());
                reuniao_nova.setNum(num.getText().toString());

                reuniao_nova.setDescricao(descricao.getText().toString());
                reuniao_nova.setObservacao(observacao.getText().toString());

                String dt_inicio_ = (dt_inicio.getYear() + 1) + "-" + dt_inicio.getMonth() + "-" + dt_inicio.getDayOfMonth();
                String dt_fim_ = (dt_fim.getYear() + 1) + "-" + dt_fim.getMonth() + "-" + dt_fim.getDayOfMonth();

                String h_inicio_ = h_inicio.getHour() + ":" + h_inicio.getMinute();
                String h_fim_ = h_fim.getHour() + ":" + h_fim.getMinute();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                Date _dt_inicio_ = sdf.parse(dt_inicio_ + " " + h_inicio_);
                Date _dt_fim_ = sdf.parse(dt_fim_ + " " + h_fim_);

                reuniao_nova.setInicio(_dt_inicio_);
                reuniao_nova.setFim(_dt_fim_);

                ReuniaoDAO reuniaoDAO = new ReuniaoDAO(DB.connection);
                reuniaoDAO.create(reuniao_nova);

                return true;
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            catch(NullPointerException ex){
                ex.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getContext(), "Reunião adicionada com sucesso!",
                        Toast.LENGTH_LONG).show();
                ComunidadeCRUDActivity activity = (ComunidadeCRUDActivity) getActivity();
                activity.getSupportFragmentManager().popBackStack();
            }else{
                Toast.makeText(getContext(), "Dados inválidos!",
                        Toast.LENGTH_LONG).show();
            }
            bt.setText("Adicionar reunião");
            bt.setEnabled(true);
        }
    }

}
