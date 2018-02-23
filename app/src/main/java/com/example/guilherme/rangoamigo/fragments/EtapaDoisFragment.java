package com.example.guilherme.rangoamigo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.CadastroEventoActivity;
import com.example.guilherme.rangoamigo.models.Evento;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class EtapaDoisFragment extends Fragment {


    private static final int PLACE_PICKER_REQUEST = 1;
    private View view;
    private TextInputLayout txtEndereco;
    private TextInputLayout txtNomeLocal;
    private Button btnVerMapa;


    public EtapaDoisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_etapadois, container, false);

        txtNomeLocal = (TextInputLayout) view.findViewById(R.id.txtNomeLocal);
        txtEndereco = (TextInputLayout) view.findViewById(R.id.txtEndereco);
        btnVerMapa = (Button) view.findViewById(R.id.btnVerMapa);

        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST){

            if (resultCode == getActivity().RESULT_OK){

                Place place = PlacePicker.getPlace(getActivity(),data);

                txtNomeLocal.getEditText().setText(place.getName());
                txtEndereco.getEditText().setText(place.getAddress());
            }
        }
    }


    public boolean validaDadosEvento()
    {
        boolean bRetorno = true;

        if(txtNomeLocal.getEditText().getText().toString().isEmpty())
        {
            ((CadastroEventoActivity)getActivity()).apresentaMensagem("Aviso!", "Informe o Nome do Local!");
            return false;
        }
        if(txtEndereco.getEditText().getText().toString().isEmpty())
        {
            ((CadastroEventoActivity)getActivity()).apresentaMensagem("Aviso!", "Informe Endereço do Evento!");
            return false;
        }

        return bRetorno;
    }

    public Evento getDatosEvento(Evento oEvento)
    {
        Evento oRetEvento = oEvento;

        oRetEvento.NomeLocal = txtNomeLocal.getEditText().getText().toString();
        oRetEvento.Endereco = txtEndereco.getEditText().getText().toString();

        return oRetEvento;
    }

}
