package ruben.dam.izv.com.proyecto_0.vistas.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;

/*
 * Created by Ruben on 19/10/2016.
 */

public class Lista implements Parcelable {

    private long id, id_nota;
    private String texto;

    //CREADOR POR DEFECTO
    public Lista(){ this(0,0,null);}

    public Lista(long id, long id_nota, String texto){
        this.id=id;
        this.id_nota=id_nota;
        this.texto=texto;
    }
    //PARCELABLE
    protected Lista(Parcel in) {
        id = in.readLong();
        id_nota = in.readLong();
        texto = in.readString();
    }

    public static final Creator<Lista> CREATOR = new Creator<Lista>() {
        @Override
        public Lista createFromParcel(Parcel in) {
            return new Lista(in);
        }

        @Override
        public Lista[] newArray(int size) {
            return new Lista[size];
        }
    };

    //GETS Y SETS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_nota() {
        return id_nota;
    }

    public void setId_nota(long id_nota) {
        this.id_nota = id_nota;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public static Creator<Lista> getCREATOR() {
        return CREATOR;
    }

    public ContentValues getCV(){ return this.getCV(false);}

    public ContentValues getCV(boolean withId){
        ContentValues valores = new ContentValues();

        if(withId){
            valores.put(ContratoBaseDatos.TablaLista._ID, this.getId());
        }

        valores.put(ContratoBaseDatos.TablaLista.ID_NOTA, this.getId_nota());
        valores.put(ContratoBaseDatos.TablaLista.TEXTO, this.getTexto());

        return valores;
    }

    public static Lista getLista(Cursor c){
        Lista objeto = new Lista();

        objeto.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista._ID)));
        objeto.setId_nota(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista.ID_NOTA)));
        objeto.setTexto(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaLista.TEXTO)));

        return objeto;
    }

    public String toString(){

        return "Lista{" + "id = " + id + ", id_nota = " + id_nota + ", texto = '" + texto + '\'' + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeLong(id_nota);
        dest.writeString(texto);
    }
}
