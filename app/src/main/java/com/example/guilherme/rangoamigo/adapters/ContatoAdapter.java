package com.example.guilherme.rangoamigo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.models.Contato;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.utils.image.ControleImagem;
import com.example.guilherme.rangoamigo.viewholders.ContatoViewHolder;

import java.util.ArrayList;

/**
 * Created by Guilherme on 11/02/2018.
 */

public class ContatoAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Contato> contatos;


    public ContatoAdapter(ArrayList<Contato> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    public void setContatos(ArrayList<Contato> contatos) {
        this.contatos = contatos;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_contato, parent, false);
        ContatoViewHolder holder = new ContatoViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ContatoViewHolder contatoHolder = (ContatoViewHolder) viewHolder;
        Contato contato = contatos.get(position);

        /*TODO: Pegar a imagem do contato*/
        //contatoHolder.imgContato.setImageDrawable(ControleImagem.decodeBase64(contato. .Imagem, context.getResources()));

        contatoHolder.nomeContato.setText(contato.nome);
        //mais campos

    }

    @Override
    public int getItemCount() { return contatos.size(); }
}
