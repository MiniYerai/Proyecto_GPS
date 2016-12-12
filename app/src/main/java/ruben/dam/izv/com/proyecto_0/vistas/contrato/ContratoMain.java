package ruben.dam.izv.com.proyecto_0.vistas.contrato;

import android.database.Cursor;

import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

public interface ContratoMain {

    interface InterfaceModelo {

        void close();

        long deleteNota(int position);

        long deleteNota(Nota n);

        Nota getNota(int position);

        void loadData(OnDataLoadListener listener);

        interface OnDataLoadListener {
            public void setCursor(Cursor c);
        }
    }

    interface InterfacePresentador {

        void onAddNota();

        void onAddAudio();

        void onAddLista();

        void onDeleteNota(int position);

        void onDeleteNota(Nota n);

        void onEditNota(int position);

        void onEditNota(Nota n);

        void onPause();

        void onResume();

        void onShowBorrarNota(int position);

        void onSeeMap();

    }

    interface InterfaceVista {

        void mostrarAgregarNota();

        void mostrarAgregarLista();

        void mostrarAgregarAudio();

        void mostrarDatos(Cursor c);

        void mostrarEditarNota(Nota n);

        void mostrarConfirmarBorrarNota(Nota n);

        void mostrarMapa();

    }

}