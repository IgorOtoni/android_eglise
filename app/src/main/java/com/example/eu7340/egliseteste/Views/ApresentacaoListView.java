package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApresentacaoListView extends LinearLayout {

    private Membro membro;
    private Funcao funcao;
    private View view;

    public ApresentacaoListView(Context context, AttributeSet attrs, Membro membro, Funcao funcao) {
        super(context, attrs);
        this.membro = membro;
        this.funcao = funcao;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.apresentacao_list, this);

        TextView nome = view.findViewById(R.id.apresentacao_nome);
        nome.setText(membro.getNome());

        TextView funcao_ = view.findViewById(R.id.apresentacao_funcao);
        funcao_.setText(funcao.getNome());

        TextView descricao = view.findViewById(R.id.apresentacao_descricao);
        descricao.setText(membro.getDescricao());

        CarregaFotoMembro carregaFotoMembro_task = new CarregaFotoMembro();
        carregaFotoMembro_task.execute(membro);
    }

    private class CarregaFotoMembro extends AsyncTask<Membro, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Membro... params) {
            try {
                URL url = new URL("http://eglise.com.br/storage/" + (params[0].getFoto() != null ? "membros/" + params[0].getFoto() : "no-foto.png"));
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
            ImageView imageView = view.findViewById(R.id.apresentacao_foto);
            imageView.setImageBitmap(result);
        }
    }

}
