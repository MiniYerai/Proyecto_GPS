package ruben.dam.izv.com.proyecto_0.vistas.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Lista;

/**
 * Created by Ruben on 03/11/2016.
 */

public class GestionLista extends Gestion<Lista> {

    public GestionLista(Context c) {
        super(c);
    }

    public GestionLista(Context c, boolean write) {
        super(c, write);
    }

    @Override
    public long insert(Lista objeto) {
        return this.insert(ContratoBaseDatos.TablaLista.TABLA, objeto.getCV());
    }

    @Override
    public long insert(ContentValues objeto) {
        return this.insert(ContratoBaseDatos.TablaLista.TABLA, objeto);
    }

    @Override
    public int deleteAll() {
        return this.deleteAll(ContratoBaseDatos.TablaLista.TABLA);
    }

    public int delete(String condicion, String[] argumentos){
        return this.delete(ContratoBaseDatos.TablaLista.TABLA, condicion, argumentos);
    }

    @Override
    public int delete(Lista objeto) {
        String condicion = ContratoBaseDatos.TablaLista._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        return this.delete(ContratoBaseDatos.TablaLista.TABLA, condicion, argumentos);
    }

    public Lista get(long id){
        String where = ContratoBaseDatos.TablaLista._ID + " = ? ";
        String[] parametros = {id + ""};
        Cursor c = this.getCursor(ContratoBaseDatos.TablaLista.PROJECTION_lISTA, where, parametros, null, null, ContratoBaseDatos.TablaLista.SORT_ORDER_DEFAULT_LISTA);
        if(c.getCount() > 0) {
            c.moveToFirst();
            Lista lista = Lista.getLista(c);
            return lista;
        }
        return null;
    }

    @Override
    public int update(Lista objeto) {
        ContentValues valores = objeto.getCV();
        String condicion = ContratoBaseDatos.TablaLista._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        return this.update(ContratoBaseDatos.TablaLista.TABLA, valores, condicion, argumentos);
    }

    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos) {
        return this.update(ContratoBaseDatos.TablaLista.TABLA, valores, condicion, argumentos);
    }

    @Override
    public Cursor getCursor() {
        return this.getCursor(ContratoBaseDatos.TablaLista.TABLA, ContratoBaseDatos.TablaLista.PROJECTION_lISTA, ContratoBaseDatos.TablaLista.SORT_ORDER_DEFAULT_LISTA);
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.getCursor(ContratoBaseDatos.TablaLista.TABLA, columns, selection, selectionArgs, groupBy, having, orderBy);
    }
}
