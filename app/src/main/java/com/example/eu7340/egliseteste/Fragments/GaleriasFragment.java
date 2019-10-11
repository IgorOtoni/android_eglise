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

import com.example.eu7340.egliseteste.Models.Galeria;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.GaleriaListView;
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

public class GaleriasFragment extends Fragment {

    private MyJSONObject configuracao;

    private LinearLayout galerias_area;

    private TextView msg_loading;
    private TextView msg_erro;

    private CarregaGalerias carregaGalerias_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_galerias, container, false);

        this.configuracao = Sessao.ultima_configuracao;

        msg_loading = view.findViewById(R.id.msg_loading);
        msg_erro = view.findViewById(R.id.msg_erro);
        msg_erro.setVisibility(View.GONE);

        galerias_area = view.findViewById(R.id.galerias_area);

        carregaGalerias_task = new CarregaGalerias(view);
        carregaGalerias_task.execute();

        return view;
    }

    public void onResume(){
        super.onResume();

        if(carregaGalerias_task != null) carregaGalerias_task.cancel(false);
        carregaGalerias_task = new CarregaGalerias(getView());
        carregaGalerias_task.execute();
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaGalerias_task != null) carregaGalerias_task.cancel(false);
    }

    public static GaleriasFragment newInstance() {
        return new GaleriasFragment();
    }

    private class DadosGalerias{
        public Galeria galeria;
    }

    private class CarregaGaleriasRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaGalerias extends AsyncTask<Object, Object, CarregaGaleriasRetorno> {

        private final View view;

        public CarregaGalerias(View view) {
            this.view = view;
        }

        public CarregaGaleriasRetorno doInBackground(Object... objects) {

            String retorno = null;
            CarregaGaleriasRetorno retorno_ = new CarregaGaleriasRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/galerias/" + configuracao.getString("url"));

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

        protected void onPostExecute(CarregaGaleriasRetorno result) {
            if(!isCancelled()) {
                msg_loading.setVisibility(View.GONE);

                galerias_area.removeAllViews();

                if (result.erro) {
                    Toast.makeText(getContext(), result.mensagem,
                            Toast.LENGTH_LONG).show();
                    msg_erro.setVisibility(View.VISIBLE);
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONArray galerias = new MyJSONArray(result_.getArray("galerias"));
                    MyJSONObject fotos_ = new MyJSONObject(result_.getObjetc("fotos"));
                    for (int x = 0; x < galerias.size(); x++) {
                        final MyJSONObject galeria = new MyJSONObject(galerias.getObjetc(x));
                        final MyJSONArray fotos = new MyJSONArray(fotos_.getArray(galeria.getString("id")));
                        GaleriaListView evento_view = new GaleriaListView(getContext(), null, galeria, fotos);
                        galerias_area.addView(evento_view);
                    }
                }
            }
        }
    }
}
