package com.example.guilherme.rangoamigo.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.EventoActivity;
import com.example.guilherme.rangoamigo.adapters.ContatoAdapter;
import com.example.guilherme.rangoamigo.adapters.EventoAdapter;
import com.example.guilherme.rangoamigo.models.Contato;
import com.example.guilherme.rangoamigo.models.ContatoEmail;
import com.example.guilherme.rangoamigo.models.ContatoFone;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.RetornoLista;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.example.guilherme.rangoamigo.utils.access.PesquisaContato;
import com.example.guilherme.rangoamigo.utils.connections.JSONParser;
import com.example.guilherme.rangoamigo.utils.connections.NetworkState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatoFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private View view;
    private RecyclerView recyclerView;
    private FrameLayout progBarHolder;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;

    private ArrayList<Contato> listaContatos;
    private ContatoAdapter contatoAdapter;

    public ContatoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contato, container, false);

        progBarHolder = (FrameLayout) view.findViewById(R.id.progBarHolder);
        recyclerView = (RecyclerView) view.findViewById(R.id.recViewContatos);
        listaContatos = new ArrayList<Contato>();
        contatoAdapter = new ContatoAdapter(listaContatos, view.getContext());
        contatoAdapter.setMsgVazio(R.string.convite_sem_registros);
        recyclerView.setAdapter(contatoAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        AcessoPreferences.setContext(this.getContext());

        if(AcessoPreferences.getDadosContatos().isEmpty()){
            //precisa carregar os contatos pela primeira vez
            this.buscaContaotosDipositivo();
            this.carregaContatos();
        }
        else{
            //carrega de preferences para sincronizar depois
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Contato>>(){}.getType();
            listaContatos = new Gson().fromJson(AcessoPreferences.getDadosContatos(), listType);
            atualizaLista(listaContatos);
        }
        return view;
    }

    public void carregaContatos(){

        ContatosAsyncTask task = new ContatosAsyncTask();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else{
            task.execute();
        }
    }

    private void buscaContaotosDipositivo()
    {
        // Verifica SDK e se ja foi dado permissao
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Versao amdroid anterior a 6.0 ou ja dado permissao -> carregar os contatos para sincronizar depois.
            listaContatos = new PesquisaContato(getContext()).pesquisarTodos();
        }
    }

    private void atualizaLista(ArrayList<Contato> lista){
        this.contatoAdapter.setContatos(lista);
        this.contatoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                buscaContaotosDipositivo();
            } else {

                ((EventoActivity)getActivity()).apresentaMensagem("Necessário Permissão!",
                        "Conceda permissão para listar os contatos.");
            }
        }
    }

    /* --------------  Asynktask ----------------------- */

    class ContatosAsyncTask extends AsyncTask<Void, Void, JSONObject>{

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public ContatosAsyncTask(){
            this.url = getResources().getString(R.string.urlRangoAmigo);
        }

        @Override
        protected void onPreExecute() {
            if(!NetworkState.isNetworkAvaible(getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))){
                ((EventoActivity)getActivity()).apresentaMensagem("Erro de Conexão!", "É necessário conexão com internet para executar essa operação.");
            }

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progBarHolder.setAnimation(inAnimation);
            progBarHolder.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {


            List<Perfil> params = new ArrayList<Perfil>();
            Perfil perfil = new Perfil();
            String sNumAux = "";

            //parse para pegar apenas números
            Pattern pattern = Pattern.compile("[^\\d]", Pattern.CASE_INSENSITIVE);
            Matcher matcher;

            for(int i = 0; i < listaContatos.size(); i++) {
                for(int j = 0; j < listaContatos.get(i).numeros.size(); j++) {
                    sNumAux = listaContatos.get(i).numeros.get(j).fone;
                    matcher = pattern.matcher(sNumAux);
                    sNumAux = matcher.replaceAll("");

                    if (sNumAux.length() > 11)
                        sNumAux = sNumAux.substring(sNumAux.length() - 11);

                    perfil = new Perfil();
                    listaContatos.get(i).numeros.get(j).numero = Long.parseLong(sNumAux);
                    perfil.CelNumero = listaContatos.get(i).numeros.get(j).numero;

                    params.add(perfil);
                }
            }

            url = url + "api/perfis/SincronizarContatos";

            try{
                Gson gson= new Gson();
                String testejson = gson.toJson(params);
                StringEntity postingString = new StringEntity(testejson); //convert to json
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", postingString);
                return json;
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

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progBarHolder.setAnimation(outAnimation);
            progBarHolder.setVisibility(View.GONE);

            if(jsonObject != null){

                Gson gson = new Gson();
                Type listType = new TypeToken<RetornoLista<Perfil>>(){}.getType();
                RetornoLista<Perfil> retornoContatos = new Gson().fromJson(jsonObject.toString(), listType);

                boolean bProxContato;
                ArrayList<Contato> contatosSessao = new ArrayList<Contato>();
                Contato contatoAux;
                ContatoFone oFone;
                ContatoEmail oEmail;

                if(retornoContatos.Ok) {
                    /* Cruza dados contatos e perfis */
                    for (int i = 0; i < listaContatos.size(); i++) {
                        bProxContato = false;
                        for (int j = 0; j < listaContatos.get(i).numeros.size(); j++) {
                            for(int l = 0; l < retornoContatos.Dados.size(); l++){
                                if(listaContatos.get(i).numeros.get(j).numero == retornoContatos.Dados.get(l).CelNumero) {

                                    //limpa os telefones e adiciona o telefoen encontrado.
                                    oFone = new ContatoFone(listaContatos.get(i).numeros.get(j).fone, listaContatos.get(i).numeros.get(j).tipo);
                                    oFone.numero = listaContatos.get(i).numeros.get(j).numero;
                                    listaContatos.get(i).numeros.clear();
                                    listaContatos.get(i).numeros.add(oFone);
                                    listaContatos.get(i).cadastrado = true;

                                    //pega o email cadastrado se nao tiver email no contato.
                                    if(listaContatos.get(i).emails.size()==0){
                                        oEmail = new ContatoEmail(retornoContatos.Dados.get(l).Email, "");
                                        listaContatos.get(i).emails.add(oEmail);
                                    }

                                    //pega o foto cadastrada se nao tiver imagem no contato.
                                    if(listaContatos.get(i).foto == null && !retornoContatos.Dados.get(l).Foto.isEmpty()){
                                        listaContatos.get(i).foto = retornoContatos.Dados.get(l).Foto;
                                    }

                                    //passa para o próximo contato
                                    bProxContato = true;
                                    break;
                                }
                            }
                            if(bProxContato) break;
                        }
                    }
                }

                //Converter para string json e salvar em preferences
                String jsonContatos = gson.toJson(listaContatos);
                AcessoPreferences.setDadosContatos(jsonContatos);

                atualizaLista(listaContatos);

                Log.i("RetornoContatos - >",jsonObject.toString());
                Log.i("Sincronizados - >", jsonContatos);

            }
        }
    }

}
