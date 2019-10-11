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
import com.example.eu7340.egliseteste.Views.SermaoListView;
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

public class SermoesFragment extends Fragment {

    private MyJSONObject configuracao;

    private LinearLayout sermoes_area;

    private TextView msg_loading;
    private TextView msg_erro;

    private CarregaSermoes carregaSermoes_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sermoes, container, false);

        this.configuracao = Sessao.ultima_configuracao;

        msg_loading = view.findViewById(R.id.msg_loading);
        msg_erro = view.findViewById(R.id.msg_erro);
        msg_erro.setVisibility(View.GONE);

        sermoes_area = view.findViewById(R.id.sermoes_area);

        carregaSermoes_task = new CarregaSermoes(view);
        carregaSermoes_task.execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(carregaSermoes_task != null) carregaSermoes_task.cancel(false);
        carregaSermoes_task = new CarregaSermoes(getView());
        carregaSermoes_task.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(carregaSermoes_task != null) carregaSermoes_task.cancel(false);
    }

    public static SermoesFragment newInstance() {
        return new SermoesFragment();
    }

    private class CarregaSermoesRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaSermoes extends AsyncTask<Object, Object, CarregaSermoesRetorno> {

        private final View view;

        public CarregaSermoes(View view) {
            this.view = view;
        }

        public CarregaSermoesRetorno doInBackground(Object... objects) {

            String retorno = null;
            CarregaSermoesRetorno retorno_ = new CarregaSermoesRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/sermoes/" + configuracao.getString("url"));

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

        protected void onPostExecute(CarregaSermoesRetorno result) {
            if(!isCancelled()) {
                msg_loading.setVisibility(View.GONE);

                sermoes_area.removeAllViews();

                if (result.erro) {
                    Toast.makeText(getContext(), result.mensagem,
                            Toast.LENGTH_LONG).show();
                    msg_erro.setVisibility(View.VISIBLE);
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONArray sermoes = new MyJSONArray(result_.getArray("sermoes"));
                    for (int x = 0; x < sermoes.size(); x++) {
                        final MyJSONObject sermao = new MyJSONObject(sermoes.getObjetc(x));
                        SermaoListView sermao_view = new SermaoListView(getContext(), null, sermao);
                        sermoes_area.addView(sermao_view);
                    }
                }
            }
        }
    }
}
