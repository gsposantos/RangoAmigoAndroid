package com.example.guilherme.rangoamigo.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.models.Contato;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.utils.image.ControleImagem;
import com.example.guilherme.rangoamigo.viewholders.ContatoViewHolder;
import com.example.guilherme.rangoamigo.viewholders.VazioViewHolder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Guilherme on 11/02/2018.
 */

public class ContatoAdapter extends RecyclerView.Adapter {

    private static final int VIEW_CONTATOS = 0;
    private static final int VIEW_VAZIO = 1;

    private Context context;
    private ArrayList<Contato> contatos;
    private int msgVazio = 0;


    public ContatoAdapter(ArrayList<Contato> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    public void setContatos(ArrayList<Contato> contatos) {

        this.contatos = contatos;
    }

    public void setMsgVazio(int msgVazio) {
        this.msgVazio = msgVazio;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(viewType == VIEW_CONTATOS) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contato, parent, false);
            ContatoViewHolder holder = new ContatoViewHolder(view);
            return holder;
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vazio, parent, false);
            VazioViewHolder holderVazio  = new VazioViewHolder(view);
            return holderVazio;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        int tipo = getItemViewType(position);

        if(tipo == VIEW_CONTATOS) {

            ContatoViewHolder contatoHolder = (ContatoViewHolder) viewHolder;
            Contato contato = contatos.get(position);

            if (contato.fotoUri != null) {
                try {
                    Uri uri = Uri.parse(contato.fotoUri);
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    contatoHolder.imgContato.setImageDrawable(Drawable.createFromStream(inputStream, uri.toString()));
                } catch (FileNotFoundException e) {
                    //contatoHolder.imgContato.setImageDrawable(Drawable.createFromStream(inputStream, uri.toString()));
                }
            }else if(contato.foto64 != null )
            {
                contatoHolder.imgContato.setImageDrawable(ControleImagem.decodeBase64(contato.foto64, context.getResources()));
            }
            else {
                //tenta resolver aparente problema de cache de imagens
                contatoHolder.imgContato.setImageResource(R.drawable.icon_perfil);
            }

            contatoHolder.nomeContato.setText(contato.nome);
            contatoHolder.foneContato.setText(contato.numeros.get(0).fone);

            if(contato.emails.size()>0)
                contatoHolder.emailContato.setText(contato.emails.get(0).endereco);
            else
                contatoHolder.emailContato.setText(R.string.contato_sem_email);

            if(!contato.cadastrado){
                contatoHolder.context = context;
                contatoHolder.destaque = true;
                contatoHolder.contatoCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.secondaryLightColorAlfa));
            }
        }
        else if(tipo == VIEW_VAZIO){
            VazioViewHolder vazioHolder = (VazioViewHolder) viewHolder;

            if(this.msgVazio > 0)
                vazioHolder.textoVazio.setText(this.msgVazio);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (contatos.size() == 0) {
            return VIEW_VAZIO;
        }else{
            return VIEW_CONTATOS;
        }
    }

    @Override
    public int getItemCount() {
        if(contatos.size() == 0){
            return 1;
        }else {
            return contatos.size();
        }
    }
}
