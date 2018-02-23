package com.example.guilherme.rangoamigo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.DataEventoAdapter;
import com.example.guilherme.rangoamigo.utils.layout.RecyclerItemClickListener;

import java.util.ArrayList;

public class DatasEventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataEventoAdapter dataEventoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datas_event);

        recyclerView = (RecyclerView) findViewById(R.id.recViewConvidado);
        //listaDatas = new ArrayList<Convidado>();
        //dataEventoAdapter = new DataEventoAdapter(this, listaConvidados);
        //recyclerView.setAdapter(dataEventoAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        //RecyclerItemClickListener
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                boolean bMarcado = false;

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        }));

    }
}
