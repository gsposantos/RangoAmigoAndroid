package com.example.guilherme.rangoamigo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;
import com.example.guilherme.rangoamigo.fragments.ConvidadosFragment;
import com.example.guilherme.rangoamigo.fragments.ConviteFragment;
import com.example.guilherme.rangoamigo.fragments.DetalhesFragment;
import com.example.guilherme.rangoamigo.fragments.EventoFragment;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.RetornoLista;
import com.example.guilherme.rangoamigo.models.RetornoSimples;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.example.guilherme.rangoamigo.utils.connections.JSONParser;
import com.example.guilherme.rangoamigo.utils.connections.NetworkState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class DetalheEventoActivity extends MasterActivity {

    private Toolbar toolbar;
    private FrameLayout progBarHolder;
    private ViewPager viewPagerDetalheEvento;
    private TabLayout tabDetalheEvento;
    private ViewPagerAdapter pagerAdapter;
    private String sCodEvento;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private Evento oEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_evento);

        /*TODO: Consultar dados do evento */
        Intent it = getIntent();
        this.sCodEvento = it.getStringExtra("CodEvento");

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

    @Override
    protected void onResume() {
        super.onResume();

        if(!this.sCodEvento.isEmpty() || this.sCodEvento != null) {
            this.carregaDetalheEvento();
        }
        else
        {
            this.showAlert("Evento Inváido!", "Não foi possível identificar o evento para carregar os detalhes.", new AlertDialogCallback() {
                @Override
                public void call() {
                    finish();
                }
            });
        }
    }

    public void carregaDetalheEvento()
    {
        DetalheEventoAsyncTask task = new DetalheEventoAsyncTask();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
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

    public Evento getEvento() {
        return oEvento;
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

    private String getTagFragment(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    /* --------------  Asynktask ----------------------- */
    class DetalheEventoAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public DetalheEventoAsyncTask(){
            this.url = getResources().getString(R.string.urlRangoAmigo);
        }

        @Override
        protected void onPreExecute() {

            if(!NetworkState.isNetworkAvaible(getSystemService(Context.CONNECTIVITY_SERVICE))){
                showAlert("Erro de Conexão!", "É necessário conexão com internet para executar essa operação.");
            }

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progBarHolder.setAnimation(inAnimation);
            progBarHolder.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            url = url + "api/eventos/DetalharEvento";

            try{

                Gson gson = new Gson();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("codEvento", sCodEvento));

                JSONObject json = jsonParser.makeHttpRequest(url, "GET", params);
                return json;
            }
            catch (Exception e)
            {
                showAlert("Erro de consulta!", "Ocorreu um erro tentando consultar os dados");
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            JSONArray jsonArray;

            if(jsonObject != null){

                Gson gson = new Gson();
                Type listType = new TypeToken<RetornoSimples<Evento>>(){}.getType();
                RetornoSimples<Evento> retornoEventos = gson.fromJson(jsonObject.toString(), listType);

                if(retornoEventos.Ok){
                    if(retornoEventos.Dados != null){

                        /*TODO: Testar se tem registros e mostrar mensagem  */
                        String sFragTag;
                        oEvento = retornoEventos.Dados;
                        FragmentManager fm = getSupportFragmentManager();
                        sFragTag = getTagFragment(viewPagerDetalheEvento.getId(), 0);
                        DetalhesFragment detalheEvento = (DetalhesFragment) fm.findFragmentByTag(sFragTag);
                        detalheEvento.carregaDetalheEvento(oEvento);

                        sFragTag = getTagFragment(viewPagerDetalheEvento.getId(), 1);
                        ConvidadosFragment convidadosEvento = (ConvidadosFragment) fm.findFragmentByTag(sFragTag);
                        convidadosEvento.carregaConvidadosEvento(oEvento.Convidados);
                    }
                    else {
                        showAlert("Informação", "Eventos não encontrados.");
                    }
                }
                else
                {
                    //mensagem de erro
                    showAlert("Informação", retornoEventos.Mensagem);
                }

                Log.i("eventosJson - >", jsonObject.toString());
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progBarHolder.setAnimation(outAnimation);
            progBarHolder.setVisibility(View.GONE);

        }
    }

}
