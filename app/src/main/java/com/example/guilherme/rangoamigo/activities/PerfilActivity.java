package com.example.guilherme.rangoamigo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.fragments.PinFragment;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.Retorno;
import com.example.guilherme.rangoamigo.models.RetornoSimples;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.example.guilherme.rangoamigo.utils.connections.JSONParser;
import com.example.guilherme.rangoamigo.utils.connections.NetworkState;
import com.example.guilherme.rangoamigo.utils.image.ControleImagem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerfilActivity extends MasterActivity  {

    private Toolbar toolbar;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private Button btnSalvar;
    private ImageButton imgBtPerfil;
    private FrameLayout progBarHolder;

    private TextInputLayout txtDDD;
    private TextInputLayout txtTelefone;
    private TextInputLayout txtEmail;
    private TextInputLayout txtNome;

    private boolean bAlterouImagem = false;

    private String sDDD;
    private String sTelefone;
    private String sEmail;
    private String sNome;
    private String sFoto;

    private Perfil oPerfilAux;
    private RetornoSimples<Perfil> oRetPerfilAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarPerfil);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progBarHolder = (FrameLayout) findViewById(R.id.progBarHolder);

        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sDDD = txtDDD.getEditText().getText().toString();
                sTelefone = txtTelefone.getEditText().getText().toString();
                sNome = txtNome.getEditText().getText().toString();
                sEmail = txtEmail.getEditText().getText().toString();

                BitmapDrawable drawable = (BitmapDrawable) imgBtPerfil.getBackground();
                Bitmap bitmap = drawable.getBitmap();

                if (bAlterouImagem) {
                    /*TODO: como identificar o formato de imagem??*/
                    /*TODO: como definir qualidade da imagem??*/
                    sFoto = ControleImagem.encodeBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
                }

                if(validarCampos()) {

                    PerfilActivity.PerfilAsyncTask task = new PerfilActivity.PerfilAsyncTask();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        task.execute();
                    }
                }
            }
        });

        imgBtPerfil = (ImageButton) findViewById(R.id.imgBtPerfil);
        imgBtPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentImg = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intentImg, "Selecione uma imagem"), 1234);
            }
        });

        txtDDD = (TextInputLayout) findViewById(R.id.txtDDDPerfil);
        txtTelefone = (TextInputLayout) findViewById(R.id.txtFonePerfil);
        txtNome = (TextInputLayout) findViewById(R.id.txtNome);
        txtEmail = (TextInputLayout) findViewById(R.id.txtEmail);

        /*Load dos dados se ja estiver logado*/
        if(this.verificaPerfil()){

            oPerfilAux = getPerfilPreferences();

            String dddAux = Long.toString(oPerfilAux.CelNumero).substring(0,2);
            String foneAux = Long.toString(oPerfilAux.CelNumero).substring(2);

            //preenche os campos
            txtDDD.getEditText().setText(dddAux);
            txtTelefone.getEditText().setText(foneAux);
            txtNome.getEditText().setText(oPerfilAux.Nome);
            txtEmail.getEditText().setText(oPerfilAux.Email);

            if(oPerfilAux.Foto != null)
                imgBtPerfil.setBackground(ControleImagem.decodeBase64(oPerfilAux.Foto, this.getResources()));

            //desabilita para edicao
            txtDDD.getEditText().setEnabled(false);
            txtTelefone.getEditText().setEnabled(false);
            //txtNome.getEditText().setEnabled(false);
            //txtEmail.getEditText().setEnabled(false);
        }
    }

    private Perfil getPerfilPreferences()
    {
        Gson gson = new Gson();
        Type listType = new TypeToken<RetornoSimples<Perfil>>(){}.getType();
        oRetPerfilAux = gson.fromJson(AcessoPreferences.getDadosPerfil(), listType);

        Perfil oPerfil = oRetPerfilAux.Dados;

        return oPerfil;
    }

    /**
     *
     * @return verdadeiro se há perfil na sessao e falso caso contrário
     */
    private boolean verificaPerfil(){
        return !AcessoPreferences.getDadosPerfil().isEmpty();
    }

    private boolean validarCampos(){

        boolean retorno = true;

        //Numero do Celular
        if(sDDD.length() == 0 || sTelefone.length() == 0){
            this.showAlert("Campos Obrigatórios!", "Preencha os campos DDD e Telefone adequadamente.");
            return false;
        }

        //Nome
        if(sNome.length() == 0){
            this.showAlert("Campos Obrigatórios!", "Preencha o campo Nome adequadamente.");
            return false;
        }

        //Email
        if(sEmail.length() == 0){
            this.showAlert("Campos Obrigatórios!", "Preencha o campo Email adequadamente.");
            return false;
        }
        else if(!emailValido(sEmail)){
            this.showAlert("Formato Incorreto!", "Preencha o campo Email adequadamente.");
            return false;
        }

        return retorno;
    }

    private boolean emailValido(String email)
    {
        String regExp =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence stringEntrada = email;

        Pattern pattern = Pattern.compile(regExp,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(stringEntrada);

        if(matcher.matches())
            return true;
        else
            return false;
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

    /* EVENTO RETORNO GALERIA */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED){
            if(requestCode == 1234){
                Uri imagemSelecioanda = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imagemSelecioanda);
                    imgBtPerfil.setBackground(Drawable.createFromStream(inputStream, imagemSelecioanda.toString()));
                    bAlterouImagem = true;
                } catch (FileNotFoundException e) {
                    this.showAlert("Erro!", "Ocoreu um problema para carregar a imagem "+ imagemSelecioanda.toString());
                }
            }
        }
    }

    /* --------------  Asynktask ----------------------- */
    class PerfilAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public PerfilAsyncTask(){
            this.url = getResources().getString(R.string.urlRangoAmigo);
        }

        @Override
        protected void onPreExecute() {

            if(!NetworkState.isNetworkAvaible(getSystemService(Context.CONNECTIVITY_SERVICE))){
                showAlert("Erro de Conexão!", "É necessário conexão com internet para executar essa operação.");
            }

            btnSalvar.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progBarHolder.setAnimation(inAnimation);
            progBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            Perfil oPerfil = new Perfil();

            String celNumAux = sDDD+sTelefone;
            oPerfil.CelNumero = Long.parseLong(celNumAux, 10);
            oPerfil.Nome = sNome;
            oPerfil.Email = sEmail;
            oPerfil.Foto = sFoto;

            if(verificaPerfil()) {
                url = url + "api/perfis/AlterarPerfil";
            }
            else {
                url = url + "api/perfis/CriarPerfil";
            }

            try{
                Gson gson= new Gson();
                String sJson = gson.toJson(oPerfil);
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
            btnSalvar.setEnabled(true);

            if(jsonObject != null){

                Gson gson = new Gson();
                Type listType = new TypeToken<RetornoSimples<Integer>>(){}.getType();
                RetornoSimples<Integer> retornoPerfil = gson.fromJson(jsonObject.toString(), listType);

                if(retornoPerfil.Ok){
                    if(retornoPerfil.Dados == 1){
                        String sMensagem = "";
                        if(!verificaPerfil()){
                            sMensagem = "Perfil Cadastrado. Faça login para acessar os eventos";
                        }
                        else                        {
                            sMensagem = "Perfil Alterado. Volte para acessar os eventos ";

                            /*Recarega perfil na sessao */

                            if(sEmail != null)
                                oRetPerfilAux.Dados.Email = sEmail;

                            if(sNome != null)
                                oRetPerfilAux.Dados.Nome = sNome;

                            if(sFoto != null)
                                oRetPerfilAux.Dados.Foto = sFoto;

                            Gson gsonAux= new Gson();
                            String sJson = gsonAux.toJson(oRetPerfilAux);
                            AcessoPreferences.setDadosPerfil(sJson);
                        }

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
