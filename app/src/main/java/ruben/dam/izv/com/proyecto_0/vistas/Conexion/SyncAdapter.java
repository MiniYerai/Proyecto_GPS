package ruben.dam.izv.com.proyecto_0.vistas.Conexion;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoNota;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;
import ruben.dam.izv.com.proyecto_0.vistas.util.Constantes;
import ruben.dam.izv.com.proyecto_0.vistas.util.Utilidades;

/**
 * Created by Ruben on 14/11/2016.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();

    ContentResolver resolver;
    private Gson gson = new Gson();

    /**
     * Proyección para las consultas
     */
    private static final String[] PROJECTION = new String[]{
            ContratoBaseDatos.TablaNota._ID,
            ContratoBaseDatos.TablaNota.TITULO,
            ContratoBaseDatos.TablaNota.NOTA,
            ContratoBaseDatos.TablaNota.IMAGEN,
            ContratoBaseDatos.TablaNota.VOZ,
            ContratoBaseDatos.TablaNota.LISTA,
            ContratoBaseDatos.TablaNota.COLOR,
            ContratoBaseDatos.TablaNota.FECHA
    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_TITULO = 1;
    public static final int COLUMNA_NOTA = 2;
    public static final int COLUMNA_IMAGEN = 3;
    public static final int COLUMNA_VOZ = 4;
    public static final int COLUMNA_LISTA = 5;
    public static final int COLUMNA_COLOR = 6;
    public static final int COLUMNA_FECHA = 7;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        resolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.i(TAG, "onPerformSync()...");

        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (!soloSubida) {
            realizarSincronizacionLocal(syncResult);
        } else {
            realizarSincronizacionRemota();
        }
    }

    public static void inicializarSyncAdapter(Context context) {
        obtenerCuentaASincronizar(context);
    }



    private void realizarSincronizacionLocal(final SyncResult syncResult) {
        Log.i(TAG, "Actualizando el cliente.");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        Constantes.GET_URL,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGet(response, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, error.networkResponse.toString());
                            }
                        }
                )
        );
    }

    /**
     * Procesa la respuesta del servidor al pedir que se retornen todas las notas.
     *
     * @param response   Respuesta en formato Json
     * @param syncResult Registro de resultados de sincronización
     */
    private void procesarRespuestaGet(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocales(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemota() {
        Log.i(TAG, "Actualizando el servidor...");

        iniciarActualizacion();

        Cursor c = obtenerRegistrosSucios();

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID);

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERT_URL,
                                Utilidades.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsert(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.getMessage());
                                    }
                                }

                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();
    }

    /**
     * Obtiene el registro que se acaba de marcar como "pendiente por sincronizar" y
     * con "estado de sincronización"
     *
     * @return Cursor con el registro.
     */
    private Cursor obtenerRegistrosSucios() {
        Uri uri = ContratoBaseDatos.CONTENT_URI_NOTA;
        String selection = ContratoBaseDatos.TablaNota.PENDIENTE_INSERCION + "=? AND "
                + ContratoBaseDatos.TablaNota.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContratoBaseDatos.ESTADO_SYNC + ""};

        return resolver.query(uri, PROJECTION, selection, selectionArgs, null);
    }

    /**
     * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente
     */
    private void iniciarActualizacion() {
        Uri uri = ContratoBaseDatos.CONTENT_URI_NOTA;
        String selection = ContratoBaseDatos.TablaNota.PENDIENTE_INSERCION + "=? AND "
                + ContratoBaseDatos.TablaNota.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContratoBaseDatos.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(ContratoBaseDatos.TablaNota.ESTADO, ContratoBaseDatos.ESTADO_SYNC);

        int results = resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "Registros puestos en cola de inserción:" + results);
    }

    /**
     * Limpia el registro que se sincronizó y le asigna la nueva id remota proveida
     * por el servidor
     *
     * @param idRemota id remota
     */
    private void finalizarActualizacion(String idRemota, int idLocal) {
        Uri uri = ContratoBaseDatos.CONTENT_URI_NOTA;
        String selection = ContratoBaseDatos.TablaNota._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(ContratoBaseDatos.TablaNota.PENDIENTE_INSERCION, "0");
        v.put(ContratoBaseDatos.TablaNota.ESTADO, ContratoBaseDatos.ESTADO_OK);

        resolver.update(uri, v, selection, selectionArgs);
    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en formato Json
     */
    public void procesarRespuestaInsert(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = response.getString(Constantes.ID_NOTA);

            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, mensaje);
                    finalizarActualizacion(idRemota, idLocal);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarDatosLocales(JSONObject response, SyncResult syncResult) {

        JSONArray notas = null;

        try {
            // Obtener array "notas"
            notas = response.getJSONArray(Constantes.NOTAS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Nota[] res = gson.fromJson(notas != null ? notas.toString() : null, Nota[].class);
        List<Nota> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, Nota> expenseMap = new HashMap<String, Nota>();
        for (Nota e : data) {
            expenseMap.put(String.valueOf(e.getId()), e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContratoBaseDatos.CONTENT_URI_NOTA;
        String select = ContratoBaseDatos.TablaNota._ID + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id, titulo, nota, imagen, voz, lista, color, fecha;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(COLUMNA_ID);
            titulo = c.getString(COLUMNA_TITULO);
            nota = c.getString(COLUMNA_NOTA);
            imagen = c.getString(COLUMNA_IMAGEN);
            voz = c.getString(COLUMNA_VOZ);
            lista = c.getString(COLUMNA_LISTA);
            color = c.getString(COLUMNA_COLOR);
            fecha = c.getString(COLUMNA_FECHA);


            Nota match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContratoBaseDatos.CONTENT_URI_NOTA.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b = match.getTitulo() != null && !match.getTitulo().equals(titulo);
                boolean b1 = match.getNota() != null && !match.getNota().equals(nota);
                boolean b2 = match.getImagen() != null && !match.getImagen().equals(imagen);
                boolean b3 = match.getVoz() != null && !match.getVoz().equals(voz);
                boolean b4 = match.getLista() != null && !match.getLista().equals(lista);
                boolean b5 = match.getColor() != null && !match.getColor().equals(color);
                boolean b6 = match.getFecha() != null && !match.getFecha().equals(fecha);

                if (b || b1 || b2 || b3 || b4 || b5 || b6) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContratoBaseDatos.TablaNota.TITULO, match.getTitulo())
                            .withValue(ContratoBaseDatos.TablaNota.NOTA, match.getNota())
                            .withValue(ContratoBaseDatos.TablaNota.IMAGEN, match.getImagen())
                            .withValue(ContratoBaseDatos.TablaNota.VOZ, match.getVoz())
                            .withValue(ContratoBaseDatos.TablaNota.LISTA, match.getLista())
                            .withValue(ContratoBaseDatos.TablaNota.COLOR, match.getColor())
                            .withValue(ContratoBaseDatos.TablaNota.FECHA, match.getFecha())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContratoBaseDatos.CONTENT_URI_NOTA.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Nota e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.getId());
            ops.add(ContentProviderOperation.newInsert(ContratoBaseDatos.CONTENT_URI_NOTA)
                    .withValue(ContratoBaseDatos.TablaNota._ID, e.getId())
                    .withValue(ContratoBaseDatos.TablaNota.TITULO, e.getTitulo())
                    .withValue(ContratoBaseDatos.TablaNota.NOTA, e.getNota())
                    .withValue(ContratoBaseDatos.TablaNota.IMAGEN, e.getImagen())
                    .withValue(ContratoBaseDatos.TablaNota.VOZ, e.getVoz())
                    .withValue(ContratoBaseDatos.TablaNota.LISTA, e.getLista())
                    .withValue(ContratoBaseDatos.TablaNota.COLOR, e.getColor())
                    .withValue(ContratoBaseDatos.TablaNota.FECHA, e.getFecha())
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(ContratoBaseDatos.AUTORIDAD, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContratoBaseDatos.CONTENT_URI_NOTA,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Inicia manualmente la sincronización
     *
     * @param context    Contexto para crear la petición de sincronización
     * @param onlyUpload Usa true para sincronizar el servidor o false para sincronizar el cliente
     */
    public static void sincronizarAhora(Context context, boolean onlyUpload) {
        Log.i(TAG, "Realizando petición de sincronización manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(obtenerCuentaASincronizar(context),
                context.getString(R.string.provider_authority), bundle);
    }

    /**
     * Crea u obtiene una cuenta existente
     *
     * @param context Contexto para acceder al administrador de cuentas
     * @return cuenta auxiliar.
     */
    public static Account obtenerCuentaASincronizar(Context context) {
        // Obtener instancia del administrador de cuentas
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Crear cuenta por defecto
        Account newAccount = new Account(
                context.getString(R.string.app_name), Constantes.ACCOUNT_TYPE);

        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(newAccount)) {

            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

        }
        Log.i(TAG, "Cuenta de usuario obtenida.");
        return newAccount;
    }
}