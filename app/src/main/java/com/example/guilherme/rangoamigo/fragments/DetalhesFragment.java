package com.example.guilherme.rangoamigo.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.adapters.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalhesFragment extends Fragment {

    private View view;
    private CardView divLocal;
    private ViewPager viewPagerDatas;
    private TabLayout tabDatas;
    private ViewPagerAdapter pagerAdapter;

    public DetalhesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detalhes, container, false);


        divLocal = (CardView) view.findViewById(R.id.cardViewLocal);
        divLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //desvia para mapas

                //Intent intent = FeedsActivity.getCallingIntent(getActivity(), mFeed.node.title, mFeed.node.id);
                //startActivity(intent);

            }
        });

        this.viewPagerDatas = (ViewPager) view.findViewById(R.id.pagerDatas);
        this.setupViewPage(this.viewPagerDatas);

        this.tabDatas = (TabLayout) view.findViewById(R.id.tabDatas);
        this.tabDatas.setupWithViewPager(this.viewPagerDatas);

        return view;
    }

    private void setupViewPage(ViewPager viewPager){
        /* Instancia uma ViewPager e o PagerAdapter. */
        pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        pagerAdapter.addFragment(new Fragment(), "Data(s) do Evento");
        pagerAdapter.addFragment(new Fragment(), "Participação");

        viewPager.setAdapter(pagerAdapter);
    }

}
