package com.example.eu7340.egliseteste;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eu7340.egliseteste.DB.DatabaseHelper;
import com.example.eu7340.egliseteste.Fragments.CongregacoesFragment;
import com.example.eu7340.egliseteste.Fragments.EgliseFragment;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private static Fragment last_fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.mn_eglise:
                    //getSupportActionBar().setTitle("Plataforma Église");
                    Fragment egliseFragment = EgliseFragment.newInstance();
                    openFragment(egliseFragment);
                    return true;
                case R.id.mn_congregacoes:
                    //getSupportActionBar().setTitle("Congregações");
                    Fragment congregacoesFragment = CongregacoesFragment.newInstance();
                    openFragment(congregacoesFragment);
                    return true;
            }
            return false;
        }
    };

    private void openFragment(Fragment fragment) {
        last_fragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DatabaseHelper db_task = new DatabaseHelper(this);
        db_task.execute();

        getSupportActionBar().setTitle("Plataforma Église");
        if(last_fragment == null) {
            Fragment egliseFragment = EgliseFragment.newInstance();
            openFragment(egliseFragment);
        }else{
            openFragment(last_fragment);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        last_fragment = null;
        finish();
    }
}
