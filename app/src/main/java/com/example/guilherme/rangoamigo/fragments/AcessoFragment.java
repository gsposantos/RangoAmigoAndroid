package com.example.guilherme.rangoamigo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guilherme.rangoamigo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcessoFragment extends Fragment {


    public AcessoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_acesso, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

		/*FoneFragment é a primeiro fragament*/
        transaction.replace(R.id.acesso_frame, new FoneFragment());
        transaction.commit();
        return view;
    }

}
