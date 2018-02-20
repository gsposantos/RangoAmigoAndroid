package com.example.guilherme.rangoamigo.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.utils.connections.JSONParser;
import com.example.guilherme.rangoamigo.utils.connections.NetworkState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalEventoActivity extends MasterActivity {

    private Button btnLocal;
    private Button btnRota;
    private String sLocalConsulta;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_evento);

        Intent it = getIntent();

        String sEnderecoEvento = it.getStringExtra("EnderecoEvento");
        String sLocalEvento = it.getStringExtra("LocalEvento");

        if (!sEnderecoEvento.isEmpty() && !sLocalEvento.isEmpty()) {
            sLocalConsulta = sEnderecoEvento + " - " + sLocalEvento;
        } else if (!sEnderecoEvento.isEmpty()) {
            sLocalConsulta = sEnderecoEvento;
        } else if (!sLocalEvento.isEmpty()) {
            sLocalConsulta = sLocalEvento;
        }

        this.consultaLocalizado(sLocalConsulta);

        this.btnLocal = (Button) findViewById(R.id.btnLocal);
        this.btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + String.valueOf(latitude) + "," + String.valueOf(longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (null != mapIntent.resolveActivity(getPackageManager())) {
                    startActivity(mapIntent);
                }
            }
        });

        this.btnRota = (Button) findViewById(R.id.btnRota);
        this.btnRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //desvia para mapas
                /*TODO: Consultar Localizacao lat e long*/

                //Uri gmmIntentUri = Uri.parse("google.navigation:?q=" + sLocalConsulta);
                Uri gmmIntentUri = Uri.parse("google.navigation:?q=" + String.valueOf(latitude) + "," + String.valueOf(longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (null != mapIntent.resolveActivity(getPackageManager())) {
                    startActivity(mapIntent);
                }
            }
        });

    }

    public void consultaLocalizado(String sLocalEvento) {
        GeoLocalAsyncTask task = new GeoLocalAsyncTask();
        try {
            Geocoder selected_place_geocoder = new Geocoder(this);
            List<Address> address;

            address = selected_place_geocoder.getFromLocationName(sLocalEvento, 5);

            if (address != null && address.size()>0) {
                Address location = address.get(0);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            else {
                // Executa task para consulta
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    task.execute();
                }
            }
        } catch (Exception e) {
            // Executa task para consulta
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }
        }
    }

    /* --------------  Asynktask ----------------------- */
    class GeoLocalAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private String url = "";
        private JSONParser jsonParser = new JSONParser();

        public GeoLocalAsyncTask() {
            url = "http://maps.googleapis.com/maps/api/geocode/json";
        }

        @Override
        protected void onPreExecute() {

            if (!NetworkState.isNetworkAvaible(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                showAlert("Erro de Conexão!", "É necessário conexão com internet para executar essa operação.");
            }

//            inAnimation = new AlphaAnimation(0f, 1f);
//            inAnimation.setDuration(200);
//            progBarHolder.setAnimation(inAnimation);
//            progBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            try {

                Gson gson = new Gson();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("address", sLocalConsulta));
                params.add(new BasicNameValuePair("sensor", "false"));

                JSONObject json = jsonParser.makeHttpRequest(url, "GET", params);
                return json;
            } catch (Exception e) {
                showAlert("Erro de consulta!", "Ocorreu um erro tentando consultar os dados");
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            JSONArray jsonArray;

//            outAnimation = new AlphaAnimation(1f, 0f);
//            outAnimation.setDuration(200);
//            progBarHolder.setAnimation(outAnimation);
//            progBarHolder.setVisibility(View.GONE);

            if (jsonObject != null) {

                try {

                    longitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");

                    latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*DEBUG: Log.d
                ERROR: Log.e
                INFO: Log.i
                VERBOSE: Log.v
                WARN: Log.w*/

                Log.i("ObjetoJson - >", jsonObject.toString());
            }
        }
    }

}
