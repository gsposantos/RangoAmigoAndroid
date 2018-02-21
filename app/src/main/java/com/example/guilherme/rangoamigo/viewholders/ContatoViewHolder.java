package com.example.guilherme.rangoamigo.viewholders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guilherme.rangoamigo.R;

/**
 * Created by Guilherme on 11/02/2018.
 */

public class ContatoViewHolder  extends RecyclerView.ViewHolder  implements View.OnLongClickListener {

    public CardView contatoCard;
    public ImageView imgContato;
    public TextView nomeContato;
    public TextView foneContato;
    public TextView emailContato;
    public  boolean destaque = false;

    public Context context;

    public ContatoViewHolder(View view) {
        super(view);
        view.setOnLongClickListener(this);

        this.contatoCard = (CardView) view.findViewById(R.id.contato_container);
        this.imgContato = (ImageView) view.findViewById(R.id.imgContato);
        this.nomeContato = (TextView) view.findViewById(R.id.txtNomeContato);
        this.foneContato = (TextView) view.findViewById(R.id.txtFoneContato);
        this.emailContato = (TextView) view.findViewById(R.id.txtEmailContato);
    }

    @Override
    public boolean onLongClick(View view) {

        if(this.destaque){
            this.contatoCard.setCardBackgroundColor(ContextCompat.getColor(this.context, R.color.secondaryLightColor));
        }
        return false;
    }
}
