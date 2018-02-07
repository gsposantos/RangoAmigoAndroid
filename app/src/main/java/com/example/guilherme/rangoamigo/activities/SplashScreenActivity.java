package com.example.guilherme.rangoamigo.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide(); //remove barra superior
        setContentView(R.layout.activity_splash_screen);

        AcessoPreferences.setContext(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //aqui deve verificar se ja tem sessao para definir o direcionamento
                //por enquanto vai direto pro login
                Intent intent;
                if(!AcessoPreferences.getDadosPerfil().isEmpty()){
                    intent = new Intent(SplashScreenActivity.this,EventoActivity.class);
                    //passar parametros?...
                }
                else{
                    intent = new Intent(SplashScreenActivity.this,AcessoActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}
