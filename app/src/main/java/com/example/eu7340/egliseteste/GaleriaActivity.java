package com.example.eu7340.egliseteste;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eu7340.egliseteste.utils.MyJSONArray;
import com.example.eu7340.egliseteste.utils.MyJSONObject;
import com.example.eu7340.egliseteste.utils.Servidor;
import com.example.eu7340.egliseteste.utils.Sessao;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GaleriaActivity extends AppCompatActivity {

    private MyJSONObject configuracao;
    private MyJSONObject galeria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        getSupportActionBar().setTitle("Evento fixo");

        configuracao = Sessao.ultima_configuracao;

        Gson gson = new Gson();
        galeria = gson.fromJson(getIntent().getStringExtra("galeria_detalhe"), MyJSONObject.class);

        TextView nome = findViewById(R.id.galeria_nome);
        nome.setText(galeria.getString("nome"));

        TextView descricao = findViewById(R.id.galeria_descricao);
        descricao.setText(galeria.getString("descricao"));

        CarregaGalerias carregaGalerias = new CarregaGalerias(this);
        carregaGalerias.execute();
    }

    private class CarregaGaleriasRetorno{
        public boolean erro;
        public String retorno;
        public String mensagem;
    }

    private class CarregaGalerias extends AsyncTask<Object, Object, CarregaGaleriasRetorno> {

        private final Context context;

        public CarregaGalerias(Context context) {
            this.context = context;
        }

        public CarregaGaleriasRetorno doInBackground(Object... objects) {

            String retorno = null;
            CarregaGaleriasRetorno retorno_ = new CarregaGaleriasRetorno();
            retorno_.erro = true;

            try {

                URL url = new URL(Servidor.ip + "/api/galeria/" + configuracao.getString("url") + "/" + galeria.getString("id"));

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

        protected void onPostExecute(CarregaGaleriasRetorno result) {
            if(!isCancelled()) {

                if (result.erro) {
                    Toast.makeText(context, result.mensagem,
                            Toast.LENGTH_LONG).show();
                } else {
                    MyJSONObject result_ = new MyJSONObject(result.retorno);
                    MyJSONObject galeria = new MyJSONObject(result_.getObjetc("galeria"));
                    MyJSONArray fotos = new MyJSONArray(result_.getArray("fotos"));

                    // =========================================================================================

                    LinearLayout fotos_layout = findViewById(R.id.fotos_layout);
                    LinearLayout linearLayout = new LinearLayout(fotos_layout.getContext());

                    for(int x = 0; x < fotos.size(); x++){
                        MyJSONObject foto = new MyJSONObject(fotos.getObjetc(x));
                        byte[] decodedString = Base64.decode(foto.getString("foto"), Base64.DEFAULT);
                        Bitmap foto_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        FrameLayout.LayoutParams linearLayout_ = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        linearLayout_.setMargins(0, 0, 0, 10);
                        linearLayout.setLayoutParams(linearLayout_);
                        ImageView imageView = new ImageView(fotos_layout.getContext());
                        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                        imageView.setAdjustViewBounds(true);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageBitmap(foto_);
                        linearLayout.addView(imageView);

                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        fotos_layout.addView(linearLayout);
                        linearLayout = new LinearLayout(fotos_layout.getContext());
                    }

                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    fotos_layout.addView(linearLayout);

                    // =========================================================================================
                }

            }
        }
    }
}
