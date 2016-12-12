package ruben.dam.izv.com.proyecto_0.vistas.dialogo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ruben.dam.izv.com.proyecto_0.R;

/**
 * Created by dam on 1/12/16.
 */

public class DialogoImagenCompleta extends DialogFragment {

    private static final String TAG = DialogoColor.class.getSimpleName();

    private Bitmap bit;

    public DialogoImagenCompleta(Bitmap bit) {

        this.bit=bit;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialogoImagen();
    }

    public AlertDialog createDialogoImagen() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.vista_completa, null);

        builder.setView(v);

        ImageView iv = (ImageView) v.findViewById(R.id.ivCompleta);
        Log.v(TAG,"RUTA: " + bit);
        iv.setImageBitmap(bit);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    public void show(FragmentManager ft, String dialogoImagenCompleta) {
    }

}
