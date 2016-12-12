package ruben.dam.izv.com.proyecto_0.vistas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilFecha {

    public static long dateToLong(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
            Date fecha = sdf.parse(date);
            return fecha.getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public static String formatDate(Date date) {
        return formatStringDate("d MMMM yyyy hh:mm:ss", date);
    }
    public static String formatDate2(Date date) {
        return formatStringDate("dd/MM/yyyy HH:mm:ss", date);
    }

    public static String formatStringDate(String formatStr, Date date) {
        if (date == null) {
            return null;
        }
        return android.text.format.DateFormat.format(formatStr, date).toString();
    }

}