package com.example.guilherme.rangoamigo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.ConvidadoAdapter;
import com.example.guilherme.rangoamigo.adapters.EventoAdapter;
import com.example.guilherme.rangoamigo.models.Contato;
import com.example.guilherme.rangoamigo.models.Convidado;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConvidadosFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private FrameLayout progBarHolder;
    private ArrayList<Convidado> listaConvidados;
    private ConvidadoAdapter convidadoContato;

    public ConvidadosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_convidados, container, false);

        progBarHolder = (FrameLayout) view.findViewById(R.id.progBarHolder);
        recyclerView = (RecyclerView) view.findViewById(R.id.recViewConvidado);
        listaConvidados = new ArrayList<Convidado>();
        convidadoContato = new ConvidadoAdapter(view.getContext(), listaConvidados);
        recyclerView.setAdapter(convidadoContato);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        return view;
    }

    private void atualizaLista(ArrayList<Convidado> lista){
        this.convidadoContato.setMsgVazio(R.string.contato_sem_registros);
        this.convidadoContato.setConvidados(lista);
        this.convidadoContato.notifyDataSetChanged();
    }

    public void carregaConvidadosEvento(ArrayList<Convidado> lstCovidadosEvento){

        //Recebe lista de convidados do evento
        //Cruza com contatos sincronizados para decidir que campos mostrar
        //Se ta na lista de contatos, mostra todos os dados. Senão, só nome, foto e email)
        //Monta uma lista de convidados nova para atualizar no adapter

        ArrayList<Contato> listaContatos = new ArrayList<Contato>();
        Convidado oConvidadoAux;

        if(!AcessoPreferences.getDadosContatos().isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Contato>>() {
            }.getType();
            listaContatos = new Gson().fromJson(AcessoPreferences.getDadosContatos(), listType);
        }

        for(int i=0 ; i <  lstCovidadosEvento.size(); i++){
            for(int j=0 ; j <  listaContatos.size(); j++){

                if(lstCovidadosEvento.get(i).CelNumero == listaContatos.get(j).numeros.get(0).numero){
                    listaConvidados.add(lstCovidadosEvento.get(i));
                    break;
                }

                //percorreu toda lista e nao encontrou contato, cria perfil sem dados
                if(j == listaContatos.size()-1){
                    oConvidadoAux = new Convidado();
                    oConvidadoAux.Nome = lstCovidadosEvento.get(i).Nome;
                    oConvidadoAux.Email = lstCovidadosEvento.get(i).Email;
                    oConvidadoAux.Foto = lstCovidadosEvento.get(i).Foto;
                    listaConvidados.add(oConvidadoAux);
                }
            }
        }
        this.atualizaLista(listaConvidados);
    }
}
