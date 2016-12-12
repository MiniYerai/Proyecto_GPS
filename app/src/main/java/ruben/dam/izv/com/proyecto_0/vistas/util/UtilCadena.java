package ruben.dam.izv.com.proyecto_0.vistas.util;

/**
 * Created by Ruben on 29/10/2016.
 */

public class UtilCadena {

    public static String getCondiciones (String CondicionesIni, String NewCondicion){

        return getCondiciones(CondicionesIni, NewCondicion, "and");
    }

    public static String getCondiciones(String CondicionesIni, String NewCondicion, String Conector){

        if(CondicionesIni==null || CondicionesIni.trim().length() == 0){
            return NewCondicion;
        }

        return CondicionesIni + " " + Conector + " " + NewCondicion;
    }

    public static String[] getNewArray(String[] ArrayInicial, String parametro){
        String[] newArray;

        if(ArrayInicial == null){
            return new String[]{parametro};
        }

        newArray = new String[ArrayInicial.length + 1];

        for (int i = 0; i< ArrayInicial.length; i++){
            newArray[i]=ArrayInicial[i];
        }

        newArray[ArrayInicial.length] = parametro;

        return newArray;
    }
}
