package com.example.guilherme.rangoamigo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.SolverVariable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.EventoActivity;
import com.example.guilherme.rangoamigo.activities.MasterActivity;
import com.example.guilherme.rangoamigo.activities.SplashScreenActivity;
import com.example.guilherme.rangoamigo.models.Perfil;
import com.example.guilherme.rangoamigo.models.Retorno;
import com.example.guilherme.rangoamigo.models.RetornoSimples;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinFragment extends Fragment {

    private View view;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private Button btn;
    private FrameLayout progBarHolder;

    private String jsonPerfil;
    private int valPIN;

    public PinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pin, container, false);

        progBarHolder = (FrameLayout) view.findViewById(R.id.progBarHolder);
        btn = (Button) view.findViewById(R.id.btnAcessar);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btn.setEnabled(false);
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progBarHolder.setAnimation(inAnimation);
                progBarHolder.setVisibility(View.VISIBLE);

                TextInputLayout txtPIN = (TextInputLayout) view.findViewById(R.id.txtPIN);
                int pinAux = Integer.parseInt(txtPIN.getEditText().getText().toString());

                //enquanto nao for implementado o envio de sms
                int backDoor = 1234;

                //testar PIN
                if(valPIN == pinAux || backDoor == pinAux){

                    //Salvar contexto
                    AcessoPreferences.setContext(getContext());
                    AcessoPreferences.setDadosPerfil(jsonPerfil);

                    // Acessar chamando intent
                    Intent intent;
                    intent = new Intent(getActivity(),EventoActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else
                {
                    //errooou
                    ((MasterActivity)getActivity()).showAlert("PIN incorreto!", "Informe o código recebido por SMS para prosseguir.");

                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progBarHolder.setAnimation(outAnimation);
                    progBarHolder.setVisibility(View.GONE);
                    btn.setEnabled(true);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView txtNumTelefone = (TextView) view.findViewById(R.id.txtNumTelefone);

        Bundle arguments = getArguments();
        jsonPerfil = arguments.getString("jsonPerfil");

        Gson gson = new Gson();
        Type listType = new TypeToken<RetornoSimples<Perfil>>(){}.getType();
        RetornoSimples<Perfil> retornoPerfil = gson.fromJson(jsonPerfil, listType);

        // ERRO - Unterminated object at line 1 column 181 path $.Foto
        //Perfil oPerfil = gson.fromJson(((ArrayList)retornoPerfil.Dados).get(0).toString(), Perfil.class);
        //Perfil oPerfil = new Perfil();
        //oPerfil.mapearPerfil(((ArrayList)retornoPerfil.Dados).get(0).toString());

        valPIN = retornoPerfil.getDados().PIN;
        String sNumTelefone = Long.valueOf(retornoPerfil.getDados().CelNumero).toString();
        txtNumTelefone.setText("(" + sNumTelefone.substring(0,2) + ") " + sNumTelefone.substring(2)) ;
    }
}
