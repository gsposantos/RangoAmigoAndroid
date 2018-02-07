package com.example.guilherme.rangoamigo.utils.connections;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;

import java.util.Objects;

/**
 * Created by eafdecision9 on 07/10/17.
 */

public class NetworkState {

    public static boolean isNetworkAvaible(Object systemService)
    {
        ConnectivityManager connectivityManager =  (ConnectivityManager) systemService;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }
}
