package com.example.guilherme.rangoamigo.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;
import com.example.guilherme.rangoamigo.fragments.ConvidadosFragment;
import com.example.guilherme.rangoamigo.fragments.ConviteFragment;
import com.example.guilherme.rangoamigo.fragments.DetalhesFragment;
import com.example.guilherme.rangoamigo.fragments.EventoFragment;
import com.example.guilherme.rangoamigo.models.DataEvento;
import com.example.guilherme.rangoamigo.models.DataEventoVoto;
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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class DetalheEventoActivity extends MasterActivity {

    private Toolbar toolbar;
    private FrameLayout progBarHolder;
    private ViewPager viewPagerDetalheEvento;
    private TabLayout tabDetalheEvento;
    private ViewPagerAdapter pagerAdapter;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private Calendar calendar;

    private Evento oEvento;
    private DataEvento oDataEventoSugestao;
    private boolean bOrganizador = false;
    private int botaoAcionado;
    private String sCodEvento;

    public static final int ACAO_OBTER = 0;
    public static final int ACAO_RECUSAR = 1;
    public static final int ACAO_REMOVER = 2;
    public static final int ACAO_DATAADD = 3;


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
            botaoAcionado = ACAO_OBTER;
            this.executaDetalheEvento();
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

    public void executaDetalheEvento()
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

    private void desviaCadastroEvento() {

        Intent intent = new Intent(DetalheEventoActivity.this, CadastroEventoActivity.class);
        //parametros
        startActivity(intent);

    }

    private void desviaSugestaoDataEvento() {

        calendar = Calendar.getInstance();

        int anoAtual = calendar.get(Calendar.YEAR) ;
        int mesAtual = calendar.get(Calendar.MONTH);
        int diaAtual = calendar.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(DetalheEventoActivity.this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int ano, int mes, int dia) {

                    int horaAtual = calendar.get(Calendar.HOUR);
                    int minAtual = calendar.get(Calendar.MINUTE);

                    final int anoSelecionado = ano;
                    final int mesSelecionado = mes;
                    final int diaSelecionado = dia;

                    TimePickerDialog timePickerDialog = new TimePickerDialog(DetalheEventoActivity.this,
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

                                        //seta acao ACAO_DATAADD
                                        oDataEventoSugestao = new DataEvento();
                                        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                        oDataEventoSugestao.DiaEvento = formatter.format(oNovaData);

                                        Perfil oPerfilDataEvento = getPerfilPreferences();
                                        DataEventoVoto oDataEventoVotoAux = new DataEventoVoto();

                                        oDataEventoVotoAux.CelNumero = oPerfilDataEvento.CelNumero;
                                        oDataEventoVotoAux.Voto = true; //sugeriu a data e ja vota nela.
                                        oDataEventoSugestao.Participacao = new ArrayList<DataEventoVoto>();
                                        oDataEventoSugestao.Participacao.add(oDataEventoVotoAux);

                                        botaoAcionado = ACAO_DATAADD;
                                        executaDetalheEvento();
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

    private void desviaListaDataEvento() {


    }

    /* EVENTO ACTION BAR */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        if(bOrganizador){
            //carrega opcoes eventos
            inflater.inflate(R.menu.menu_detalhe_evento, menu);
        }
        else{
            inflater.inflate(R.menu.menu_detalhe_convite, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //pega o id do item
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                // clicou no icone para voltar
                finish();
                return true;
            case R.id.menu_item_aceitar:
                // clicou no icone para aceitar
                desviaListaDataEvento();
                return true;
            case R.id.menu_item_sugerir:
                // clicou no icone para sugerir data
                desviaSugestaoDataEvento();
                return true;
            case R.id.menu_item_recusar:
                // clicou no icone para RECUSAR todas as datas
                botaoAcionado = ACAO_RECUSAR;
                this.executaDetalheEvento();
                return true;
            case R.id.menu_item_remover:
                // clicou no icone para EXCLUIR EVENTO
                botaoAcionado = ACAO_REMOVER;
                this.executaDetalheEvento();
                return true;
            case R.id.menu_item_editar:
                // clicou no icone para EDITAR EVENTO
                desviaCadastroEvento();
                return true;
            default:
                return false;
        }
    }

    private String getTagFragment(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    private Perfil getPerfilPreferences()
    {
        Gson gson = new Gson();
        Type listType = new TypeToken<RetornoSimples<Perfil>>(){}.getType();
        RetornoSimples<Perfil> oRetPerfilAux = gson.fromJson(AcessoPreferences.getDadosPerfil(), listType);

        Perfil oPerfil = oRetPerfilAux.Dados;

        return oPerfil;
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

            try{

                Gson gson = new Gson();

                switch (botaoAcionado){

                    case ACAO_OBTER:
                        url = url + "api/eventos/DetalharEvento";

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("codEvento", sCodEvento));

                        JSONObject jsonObter = jsonParser.makeHttpRequest(url, "GET", params);
                        return jsonObter;

                    case ACAO_RECUSAR:
                        url = url + "api/eventos/VotarDataEvento";

                        Perfil oPerfilAux = getPerfilPreferences();
                        Evento oEventoAux = new Evento();

                        oEventoAux.CodEvento = Integer.valueOf(sCodEvento);

                        DataEvento oDataEvento;
                        DataEventoVoto oDataEventoVoto;

                        oEventoAux.Datas = new ArrayList<DataEvento>();

                        for(int i = 0; i < oEvento.Datas.size() ; i++ ){

                            oDataEvento = new DataEvento();
                            oDataEvento.Participacao = new ArrayList<DataEventoVoto>();
                            oDataEventoVoto = new DataEventoVoto();

                            oDataEventoVoto.CelNumero = oPerfilAux.CelNumero;
                            oDataEventoVoto.Voto = false; //sempre false para o voto

                            oDataEvento.Participacao.add(oDataEventoVoto);
                            oEventoAux.Datas.add(oDataEvento);
                        }

                        String sJson = gson.toJson(oEventoAux);
                        StringEntity postingString = new StringEntity(sJson);
                        JSONObject jsonDataEvento = jsonParser.makeHttpRequest(url, "POST", postingString);

                        return jsonDataEvento;

                    case ACAO_DATAADD:
                        //SugerirDataEvento(Evento oEventoVoto)
                        url = url + "api/eventos/SugerirDataEvento";

                        // instanciar evento com data evento e uma participacao
                        Evento oEventoDataEvento = new Evento();
                        oEventoDataEvento.CodEvento = Integer.valueOf(sCodEvento);
                        oEventoDataEvento.Datas = new ArrayList<DataEvento>();
                        oEventoDataEvento.Datas.add(oDataEventoSugestao);

                        String sJsonSugestao = gson.toJson(oEventoDataEvento);
                        StringEntity postString = new StringEntity(sJsonSugestao);
                        JSONObject jsonDatasSugestao = jsonParser.makeHttpRequest(url, "POST", postString);

                        return jsonDatasSugestao;

                    case ACAO_REMOVER:
                        //url = url + "api/eventos/DetalharEvento";
                        return null;

                    default:
                       return null;                }


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

                switch (botaoAcionado) {
                    case ACAO_OBTER:
                        Gson gson = new Gson();
                        Type listType = new TypeToken<RetornoSimples<Evento>>() {
                        }.getType();
                        RetornoSimples<Evento> retornoEventos = gson.fromJson(jsonObject.toString(), listType);

                        if (retornoEventos.Ok) {
                            if (retornoEventos.Dados != null) {

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

                                //verifica organizdor do evento

                                for (int i = 0; i < oEvento.Convidados.size(); i++) {
                                    if (oEvento.Convidados.get(i).Organizador) {
                                        if (getPerfilPreferences().CelNumero == oEvento.Convidados.get(i).CelNumero) {
                                            bOrganizador = true;
                                            break;
                                        }
                                    }
                                }

                                //recarrega menu
                                invalidateOptionsMenu();

                            } else {
                                showAlert("Informação", "Evento não encontrado.");
                            }
                        } else {
                            //mensagem de erro
                            showAlert("Informação", retornoEventos.Mensagem);
                        }

                        Log.i("eventosJson - >", jsonObject.toString());

                    case ACAO_RECUSAR:
                        //
                    case ACAO_DATAADD:
                        //
                }

            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progBarHolder.setAnimation(outAnimation);
            progBarHolder.setVisibility(View.GONE);

        }
    }

}
