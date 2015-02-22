package com.izv.android.proyectoinmobiliariahibernate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ivan on 22/02/2015.
 */
public class Imagen implements Serializable {
    private Integer id;
    private String ruta;
    private int idInmueble;

    public Imagen() {
    }

    public Imagen(String ruta, int idInmueble) {
        this.ruta = ruta;
        this.idInmueble = idInmueble;
    }

    public Imagen(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.ruta = object.getString("ruta");
            this.idInmueble = object.getInt("idInmueble");
        }catch (JSONException ex){

        }
    }

    public JSONObject getJSON(){
        JSONObject objetoJSON = new JSONObject();
        try{
            objetoJSON.put("id",this.id);
            objetoJSON.put("ruta",this.ruta);
            objetoJSON.put("idInmueble",this.idInmueble);
            return objetoJSON;

        }catch (Exception e){
            return null;
        }
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getRuta() {
        return this.ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    public int getIdInmueble() {
        return this.idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

}
