package cl.brayan.georegistro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.Marker;

public class Mapa extends FragmentActivity {

    private static final int SOLICITUD_UBICACION = 100;

    private MapView mapa;
    private MyLocationNewOverlay capaUbicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                getApplicationContext(),
                getPreferences(MODE_PRIVATE)
        );

        setContentView(R.layout.activity_mapa);

        mapa = findViewById(R.id.map);
        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setMultiTouchControls(true);
        mapa.getController().setZoom(16.0);

        Intent intent = getIntent();

        if (intent.hasExtra("latitud") && intent.hasExtra("longitud")) {

            double latitud = intent.getDoubleExtra("latitud", -36.6066);
            double longitud = intent.getDoubleExtra("longitud", -72.1034);

            GeoPoint punto = new GeoPoint(latitud, longitud);
            mapa.getController().setCenter(punto);

            Marker marcador = new Marker(mapa);
            marcador.setPosition(punto);
            marcador.setTitle(intent.getStringExtra("titulo"));
            mapa.getOverlays().add(marcador);

        } else {
            mapa.getController().setCenter(
                    new GeoPoint(-36.6066, -72.1034)
            );

            comprobarPermisoUbicacion();
        }
    }


    private void comprobarPermisoUbicacion() {
        boolean permisoAproximado =
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        boolean permisoPreciso =
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        if (permisoAproximado || permisoPreciso) {
            activarUbicacion();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    SOLICITUD_UBICACION
            );
        }
    }

    private void activarUbicacion() {
        if (capaUbicacion != null) {
            return;
        }

        capaUbicacion = new MyLocationNewOverlay(
                new GpsMyLocationProvider(this),
                mapa
        );

        mapa.getOverlays().add(capaUbicacion);
        capaUbicacion.enableMyLocation();

        capaUbicacion.runOnFirstFix(() -> {
            GeoPoint ubicacion = capaUbicacion.getMyLocation();

            if (ubicacion != null) {
                runOnUiThread(() ->
                        mapa.getController().animateTo(ubicacion)
                );
            }
        });

        mapa.invalidate();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults
        );

        if (requestCode == SOLICITUD_UBICACION) {
            boolean concedido = false;

            for (int resultado : grantResults) {
                if (resultado == PackageManager.PERMISSION_GRANTED) {
                    concedido = true;
                    break;
                }
            }

            if (concedido) {
                activarUbicacion();
            } else {
                Toast.makeText(
                        this,
                        "Puedes consultar el mapa, pero no mostrar tu ubicacion.",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mapa != null) {
            mapa.onResume();
        }

        if (capaUbicacion != null) {
            comprobarPermisoUbicacion();
        }
    }

    @Override
    protected void onPause() {
        if (capaUbicacion != null) {
            capaUbicacion.disableMyLocation();
        }

        if (mapa != null) {
            mapa.onPause();
        }

        super.onPause();
    }
}
