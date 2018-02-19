package com.example.guilherme.rangoamigo.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.DetalheEventoActivity;
import com.example.guilherme.rangoamigo.activities.LocalEventoActivity;
import com.example.guilherme.rangoamigo.adapters.ParticipacaoAdapter;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;
import com.example.guilherme.rangoamigo.models.ConfimacaoEvento;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.utils.image.ControleImagem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalhesFragment extends Fragment {

    private View view;
    private CardView divLocal;
    private CardView divNome;
    private CardView divDatas;
    //private ViewPager viewPagerDatas;
    private TabLayout tabDatas;
    private ViewPagerAdapter pagerAdapter;
    private Evento oEvento;

    private ArrayList<String> listDatas;
    private HashMap<String, ArrayList<ConfimacaoEvento>> listParticipacao;
    private ExpandableListView expandableListView;

    public DetalhesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detalhes, container, false);


        divNome = (CardView) view.findViewById(R.id.cardViewId);
        divDatas = (CardView) view.findViewById(R.id.cardViewDatas);

        divNome.setVisibility(View.GONE);
        divDatas.setVisibility(View.GONE);

        divLocal = (CardView) view.findViewById(R.id.cardViewLocal);
        divLocal.setVisibility(View.GONE);
        divLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LocalEventoActivity.class);
                intent.putExtra("EnderecoEvento", oEvento.Endereco);
                intent.putExtra("LocalEvento", oEvento.NomeLocal);
                startActivity(intent);

            }
        });

//        this.viewPagerDatas = (ViewPager) view.findViewById(R.id.pagerDatas);
//        this.setupViewPage(this.viewPagerDatas);
//
//        this.tabDatas = (TabLayout) view.findViewById(R.id.tabDatas);
//        this.tabDatas.setupWithViewPager(this.viewPagerDatas);


        expandableListView = (ExpandableListView) view.findViewById(R.id.listDatas);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(ExpandableList.this, "Group: "+groupPosition+"| Item: "+childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(ExpandableList.this, "Group (Expand): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(ExpandableList.this, "Group (Collapse): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /*
    private void setupViewPage(ViewPager viewPager){
        pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        pagerAdapter.addFragment(new Fragment(), "Data(s) do Evento");
        pagerAdapter.addFragment(new Fragment(), "Participação");
        viewPager.setAdapter(pagerAdapter);
    }
    */


    public TabLayout getTabDatas() {
        return tabDatas;
    }

    public void carregaDetalheEvento(Evento oEvento){

        //Imagem Evento
        if(oEvento.Imagem != null && !oEvento.Imagem.isEmpty() ) {
            ImageView imgEvento = (ImageView) view.findViewById(R.id.imgDetalheEvento);
            //imgEvento.setBackground(ControleImagem.decodeBase64(oEvento.Imagem , view.getResources()));
            imgEvento.setImageDrawable(ControleImagem.decodeBase64(oEvento.Imagem , view.getResources()));
        }

        //Nome Evento
        TextView txtNomeEvento = (TextView) view.findViewById(R.id.txtDetalheNomeEvento);
        txtNomeEvento.setText(oEvento.NomeEvento);

        //Local Evento
        TextView txtLocalEvento = (TextView) view.findViewById(R.id.txtLocalEvento);
        txtLocalEvento.setText(oEvento.NomeLocal);

        //Endereco Evento
        TextView txtEnderecoEvento = (TextView) view.findViewById(R.id.txtEnderecoEvento);
        txtEnderecoEvento.setText(oEvento.Endereco);

        //Datas ...
        listDatas = new ArrayList<String>();
        listParticipacao = new HashMap<String, ArrayList<ConfimacaoEvento>>();

        for(int i = 0; i < oEvento.Datas.size(); i++){
            listDatas.add(oEvento.Datas.get(i).DiaEventoFormatado);
            ArrayList<ConfimacaoEvento> qtds = new ArrayList<ConfimacaoEvento>();
            qtds.add(new ConfimacaoEvento(oEvento.Datas.get(i).Quorum, oEvento.Datas.get(i).Participacao.size()));
            listParticipacao.put(oEvento.Datas.get(i).DiaEventoFormatado, qtds);
        }

        ParticipacaoAdapter oPartAdapter = new ParticipacaoAdapter(getActivity(), listDatas, listParticipacao);
        expandableListView.setAdapter(oPartAdapter);
        oPartAdapter.notifyDataSetChanged();

        divNome.setVisibility(View.VISIBLE);
        divDatas.setVisibility(View.VISIBLE);
        divLocal.setVisibility(View.VISIBLE);

    }

}
