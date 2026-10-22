package cl.brayan.georegistro;

import android.content.Intent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CrearIncidencias extends AppCompatActivity {

    private EditText etTitulo, etDescripcion;
    private Spinner spCategoria;
    private TextView txtFecha, txtHora;

    private Calendar calendario;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private TextView txtCoordenadas;
    private Double latitudSeleccionada = null;
    private Double longitudSeleccionada = null;

    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private final SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_incidencias);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        txtCoordenadas = findViewById(R.id.txtCoordenadas);

        Button btnElegirUbicacion =
                findViewById(R.id.btnElegirUbicacion);

        btnElegirUbicacion.setOnClickListener(v -> {
            Intent intent = new Intent(
                    CrearIncidencias.this,
                    SeleccionarUbicacion.class
            );

            startActivityForResult(intent, 200);
        });

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        spCategoria = findViewById(R.id.spCategoria);
        txtFecha = findViewById(R.id.txtFecha);
        txtHora = findViewById(R.id.txtHora);

        Button btnCambiarFecha = findViewById(R.id.btnCambiarFecha);
        Button btnCambiarHora = findViewById(R.id.btnCambiarHora);

        Button btnRegistrarIncidencia = findViewById(R.id.btnRegistrarIncidencia);

        calendario = Calendar.getInstance();
        actualizarFechaHora();

        btnCambiarFecha.setOnClickListener(v -> cambiarFecha());
        btnCambiarHora.setOnClickListener(v -> cambiarHora());

        btnRegistrarIncidencia.setOnClickListener(v -> registrarIncidencia());

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                }
        );
    }

    private void actualizarFechaHora() {
        txtFecha.setText(formatoFecha.format(calendario.getTime()));
        txtHora.setText(formatoHora.format(calendario.getTime()));
    }

    private void cambiarFecha() {new DatePickerDialog(this,
                (view, year, month, day) -> {
                    calendario.set(Calendar.YEAR, year);
                    calendario.set(Calendar.MONTH, month);
                    calendario.set(Calendar.DAY_OF_MONTH, day);
                    actualizarFechaHora();
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void cambiarHora() {new TimePickerDialog(this,
                (view, hour, minute) -> {
                    calendario.set(Calendar.HOUR_OF_DAY, hour);
                    calendario.set(Calendar.MINUTE, minute);
                    actualizarFechaHora();
                },
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void registrarIncidencia() {

        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = spCategoria.getSelectedItem().toString();

        if (titulo.isEmpty()) {
            etTitulo.setError("Ingresa un título");
            return;
        }

        if (descripcion.isEmpty()) {
            etDescripcion.setError("Ingresa una descripción");
            return;
        }

        if (spCategoria.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Selecciona una categoría",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (latitudSeleccionada == null
                || longitudSeleccionada == null) {

            Toast.makeText(
                    this,
                    "Selecciona la ubicacion de la incidencia",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Map<String, Object> incidencia = new HashMap<>();
        incidencia.put("titulo", titulo);
        incidencia.put("descripcion", descripcion);
        incidencia.put("categoria", categoria);
        incidencia.put("fecha", txtFecha.getText().toString());
        incidencia.put("hora", txtHora.getText().toString());
        incidencia.put("estado", "Pendiente");
        incidencia.put("latitud", latitudSeleccionada);
        incidencia.put("longitud", longitudSeleccionada);
        incidencia.put("uidUsuario", auth.getCurrentUser().getUid());
        incidencia.put("fechaCreacion", FieldValue.serverTimestamp());

        db.collection("incidencias")
                .add(incidencia)
                .addOnSuccessListener(documento -> {
                    Toast.makeText(this, "Incidencia registrada",
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al guardar" + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200
                && resultCode == RESULT_OK
                && data != null) {

            latitudSeleccionada =
                    data.getDoubleExtra("latitud", 0);

            longitudSeleccionada =
                    data.getDoubleExtra("longitud", 0);

            txtCoordenadas.setText(
                    String.format(
                            Locale.getDefault(),
                            "%.6f, %.6f",
                            latitudSeleccionada,
                            longitudSeleccionada
                    )
            );
        }
    }
}