package ruben.dam.izv.com.proyecto_0.vistas.vistas.audios;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.vistas.notas.VistaNota;

/**
 * Created by dam on 3/11/16.
 */

public class VistaAudio extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private boolean hablando = false;
    private static final int CTE = 1, CTE2 = 2;
    private TextToSpeech tts;
    private String frase = "";
    private VistaNota nota;
    private String prueba="adios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        init();
    }

    public void init() {
        HiloInit hi=new HiloInit();
    }
    class HiloInit extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Intent i = new Intent();
            i.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(i, CTE);
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CTE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
            } else {
                Intent intent = new Intent();
                intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(intent);
            }
        } else if (requestCode == CTE2) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> textos = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                frase = textos.get(0);
                añadirtv2(getCurrentFocus(), frase);
                Hilo s = new Hilo(frase);
                s.execute();
            }
        }
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            hablando = true;
            tts.setLanguage(new Locale("es", "ES"));
        } else {
            Log.v("TTSERROR: ", "No se puede reproducir");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }


    public void hablar(View v) {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hablar ahora");
        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        startActivityForResult(i, CTE2);
    }

    class Hilo extends AsyncTask<Object, Integer, String> {


        Hilo(String... p) {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object[] params) {
            String frase = "";
            try {

            } catch (Exception ex) {

            }

            return frase;
        }
    }

    public void añadirtv2(View v, String mensaje) {
        LinearLayout svl;
        svl = (LinearLayout) findViewById(R.id.ly);
        TextView view = new TextView(this);
        mensaje = Character.toUpperCase(mensaje.charAt(0))+mensaje.substring(1,mensaje.length());

        Intent intent = new Intent(VistaAudio.this, VistaNota.class);
        Bundle b = new Bundle();

        if (mensaje.equals("") || mensaje.equals(null))

            b.putString("voz", "TEXTO VACIO");

        else
        {
            b.putString("voz", mensaje );
        }
        intent.putExtras(b);
        //intent.putExtra("voz",mensaje);
        startActivity(intent);

        //nota.TextAudio(mensaje);

        //getIntent().getExtras().getString(mensaje);


        view.setText(mensaje);
        view.setTextSize(30);
        view.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(50, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        view.setLayoutParams(llp);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        svl.addView(view, params);
    }

}
