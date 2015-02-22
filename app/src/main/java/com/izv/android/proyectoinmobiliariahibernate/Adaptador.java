package com.izv.android.proyectoinmobiliariahibernate;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Ivan on 29/11/2014.
 */
public class Adaptador extends ArrayAdapter<Vendedor> {

    private ArrayList<Vendedor> lista;
    private Context contexto;
    private int recurso;
    private static LayoutInflater i;

    public Adaptador(Context context, int resource, ArrayList<Vendedor> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;
        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView tv1, tv2, tv3;
        public ImageView iv;
        public int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            convertView = i.inflate(recurso, null);


            vh = new ViewHolder();
            vh.iv = (ImageView)convertView.findViewById(R.id.ivFtipo);
            vh.tv1 = (TextView)convertView.findViewById(R.id.tvDireccion);
            vh.tv2 = (TextView)convertView.findViewById(R.id.tvTipo);
            vh.tv3 = (TextView)convertView.findViewById(R.id.tvPrecio);

            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.position = position;
        vh.tv1.setTag(position);

        vh.tv1.setText(lista.get(position).getLocalidad());
        vh.tv2.setText(lista.get(position).getTipo());
        vh.tv3.setText(lista.get(position).getPrecio()+"");

        if(lista.get(position).getTipo().equals("casa")) {
            vh.iv.setImageResource(R.drawable.house);
        }else if(lista.get(position).getTipo().equals("piso")) {
            vh.iv.setImageResource(R.drawable.flat);
        }else if(lista.get(position).getTipo().equals("cochera")) {
            vh.iv.setImageResource(R.drawable.garage);
        }else if(lista.get(position).getTipo().equals("habitación")) {
            vh.iv.setImageResource(R.drawable.room);
        }else if(lista.get(position).getTipo().equals("habitacion")) {
            vh.iv.setImageResource(R.drawable.room);
        }
        vh.iv.setTag(position);

        return convertView;
    }
}