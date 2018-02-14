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
import com.example.guilherme.rangoamigo.viewholders.VazioViewHolder;

import java.util.ArrayList;

/**
 * Created by Guilherme on 04/02/2018.
 */

public class EventoAdapter extends RecyclerView.Adapter {

    private static final int VIEW_EVENTOS = 0;
    private static final int VIEW_VAZIO = 1;

    private Context context;
    private ArrayList<Evento> eventos;
    private int msgVazio;
    

    public EventoAdapter(ArrayList<Evento> eventos, Context context) {
        this.eventos = eventos;
        this.context = context;
    }

    public void setMsgVazio(int msgVazio) {
        this.msgVazio = msgVazio;
    }

    public void setEventos(ArrayList<Evento> eventos) {
        this.eventos = eventos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(viewType == VIEW_EVENTOS) {
            view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
            EventoViewHolder holder = new EventoViewHolder(view);
            return holder;
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vazio, parent, false);
            VazioViewHolder holderVazio  = new VazioViewHolder(view);
            return holderVazio;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {

        int tipo = getItemViewType(position);

        if(tipo == VIEW_EVENTOS) {

            EventoViewHolder holder = (EventoViewHolder) viewHolder;
            Evento evento = eventos.get(position);

            holder.imgEvento.setImageDrawable(ControleImagem.decodeBase64(evento.Imagem, context.getResources()));

            holder.codEvento = evento.CodEvento;
            holder.nomeEvento.setText(evento.NomeEvento);
            holder.dataEvento.setText(evento.Datas.get(0).DiaEventoFormatado);
            holder.enderecoEvento.setText(evento.Endereco);
            holder.localEvento.setText(evento.NomeLocal);
        }
        else if(tipo == VIEW_VAZIO){
            VazioViewHolder vazioHolder = (VazioViewHolder) viewHolder;
            vazioHolder.textoVazio.setText(this.msgVazio);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (eventos.size() == 0) {
            return VIEW_VAZIO;
        }else{
            return VIEW_EVENTOS;
        }
    }

    @Override
    public int getItemCount() {
        if(eventos.size() == 0){
            return 1;
        }else {
            return eventos.size();
        }
    }

}
