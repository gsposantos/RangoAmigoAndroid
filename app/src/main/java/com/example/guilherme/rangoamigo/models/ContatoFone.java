package com.example.guilherme.rangoamigo.models;

/**
 * Created by Guilherme on 11/02/2018.
 */

public class ContatoFone {

    public String fone;
    public long numero;
    public String tipo;

    public ContatoFone(String fone, String tipo) {
        this.fone = fone;
        this.tipo = tipo;
    }
}