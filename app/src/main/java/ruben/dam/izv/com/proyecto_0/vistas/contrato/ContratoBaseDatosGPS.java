package ruben.dam.izv.com.proyecto_0.vistas.contrato;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatosGPS.LATITUD;
import static ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatosGPS.LONGITUD;

/**
 * Created by dam on 29/11/16.
 */
@DatabaseTable
public class ContratoBaseDatosGPS {

// Nombre de la base de datos
    public final static String BASEDATOS = "quiip.sqlite";

    //nombre de las columnas (constantes por cada campo y por cada variable de la clase)
    public static final String ID = "_id";
    public static final String TITULO = "titulo";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";

    //Asocia las variables con cada campo de la tabla
    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = TITULO)
    private String titulo;
    @DatabaseField(columnName = LATITUD)
    private String latitud;
    @DatabaseField(columnName = LONGITUD)
    private String longitud;

    //Constructor
    public ContratoBaseDatosGPS(){}

    //GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
