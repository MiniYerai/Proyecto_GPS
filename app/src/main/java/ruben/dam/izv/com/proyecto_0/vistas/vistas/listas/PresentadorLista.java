package ruben.dam.izv.com.proyecto_0.vistas.vistas.listas;

import android.content.Context;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoLista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Lista;


/**
 * Created by Ruben on 03/11/2016.
 */

public class PresentadorLista implements ContratoLista.InterfazPresentador {

    private ContratoLista.InterfazModelo modelo;
    private ContratoLista.InterfazVista vista;

    public PresentadorLista(ContratoLista.InterfazVista vista){
        this.vista = vista;
        this.modelo = new ModeloLista((Context)vista);
    }
    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public long onSaveLista(Lista l) {
        return modelo.saveLista(l);
    }
}
