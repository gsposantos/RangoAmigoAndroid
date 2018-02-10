package com.example.guilherme.rangoamigo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;

public class CadastroEventoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private FrameLayout progBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        /*TODO: Consultar dados do evento */
        Intent it = getIntent();
        String sCodEvento = it.getStringExtra("CodEvento");

        Toast.makeText(this, "Evento = " + sCodEvento, Toast.LENGTH_LONG).show();



        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarCadEvento);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progBarHolder = (FrameLayout) findViewById(R.id.progBarHolder);
    }


    /* EVENTO ACTION BAR */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //pega o id do item
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                // clicou no icone para voltar
                finish();
                return true;
            default:
                return false;
        }
    }
}
