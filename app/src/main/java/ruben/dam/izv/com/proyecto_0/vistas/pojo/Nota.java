package ruben.dam.izv.com.proyecto_0.vistas.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;

public class Nota implements Parcelable {

    public static final Creator<Nota> CREATOR = new Creator<Nota>() {
        @Override
        public Nota createFromParcel(Parcel in) {
            return new Nota(in);
        }

        @Override
        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };
    private long id;
    private String titulo, nota, imagen, voz, color, fecha;
    private Integer lista;

    public Nota() {
        this(0, null, null, null, null, null, null, null);
    }

    public Nota(long id, String titulo, String nota, String imagen, String voz, Integer lista, String color, String fecha) {
        this.id = id;
        this.titulo = titulo;
        this.nota = nota;
        this.imagen = imagen;
        this.voz = voz;
        this.lista = lista;
        this.color = color;
        this.fecha = fecha;
    }

    protected Nota(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        nota = in.readString();
        imagen = in.readString();
        voz = in.readString();
        lista = in.readInt();
        color = in.readString();
        fecha = in.readString();
    }

    public static Creator<Nota> getCREATOR() {
        return CREATOR;
    }

    public static Nota getNota(Cursor c) {
        Nota objeto = new Nota();

        objeto.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaNota._ID)));
        objeto.setTitulo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TITULO)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.NOTA)));
        objeto.setImagen(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.IMAGEN)));
        objeto.setVoz(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.VOZ)));
        objeto.setLista(c.getInt(c.getColumnIndex(String.valueOf(ContratoBaseDatos.TablaNota.LISTA))));
        objeto.setColor(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.COLOR)));
        objeto.setFecha(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.FECHA)));

        return objeto;
    }

    public long getId() {
        return id;
    }

    public void setId(String id) {
        try {
            this.id = Long.parseLong(id);
        } catch(NumberFormatException e){
            this.id = 0;
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getLista() {
        return lista;
    }

    public void setLista(Integer lista) {
        this.lista = lista;
    }

    public String getTitulo() {
        return titulo;
    }//devuelve titulo

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNota() {
        return nota;
    }//devuelve nota

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getImagen() {
        return imagen;
    }//devuelve la ruta de la imagen

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVoz() {
        return "voz";
    }//devuelve la ruta del audio

    public void setVoz(String voz) {
        this.voz = voz;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ContentValues getContentValues(){
        return this.getContentValues(false);
    }

    public ContentValues getContentValues(boolean withId){
        ContentValues valores = new ContentValues();
        if(withId){
            valores.put(ContratoBaseDatos.TablaNota._ID, this.getId());
        }
        valores.put(ContratoBaseDatos.TablaNota.TITULO, this.getTitulo());
        valores.put(ContratoBaseDatos.TablaNota.NOTA, this.getNota());
        valores.put(ContratoBaseDatos.TablaNota.IMAGEN, this.getImagen());
        valores.put(ContratoBaseDatos.TablaNota.VOZ, this.getVoz());
        valores.put(String.valueOf(ContratoBaseDatos.TablaNota.LISTA),this.getLista());
        valores.put(ContratoBaseDatos.TablaNota.COLOR, this.getColor());
        valores.put(ContratoBaseDatos.TablaNota.FECHA,this.getFecha());

        return valores;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nota='" + nota + '\'' +
                ", imagen='" + imagen + '\'' +
                ", voz='" + voz + '\''+
                ", lista=" + lista +
                ", color='" + color + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(nota);
        dest.writeString(imagen);
        dest.writeString(voz);
        dest.writeInt(lista);
        dest.writeString(color);
        dest.writeString(fecha);
    }
}