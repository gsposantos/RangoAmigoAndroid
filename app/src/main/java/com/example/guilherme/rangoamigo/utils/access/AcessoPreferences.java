package com.example.guilherme.rangoamigo.utils.access;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Guilherme on 05/01/2018.
 */

public class AcessoPreferences {

    private static final String PREFS_NAME = "preferece";
    private static Context mContext;

    public static void setContext(Context context){
        AcessoPreferences.mContext = context;
    }

    public static String getDadosPerfil(){
        SharedPreferences preferences = AcessoPreferences.mContext.getSharedPreferences(PREFS_NAME, 0);
        return preferences.getString("perfil_json", "");
    }

    public static void setDadosPerfil(String json)
    {
        SharedPreferences.Editor editor = AcessoPreferences.mContext.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString("perfil_json", json);
        editor.commit();
    }

    public static String getDadosEvento(){
        SharedPreferences preferences = AcessoPreferences.mContext.getSharedPreferences(PREFS_NAME, 0);
        return preferences.getString("evento_json", "");
    }

    public static void setDadosEvento(String json)
    {
        SharedPreferences.Editor editor = AcessoPreferences.mContext.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString("evento_json", json);
        editor.commit();
    }

    public static String getDadosContatos(){
        SharedPreferences preferences = AcessoPreferences.mContext.getSharedPreferences(PREFS_NAME, 0);
        return preferences.getString("contatos_json", "");
    }

    public static void setDadosContatos(String json)
    {
        SharedPreferences.Editor editor = AcessoPreferences.mContext.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString("contatos_json", json);
        editor.commit();
    }
}
