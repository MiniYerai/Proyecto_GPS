package ruben.dam.izv.com.proyecto_0.vistas.vistas.main;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoMain;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

public class PresentadorQuip implements ContratoMain.InterfacePresentador{// llama a las funciones

    private ContratoMain.InterfaceModelo modelo;
    private ContratoMain.InterfaceVista vista;
    private ContratoMain.InterfaceModelo.OnDataLoadListener oyente;

    public PresentadorQuip(ContratoMain.InterfaceVista vista) {//muestra el listado de todas las notas
        this.vista = vista;
        this.modelo = new ModeloQuip((Context)vista);
        oyente = new ContratoMain.InterfaceModelo.OnDataLoadListener() {
            @Override
            public void setCursor(Cursor c) {
                PresentadorQuip.this.vista.mostrarDatos(c);
            }
        };
    }

    @Override
    public void onAddNota() {//Añade una nota
        this.vista.mostrarAgregarNota();
    }

    @Override
    public void onAddAudio() {
        this.vista.mostrarAgregarAudio();
    }

    @Override
    public void onAddLista() {

    }

    /*@Override
    public void mostrarAgregarLista() {//muestra activity_nota para añadir una nota
        Intent i = new Intent(this, VistaLista.class);
        startActivity(i);
    }*/

    @Override
    public void onDeleteNota(Nota n) {//borra la nota por objeto
        this.modelo.deleteNota(n);
        this.modelo.loadData(oyente);
    }

    @Override
    public void onEditNota(Nota n) {
        this.vista.mostrarEditarNota(n);
    }//edita la nota por objeto

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
        this.modelo.loadData(oyente);
    }



    @Override
    public void onShowBorrarNota(int position) {//muestra la confirmacion de borrarlo o no
        Nota n = this.modelo.getNota(position);
        this.vista.mostrarConfirmarBorrarNota(n);
    }

    @Override
    public void onSeeMap() {
        this.vista.mostrarMapa();
    }

    @Override
    public void onDeleteNota(int position) {//borra la nota por posicion
        this.modelo.deleteNota(position);
        this.modelo.loadData(oyente);
    }

    @Override
    public void onEditNota(int position) {//edita la nota por posicion
        Nota n = this.modelo.getNota(position);
        this.onEditNota(n);
    }

}
