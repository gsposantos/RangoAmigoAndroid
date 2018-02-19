package com.example.guilherme.rangoamigo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.guilherme.rangoamigo.R;
import com.example.guilherme.rangoamigo.models.ConfimacaoEvento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Guilherme on 18/02/18.
 */

public class ParticipacaoAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> listDatas;
    private HashMap<String, ArrayList<ConfimacaoEvento>> listParticipacao;
    private LayoutInflater inflater;

    public ParticipacaoAdapter(Context context, ArrayList<String> listDatas, HashMap<String, ArrayList<ConfimacaoEvento>> listParticipacao){
        this.listDatas = listDatas;
        this.listParticipacao = listParticipacao;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return listDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listParticipacao.get(listDatas.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listParticipacao.get(listDatas.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderHeader holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.header_list_view, null);
            holder = new ViewHolderHeader();
            convertView.setTag(holder);

            holder.txtDataEvento = (TextView) convertView.findViewById(R.id.txtDataEvento);
        }
        else{
            holder = (ViewHolderHeader) convertView.getTag();
        }

        holder.txtDataEvento.setText(listDatas.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem holder;

        ConfimacaoEvento val = (ConfimacaoEvento) getChild(groupPosition, childPosition);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_list_view, null);
            holder = new ViewHolderItem();
            convertView.setTag(holder);

            holder.txtQtd = (TextView) convertView.findViewById(R.id.txtQtd);
        }
        else{
            holder = (ViewHolderItem) convertView.getTag();
        }

        //holder.tvItem.setText(val);
        holder.txtQtd.setText(String.valueOf(val.qtdConfirmados) + " / " + String.valueOf(val.qtdConvidados));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolderHeader {
        TextView txtDataEvento;
    }

    class ViewHolderItem {
        TextView txtQtd;
        ProgressBar progBarQuantidade;
    }

}
