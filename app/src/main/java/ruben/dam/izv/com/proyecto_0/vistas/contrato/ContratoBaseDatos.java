package ruben.dam.izv.com.proyecto_0.vistas.contrato;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContratoBaseDatos {

    //CONSTANTES PROVEEDOR GENERICO
    public final static String AUTORIDAD = "ruben.dam.izv.com.proyecto_0.vistas.contentprovider";
    public final static Uri CONTENT_URI = Uri.parse("content://"+AUTORIDAD);
    public final static Uri CONTENT_URI_NOTA = Uri.withAppendedPath(CONTENT_URI, TablaNota.TABLA);
    public final static Uri CONTENT_URI_LITA = Uri.withAppendedPath(CONTENT_URI, TablaLista.TABLA);

    public final static String BASEDATOS = "quiip.sqlite";

    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;

    private ContratoBaseDatos(){
    }

    public static abstract class TablaNota implements BaseColumns {
        //BaseColumns incluye de forma predeterminada el campo _id
        public static final String TABLA = "nota";
        public static final String TITULO = "titulo";
        public static final String NOTA = "nota";
        public static final String IMAGEN = "imagen";
        public static final String VOZ = "voz";
        public static final String LISTA = "lista";
        public static final String COLOR = "color";
        public static final String FECHA = "fecha";
        public static final String ESTADO = "estado";
        public static final String PENDIENTE_INSERCION = "pendiente_insercion";

        public static final String[] PROJECTION_ALL = {_ID, TITULO, NOTA, IMAGEN, VOZ, LISTA, COLOR, FECHA, ESTADO, PENDIENTE_INSERCION};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";

        //TIPOS MIME
        //CONCRETO
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTORIDAD + "." + TABLA;
        //TODO
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTORIDAD + "." + TABLA;

    }

    public static abstract class TablaLista implements BaseColumns{

        public static final String TABLA = "Lista";
        public static final String ID_NOTA = "id_nota";
        public static final String TEXTO = "texto";

        public static final String[] PROJECTION_lISTA = {_ID, ID_NOTA, TEXTO};
        public static final String SORT_ORDER_DEFAULT_LISTA = _ID + " desc";

        //TIPOS MIME
        //CONCRETO
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTORIDAD + "." + TABLA;
        //TODO
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTORIDAD + "." + TABLA;
    }
}