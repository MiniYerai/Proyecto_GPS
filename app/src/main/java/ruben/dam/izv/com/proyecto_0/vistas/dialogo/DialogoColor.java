package ruben.dam.izv.com.proyecto_0.vistas.dialogo;

import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import harmony.java.awt.Color;
import ruben.dam.izv.com.proyecto_0.R;

/**
 * Created by dam on 23/11/16.
 */

public class DialogoColor extends DialogFragment implements View.OnClickListener{

    private static final String TAG = DialogoColor.class.getSimpleName();

    private EditText titulo, nota;

    private TextView color;

    public DialogoColor(EditText titulo, EditText nota, TextView color) {

        this.titulo = titulo;
        this.nota = nota;
        this.color = color;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialogoColor();
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog createDialogoColor() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_color, null);

        builder.setView(v);

        FloatingActionButton morado = (FloatingActionButton) v.findViewById(R.id.morado);
        FloatingActionButton magenta = (FloatingActionButton) v.findViewById(R.id.magenta);
        FloatingActionButton verde = (FloatingActionButton) v.findViewById(R.id.verde);
        FloatingActionButton azul = (FloatingActionButton) v.findViewById(R.id.azul);
        FloatingActionButton gris = (FloatingActionButton) v.findViewById(R.id.gris);
        FloatingActionButton azul_oscuro = (FloatingActionButton) v.findViewById(R.id.azul_oscuro);
        FloatingActionButton blanco = (FloatingActionButton) v.findViewById(R.id.blanco);
        FloatingActionButton rojo = (FloatingActionButton) v.findViewById(R.id.rojo);
        FloatingActionButton naranja = (FloatingActionButton) v.findViewById(R.id.naranja);

        //nota = new VistaNota().findViewById(R.id.llcolor);
        //View nota = inflater.inflate(R.layout.activity_nota, null);


        //ll = (LinearLayout) nota.findViewById(R.id.llcolor);
        morado.setOnClickListener(DialogoColor.this);
        magenta.setOnClickListener(DialogoColor.this);
        verde.setOnClickListener(DialogoColor.this);
        azul.setOnClickListener(DialogoColor.this);
        gris.setOnClickListener(DialogoColor.this);
        azul_oscuro.setOnClickListener(DialogoColor.this);
        blanco.setOnClickListener(DialogoColor.this);
        rojo.setOnClickListener(DialogoColor.this);
        naranja.setOnClickListener(DialogoColor.this);

       /* morado.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titulo.setBackgroundResource(R.color.morado);
                        titulo.setTextColor(Color.WHITE.getRGB());

                        Toast.makeText(getActivity(), "MORADO", Toast.LENGTH_SHORT).show();
                        //dismiss();
                    }
                }
        );

        azul.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titulo.setBackgroundResource(R.color.azul);
                        titulo.setTextColor(Color.WHITE.getRGB());

                        Toast.makeText(getActivity(), "verde", Toast.LENGTH_SHORT).show();
                        //dismiss();
                    }
                }

        );*/

        return builder.create();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rojo:
                titulo.setBackgroundResource(R.color.rojo);
                titulo.setTextColor(Color.WHITE.getRGB());
                color.setText("rojo");
                dismiss();
                break;
            case R.id.azul:
                titulo.setBackgroundResource(R.color.azul);
                titulo.setTextColor(Color.BLACK.getRGB());
                color.setText("azul");
                dismiss();
                break;
            case R.id.verde:
                titulo.setBackgroundResource(R.color.verde);
                titulo.setTextColor(Color.WHITE.getRGB());
                color.setText("verde");
                dismiss();
                break;
            case R.id.morado:
                titulo.setBackgroundResource(R.color.morado);
                titulo.setTextColor(Color.WHITE.getRGB());
                color.setText("morado");
                dismiss();
                //Toast.makeText(getActivity(), "MORADO", Toast.LENGTH_SHORT).show();
                break;
            case R.id.magenta:

                titulo.setBackgroundResource(R.color.magenta);
                titulo.setTextColor(Color.WHITE.getRGB());
                color.setText("magenta");
                dismiss();
                break;
            case R.id.gris:
                titulo.setBackgroundResource(R.color.gris);
                titulo.setTextColor(Color.WHITE.getRGB());
                color.setText("gris");
                dismiss();
                break;
            case R.id.azul_oscuro:
                titulo.setBackgroundResource(R.color.azul_oscuro);
                titulo.setTextColor(Color.WHITE.getRGB());
                color.setText("azul_oscuro");
                dismiss();
                break;
            case R.id.blanco:
                titulo.setBackgroundResource(R.color.transparente);
                titulo.setTextColor(Color.BLACK.getRGB());
                color.setText("exit");
                dismiss();
                break;
            case R.id.naranja:
                titulo.setBackgroundResource(R.color.naranja);
                titulo.setTextColor(Color.WHITE.getRGB());
                color.setText("naranja");
                dismiss();
                break;
        }
    }

    public void show(FragmentManager fragmentManager, String loginDialog) {
    }
}
