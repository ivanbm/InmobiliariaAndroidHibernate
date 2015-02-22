package com.izv.android.proyectoinmobiliariahibernate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ivan on 30/11/2014.
 */
public class Anadir extends MainActivity{
    private Spinner sp;
    private EditText direccion, localidad, precio;
    private final static String URLBASE = "http://192.168.1.101:8080/Practica4AADInmobiliariaHibernate/";
    String foto, idInsertado;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir);

        llenarSpinner();
        eventoBoton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anadir_imagen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    public void llenarSpinner(){
        sp = (Spinner) findViewById(R.id.spTipo);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.array_tipo, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptador);
    }



    public void anadirV(){
        String dir, loc, pre, tipo;

        direccion = (EditText)findViewById(R.id.etDireccion);
        localidad = (EditText)findViewById(R.id.etLocalidad);
        precio = (EditText)findViewById(R.id.etPrecio);
        sp = (Spinner) findViewById(R.id.spTipo);

        dir = direccion.getText().toString();
        loc = localidad.getText().toString();
        pre = precio.getText().toString();
        tipo = sp.getSelectedItem().toString();

        Vendedor v = new Vendedor(dir,loc,tipo,pre);

        PostRestful post = new PostRestful();
        ParametrosPost pp = new ParametrosPost();
        pp.url=URLBASE+"control?target=inmueble&op=insert&action=view";
        pp.json=v.getJSON();
        post.execute(pp);

        finish();

        this.finish();

    }

    public void eventoBoton(){
        Button bt = (Button)findViewById(R.id.btAnadir);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                anadirV();
            }
        });
    }


    static class ParametrosPost{
        String url;
        JSONObject json;
    }

    private class PostRestful extends AsyncTask<ParametrosPost, Void, String> {

        @Override
        protected String doInBackground(ParametrosPost[] params) {
            String r = ClienteRestFul.post(params[0].url,params[0].json);
            return r;
        }

        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);

            Toast.makeText(Anadir.this, r, Toast.LENGTH_SHORT).show();
            System.out.println("TOKEN "+r);
            JSONTokener token = new JSONTokener(r);
            try {
                JSONObject objeto = new JSONObject(token);
                idInsertado = objeto.getString("r").toString();

                //ad.notifyDataSetChanged();
            } catch (JSONException e) {

            }
        }
    }


    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}

