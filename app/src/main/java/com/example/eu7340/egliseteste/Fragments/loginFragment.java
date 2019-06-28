package com.example.eu7340.egliseteste.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eu7340.egliseteste.AppUsuarioActivity;
import com.example.eu7340.egliseteste.DB.ConfiguracaoDAO;
import com.example.eu7340.egliseteste.DB.CongregacaoDAO;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.FuncaoDAO;
import com.example.eu7340.egliseteste.DB.MembroDAO;
import com.example.eu7340.egliseteste.DB.PerfilDAO;
import com.example.eu7340.egliseteste.DB.UsuarioDAO;
import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.Perfil;
import com.example.eu7340.egliseteste.Models.Usuario;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.jbcrypt.BCrypt;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class LoginFragment extends Fragment {

    private Congregacao congregacao;
    private Configuracao configuracao;

    private EditText email;
    private EditText senha;

    private Button bt;

    private CheckBox cb;

    private ValidarVerificarAutenticar validarVerificarAutenticar_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);
        this.configuracao = gson.fromJson(getActivity().getIntent().getStringExtra("configuracao_app"), Configuracao.class);

        email = view.findViewById(R.id.login_email);

        senha = view.findViewById(R.id.login_senha);

        bt = view.findViewById(R.id.login_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticar(email.getText().toString(), senha.getText().toString(), congregacao, configuracao);
            }
        });

        cb = view.findViewById(R.id.login_cb);

        try{
            File directory = getActivity().getFilesDir();
            File file = new File(directory, "elgise.auth");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linha_1 = br.readLine();
            String linha_2 = br.readLine();
            if(linha_1.contains("Lembrar: 1")){
                String login = linha_2.replace("Login: ", "").replace("\n", "");
                email.setText(login);
                cb.setChecked(true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void onDestroyView(){
        super.onDestroyView();

        if(validarVerificarAutenticar_task != null) validarVerificarAutenticar_task.cancel(true);
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private class DadosParaAutenticacao{
        public Congregacao congregacao;
        public Configuracao configuracao;
        public String login;
        public String senha;
    }

    private class DadosParaLogin{
        public Congregacao congregacao;
        public Configuracao configuracao;
        public Usuario usuario;
        public Perfil perfil;
        public Membro membro;
        public Funcao funcao;
    }

    public void autenticar(String login, String senha, Congregacao congregacao, Configuracao configuracao){
        validarVerificarAutenticar_task = new ValidarVerificarAutenticar();
        DadosParaAutenticacao dadosParaAutenticacao = new DadosParaAutenticacao();
        dadosParaAutenticacao.congregacao = congregacao;
        dadosParaAutenticacao.configuracao = configuracao;
        dadosParaAutenticacao.login = login;
        dadosParaAutenticacao.senha = senha;
        validarVerificarAutenticar_task.execute(dadosParaAutenticacao);
    }

    private class ValidarVerificarAutenticar extends AsyncTask<DadosParaAutenticacao, Void, DadosParaLogin> {

        public ValidarVerificarAutenticar(){
            bt.setEnabled(false);
            bt.setText("Aguarde...");
        }

        @Override
        protected DadosParaLogin doInBackground(DadosParaAutenticacao... params) {
            try {

                UsuarioDAO usuarioDAO = new UsuarioDAO(DB.connection);
                List<Usuario> resultado = usuarioDAO.queryForEq("email", params[0].login);

                if(resultado != null && resultado.size() == 1){
                    Usuario usuario = resultado.get(0);

                    PerfilDAO perfilDAO = new PerfilDAO(DB.connection);
                    Perfil perfil = perfilDAO.queryForId(usuario.getPerfil().getId());
                    CongregacaoDAO congregacaoDAO = new CongregacaoDAO(DB.connection);
                    Congregacao congregacao = congregacaoDAO.queryForId(perfil.getCongregacao().getId());
                    ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO(DB.connection);
                    Configuracao configuracao = configuracaoDAO.queryForEq("id_igreja", congregacao.getId()).get(0);
                    MembroDAO membroDAO = new MembroDAO(DB.connection);
                    Membro membro = membroDAO.queryForId(usuario.getMembro().getId());
                    Funcao funcao = null;
                    FuncaoDAO funcaoDAO = new FuncaoDAO(DB.connection);
                    if(membro.getFuncao() != null) funcao = funcaoDAO.queryForId(membro.getFuncao().getId());

                    DadosParaLogin dados = new DadosParaLogin();
                    dados.usuario = usuario;
                    dados.perfil = perfil;
                    dados.congregacao = congregacao;
                    dados.configuracao = configuracao;
                    dados.membro = membro;
                    dados.funcao = funcao;

                    if(BCrypt.checkpw(params[0].senha, usuario.getPassword().replace("$2y$","$2a$"))){

                        return dados;
                    }
                }

            }catch (SQLException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(DadosParaLogin dados) {
            if(dados != null){

                Toast.makeText(getContext(), "Bem vindo " + dados.usuario.getNome() + "!",
                        Toast.LENGTH_LONG).show();

                Gson gson = new Gson();
                String usuario_json = gson.toJson(dados.usuario);
                String perfil_json = gson.toJson(dados.perfil);
                String congregacao_json = gson.toJson(dados.congregacao);
                String configuracao_json = gson.toJson(dados.configuracao);
                String membro_json = gson.toJson(dados.membro);
                String funcao_json = null;
                if(dados.funcao != null) funcao_json = gson.toJson(dados.funcao);

                Intent intent = new Intent(getContext(), AppUsuarioActivity.class);

                intent.putExtra("usuario_app", usuario_json);
                intent.putExtra("perfil_app", perfil_json);
                intent.putExtra("congregacao_app", congregacao_json);
                intent.putExtra("configuracao_app", configuracao_json);
                intent.putExtra("membro_app", membro_json);
                if(funcao_json != null) intent.putExtra("funcao_app", funcao_json);

                if(cb.isChecked()){
                    try {
                        File directory = getActivity().getFilesDir();
                        File file = new File(directory, "elgise.auth");
                        if(!file.exists()) file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file);
                        String auth = "Lembrar: 1\nLogin: " + dados.usuario.getEmail();
                        fos.write(auth.getBytes());
                        fos.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    File directory = getActivity().getFilesDir();
                    File file = new File(directory, "elgise.auth");
                    file.delete();
                }

                Sessao.existe_login_ativo = true;

                startActivity(intent);

            }else{
                Toast.makeText(getContext(), "Senha inválida!",
                        Toast.LENGTH_LONG).show();
            }

            bt.setEnabled(true);
            bt.setText("Autenticar");
        }
    }
}
