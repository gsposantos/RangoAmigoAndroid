package com.example.guilherme.rangoamigo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.DataEventoAdapter;
import com.example.guilherme.rangoamigo.models.Convidado;
import com.example.guilherme.rangoamigo.models.DataEvento;
import com.example.guilherme.rangoamigo.models.DataEventoVoto;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.RetornoSimples;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.example.guilherme.rangoamigo.utils.connections.JSONParser;
import com.example.guilherme.rangoamigo.utils.connections.NetworkState;
import com.example.guilherme.rangoamigo.utils.layout.RecyclerItemClickListener;
import com.example.guilherme.rangoamigo.viewholders.DataEventoViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DatasEventActivity extends MasterActivity {

    private RecyclerView recyclerView;
    private DataEventoAdapter dataEventoAdapter;
    private Evento oEventoDataVoto;
    private Evento oEvento;
    private ArrayList<DataEvento> listaDatas;
    private Perfil oPerfilAux;
    private Button btnGravar;
    private FrameLayout progBarHolder;
    private AlphaAnimation outAnimation;
    private AlphaAnimation inAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datas_event);

        AcessoPreferences.setContext(this);
        String jsonEvento = AcessoPreferences.getDadosEvento();

        Gson gson = new Gson();
        Type listType = new TypeToken<Evento>() {}.getType();
        oEvento = gson.fromJson(jsonEvento, listType);

        oPerfilAux = getPerfilPreferences();

        oEventoDataVoto = new Evento();
        oEventoDataVoto.CodEvento = oEvento.CodEvento;
        oEventoDataVoto.Datas = new ArrayList<DataEvento>();

        DataEvento oDataEventoAux;
        DataEventoVoto oDtVoto;

        //recupera apenas as datas para adicionar a participacao depois
        for(int i = 0 ; i < oEvento.Datas.size() ; i++){

            oDataEventoAux = new DataEvento();
            oDataEventoAux.DiaEvento = oEvento.Datas.get(i).DiaEvento;
            oDataEventoAux.Original = oEvento.Datas.get(i).Original;

            oDataEventoAux.Participacao = new ArrayList<DataEventoVoto>();

            oDtVoto = new DataEventoVoto();
            oDtVoto.CelNumero = oPerfilAux.CelNumero;
            oDtVoto.Voto = false;

            oDataEventoAux.Participacao.add(oDtVoto);

            oEventoDataVoto.Datas.add(oDataEventoAux);

        }

        recyclerView = (RecyclerView) findViewById(R.id.recViewDatas);

        if(oEvento.Datas.size() > 0) {

            listaDatas = oEvento.Datas;
            dataEventoAdapter = new DataEventoAdapter(this, listaDatas);
            recyclerView.setAdapter(dataEventoAdapter);

        }

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        //RecyclerItemClickListener
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                DataEventoVoto oDtVoto;

                //tenho o voto da posicao que foi clicada
                DataEventoViewHolder viewHolder = (DataEventoViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

                //Tenho a posicao da data clicada para marcar a participacao.
                if(oEventoDataVoto.Datas.get(position).Participacao == null){
                    oEventoDataVoto.Datas.get(position).Participacao = new ArrayList<DataEventoVoto>();

                    oDtVoto = new DataEventoVoto();
                    oDtVoto.CelNumero = oPerfilAux.CelNumero;
                    oDtVoto.Voto = !viewHolder.chkVoto.isChecked();

                    oEventoDataVoto.Datas.get(position).Participacao.add(oDtVoto);
                }
                else if(oEventoDataVoto.Datas.get(position).Participacao.size() > 0) {

                    //Apenas uma participacao por data.
                    oEventoDataVoto.Datas.get(position).Participacao.get(0).CelNumero = oPerfilAux.CelNumero;
                    oEventoDataVoto.Datas.get(position).Participacao.get(0).Voto = !viewHolder.chkVoto.isChecked();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        }));

        progBarHolder = (FrameLayout) findViewById(R.id.progBarHolder);

        btnGravar = (Button) findViewById(R.id.btnGravar);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VotoEventoAsyncTask task = new VotoEventoAsyncTask();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    task.execute();
                }
            }
        });

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
    class VotoEventoAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public VotoEventoAsyncTask(){
            this.url = getResources().getString(R.string.urlRangoAmigo);
        }

        @Override
        protected void onPreExecute() {

            if(!NetworkState.isNetworkAvaible(getSystemService(Context.CONNECTIVITY_SERVICE))){
                showAlert("Erro de Conexão!", "É necessário conexão com internet para executar essa operação.");
            }

            btnGravar.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progBarHolder.setAnimation(inAnimation);
            progBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {


            url = url + "api/eventos/VotarDataEvento";

            try{
                Gson gson= new Gson();
                String sJson = gson.toJson(oEventoDataVoto);
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
            btnGravar.setEnabled(true);

            if(jsonObject != null){

                Gson gson = new Gson();
                Type listType = new TypeToken<RetornoSimples<Integer>>(){}.getType();
                RetornoSimples<Integer> retornoPerfil = gson.fromJson(jsonObject.toString(), listType);

                if(retornoPerfil.Ok){
                    if(retornoPerfil.Dados > 0){

                        String sMensagem = "Disponibilidade atualizada. Volte para continuar acompanhando o evento.";

                        showAlert("Sucesso!", sMensagem, new MasterActivity.AlertDialogCallback() {
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

                /*DEBUG: Log.d
                ERROR: Log.e
                INFO: Log.i
                VERBOSE: Log.v
                WARN: Log.w*/

                Log.i("ObjetoJson - >",jsonObject.toString());
            }
        }
    }
}
