package com.izv.android.proyectoinmobiliariahibernate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private Adaptador ad;
    private ListView lv;
    private Vendedor v;
    private ArrayList<Vendedor> inmuebles = new ArrayList<Vendedor>();
    private final static int ANADIR_VENDEDOR = 1;
    private final static String URLBASE = "http://192.168.1.101:8080/Practica4AADInmobiliariaHibernate/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        inmuebles = new ArrayList<Vendedor>();
        lv = (ListView) findViewById(R.id.listView);
        ad = new Adaptador(this, R.layout.detalle, inmuebles);
        lv.setAdapter(ad);
        cargarActividades();

        registerForContextMenu(lv);

        final FragmentoDos fdos = (FragmentoDos) getFragmentManager().findFragmentById(R.id.fragment4);
        final boolean horizontal = fdos != null && fdos.isInLayout(); //Saber que orientación tengo
        int v = getResources().getConfiguration().orientation;

        switch (v) {

            case Configuration.ORIENTATION_LANDSCAPE:
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                break;
        }

        lv = (ListView)findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(horizontal){
                    //ac = (Actividad)lv.getItemAtPosition(position);
                    //initComponents();
                    //cargarLista();
                }else{
                    Vendedor v = (Vendedor)lv.getItemAtPosition(position);
                    Intent i = new Intent(MainActivity.this, Secundaria.class);
                    i.putExtra("id", v.getId());
                    startActivityForResult(i, 4);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.anadir) {
            anadir();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()){

            case R.id.elimiar:
                eliminar(index);
                ad.notifyDataSetChanged();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void cargarActividades() {
        String[] peticiones = new String[1];
        peticiones[0] = "control?target=inmueble&op=select_android&action=view";
        GetRestful get = new GetRestful();
        get.execute(peticiones);
    }

    private class GetRestful extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String[] params) {
            String[] r = new String[params.length];
            int contador = 0;
            for (String s : params) {
                r[contador] = ClienteRestFul.get(URLBASE + s);
                contador++;
            }
            return r;
        }

        @Override
        protected void onPostExecute(String[] r) {
            super.onPostExecute(r);
            Toast.makeText(MainActivity.this, r[0], Toast.LENGTH_SHORT).show();

            JSONTokener token = new JSONTokener(r[0]);
            try {
                JSONObject objeto = new JSONObject(token);

                //JSONArray array = new JSONArray(token);
                JSONArray array = objeto.getJSONArray("inmueble");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Vendedor v = new Vendedor(obj);
                    System.out.println("BBBB "+v.getLocalidad());
                    inmuebles.add(v);
                }
                ad.notifyDataSetChanged();
            } catch (JSONException e) {
                System.out.println(e);
            }
        }
    }

    /*-------------------------------------*/
    /*--          AÑADIR INMUEBLE        --*/
    /*-------------------------------------*/
    public void anadir() {
        Intent i = new Intent(this, Anadir.class);

        startActivityForResult(i, ANADIR_VENDEDOR);
    }


    /*-------------------------------------*/
    /*--          AÑADIR INMUEBLE        --*/
    /*-------------------------------------*/
    public void eliminar(final int index){

        Vendedor v = (Vendedor)lv.getItemAtPosition(index);
        String[] peticiones = new String[1];
        peticiones[0] = "control?target=inmueble&op=delete&action=op&id="+v.getId();
        DeleteRestFul del = new DeleteRestFul();
        del.execute(peticiones);


    }


    private class DeleteRestFul extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String[] params) {
            String[] r = new String[params.length];
            int contador = 0;
            for (String s : params) {
                r[contador] = ClienteRestFul.delete(URLBASE + params[0]);
                contador++;
            }
            return r;
        }

        @Override
        protected void onPostExecute(String[] r) {
            super.onPostExecute(r);
            Toast.makeText(MainActivity.this, r[0], Toast.LENGTH_SHORT).show();

            JSONTokener token = new JSONTokener(r[0]);
            try {
                JSONObject objeto = new JSONObject(token);

                //JSONArray array = new JSONArray(token);
                JSONArray array = objeto.getJSONArray("inmueble");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Vendedor v = new Vendedor(obj);
                    System.out.println("CCCC "+v.getLocalidad());
                    inmuebles.add(v);
                }
                ad.notifyDataSetChanged();
            } catch (JSONException e) {
                System.out.println(e);
            }
        }
    }



}
