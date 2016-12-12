package ruben.dam.izv.com.proyecto_0.vistas.vistas.listas;

import android.content.Context;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoLista;
import ruben.dam.izv.com.proyecto_0.vistas.gestion.GestionLista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Lista;

/**
 * Created by Ruben on 03/11/2016.
 */

public class ModeloLista implements ContratoLista.InterfazModelo {

    private GestionLista gl;

    public ModeloLista(Context c){ gl = new GestionLista(c); }

    @Override
    public void close() {

    }

    @Override
    public Lista getLista(long id) {
        return null;
    }

    @Override
    public long saveLista(Lista l) {
        return gl.insert(l);
    }
}
