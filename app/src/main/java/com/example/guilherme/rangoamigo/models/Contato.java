package com.example.guilherme.rangoamigo.models;

import java.util.ArrayList;

/**
 * Created by Guilherme on 11/02/2018.
 */

public class Contato {

    public String id;
    public String nome;
    public String foto;
    public ArrayList<ContatoEmail> emails;
    public ArrayList<ContatoFone> numeros;

    public Contato(String id, String nome, String foto) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
        this.emails = new ArrayList<ContatoEmail>();
        this.numeros = new ArrayList<ContatoFone>();
    }

    @Override
    public String toString() {
        String result = nome;
        if (numeros.size() > 0) {
            ContatoFone number = numeros.get(0);
            result += " (" + number.numero + " - " + number.tipo + ")";
        }
        if (emails.size() > 0) {
            ContatoEmail email = emails.get(0);
            result += " [" + email.endereco + " - " + email.tipo + "]";
        }
        return result;
    }

    public void adicionaEmail(String address, String type) {
        emails.add(new ContatoEmail(address, type));
    }

    public void adicionaNumero(String number, String type) {
        numeros.add(new ContatoFone(number, type));
    }
}
