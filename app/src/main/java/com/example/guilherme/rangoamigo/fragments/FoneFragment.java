package com.example.guilherme.rangoamigo.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.AcessoActivity;
import com.example.guilherme.rangoamigo.activities.MasterActivity;
import com.example.guilherme.rangoamigo.activities.PerfilActivity;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.Retorno;
import com.example.guilherme.rangoamigo.models.RetornoSimples;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FoneFragment extends Fragment {

    private View view;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private Button btn;
    private FrameLayout progBarHolder;

    private String sDDD;
    private String sTelefone;


    public FoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fone, container, false);

        progBarHolder = (FrameLayout) view.findViewById(R.id.progBarHolder);
        btn = (Button) view.findViewById(R.id.btnAvancar);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextInputLayout txtDDD = (TextInputLayout) view.findViewById(R.id.txtDDD);
                TextInputLayout txtTelefone = (TextInputLayout) view.findViewById(R.id.txtTelefone);

                sDDD = txtDDD.getEditText().getText().toString();
                sTelefone = txtTelefone.getEditText().getText().toString();

                if(validarCampos()) {

                    FoneFragment.LoginAsyncTask task = new FoneFragment.LoginAsyncTask();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        task.execute();
                    }
                }
            }
        });

        Button btnNovo = (Button) view.findViewById(R.id.btnNovo);
        btnNovo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*TODO: Cosiderar passar parametros de telefone*/

                //redireciona para tela de cadastro
                Intent intent = new Intent(getActivity(), PerfilActivity.class);
                intent.putExtra("modoTela", "NOVO");
                startActivity(intent);
            }
        });

        return view;
    }

    private boolean validarCampos(){
        if(sDDD.length() == 0 || sTelefone.length() == 0){
            ((MasterActivity)getActivity()).showAlert("Campos Obrigatórios!", "Preencha os campos DDD e Telefone adequadamente.");
            return false;
        }
        return true;
    }


    /* --------------  Asynktask ----------------------- */
    class LoginAsyncTask extends AsyncTask<Void, Void, JSONObject>{

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public LoginAsyncTask(){
            this.url = getResources().getString(R.string.urlRangoAmigo);
        }

        @Override
        protected void onPreExecute() {

            if(!NetworkState.isNetworkAvaible(getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))){
                ((MasterActivity)getActivity()).showAlert("Erro de Conexão!", "É necessário conexão com internet para executar essa operação.");
            }

            btn.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progBarHolder.setAnimation(inAnimation);
            progBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            //List<Perfil> params = new ArrayList<Perfil>();
            Perfil perfil = new Perfil();

            String celNumAux = sDDD+sTelefone;
            perfil.CelNumero = Long.parseLong(celNumAux, 10);
            //params.add(perfil);

            url = url + "api/perfis/ObterPerfil";
            //url = url + "api/perfis/SincronizarContatos";

            try{
                Gson gson= new Gson();
                String sJson = gson.toJson(perfil);
                StringEntity postingString = new StringEntity(sJson); //convert to json
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", postingString);
                return json;
            }
            catch (Exception e)
            {
                ((MasterActivity)getActivity()).showAlert("Erro de consulta!", "Ocorreu um erro tentando consultar os dados");
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
            btn.setEnabled(true);

            if(jsonObject != null){

//                Gson gson = new Gson();
//                Retorno retornoPerfil = gson.fromJson(jsonObject.toString(), Retorno.class);

                Gson gson = new Gson();
                Type listType = new TypeToken<RetornoSimples<Perfil>>(){}.getType();
                RetornoSimples<Perfil> retornoPerfil = gson.fromJson(jsonObject.toString(), listType);

                if(retornoPerfil.Ok){

                    //chamada que faz o replace do fragment
                    //if(!((ArrayList)retornoPerfil.Dados).isEmpty()) {
                    if(retornoPerfil.Dados != null){

                        PinFragment fragment = new PinFragment();

                        Bundle arguments = new Bundle();
                        arguments.putString("jsonPerfil", jsonObject.toString());;
                        fragment.setArguments(arguments);

                        /*Faz replace no frame raiz redirecionando para fragmento de pin*/
                        FragmentTransaction trans = getFragmentManager()
                                .beginTransaction();

                        trans.replace(R.id.acesso_frame, fragment);

                        /*adiciona o fragment atual na pilha e permite retornar usando botao back*/
                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        trans.addToBackStack(null);


                        trans.commit();

//                        Intent intent = new Intent(LoginActivity.this, AcessoActivity.class);
//                        intent.putExtra("jsonPerfil", jsonObject.toString());
//                        startActivity(intent);
//                        finish();
                    }
                    else{
                        ((MasterActivity)getActivity()).showAlert("Informação", "Perfil não cadastrado.");
                    }
                }
                else
                {
                    //mensagem de erro
                    ((MasterActivity)getActivity()).showAlert("Informação", retornoPerfil.Mensagem);
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
