package ruben.dam.izv.com.proyecto_0.vistas.vistas.notas;

import android.content.Context;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoLista;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoNota;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Lista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

public class PresentadorNota implements ContratoNota.InterfacePresentador {

    private ContratoNota.InterfaceModelo modelo;
    private ContratoLista.InterfazModelo modeloL;
    private ContratoNota.InterfaceVista vista;

    public PresentadorNota(ContratoNota.InterfaceVista vista) {
        this.vista = vista;
        this.modelo = new ModeloNota((Context)vista);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
    }

    @Override
    public long onSaveNota(Nota n) {
        return this.modelo.saveNota(n);
    }

    @Override
    public void onAddAudio() {
        this.vista.mostrarAgregarAudio();
    }

    @Override
    public void onAddImg() {
        this.vista.mostrarImagen();
    }

    @Override
    public void imagenCompleta() {
        this.vista.visualizador();
    }

    @Override
    public void onMostrarMap() {
        this.vista.mostrarMaps();
    }

    public long onSaveLista(Lista l){
        return this.modeloL.saveLista(l);
    }

}