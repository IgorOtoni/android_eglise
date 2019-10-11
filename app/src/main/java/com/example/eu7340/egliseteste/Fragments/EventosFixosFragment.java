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

import com.example.eu7340.egliseteste.Views.EventoFixoListView;
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

public class EventosFixosFragment extends Fragment {

    private MyJSONObject configuracao;

    private LinearLayout eventosfixos_area;

    private TextView msg_loading;
    private TextView msg_erro;

    private CarregaEventosFixos carregaEventosFixos_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos_fixos, container, false);

        this.configuracao = Sessao.ultima_configuracao;

        msg_loading = view.findViewById(R.id.msg_loading);
        msg_erro = view.findViewById(R.id.msg_erro);
        msg_erro.setVisibility(View.GONE);

        eventosfixos_area = view.findViewById(R.id.eventosfixos_area);

        carregaEventosFixos_task = new CarregaEventosFixos(view);
        carregaEventosFixos_task.execute();

        return view;
    }

    public void onResume(){
        super.onResume();

        if(carregaEventosFixos_task != null) carregaEventosFixos_task.cancel(false);
        carregaEventosFixos_task = new CarregaEventosFixos(getView());
        carregaEventosFixos_task.execute();
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaEventosFixos_task != null) carregaEventosFixos_task.cancel(false);
    }

    public static EventosFixosFragment newInstance() {
        return new EventosFixosFragment();
    }

    private class CarregaEventosFixosRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaEventosFixos extends AsyncTask<Object, Object, CarregaEventosFixosRetorno> {

        private final View view;

        public CarregaEventosFixos(View view) {
            this.view = view;
        }

        public CarregaEventosFixosRetorno doInBackground(Object... objects) {

            String retorno = null;
            CarregaEventosFixosRetorno retorno_ = new CarregaEventosFixosRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/eventosfixos/" + configuracao.getString("url"));

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

        protected void onPostExecute(CarregaEventosFixosRetorno result) {
            if(!isCancelled()) {
                msg_loading.setVisibility(View.GONE);

                eventosfixos_area.removeAllViews();

                if (result.erro) {
                    Toast.makeText(getContext(), result.mensagem,
                            Toast.LENGTH_LONG).show();
                    msg_erro.setVisibility(View.VISIBLE);
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONArray eventosfixos = new MyJSONArray(result_.getArray("eventosfixos"));
                    for (int x = 0; x < eventosfixos.size(); x++) {
                        final MyJSONObject eventofixo = new MyJSONObject(eventosfixos.getObjetc(x));
                        EventoFixoListView eventofixo_view = new EventoFixoListView(getContext(), null, eventofixo);
                        eventosfixos_area.addView(eventofixo_view);
                    }
                }
            }
        }
    }
}
