package ruben.dam.izv.com.proyecto_0.vistas.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;

public class Ayudante extends SQLiteOpenHelper {

    //sqlite
    //tipos de datos https://www.sqlite.org/datatype3.html
    //fechas https://www.sqlite.org/lang_datefunc.html
    //trigger https://www.sqlite.org/lang_createtrigger.html

    private static final int VERSION = 3;
    private static final String DATABASE_ALTER = "ALTER TABLE "
            + ContratoBaseDatos.TablaNota.TABLA + " ADD COLUMN " + ContratoBaseDatos.TablaNota.IMAGEN + " text," +
            ContratoBaseDatos.TablaNota.VOZ + " text, " + ContratoBaseDatos.TablaNota.LISTA + " integer, " +
            ContratoBaseDatos.TablaNota.COLOR + " text, " +
            ContratoBaseDatos.TablaNota.FECHA + " text;";
    private static final String DATABASE_ALTER2 = "ALTER TABLE " +
            ContratoBaseDatos.TablaNota.TABLA + " ADD COLUMN " + ContratoBaseDatos.TablaNota.ESTADO +" text; ";
    private static final String DATABASE_ALTER3 = "ALTER TABLE " +
            ContratoBaseDatos.TablaNota.TABLA + " ADD COLUMN " + ContratoBaseDatos.TablaNota.PENDIENTE_INSERCION + " integer NOT NULL DEFAULT 0;";
    private static final String TABLA_TEMPORAL = "CREATE TEMP TABLE tablaTemp (" +
            ContratoBaseDatos.TablaNota._ID + " integer primary key autoincrement, " +
            ContratoBaseDatos.TablaNota.TITULO + " text, " +
            ContratoBaseDatos.TablaNota.NOTA + " text " +
            ");";
    private static final String TABLA_TEMPORAL2 = "CREATE TEMP TABLE tablaTemp2 (" +
            ContratoBaseDatos.TablaNota._ID + " integer primary key autoincrement, " +
            ContratoBaseDatos.TablaNota.TITULO + " text, " +
            ContratoBaseDatos.TablaNota.NOTA + " text " +
            ContratoBaseDatos.TablaNota.IMAGEN + " text, " +
            ContratoBaseDatos.TablaNota.VOZ + " text, " +
            ContratoBaseDatos.TablaNota.LISTA + " integer, " +
            ContratoBaseDatos.TablaNota.COLOR + " text, " +
            ContratoBaseDatos.TablaNota.FECHA + " text, " +
            ContratoBaseDatos.TablaNota.ESTADO + " text DEFAULT"+ ContratoBaseDatos.ESTADO_OK+"," +
            ContratoBaseDatos.TablaNota.PENDIENTE_INSERCION + " integer NOT NULL DEFAULT 0 " +
            ");";
    private static final String INSERTAR_TEMP = "INSERT INTO tablaTemp SELECT * FROM " +
            ContratoBaseDatos.TablaNota.TABLA;
    private static final String INSERTAR_TEMP2 = "INSERT INTO tablaTemp2 SELECT * FROM " +
            ContratoBaseDatos.TablaNota.TABLA;
    private static final String INSERTAR_DATOS = "INSERT INTO " + ContratoBaseDatos.TablaNota.TABLA + " SELECT " +
            ContratoBaseDatos.TablaNota._ID + ", " +
            ContratoBaseDatos.TablaNota.TITULO + ", " +
            ContratoBaseDatos.TablaNota.NOTA + ", " +
            "null , null, null, null, null, null, null " +
            " FROM tablaTemp";
    private static final String INSERTAR_DATOS2 = "INSERT INTO " + ContratoBaseDatos.TablaNota.TABLA + " SELECT " +
            ContratoBaseDatos.TablaNota._ID + ", " +
            ContratoBaseDatos.TablaNota.TITULO + ", " +
            ContratoBaseDatos.TablaNota.NOTA + ", " +
            ContratoBaseDatos.TablaNota.IMAGEN + ", " +
            ContratoBaseDatos.TablaNota.VOZ + ", " +
            ContratoBaseDatos.TablaNota.LISTA + ", " +
            ContratoBaseDatos.TablaNota.COLOR + ", " +
            ContratoBaseDatos.TablaNota.FECHA + ", " +
            ContratoBaseDatos.TablaNota.ESTADO + "," +
            ContratoBaseDatos.TablaNota.PENDIENTE_INSERCION + ", " +
            "null, null" + " FROM tablaTemp2";
    private static final String DROPTABLE = "DROP TABLE if exists " + ContratoBaseDatos.TablaNota.TABLA;
    private static final String DROPTABLETEMP = "DROP TABLE if exists tablaTemp";
    private static final String DROPTABLETEMP2 = "DROP TABLE if exists tablaTemp2";

    public Ayudante(Context context) {
        super(context, ContratoBaseDatos.BASEDATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//crea la base de datos
        String sql;
        sql = "create table if not exists " + ContratoBaseDatos.TablaNota.TABLA +
                " (" +
                ContratoBaseDatos.TablaNota._ID + " integer primary key autoincrement , " +
                ContratoBaseDatos.TablaNota.TITULO + " text, " +
                ContratoBaseDatos.TablaNota.NOTA + " text, " +
                ContratoBaseDatos.TablaNota.IMAGEN + " text, " +
                ContratoBaseDatos.TablaNota.VOZ + " text, " +
                ContratoBaseDatos.TablaNota.LISTA + " integer, " +
                ContratoBaseDatos.TablaNota.COLOR + " text, " +
                ContratoBaseDatos.TablaNota.FECHA + " text, " +
                ContratoBaseDatos.TablaNota.ESTADO + " text DEFAULT" + ContratoBaseDatos.ESTADO_OK + ", " +
                ContratoBaseDatos.TablaNota.PENDIENTE_INSERCION + " integer NOT NULL DEFAULT 0 " +
                ");";


        Log.v("sql", sql);
        db.execSQL(sql);

        sql = "create table if not exists " + ContratoBaseDatos.TablaLista.TABLA +
                " (" + ContratoBaseDatos.TablaLista._ID + " integer primary key autoincrement, " +
                ContratoBaseDatos.TablaLista.ID_NOTA + " integer, " +
                ContratoBaseDatos.TablaLista.TEXTO + " text " + ")";
        Log.v("sqlLista", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//modifica la base de datos

        if(oldVersion<2){

            db.execSQL(TABLA_TEMPORAL);
            db.execSQL(INSERTAR_TEMP);
            db.execSQL(DROPTABLE);
            onCreate(db);
            db.execSQL(INSERTAR_DATOS);
            db.execSQL(DROPTABLETEMP);

            //db.execSQL(DATABASE_ALTER);
            //db.execSQL(DATABASE_ALTER2);
            //db.execSQL(DATABASE_ALTER3);
            //db.execSQL(DATABASE_ALTERPrueba);
        }else if(oldVersion<3){
            db.execSQL(TABLA_TEMPORAL2);
            db.execSQL(INSERTAR_TEMP2);
            db.execSQL(DROPTABLE);
            onCreate(db);
            db.execSQL(INSERTAR_DATOS2);
            db.execSQL(DROPTABLETEMP2);
        }
        /*String sql="drop table if exists " + ContratoBaseDatos.TablaNota.TABLA;
        db.execSQL(sql);
        sql=
        db.execSQL(sql);
        */
        //Log.v("sql",sql);
        //LO SUYO ES GUARDAR LOS DATOS Y ACTUALIZAR LA TABLA
        //PASOS:
        //CREAR TABLA TEMPORAL
        //PASAR LOS DATOS A LA TABLA TEMPORAL
        //BORRAR TABLA VIEJA
        //CREAR TABLA NUEVA
        //LLEVAR LOS DATOS DE LA TABLA TEMPORAL A LA TABLA NUEVA
        //BORRAR LA TABLA TEMPORAL
    }
}