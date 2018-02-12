package com.example.guilherme.rangoamigo.viewholders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.CadastroEventoActivity;
import com.example.guilherme.rangoamigo.activities.DetalheEventoActivity;

/**
 * Created by Guilherme on 04/02/2018.
 */

public class EventoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public int codEvento;
    public ImageView imgEvento;
    public TextView nomeEvento;
    public TextView dataEvento;
    public TextView localEvento;
    public TextView enderecoEvento;

    public EventoViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);

        this.codEvento = 0;
        this.imgEvento = (ImageView) view.findViewById(R.id.imgEvento);
        this.nomeEvento = (TextView) view.findViewById(R.id.txtNomeEvento);
        this.dataEvento = (TextView) view.findViewById(R.id.txtDataEvento);
        this.localEvento = (TextView) view.findViewById(R.id.txtLocalEvento);
        this.enderecoEvento = (TextView) view.findViewById(R.id.txtEnderecoEvento);
    }

    @Override
    public void onClick(View view) {

        //Toast.makeText(view.getContext(), "Evento = " + String.valueOf(this.codEvento), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), DetalheEventoActivity.class);
        intent.putExtra("CodEvento", String.valueOf(this.codEvento));
            view.getContext().startActivity(intent);
    }
}
