package com.example.eu7340.egliseteste.Views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eu7340.egliseteste.DB.DB;
import com.example.eu7340.egliseteste.DB.FrequenciaDAO;
import com.example.eu7340.egliseteste.DB.MembroComunidadeDAO;
import com.example.eu7340.egliseteste.Models.Comunidade;
import com.example.eu7340.egliseteste.Models.Frequencia;
import com.example.eu7340.egliseteste.Models.Funcao;
import com.example.eu7340.egliseteste.Models.Membro;
import com.example.eu7340.egliseteste.Models.MembroComunidade;
import com.example.eu7340.egliseteste.R;
import com.example.eu7340.egliseteste.utils.Sessao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequenciaListView extends LinearLayout {

    private Membro membro;
    private Frequencia frequencia;
    private View view;

    private TextView membro_;
    private CheckBox cb;

    public FrequenciaListView(Context context, AttributeSet attrs, Frequencia frequencia, Membro membro) {
        super(context, attrs);
        this.frequencia = frequencia;
        this.membro = membro;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.frequencia_list, this);

        membro_ = view.findViewById(R.id.frequencia_membro);
        membro_.setText(membro.getNome());

        cb = view.findViewById(R.id.frequencia_cb);
        cb.setChecked(frequencia.isAusente());
        cb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LancaPresenca lancaPresenca_task = new LancaPresenca();
                lancaPresenca_task.execute(cb.isChecked());
            }
        });
    }

    private class LancaPresenca extends AsyncTask<Boolean, Object, Object>{
        @Override
        protected Boolean doInBackground(Boolean... params) {
            try {

                FrequenciaDAO frequenciaDAO = new FrequenciaDAO(DB.connection);
                frequencia.setAusente(params[0]);
                /*if(frequencia.getId() == -1){
                    int id = frequenciaDAO.create(frequencia);
                    frequencia.setId(id);
                }else{*/
                    frequenciaDAO.update(frequencia);
                //}

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object resp) {

        }
    }

}
