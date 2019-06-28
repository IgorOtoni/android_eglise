package com.example.eu7340.egliseteste.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.DB.BannerDAO;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.FuncaoDAO;
import com.example.eu7340.egliseteste.DB.MembroDAO;
import com.example.eu7340.egliseteste.Models.Banner;
import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.Views.ApresentacaoListView;
import com.example.eu7340.egliseteste.utils.SliderAdapter;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private Congregacao congregacao;
    private Configuracao configuracao;
    private Timer timer;

    private ViewPager viewPager;
    private TabLayout indicator;

    private List<Bitmap> fotos;
    private List<String> titulos;
    private List<String> descricoes;

    private LinearLayout map;
    private LinearLayout linearLayout;

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
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);
        this.configuracao = gson.fromJson(getActivity().getIntent().getStringExtra("configuracao_app"), Configuracao.class);

        viewPager = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicator);

        carregaBanners_task = new CarregaBanners();
        carregaBanners_task.execute(congregacao);

        TextView apresentacao = view.findViewById(R.id.congregacao_apresentacao);
        if(configuracao.getTexto_apresentativo() != null) {
            apresentacao.setText(configuracao.getTexto_apresentativo());
        }else{
            apresentacao.setVisibility(View.GONE);
        }

        TextView telefone = view.findViewById(R.id.congregacao_telefone);
        if(congregacao.getTelefone() != null) {
            telefone.setText(congregacao.getTelefone());
        }else{
            telefone.setVisibility(View.GONE);
        }

        TextView email = view.findViewById(R.id.congregacao_email);
        if(congregacao.getEmail() != null) {
            email.setText(congregacao.getEmail());
        }else{
            email.setVisibility(View.GONE);
        }

        linearLayout = view.findViewById(R.id.main_linear_layout);

        carregaMembrosImportantes_task = new CarregaMembrosImportantes();
        carregaMembrosImportantes_task.execute(congregacao);

        map = view.findViewById(R.id.map_container);

        if(congregacao.getCep() != null) openFragment(MapCongregacaoFragment.newInstance());
        else map.setVisibility(View.GONE);

        return view;
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaBanners_task != null) carregaBanners_task.cancel(true);
        if(carregaMembrosImportantes_task != null) carregaMembrosImportantes_task.cancel(true);
        if(timer != null) timer.cancel();
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private class Dados_banner{
        public Bitmap foto;
        public String titulo;
        public String descricao;
    }

    private class DadosApresentacao{
        public Funcao funcao;
        public Membro membro;
    }

    private class CarregaMembrosImportantes extends AsyncTask<Congregacao, Void, List<DadosApresentacao>> {
        @Override
        protected List<DadosApresentacao> doInBackground(Congregacao... params) {
            try{
                List<DadosApresentacao> membros_importantes = new ArrayList<>();

                FuncaoDAO funcaoDAO = new FuncaoDAO(DB.connection);
                QueryBuilder<Funcao, Integer> _filtro = funcaoDAO.queryBuilder();
                _filtro.where().like("id_igreja", params[0].getId()).and().like("apresentar", true);

                PreparedQuery<Funcao> preparedQuery = _filtro.prepare();
                List<Funcao> funcoes = funcaoDAO.query(preparedQuery);
                for(int x = 0; x < funcoes.size(); x++){

                    MembroDAO membroDAO = new MembroDAO(DB.connection);
                    QueryBuilder<Membro, Integer> __filtro = membroDAO.queryBuilder();
                    __filtro.where().like("id_funcao", funcoes.get(x).getId()).and().like("id_igreja", params[0].getId());
                    PreparedQuery<Membro> _preparedQuery = __filtro.prepare();
                    List<Membro> membros = membroDAO.query(_preparedQuery);

                    for(int y = 0; y < membros.size(); y++){

                        DadosApresentacao dadosApresentacao = new DadosApresentacao();
                        dadosApresentacao.funcao = funcoes.get(x);
                        dadosApresentacao.membro = membros.get(y);
                        membros_importantes.add(dadosApresentacao);

                    }

                }

                return membros_importantes;
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<DadosApresentacao> membros_importantes) {
            if(membros_importantes != null) for(int x = 0; x < membros_importantes.size(); x++){
                ApresentacaoListView apresentacaoListView = new ApresentacaoListView(getContext(), null, membros_importantes.get(x).membro,  membros_importantes.get(x).funcao);
                linearLayout.addView(apresentacaoListView);
            }
        }
    }

    private class CarregaBanners extends AsyncTask<Congregacao, Void, List<Dados_banner>> {
        @Override
        protected List<Dados_banner> doInBackground(Congregacao... params) {
            try {
                List<Dados_banner> resutado = new ArrayList<>();

                BannerDAO bannerDAO = new BannerDAO(DB.connection);
                QueryBuilder<Banner, Integer> _filtro = bannerDAO.queryBuilder();
                _filtro.where().like("id_igreja", params[0].getId());
                PreparedQuery<Banner> preparedQuery = _filtro.prepare();
                List<Banner> banners = bannerDAO.query(preparedQuery);
                for (int x = 0; x < banners.size(); x++) {
                    URL url = new URL("http://eglise.com.br/storage/banners/" + banners.get(x).getFoto());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    Dados_banner dados_banner = new Dados_banner();

                    dados_banner.foto = myBitmap;
                    dados_banner.titulo = banners.get(x).getNome();
                    dados_banner.descricao = banners.get(x).getDescricao();

                    resutado.add(dados_banner);
                }

                return resutado;
            }catch (MalformedURLException ex){
                ex.printStackTrace();
            }catch (IOException ex){
                ex.printStackTrace();
            }catch (SQLException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Dados_banner> banners) {
            fotos = new ArrayList<>();
            titulos = new ArrayList<>();
            descricoes = new ArrayList<>();
            for(int x = 0; x < banners.size(); x++){
                fotos.add(banners.get(x).foto);
                titulos.add(banners.get(x).titulo);
                descricoes.add(banners.get(x).descricao);
            }
            viewPager.setAdapter(new SliderAdapter(getContext(), fotos, titulos/*, descricoes*/));
            indicator.setupWithViewPager(viewPager, true);

            timer = new Timer();
            timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        }
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
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
