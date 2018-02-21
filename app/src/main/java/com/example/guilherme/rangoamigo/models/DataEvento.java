package com.example.guilherme.rangoamigo.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Guilherme on 03/02/2018.
 */

public class DataEvento {

    //nao utilizar :: pode ser q de problema no parse do json
    public String DiaEvento;
    //public String DiaEventoEnvio;

    //campos desnecessário. Remover este campo e testar como fica o parse do json
    public String DiaEventoFormatado;

    public int Quorum;
    public boolean Original; //deve ser repensado

    public ArrayList<DataEventoVoto> Participacao;

}
