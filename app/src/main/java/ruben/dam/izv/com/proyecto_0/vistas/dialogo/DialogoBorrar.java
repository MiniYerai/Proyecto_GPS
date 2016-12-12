package ruben.dam.izv.com.proyecto_0.vistas.dialogo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

/**
 * Created by Pilar on 26/09/2016.
 */

public class DialogoBorrar extends DialogFragment {
    private Nota n;
    // Interfaz de comunicación
    OnBorrarDialogListener listener;

    public DialogoBorrar() {
    }

    public static DialogoBorrar newInstance(Nota n) {
        DialogoBorrar fragment = new DialogoBorrar();
        Bundle args = new Bundle();
        args.putParcelable("nota",n);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            n=getArguments().getParcelable("nota");
        }
    }
    //MUESTRA UN MENSAJE DE CONFIRMACION DE BORRAR
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialogBorrar();
    }
    public AlertDialog createDialogBorrar() {
        String titulo_dialogo= String.format("%s %s", getString(R.string.etiqueta_dialogo_borrar),n.getTitulo());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo_dialogo);
        builder.setMessage(R.string.mensaje_confirm_borrar);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               listener.onBorrarPossitiveButtonClick(n);//Borra la nota
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onBorrarNegativeButtonClick();//No borra la nota
            }
        });
        AlertDialog alertBorrar = builder.create();
        return alertBorrar;
    }
    public AlertDialog createDialogBorrarImg() {
        String titulo_dialogo= String.format("%s %s", getString(R.string.etiqueta_dialogo_borrar),n.getTitulo());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo_dialogo);
        builder.setMessage(R.string.mensaje_confirm_borrar);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onBorrarPossitiveButtonClick(n);//Borra la nota
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onBorrarNegativeButtonClick();//No borra la nota
            }
        });
        AlertDialog alertBorrar = builder.create();
        return alertBorrar;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnBorrarDialogListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() +
                            " no implementó OnBorrarDialogListener");

        }
    }

}

