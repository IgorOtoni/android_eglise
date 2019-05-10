package com.example.eu7340.egliseteste;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class SiteCongregacao extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_congregacao);

        Gson gson = new Gson();
        Congregacao congregacao = gson.fromJson(getIntent().getStringExtra("json"), Congregacao.class);

        webView = (WebView) findViewById(R.id.site_congregacao);

        getSupportActionBar().setTitle(congregacao.getNome());

        CarregaSite carregaSite_task = new CarregaSite(congregacao);
        carregaSite_task.execute();

    }

    private class CarregaSite extends AsyncTask<Object, Object, String> {

        private final Congregacao congregacao;

        public CarregaSite(Congregacao congregacao){
            this.congregacao = congregacao;
        }

        public String doInBackground(Object... objects) {
            try {
                ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO(DB.connection);
                QueryBuilder<Configuracao, Integer> _filtro = configuracaoDAO.queryBuilder();
                _filtro.where().like("id_igreja", congregacao.getId());
                PreparedQuery<Configuracao> preparedQuery = _filtro.prepare();
                List<Configuracao> _resultado = configuracaoDAO.query(preparedQuery);
                return _resultado.get(0).getUrl();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String url) {
            webView.loadUrl("http://www.eglise.com.br/" + url);
        }

    }
}
