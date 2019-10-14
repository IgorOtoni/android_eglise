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
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.ProdutoListView;
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

public class ProdutosFragment extends Fragment {

    private MyJSONObject configuracao;

    private LinearLayout produtos_area;

    private TextView msg_loading;
    private TextView msg_erro;

    private CarregaProdutos carregaProdutos_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        configuracao = Sessao.ultima_configuracao;

        msg_loading = view.findViewById(R.id.msg_loading);
        msg_erro = view.findViewById(R.id.msg_erro);
        msg_erro.setVisibility(View.GONE);

        produtos_area = view.findViewById(R.id.produtos_area);

        carregaProdutos_task = new CarregaProdutos(view);
        carregaProdutos_task.execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(carregaProdutos_task != null) carregaProdutos_task.cancel(false);
        carregaProdutos_task = new CarregaProdutos(getView());
        carregaProdutos_task.execute();
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaProdutos_task != null) carregaProdutos_task.cancel(false);
    }

    public static ProdutosFragment newInstance() {
        return new ProdutosFragment();
    }

    private class CarregaProdutosRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaProdutos extends AsyncTask<Congregacao, Object, CarregaProdutosRetorno> {

        private final View view;

        public CarregaProdutos(View view) {
            this.view = view;
        }

        public CarregaProdutosRetorno doInBackground(Congregacao... objects) {

            String retorno = null;
            CarregaProdutosRetorno retorno_ = new CarregaProdutosRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/produtos/" + configuracao.getString("url"));

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

        protected void onPostExecute(CarregaProdutosRetorno result) {
            if(!isCancelled()) {
                msg_loading.setVisibility(View.GONE);

                produtos_area.removeAllViews();

                if (result.erro) {
                    Toast.makeText(getContext(), result.mensagem,
                            Toast.LENGTH_LONG).show();
                    msg_erro.setVisibility(View.VISIBLE);
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONArray produtos = new MyJSONArray(result_.getArray("produtos"));
                    MyJSONObject categorias = new MyJSONObject(result_.getObjetc("categorias"));
                    MyJSONObject ofertas = new MyJSONObject(result_.getObjetc("ofertas"));
                    for (int x = 0; x < produtos.size(); x++) {
                        final MyJSONObject produto = new MyJSONObject(produtos.getObjetc(x));
                        final MyJSONObject categoria = new MyJSONObject(categorias.getObjetc(produto.getString("id_categoria")));
                        final MyJSONObject oferta = new MyJSONObject(ofertas.getObjetc(produto.getString("id")));
                        ProdutoListView produto_view = new ProdutoListView(getContext(), null, produto, categoria, oferta);
                        produtos_area.addView(produto_view);
                    }
                }
            }
        }
    }
}
