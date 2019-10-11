package com.example.eu7340.egliseteste.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.ApresentacaoListView;
import com.example.eu7340.egliseteste.utils.MyJSONArray;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.example.eu7340.egliseteste.utils.Servidor;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.example.eu7340.egliseteste.utils.SliderAdapter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class HomeFragment extends Fragment {

    private MyJSONObject congregacao;
    private MyJSONObject configuracao;
    private Timer timer;

    private ViewPager viewPager;
    private TabLayout indicator;

    private List<Bitmap> fotos;
    private List<String> titulos;
    private List<String> descricoes;

    private LinearLayout map;
    private LinearLayout linearLayout;
    private LinearLayout membros_area;

    private CarregaBanners carregaBanners_task;
    private CarregaMembrosImportantes carregaMembrosImportantes_task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider, container, false);

        Gson gson = new Gson();
        this.congregacao = Sessao.ultima_congregacao;
        this.configuracao = Sessao.ultima_configuracao;

        viewPager = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicator);

        carregaBanners_task = new CarregaBanners();
        carregaBanners_task.execute(congregacao);

        TextView apresentacao = view.findViewById(R.id.congregacao_apresentacao);
        if(configuracao.getString("texto_apresentativo") != null) {
            apresentacao.setText(configuracao.getString("texto_apresentativo"));
        }else{
            apresentacao.setVisibility(View.GONE);
        }

        TextView telefone = view.findViewById(R.id.congregacao_telefone);
        if(congregacao.getString("telefone") != null) {
            telefone.setText(congregacao.getString("telefone"));
        }else{
            telefone.setVisibility(View.GONE);
        }

        TextView email = view.findViewById(R.id.congregacao_email);
        if(congregacao.getString("email") != null) {
            email.setText(congregacao.getString("email"));
        }else{
            email.setVisibility(View.GONE);
        }

        linearLayout = view.findViewById(R.id.main_linear_layout);

        membros_area = view.findViewById(R.id.membros_area);

        carregaMembrosImportantes_task = new CarregaMembrosImportantes(view);
        carregaMembrosImportantes_task.execute();

        map = view.findViewById(R.id.map_container);

        if(congregacao.getString("cep") != null) openFragment(MapSiteFragment.newInstance());
        else map.setVisibility(View.GONE);

        return view;
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaBanners_task != null) carregaBanners_task.cancel(false);
        if(carregaMembrosImportantes_task != null) carregaMembrosImportantes_task.cancel(false);
        if(timer != null) timer.cancel();
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private class CarregaMembrosImportantesRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaMembrosImportantes extends AsyncTask<Object, Void, CarregaMembrosImportantesRetorno> {

        private View view;

        public CarregaMembrosImportantes(View view){
            this.view = view;
        }

        @Override
        protected CarregaMembrosImportantesRetorno doInBackground(Object... params) {
            String retorno = null;
            CarregaMembrosImportantesRetorno retorno_ = new CarregaMembrosImportantesRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/apresentacao/" + configuracao.getString("url"));

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

        @Override
        protected void onPostExecute(CarregaMembrosImportantesRetorno result) {
            MyJSONObject result_ = new MyJSONObject(result.retorno);
            MyJSONArray funcoes = new MyJSONArray(result_.getArray("funcoes"));
            MyJSONObject membros_ = new MyJSONObject(result_.getObjetc("membros"));
            boolean existe_membro = false;
            for(int x = 0; x < funcoes.size(); x++){
                MyJSONObject funcao = new MyJSONObject(funcoes.getObjetc(x));
                MyJSONArray membros = new MyJSONArray(membros_.getArray(funcao.getString("id")));
                for(int y = 0; y < membros.size(); y++) {
                    existe_membro = true;
                    MyJSONObject membro = new MyJSONObject(membros.getObjetc(y));
                    ApresentacaoListView apresentacaoListView = new ApresentacaoListView(getContext(), null, membro, funcao);
                    membros_area.addView(apresentacaoListView);
                }
            }
            if(!existe_membro){
                TextView titulo = view.findViewById(R.id.congregacao_equipe_titulo);
                titulo.setVisibility(View.GONE);
                membros_area.setVisibility(View.GONE);
            }
            return;
        }
    }

    private class CarregaBannersRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaBanners extends AsyncTask<Object, Void, CarregaBannersRetorno> {
        @Override
        protected CarregaBannersRetorno doInBackground(Object... params) {

            String retorno = null;
            CarregaBannersRetorno retorno_ = new CarregaBannersRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/banners/" + configuracao.getString("url"));

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

        @Override
        protected void onPostExecute(CarregaBannersRetorno result) {
            if(result.erro){
                Toast.makeText(getContext(), result.mensagem,
                        Toast.LENGTH_LONG).show();
            }else {
                fotos = new ArrayList<>();
                titulos = new ArrayList<>();
                descricoes = new ArrayList<>();
                MyJSONObject result_ = new MyJSONObject(result.retorno);
                MyJSONArray banners = new MyJSONArray(result_.getArray("banners"));
                for (int x = 0; x < banners.size(); x++) {
                    MyJSONObject banner = new MyJSONObject(banners.getObjetc(x));
                    byte[] decodedString = Base64.decode(banner.getString("foto"), Base64.DEFAULT);
                    Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    fotos.add(foto_);
                    titulos.add(banner.getString("nome"));
                    descricoes.add(banner.getString("descricao"));
                }
                viewPager.setAdapter(new SliderAdapter(getContext(), fotos, titulos/*, descricoes*/));
                indicator.setupWithViewPager(viewPager, true);

                timer = new Timer();
                timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
            }
        }
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            if(getActivity() == null) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < fotos.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
