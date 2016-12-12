package ruben.dam.izv.com.proyecto_0.vistas.vistas.notas;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ruben.dam.izv.com.proyecto_0.R;

public class DialogoFragment extends BottomSheetDialogFragment {

    static DialogoFragment newInstance() {
        return new DialogoFragment();
    }

    private VistaNota nota;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_dialogo_fragment, container, false);
        Button bSou=(Button) v.findViewById(R.id.btAddImg);
        bSou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nota.tipo("img");
                //Toast.makeText(getContext(),"hola",Toast.LENGTH_LONG).show();
                Log.v("loglog","loglog");
            }
        });
        return v;
    }
}
