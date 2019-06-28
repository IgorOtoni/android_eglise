package com.example.eu7340.egliseteste;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eu7340.egliseteste.DB.ConfiguracaoDAO;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.EventoDAO;
import com.example.eu7340.egliseteste.DB.EventoFixoDAO;
import com.example.eu7340.egliseteste.DB.GaleriaDAO;
import com.example.eu7340.egliseteste.DB.MenuDAO;
import com.example.eu7340.egliseteste.DB.NoticiaDAO;
import com.example.eu7340.egliseteste.DB.PublicacaoDAO;
import com.example.eu7340.egliseteste.DB.SermaoDAO;
import com.example.eu7340.egliseteste.Fragments.EventosFixosFragment;
import com.example.eu7340.egliseteste.Fragments.EventosFragment;
import com.example.eu7340.egliseteste.Fragments.GaleriasFragment;
import com.example.eu7340.egliseteste.Fragments.HomeFragment;
import com.example.eu7340.egliseteste.Fragments.LoginFragment;
import com.example.eu7340.egliseteste.Fragments.NoticiasFragment;
import com.example.eu7340.egliseteste.Fragments.PublicacoesFragment;
import com.example.eu7340.egliseteste.Fragments.SermoesFragment;
import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.Evento;
import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.example.eu7340.egliseteste.Models.Galeria;
import com.example.eu7340.egliseteste.Models.Menu;
import com.example.eu7340.egliseteste.Models.Noticia;
import com.example.eu7340.egliseteste.Models.Publicacao;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class AppCongregacaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Fragment last_fragment;

    private Congregacao congregacao;
    private Configuracao configuracao;
    private View headerView;
    private android.view.Menu menu;
    private List<Menu> menus;
    private android.view.Menu sub_menu;
    private ImageView logo;

    private CarregaLogoCongregacao carregaLogoCongregacao_task;
    private CarregaLinksCongregacao carregaLinksCongregacao_task;
    private CarregaMenusCongregacao carregaMenusCongregacao_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_congregacao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Gson gson = new Gson();
        this.congregacao = gson.fromJson(getIntent().getStringExtra("congregacao_app"), Congregacao.class);
        this.configuracao = gson.fromJson(getIntent().getStringExtra("configuracao_app"), Configuracao.class);

        if(congregacao == null || configuracao == null){
            congregacao = Sessao.ultima_congregacao;
            configuracao = Sessao.ultima_configuracao;

            String congregacao_json = gson.toJson(congregacao);
            String configuracao_json = gson.toJson(configuracao);

            Intent intent = new Intent(this, AppCongregacaoActivity.class);

            intent.putExtra("congregacao_app", congregacao_json);
            intent.putExtra("configuracao_app", configuracao_json);

            this.setIntent(intent);
        }else{
            Sessao.ultima_congregacao = congregacao;
            Sessao.ultima_configuracao = configuracao;
        }

        getSupportActionBar().setTitle(this.congregacao.getNome());

        headerView = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        sub_menu = ((MenuItem)menu.findItem(R.id.social_menu)).getSubMenu();
        logo = headerView.findViewById(R.id.congregacao_logo);

        getSupportActionBar().setTitle(congregacao.getNome());

        carregaLogoCongregacao_task = new CarregaLogoCongregacao();
        carregaLogoCongregacao_task.execute(congregacao);

        TextView congregacao_nome = headerView.findViewById(R.id.congregacao_nome);
        congregacao_nome.setText(congregacao.getNome());
        TextView congregacao_email = headerView.findViewById(R.id.congregacao_email);
        if(congregacao.getEmail() == null) congregacao_email.setVisibility(View.GONE);
        else congregacao_email.setText(congregacao.getEmail());

        carregaLinksCongregacao_task = new CarregaLinksCongregacao();
        carregaLinksCongregacao_task.execute(congregacao);

        carregaMenusCongregacao_task = new CarregaMenusCongregacao();
        carregaMenusCongregacao_task.execute(congregacao);

        if(last_fragment == null) openFragment(HomeFragment.newInstance());
        else openFragment(last_fragment);
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaLogoCongregacao_task != null) carregaLogoCongregacao_task.cancel(true);
        if(carregaLinksCongregacao_task != null) carregaLinksCongregacao_task.cancel(true);
        if(carregaMenusCongregacao_task != null) carregaMenusCongregacao_task.cancel(true);
    }

    private class CarregaMenusCongregacao extends AsyncTask<Congregacao, Void, List<Menu>> {
        @Override
        protected List<Menu> doInBackground(Congregacao... params) {
            try {
                ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO(DB.connection);
                QueryBuilder<Configuracao, Integer> _filtro = configuracaoDAO.queryBuilder();
                _filtro.where().like("id_igreja", params[0].getId());
                PreparedQuery<Configuracao> preparedQuery = _filtro.prepare();
                List<Configuracao> _resultado = configuracaoDAO.query(preparedQuery);

                MenuDAO menuDAO = new MenuDAO(DB.connection);
                QueryBuilder<Menu, Integer> __filtro = menuDAO.queryBuilder();
                __filtro.where().like("id_configuracao", _resultado.get(0).getId());
                __filtro.orderBy("ordem", true);
                PreparedQuery<Menu> _preparedQuery = __filtro.prepare();
                List<Menu> __resultado = menuDAO.query(_preparedQuery);

                return __resultado;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Menu> todos_menus) {
            //menu.clear();
            menus = todos_menus;

            for(int qtd_menus = 0; qtd_menus < todos_menus.size(); qtd_menus++){
                menu.add(0, menus.get(qtd_menus).getId(), menus.get(qtd_menus).getOrdem(), menus.get(qtd_menus).getNome());
            }
        }
    }

    private class CarregaLinksCongregacao extends AsyncTask<Congregacao, Void, String[]> {
        @Override
        protected String[] doInBackground(Congregacao... params) {
            try {
                ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO(DB.connection);
                QueryBuilder<Configuracao, Integer> _filtro = configuracaoDAO.queryBuilder();
                _filtro.where().like("id_igreja", params[0].getId());
                PreparedQuery<Configuracao> preparedQuery = _filtro.prepare();
                List<Configuracao> _resultado = configuracaoDAO.query(preparedQuery);
                String[] links = new String[3];
                links[0] = _resultado.get(0).getFacebook();
                links[1] = _resultado.get(0).getTwitter();
                links[2] = _resultado.get(0).getYoutube();
                return links;
            }catch (SQLException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] links) {
            MenuItem facebook_menu = sub_menu.findItem(R.id.facebook_link);
            MenuItem twitter_menu = sub_menu.findItem(R.id.twitter_link);
            MenuItem youtube_menu = sub_menu.findItem(R.id.youtube_link);
            if(links[0] == null){
                facebook_menu.setVisible(false);
            }else{
                Intent facebook_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(links[0]));
                facebook_menu.setIntent(facebook_intent);
            }
            if(links[1] == null){
                twitter_menu.setVisible(false);
            }else{
                Intent twitter_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(links[1]));
                twitter_menu.setIntent(twitter_intent);
            }
            if(links[2] == null){
                youtube_menu.setVisible(false);
            }else{
                Intent youtube_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(links[2]));
                youtube_menu.setIntent(youtube_intent);
            }
        }
    }

    /*public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem facebook_menu = menu.findItem(R.id.facebook_link);
        facebook_menu.setVisible(facebook_menu.getIntent() != null);
        MenuItem twitter_menu = menu.findItem(R.id.twitter_link);
        facebook_menu.setVisible(facebook_menu.getIntent() != null);
        MenuItem youtube_menu = menu.findItem(R.id.youtube_link);
        facebook_menu.setVisible(facebook_menu.getIntent() != null);
        return true;
    }*/

    private class CarregaLogoCongregacao extends AsyncTask<Congregacao, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Congregacao... params) {
            try {
                URL url = new URL("http://eglise.com.br/storage/" + (params[0].getLogo() != null ? "igrejas/" + params[0].getLogo() : "no-logo.jpg"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (MalformedURLException ex){
                ex.printStackTrace();
            }catch (IOException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            logo.setImageBitmap(result);
        }
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
        last_fragment = null;
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_congregacao, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        for(int cont = 0; cont < menus.size(); cont++){
            if(id == menus.get(cont).getId()){
                MenuItem menu_item = menu.findItem(menus.get(cont).getId());
                if(menus.get(cont).getLink().contains("http://") || menus.get(cont).getLink().contains("https://")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(menus.get(cont).getLink())));
                }else if(menus.get(cont).getLink().contains("modulo")) {
                    String[] split = menus.get(cont).getLink().split("-");
                    switch(split[1]){
                        case "apresentacao":
                            openFragment(HomeFragment.newInstance());
                            break;
                        case "eventos":
                            openFragment(EventosFragment.newInstance());
                            break;
                        case "eventosfixos":
                            openFragment(EventosFixosFragment.newInstance());
                            break;
                        case "publicacoes":
                            openFragment(PublicacoesFragment.newInstance());
                            break;
                        case "noticias":
                            openFragment(NoticiasFragment.newInstance());
                            break;
                        case "sermoes":
                            openFragment(SermoesFragment.newInstance());
                            break;
                        case "galerias":
                            openFragment(GaleriasFragment.newInstance());
                            break;
                        case "login":
                            openFragment(LoginFragment.newInstance());
                            break;
                    }
                }else if(menus.get(cont).getLink().contains("evento")) {
                    String[] split = menus.get(cont).getLink().split("-");
                    int id_evento = Integer.parseInt(split[1]);
                    AbrirEventoActivity abrirEventoActivity = new AbrirEventoActivity(this);
                    abrirEventoActivity.execute(id_evento);
                }else if(menus.get(cont).getLink().contains("noticia")) {
                    String[] split = menus.get(cont).getLink().split("-");
                    int id_noticia = Integer.parseInt(split[1]);
                    AbrirNoticiaActivity abrirNoticiaActivity = new AbrirNoticiaActivity(this);
                    abrirNoticiaActivity.execute(id_noticia);
                }else if(menus.get(cont).getLink().contains("eventofixo")) {
                    String[] split = menus.get(cont).getLink().split("-");
                    int id_eventofixo = Integer.parseInt(split[1]);
                    AbrirEventoFixoActivity abrirEventoFixoActivity = new AbrirEventoFixoActivity(this);
                    abrirEventoFixoActivity.execute(id_eventofixo);
                }else if(menus.get(cont).getLink().contains("publicacao")) {
                    String[] split = menus.get(cont).getLink().split("-");
                    int id_publicacao = Integer.parseInt(split[1]);
                    AbrirPublicacaoActivity abrirPublicacaoActivity = new AbrirPublicacaoActivity(this);
                    abrirPublicacaoActivity.execute(id_publicacao);
                }else if(menus.get(cont).getLink().contains("sermao")) {
                    String[] split = menus.get(cont).getLink().split("-");
                    int id_sermao = Integer.parseInt(split[1]);
                    AbrirSermaoActivity abrirSermaoActivity = new AbrirSermaoActivity(this);
                    abrirSermaoActivity.execute(id_sermao);
                }else if(menus.get(cont).getLink().contains("galeria")) {
                    String[] split = menus.get(cont).getLink().split("-");
                    int id_galeria = Integer.parseInt(split[1]);
                    AbrirGaleriaActivity abrirGaleriaActivity = new AbrirGaleriaActivity(this);
                    abrirGaleriaActivity.execute(id_galeria);
                }
            }
        }

        if (id == R.id.facebook_link) {
            startActivity(item.getIntent());
        } else if (id == R.id.twitter_link) {
            startActivity(item.getIntent());
        } else if (id == R.id.twitter_link) {
            startActivity(item.getIntent());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment) {
        last_fragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_app_congregacao, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    private class AbrirEventoActivity extends AsyncTask<Integer, Void, Evento> {

        private Context context;

        public AbrirEventoActivity(Context context){
            this.context = context;
        }

        @Override
        protected Evento doInBackground(Integer... id) {
            try {

                EventoDAO eventoDAO = new EventoDAO(DB.connection);
                Evento evento = eventoDAO.queryForId(id[0]);
                return evento;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Evento evento) {
            Gson gson = new Gson();
            String evento_json = gson.toJson(evento);

            Intent intent = new Intent(context, EventoActivity.class);

            intent.putExtra("evento_detalhe", evento_json);

            context.startActivity(intent);
        }
    }

    private class AbrirEventoFixoActivity extends AsyncTask<Integer, Void, EventoFixo> {

        private Context context;

        public AbrirEventoFixoActivity(Context context){
            this.context = context;
        }

        @Override
        protected EventoFixo doInBackground(Integer... id) {
            try {

                EventoFixoDAO eventofixoDAO = new EventoFixoDAO(DB.connection);
                EventoFixo eventofixo = eventofixoDAO.queryForId(id[0]);
                return eventofixo;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(EventoFixo eventofixo) {
            Gson gson = new Gson();
            String eventofixo_json = gson.toJson(eventofixo);

            Intent intent = new Intent(context, EventoFixoActivity.class);

            intent.putExtra("eventofixo_detalhe", eventofixo_json);

            context.startActivity(intent);
        }
    }

    private class AbrirSermaoActivity extends AsyncTask<Integer, Void, Sermao> {

        private Context context;

        public AbrirSermaoActivity(Context context){
            this.context = context;
        }

        @Override
        protected Sermao doInBackground(Integer... id) {
            try {

                SermaoDAO sermaoDAO = new SermaoDAO(DB.connection);
                Sermao sermao = sermaoDAO.queryForId(id[0]);
                return sermao;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Sermao sermao) {
            Gson gson = new Gson();
            String sermao_json = gson.toJson(sermao);

            Intent intent = new Intent(context, SermaoActivity.class);

            intent.putExtra("sermao_detalhe", sermao_json);

            context.startActivity(intent);
        }
    }

    private class AbrirGaleriaActivity extends AsyncTask<Integer, Void, Galeria> {

        private Context context;

        public AbrirGaleriaActivity(Context context){
            this.context = context;
        }

        @Override
        protected Galeria doInBackground(Integer... id) {
            try {

                GaleriaDAO galeriaDAO = new GaleriaDAO(DB.connection);
                Galeria galeria = galeriaDAO.queryForId(id[0]);
                return galeria;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Galeria galeria) {
            Gson gson = new Gson();
            String galeria_json = gson.toJson(galeria);

            Intent intent = new Intent(context, GaleriaActivity.class);

            intent.putExtra("galeria_detalhe", galeria_json);

            context.startActivity(intent);
        }
    }

    private class AbrirNoticiaActivity extends AsyncTask<Integer, Void, Noticia> {

        private Context context;

        public AbrirNoticiaActivity(Context context){
            this.context = context;
        }

        @Override
        protected Noticia doInBackground(Integer... id) {
            try {

                NoticiaDAO noticiaDAO = new NoticiaDAO(DB.connection);
                Noticia noticia = noticiaDAO.queryForId(id[0]);
                return noticia;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Noticia noticia) {
            Gson gson = new Gson();
            String noticia_json = gson.toJson(noticia);

            Intent intent = new Intent(context, NoticiaActivity.class);

            intent.putExtra("noticia_detalhe", noticia_json);

            context.startActivity(intent);
        }
    }

    private class AbrirPublicacaoActivity extends AsyncTask<Integer, Void, Publicacao> {

        private Context context;

        public AbrirPublicacaoActivity(Context context){
            this.context = context;
        }

        @Override
        protected Publicacao doInBackground(Integer... id) {
            try {

                PublicacaoDAO publicacaoDAO = new PublicacaoDAO(DB.connection);
                Publicacao publicacao = publicacaoDAO.queryForId(id[0]);
                return publicacao;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Publicacao publicacao) {
            Gson gson = new Gson();
            String publicacao_json = gson.toJson(publicacao);

            Intent intent = new Intent(context, PublicacaoActivity.class);

            intent.putExtra("publicacao_detalhe", publicacao_json);

            context.startActivity(intent);
        }
    }
}
