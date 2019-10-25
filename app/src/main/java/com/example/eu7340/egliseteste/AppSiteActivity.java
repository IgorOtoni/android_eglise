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
import android.util.Base64;
import android.view.Menu;
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

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.EventoDAO;
import com.example.eu7340.egliseteste.DB.EventoFixoDAO;
import com.example.eu7340.egliseteste.DB.GaleriaDAO;
import com.example.eu7340.egliseteste.DB.NoticiaDAO;
import com.example.eu7340.egliseteste.DB.PublicacaoDAO;
import com.example.eu7340.egliseteste.DB.SermaoDAO;
import com.example.eu7340.egliseteste.Fragments.EventosFixosFragment;
import com.example.eu7340.egliseteste.Fragments.EventosFragment;
import com.example.eu7340.egliseteste.Fragments.GaleriasFragment;
import com.example.eu7340.egliseteste.Fragments.HomeFragment;
import com.example.eu7340.egliseteste.Fragments.LoginFragment;
import com.example.eu7340.egliseteste.Fragments.NoticiasFragment;
import com.example.eu7340.egliseteste.Fragments.ProdutosFragment;
import com.example.eu7340.egliseteste.Fragments.PublicacoesFragment;
import com.example.eu7340.egliseteste.Fragments.SermoesFragment;
import com.example.eu7340.egliseteste.Models.Evento;
import com.example.eu7340.egliseteste.Models.EventoFixo;
import com.example.eu7340.egliseteste.Models.Galeria;
import com.example.eu7340.egliseteste.Models.Noticia;
import com.example.eu7340.egliseteste.Models.Publicacao;
import com.example.eu7340.egliseteste.Models.Sermao;
import com.example.eu7340.egliseteste.utils.MyJSONArray;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.gson.Gson;

import java.sql.SQLException;

public class AppCongregacaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Fragment last_fragment;

    private MyJSONObject congregacao;
    private MyJSONObject configuracao;
    private MyJSONArray menus;
    private View headerView;
    private android.view.Menu menu;
    private android.view.Menu sub_menu;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_site);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if(congregacao == null || configuracao == null){
            congregacao = Sessao.ultima_congregacao;
            configuracao = Sessao.ultima_configuracao;
            menus = Sessao.ultimos_menus;
        }else{
            Sessao.ultima_congregacao = congregacao;
            Sessao.ultima_configuracao = configuracao;
            Sessao.ultimos_menus = menus;
        }

        getSupportActionBar().setTitle(this.congregacao.getString("nome"));

        headerView = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        sub_menu = ((MenuItem)menu.findItem(R.id.social_menu)).getSubMenu();
        logo = headerView.findViewById(R.id.congregacao_logo);

        getSupportActionBar().setTitle(congregacao.getString("nome"));

        byte[] decodedString = Base64.decode(congregacao.getString("logo"), Base64.DEFAULT);
        Bitmap logo_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        logo.setImageBitmap(logo_);

        TextView congregacao_nome = headerView.findViewById(R.id.congregacao_nome);
        congregacao_nome.setText(congregacao.getString("nome"));
        TextView congregacao_email = headerView.findViewById(R.id.congregacao_email);
        if(congregacao.getString("email") == null) congregacao_email.setVisibility(View.GONE);
        else congregacao_email.setText(congregacao.getString("email"));

        MenuItem facebook_menu = sub_menu.findItem(R.id.facebook_link);
        MenuItem twitter_menu = sub_menu.findItem(R.id.twitter_link);
        MenuItem youtube_menu = sub_menu.findItem(R.id.youtube_link);
        if(configuracao.getString("facebook") == null || configuracao.getString("facebook").equals("null")){
            facebook_menu.setVisible(false);
        }else{
            Intent facebook_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(configuracao.getString("facebook")));
            facebook_menu.setIntent(facebook_intent);
        }
        if(configuracao.getString("twitter") == null || configuracao.getString("facebook").equals("null")){
            twitter_menu.setVisible(false);
        }else{
            Intent twitter_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(configuracao.getString("twitter")));
            twitter_menu.setIntent(twitter_intent);
        }
        if(configuracao.getString("youtube") == null || configuracao.getString("facebook").equals("null")){
            youtube_menu.setVisible(false);
        }else{
            Intent youtube_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(configuracao.getString("youtube")));
            youtube_menu.setIntent(youtube_intent);
        }

        for(int qtd_menus = 0; qtd_menus < menus.size(); qtd_menus++){
            MyJSONObject menu_ = new MyJSONObject(menus.getObjetc(qtd_menus));
            menu.add(0, menu_.getInt("id"), menu_.getInt("ordem"), menu_.getString("nome"));
        }

        if(last_fragment == null) openFragment(HomeFragment.newInstance());
        else openFragment(last_fragment);
    }

    public void onDestroy(){
        super.onDestroy();
    }

    /*public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem facebook_menu = menu.findItem(R.id.facebook_link);
        facebook_menu.setVisible(facebook_menu.getIntent() != null);
        MenuItem twitter_menu = menu.findItem(R.id.twitter_link);
        twitter_menu.setVisible(facebook_menu.getIntent() != null);
        MenuItem youtube_menu = menu.findItem(R.id.youtube_link);
        youtube_menu.setVisible(facebook_menu.getIntent() != null);
        return true;
    }*/

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
        getMenuInflater().inflate(R.menu.app_site, menu);

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
            MyJSONObject menu_ = new MyJSONObject(menus.getObjetc(cont));
            if(id == menu_.getInt("id")){
                MenuItem menu_item = menu.findItem(menu_.getInt("id"));
                if(menu_.getString("link").contains("http://") || menu_.getString("link").contains("https://")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(menu_.getString("link"))));
                }else if(menu_.getString("link").contains("modulo")) {
                    String[] split = menu_.getString("link").split("-");
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
                        case "produtos":
                            openFragment(ProdutosFragment.newInstance());
                            break;
                        case "login":
                            openFragment(LoginFragment.newInstance());
                            break;
                    }
                }else if(menu_.getString("link").contains("evento")) {
                    String[] split = menu_.getString("link").split("-");
                    int id_evento = Integer.parseInt(split[1]);
                    AbrirEventoActivity abrirEventoActivity = new AbrirEventoActivity(this);
                    abrirEventoActivity.execute(id_evento);
                }else if(menu_.getString("link").contains("noticia")) {
                    String[] split = menu_.getString("link").split("-");
                    int id_noticia = Integer.parseInt(split[1]);
                    AbrirNoticiaActivity abrirNoticiaActivity = new AbrirNoticiaActivity(this);
                    abrirNoticiaActivity.execute(id_noticia);
                }else if(menu_.getString("link").contains("eventofixo")) {
                    String[] split = menu_.getString("link").split("-");
                    int id_eventofixo = Integer.parseInt(split[1]);
                    AbrirEventoFixoActivity abrirEventoFixoActivity = new AbrirEventoFixoActivity(this);
                    abrirEventoFixoActivity.execute(id_eventofixo);
                }else if(menu_.getString("link").contains("publicacao")) {
                    String[] split = menu_.getString("link").split("-");
                    int id_publicacao = Integer.parseInt(split[1]);
                    AbrirPublicacaoActivity abrirPublicacaoActivity = new AbrirPublicacaoActivity(this);
                    abrirPublicacaoActivity.execute(id_publicacao);
                }else if(menu_.getString("link").contains("sermao")) {
                    String[] split = menu_.getString("link").split("-");
                    int id_sermao = Integer.parseInt(split[1]);
                    AbrirSermaoActivity abrirSermaoActivity = new AbrirSermaoActivity(this);
                    abrirSermaoActivity.execute(id_sermao);
                }else if(menu_.getString("link").contains("galeria")) {
                    String[] split = menu_.getString("link").split("-");
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
