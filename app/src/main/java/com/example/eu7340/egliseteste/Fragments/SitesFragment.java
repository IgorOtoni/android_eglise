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

import com.example.eu7340.egliseteste.Views.CongregacaoListView;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.MyJSONArray;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.example.eu7340.egliseteste.utils.Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SitesFragment extends Fragment {

    private LinearLayout congregacoes_area;

    private TextView msg_loading;
    private TextView msg_erro;

    private CarregaCongregacoes carrega_congregacoes_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sites, container, false);

        msg_loading = view.findViewById(R.id.msg_loading);
        msg_erro = view.findViewById(R.id.msg_erro);
        msg_erro.setVisibility(View.GONE);

        congregacoes_area = view.findViewById(R.id.congregacoes_area);

        carrega_congregacoes_task = new CarregaCongregacoes(view);
        carrega_congregacoes_task.execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(carrega_congregacoes_task != null) carrega_congregacoes_task.cancel(false);
        carrega_congregacoes_task = new CarregaCongregacoes(getView());
        carrega_congregacoes_task.execute();
    }

    public void onDestroy(){
        super.onDestroy();

        if(carrega_congregacoes_task != null) carrega_congregacoes_task.cancel(false);
    }

    public static SitesFragment newInstance() {
        SitesFragment sitesFragment = new SitesFragment();

        return sitesFragment;
    }

    private class CarregaCongregacoesRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaCongregacoes extends AsyncTask<Object, Object, CarregaCongregacoesRetorno> {

        private final View view;

        public CarregaCongregacoes(View view) {
            this.view = view;
        }

        public CarregaCongregacoesRetorno doInBackground(Object... objects) {

            String retorno = null;
            CarregaCongregacoesRetorno retorno_ = new CarregaCongregacoesRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/sites");

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

        protected void onPostExecute(CarregaCongregacoesRetorno result) {
            if(!isCancelled()) {

                msg_loading.setVisibility(View.GONE);

                congregacoes_area.removeAllViews();

                if (result.erro) {
                    Toast.makeText(view.getContext(), result.mensagem,
                            Toast.LENGTH_LONG).show();
                    msg_erro.setVisibility(View.VISIBLE);
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONArray sites_ = new MyJSONArray(result_.getArray("sites"));
                    MyJSONObject configuracoes_ = new MyJSONObject(result_.getObjetc("configuracoes"));
                    MyJSONObject menus_ = new MyJSONObject(result_.getObjetc("menus"));

                    for (int x = 0; x < sites_.size(); x++) {

                        final MyJSONObject congregacao = new MyJSONObject(sites_.getObjetc(x));
                        final MyJSONObject configuracao = new MyJSONObject(configuracoes_.getObjetc(congregacao.getString("id")));
                        final MyJSONArray menus = new MyJSONArray(menus_.getArray(congregacao.getString("id")));
                        CongregacaoListView congregacao_list = new CongregacaoListView(view.getContext(), null, congregacao, configuracao, menus);
                        congregacoes_area.addView(congregacao_list);
                    }
                }
            }
        }
    }
}
