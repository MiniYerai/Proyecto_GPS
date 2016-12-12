package ruben.dam.izv.com.proyecto_0.vistas.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;


import harmony.java.awt.Color;
import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

public class AdaptadorNota extends CursorAdapter {

    public AdaptadorNota(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override //muestra en cada TextView el titulo de la nota
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.tvTituloNota);//Layout item.xml
        //CheckBox ch = (CheckBox) view.findViewById(R.id.tvTituloNota);//Layout item.xml
        Nota n = Nota.getNota(cursor);
        tv.setText(n.getTitulo());
        if(n.getColor()!=null) {
            switch (n.getColor()) {
                case "rojo":
                    tv.setBackgroundResource(R.color.rojo);
                    tv.setTextColor(Color.WHITE.getRGB());

                    break;
                case "azul":
                    tv.setBackgroundResource(R.color.azul);
                    tv.setTextColor(Color.black.getRGB());
                    break;
                case "verde":
                    tv.setBackgroundResource(R.color.verde);
                    tv.setTextColor(Color.WHITE.getRGB());
                    break;
                case "morado":
                    tv.setBackgroundResource(R.color.morado);
                    tv.setTextColor(Color.WHITE.getRGB());
                    break;
                case "magenta":
                    tv.setBackgroundResource(R.color.magenta);
                    tv.setTextColor(Color.WHITE.getRGB());
                    break;
                case "gris":
                    tv.setBackgroundResource(R.color.gris);
                    tv.setTextColor(Color.WHITE.getRGB());
                    break;
                case "azul_oscuro":
                    tv.setBackgroundResource(R.color.azul_oscuro);
                    tv.setTextColor(Color.WHITE.getRGB());
                    break;
                case "naranja":
                    tv.setBackgroundResource(R.color.naranja);
                    tv.setTextColor(Color.WHITE.getRGB());
                    break;
                default:
                    tv.setBackgroundResource(R.color.transparente);
                    tv.setTextColor(Color.GRAY.getRGB());
            }
        }
        //ch.setText(n.getId()+"");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//no lo se
        return super.getView(position, convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {//no lo se
        return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    }
}