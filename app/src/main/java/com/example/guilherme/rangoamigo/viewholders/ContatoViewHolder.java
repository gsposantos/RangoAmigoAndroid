package com.example.guilherme.rangoamigo.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guilherme.rangoamigo.R;

/**
 * Created by Guilherme on 11/02/2018.
 */

public class ContatoViewHolder  extends RecyclerView.ViewHolder {

    public ImageView imgContato;
    public TextView nomeContato;
    public TextView foneContato;
    public TextView emailContato;

    public ContatoViewHolder(View view) {
        super(view);

        this.imgContato = (ImageView) view.findViewById(R.id.imgContato);
        this.nomeContato = (TextView) view.findViewById(R.id.txtNomeContato);
        this.foneContato = (TextView) view.findViewById(R.id.txtFoneContato);
        this.emailContato = (TextView) view.findViewById(R.id.txtEmailContato);
    }
}
