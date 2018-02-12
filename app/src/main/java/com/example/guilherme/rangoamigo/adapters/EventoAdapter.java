package com.example.guilherme.rangoamigo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.utils.image.ControleImagem;
import com.example.guilherme.rangoamigo.viewholders.EventoViewHolder;

import java.util.ArrayList;

/**
 * Created by Guilherme on 04/02/2018.
 */

public class EventoAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Evento> eventos;
    

    public EventoAdapter(ArrayList<Evento> eventos, Context context) {
        this.eventos = eventos;
        this.context = context;
    }

    public void setEventos(ArrayList<Evento> eventos) {
        this.eventos = eventos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
        EventoViewHolder holder = new EventoViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {

        EventoViewHolder holder = (EventoViewHolder) viewHolder;
        Evento evento =  eventos.get(position);

        holder.imgEvento.setImageDrawable(ControleImagem.decodeBase64(evento.Imagem, context.getResources()));

        holder.codEvento = evento.CodEvento;
        holder.nomeEvento.setText(evento.NomeEvento);
        holder.dataEvento.setText(evento.Datas.get(0).DiaEventoFormatado);
        holder.enderecoEvento.setText(evento.Endereco);
        holder.localEvento.setText(evento.NomeLocal);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

}
