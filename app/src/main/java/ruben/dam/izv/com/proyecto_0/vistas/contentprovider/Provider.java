package ruben.dam.izv.com.proyecto_0.vistas.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;
import ruben.dam.izv.com.proyecto_0.vistas.gestion.GestionLista;
import ruben.dam.izv.com.proyecto_0.vistas.gestion.GestionNota;
import ruben.dam.izv.com.proyecto_0.vistas.util.UtilCadena;


/**
 * Created by Ruben on 29/10/2016.
 */

public class Provider extends ContentProvider {

    //URI_MATCHER
    private static final UriMatcher URI_MATCHER;

    //CURSORES
    private static final int TODO_NOTA = 0;
    private static final int CONCRETO_NOTA = 1;
    private static final int TODO_LISTA = 2;
    private static final int CONCRETO_LISTA = 3;

    //GESTORES
    private static GestionNota gn;
    private static GestionLista gl;


    //CONSTRUCTOR ESTATICO
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        //CURSOR 0 TABLA NOTAS
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaNota.TABLA, TODO_NOTA);
        //CURSOR 1
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaNota.TABLA + "/#", CONCRETO_NOTA);
        //CURSOR 2 TABLA LISTAS
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD,ContratoBaseDatos.TablaLista.TABLA, TODO_LISTA);
        //CURSOR 3
        URI_MATCHER.addURI(ContratoBaseDatos.AUTORIDAD, ContratoBaseDatos.TablaLista.TABLA + "/#", CONCRETO_LISTA);
    }

    public Provider(){}

    @Override
    public boolean onCreate() {
        gn = new GestionNota(getContext());
        gl = new GestionLista(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;

        int tipo =URI_MATCHER.match(uri);
        String id;
        String tabla;

        if(tipo<0){
            throw new IllegalArgumentException("Error no devuelve QUERY");
        }
        if(tipo == CONCRETO_NOTA){
            id = uri.getLastPathSegment();
            selection = UtilCadena.getCondiciones(selection,ContratoBaseDatos.TablaNota._ID + " = ?");
            selectionArgs = UtilCadena.getNewArray(selectionArgs, id);
            tabla =ContratoBaseDatos.TablaNota.TABLA;
        }else if(tipo == CONCRETO_LISTA){
            id = uri.getLastPathSegment();
            selection = UtilCadena.getCondiciones(selection,ContratoBaseDatos.TablaLista._ID + " = ?");
            selectionArgs = UtilCadena.getNewArray(selectionArgs, id);
            tabla =ContratoBaseDatos.TablaLista.TABLA;
        }else if(tipo == TODO_NOTA){
            selection = null;
            selectionArgs = null;
            tabla =ContratoBaseDatos.TablaNota.TABLA;

        }else{
            throw new IllegalArgumentException("URI no soportada: " + uri);
        }

        c = gn.getCursor(tabla,projection,selection,selectionArgs,null,null,ContratoBaseDatos.TablaLista._ID + " DESC");
        c.setNotificationUri(getContext().getContentResolver(),uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (URI_MATCHER.match(uri)){
            case TODO_NOTA: return ContratoBaseDatos.TablaNota.CONTENT_TYPE;
            case TODO_LISTA: return ContratoBaseDatos.TablaLista.CONTENT_TYPE;
            case CONCRETO_NOTA: return ContratoBaseDatos.TablaNota.CONTENT_ITEM_TYPE;
            case CONCRETO_LISTA: return ContratoBaseDatos.TablaLista.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Tipo de actividad desconocida: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int tipo = URI_MATCHER.match(uri);
        long id = 0;

        switch (tipo){
            case TODO_NOTA: id = gn.insert(values); break;
            case TODO_LISTA: id = gl.insert(values); break;
            //default: throw  new IllegalArgumentException("Error de inserción Uri desconocida: " + uri);
        }

        if( id > 0 ){
            Uri uriGelle = ContentUris.withAppendedId(uri,id);
            getContext().getContentResolver().notifyChange(uriGelle,null);
            return uriGelle;
        }
        throw new IllegalArgumentException("Error de insercion de fila en: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int tipo = URI_MATCHER.match(uri);
        int borrados = 0;
        String id;
        String newSelection;

        //gestor = new GestionNota(getContext());

        switch (tipo){

            case CONCRETO_NOTA:

                id = uri.getLastPathSegment();
                borrados = gn.delete(ContratoBaseDatos.TablaNota._ID + " = ?", new String[]{id});
                //newSelection = UtilCadena.getCondiciones(selection, ContratoBaseDatos.TablaNota._ID + " = ?");
            break;

            case  CONCRETO_LISTA:

                id = uri.getLastPathSegment();
                borrados = gn.delete(ContratoBaseDatos.TablaLista._ID + " = ?", new String[]{id});
                //newSelection = UtilCadena.getCondiciones(selection,ContratoBaseDatos.TablaLista._ID + " = ?");
            break;

            case TODO_NOTA:

                borrados = gn.delete(selection, selectionArgs);
            break;

            case TODO_LISTA:

                borrados = gl.delete(selection, selectionArgs);
            break;
            default:
                throw new IllegalArgumentException("Elemento actividad desconocido: " + uri);
        }

        if(borrados > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return borrados;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int tipo = URI_MATCHER.match(uri);

        String id= uri.getLastPathSegment();

        if (tipo<0){
            throw new IllegalArgumentException("Error de UPDATE: " + uri);
        }else if (tipo == CONCRETO_NOTA){
            selection = UtilCadena.getCondiciones(selection,ContratoBaseDatos.TablaNota._ID + " = ?");
            selectionArgs = UtilCadena.getNewArray(selectionArgs, id);
        }else if(tipo == CONCRETO_LISTA){
            selection = UtilCadena.getCondiciones(selection,ContratoBaseDatos.TablaNota._ID + " = ?");
            selectionArgs = UtilCadena.getNewArray(selectionArgs, id);
        }
        int valor = gn.update(values,selection,selectionArgs);
        if(valor > 0){
            getContext().getContentResolver().notifyChange(uri,null);

        }
        return valor;
    }
}
