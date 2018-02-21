package com.example.guilherme.rangoamigo.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;
import com.example.guilherme.rangoamigo.fragments.ContatoFragment;
import com.example.guilherme.rangoamigo.fragments.ConviteFragment;
import com.example.guilherme.rangoamigo.fragments.EventoFragment;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;

import java.util.ArrayList;
import java.util.List;

public class EventoActivity extends MasterActivity   {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ViewPagerAdapter pagerAdapter;
    private ViewPager viewPagerEvento;
    private TabLayout tabEvento;
    private int tabEventoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        //instancia do toolbar
        this.toolbar = (Toolbar) findViewById(R.id.toolbarEvento);

        //seta o toolbar como action bar
        setSupportActionBar(toolbar);

        //recupera o action bar criado
        ActionBar actionBar = getSupportActionBar();

        //configura o icone
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //habilita visualizacao do menu
        actionBar.setDisplayHomeAsUpEnabled(true);

        //instancia o DrawerLayout da main activity para ter o controle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_eventos);

        //instancia o NavigationView da main activity para ter o controle
        navigationView = (NavigationView) findViewById(R.id.navigation_view_eventos);


        /* EVENTO ITEM MENU GAVETA */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // marca item de menu recibio como selecionado
                //item.setChecked(false);

                //fecha o menu pq o usuário selecionou um item
                mDrawerLayout.closeDrawers();

                 /*VERIFICA QUAL ITEL FOI CLIADO PARA EXECUTAR ACAO*/
                switch (item.getItemId()){
                    case R.id.navigation_item_sair:
                        efetuarLogout();
                        return true;
                    case R.id.navigation_item_perfil:
                        desviaCadastroPerfil();
                        return true;
                    case R.id.navigation_item_fechar:
                        showConfirm("Aviso!", "Deseja realmente fechar o aplicativo?", new ConfirmDialogCallback() {
                            @Override
                            public void callOk() {
                                finish();
                            }

                            @Override
                            public void callCancel() {
                                //faz nada
                            }
                        });
                        //finish();
                        return true;
                }
                return true;
            }
        });

        this.viewPagerEvento = (ViewPager) findViewById(R.id.pagerEvento);
        this.setupViewPage(this.viewPagerEvento);

        this.tabEvento = (TabLayout) findViewById(R.id.tabEventos);
        this.tabEvento.setupWithViewPager(this.viewPagerEvento);

        tabEvento.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //psiciona tab
                viewPagerEvento.setCurrentItem(tab.getPosition());

                //limpar action menu e guardar posicao selecionada
                tabEventoPosition = tab.getPosition();
                invalidateOptionsMenu();

                if (tabEventoPosition == 0) {
                    //atualiar fragment de eventos

                } else if (tabEventoPosition == 1) {
                    //atualiar fragment de convites

                } else {
                    //atualiar fragment de contatos
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void desviaCadastroPerfil() {
        Intent intent = new Intent(EventoActivity.this, PerfilActivity.class);
        startActivity(intent);
    }

    private void efetuarLogout() {
        AcessoPreferences.setContext(this);
        AcessoPreferences.setDadosPerfil("");

        Intent intent = new Intent(EventoActivity.this, AcessoActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupViewPage(ViewPager viewPager){
        /* Instancia uma ViewPager e o PagerAdapter. */
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EventoFragment(), "Eventos");
        pagerAdapter.addFragment(new ConviteFragment(), "Convites");
        pagerAdapter.addFragment(new ContatoFragment(), "Contatos");
        viewPager.setAdapter(pagerAdapter);
    }

    private String getTagFragment(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    public void apresentaMensagem(String sTitulo, String sMensagem){
        this.showAlert(sTitulo, sMensagem);
    }

    /* EVENTO ACTION BAR */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        //verificar qual tab esta seleciona para inflar o menu certo.
        if (tabEventoPosition == 0)
        {
            //carrega opcoes eventos
            inflater.inflate(R.menu.menu_evento, menu);
        }
        else if (tabEventoPosition == 1) {
            //carrega action eventos
            inflater.inflate(R.menu.menu_evento, menu);
        }
        else {
            //carrega opcoes contatos
            inflater.inflate(R.menu.menu_contato, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //pega o id do item
        int id = item.getItemId();
        String sFragTag;

        FragmentManager fm = getSupportFragmentManager();

        switch (id){

            case android.R.id.home:
                // clicou no icone para abrir o mennu
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_item_sincronizar:
                //para sincronizar os contatos deve chamar o método do fragment de contatos.
                sFragTag = getTagFragment(viewPagerEvento.getId(), 2);
                ContatoFragment contatosEvento = (ContatoFragment) fm.findFragmentByTag(sFragTag);
                contatosEvento.carregaContatos();
                return true;

            case R.id.menu_item_atualizar_eventos:
                //para atualizar os eventos deve chamar o método do fragment de eventos.
                sFragTag = getTagFragment(viewPagerEvento.getId(), 0);
                EventoFragment fragEvento = (EventoFragment) fm.findFragmentByTag(sFragTag);
                fragEvento.carregaEventos();

                sFragTag = getTagFragment(viewPagerEvento.getId(), 1);
                ConviteFragment fragConvite = (ConviteFragment) fm.findFragmentByTag(sFragTag);
                fragConvite.carregaConvites();

                return true;
            default:
                return false;
        }
    }

}
