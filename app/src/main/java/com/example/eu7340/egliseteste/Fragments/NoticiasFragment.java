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

import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Views.NoticiaListView;
import com.example.eu7340.egliseteste.R;
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

public class NoticiasFragment extends Fragment {

    private MyJSONObject configuracao;

    private LinearLayout noticias_area;

    private TextView msg_loading;
    private TextView msg_erro;

    private CarregaNoticias carregaNoticias_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        configuracao = Sessao.ultima_configuracao;

        msg_loading = view.findViewById(R.id.msg_loading);
        msg_erro = view.findViewById(R.id.msg_erro);
        msg_erro.setVisibility(View.GONE);

        noticias_area = view.findViewById(R.id.noticias_area);

        carregaNoticias_task = new CarregaNoticias(view);
        carregaNoticias_task.execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(carregaNoticias_task != null) carregaNoticias_task.cancel(false);
        carregaNoticias_task = new CarregaNoticias(getView());
        carregaNoticias_task.execute();
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaNoticias_task != null) carregaNoticias_task.cancel(false);
    }

    public static NoticiasFragment newInstance() {
        return new NoticiasFragment();
    }

    private class CarregaNoticiasRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaNoticias extends AsyncTask<Congregacao, Object, CarregaNoticiasRetorno> {

        private final View view;

        public CarregaNoticias(View view) {
            this.view = view;
        }

        public CarregaNoticiasRetorno doInBackground(Congregacao... objects) {

            String retorno = null;
            CarregaNoticiasRetorno retorno_ = new CarregaNoticiasRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/noticias/" + configuracao.getString("url"));

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

        protected void onPostExecute(CarregaNoticiasRetorno result) {
            if(!isCancelled()) {
                msg_loading.setVisibility(View.GONE);

                noticias_area.removeAllViews();

                if (result.erro) {
                    Toast.makeText(getContext(), result.mensagem,
                            Toast.LENGTH_LONG).show();
                    msg_erro.setVisibility(View.VISIBLE);
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONArray noticias = new MyJSONArray(result_.getArray("noticias"));
                    for (int x = 0; x < noticias.size(); x++) {
                        final MyJSONObject noticia = new MyJSONObject(noticias.getObjetc(x));
                        NoticiaListView noticia_view = new NoticiaListView(getContext(), null, noticia);
                        noticias_area.addView(noticia_view);
                    }
                }
            }
        }
    }
}
