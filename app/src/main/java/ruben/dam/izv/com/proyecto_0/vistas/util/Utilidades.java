package ruben.dam.izv.com.proyecto_0.vistas.util;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;

public class Utilidades {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_TITULO = 1;
    public static final int COLUMNA_NOTA = 2;
    public static final int COLUMNA_IMAGEN = 3;
    public static final int COLUMNA_VOZ = 4;
    public static final int COLUMNA_LISTA = 5;
    public static final int COLUMNA_COLOR = 6;
    public static final int COLUMNA_FECHA = 7;

    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Copia los datos de una nota almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String titulo, nota, imagen, voz, lista, color, fecha;

        titulo = c.getString(COLUMNA_TITULO);
        nota = c.getString(COLUMNA_NOTA);
        imagen = c.getString(COLUMNA_IMAGEN);
        voz = c.getString(COLUMNA_VOZ);
        lista = c.getString(COLUMNA_LISTA);
        color = c.getString(COLUMNA_COLOR);
        fecha = c.getString(COLUMNA_FECHA);

        try {
            jObject.put(ContratoBaseDatos.TablaNota.TITULO, titulo);
            jObject.put(ContratoBaseDatos.TablaNota.NOTA, nota);
            jObject.put(ContratoBaseDatos.TablaNota.IMAGEN, imagen);
            jObject.put(ContratoBaseDatos.TablaNota.VOZ, voz);
            jObject.put(ContratoBaseDatos.TablaNota.LISTA, lista);
            jObject.put(ContratoBaseDatos.TablaNota.COLOR, color);
            jObject.put(ContratoBaseDatos.TablaNota.FECHA, fecha);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }
}
