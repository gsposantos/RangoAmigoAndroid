package com.example.guilherme.rangoamigo.utils.access;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;

import com.example.guilherme.rangoamigo.models.Contato;

import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Guilherme on 11/02/2018.
 */

public class PesquisaContato {

    private final Context context;

    public PesquisaContato(Context context) {
        this.context = context;
    }

    public ArrayList<Contato> pesquisarTodos() {

        String[] retornoCampos = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI
        };

        String sOrdenacao = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        ArrayList<Contato> listaContatos = new ArrayList<>();

        CursorLoader cursorLoader = new CursorLoader(context,
                ContactsContract.Contacts.CONTENT_URI,
                retornoCampos, // colunas retornadas
                null,  // criterio de seleção (nenhum)
                null,  //argumentos para seleção (nenhum)
                sOrdenacao //ordenado por nome
        );

        Cursor c = cursorLoader.loadInBackground();

        final Map<String, Contato> contatosMap = new HashMap<>(c.getCount());

        if (c.moveToFirst()) {

            int idIndice = c.getColumnIndex(ContactsContract.Contacts._ID);
            int nomeIndice = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int fotoIndice = c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);

            //mapa dos contatos com id e objeto para combinar email e numeros
            //incrementa lista de contatos
            do {
                String contatoId = c.getString(idIndice);
                String contatoNome = c.getString(nomeIndice);
                String contatoFoto = c.getString(fotoIndice);
                Contato contato = new Contato(contatoId, contatoNome, contatoFoto);
                contatosMap.put(contatoId, contato);
                listaContatos.add(contato);
            } while (c.moveToNext());
        }

        c.close();

        //carrega apenas telefone celular
        this.combinarNumerosContato(contatosMap);

        //carrega email
        this.combinarEmailsContato(contatosMap);

        //retorna apenas contatos com pelo menos um numero celular
        return this.filtrarContato(listaContatos);

    }

    private void combinarNumerosContato(Map<String, Contato> contatoMap) {

        // pesquisa numeros
        final String[] retornoNumeros = new String[]{
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

        //String selectionArgs[] = {String.valueOf(Phone.TYPE_MOBILE)};
        CursorLoader cursorLoader = new CursorLoader(context,
                Phone.CONTENT_URI,
                retornoNumeros, // colunas retornadas
                null, // criterio de seleção (nenhum)
                null, // argumentos para seleção (nenhum)
                null // ordenação (default)
        );

        Cursor fone = cursorLoader.loadInBackground();

        if (fone.moveToFirst()) {
            final int indiceNumeroContato = fone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int indiceTipoContato = fone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            final int indiceIdContato = fone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

            while (!fone.isAfterLast()) {
                final String numero = fone.getString(indiceNumeroContato);
                final String idContato = fone.getString(indiceIdContato);
                Contato contato = contatoMap.get(idContato);
                if (contato == null) {
                    continue;
                }
                final int tipo = fone.getInt(indiceTipoContato);
                if(Phone.TYPE_MOBILE == tipo) {
                    String customLabel = "Custom";
                    CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), tipo, customLabel);
                    contato.adicionaNumero(numero, phoneType.toString());
                }
                fone.moveToNext();
            }
        }
        fone.close();
    }

    private void combinarEmailsContato(Map<String, Contato> contatoMap) {

        // pesquisa email
        final String[] retornoEmail = new String[]{
                Email.DATA,
                Email.TYPE,
                Email.CONTACT_ID,
        };

        CursorLoader cursorLoader = new CursorLoader(context,
                Email.CONTENT_URI,
                retornoEmail, // colunas retornadas
                null, // criterio de seleção (nenhum)
                null, // argumentos para seleção (nenhum)
                null // ordenação (default)
        );

        Cursor email = cursorLoader.loadInBackground();

        if (email.moveToFirst()) {
            final int indiceEmailContato  = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            final int indiceTipoContato = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE);
            final int indiceIdContato = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);

            while (!email.isAfterLast()) {
                final String endereco = email.getString(indiceEmailContato);
                final String idContato = email.getString(indiceIdContato);
                final int tipo = email.getInt(indiceTipoContato);
                String customLabel = "Custom";
                Contato contato = contatoMap.get(idContato);
                if (contato == null) {
                    continue;
                }
                CharSequence tipoEmail = ContactsContract.CommonDataKinds.Email.getTypeLabel(context.getResources(), tipo, customLabel);
                contato.adicionaEmail(endereco, tipoEmail.toString());
                email.moveToNext();
            }
        }

        email.close();
    }

    private ArrayList<Contato> filtrarContato(ArrayList<Contato> listaContatos){

        for (int i = listaContatos.size()-1; i >= 0; i--){
            if(listaContatos.get(i).numeros.size()==0){
                listaContatos.remove(i);
            }
        }
        return listaContatos;
    }

}
