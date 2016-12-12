package ruben.dam.izv.com.proyecto_0.vistas.vistas.main;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.Conexion.SyncAdapter;
import ruben.dam.izv.com.proyecto_0.vistas.adaptadores.AdaptadorNota;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoMain;
import ruben.dam.izv.com.proyecto_0.vistas.dialogo.DialogoBorrar;
import ruben.dam.izv.com.proyecto_0.vistas.dialogo.OnBorrarDialogListener;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

import ruben.dam.izv.com.proyecto_0.vistas.vistas.audios.VistaAudio;
import ruben.dam.izv.com.proyecto_0.vistas.vistas.gps.VistaLocalizacion;
import ruben.dam.izv.com.proyecto_0.vistas.vistas.notas.VistaNota;

public class VistaQuip extends AppCompatActivity implements ContratoMain.InterfaceVista, OnBorrarDialogListener, SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, LoaderManager.LoaderCallbacks<Cursor> {

    //PERMISOS
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    private static final int MY_PERMISSIONS_WRITE_SYNC_SETTINGS = 4;
    private static final int MY_PERMISSIONS_READ_SYNC_SETTINGS = 5;
    private static final int MY_PERMISSIONS_INTERNET = 6;
    private static final int PETICION_PERMISO_LOCALIZACION = 7;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private AdaptadorNota adaptador;
    private PresentadorQuip presentador;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quip);

        VistaQuip.HiloPermisos HP = new VistaQuip.HiloPermisos();
        HP.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_nav);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        navView = (NavigationView)findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;


                        switch (menuItem.getItemId()) {
                            case R.id.menu_nota:
                                presentador.onAddNota();
                                break;
                            case R.id.menu_lista:
                                Toast.makeText(VistaQuip.this,"Añadir lista",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.menu_audio:
                                presentador.onAddAudio();
                                break;
                            case R.id.menu_opcion_1:
                                Log.i("NavigationView", "Pulsada opción 1");
                                break;
                            case R.id.menu_opcion_2:
                                Log.i("NavigationView", "Pulsada opción 2");
                                break;
                            case R.id.menu_mapa:
                                presentador.onSeeMap();
                                break;
                        }

                        if(fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });

        presentador = new PresentadorQuip(this);

        lv = (ListView) findViewById(R.id.lvListaNotas);//Obtiene la ListView

        adaptador = new AdaptadorNota(this, null);//Crea un adaptador nuevo

        lv.setAdapter(adaptador);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {//Si pincha edita la nota
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presentador.onEditNota(i);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {//Si lo mantiene borra la nota
                //Toast.makeText(VistaQuip.this, "delete", Toast.LENGTH_SHORT).show();
                presentador.onShowBorrarNota(i);
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.IBMas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentador.onAddNota();
            }
        });

        getSupportLoaderManager().initLoader(1, null, this);
    }

    private void permisos() {
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
        //PERMISO DE LOCALIZACIÓN
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_buscar);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_buscar:
                //Toast.makeText(getApplicationContext(),"Buscar nota",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_del:
                Toast.makeText(getApplicationContext(),"Eliminar nota",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_setins:
                Toast.makeText(getApplicationContext(),"Configuración",Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_sync:
                SyncAdapter.sincronizarAhora(this, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
                //return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    public void mostrarAgregarNota() {//muestra activity_nota para añadir una nota
        //Toast.makeText(VistaQuip.this, "add", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        startActivity(i);
    }

    @Override
    public void mostrarAgregarLista() {

    }

    @Override
    public void mostrarAgregarAudio() {
        Intent audio = new Intent(this, VistaAudio.class);
        startActivity(audio);
    }

    @Override
    public void mostrarDatos(Cursor c) {
        adaptador.changeCursor(c);//Crea los textView en la lista
    }//muestra las notas que hemos creado activity_quip

    @Override
    public void mostrarEditarNota(Nota n) {//muestra activity_nota para modificar una nota
        //Toast.makeText(VistaQuip.this, "edit", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        Bundle b = new Bundle();
        b.putParcelable("nota", n);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void mostrarConfirmarBorrarNota(Nota n) {//Llama a DialogoBorrar
        DialogoBorrar fragmentBorrar = DialogoBorrar.newInstance(n);
        fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");

    }

    @Override
    public void mostrarMapa() {
        Intent gps = new Intent(this, VistaLocalizacion.class);
        startActivity(gps);
    }

    @Override
    public void onBorrarPossitiveButtonClick(Nota n) {
        presentador.onDeleteNota(n);
    }//lo borra si le da a ACEPTAR

    @Override
    public void onBorrarNegativeButtonClick() {//no hace nada si le da CANCELAR

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        Toast.makeText(getApplicationContext(), "Buscador activado", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        Toast.makeText(getApplicationContext(), "Buscador desactivado", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Toast.makeText(this, query + " 1", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        //CharSequence n = newText;

        //VistaQuip.this.adaptador.getFilter().filter(n);
        Toast.makeText(getApplicationContext(), adaptador.getItem(1).toString(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(), ContratoBaseDatos.CONTENT_URI_NOTA, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adaptador.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adaptador.swapCursor(null);
    }

    class HiloPermisos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            permisos();
            return null;
        }
    }
}
