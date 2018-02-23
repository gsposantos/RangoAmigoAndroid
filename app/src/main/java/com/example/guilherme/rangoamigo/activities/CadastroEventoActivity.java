package com.example.guilherme.rangoamigo.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;
import com.example.guilherme.rangoamigo.fragments.EtapaDoisFragment;
import com.example.guilherme.rangoamigo.fragments.EtapaTresFragment;
import com.example.guilherme.rangoamigo.fragments.EtapaUmFragment;
import com.example.guilherme.rangoamigo.fragments.FoneFragment;
import com.example.guilherme.rangoamigo.models.Convidado;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.RetornoSimples;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.example.guilherme.rangoamigo.utils.connections.JSONParser;
import com.example.guilherme.rangoamigo.utils.connections.NetworkState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CadastroEventoActivity extends MasterActivity {

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
    private Calendar calendar;
    private Evento oEventoCadastro;

    //private int etapaCadastro;
    //private int tabEventoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        /*TODO: Consultar dados do evento */
        Intent it = getIntent();
        String sCodEvento = it.getStringExtra("CodEvento");

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

        this.viewPagerEvento.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        /* Desnecessario ... por enquanto
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
        });*/

        /* EVENTOS TAB - click dos Botoes */

        this.tabEvento.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //limpar action menu e guardar posicao selecionada
                int tabPosition = tab.getPosition();
                //invalidateOptionsMenu();

                //setar de cada posiao fragmento
                //FragmentManager fm = getSupportFragmentManager();
                //String sFragTag;// = getTagFragment(viewPagerEvento.getId(), tabPosition);

                if (tabPosition == 0) {
                    //atualiar informacao etapa
                    btnVoltar.setVisibility(View.GONE);
                    btnAvancar.setVisibility(View.VISIBLE);
                    txtEtapa.setText("1 / 3");

                } else if (tabPosition == 1) {
                    //atualiar informacao etapa
                    btnVoltar.setVisibility(View.VISIBLE);
                    btnAvancar.setVisibility(View.VISIBLE);
                    txtEtapa.setText("2 / 3");

                    /*
                    sFragTag = getTagFragment(viewPagerEvento.getId(), tabPosition-1);
                    EtapaUmFragment etapaUmFragment = (EtapaUmFragment) fm.findFragmentByTag(sFragTag);

                    //Validar dados Evento - envolver fragmento
                    if(etapaUmFragment.validaDadosEvento()){
                        oEventoCadastro = etapaUmFragment.getDatosEvento();
                    }
                    */

                } else {
                    //atualiar informacao etapa
                    btnVoltar.setVisibility(View.VISIBLE);
                    btnAvancar.setVisibility(View.VISIBLE);
                    txtEtapa.setText("3 / 3");

                    /*
                    sFragTag = getTagFragment(viewPagerEvento.getId(), tabPosition-1);
                    EtapaDoisFragment etapaDoisFragment = (EtapaDoisFragment) fm.findFragmentByTag(sFragTag);

                    //Validar dados Evento Mapa - envolver fragmento
                    //Validar dados Evento - envolver fragmento
                    if(etapaDoisFragment.validaDadosEvento()){
                        oEventoCadastro = etapaDoisFragment.getDatosEvento(oEventoCadastro);
                    }
                    */
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
                if(position < 2) {

                    FragmentManager fm = getSupportFragmentManager();
                    String sFragTag = getTagFragment(viewPagerEvento.getId(), position);
                    switch (position){
                        case 0:
                            //valida etapa 1
                            EtapaUmFragment etapaUmFragment = (EtapaUmFragment) fm.findFragmentByTag(sFragTag);
                            //Validar dados Evento - envolver fragmento
                            if(etapaUmFragment.validaDadosEvento()){
                                oEventoCadastro = etapaUmFragment.getDatosEvento();
                                position = position + 1;
                            }
                            break;
                        case 1:
                            //valida etapa 2
                            EtapaDoisFragment etapaDoisFragment = (EtapaDoisFragment) fm.findFragmentByTag(sFragTag);
                            //Validar dados Evento Mapa - envolver fragmento
                            if(etapaDoisFragment.validaDadosEvento()){
                                oEventoCadastro = etapaDoisFragment.getDatosEvento(oEventoCadastro);
                                position = position + 1;
                            }
                            break;
                    }
                    //seta proximo fragment ou atual
                    tabEvento.getTabAt(position).select();
                }else if(position == 2){

                    FragmentManager fm = getSupportFragmentManager();
                    String sFragTag = getTagFragment(viewPagerEvento.getId(), position);
                    EtapaTresFragment etapaTresFragment = (EtapaTresFragment) fm.findFragmentByTag(sFragTag);

                    //Atribui a lista de contatos
                    if(etapaTresFragment.validaConvidadosEvento()) {
                        oEventoCadastro.Convidados = etapaTresFragment.getConvidadosSelecioandos();
                        //adicionar o organziador
                        Convidado oConvidadoOrg = new Convidado();
                        oConvidadoOrg.Organizador = true;
                        oConvidadoOrg.CelNumero = getPerfilPreferences().CelNumero;

                        oEventoCadastro.Convidados.add(oConvidadoOrg);

                        //Finaliza o cadastro ...

                        EventoAsyncTask task = new EventoAsyncTask();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            task.execute();
                        }
                    }
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

    public void apresentaMensagem(String sTitulo, String sMensagem){
        this.showAlert(sTitulo, sMensagem);
    }

    public void desviaSelecionaDataEvento() {

        calendar = Calendar.getInstance();

        int anoAtual = calendar.get(Calendar.YEAR) ;
        int mesAtual = calendar.get(Calendar.MONTH);
        int diaAtual = calendar.get(Calendar.DAY_OF_MONTH);

        // Abre um Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(CadastroEventoActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {

                        int horaAtual = calendar.get(Calendar.HOUR);
                        int minAtual = calendar.get(Calendar.MINUTE);

                        final int anoSelecionado = ano;
                        final int mesSelecionado = mes;
                        final int diaSelecionado = dia;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(CadastroEventoActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hora, int minuto) {

                                        // recupera data selecionada para adicionar ao evento
                                        Date oNovaData; // = new GregorianCalendar(anoSelecionado, mesSelecionado, diaSelecionado).getTime();

                                        Calendar oDateAux = Calendar.getInstance();
                                        oDateAux.setTimeInMillis(0);
                                        oDateAux.set(anoSelecionado, mesSelecionado, diaSelecionado, hora, minuto, 0);
                                        oNovaData = oDateAux.getTime();

                                        //validar data (maior que hoje)
                                        if(oNovaData.after(calendar.getTime())){
                                            //setar o campo no fragmento
                                            FragmentManager fm = getSupportFragmentManager();
                                            String sFragTag = getTagFragment(viewPagerEvento.getId(), 0);
                                            EtapaUmFragment etapaFragment = (EtapaUmFragment) fm.findFragmentByTag(sFragTag);
                                            etapaFragment.configuraDataEvento(oNovaData);
                                        }
                                        else
                                        {
                                            showAlert("Data Inválida!", "Selecione uma data posterior a data atual.");
                                        }

                                    }
                                }, horaAtual, minAtual, true);

                        timePickerDialog.show();
                    }
                }, anoAtual, mesAtual, diaAtual);

        datePickerDialog.show();

    }

    public Perfil getPerfilPreferences()
    {
        Gson gson = new Gson();
        Type listType = new TypeToken<RetornoSimples<Perfil>>(){}.getType();
        RetornoSimples<Perfil> oRetPerfilAux = gson.fromJson(AcessoPreferences.getDadosPerfil(), listType);

        Perfil oPerfil = oRetPerfilAux.Dados;

        return oPerfil;
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

    /* --------------  Asynktask ----------------------- */
    class EventoAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public EventoAsyncTask(){
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

            url = url + "api/eventos/Incluir";

            try{
                Gson gson= new Gson();
                String sJson = gson.toJson(oEventoCadastro);
                StringEntity postingString = new StringEntity(sJson); //convert to json
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", postingString);
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

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progBarHolder.setAnimation(outAnimation);
            progBarHolder.setVisibility(View.GONE);

            if(jsonObject != null){

                Gson gson = new Gson();
                Type listType = new TypeToken<RetornoSimples<Integer>>(){}.getType();
                RetornoSimples<Integer> retornoPerfil = gson.fromJson(jsonObject.toString(), listType);

                if(retornoPerfil.Ok){
                    if(retornoPerfil.Dados == 1){
                        String sMensagem = "Evento Cadastrado.  Volte para acompanhar a participação.";

                        showAlert("Sucesso!", sMensagem, new AlertDialogCallback() {
                            @Override
                            public void call() {
                                finish();
                            }
                        });
                    }
                    else{
                        showAlert("Informação", retornoPerfil.Mensagem);
                    }
                }
                else
                {
                    //mensagem de erro
                    showAlert("Informação", retornoPerfil.Mensagem);
                }

                Log.i("ObjetoJson - >",jsonObject.toString());
            }
        }
    }

}
