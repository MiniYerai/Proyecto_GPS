package ruben.dam.izv.com.proyecto_0.vistas.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Toast;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

/**
 * Created by Ruben on 28/11/2016.
 */

public class AdaptadorMapa extends CursorAdapter {

    public AdaptadorMapa(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override //muestra en cada TextView el titulo de la nota
    public void bindView(View view, Context context, Cursor cursor) {

        Nota n = Nota.getNota(cursor);
        Toast.makeText(context, n.getTitulo(), Toast.LENGTH_SHORT).show();
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
