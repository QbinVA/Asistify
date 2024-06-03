package com.example.asistify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetallesClase extends AppCompatActivity {

    Button asistencia;
    TextView detallesNombreClase, detallesCodigo;

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

        asistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetallesClase.this, "Asistencia Registrada", Toast.LENGTH_SHORT).show();
            }
        });

    }
}