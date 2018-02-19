package com.example.guilherme.rangoamigo.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.AcessoActivity;
import com.example.guilherme.rangoamigo.activities.EventoActivity;
import com.example.guilherme.rangoamigo.activities.MasterActivity;
import com.example.guilherme.rangoamigo.adapters.EventoAdapter;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.RetornoLista;
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
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConviteFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Evento> listaEventos;
    private EventoAdapter eventoAdapter;
    private FrameLayout progBarHolder;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;

    public ConviteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_convite, container, false);

        progBarHolder = (FrameLayout) view.findViewById(R.id.progBarHolder);

        recyclerView = (RecyclerView) view.findViewById(R.id.recView);
        listaEventos = new ArrayList<Evento>();
        eventoAdapter = new EventoAdapter(listaEventos, view.getContext());

        recyclerView.setAdapter(eventoAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

       this.carregaConvites();

        return view;
    }

    public void carregaConvites(){

        ConviteAsyncTask task = new ConviteAsyncTask();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    private void atualizaLista(ArrayList<Evento> lista){
        this.eventoAdapter.setMsgVazio(R.string.convite_sem_registros);
        this.eventoAdapter.setEventos(lista);
        this.eventoAdapter.notifyDataSetChanged();
    }

    /* --------------  Asynktask ----------------------- */
    class ConviteAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public ConviteAsyncTask(){
            this.url = getResources().getString(R.string.urlRangoAmigo);
        }

        @Override
        protected void onPreExecute() {

            if(!NetworkState.isNetworkAvaible(getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))){
                ((EventoActivity)getActivity()).apresentaMensagem("Erro de Conexão!", "É necessário conexão com internet para executar essa operação.");
            }

            //btn.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progBarHolder.setAnimation(inAnimation);
            progBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            url = url + "api/eventos/ListarConvites";

            try{
                //Gson gson = new Gson();
                //verifica se o usuario não limpou os dados do APP
                String sJson = AcessoPreferences.getDadosPerfil();
                if(!sJson.isEmpty()) {

                    Gson gson = new Gson();
                    Type listType = new TypeToken<RetornoSimples<Perfil>>(){}.getType();
                    RetornoSimples<Perfil> retornoPerfil = gson.fromJson(sJson, listType);

                    Perfil oPerfil = retornoPerfil.Dados;
                    sJson = gson.toJson(oPerfil);

                    StringEntity postingString = new StringEntity(sJson); //convert to json
                    JSONObject json = jsonParser.makeHttpRequest(url, "POST", postingString);
                    return json;
                }
                else
                {
                    //desvia para login
                    Intent intent;
                    intent = new Intent(getActivity(), AcessoActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
            catch (Exception e)
            {
                ((EventoActivity)getActivity()).apresentaMensagem("Erro de consulta!", "Ocorreu um erro tentando consultar os dados");
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            JSONArray jsonArray;

            if(jsonObject != null){

                Gson gson = new Gson();
                Type listType = new TypeToken<RetornoLista<Evento>>(){}.getType();
                RetornoLista<Evento> retornoEventos = gson.fromJson(jsonObject.toString(), listType);

                if(retornoEventos.Ok){
                    if(retornoEventos.Dados != null){

                        /*TODO: Testar se tem registros e mostrar mensagem*/
                        if(retornoEventos.Dados.size() > 0) {
                            atualizaLista(retornoEventos.Dados);
                        }
                        else {
                            atualizaLista(new ArrayList<Evento>());
                        }
                    }
                    else{
                        ((EventoActivity)getActivity()).apresentaMensagem("Informação", "Eventos não encontrados.");
                    }
                }
                else
                {
                    ((EventoActivity)getActivity()).apresentaMensagem("Informação", retornoEventos.Mensagem);
                }

                Log.i("ObjetoJson - >",jsonObject.toString());
            }
            else
            {
                atualizaLista(new ArrayList<Evento>());
                ((EventoActivity)getActivity()).apresentaMensagem("Erro", "Dados não encotrados.");
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progBarHolder.setAnimation(outAnimation);
            progBarHolder.setVisibility(View.GONE);
            //btn.setEnabled(true);
        }
    }

}
