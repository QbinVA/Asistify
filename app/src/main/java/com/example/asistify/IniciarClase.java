package com.example.asistify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class IniciarClase extends AppCompatActivity {

    private Button atras, btnUnirClase;
    private EditText codigoUC;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_clase);

        atras = findViewById(R.id.adios);
        btnUnirClase = findViewById(R.id.btnUnirClase);
        codigoUC = findViewById(R.id.codigoUC);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IniciarClase.this, menualumno.class);
                startActivity(intent);
                finish();
            }
        });

        // Verificar si el intent tiene el código de la clase
        Intent intent = getIntent();
        String codigoClase = intent.getStringExtra("codigoClase");
        if (codigoClase != null) {
            codigoUC.setText(codigoClase); // Rellenar el campo de código de clase
            unirseAClase(codigoClase); // Unirse a la clase automáticamente
        }

        btnUnirClase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoClase = codigoUC.getText().toString().trim();
                unirseAClase(codigoClase);
            }
        });
    }

    private void unirseAClase(String codigoClase) {
        if (TextUtils.isEmpty(codigoClase)) {
            Toast.makeText(IniciarClase.this, "Por favor, ingrese el código de la clase", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(IniciarClase.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        databaseReference.child("clases").child(codigoClase).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    InfoClases infoClases = snapshot.getValue(InfoClases.class);

                    if (infoClases != null) {
                        // Añadir el usuario a la lista de usuarios de la clase
                        databaseReference.child("clases").child(codigoClase).child("usuarios").child(userId).setValue(true);

                        // Añadir la clase a la lista de clases del usuario
                        databaseReference.child("users").child(userId).child("clases").child(codigoClase).setValue(infoClases);

                        Toast.makeText(IniciarClase.this, "Unido a la clase con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(IniciarClase.this, "Código de clase no válido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(IniciarClase.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
