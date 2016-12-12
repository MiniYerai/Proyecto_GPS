package ruben.dam.izv.com.proyecto_0.vistas.vistas.notas;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import harmony.java.awt.Color;
import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoNota;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Lista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;
import ruben.dam.izv.com.proyecto_0.vistas.util.UtilFecha;

public class VistaNotaOld extends AppCompatActivity implements ContratoNota.InterfaceVista, TextToSpeech.OnInitListener {

    //PDF
    private final static String NOMBRE_DIRECTORIO = "PDF_Quip";
    private final static String NOMBRE_DOCUMENTO = "NotaPDF";
    private final static String EXTENSION_DOCUMENTO = ".pdf";
    private final static String ETIQUETA_ERROR = "ERROR";
    //PERMISOS
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    private static final int MY_PERMISSIONS_WRITE_SYNC_SETTINGS = 4;
    private static final int MY_PERMISSIONS_READ_SYNC_SETTINGS = 5;
    private static final int MY_PERMISSIONS_INTERNET = 6;
    private static int c = 1;
    //Hablar
    private TextToSpeech textToSpeech;
    //llamo a la clase MagicalCamera
    private MagicalCamera magicalCamera;
    //ACTIVITY RESOULT CODE (PARA SABER LA RESPUESTA QUE LE PIDO)
    private int RESIZE_PHOTO_PIXELS_PRECENTAGE = 3000;
    //EDITOR DE TEXTO
    private EditText editTextTitulo, editTextNota;
    //TEXT VIEW
    private TextView tvFecha;
    //IMAGEN VIEW
    private ImageView img;
    //CLASES POJO
    private Nota nota = new Nota();
    private Lista lista = new Lista();
    //PRESENTADOR NOTA
    private PresentadorNota presentador;
    //BOTTOM SHEET
    private BottomSheetBehavior mBottomSheetBehavior;
    //guardara la ruta para la Base de Datos
    private String ruta="";
    //DEVUELVE FECHAS
    private UtilFecha fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        presentador = new PresentadorNota(this);

        textToSpeech = new TextToSpeech(VistaNotaOld.this, this);

        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);

        tvFecha=(TextView) findViewById(R.id.tvFecha);

        //muestra el texto traducido


        //recoge los permisos
        //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //comprueba de que ha solicitado los permisos
        permisos();

        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if(extras!=null) {
            if (extras.get("voz") == null) {
                if (savedInstanceState != null) {
                    nota = savedInstanceState.getParcelable("nota");
                } else {
                    Bundle b = getIntent().getExtras();
                    if (b != null) {
                        nota = b.getParcelable("nota");
                    }
                }
            }
        }
        mostrarNota(nota);

        //BOTTOM BAR
        //bottomSheet();

        //Añadir Imagen
        presentador.onAddImg();

        //Añadir Audio
        addAudio();

        //Mostrar imagen
        mostrarIMG();

        TextAudio();

        //Crear PDF
        crearPDF();

        //Hablar
        hablar();
    }

    private void bottomSheet(){
        //final View bottomSheet = findViewById( R.id.activity_dialogo_fragment );

        ImageButton bMas=(ImageButton) findViewById(R.id.IBMas2);
        ImageButton bSou=(ImageButton) findViewById(R.id.IBSouce);

//--------------------------------------------------------------------------------------------------

        bMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bsdFragment =
                        DialogoFragment.newInstance();

                bsdFragment.show(VistaNotaOld.this.getSupportFragmentManager(), "BSDialog");
            }
        });
//--------------------------------------------------------------------------------------------------
    }

    protected void tipo(String t){
        if(t.equals("img")) {
            mostrarImagen();
        }
    }
    private void permisos(){
        //PERMISO DE ESCRITURA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
        //PERMISO DE LECTURA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        }
        //PERMISO DE CAMARA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }

        //PERMISO DE SINCRONIZACION DE ESCRITURA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_SYNC_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_SYNC_SETTINGS)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_SYNC_SETTINGS},
                        MY_PERMISSIONS_WRITE_SYNC_SETTINGS);

            }
        }
        //PERMISO DE SINCRONIZACION DE LECTURA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SYNC_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SYNC_SETTINGS)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SYNC_SETTINGS},
                        MY_PERMISSIONS_READ_SYNC_SETTINGS);

            }
        }
        //PERMISO DE INTERNET
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_INTERNET);

            }
        }
    }
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_WRITE_SYNC_SETTINGS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_READ_SYNC_SETTINGS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    private void addAudio(){
        ImageButton btAudi=(ImageButton) findViewById(R.id.btAddAudio);

        btAudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.onAddAudio();
            }
        });

    }
    public void TextAudio(){


        /*if(!equals(audio.notaA)) {
            String voz2=audio.notaA;
            Toast.makeText(VistaNota.this, voz2, Toast.LENGTH_LONG).show();
        }*/

        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras != null) {//ver si contiene datos
            if(extras.get("voz")!=null) {
                String voz = (String) extras.get("voz");//Obtengo la voz
                editTextNota.setText(voz);

                //audi=voz;
                //Toast.makeText(VistaNota.this, voz, Toast.LENGTH_LONG).show();
            }
        }/*else {
            Toast.makeText(VistaNota.this,"Error",Toast.LENGTH_LONG).show();
        }*/

    }

    private void hablar() {

        ImageButton btnHablar = (ImageButton) findViewById(R.id.btnHablar);
        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextNota.getText().toString().length()>0) {
                    textToSpeech.setLanguage(new Locale("spa", "ESP"));
                    speak(editTextNota.getText().toString());
                }
            }

            private void speak(String str){

                textToSpeech.speak( str, TextToSpeech.QUEUE_FLUSH, null );
                textToSpeech.setSpeechRate( 0.0f );
                textToSpeech.setPitch( 0.0f );
            }
        });
    }

    @Override
    protected void onDestroy(){

        if ( textToSpeech != null )
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        VistaNotaOld.super.onDestroy();
    }


    @Override
    public void onInit(int status) {
        if ( status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED )
        {
            Toast.makeText( VistaNotaOld.this, "ERROR", Toast.LENGTH_SHORT ).show();
        }
    }

    private void crearPDF() {
        if(editTextTitulo.getText().toString().length()>0) {
            ImageButton btnpdf = (ImageButton) findViewById(R.id.btnPDF);
            btnpdf.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    // Creamos el documento.
                    com.lowagie.text.Document documento = new com.lowagie.text.Document();
                    try {

                        // Creamos el fichero con el nombre que deseemos.
                        String fichero = getNombreValido();
                        File f = crearFichero(fichero);

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), "PDF creado", Toast.LENGTH_SHORT);

                        toast1.show();

                        // Creamos el flujo de datos de salida para el fichero donde guardaremos el pdf.
                        FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());

                        // Asociamos el flujo que acabamos de crear al documento.
                        com.lowagie.text.pdf.PdfWriter writer = com.lowagie.text.pdf.PdfWriter.getInstance(documento, ficheroPdf);

                        // Incluimos el pie de pagina y una cabecera
                        HeaderFooter pie = new HeaderFooter(new Phrase("Desarrollado por Rubén, Virginia y Andrés.                 " +
                                "         I.E.S Zaidin-Vergeles"), false);

                        documento.setFooter(pie);

                        // Abrimos el documento.
                        documento.open();

                        // Añadimos un titulo con una fuente personalizada.
                        Font font = FontFactory.getFont(FontFactory.HELVETICA, 28, Font.BOLD, Color.BLACK);
                        documento.add(new com.lowagie.text.Paragraph(nota.getTitulo(), font));

                        // Añadimos un espacio para que quede mejor xD
                        String espacio = " ";
                        Font fontE = FontFactory.getFont(FontFactory.HELVETICA, 28, Font.BOLD, Color.BLACK);
                        documento.add(new com.lowagie.text.Paragraph(espacio, fontE));

                        // Añadimos la descripción de la nota.
                        String descripcion = nota.getNota();
                        Font fontD = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.NORMAL, Color.BLACK);
                        documento.add(new com.lowagie.text.Paragraph(descripcion, fontD));

                        //Añadimos la imagen con tamaño inferior
                        if (nota.getImagen() != null) {
                            Bitmap bitmap = BitmapFactory.decodeFile(nota.getImagen());
                            int origWidth = bitmap.getWidth();
                            int origHeight = bitmap.getHeight();
                            final int destWidth = 450;//or the width you need
                            if (origWidth > destWidth) {
                                int destHeight = origHeight / (origWidth / destWidth);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                                Bitmap auxB = Bitmap.createScaledBitmap(bitmap, 450, destHeight, false);
                                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                                auxB.compress(Bitmap.CompressFormat.JPEG, 60, stream1);
                                Image imagen = Image.getInstance(stream1.toByteArray());
                                documento.add(imagen);
                            }
                        }


                        /*
                        // Añadimos la imagen
                        if(nota.getImagen()!=null) {
                            Image imagen = Image.getInstance(nota.getImagen());
                            documento.add(imagen);
                        }
                        */

                    } catch (com.lowagie.text.DocumentException e) {

                        Log.e(ETIQUETA_ERROR, e.getMessage());

                    } catch (IOException e) {

                        Log.e(ETIQUETA_ERROR, e.getMessage());

                    } finally {

                        // Cerramos el documento.
                        documento.close();
                    }
                }
                // Crea un fichero con el nombre que se le pasa a la funcion y en la ruta especificada.

                public File crearFichero(String nombreFichero) throws IOException {
                    File ruta = getRuta();
                    File fichero = null;
                    if (ruta != null)
                        fichero = new File(ruta, nombreFichero);
                    return fichero;
                }

                // Hacemos una función para que no sobreescriba el documento
                public String getNombreValido() {
                    File ruta = getRuta();
                    File f = null;
                    String nombre = NOMBRE_DOCUMENTO + EXTENSION_DOCUMENTO;
                    f = new File(ruta, nombre);
                    if (!f.exists()) {
                        nombre = NOMBRE_DOCUMENTO + EXTENSION_DOCUMENTO;
                    } else {
                        c++;
                        nombre = NOMBRE_DOCUMENTO + c + EXTENSION_DOCUMENTO;
                    }
                    return nombre;
                }

                /**
                 * Obtenemos la ruta donde vamos a almacenar el fichero.
                 */
                public File getRuta() {

                    // El fichero sera almacenado en un directorio dentro del directorio Descargas
                    File ruta = null;
                    if (Environment.MEDIA_MOUNTED.equals(Environment
                            .getExternalStorageState())) {
                        ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), NOMBRE_DIRECTORIO);

                        if (ruta != null) {
                            if (!ruta.mkdir()) {
                                if (!ruta.exists()) {
                                    return null;
                                }
                            }
                        }
                    } else {
                    }

                    return ruta;
                }
            });
        }
    }

    private void mostrarIMG(){
        // $$$$$$$$$
        ruta=nota.getImagen();
        if(ruta!=null){
            img.setVisibility(View.VISIBLE);
        }
        //------------------------------------
        /*if(nota.getImagen()!=null){
            img.setVisibility(View.VISIBLE);
        }else if(nota.getImagen()!=""){
            img.setVisibility(View.VISIBLE);
        }*/
    }

    private void Camara(){

        magicalCamera = new MagicalCamera(this,RESIZE_PHOTO_PIXELS_PRECENTAGE);
        img=(ImageView) findViewById(R.id.iv);

        magicalCamera.takePhoto();
    }
    private void galeria(){

        magicalCamera = new MagicalCamera(this,RESIZE_PHOTO_PIXELS_PRECENTAGE);
        img=(ImageView) findViewById(R.id.iv);

        magicalCamera.selectedPicture("Selecciona la imagen");
    }

    @Override //Para saber de donde viene la peticion.
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

            /*if(ruta!="null" || ruta!="") {
                nota.setImagen(ruta);
            }else{
                nota.setImagen(nota.getImagen());
            }*/
        //NO SE NECESITA
        //byte[] myArray = MagicalCamera.bitmapToBytes(magicalCamera.getMyPhoto(), MagicalCamera.PNG);
        //String stringbase64 = MagicalCamera.bytesToStringBase64(myArray);
        //Toast.makeText(VistaNota.this,requestCode,Toast.LENGTH_LONG).show();
        if(resultCode!=0) {
            magicalCamera.resultPhoto(requestCode, resultCode, data);

            img.setImageBitmap(magicalCamera.getMyPhoto());
            img.setVisibility(View.VISIBLE);
            //BitMapToString(magicalCamera.getMyPhoto());

            ruta = getRealPathFromURI(BitmapUri(getApplicationContext(), magicalCamera.getMyPhoto()));

            if (magicalCamera.savePhotoInMemoryDevice(magicalCamera.getMyPhoto(), "nota", "Fotos de Quip", MagicalCamera.PNG, true)) {
                Toast.makeText(getApplicationContext(), "La foto se ha guardado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error de foto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri BitmapUri(Context context, Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //convertidor de BitMap a String
    /*private void BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);

        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("LOOK", temp);
        ruta=temp;
    }*/

    //Convertidor de String a Bitmap
    /*public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }*/

    //CONVERSOR DE BITMAP A URI
    @Override
    protected void onPause() {
        saveNota();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
    }

    @Override
    public void mostrarNota(Nota n) {// muestra la nota entera para modificarla


        // $$$$$$$$$
        //muestra la imagen
        if(nota.getImagen()!=null) {
            if(!nota.getImagen().equals("")) {
                img = (ImageView) findViewById(R.id.iv);

                //Bitmap aux = StringToBitMap(nota.getImagen());
                //Uri aux = StringToUri(nota.getImagen());
                img.setImageURI(Uri.parse(nota.getImagen()));
            }
        }

        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
        if(nota.getFecha()!=null) {
            tvFecha.setText(nota.getFecha());
        }else{
            tvFecha.setText("");
        }
        //Toast.makeText(VistaNota.this, nota.getFecha(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarAgregarAudio() {
        // Intent audio = new Intent(this, VistaAudio.class);
        //startActivity(audio);
    }

    @Override
    public void mostrarImagen() {
        ImageButton btImg=(ImageButton) findViewById(R.id.btAddImg);

        btImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Representa a un conjunto de caracteres
                final CharSequence[] opciones = {"Camara", "Galería", "Cancelar"};
                //Parametro de dialogo
                final AlertDialog.Builder builder = new AlertDialog.Builder(VistaNotaOld.this);
                //Titulo del Dialogo
                builder.setTitle("Opciones de Imagen");
                //Muestra las opciones
                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override//Detecta la opción que hayamos elegido
                    public void onClick(DialogInterface dialog, int opcion) {
                        if (opciones[opcion] == "Camara") {
                            Camara();//Nos abre la camara del movil
                        } else if (opciones[opcion] == "Galería") {
                            galeria();
                        } else if (opciones[opcion] == "Cancelar") {
                            //desaparece el dialogo
                            dialog.dismiss();
                        }
                    }
                });
                builder.show(); //muestra el dialogo
                //Toast.makeText(getApplicationContext(), "Añadir Imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void visualizador() {

    }

    @Override
    public void mostrarMaps() {

    }

    private void saveNota() {//guarda la nota

        Date d = new Date();

        if(editTextTitulo.getText().toString().length()>0) {
            nota.setTitulo(editTextTitulo.getText().toString());
        }else if(editTextNota.getText().toString().length()>0 || ruta!=null){
            nota.setTitulo("Nota "+fecha.formatDate2(d));
        }else{
            nota.setTitulo(editTextTitulo.getText().toString());
        }

        nota.setNota(editTextNota.getText().toString());

        if(nota.getImagen()!=null){
            if(!ruta.equals("")){
                nota.setImagen(ruta);
            }else if(!nota.getImagen().equals("")){
                nota.setImagen(nota.getImagen());
            }
        }else{
            nota.setImagen(ruta);
        }
        //nota.setImagen("img");

        //Toast.makeText(getApplicationContext(),ruta,Toast.LENGTH_SHORT).show();
        nota.setVoz("voz");
        nota.setLista(0);
        nota.setColor("black");

        nota.setFecha(fecha.formatDate(d));
        long r = presentador.onSaveNota(nota);
        if(r > 0 & nota.getId() == 0){
            nota.setId(r);
        }
    }

    private void saveLista(){//guarda la lista

        lista.setId_nota(0);
        lista.setTexto("texto");

        long r = presentador.onSaveLista(lista);

        if (r > 0 & nota.getId() == 0){
            nota.setId(r);
        }
    }
}