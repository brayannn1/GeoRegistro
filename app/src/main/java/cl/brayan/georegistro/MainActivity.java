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


public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etContrasena;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etContrasena = findViewById(R.id.etContrasena);

        Button btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registro.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);
                    return insets;
                });
    }

        private void iniciarSesion() {
            String correo = etEmail.getText().toString().trim();
            String contrasena = etContrasena.getText().toString();

            if(correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this,
                        "Completa el correo y contraseña",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etEmail.setError("Ingresa un correo valido");
                return;
            }

            mAuth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, task -> {

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(this,
                                    "Error al iniciar sesion",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}