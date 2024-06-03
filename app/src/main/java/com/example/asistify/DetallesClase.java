package com.example.asistify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetallesClase extends AppCompatActivity {

    Button asistencia;
    TextView detallesNombreClase, detallesCodigo;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_clase);

        detallesNombreClase = findViewById(R.id.dNombre);
        detallesCodigo = findViewById(R.id.dCodigo);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detallesNombreClase.setText(bundle.getString("NombreClase"));
            detallesCodigo.setText(bundle.getString("Codigo"));
        }

        asistencia = findViewById(R.id.asistencia);

        // Inicializar FirebaseAuth y obtener el usuario actual
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        asistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    String claseCodigo = detallesCodigo.getText().toString();

                    // Obtener referencia a la base de datos para el usuario actual
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                    // Obtener nombre y correo electrónico del usuario usando HelperClass
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                HelperClass user = snapshot.getValue(HelperClass.class);
                                if (user != null) {
                                    String nombre = user.getName();
                                    String email = user.getEmail();

                                    // Registrar la asistencia
                                    registrarAsistencia(claseCodigo, userId, nombre, email);
                                }
                            } else {
                                Toast.makeText(DetallesClase.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DetallesClase.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(DetallesClase.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registrarAsistencia(String claseCodigo, String userId, String nombre, String email) {
        // Obtener la referencia a la base de datos para la asistencia
        databaseReference = FirebaseDatabase.getInstance().getReference("clases").child(claseCodigo).child("asistencias");

        // Registrar la asistencia
        String asistenciaId = databaseReference.push().getKey();
        if (asistenciaId != null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Map<String, Object> asistenciaData = new HashMap<>();
            asistenciaData.put("userId", userId);
            asistenciaData.put("nombre", nombre);
            asistenciaData.put("email", email);
            asistenciaData.put("timestamp", timestamp);

            databaseReference.child(asistenciaId).setValue(asistenciaData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DetallesClase.this, "Asistencia Registrada", Toast.LENGTH_SHORT).show();
                    // Redirigir a menualumno y abrir el fragmento clases_menu
                    Intent intent = new Intent(DetallesClase.this, menualumno.class);
                    intent.putExtra("openFragment", "clases_menu");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DetallesClase.this, "Error al registrar asistencia", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetallesClase.this, "Error al generar ID de asistencia", Toast.LENGTH_SHORT).show();
        }
    }
}
