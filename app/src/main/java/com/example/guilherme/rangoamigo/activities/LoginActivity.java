package com.example.guilherme.rangoamigo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.Retorno;
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
import java.util.List;

public class LoginActivity extends MasterActivity {

    private String sDDD;
    private String sTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void avancarLogin(View v){
        TextInputLayout txtDDD = (TextInputLayout) findViewById(R.id.txtDDD);
        TextInputLayout txtTelefone = (TextInputLayout) findViewById(R.id.txtTelefone);

        this.sDDD = txtDDD.getEditText().getText().toString();
        this.sTelefone = txtTelefone.getEditText().getText().toString();

        if(this.validarCampos()) {

            TarefasAsyncTask task = new TarefasAsyncTask();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }
        }
    }

    private boolean validarCampos(){
        if(sDDD.length() == 0 || sTelefone.length() == 0){
            this.showAlert("Campos Obrigatórios!", "Preencha os campos DDD e Telefone adequadamente.");
            return false;
        }
        return true;
    }

    private void salvarPerfil(String jsonPerfil){
        AcessoPreferences.setContext(this);
        AcessoPreferences.setDadosPerfil(jsonPerfil);
    }


    public void avancarCadastro(View v){

        //pega os dados caso tenha preenchido
        TextInputLayout txtDDD = (TextInputLayout) findViewById(R.id.txtDDD);
        TextInputLayout txtTelefone = (TextInputLayout) findViewById(R.id.txtTelefone);

        //redireciona para tela de cadastro
        Intent intent = new Intent(LoginActivity.this, PerfilActivity.class);
        intent.putExtra("modoTela", "NOVO");

        String sDDD = txtDDD.getEditText().getText().toString();
        String sTelefone = txtTelefone.getEditText().getText().toString();

        if(!sDDD.isEmpty())
            intent.putExtra("txtDDD", sDDD);

        if(!sTelefone.isEmpty())
            intent.putExtra("txtTelefone", sTelefone);

        startActivity(intent);
        finish();
    }


    /* --------------  Asynktask ----------------------- */

    class TarefasAsyncTask extends AsyncTask<Void, Void, JSONObject>{

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public TarefasAsyncTask(){
            this.url = getResources().getString(R.string.urlRangoAmigo);
        }

        @Override
        protected void onPreExecute() {
            if(!NetworkState.isNetworkAvaible(getSystemService(Context.CONNECTIVITY_SERVICE))){
                showAlert("Erro de Conexão!", "É necessário conexão com internet para executar essa operação");
            }
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            List<Perfil> params = new ArrayList<Perfil>();
            Perfil perfil = new Perfil();

            String celNumAux = sDDD+sTelefone;
            perfil.CelNumero = Long.parseLong(celNumAux, 10);
            params.add(perfil);

            //url = url + "api/perfis/ObterPerfil";
            url = url + "api/perfis/SincronizarContatos";

            try{
                Gson gson= new Gson();
                String sJson = gson.toJson(params);
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

            if(jsonObject != null){

                Gson gson = new Gson();
                RetornoSimples retornoPerfil = gson.fromJson(jsonObject.toString(), RetornoSimples.class);

                if(retornoPerfil.Ok){
                    if(!((ArrayList)retornoPerfil.Dados).isEmpty()) {

                        //redireciona para tela de acesso
                        Intent intent = new Intent(LoginActivity.this, AcessoActivity.class);
                        intent.putExtra("jsonPerfil", jsonObject.toString());
                        startActivity(intent);
                        finish();
                    }
                    else{
                        showAlert("Informação", "Perfil não cadastrado.");
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
