package com.example.eu7340.egliseteste.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.PublicacaoListView;
import com.example.eu7340.egliseteste.utils.MyJSONArray;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.example.eu7340.egliseteste.utils.Servidor;
import com.example.eu7340.egliseteste.utils.Sessao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PublicacoesFragment extends Fragment {

    private MyJSONObject configuracao;

    private LinearLayout publicacoes_area;

    private TextView msg_loading;
    private TextView msg_erro;

    private CarregaPublicacoes carregaPublicacoes_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_publicacoes, container, false);

        this.configuracao = Sessao.ultima_configuracao;

        msg_loading = view.findViewById(R.id.msg_loading);
        msg_erro = view.findViewById(R.id.msg_erro);
        msg_erro.setVisibility(View.GONE);

        publicacoes_area = view.findViewById(R.id.publicacoes_area);

        carregaPublicacoes_task = new CarregaPublicacoes(view);
        carregaPublicacoes_task.execute();

        return view;
    }

    public void onResume(){
        super.onResume();

        if(carregaPublicacoes_task != null) carregaPublicacoes_task.cancel(true);
        carregaPublicacoes_task = new CarregaPublicacoes(getView());
        carregaPublicacoes_task.execute();
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaPublicacoes_task != null) carregaPublicacoes_task.cancel(true);
    }

    public static PublicacoesFragment newInstance() { return new PublicacoesFragment(); }

    private class CarregaPublicacoesRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaPublicacoes extends AsyncTask<Object, Object, CarregaPublicacoesRetorno> {

        private final View view;

        public CarregaPublicacoes(View view) {
            this.view = view;
        }

        public CarregaPublicacoesRetorno doInBackground(Object... objects) {

            String retorno = null;
            CarregaPublicacoesRetorno retorno_ = new CarregaPublicacoesRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/publicacoes/" + configuracao.getString("url"));

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(Servidor.max_time_con);
                conn.setConnectTimeout(Servidor.max_time_con);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String response = null;
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        if(response == null) response = line;
                        else response += line;
                    }
                    retorno_.erro = false;
                    retorno_.retorno = response;
                }else {
                    retorno = "Response: " + responseCode;
                }
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                retorno =  e.getMessage();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                retorno =  e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                retorno =  e.getMessage();
            }

            retorno_.mensagem = retorno;
            return retorno_;
        }

        protected void onPostExecute(CarregaPublicacoesRetorno result) {
            if(!isCancelled()) {
                msg_loading.setVisibility(View.GONE);

                publicacoes_area.removeAllViews();

                if (result.erro) {
                    Toast.makeText(getContext(), result.mensagem,
                            Toast.LENGTH_LONG).show();
                    msg_erro.setVisibility(View.VISIBLE);
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONArray publicacoes = new MyJSONArray(result_.getArray("publicacoes"));
                    for (int x = 0; x < publicacoes.size(); x++) {
                        final MyJSONObject publicacao = new MyJSONObject(publicacoes.getObjetc(x));
                        PublicacaoListView publicacao_view = new PublicacaoListView(getContext(), null, publicacao);
                        publicacoes_area.addView(publicacao_view);
                    }
                }
            }
        }
    }
}
