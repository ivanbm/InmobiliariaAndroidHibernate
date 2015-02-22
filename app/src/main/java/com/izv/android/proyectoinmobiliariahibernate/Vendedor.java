package com.izv.android.proyectoinmobiliariahibernate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ivan on 29/11/2014.
 */
public class Vendedor implements Serializable, Comparable<Vendedor> {
    private int id, subido;
    private String calle, localidad, tipo;
    private double precio;


    public Vendedor(){
        this(0, "", "", "", 0.0);
    }

    public Vendedor(int id, String calle, String localidad, String tipo, double precio) {
        this.id = id;
        this.calle = calle;
        this.localidad = localidad;
        this.tipo = tipo;
        this.precio = precio;
    }

    public Vendedor(String calle, String localidad, String tipo, String precio, int subido) {
        this.calle = calle;
        this.localidad = localidad;
        this.tipo = tipo;
        try{
            this.precio = Double.parseDouble(precio);
        }catch (NumberFormatException e){
            this.precio = 0.0;
        }
        this.subido = subido;
    }

    public Vendedor(String calle, String localidad, String tipo, String precio) {
        this.calle = calle;
        this.localidad = localidad;
        this.tipo = tipo;
        try{
            this.precio = Double.parseDouble(precio);
        }catch (NumberFormatException e){
            this.precio = 0.0;
        }
    }

    public Vendedor(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.calle = object.getString("calle");
            this.localidad = object.getString("localidad");
            this.tipo = object.getString("tipo");
            this.precio = object.getDouble("precio");
        }catch (JSONException ex){

        }
    }

    public JSONObject getJSON(){
        JSONObject objetoJSON = new JSONObject();
        try{
            objetoJSON.put("id",this.id);
            objetoJSON.put("calle",this.calle);
            objetoJSON.put("localidad",this.localidad);
            objetoJSON.put("tipo",this.tipo);
            objetoJSON.put("precio",this.precio);
            return objetoJSON;

        }catch (Exception e){
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getSubido() {
        return subido;
    }

    public void setSubido(int subido) {
        this.subido = subido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vendedor vendedor = (Vendedor) o;

        if (precio != vendedor.precio) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "id=" + id +
                ", subido=" + subido +
                ", calle='" + calle + '\'' +
                ", localidad='" + localidad + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                '}';
    }

    @Override
    public int compareTo(Vendedor vendedor) {
        return this.getLocalidad().compareTo(vendedor.getLocalidad());
    }
}
