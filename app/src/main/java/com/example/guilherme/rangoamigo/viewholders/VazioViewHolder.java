package com.example.guilherme.rangoamigo.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.guilherme.rangoamigo.R;

import org.w3c.dom.Text;

/**
 * Created by Guilherme on 13/02/18.
 */

public class VazioViewHolder extends RecyclerView.ViewHolder {


    public TextView textoVazio;

    public VazioViewHolder(View itemView) {
        super(itemView);

        this.textoVazio = (TextView) itemView.findViewById(R.id.txtTextoVazio);
    }
}
