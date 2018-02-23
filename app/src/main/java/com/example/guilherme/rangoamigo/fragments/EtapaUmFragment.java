package com.example.guilherme.rangoamigo.fragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.guilherme.rangoamigo.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class EtapaUmFragment extends Fragment {


    private View view;
    private ImageButton imgEvento;

    public EtapaUmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_etapaum, container, false);

        imgEvento = (ImageButton) view.findViewById(R.id.imgEvento);
        imgEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentImg = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intentImg, "Selecione uma imagem"), 1234);
            }
        });

        return view;
    }

    /* EVENTO RETORNO GALERIA */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != getActivity().RESULT_CANCELED){

            if(requestCode == 1234){

                Uri imagemSelecioanda = data.getData();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imagemSelecioanda);
                    imgEvento.setBackground(Drawable.createFromStream(inputStream, imagemSelecioanda.toString()));
                    //bAlterouImagem = true;
                } catch (FileNotFoundException e) {
                   // this.showAlert("Erro!", "Ocoreu um problema para carregar a imagem "+ imagemSelecioanda.toString());
                }

            }

        }
    }

}
