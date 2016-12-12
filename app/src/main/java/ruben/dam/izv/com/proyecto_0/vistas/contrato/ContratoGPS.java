package ruben.dam.izv.com.proyecto_0.vistas.contrato;

import ruben.dam.izv.com.proyecto_0.vistas.pojo.GPS;

/**
 * Created by dam on 1/12/16.
 */

public interface ContratoGPS {

    interface InterfaceModelo {

        void close();

        GPS getGPS(long id);

        long saveGPS(GPS gps);

    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        long onSaveGPS(GPS gps);

    }

    interface InterfaceVista {

        void mostrarGPS(GPS gps);

    }
}
