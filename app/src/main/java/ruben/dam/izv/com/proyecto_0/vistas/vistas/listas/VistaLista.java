package ruben.dam.izv.com.proyecto_0.vistas.vistas.listas;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoLista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Lista;

public class VistaLista extends AppCompatActivity implements ContratoLista.InterfazVista {

    private LinearLayout ll;
    private ArrayList<EditText> listaEt;
    private PresentadorLista presentador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        presentador = new PresentadorLista(this);

        init();
    }

    private void init(){

        final EditText etTitulo = (EditText) findViewById(R.id.etTitulo);
        final EditText etNew = (EditText) findViewById(R.id.etNotaItem);

        Button b = (Button) findViewById(R.id.btGuardar);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Lista l = new Lista();
                //l.setTitulo(etTitulo.getText().toString());
                String todo = "";
                //Gson gson = new Gson();
                for(EditText et:listaEt){
                    //gson.toJson(et.getText().toString());
                    todo+="\"" + et.getText().toString() + "\"" + ",";
                }
                todo = "[" + todo + "]";
                l.setTexto(todo);
                Toast.makeText(VistaLista.this,l.toString(),Toast.LENGTH_SHORT).show();
                presentador.onSaveLista(l);
            }
        });
        listaEt = new ArrayList<>();
        ll = (LinearLayout) findViewById(R.id.lladdlista);
        b = (Button) findViewById(R.id.botonAddLista);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                EditText et = new EditText(VistaLista.this);

                et.setLayoutParams(params);
                et.setHint(getString(R.string.nota));

                ll.addView(et);
                listaEt.add(et);
            }
        });
    }

    @Override
    public void mostrarLista(Lista l) {

    }

    /*private void saveLista(){//guarda la lista

        lista.setId_nota(0);
        lista.setTexto("texto");

        long r = presentador.onSaveLista(lista);

        if (r > 0 & nota.getId() == 0){
            nota.setId(r);
        }
    }*/
}
