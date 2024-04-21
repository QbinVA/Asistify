package com.example.asistify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
                if (databaseHelper.checkUser(email,password)) {
                    Toast.makeText(loginalumno.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(loginalumno.this, "Correo o usuario incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}