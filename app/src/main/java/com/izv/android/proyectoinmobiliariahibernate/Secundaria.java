package com.izv.android.proyectoinmobiliariahibernate;

/**
 * Created by Ivan on 29/11/2014.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Secundaria extends Activity {
    private int id;
    private ArrayList<String> rutaImg = new ArrayList<String>();
    private ArrayList<File> listaImg;
    private ImageView ivFoto;
    private int posicion = 0;
    private final static String URLBASE = "http://192.168.1.101:8080/Practica4AADInmobiliariaHibernate/";
    private static int PICK = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secundaria);
        id = getIntent().getExtras().getInt("id");
        final FragmentoDos fdos = (FragmentoDos)getFragmentManager().findFragmentById(R.id.fragment);

        //obtenerImagenes(id);
        cargarImg();
        botonAnterior();
        botonSiguiente();
        eventoFoto();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //s=savedInstanceState.getString("eres");
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
        if (id == R.id.anadir) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cargarImg() {

        String[] peticiones = new String[1];
        peticiones[0] = "control?target=inmueble&op=view_android&action=view&id="+id;
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
            //Toast.makeText(MainActivity.this, r[0], Toast.LENGTH_SHORT).show();

            JSONTokener token = new JSONTokener(r[0]);
            try {
                JSONObject objeto = new JSONObject(token);
                JSONArray array = objeto.getJSONArray("fotos");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Imagen g = new Imagen(obj);
                    rutaImg.add(corregirRuta(g.getRuta()));
                    System.out.println(corregirRuta(g.getRuta()));

                }
                mostrarImg(0);
                //ad.notifyDataSetChanged();
            } catch (JSONException e) {
                System.out.println(e);
            }
        }
    }

    public String corregirRuta(String ruta){
        String r = ruta.replace("C:\\Users\\Ivan\\Desktop\\Mañana\\DAM-2\\AAD\\Practica 4\\Practica4AADInmobiliariaHibernate\\build\\web\\images",
                "http://192.168.1.101:8080/Practica4AADInmobiliariaHibernate/images");
        return r;
    }


    //HAY QUE PRGRAMAR LOS BOTONES Y COMPROBAR QUE VA EL VISOR
    public void mostrarImg(int pos){
        ivFoto = (ImageView)findViewById(R.id.ivVisor);
        Picasso.with(this).load(rutaImg.get(pos)).into(ivFoto);
    }

    public void botonSiguiente(){
        Button bt = (Button)findViewById(R.id.btSig);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if(posicion+1 > rutaImg.size()-1) {
                    posicion = 0;
                }else{
                    posicion = posicion+1;
                }
                mostrarImg(posicion);
            }
        });
    }

    public void botonAnterior(){
        Button bt = (Button)findViewById(R.id.btAnt);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if(posicion-1 < 0) {
                    posicion = rutaImg.size()-1;
                }else{
                    posicion = posicion-1;
                }
                mostrarImg(posicion);
            }
        });
    }

    public void eventoFoto(){
        Button bt = (Button)findViewById(R.id.btSubir);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                pick();
            }
        });
    }


    /*-------------------------------------*/
    /*--           SUBIR FOTO            --*/
    /*-------------------------------------*/

    public void pick(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK);
        }
    }

    private String archivoSubir;

    public String getPath(Uri uri) {
        Cursor cur = getContentResolver().query(uri,null, null, null, null);
        cur.moveToFirst();
        String path = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
        cur.close();
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String rutaUriArchivo = getPath(uri);
            archivoSubir = getPath(uri);
            Toast.makeText(this, rutaUriArchivo, Toast.LENGTH_SHORT).show();
            new Upload().execute();

        }
    }

    class Upload extends AsyncTask<String,Integer,String> {


        //String url="http://192.168.208.187:8080/Inmobiliaria/subir/ControlSubir";
        String url=URLBASE+"control?target=inmueble&op=subir&action=op&idInmuebleFoto="+id;



        public String postFile(String urlPeticion, String nombreParametro, String nombreArchivo) {
            String resultado="";
            int status=0;
            try {
                URL url = new URL(urlPeticion);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setDoOutput(true);
                conexion.setRequestMethod("POST");
                FileBody fileBody = new FileBody(new File(nombreArchivo));
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
                multipartEntity.addPart(nombreParametro, fileBody);
                multipartEntity.addPart("nombre", new StringBody("valor"));
                conexion.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
                OutputStream out = conexion.getOutputStream();
                try {
                    multipartEntity.writeTo(out);
                }catch (Exception e){
                    Log.v("excepcion 1", e.toString());
                }finally {
                    out.close();
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String decodedString;
                while ((decodedString = in.readLine()) != null) {
                    resultado+=decodedString+"\n";
                }
                in.close();
                status = conexion.getResponseCode();
            } catch (MalformedURLException ex) {
                Log.v("excepcion 2",ex.toString());
            } catch (IOException ex) {
                Log.v("excepcion 3",ex.toString());
            }
            return resultado+"\n"+status;
        }

        @Override
        protected String doInBackground(String... params) {
            String r = postFile(url, "archivo", archivoSubir);
            return r;
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            Toast.makeText(Secundaria.this,strings,Toast.LENGTH_SHORT).show();

        }
    }

}

