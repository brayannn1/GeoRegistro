package cl.brayan.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText etCorreoRegistro;
    private EditText etContrasenaRegistro;
    private EditText etConfirmarContrasena;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etCorreoRegistro = findViewById(R.id.etCorreoRegistro);
        etContrasenaRegistro = findViewById(R.id.etContrasenaRegistro);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);

        Button btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(v -> crearCuenta());
    }

        private void crearCuenta() {
            String correo = etCorreoRegistro.getText().toString().trim();
            String contrasena = etContrasenaRegistro.getText().toString();
            String confirmar = etConfirmarContrasena.getText().toString();

            if(correo.isEmpty() || contrasena.isEmpty() || confirmar.isEmpty()) {
                Toast.makeText(this,"Completa todos los campos",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etCorreoRegistro.setError("Correo no valido");
                return;
            }

            if(contrasena.length()< 6) {
                etContrasenaRegistro.setError("La contraseña debe tener al menos 6 caracteres");
                return;
            }

            if(!contrasena.equals(confirmar)) {
                etConfirmarContrasena.setError("Las contraseñas no coinciden");
                return;
            }

            auth.createUserWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();

                            Map<String, Object> usuario = new HashMap<>();
                            usuario.put("uid", uid);
                            usuario.put("correo", correo);

                            db.collection("usuarios")
                                    .document(uid)
                                    .set(usuario)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this,"Cuenta creada correctamente",
                                                Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(Registro.this, HomeScreen.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this,"Cuenta creada, pero fallo el guardado en firestore",
                                                    Toast.LENGTH_LONG).show());
                        } else {
                            Toast.makeText(this,"Error al crear la cuenta:" + task.getException()
                                            .getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}