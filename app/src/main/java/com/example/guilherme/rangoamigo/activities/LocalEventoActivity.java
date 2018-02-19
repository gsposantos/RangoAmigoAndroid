package com.example.guilherme.rangoamigo.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
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
            sLocalConsulta = sEnderecoEvento + "-" + sLocalEvento;
        } else if (!sEnderecoEvento.isEmpty()) {
            sLocalConsulta = sEnderecoEvento;
        } else if (!sLocalEvento.isEmpty()) {
            sLocalConsulta = sLocalEvento;
        }

        //Buscar a localizcao antes do cliente escolher mapa
        //GeoLocalAsyncTask ... vai ter que separar dessa classe

        this.btnLocal = (Button) findViewById(R.id.btnLocal);
        this.btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        this.btnRota = (Button) findViewById(R.id.btnRota);
        this.btnRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //desvia para mapas

                /*TODO: Consultar Localizacao lat e long*/

                Uri gmmIntentUri = Uri.parse("google.navigation:?q=" + sLocalConsulta);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (null != mapIntent.resolveActivity(getPackageManager())) {
                    startActivity(mapIntent);
                }
            }
        });

    }

    public void consultaLocalizado(String sLocalEvento) {
        try {
            Geocoder selected_place_geocoder = new Geocoder(this);
            List<Address> address;

            address = selected_place_geocoder.getFromLocationName(sLocalEvento, 5);

            if (address == null) {
                //d.dismiss();
            } else {
                Address location = address.get(0);
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            }

        } catch (Exception e) {
            e.printStackTrace();

            // Executa task para consulta

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


            //?address=" + this.place + "&sensor=false";

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
                    /*
                    //JSONObject jsonObj = new JSONObject(result.toString());
                    jsonArray = jsonObject.getJSONArray("results");

                    // Extract the Place descriptions from the results
                    // resultList = new ArrayList<String>(resultJsonArray.length());

                    JSONObject before_geometry_jsonObj = jsonArray.getJSONObject(0);

                    JSONObject geometry_jsonObj = before_geometry_jsonObj.getJSONObject("geometry");

                    JSONObject location_jsonObj = geometry_jsonObj.getJSONObject("location");

                    String lat_helper = location_jsonObj.getString("lat");
                    double lat = Double.valueOf(lat_helper);

                    String lng_helper = location_jsonObj.getString("lng");
                    double lng = Double.valueOf(lng_helper);

                    //LatLng point = new LatLng(lat, lng);
                    */

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
