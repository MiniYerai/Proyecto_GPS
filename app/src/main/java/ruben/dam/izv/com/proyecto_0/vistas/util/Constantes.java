package ruben.dam.izv.com.proyecto_0.vistas.util;

/**
 * Constantes
 */
public class Constantes {

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta característica.
     */
    //private static final String PUERTO_HOST = ":3306";

    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "http://localhost";//192.168.208.139

    /**
     * URLs del Web Service
     PUERTO_HOST*/
    public static final String GET_URL = IP + "/Sincronizacion_Datos/web/obtener_notas.php";
    public static final String INSERT_URL = IP + "/Sincronizacion_Datos/web/insertar_notas.php";

    /**
     * Campos de las respuestas Json
     */
    public static final String ID_NOTA = "idNota";
    public static final String ESTADO = "estado";
    public static final String NOTAS = "notas";
    public static final String MENSAJE = "mensaje";


    //Códigos del campo {@link ESTADO}
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    /**
     * Tipo de cuenta para la sincronización
     */
    public static final String ACCOUNT_TYPE = "ruben.dam.izv.com.proyecto_0.vistas.account";


}
