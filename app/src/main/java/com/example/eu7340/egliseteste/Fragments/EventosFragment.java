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

import com.example.eu7340.egliseteste.Views.EventoListView;
import com.example.eu7340.egliseteste.Models.Congregacao;
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

public class EventosFragment extends Fragment {

    private MyJSONObject configuracao;

    private LinearLayout eventos_area;

    private TextView msg_loading;
    private TextView msg_erro;

    private CarregaEventos carregaEventos_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);

        this.configuracao = Sessao.ultima_configuracao;

        msg_loading = view.findViewById(R.id.msg_loading);
        msg_erro = view.findViewById(R.id.msg_erro);
        msg_erro.setVisibility(View.GONE);

        eventos_area = view.findViewById(R.id.eventos_area);

        carregaEventos_task = new CarregaEventos(view);
        carregaEventos_task.execute();

        return view;
    }

    public void onResume(){
        super.onResume();

        if(carregaEventos_task != null) carregaEventos_task.cancel(true);
        carregaEventos_task = new CarregaEventos(getView());
        carregaEventos_task.execute();
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaEventos_task != null) carregaEventos_task.cancel(true);
    }

    private class CarregaEventosRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    public static EventosFragment newInstance() {
        return new EventosFragment();
    }

    private class CarregaEventos extends AsyncTask<Congregacao, Object, CarregaEventosRetorno> {

        private final View view;

        public CarregaEventos(View view) {
            this.view = view;
        }

        public CarregaEventosRetorno doInBackground(Congregacao... objects) {

            String retorno = null;
            CarregaEventosRetorno retorno_ = new CarregaEventosRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/eventos/" + configuracao.getString("url"));

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

        protected void onPostExecute(CarregaEventosRetorno result) {
            if(!isCancelled()) {
                msg_loading.setVisibility(View.GONE);

                eventos_area.removeAllViews();

                if (result.erro) {
                    Toast.makeText(getContext(), result.mensagem,
                            Toast.LENGTH_LONG).show();
                    msg_erro.setVisibility(View.VISIBLE);
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONArray eventos = new MyJSONArray(result_.getArray("eventos"));
                    for (int x = 0; x < eventos.size(); x++) {
                        final MyJSONObject evento = new MyJSONObject(eventos.getObjetc(x));
                        EventoListView evento_view = new EventoListView(getContext(), null, evento);
                        eventos_area.addView(evento_view);
                    }
                }
            }
        }
    }
}
