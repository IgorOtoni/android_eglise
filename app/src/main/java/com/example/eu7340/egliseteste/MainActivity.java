package com.example.eu7340.egliseteste;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.mn_eglise:
                    getSupportActionBar().setTitle("Plataforma Église");
                    Fragment egliseFragment = EgliseFragment.newInstance();
                    openFragment(egliseFragment);
                    return true;
                case R.id.mn_congregacoes:
                    getSupportActionBar().setTitle("Congregações");
                    Fragment congregacoesFragment = CongregacoesFragment.newInstance();
                    openFragment(congregacoesFragment);
                    return true;
            }
            return false;
        }
    };

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // CRIANDO CONEXAO
        //try {
            /*Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager
                    .getConnection("jdbc:mysql://192.168.0.250:3306/DB_SGE_EGLISE?"
                            + "user=paulo&password=123456");
            Connection connect = DriverManager
                    .getConnection("jdbc:mysql://192.168.0.105:3306/db_sge_eglise",
                            "root", "usbw");*/
            DatabaseHelper db_task = new DatabaseHelper(this);
            db_task.execute();

            getSupportActionBar().setTitle("Plataforma Église");
            Fragment egliseFragment = EgliseFragment.newInstance();
            openFragment(egliseFragment);
        /*}catch(SQLException ex){
            Toast.makeText(this, "Não foi possível conectar ao banco de dados!",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
            Toast.makeText(this, "Não foi possível resolver o classe do driver!",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }*/
    }

}
