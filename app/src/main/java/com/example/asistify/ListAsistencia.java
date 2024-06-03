package com.example.asistify;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListAsistencia extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AsistenciaAdapter adapter;
    private List<Asistencia> asistenciaList;
    private DatabaseReference clasesRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_asistencia);

        // Inicializa la lista de asistencias
        asistenciaList = new ArrayList<>();

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAsistencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AsistenciaAdapter(asistenciaList);
        recyclerView.setAdapter(adapter);

        // Obtiene el usuario actualmente autenticado
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Obtiene una referencia a las clases creadas por el usuario actual
            clasesRef = FirebaseDatabase.getInstance().getReference("clases");

            clasesRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot claseSnapshot : dataSnapshot.getChildren()) {
                            InfoClases infoClase = claseSnapshot.getValue(InfoClases.class);
                            if (infoClase != null) {
                                String codigoClase = infoClase.getCodigo();
                                obtenerAsistencias(codigoClase);
                                break; // Asume que solo hay una clase por usuario
                            }
                        }
                    } else {
                        Toast.makeText(ListAsistencia.this, "No se encontró información de la clase", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListAsistencia.this, "Error al obtener información de la clase", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void obtenerAsistencias(String codigoClase) {
        // Obtiene una referencia a las asistencias de la clase actual
        DatabaseReference asistenciasRef = FirebaseDatabase.getInstance().getReference("clases").child(codigoClase).child("asistencias");

        // Escucha los cambios en las asistencias
        asistenciasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Borra la lista actual de asistencias
                asistenciaList.clear();
                // Itera sobre las asistencias
                for (DataSnapshot asistenciaSnapshot : dataSnapshot.getChildren()) {
                    // Obtiene los datos de la asistencia
                    String nombre = asistenciaSnapshot.child("nombre").getValue(String.class);
                    String email = asistenciaSnapshot.child("email").getValue(String.class);
                    String timestamp = asistenciaSnapshot.child("timestamp").getValue(String.class);

                    // Crea un objeto Asistencia y agrégalo a la lista
                    Asistencia asistencia = new Asistencia(nombre, email, timestamp);
                    asistenciaList.add(asistencia);
                }
                // Notifica al adaptador que los datos han cambiado
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja los errores de Firebase
                Toast.makeText(ListAsistencia.this, "Error al obtener las asistencias", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
