package ruben.dam.izv.com.proyecto_0.vistas.contrato;

/*
 * Created by Ruben on 19/10/2016.
 */


import ruben.dam.izv.com.proyecto_0.vistas.pojo.Lista;

public interface ContratoLista {

    interface InterfazModelo {

        void close();

        Lista getLista(long id);

        long saveLista(Lista l);

    }

    interface InterfazPresentador {

        void onPause();

        void onResume();

        long onSaveLista(Lista l);

    }

    interface InterfazVista {

        void mostrarLista(Lista l);

    }
}
