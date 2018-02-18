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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.DetalheEventoActivity;
import com.example.guilherme.rangoamigo.activities.LocalEventoActivity;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.utils.image.ControleImagem;

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

                //desvia para mapas
//                Uri gmmIntentUri = Uri.parse("google.navigation:?q=Mercado Público, Porto Alegre");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                if (null != mapIntent.resolveActivity(getActivity().getPackageManager())) {
//                    startActivity(mapIntent);
//                }
                Intent intent = new Intent(getActivity(), LocalEventoActivity.class);
                startActivity(intent);

            }
        });

//        this.viewPagerDatas = (ViewPager) view.findViewById(R.id.pagerDatas);
//        this.setupViewPage(this.viewPagerDatas);
//
//        this.tabDatas = (TabLayout) view.findViewById(R.id.tabDatas);
//        this.tabDatas.setupWithViewPager(this.viewPagerDatas);

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


        divNome.setVisibility(View.VISIBLE);
        divDatas.setVisibility(View.VISIBLE);
        divLocal.setVisibility(View.VISIBLE);

    }

}
