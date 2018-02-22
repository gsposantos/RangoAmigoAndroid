package com.example.guilherme.rangoamigo.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;
import com.example.guilherme.rangoamigo.fragments.EtapaDoisFragment;
import com.example.guilherme.rangoamigo.fragments.EtapaTresFragment;
import com.example.guilherme.rangoamigo.fragments.EtapaUmFragment;
import com.example.guilherme.rangoamigo.fragments.FoneFragment;


public class CadastroEventoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private FrameLayout progBarHolder;
    private ViewPager viewPagerEvento;
    private TabLayout tabEvento;
    private ViewPagerAdapter pagerAdapter;

    private Button btnVoltar;
    private Button btnAvancar;
    private TextView txtEtapa;

    //private int etapaCadastro;
    //private int tabEventoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        /*TODO: Consultar dados do evento */
        Intent it = getIntent();
        String sCodEvento = it.getStringExtra("CodEvento");

        //Toast.makeText(this, "Evento = " + sCodEvento, Toast.LENGTH_LONG).show();


        txtEtapa = (TextView) findViewById(R.id.txtEtapa);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarCadEvento);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progBarHolder = (FrameLayout) findViewById(R.id.progBarHolder);

        this.viewPagerEvento = (ViewPager) findViewById(R.id.pagerEvento);
        this.setupViewPage(this.viewPagerEvento);

        this.tabEvento = (TabLayout) findViewById(R.id.tabEventos);
        this.tabEvento.setupWithViewPager(this.viewPagerEvento);

        //invisivel por questoes de layout
        this.tabEvento.setVisibility(View.GONE);

        /* Desnecessario ... por enquanto */
        this.viewPagerEvento.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //faz nada

            }

            @Override
            public void onPageSelected(int position) {

                //Executa antes do evento do TAB
                //Atualizar informacao sobre etapa??

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                //faz nada

            }
        });

        /* EVENTOS TAB - click dos Botoes */

        this.tabEvento.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //poiciona tab - refrencia para saber a etapa e controlar a interface
                viewPagerEvento.setCurrentItem(tab.getPosition());

                //limpar action menu e guardar posicao selecionada
                int tabPosition = tab.getPosition();
                //invalidateOptionsMenu();

                if (tabPosition == 0) {
                    //atualiar informacao etapa
                    btnVoltar.setVisibility(View.GONE);
                    btnAvancar.setVisibility(View.VISIBLE);
                    txtEtapa.setText("1 / 3");

                    //Validar dados Evento - envolver fragmento

                } else if (tabPosition == 1) {
                    //atualiar informacao etapa
                    btnVoltar.setVisibility(View.VISIBLE);
                    btnAvancar.setVisibility(View.VISIBLE);
                    txtEtapa.setText("2 / 3");

                } else {
                    //atualiar informacao etapa
                    btnVoltar.setVisibility(View.VISIBLE);
                    btnAvancar.setVisibility(View.VISIBLE);
                    txtEtapa.setText("3 / 3");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        /* EVENTOS BOTOES - Avancar e Voltar */

        btnVoltar =  (Button) findViewById(R.id.btnVoltar);
        btnVoltar.setVisibility(View.GONE);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = tabEvento.getSelectedTabPosition();
                if(position != 0) {
                    tabEvento.getTabAt(position - 1).select();
                }

            }
        });

        btnAvancar = (Button) findViewById(R.id.btnAvancar);
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = tabEvento.getSelectedTabPosition();
                if(position != 2) {
                    tabEvento.getTabAt(position + 1).select();
                }

            }
        });
    }


    private void setupViewPage(ViewPager viewPager){
        /* Instancia uma ViewPager e o PagerAdapter. */
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EtapaUmFragment(), "Etapa 1");
        pagerAdapter.addFragment(new EtapaDoisFragment(), "Etapa 2");
        pagerAdapter.addFragment(new EtapaTresFragment(), "Etapa 3");
        viewPager.setAdapter(pagerAdapter);
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
