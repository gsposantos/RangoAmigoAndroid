package com.example.guilherme.rangoamigo.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.fragments.AcessoFragment;
import com.example.guilherme.rangoamigo.fragments.FoneFragment;

public class AcessoActivity extends MasterActivity {


    ViewPager pagerAcesso;
    ViewPagerAdapter pagerAdapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acesso);

        //instancia do toolbar
        this.toolbar = (Toolbar) findViewById(R.id.toolbarAcesso);

        //seta o toolbar como action bar
        setSupportActionBar(toolbar);

        //recupera o action bar criado
        ActionBar actionBar = getSupportActionBar();

        //configura o icone
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //habilita visualizacao do menu
        actionBar.setDisplayHomeAsUpEnabled(true);

        //instancia o DrawerLayout da main activity para ter o controle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //instancia o NavigationView da main activity para ter o controle
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        /* EVENTO ITEM MENU GAVETA */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // marca item de menu recibio como selecionado
                item.setChecked(true);

                //fecha o menu pq o usuário selecionou um item
                mDrawerLayout.closeDrawers();

                /*VERIFICA QUAL ITEL FOI CLIADO PARA EXECUTAR ACAO*/

                switch (item.getItemId()){
                    case R.id.navigation_item_login:
                        setupViewPage((ViewPager) findViewById(R.id.pagerAcesso));
                        return true;
                    case R.id.navigation_item_fechar:
                        finish();
                        return true;
                    case R.id.navigation_item_novo:
                         /*TODO: Considerar passar parametros de preenchidos*/
                        //redireciona para tela de cadastro
                        Intent intent = new Intent(AcessoActivity.this, PerfilActivity.class);
                        intent.putExtra("modoTela", "NOVO");
                        startActivity(intent);
                        return true;
                }

                return true;
            }
        });

        this.setupViewPage((ViewPager) findViewById(R.id.pagerAcesso));
    }

    private void setupViewPage(ViewPager viewPager){
        /* Instancia uma ViewPager e o PagerAdapter. */
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    /* EVENTO ACTION BAR */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //pega o id do item
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                // clicou no icone para abrir o mennu
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return false;
        }
    }

    /* Classe PagerAdapter */
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new AcessoFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
