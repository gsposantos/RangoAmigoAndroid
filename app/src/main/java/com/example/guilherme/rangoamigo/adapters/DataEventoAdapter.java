package com.example.guilherme.rangoamigo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.models.DataEvento;
import com.example.guilherme.rangoamigo.viewholders.DataEventoViewHolder;
import com.example.guilherme.rangoamigo.viewholders.VazioViewHolder;

import java.util.ArrayList;

/**
 * Created by Guilherme on 23/02/2018.
 */

public class DataEventoAdapter extends RecyclerView.Adapter {

    private static final int VIEW_CONTATOS = 0;
    private static final int VIEW_VAZIO = 1;

    private Context context;
    private ArrayList<DataEvento> listaDatas;
    private int msgVazio = 0;

    public DataEventoAdapter(Context context, ArrayList<DataEvento> listaDatas) {
        this.context = context;
        this.listaDatas = listaDatas;
    }

//    public void setDatasEvento(ArrayList<DataEvento> listaDatas) {
//
//        this.listaDatas = listaDatas;
//    }

    public void setMsgVazio(int msgVazio) {
        this.msgVazio = msgVazio;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_CONTATOS) {
            view = LayoutInflater.from(context).inflate(R.layout.item_data_evento, parent, false);
            DataEventoViewHolder holder = new DataEventoViewHolder(view);
            return holder;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vazio, parent, false);
            VazioViewHolder holderVazio = new VazioViewHolder(view);
            return holderVazio;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        int tipo = getItemViewType(position);

        if (tipo == VIEW_CONTATOS) {

            DataEventoViewHolder contatoHolder = (DataEventoViewHolder) viewHolder;
            DataEvento oDataEvento = listaDatas.get(position);

            contatoHolder.txtdataEvento.setText(oDataEvento.DiaEventoFormatado);

        } else if (tipo == VIEW_VAZIO) {
            VazioViewHolder vazioHolder = (VazioViewHolder) viewHolder;

            if (this.msgVazio > 0)
                vazioHolder.textoVazio.setText(this.msgVazio);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listaDatas.size() == 0) {
            return VIEW_VAZIO;
        } else {
            return VIEW_CONTATOS;
        }
    }

    @Override
    public int getItemCount() {
        if (listaDatas.size() == 0) {
            return 1;
        } else {
            return listaDatas.size();
        }
    }
}
