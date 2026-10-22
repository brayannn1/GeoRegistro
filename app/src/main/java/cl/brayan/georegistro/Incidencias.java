package cl.brayan.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Incidencias extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtNoIncidencias;
    private IncidenciaAdapter adapter;
    private final List<Map<String, Object>> listaIncidencias =
            new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_incidencias);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        txtNoIncidencias = findViewById(R.id.txtNoIncidencias);

        Button btnCrearIncidencia = findViewById(R.id.btnCrearIncidencia);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IncidenciaAdapter(listaIncidencias);
        recyclerView.setAdapter(adapter);

        btnCrearIncidencia.setOnClickListener(v -> {
            Intent intent = new Intent(Incidencias.this, CrearIncidencias.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);
                    return insets;
                });

        cargarIncidencias();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && auth != null) {
            cargarIncidencias();
        }
    }

    private void cargarIncidencias() {

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this,
                    "Debes iniciar sesión",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        db.collection("incidencias")
                .whereEqualTo("uidUsuario", uid)
                .whereEqualTo("estado", "Pendiente")
                .get()
                .addOnSuccessListener(resultado -> {

                    listaIncidencias.clear();

                    for (var documento : resultado.getDocuments()) {
                        Map<String, Object> incidencia = documento.getData();

                        if (incidencia != null) {
                            incidencia.put("id", documento.getId());
                            listaIncidencias.add(incidencia);
                        }
                    }

                    adapter.notifyDataSetChanged();

                    boolean vacia = listaIncidencias.isEmpty();

                    txtNoIncidencias.setVisibility(
                            vacia ? View.VISIBLE : View.GONE);

                    recyclerView.setVisibility(
                            vacia ? View.GONE : View.VISIBLE);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error al cargar: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}