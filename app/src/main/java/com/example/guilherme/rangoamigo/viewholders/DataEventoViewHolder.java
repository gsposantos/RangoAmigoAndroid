package com.example.guilherme.rangoamigo.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.guilherme.rangoamigo.R;

/**
 * Created by Guilherme on 23/02/2018.
 */

public class DataEventoViewHolder  extends RecyclerView.ViewHolder  implements View.OnClickListener {

    public Switch chkVoto;
    public TextView txtdataEvento;

    public Context context;

    public DataEventoViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);

        this.txtdataEvento = (TextView) view.findViewById(R.id.txtDataEvento);
        this.chkVoto = (Switch) view.findViewById(R.id.chkVoto);
    }

    @Override
    public void onClick(View v) {
        this.chkVoto.setChecked(!this.chkVoto.isChecked());
    }
}
