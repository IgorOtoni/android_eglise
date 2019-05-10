package com.example.eu7340.egliseteste;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class AppCongregacao extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Congregacao congregacao;
    private View headerView;
    private android.view.Menu menu;
    private android.view.Menu sub_menu;

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
        this.congregacao = gson.fromJson(getIntent().getStringExtra("json"), Congregacao.class);

        headerView = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        sub_menu = ((MenuItem)menu.findItem(R.id.social_menu)).getSubMenu();

        getSupportActionBar().setTitle(congregacao.getNome());

        CarregaLogoCongregacao carregaLogoCongregacao_task = new CarregaLogoCongregacao();
        carregaLogoCongregacao_task.execute(congregacao);

        TextView congregacao_nome = headerView.findViewById(R.id.congregacao_nome);
        congregacao_nome.setText(congregacao.getNome());
        TextView congregacao_email = headerView.findViewById(R.id.congregacao_email);
        if(congregacao.getEmail() == null) congregacao_email.setVisibility(View.GONE);
        else congregacao_email.setText(congregacao.getEmail());

        CarregaLinksCongregacao carregaLinksCongregacao_task = new CarregaLinksCongregacao();
        carregaLinksCongregacao_task.execute(congregacao);

        //ImageView imageView = findViewById(R.id.congregacao_logo);
        //http://eglise.com.br/storage/igrejas/logo-igreja-1.jpg
    }

    private class CarregaMenusCongregacao extends AsyncTask<Congregacao, Void, ArrayList<Object>> {
        @Override
        protected ArrayList<Object> doInBackground(Congregacao... params) {
            try {
                ArrayList<Object> resultado = new ArrayList<>();

                ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO(DB.connection);
                QueryBuilder<Configuracao, Integer> _filtro = configuracaoDAO.queryBuilder();
                _filtro.where().like("id_igreja", params[0].getId());
                PreparedQuery<Configuracao> preparedQuery = _filtro.prepare();
                List<Configuracao> _resultado = configuracaoDAO.query(preparedQuery);

                MenuDAO menuDAO = new MenuDAO(DB.connection);
                QueryBuilder<Menu, Integer> __filtro = menuDAO.queryBuilder();
                __filtro.where().like("id_configuracao", _resultado.get(0).getId());
                PreparedQuery<Menu> _preparedQuery = __filtro.prepare();
                List<Menu> __resultado = menuDAO.query(_preparedQuery);

                resultado.add(__resultado);

                ArrayList<List> sub_menus_por_menu = new ArrayList<>();
                for(int x = 0; x < __resultado.size(); x++){
                    SubMenuDAO subMenuDAO = new SubMenuDAO(DB.connection);
                    QueryBuilder<SubMenu, Integer> ___filtro = subMenuDAO.queryBuilder();
                    ___filtro.where().like("id_menu", __resultado.get(0).getId());
                    PreparedQuery<SubMenu> __preparedQuery = ___filtro.prepare();
                    List<SubMenu> ___resultado = subMenuDAO.query(__preparedQuery);

                    sub_menus_por_menu.add(___resultado);
                }

                resultado.add(sub_menus_por_menu);



                return resultado;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Object> todos_menus) {
            List<Menu> menus = (List<Menu>) todos_menus.get(0);

            for(int qtd_menus = 0; qtd_menus < todos_menus.size(); qtd_menus++){

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
                URL url = new URL("http://eglise.com.br/storage/igrejas/" + params[0].getLogo());
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
            ImageView imageView = findViewById(R.id.congregacao_logo);
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.facebook_link) {
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
}
