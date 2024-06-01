package com.example.asistify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class clasesRegistro extends AppCompatActivity {

    Button back, crearClase;
    EditText nDocente, nClase, nMateria, codigoCC;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clases_registro);

        nDocente = findViewById(R.id.nombreDocente);
        nClase = findViewById(R.id.nombreClase);
        nMateria = findViewById(R.id.materia);
        codigoCC = findViewById(R.id.codigoCC);

        crearClase = findViewById(R.id.btnCrearClase);
        back = findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(clasesRegistro.this, menualumno.class);
                startActivity(intent);
                finish();
            }
        });

        crearClase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    public void uploadData(){
        String docente = nDocente.getText().toString();
        String clase = nClase.getText().toString();
        String materia = nMateria.getText().toString();
        String codigo = codigoCC.getText().toString();

        // Asegurarse de que el usuario esté autenticado
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Obtener el UID del usuario

            // Crear una nueva instancia de InfoClases incluyendo el userId
            InfoClases infoClases = new InfoClases(docente, clase, materia, codigo, userId);

            // Usar el UID del usuario como parte de la referencia en la base de datos
            FirebaseDatabase.getInstance().getReference("clases").child(codigo)
                    .setValue(infoClases).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(clasesRegistro.this, "Clase Creada", Toast.LENGTH_SHORT).show();
                                // Iniciar automáticamente la clase
                                Intent intent = new Intent(clasesRegistro.this, IniciarClase.class);
                                intent.putExtra("codigoClase", codigo); // Pasar el código de la clase a IniciarClase
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(clasesRegistro.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Manejar el caso donde el usuario no está autenticado
            Toast.makeText(clasesRegistro.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
