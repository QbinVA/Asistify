package com.example.asistify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class loginalumno extends AppCompatActivity {

    EditText editTextEmail, ediTextPassword;
    Button buttonLogin, buttonregistro;
    DatabaseHelper myDb;
    TextWatcher textWatcher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginalumno);

        editTextEmail = findViewById(R.id.edittxtemail);
        ediTextPassword = findViewById(R.id.edittxtnumberpassword);
        buttonLogin = findViewById(R.id.btnregistrar);
        buttonregistro = findViewById(R.id.btnregistro);
        myDb = new DatabaseHelper(this);

        buttonLogin.setEnabled(false);
        textWatcher = getTextWatcher();

        editTextEmail.addTextChangedListener(textWatcher);
        ediTextPassword.addTextChangedListener(textWatcher);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = ediTextPassword.getText().toString();
                boolean isValid = myDb.checkLogin(email, password);
                if(isValid){
                    Intent intent = new Intent(loginalumno.this, menualumno.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(loginalumno.this, "Uusario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });



        buttonregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginalumno.this, activity_registro.class);
                startActivity(intent);
            }
        });



    }

    private TextWatcher getTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextEmail.getText().length() > 0 && ediTextPassword.getText().length() > 0){
                    buttonLogin.setEnabled(true);
                } else {
                    buttonLogin.setEnabled(false);
                }
            }
        };
    }
}