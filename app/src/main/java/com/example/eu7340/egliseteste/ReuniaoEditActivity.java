package com.example.eu7340.egliseteste;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.ReuniaoDAO;
import com.example.eu7340.egliseteste.Fragments.ReuniaoCadFragment;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Reuniao;
import com.example.eu7340.egliseteste.utils.CustomDatePicker;
import com.example.eu7340.egliseteste.utils.ViaCEP;
import com.example.eu7340.egliseteste.utils.ViaCEPException;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReuniaoEditActivity extends AppCompatActivity {

    private Reuniao reuniao;

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
    private EditarReuniao editarReuniao_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao_edit);

        getSupportActionBar().setTitle("Editar reunião");

        Gson gson = new Gson();
        reuniao = gson.fromJson(getIntent().getStringExtra("reuniao_edit"), Reuniao.class);

        cep = findViewById(R.id.reuniao_cep);
        cep.setText(reuniao.getCep());

        bairro = findViewById(R.id.reuniao_bairro);
        bairro.setText(reuniao.getBairro());

        rua = findViewById(R.id.reuniao_rua);
        rua.setText(reuniao.getRua());

        num = findViewById(R.id.reuniao_num);
        num.setText(reuniao.getNum());

        complemento = findViewById(R.id.reuniao_comp);
        complemento.setText(reuniao.getComplemento());

        SimpleDateFormat sdf_ano = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf_mes = new SimpleDateFormat("MM");
        SimpleDateFormat sdf_dia = new SimpleDateFormat("dd");
        SimpleDateFormat sdf_hora = new SimpleDateFormat("HH");
        SimpleDateFormat sdf_minuto = new SimpleDateFormat("mm");

        int ano_inicio = Integer.parseInt(sdf_ano.format(reuniao.getInicio()));
        int mes_inicio = Integer.parseInt(sdf_mes.format(reuniao.getInicio()));
        int dia_inicio = Integer.parseInt(sdf_dia.format(reuniao.getInicio()));
        int hora_inicio = Integer.parseInt(sdf_hora.format(reuniao.getInicio()));
        int minuto_inicio = Integer.parseInt(sdf_minuto.format(reuniao.getInicio()));

        int ano_fim = Integer.parseInt(sdf_ano.format(reuniao.getFim()));
        int mes_fim = Integer.parseInt(sdf_mes.format(reuniao.getFim()));
        int dia_fim = Integer.parseInt(sdf_dia.format(reuniao.getFim()));
        int hora_fim = Integer.parseInt(sdf_hora.format(reuniao.getFim()));
        int minuto_fim = Integer.parseInt(sdf_minuto.format(reuniao.getFim()));

        dt_inicio = findViewById(R.id.reuniao_dt_inicio);
        dt_inicio.init(ano_inicio, mes_inicio, dia_inicio, null);

        dt_fim = findViewById(R.id.reuniao_dt_fim);
        dt_fim.init(ano_fim, mes_fim, dia_fim, null);

        h_inicio = findViewById(R.id.reuniao_hr_inicio);
        h_inicio.setHour(hora_inicio);
        h_inicio.setMinute(minuto_inicio);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            h_inicio.validateInput();
        }

        h_fim = findViewById(R.id.reuniao_hr_fim);
        h_fim.setHour(hora_fim);
        h_fim.setMinute(minuto_fim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            h_fim.validateInput();
        }

        descricao = findViewById(R.id.reuniao_desc);
        descricao.setText(reuniao.getDescricao());

        observacao = findViewById(R.id.reuniao_obs);
        observacao.setText(reuniao.getObservacao());

        bt_cep = findViewById(R.id.reuniaocep_bt);
        bt_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Pattern pattern = Pattern.compile("^[0-9]{5}-[0-9]{3}$");
                Matcher matcher = pattern.matcher(cep.getText());

                if (matcher.find()) {*/
                downloadCEPTask_task = new DownloadCEPTask(bt_cep.getContext());
                downloadCEPTask_task.execute(String.valueOf(cep.getText()));
                /*}else{

                }*/
            }
        });

        bt = findViewById(R.id.reuniaocad_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.setText("Aguarde...");
                bt.setEnabled(false);
                editarReuniao_task = new EditarReuniao(bt.getContext());
                editarReuniao_task.execute();
            }
        });
    }

    private class DownloadCEPTask extends AsyncTask<String, Void, ViaCEP> {

        private Context context;

        public DownloadCEPTask(Context context){
            this.context = context;
        }

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

                reuniao.setCep(result.getCep());
                reuniao.setCidade(result.getLocalidade());
                //txtGia.setText(result.getGia());
                //txtIbge.setText(result.getIbge());
                //txtLocalidade.setText(result.getLocalidade());
                //txtLogradouro.setText(result.getLogradouro());
                //txtUf.setText(result.getUf());
            }else{
                Toast.makeText(context, "CEP inválido!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class EditarReuniao extends AsyncTask<Object, Void, Boolean> {

        private Context context;

        public EditarReuniao(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Object ... args) {

            try{
                reuniao.setBairro(bairro.getText().toString());
                reuniao.setComplemento(complemento.getText().toString());
                reuniao.setRua(rua.getText().toString());
                reuniao.setNum(num.getText().toString());

                reuniao.setDescricao(descricao.getText().toString());
                reuniao.setObservacao(observacao.getText().toString());

                String dt_inicio_ = dt_inicio.getYear() + "-" + dt_inicio.getMonth() + "-" + dt_inicio.getDayOfMonth();
                String dt_fim_ = dt_fim.getYear() + "-" + dt_fim.getMonth() + "-" + dt_fim.getDayOfMonth();

                String h_inicio_ = h_inicio.getHour() + ":" + h_inicio.getMinute();
                String h_fim_ = h_fim.getHour() + ":" + h_fim.getMinute();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                Date _dt_inicio_ = sdf.parse(dt_inicio_ + " " + h_inicio_);
                Date _dt_fim_ = sdf.parse(dt_fim_ + " " + h_fim_);

                reuniao.setInicio(_dt_inicio_);
                reuniao.setFim(_dt_fim_);

                ReuniaoDAO reuniaoDAO = new ReuniaoDAO(DB.connection);
                reuniaoDAO.update(reuniao);

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
                Toast.makeText(context, "Reunião editada com sucesso!", Toast.LENGTH_LONG).show();

                finish();
            }else{
                Toast.makeText(context, "Dados inválidos!",
                        Toast.LENGTH_LONG).show();
            }
            bt.setText("Adicionar reunião");
            bt.setEnabled(true);
        }
    }

    public void onBackPressed(){
        if(downloadCEPTask_task != null) downloadCEPTask_task.cancel(true);
        if(editarReuniao_task != null) editarReuniao_task.cancel(true);

        finish();
    }

}
