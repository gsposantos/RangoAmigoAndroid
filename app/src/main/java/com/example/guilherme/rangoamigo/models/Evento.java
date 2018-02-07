package com.example.guilherme.rangoamigo.models;

import java.util.ArrayList;

/**
 * Created by Guilherme on 03/02/2018.
 */

public class Evento {

    public int CodEvento;
    public String NomeEvento;
    public String NomeLocal;
    public String Endereco;
    public long Latitude;
    public long Longitude;
    public String Imagem;

    public ArrayList<DataEvento> Datas;
    public ArrayList<Convidado> Convidados;

}
