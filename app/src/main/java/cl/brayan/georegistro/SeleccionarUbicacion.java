
package cl.brayan.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.events.MapEventsReceiver;

import android.view.MotionEvent;

public class SeleccionarUbicacion extends AppCompatActivity
        implements MapEventsReceiver {

    private MapView mapa;
    private GeoPoint ubicacionSeleccionada;
    private Marker marcador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                getApplicationContext(),
                getPreferences(MODE_PRIVATE)
        );

        setContentView(R.layout.activity_seleccionar_ubicacion);

        mapa = findViewById(R.id.mapaSeleccion);
        Button btnConfirmar = findViewById(R.id.btnConfirmarUbicacion);

        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setMultiTouchControls(true);
        mapa.getController().setZoom(15.0);
        mapa.getController().setCenter(
                new GeoPoint(-36.6066, -72.1034)
        );

        marcador = new Marker(mapa);
        marcador.setTitle("Ubicación seleccionada");

        mapa.getOverlays().add(new MapEventsOverlay(this));

        btnConfirmar.setOnClickListener(v -> {
            if (ubicacionSeleccionada == null) {
                Toast.makeText(
                        this,
                        "Toca el mapa para elegir una ubicacion",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Intent resultado = new Intent();
            resultado.putExtra(
                    "latitud",
                    ubicacionSeleccionada.getLatitude()
            );
            resultado.putExtra(
                    "longitud",
                    ubicacionSeleccionada.getLongitude()
            );

            setResult(RESULT_OK, resultado);
            finish();
        });
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint punto) {
        ubicacionSeleccionada = punto;

        marcador.setPosition(punto);
        if (!mapa.getOverlays().contains(marcador)) {
            mapa.getOverlays().add(marcador);
        }

        mapa.invalidate();
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint punto) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapa.onResume();
    }

    @Override
    protected void onPause() {
        mapa.onPause();
        super.onPause();
    }
}
