package com.example.eu7340.egliseteste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.FotoDAO;
import com.example.eu7340.egliseteste.DB.GaleriaDAO;
import com.example.eu7340.egliseteste.Models.Foto;
import com.example.eu7340.egliseteste.Models.Galeria;
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

public class GaleriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        getSupportActionBar().setTitle("Evento fixo");

        Gson gson = new Gson();
        Galeria galeria = gson.fromJson(getIntent().getStringExtra("galeria_detalhe"), Galeria.class);

        TextView nome = findViewById(R.id.galeria_nome);
        nome.setText(galeria.getNome());

        TextView descricao = findViewById(R.id.galeria_descricao);
        descricao.setText(galeria.getDescricao());

        CarregaFotosGaleria carregaFotoGaleria_task = new CarregaFotosGaleria();
        carregaFotoGaleria_task.execute(galeria);
    }

    private class CarregaFotosGaleria extends AsyncTask<Galeria, Void, List<Bitmap>> {
        @Override
        protected List<Bitmap> doInBackground(Galeria... params) {
            try {
                List<Bitmap> resutado = new ArrayList<>();

                FotoDAO fotoDAO = new FotoDAO(DB.connection);
                QueryBuilder<Foto, Integer> _filtro = fotoDAO.queryBuilder();
                _filtro.where().like("id_galeria", params[0].getId());
                PreparedQuery<Foto> preparedQuery = _filtro.prepare();
                List<Foto> fotos = fotoDAO.query(preparedQuery);
                for (int x = 0; x < fotos.size(); x++) {
                    URL url = new URL("http://eglise.com.br/storage/galerias/" + fotos.get(x).getFoto());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    resutado.add(myBitmap);
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
        protected void onPostExecute(List<Bitmap> fotos) {
            LinearLayout fotos_layout = findViewById(R.id.fotos_layout);
            LinearLayout linearLayout = new LinearLayout(fotos_layout.getContext());
            /*int qtd = 0;*/
            for(int x = 0; x < fotos.size(); x++) {
                FrameLayout.LayoutParams linearLayout_ = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                linearLayout_.setMargins(0, 0, 0, 10);
                linearLayout.setLayoutParams(linearLayout_);
                ImageView imageView = new ImageView(fotos_layout.getContext());
                imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(fotos.get(x));
                linearLayout.addView(imageView);
                /*qtd++;

                if(qtd % 3 == 0){*/
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    fotos_layout.addView(linearLayout);
                    linearLayout = new LinearLayout(fotos_layout.getContext());
                //}
            }
            /*linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            fotos_layout.addView(linearLayout);*/
        }
    }
}
