package com.example.asistify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginalumno extends AppCompatActivity {

    EditText editTextEmail, ediTextPassword;
    Button buttonLogin;
    DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginalumno);

        editTextEmail = findViewById(R.id.edittxtemail);
        ediTextPassword = findViewById(R.id.edittxtnumberpassword);
        buttonLogin = findViewById(R.id.btniniciar);
        databaseHelper = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = ediTextPassword.getText().toString();

                if(!databaseHelper.checkUser(email,password)){
                    if(databaseHelper.addUser(email, password)){
                        Toast.makeText(loginalumno.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        buttonLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(loginalumno.this, menualumno.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else{
                        Toast.makeText(loginalumno.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(databaseHelper.checkUser(email, password)){
                        Toast.makeText(loginalumno.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        buttonLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(loginalumno.this, menualumno.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        Toast.makeText(loginalumno.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}