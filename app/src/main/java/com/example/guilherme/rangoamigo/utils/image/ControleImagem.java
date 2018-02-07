package com.example.guilherme.rangoamigo.utils.image;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Guilherme on 05/02/2018.
 */

public class ControleImagem {

    public static String encodeBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Drawable decodeBase64(String entrada, Resources res)
    {
        byte [] encodeByte= Base64.decode(entrada, Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return new BitmapDrawable(res, bitmap);
    }

}
