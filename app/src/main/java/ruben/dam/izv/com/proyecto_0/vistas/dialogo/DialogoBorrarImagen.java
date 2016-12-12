package ruben.dam.izv.com.proyecto_0.vistas.dialogo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

/**
 * Created by Ruben on 20/11/2016.
 */

public class DialogoBorrarImagen extends DialogFragment {

    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public interface OnSetTitleListener{
        void setTitle(String title);
    }

    OnSetTitleListener listener;

    public DialogoBorrarImagen() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSingleListDialog();
    }

    /**
     * Crea un Diálogo con una lista de selección simple
     *
     * @return La instancia del diálogo
     */
    public AlertDialog createSingleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = new CharSequence[3];

        items[0] = "Naranja";
        items[1] = "Mango";
        items[2] = "Banano";

        builder.setTitle("¿Qué fruta te gusta más?")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.setTitle((String) items[which]);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnSetTitleListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " no implementó OnSetTitleListener");

        }
    }
}
