package com.example.guilherme.rangoamigo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.CadastroEventoActivity;
import com.example.guilherme.rangoamigo.adapters.ContatoAdapter;
import com.example.guilherme.rangoamigo.models.Contato;
import com.example.guilherme.rangoamigo.models.Convidado;
import com.example.guilherme.rangoamigo.models.RetornoLista;
import com.example.guilherme.rangoamigo.models.RetornoSimples;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.example.guilherme.rangoamigo.utils.layout.RecyclerItemClickListener;
import com.example.guilherme.rangoamigo.viewholders.ContatoViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EtapaTresFragment extends Fragment {


    private View view;
    private ArrayList<Contato> oListaContatos;
    private ArrayList<Convidado> oListaConvidado;
    private RecyclerView recyclerView;
    private ContatoAdapter contatoAdapter;

    public EtapaTresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_etapatres, container, false);

        //Carrregar lista de contatos (apenas cadastrados) - estao nas preferences
        //Permitir selecionar com longclick e adicionar num arrar
        //Validar selecionados e mostrar mensagem lista == 0
        //Primeiro convidado é o perfil logado e fica como organizador
        //Converter lista de contatos para convidados (perfil)

        //salvar evento
        //incluir voto (para nao precisar alterar API)

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Contato>>(){}.getType();
        ArrayList<Contato> oContatosAux = gson.fromJson(AcessoPreferences.getDadosContatos(), listType);

        oListaContatos = new ArrayList<Contato>();
        oListaConvidado = new ArrayList<Convidado>();

        if(oContatosAux.size() > 0) {
            for (int i = 0; i < oContatosAux.size(); i++){
                if(oContatosAux.get(i).cadastrado){
                    oListaContatos.add(oContatosAux.get(i));
                }
            }
        }
        else {
            ((CadastroEventoActivity) getActivity()).apresentaMensagem("Aviso!", "Sincronize seus contatos para adicioná-los ao evento.");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recViewContatos);
        contatoAdapter = new ContatoAdapter(oListaContatos, view.getContext(), true);
        contatoAdapter.setMsgVazio(R.string.contato_sem_registros);

        recyclerView.setAdapter(contatoAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // faz nada nesse caso
            }

            @Override
            public void onItemLongClick(View view, int position) {


                boolean bMarcado = false;

                //pega o contato para enviar convite para usar app
                Contato oContato = oListaContatos.get(position);
                Convidado oConvidado = new Convidado();

                oConvidado.Organizador = false;
                oConvidado.CelNumero = oContato.numeros.get(0).numero;

                //testa se ja foi marcado
                for(int i=0;i<oListaConvidado.size();i++){
                    if(oListaConvidado.get(i).CelNumero ==  oConvidado.CelNumero){
                        bMarcado = true;
                        break;
                    }
                }

                if(!bMarcado) {
                    oListaConvidado.add(oConvidado);
                }
                else {
                    oListaConvidado.remove(oConvidado);
                }
            }

        }));

        return view;
    }

    public boolean validaConvidadosEvento()
    {
        boolean bRetorno = true;

        if(!(oListaConvidado.size() > 0))
        {
            ((CadastroEventoActivity)getActivity()).apresentaMensagem("Aviso!", "Selecione (com toque longo) os convidados para o evento!");
            return false;
        }

        return bRetorno;
    }

    public ArrayList<Convidado> getConvidadosSelecioandos()
    {
        return oListaConvidado;
    }

}
