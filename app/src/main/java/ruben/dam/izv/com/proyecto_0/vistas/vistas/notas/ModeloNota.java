package ruben.dam.izv.com.proyecto_0.vistas.vistas.notas;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoNota;
import ruben.dam.izv.com.proyecto_0.vistas.gestion.GestionNota;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

public class ModeloNota implements ContratoNota.InterfaceModelo {

    private GestionNota gn = null;
    private ContentResolver resolver;
    private Cursor cursor;
    private Uri uri = ContratoBaseDatos.CONTENT_URI_NOTA;

    public ModeloNota(Context c) {
        gn = new GestionNota(c);
        this.resolver=c.getContentResolver();
    }

    @Override
    public void close() {
        gn.close();
        cursor.close();
    }

    @Override
    public Nota getNota(long id) {
        return gn.get(id);
    }

    @Override
    public long saveNota(Nota n) {//inserta o modifica la nota
        long r;
        if(n.getId()==0) {
             r = this.insertNota(n);//inserta la nota
        } else {
            r = this.updateNota(n);//modifica la nota
        }
        return r;
    }

    private long deleteNota(Nota n) {

        String where= ContratoBaseDatos.TablaNota._ID + " = ?";
        String[] arguments = {n.getId() + ""};
        return this.resolver.delete(uri,where, arguments);
        //return gn.delete(n);
    }

    private long insertNota(Nota n) {
        if(n.getNota().trim().compareTo("")==0 && n.getTitulo().trim().compareTo("")==0) {
            return 0;
        }
        ContentValues valores=n.getContentValues();

        return Long.parseLong(this.resolver.insert(uri,n.getContentValues()).getLastPathSegment());

        //return gn.insert(n);
    }

    private long updateNota(Nota n) {
        if(n.getNota().trim().compareTo("")==0 && n.getTitulo().trim().compareTo("")==0) {
            this.deleteNota(n);

            String where = ContratoBaseDatos.TablaNota._ID + " = ?";
            String [] arguments = {n.getId() + ""};
            resolver.delete(uri,where,arguments);
            //gn.delete(n);
            return 0;
        }
        String where = ContratoBaseDatos.TablaNota._ID + " = ?";
        String [] arguments = {n.getId() + ""};
        return resolver.update(uri,n.getContentValues(),where,arguments);
        //return gn.update(n);
    }
}