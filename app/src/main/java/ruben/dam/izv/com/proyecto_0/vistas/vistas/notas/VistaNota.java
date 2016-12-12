package ruben.dam.izv.com.proyecto_0.vistas.vistas.notas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
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
import java.util.List;
import java.util.Locale;

import harmony.java.awt.Color;
import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.basedatos.AyudanteGPS;
import ruben.dam.izv.com.proyecto_0.vistas.dialogo.DialogoColor;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoNota;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.GPS;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Lista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;
import ruben.dam.izv.com.proyecto_0.vistas.util.UtilFecha;
import ruben.dam.izv.com.proyecto_0.vistas.vistas.audios.VistaAudio;
import ruben.dam.izv.com.proyecto_0.vistas.vistas.gps.VistaLocalizacion;


public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista, TextToSpeech.OnInitListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //PDF
    private final static String NOMBRE_DIRECTORIO = "PDF_Quip";
    private final static String NOMBRE_DOCUMENTO = "NotaPDF";
    private final static String EXTENSION_DOCUMENTO = ".pdf";
    private final static String ETIQUETA_ERROR = "ERROR";
    private static int c = 1;
    //Hablar
    private TextToSpeech textToSpeech;
    //llamo a la clase MagicalCamera
    private MagicalCamera magicalCamera;

    //ACTIVITY RESOULT CODE (PARA SABER LA RESPUESTA QUE LE PIDO)
    private int RESIZE_PHOTO_PIXELS_PRECENTAGE = 1000;

    //EDITOR DE TEXTO
    private EditText editTextTitulo, editTextNota;

    //TEXT VIEW
    private TextView tvFecha, tvColor;

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
    private String ruta="X";

    private Bitmap rutaAuxiliar;

    //DEVUELVE FECHAS
    private UtilFecha fecha;

    private Boolean borrado = false;

    //LOCALIZACIÓN GOOGLE
    private GoogleApiClient googleApiClient;

    private double longitud;
    private double latitud;

    //Ayudante GPS

    private AyudanteGPS helper = new AyudanteGPS(this);

    private boolean guardaGPS = false;

    //redimensiona la imagen para mostrar cualquier resolucion !!!IMPORTANTE PARA EL BITMAP¡¡¡
    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        presentador = new PresentadorNota(this);

        textToSpeech = new TextToSpeech(VistaNota.this, this);

        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);
        img = (ImageView) findViewById(R.id.iv);

        tvColor=(TextView) findViewById(R.id.color);
        tvFecha=(TextView) findViewById(R.id.tvFecha);

        //Localización
        googleGPS();

        //recoge los permisos
        //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //comprueba de que ha solicitado los permisos


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
        colores();
        /*if (savedInstanceState == null) {
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment, "MainFragment")
                    .commit();
        }*/
        mostrarNota(nota);

        //BOTTOM BAR
        //bottomSheet();

        //Añadir Imagen
        presentador.onAddImg();

        //Añadir Audio
        addAudio();

        //Mostrar imagen
        //mostrarIMG();

        HiloTexto ht=new HiloTexto();
        ht.execute();

        //Crear PDF
        HiloPDF hd=new HiloPDF();
        hd.execute();

        //Hablar
        HiloHablar hh=new HiloHablar();
        hh.execute();

        //Borrar imagen
        borrarImagen();

        //Vista Completa de la imagen
        completa();

        //Cambia de color
        color();

        //mapa
        HiloMap hm = new HiloMap();
        hm.execute();
    }

    private void mapGPS() {
        ImageButton ibMap = (ImageButton) findViewById(R.id.ibLocal);

        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarMaps();
            }
        });
    }

    private void googleGPS(){
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    // cuando empieza la conexion
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            latitud = mLastLocation.getLatitude();
            longitud = mLastLocation.getLongitude();
            //Toast.makeText(this, mLastLocation.getLatitude()+" -- "+mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        }/*else{
            Toast.makeText(this, "Sin datos", Toast.LENGTH_SHORT).show();
        }*/
        //Toast.makeText(this, "CONECTADO", Toast.LENGTH_SHORT).show();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);

    }

    // cuando termina la conexion
    @Override
    public void onConnectionSuspended(int i) {

    }

    //SI FALLA LA CONEXION
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //ACTUALIZA EL PUNTO EN EL QUE ESTAMOS CON LA LOCALIZACION
    @Override
    public void onLocationChanged(Location location) {
        latitud = location.getLatitude();
        longitud = location.getLongitude();
        //Toast.makeText(this, latitud + " -/- " + longitud, Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera
        //LatLng otro = new LatLng(location.getLatitude(), -location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(otro).title("Estoy aqui 2!!!!"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(otro));
    }

    private void color(){
        ImageButton color = (ImageButton) findViewById(R.id.ibColor);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(VistaNota.this, "Entra", Toast.LENGTH_SHORT).show();
                FragmentManager ft = getSupportFragmentManager();
                FragmentManager fragmentManager = getSupportFragmentManager();
                new DialogoColor(editTextTitulo,editTextNota,tvColor).show(ft, "DialogoColor");

            }
        });
    }

    private void colores(){
        String co=nota.getColor();
        if(co!=null) {
            switch (co) {
                case "rojo":
                    editTextTitulo.setBackgroundResource(R.color.rojo);
                    editTextTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "azul":
                    editTextTitulo.setBackgroundResource(R.color.azul);
                    editTextTitulo.setTextColor(Color.BLACK.getRGB());
                    break;
                case "verde":
                    editTextTitulo.setBackgroundResource(R.color.verde);
                    editTextTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "morado":
                    editTextTitulo.setBackgroundResource(R.color.morado);
                    editTextTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "magenta":
                    editTextTitulo.setBackgroundResource(R.color.magenta);
                    editTextTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "gris":
                    editTextTitulo.setBackgroundResource(R.color.gris);
                    editTextTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "azul_oscuro":
                    editTextTitulo.setBackgroundResource(R.color.azul_oscuro);
                    editTextTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "naranja":
                    editTextTitulo.setBackgroundResource(R.color.naranja);
                    editTextTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
            }
        }
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

                bsdFragment.show(VistaNota.this.getSupportFragmentManager(), "BSDialog");
            }
        });
//--------------------------------------------------------------------------------------------------
    }

    protected void tipo(String t){
        if(t.equals("img")) {
            mostrarImagen();
        }
    }

    private void completa() {
        if (img != null) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presentador.imagenCompleta();
                }
            });
        }
    }

    private void borrarImagen(){
        if(img!=null) {
            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Representa a un conjunto de caracteres
                    final CharSequence[] opciones = {"Modificar Imagen", "Borrar Imagen", "Cancelar"};
                    //Parametro de dialogo
                    final AlertDialog.Builder builder = new AlertDialog.Builder(VistaNota.this);
                    //Titulo del Dialogo
                    builder.setTitle("Opciones de Imagen");
                    //Muestra las opciones
                    builder.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override//Detecta la opción que hayamos elegido
                        public void onClick(DialogInterface dialog, int opcion) {
                            if (opciones[opcion] == "Modificar Imagen") {
                                addImagen();
                                borrado=false;
                            } else if (opciones[opcion] == "Borrar Imagen") {
                               img.setVisibility(View.GONE);
                                borrado=true;
                            } else if (opciones[opcion] == "Cancelar") {
                                //desaparece el dialogo
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show(); //muestra el dialogo
                    return false;
                }
            });
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
        //VistaNota.super.onDestroy();

        super.onDestroy();
        if (helper != null) {
            OpenHelperManager.releaseHelper();
            helper = null;
        }
    }

    @Override
    public void onInit(int status) {
        if ( status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED )
        {
            Toast.makeText( VistaNota.this, "ERROR", Toast.LENGTH_SHORT ).show();
        }
    }

    private void crearPDF() {
        if(editTextTitulo.getText().toString().length()>0) {
            ImageButton btnpdf = (ImageButton) findViewById(R.id.btnPDF);
            btnpdf.setOnClickListener(new View.OnClickListener() {

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

                        if (!nota.getImagen().equals("X")) {
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

    @Override //Para saber de donde viene la peticion.
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode==-1) {
            borrado=false;
            img.setVisibility(View.VISIBLE);
            magicalCamera.resultPhoto(requestCode, resultCode, data);

            img.setImageBitmap(scaleDownBitmap(magicalCamera.getMyPhoto(),100,getApplicationContext()));
            ruta = MagicalCamera.bytesToStringBase64(MagicalCamera.bitmapToBytes(scaleDownBitmap(magicalCamera.getMyPhoto(),100,getApplicationContext()), MagicalCamera.PNG));
            rutaAuxiliar = scaleDownBitmap(magicalCamera.getMyPhoto(), 100, getApplicationContext());

        }else{
            Toast.makeText(this, "Error de imagen "+resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    //CONVERSOR DE BITMAP A URI
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
    private String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        /*ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,60, baos);*/

        //byte [] b=baos.toByteArray();
        //Bitmap bb=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        StringBuilder base64 = new StringBuilder(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //String temp= Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("LOOK", base64.toString());
        //ruta=temp;
        return base64.toString();
    }

    //Convertidor de String a Bitmap
    public Bitmap StringToBitMap(String encodedString){

        byte[] byteArray = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        /*try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }*/
    }


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

        String rut = ruta;
        outState.putString("rut", rut);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String rut = savedInstanceState.getString("rut");
        //Toast.makeText(this, rut, Toast.LENGTH_SHORT).show();
        if (rut != "X") {
            img.setImageBitmap(MagicalCamera.bytesToBitmap(MagicalCamera.stringBase64ToBytes(rut)));
            ruta = rut;
        }
    }

    @Override
    public void mostrarNota(Nota n) {// muestra la nota entera para modificarla


        // $$$$$$$$$
        //muestra la imagen
        if(nota.getImagen()!=null) {
            if(!nota.getImagen().equals("") && !nota.getImagen().equals("X")) {


                //Bitmap aux = StringToBitMap(nota.getImagen());
                //Uri aux = StringToUri(nota.getImagen());
                //img.setImageBitmap(Uri.parse(nota.getImagen()));
                img.setImageBitmap(MagicalCamera.bytesToBitmap(MagicalCamera.stringBase64ToBytes(nota.getImagen())));
                rutaAuxiliar = MagicalCamera.bytesToBitmap(MagicalCamera.stringBase64ToBytes(nota.getImagen()));
            }
        }

        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
        if(nota.getFecha()!=null) {
            tvFecha.setText(nota.getFecha());
        }else{
            tvFecha.setText("");
        }
    }

    @Override
    public void mostrarAgregarAudio() {
        Intent audio = new Intent(this, VistaAudio.class);
        startActivity(audio);
    }

    @Override
    public void mostrarImagen() {
        ImageButton btImg=(ImageButton) findViewById(R.id.btAddImg);

        btImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addImagen();
                //Toast.makeText(getApplicationContext(), "Añadir Imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void visualizador() {
        Intent i = new Intent(this, FullscreenActivity.class);
        if (!nota.getImagen().equals("X")) {
            i.putExtra("imgC", nota.getImagen());
            startActivity(i);
        } else if (!ruta.equals("X")) {
            i.putExtra("imgC", ruta);
            startActivity(i);
        }
    }

    @Override
    public void mostrarMaps() {
        if (nota.getTitulo() != null) {
            Intent inte = new Intent(this, VistaLocalizacion.class);
            //Toast.makeText(this, nota.getId() + " ", Toast.LENGTH_SHORT).show();
            inte.putExtra("nota_id", nota.getId() + "");
            startActivity(inte);
        }
    }

    private void addImagen(){
        //Representa a un conjunto de caracteres
        final CharSequence[] opciones = {"Camara", "Galería", "Cancelar"};
        //Parametro de dialogo
        final AlertDialog.Builder builder = new AlertDialog.Builder(VistaNota.this);
        //Titulo del Dialogo
        builder.setTitle("Opciones de Imagen");
        //Muestra las opciones
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override//Detecta la opción que hayamos elegido
            public void onClick(DialogInterface dialog, int opcion) {
                if (opciones[opcion] == "Camara") {
                    HiloCamara hta = new HiloCamara();
                    hta.execute();
                    //Camara();//Nos abre la camara del movil
                } else if (opciones[opcion] == "Galería") {
                    HiloGaleria hta = new HiloGaleria();
                    hta.execute();
                    //galeria();
                } else if (opciones[opcion] == "Cancelar") {
                    //desaparece el dialogo
                    dialog.dismiss();
                }
            }
        });
        builder.show(); //muestra el dialogo
    }

    private void saveNota() {//guarda la nota

        Date d = new Date();

        if (nota.getTitulo() != null) {
            if (!nota.getTitulo().equals(editTextTitulo.getText().toString()) || !nota.getNota().equals(editTextNota.getText().toString()) || ruta != "X" || tvColor.getText().toString() != "") {
                nota.setFecha(fecha.formatDate(d));//GUARDA LA FECHA
                guardaGPS = true;
            }
        } else {
            nota.setFecha(fecha.formatDate(d));//GUARDA LA FECHA
            guardaGPS = true;
        }

        if(editTextTitulo.getText().toString().length()>0) {
            nota.setTitulo(editTextTitulo.getText().toString());
        }else if(editTextNota.getText().toString().length()>0 || ruta!="X"){
            //Toast.makeText(this, ruta, Toast.LENGTH_SHORT).show();
            nota.setTitulo("Nota "+fecha.formatDate2(d));
        }else{
            nota.setTitulo(editTextTitulo.getText().toString());
        }

        nota.setNota(editTextNota.getText().toString());
        if(borrado){
            nota.setImagen("");
        }else {
            if (nota.getImagen() != null) {
                if (ruta != "X") {
                    nota.setImagen(ruta);
                } else if (!nota.getImagen().equals("")) {
                    nota.setImagen(nota.getImagen());
                }
            } else {
                nota.setImagen(ruta);
            }
        }
        //nota.setImagen("img");

        //Toast.makeText(getApplicationContext(),ruta,Toast.LENGTH_SHORT).show();
        nota.setVoz("voz");
        nota.setLista(0);
        if(tvColor.getText().toString()!=""){
            if(tvColor.getText().toString().equals("exit")){
                nota.setColor("");
            }else {
                nota.setColor(tvColor.getText().toString());
            }
        }else if(nota.getColor()==null){
            nota.setColor("");
        }

        //Toast.makeText(this, latitud + " -- " + longitud, Toast.LENGTH_SHORT).show();

        long r = presentador.onSaveNota(nota);
        if(r > 0 & nota.getId() == 0){
            nota.setId(r);
        }
        //Toast.makeText(this, guardaGPS + "", Toast.LENGTH_SHORT).show();
        //Guarda la ubicación
        if (guardaGPS) {
            RuntimeExceptionDao<GPS, Integer> gpsDao = helper.getGPSRuntimeDAO();

            GPS gps = new GPS(nota.getId(), nota.getTitulo(), latitud, longitud, fecha.formatDate2(d));

            gpsDao.create(gps);

            //Toast.makeText(this, latitud + " / " + longitud, Toast.LENGTH_SHORT).show();
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

    class HiloCamara extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            magicalCamera = new MagicalCamera(VistaNota.this,RESIZE_PHOTO_PIXELS_PRECENTAGE);
            img=(ImageView) findViewById(R.id.iv);

            magicalCamera.takePhoto();
            return null;
        }
    }
    class HiloGaleria extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            magicalCamera = new MagicalCamera(VistaNota.this,RESIZE_PHOTO_PIXELS_PRECENTAGE);
            img=(ImageView) findViewById(R.id.iv);

            magicalCamera.selectedPicture("Selecciona la imagen");
            return null;
        }
    }

    class HiloTexto extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            TextAudio();
            return null;
        }
    }
    class HiloHablar extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            hablar();
            return null;
        }
    }
    class HiloPDF extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            crearPDF();
            return null;
        }
    }

    class HiloMap extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mapGPS();
            return null;
        }
    }
}