package ruben.dam.izv.com.proyecto_0.vistas.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dam on 4/11/16.
 */

public class gestionUnion extends Gestion {

    public gestionUnion(Context c) {
        super(c);
    }

    public gestionUnion(Context c, boolean write) {
        super(c, write);
    }

    @Override
    public long insert(Object objeto) {
        return 0;
    }

    @Override
    public long insert(ContentValues objeto) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public int delete(Object objeto) {
        return 0;
    }

    @Override
    public int update(Object objeto) {
        return 0;
    }

    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos) {
        return 0;
    }

    @Override
    public Cursor getCursor() {
        SQLiteDatabase bd= this.getBasedatos();
        String sql = "SELECT * FROM nota UNION ALL SELECT * FROM lista";
        return bd.rawQuery(sql,null);
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return null;
    }
}
