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
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eu7340.egliseteste.DB.BannerDAO;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.Models.Banner;
import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.R;
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

    ViewPager viewPager;
    TabLayout indicator;

    List<Bitmap> fotos;
    List<String> titulos;
    List<String> descricoes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider, container, false);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getActivity().getIntent().getStringExtra("congregacao_app"), Congregacao.class);
        this.configuracao = gson.fromJson(getActivity().getIntent().getStringExtra("configuracao_app"), Configuracao.class);

        viewPager=(ViewPager)view.findViewById(R.id.viewPager);
        indicator=(TabLayout)view.findViewById(R.id.indicator);

        CarregaBanners carregaBanners_task = new CarregaBanners();
        carregaBanners_task.execute(congregacao);

        TextView apresentacao = view.findViewById(R.id.congregacao_apresentacao);
        apresentacao.setText(configuracao.getTexto_apresentativo());

        return view;
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private class Dados_banner{

        public Bitmap foto;
        public String titulo;
        public String descricao;

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
        }
    }

    /*private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < color.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }*/
}
