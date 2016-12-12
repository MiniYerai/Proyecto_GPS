package ruben.dam.izv.com.proyecto_0.vistas.contrato;

import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

public interface ContratoNota {

    interface InterfaceModelo {

        void close();

        Nota getNota(long id);

        long saveNota(Nota n);

    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        long onSaveNota(Nota n);

        void onAddAudio();

        void onAddImg();

        void imagenCompleta();

        void onMostrarMap();

    }

    interface InterfaceVista {

        void mostrarNota(Nota n);

        void mostrarAgregarAudio();

        void mostrarImagen();

        void visualizador();

        void mostrarMaps();
    }

}