package ruben.dam.izv.com.proyecto_0.vistas.vistas.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoMain;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

public class ModeloQuip implements ContratoMain.InterfaceModelo {

    //private GestionNota gn = null;
    private ContentResolver resolver;
    private Cursor cursor;
    private Uri uri = ContratoBaseDatos.CONTENT_URI_NOTA;

    public ModeloQuip(Context c) {
        //gn = new GestionNota(c);
        this.resolver = c.getContentResolver();
    }

    @Override
    public void close() {
        //gn.close();
        cursor.close();
    }

    @Override
    public long deleteNota(Nota n) {//borra la nota

        String where = ContratoBaseDatos.TablaNota._ID + " = ? ";
        String[] argumentos = {n.getId() + ""};
        return this.resolver.delete(uri, where, argumentos);
        //return gn.delete(n);
    }

    @Override
    public long deleteNota(int position) {// recibe la posicion de la lista y le manda la informacion de esa nota (id, titulo, nota,...)
        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return this.deleteNota(n);
    }

    @Override
    public Nota getNota(int position) {//muestra la nota a modificar

        /*String[] projection={ContratoBaseDatos.TablaNota.TITULO};
        Nota n = new Nota();
        String seleccion = ContratoBaseDatos.TablaNota._ID + " = ?";
        String [] argumentos = {n.getId() + ""};*/

        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return n;
    }

    @Override
    public void loadData(OnDataLoadListener listener) {//Muestra las notas en la lista
        //cursor = gn.getCursor();

        cursor = resolver.query(uri, null, null, null, null);
        listener.setCursor(cursor);
    }
}