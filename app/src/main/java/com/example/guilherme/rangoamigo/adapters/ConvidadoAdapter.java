package com.example.guilherme.rangoamigo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.models.Convidado;
import com.example.guilherme.rangoamigo.utils.image.ControleImagem;
import com.example.guilherme.rangoamigo.viewholders.ContatoViewHolder;
import com.example.guilherme.rangoamigo.viewholders.VazioViewHolder;

import java.util.ArrayList;

/**
 * Created by Guilherme on 18/02/2018.
 */

public class ConvidadoAdapter extends RecyclerView.Adapter {

    private static final int VIEW_CONTATOS = 0;
    private static final int VIEW_VAZIO = 1;

    private Context context;
    private ArrayList<Convidado> convidados;
    private int msgVazio = 0;

    public ConvidadoAdapter(Context context, ArrayList<Convidado> convidados) {
        this.context = context;
        this.convidados = convidados;
    }

    public void setConvidados(ArrayList<Convidado> convidados) {

        this.convidados = convidados;
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
            Convidado oConvidado = convidados.get(position);

            contatoHolder.nomeContato.setText(oConvidado.Nome);
            if(oConvidado.Foto != null ){
                contatoHolder.imgContato.setImageDrawable(ControleImagem.decodeBase64(oConvidado.Foto, context.getResources()));
            }
            else{
                //tenta resolver aparente problema de cache de imagens
                contatoHolder.imgContato.setImageResource(R.drawable.icon_perfil);
            }

            if(oConvidado.CelNumero > 0){
                contatoHolder.foneContato.setText(String.valueOf(oConvidado.CelNumero));
            }
            else{
                contatoHolder.foneContato.setText("");
            }

            if(!oConvidado.Email.isEmpty()){
                contatoHolder.emailContato.setText(oConvidado.Email);
            }
            else{
                contatoHolder.foneContato.setText("");
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
        if (convidados.size() == 0) {
            return VIEW_VAZIO;
        }else{
            return VIEW_CONTATOS;
        }
    }

    @Override
    public int getItemCount() {
        if(convidados.size() == 0){
            return 1;
        }else {
            return convidados.size();
        }
    }
}
