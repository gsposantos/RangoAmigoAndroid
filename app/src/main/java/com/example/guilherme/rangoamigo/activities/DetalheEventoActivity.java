package com.example.guilherme.rangoamigo.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;
import com.example.guilherme.rangoamigo.fragments.ConvidadosFragment;
import com.example.guilherme.rangoamigo.fragments.DetalhesFragment;



public class DetalheEventoActivity extends MasterActivity {

    private Toolbar toolbar;
    private FrameLayout progBarHolder;
    private ViewPager viewPagerDetalheEvento;
    private TabLayout tabDetalheEvento;
    private ViewPagerAdapter pagerAdapter;
    private String sCodEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_evento);/*TODO: Consultar dados do evento */
        Intent it = getIntent();
        this.sCodEvento = it.getStringExtra("CodEvento");

        //Toast.makeText(this, "Evento = " + sCodEvento, Toast.LENGTH_LONG).show();


        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarDetalheEvento);
        toolbar.setTitle("Detalhes do Evento");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progBarHolder = (FrameLayout) findViewById(R.id.progBarHolder);

        //Configura abas e seus respectivos conteudos
        this.viewPagerDetalheEvento = (ViewPager) findViewById(R.id.pagerDetalheEvento);
        this.setupViewPage(this.viewPagerDetalheEvento);

        this.tabDetalheEvento = (TabLayout) findViewById(R.id.tabDetalheEventos);
        this.tabDetalheEvento.setupWithViewPager(this.viewPagerDetalheEvento);
    }

    public void setupViewPage(ViewPager viewPagerDetalheEvento) {

        /* Instancia uma ViewPager e o PagerAdapter. */
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new DetalhesFragment(), "Detalhes");
        pagerAdapter.addFragment(new ConvidadosFragment(), "Convidados");
        viewPagerDetalheEvento.setAdapter(pagerAdapter);
    }

    public String getCodEvento() {
        return sCodEvento;
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
