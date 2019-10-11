package com.example.eu7340.egliseteste;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eu7340.egliseteste.DB.CongregacaoModuloDAO;
import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.ModuloDAO;
import com.example.eu7340.egliseteste.DB.PerfilCongregacaoModuloDAO;
import com.example.eu7340.egliseteste.Fragments.ContaFragment;
import com.example.eu7340.egliseteste.Fragments.HomeUsuarioFragment;
import com.example.eu7340.egliseteste.Models.Configuracao;
import com.example.eu7340.egliseteste.Models.Congregacao;
import com.example.eu7340.egliseteste.Models.CongregacaoModulo;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.Modulo;
import com.example.eu7340.egliseteste.Models.Perfil;
import com.example.eu7340.egliseteste.Models.PerfilCongregacaoModulo;
import com.example.eu7340.egliseteste.Models.Usuario;
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
import java.util.ArrayList;
import java.util.List;

public class AppUsuarioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Fragment last_fragment;

    private Congregacao congregacao;
    private Configuracao configuracao;
    private Usuario usuario;
    private Perfil perfil;
    private Membro membro;
    private Funcao funcao;

    private View headerView;
    private android.view.Menu menu;
    private List<Modulo> menus;
    private ImageView logo;

    private CarregaLogoCongregacao carregaLogoCongregacao_task;
    private CarregaMenusUsuario carregaMenusUsuario_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usuario);
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
        this.usuario = gson.fromJson(getIntent().getStringExtra("usuario_app"), Usuario.class);
        this.perfil = gson.fromJson(getIntent().getStringExtra("perfil_app"), Perfil.class);
        this.membro = gson.fromJson(getIntent().getStringExtra("membro_app"), Membro.class);
        this.funcao = gson.fromJson(getIntent().getStringExtra("funcao_app"), Funcao.class);

        if(!Sessao.existe_login_ativo){
            /*congregacao = Sessao.ultima_congregacao;
            configuracao = Sessao.ultima_configuracao;
            usuario = Sessao.ultimo_usuario;
            perfil = Sessao.ultimo_perfil;
            membro = Sessao.ultimo_membro;
            funcao = Sessao.ultima_funcao;*/

            String congregacao_json = gson.toJson(congregacao);
            String configuracao_json = gson.toJson(configuracao);
            String usuario_json = gson.toJson(usuario);
            String perfil_json = gson.toJson(perfil);
            String membro_json = gson.toJson(membro);
            String funcao_json = null;
            if(funcao != null) funcao_json = gson.toJson(funcao);

            Intent intent = new Intent(this, AppCongregacaoActivity.class);

            intent.putExtra("congregacao_app", congregacao_json);
            intent.putExtra("configuracao_app", configuracao_json);
            intent.putExtra("usuario_app", usuario_json);
            intent.putExtra("perfil_app", perfil_json);
            intent.putExtra("membro_app", membro_json);
            if(funcao_json != null) intent.putExtra("funcao_app", funcao_json);

            this.setIntent(intent);
        }else{
            /*Sessao.ultima_congregacao = congregacao;
            Sessao.ultima_configuracao = configuracao;
            Sessao.ultimo_usuario = usuario;
            Sessao.ultimo_perfil = perfil;
            Sessao.ultimo_membro = membro;
            Sessao.ultima_funcao = funcao;*/
        }

        getSupportActionBar().setTitle(this.congregacao.getNome());

        headerView = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        logo = headerView.findViewById(R.id.congregacao_logo);

        carregaLogoCongregacao_task = new CarregaLogoCongregacao();
        carregaLogoCongregacao_task.execute(congregacao);

        TextView congregacao_nome = headerView.findViewById(R.id.congregacao_nome);
        congregacao_nome.setText(congregacao.getNome());
        TextView congregacao_email = headerView.findViewById(R.id.congregacao_email);
        if(congregacao.getEmail() == null) congregacao_email.setVisibility(View.GONE);
        else congregacao_email.setText(congregacao.getEmail());

        carregaMenusUsuario_task = new CarregaMenusUsuario();
        carregaMenusUsuario_task.execute(perfil);

        TextView usuario_email = headerView.findViewById(R.id.usuario_email);
        usuario_email.setText(usuario.getNome());

        if(last_fragment == null) openFragment(HomeUsuarioFragment.newInstance());
        else openFragment(last_fragment);
    }

    public void onDestroy(){
        super.onDestroy();

        if(carregaLogoCongregacao_task != null) carregaLogoCongregacao_task.cancel(true);
        if(carregaMenusUsuario_task != null) carregaMenusUsuario_task.cancel(true);
    }

    private class CarregaMenusUsuario extends AsyncTask<Perfil, Void, List<Modulo>> {
        @Override
        protected List<Modulo> doInBackground(Perfil... params) {
            try {
                List<Modulo> modulos = new ArrayList<>();

                PerfilCongregacaoModuloDAO perfilCongregacaoModuloDAO = new PerfilCongregacaoModuloDAO(DB.connection);
                QueryBuilder<PerfilCongregacaoModulo, Integer> _filtro = perfilCongregacaoModuloDAO.queryBuilder();
                _filtro.where().like("id_perfil", params[0].getId());
                PreparedQuery<PerfilCongregacaoModulo> preparedQuery = _filtro.prepare();
                List<PerfilCongregacaoModulo> _resultado = perfilCongregacaoModuloDAO.query(preparedQuery);

                CongregacaoModuloDAO congregacaoModuloDAO = new CongregacaoModuloDAO(DB.connection);
                ModuloDAO moduloDAO = new ModuloDAO(DB.connection);

                for(int x = 0; x < _resultado.size(); x++){
                    CongregacaoModulo congregacaoModulo = congregacaoModuloDAO.queryForId(_resultado.get(x).getCongregacaoModulo().getId());
                    Modulo modulo = moduloDAO.queryForId(congregacaoModulo.getModulo().getId());
                    if(modulo.getSistema().contains("android") && modulo.isGerencial()){
                        modulos.add(modulo);
                    }
                }

                return modulos;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Modulo> todos_menus) {
            //menu.clear();
            menus = todos_menus;

            String nm_menu = "Inicial";
            menu.add(0, 1000, nm_menu.charAt(0), nm_menu);
            nm_menu = "Meus dados";
            menu.add(0, 1001, nm_menu.charAt(0), nm_menu);

            for(int qtd_menus = 0; qtd_menus < todos_menus.size(); qtd_menus++){
                menu.add(0, menus.get(qtd_menus).getId(), menus.get(qtd_menus).getNome().charAt(0), menus.get(qtd_menus).getNome());
            }
        }
    }

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
        Toast.makeText(this, "Saindo...", Toast.LENGTH_LONG);

        Sessao.existe_login_ativo = false;

        Sessao.ultimo_usuario = null;
        Sessao.ultimo_perfil = null;
        Sessao.ultimo_membro = null;
        Sessao.ultima_funcao = null;

        last_fragment = null;
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            Toast.makeText(this, "Saindo...", Toast.LENGTH_LONG);

            Sessao.existe_login_ativo = false;

            Sessao.ultimo_usuario = null;
            Sessao.ultimo_perfil = null;
            Sessao.ultimo_membro = null;
            Sessao.ultima_funcao = null;

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == 1000){
            openFragment(HomeUsuarioFragment.newInstance());
        }else if(id == 1001){
            openFragment(ContaFragment.newInstance());
        }

        /*for(int cont = 0; cont < menus.size(); cont++) {
            if (id == menus.get(cont).getId()) {

            }
        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(Fragment fragment) {
        last_fragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_app_usuario, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
