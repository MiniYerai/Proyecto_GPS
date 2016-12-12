package ruben.dam.izv.com.proyecto_0.vistas.vistas.gps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.Array;
import java.sql.SQLException;
import java.util.List;

import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.vistas.basedatos.AyudanteGPS;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.GPS;

public class VistaLocalizacion extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        AyudanteGPS ayudante = new AyudanteGPS(this);

        RuntimeExceptionDao<GPS, Integer> GPSDAO = ayudante.getGPSRuntimeDAO();

        List<GPS> gpsResult = GPSDAO.queryForAll();

        if (getIntent().getStringExtra("nota_id") != null) {

            String nota_id = getIntent().getStringExtra("nota_id").toString();
            nota_id = nota_id.trim();
            long n_id = Long.parseLong(nota_id);
            //Toast.makeText(this, n_id + " ", Toast.LENGTH_SHORT).show();

            float zoomLevel = (float) 16.0;

            // Add a marker in Sydney and move the camera

            try {
                QueryBuilder<GPS, Integer> qb = GPSDAO.queryBuilder();

                qb.setWhere(qb.where().eq("nota_id", n_id));
                gpsResult = GPSDAO.query(qb.prepare());
                for (GPS gpsDatos : gpsResult) {
                    LatLng sydney = new LatLng(gpsDatos.getLatitud(), gpsDatos.getLongitud());
                    mMap.addMarker(new MarkerOptions().position(sydney).title(gpsDatos.getTitulo() + " " + gpsDatos.getFecha()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
                    //Toast.makeText(this, gpsDatos.getNota_id() + " ", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            float zoomLevel = (float) 1.0;

            for (GPS gps : gpsResult) {
                LatLng sydney = new LatLng(gps.getLatitud(), gps.getLongitud());
                mMap.addMarker(new MarkerOptions().position(sydney).title(gps.getTitulo() + " " + gps.getFecha()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
            }
        }


        // Add a marker in Sydney and move the camera

    }
}
