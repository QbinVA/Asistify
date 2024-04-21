package com.example.asistify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonAlumno, buttonMaestro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAlumno = findViewById(R.id.btnAlumno);
        buttonMaestro = findViewById(R.id.btnMaestro);

        buttonAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Iniciar activity correspondiente (login alumno)
                Intent intent = new Intent(MainActivity.this, loginalumno.class);
                startActivity(intent);
            }
        });

        buttonMaestro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Iniciar activity correspondiente (login maestro)
                Intent intent = new Intent(MainActivity.this, loginmaestro.class);
                startActivity(intent);
            }
        });
    }
}