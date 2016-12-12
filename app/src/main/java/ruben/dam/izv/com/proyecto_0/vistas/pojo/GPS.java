package ruben.dam.izv.com.proyecto_0.vistas.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by dam on 29/11/16.
 */
@DatabaseTable(tableName = "gps")
public class GPS implements Serializable {

    //Asocia las variables con cada campo de la tabla
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(index = true, canBeNull = false)
    private long nota_id;
    @DatabaseField
    private String titulo;
    @DatabaseField
    private double latitud;
    @DatabaseField
    private double longitud;
    @DatabaseField
    private String fecha;

    //@DatabaseField(persisterClass = /* clase que convierte un objeto Uri a String*/)
    public GPS(){ this(0,"",0,0,"");}

    public GPS(long nota_id, String titulo, double latitud, double longitud, String fecha){
        this.nota_id=nota_id;
        this.titulo=titulo;
        this.latitud=latitud;
        this.longitud=longitud;
        this.fecha=fecha;
    }

    //GETTERS
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public long getNota_id() {
        return nota_id;
    }

    public String getFecha() {
        return fecha;
    }

    //SETTER
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNota_id(long nota_id) {
        this.nota_id = nota_id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "GPS{" +
                "id=" + id +
                ", nota_id=" + nota_id +
                ", titulo='" + titulo + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
