package com.example.guilherme.rangoamigo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Guilherme on 08/01/2018.
 */

public class MasterActivity  extends AppCompatActivity {

    /***
     *
     * @param titulo -> Título da mensagem
     * @param mensagem -> Conteúdo da mensagem
     */
    public void showConfirm(String titulo, String mensagem)
    {
        showConfirm(titulo, mensagem, null);
    }

    /***
     *
     * @param titulo -> Título da mensagem
     * @param mensagem -> Conteúdo da mensagem
     * @param callback -> Delegate que determina o que será executado no botao Sim e Nao.
     */
    public void showConfirm(String titulo, String mensagem, final ConfirmDialogCallback callback){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(callback != null) {
                            callback.callOk();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(callback != null) {
                            callback.callCancel();
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /***
     *
     * @param titulo -> Título da mensagem
     * @param mensagem -> Conteúdo da mensagem
     */
    public void showAlert(String titulo, String mensagem){
        this.showAlert(titulo, mensagem, null);
    }

    /***
     *
     * @param titulo -> Título da mensagem
     * @param mensagem -> Conteúdo da mensagem
     * @param callback -> Delegate que determina o que será executado no botao OK.
     */
    public void showAlert(String titulo, String mensagem, final AlertDialogCallback callback){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // botão ok
                        if(callback != null)
                            callback.call();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    public void executarAlertOK(){}
//    public void executarConfirmOK(){}
//    public void executarConfirmCancel(){}

    //interface para implementar o delegate com método que sera invocado no OK do alert
    public interface AlertDialogCallback
    {
        public void call();
    }

    //interface para implementar o delegate com método que sera invocado no OK do alert
    public interface ConfirmDialogCallback
    {
        public void callOk();
        public void callCancel();
    }
}
