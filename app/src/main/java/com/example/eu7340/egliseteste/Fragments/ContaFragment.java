package com.example.eu7340.egliseteste.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.MembroDAO;
import com.example.eu7340.egliseteste.DB.UsuarioDAO;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.Perfil;
import com.example.eu7340.egliseteste.Models.Usuario;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.jbcrypt.BCrypt;
import com.example.eu7340.egliseteste.utils.CustomDatePicker;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.example.eu7340.egliseteste.utils.ViaCEP;
import com.example.eu7340.egliseteste.utils.ViaCEPException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ContaFragment extends Fragment {

    private Membro membro;
    private Perfil perfil;
    private Usuario usuario;
    private Funcao funcao;

    private EditText nome;
    private TextView funcao_;
    private EditText login;
    private EditText senha;
    private EditText senha_;
    private TextView perfil_;
    private EditText email;
    private EditText telefone;
    private EditText cep;
    private ImageButton bt_cep;
    private EditText bairro;
    private EditText rua;
    private EditText num;
    private EditText complemento;
    private CustomDatePicker dt;
    private Button bt;
    private CheckBox cb;

    private DownloadCEPTask downloadCEPTask_task;
    private AtualizarConta autalizarConta_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conta, container, false);

        usuario = Sessao.ultimo_usuario;
        perfil = Sessao.ultimo_perfil;
        membro = Sessao.ultimo_membro;
        funcao = Sessao.ultima_funcao;

        nome = view.findViewById(R.id.conta_nome);
        nome.setText(membro.getNome());

        funcao_ = view.findViewById(R.id.conta_funcao);
        if(funcao != null) funcao_.setText(funcao.getNome());
        else funcao_.setText("Não definido.");

        login = view.findViewById(R.id.conta_login);
        login.setText(usuario.getEmail());

        perfil_ = view.findViewById(R.id.conta_perfil);
        perfil_.setText(perfil.getNome());

        senha = view.findViewById(R.id.conta_senha);

        senha_ = view.findViewById(R.id.conta_senhac);

        email = view.findViewById(R.id.conta_email);
        email.setText(membro.getEmail());

        telefone = view.findViewById(R.id.conta_telefone);
        telefone.setText(membro.getTelefone());

        dt = view.findViewById(R.id.conta_dt);

        if(membro.getDt_nascimento() != null) {
            SimpleDateFormat sdf_ano = new SimpleDateFormat("yyyy");
            SimpleDateFormat sdf_mes = new SimpleDateFormat("MM");
            SimpleDateFormat sdf_dia = new SimpleDateFormat("dd");

            int ano = Integer.parseInt(sdf_ano.format(membro.getDt_nascimento()));
            int mes = Integer.parseInt(sdf_mes.format(membro.getDt_nascimento()));
            int dia = Integer.parseInt(sdf_dia.format(membro.getDt_nascimento()));

            dt.init(ano, mes, dia, null);
        }else{
            dt.init(0, 0, 0, null);
        }

        cep = view.findViewById(R.id.conta_cep);
        cep.setText(membro.getCep());

        bairro = view.findViewById(R.id.conta_bairro);
        bairro.setText(membro.getBairro());

        rua = view.findViewById(R.id.conta_rua);
        rua.setText(membro.getRua());

        num = view.findViewById(R.id.conta_num);
        num.setText(membro.getNum());

        complemento = view.findViewById(R.id.conta_comp);
        complemento.setText(membro.getComplemento());

        bt_cep = view.findViewById(R.id.contacep_bt);
        bt_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadCEPTask_task = new DownloadCEPTask(bt_cep.getContext());
                downloadCEPTask_task.execute(String.valueOf(cep.getText()));
            }
        });

        bt = view.findViewById(R.id.conta_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.setText("Aguarde...");
                bt.setEnabled(false);
                autalizarConta_task = new AtualizarConta();
                autalizarConta_task.execute();
            }
        });

        cb = view.findViewById(R.id.conta_cb);
        cb.setChecked(false);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dt.setEnabled(!cb.isChecked());
            }
        });

        return view;
    }

    public void onDestroy(){
        super.onDestroy();

        if(downloadCEPTask_task != null) downloadCEPTask_task.cancel(true);
    }

    private class DownloadCEPTask extends AsyncTask<String, Void, ViaCEP> {

        private Context context;

        public DownloadCEPTask(Context context){
            this.context = context;
        }

        @Override
        protected ViaCEP doInBackground(String... ceps) {
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

                membro.setCep(result.getCep());
                membro.setCidade(result.getLocalidade());
                membro.setEstado(result.getUf());
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

    private class AtualizarConta extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object ... params) {

            try {

                membro.setNome(nome.getText().toString());
                usuario.setNome(membro.getNome());
                usuario.setEmail(login.getText().toString());
                membro.setCep(cep.getText().toString());
                membro.setEmail(email.getText().toString());
                membro.setTelefone(telefone.getText().toString());
                membro.setBairro(bairro.getText().toString());
                membro.setRua(rua.getText().toString());
                membro.setNum(num.getText().toString());
                membro.setComplemento(complemento.getText().toString());

                if(!cb.isChecked()) {
                    String dt_ = (dt.getYear() + 1) + "-" + dt.getMonth() + "-" + dt.getDayOfMonth();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    Date _dt_ = new Date(sdf.parse(dt_).getTime());

                    membro.setDt_nascimento(_dt_);
                }else{
                    membro.setDt_nascimento(null);
                }

                if(!senha.getText().toString().isEmpty() && senha.getText().toString().length() >= 8 && senha.getText().toString().equals(senha_.getText().toString())){
                    String senha_ecriptada = BCrypt.hashpw(senha.getText().toString(), BCrypt.gensalt());
                    senha_ecriptada.replace("$2a$","$2y$");
                    usuario.setPassword(senha_ecriptada);
                }else if(!senha.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "A senha não têm 8 caracteres ou não confere.", Toast.LENGTH_LONG);
                }

                MembroDAO membroDAO = new MembroDAO(DB.connection);
                membroDAO.update(membro);
                UsuarioDAO usuarioDAO = new UsuarioDAO(DB.connection);
                usuarioDAO.update(usuario);

                return true;

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getContext(), "Dados da conta atualizados com sucessos!", Toast.LENGTH_LONG);
            }
            bt.setText("Confirmar");
            bt.setEnabled(true);
        }
    }

    public static ContaFragment newInstance() {
        return new ContaFragment();
    }
}
