package com.example.eu7340.egliseteste.DB;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import java.sql.SQLException;

/**
 * Created by EU7340 on 04/12/2018.
 */
public class DatabaseHelper extends AsyncTask<Object, Object, Object> {
    private static final String databaseName = "jdbc:mysql://192.168.0.103:3306/db_sge_eglise";
    private static final String user = "IgorOtoni";
    private static final String password = "7340";
    private final Context context;

    public DatabaseHelper(Context context){
        this.context = context;
    }

    //192.168.0.250
    //191.252.2.28
    // root Disc1plin@
    public Object doInBackground(Object... objects) {
        try {
            DB.connection = new JdbcConnectionSource(databaseName, user, password);
            DB.connection.initialize();
        }
        catch(SQLException ex){
            Toast.makeText(this.context, "Não foi possível conectar ao banco de dados!",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Object object) {
        /*Toast.makeText(this.context, "Comunicando com o banco de dados!",
                Toast.LENGTH_LONG).show();*/
    }

    /*public void close() throws IOException {
        DB.connection.close();
    }*/
}
