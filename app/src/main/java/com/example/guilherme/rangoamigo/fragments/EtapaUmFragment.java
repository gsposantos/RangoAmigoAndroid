package com.example.guilherme.rangoamigo.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.activities.CadastroEventoActivity;
import com.example.guilherme.rangoamigo.models.DataEvento;
import com.example.guilherme.rangoamigo.models.DataEventoVoto;
import com.example.guilherme.rangoamigo.models.Evento;
import com.example.guilherme.rangoamigo.utils.access.AcessoPreferences;
import com.example.guilherme.rangoamigo.utils.image.ControleImagem;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class EtapaUmFragment extends Fragment {


    private View view;
    private ImageButton imgEvento;
    private TextInputLayout txtDataEvento;
    private TextInputLayout txtNomeEvento;
    private Date oDataSelecionada;
    private boolean bAlterouImagem;

    public EtapaUmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_etapaum, container, false);

        txtNomeEvento = (TextInputLayout) view.findViewById(R.id.txtNomeEvento);

        txtDataEvento = (TextInputLayout) view.findViewById(R.id.txtDataEvento);
        txtDataEvento.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CadastroEventoActivity)getActivity()).desviaSelecionaDataEvento();
            }
        });

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

    public void configuraDataEvento(Date oDataEvento)
    {
         /*TODO: Validar data*/

        //montar a data corretamente
        this.oDataSelecionada = oDataEvento;
        Format formatter = new SimpleDateFormat("dd/MM/yyyy  -  HH:mm");
        String sDiaEvento = formatter.format(oDataEvento);

        txtDataEvento.getEditText().setText(sDiaEvento);
    }

    public boolean validaDadosEvento()
    {
        boolean bRetorno = true;

        if(txtNomeEvento.getEditText().getText().toString().isEmpty())
        {
            ((CadastroEventoActivity)getActivity()).apresentaMensagem("Aviso!", "Informe o Nome do Evento!");
            return false;
        }

        if(txtDataEvento.getEditText().getText().toString().isEmpty())
        {
            ((CadastroEventoActivity)getActivity()).apresentaMensagem("Aviso!", "Informe a Data e Hora do Evento!");
            return false;
        }
        else
        {
            String sDt = txtDataEvento.getEditText().getText().toString();
            //DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy  -  HH:mm");
            df.setLenient (false); // aqui o pulo do gato
            try {
                System.out.println(df.parse(sDt));
                // data válida
            } catch (ParseException ex) {
                ((CadastroEventoActivity)getActivity()).apresentaMensagem("Aviso!", "Data informada é inválida!");
                return false;
            }
        }

        return bRetorno;
    }

    public Evento getDatosEvento()
    {
        String sFoto = "";
        Evento oEvento = new Evento();
        DataEvento oDataEvento = new DataEvento();
        DataEventoVoto oDataEventoVoto = new DataEventoVoto();

        BitmapDrawable drawable = (BitmapDrawable) imgEvento.getBackground();
        Bitmap bitmap = drawable.getBitmap();

        if (bAlterouImagem) {
            sFoto = ControleImagem.encodeBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
        }

        if(!sFoto.isEmpty()) {
            oEvento.Imagem = sFoto;
        }

        oEvento.NomeEvento = txtNomeEvento.getEditText().getText().toString();

        AcessoPreferences.setContext(getContext());
        oDataEventoVoto.CelNumero = ((CadastroEventoActivity)getActivity()).getPerfilPreferences().CelNumero;
        oDataEventoVoto.Voto = true;



        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        oDataEvento.DiaEvento = formatter.format(oDataSelecionada);

        oDataEvento.Participacao = new ArrayList<DataEventoVoto>();
        oDataEvento.Participacao.add(oDataEventoVoto);
        oDataEvento.Original = true;

        oEvento.Datas = new ArrayList<DataEvento>();
        oEvento.Datas.add(oDataEvento);

        return oEvento;
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
                    bAlterouImagem = true;
                } catch (FileNotFoundException e) {
                    ((CadastroEventoActivity)getActivity()).apresentaMensagem("Erro!", "Ocoreu um problema para carregar a imagem "+ imagemSelecioanda.toString());
                }

            }

        }
    }

}
