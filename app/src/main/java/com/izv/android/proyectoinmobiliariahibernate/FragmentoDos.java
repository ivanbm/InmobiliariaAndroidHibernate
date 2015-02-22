package com.izv.android.proyectoinmobiliariahibernate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class FragmentoDos extends Fragment {

    private View v;


    public FragmentoDos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fragmento_dos, container, false);
        return v;
    }
    /*public void setTexto(String s){
        TextView tv = (TextView)v.findViewById(R.id.tvFragmento2);
        tv.setText(s);
    }*/


}
