package cl.brayan.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        Button btnMapa = findViewById(R.id.btnMapa);
        Button btnIncidencias = findViewById(R.id.btnIncidencias);
        Button btnCrearIncidenciaPantalla = findViewById(R.id.btnCrearIncidenciaPantalla);
        Button btnSensor = findViewById(R.id.btnSensor);
        Button btnHistorial = findViewById(R.id.btnHistorial);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnMapa.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreen.this, Mapa.class);
            startActivity(intent);
        });

        btnIncidencias.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreen.this, Incidencias.class);
            startActivity(intent);
        });

        btnCrearIncidenciaPantalla.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreen.this, CrearIncidencias.class);
            startActivity(intent);
        });

        btnSensor.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreen.this, Sensor.class);
            startActivity(intent);
        });

        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreen.this, Historial.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}