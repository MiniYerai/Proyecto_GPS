package ruben.dam.izv.com.proyecto_0.vistas.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ruben.dam.izv.com.proyecto_0.vistas.pojo.GPS;

/**
 * Created by dam on 29/11/16.
 */

public class AyudanteGPS extends OrmLiteSqliteOpenHelper {

    private final static String BASEDATOS = "GPS";

    private static final int VERSION = 1;

    // Objetos DAO que se utilizaran para acceder a la tabla GPS
    private Dao<GPS, Integer> GPSDao = null;
    private RuntimeExceptionDao<GPS, Integer> GPSRuntimeDao = null;

    public AyudanteGPS (Context c){

        super(c, BASEDATOS, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {

            Log.i(AyudanteGPS.class.getSimpleName(), "onCreate()");

            TableUtils.createTable(connectionSource, GPS.class);

        } catch (SQLException e) {
            Log.e(AyudanteGPS.class.getSimpleName()," Error al crear la base de datos 1", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            Log.i(AyudanteGPS.class.getSimpleName(), "onUpgrade()");

            TableUtils.dropTable(connectionSource, GPS.class, true);

            onCreate(database, connectionSource);
        }catch(SQLException e){
            Log.e(AyudanteGPS.class.getSimpleName(),"Error no se pudo eliminar la base de datos", e);
            throw new RuntimeExecutionException(e);
        }
    }

    public Dao<GPS, Integer> getGPSDao() throws SQLException, java.sql.SQLException {

        //Si recibe un null carga las clases del GPS
        if (GPSDao == null){
            GPSDao = getDao(GPS.class);
        }

        //Devuelve las classes
        return GPSDao;
    }

    public RuntimeExceptionDao<GPS, Integer> getGPSRuntimeDAO() {
        if (GPSRuntimeDao == null){
            GPSRuntimeDao = getRuntimeExceptionDao(GPS.class);
        }
        return GPSRuntimeDao;
    }

    //cierra la conexion
    public void close() {
        super.close();
        GPSDao = null;
        GPSRuntimeDao = null;
    }
}
