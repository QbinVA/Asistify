package com.example.asistify;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_registro extends AppCompatActivity {

    EditText emailEdittext, passwordeditext;

    Button registrobtn;
    DatabaseHelper myDb;

    TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDb = new DatabaseHelper(this);
        emailEdittext = findViewById(R.id.edittxtemail);
        passwordeditext = findViewById(R.id.edittxtnumberpassword);
        registrobtn = findViewById(R.id.btnregistrar);

        registrobtn.setEnabled(false); // Desactiva el botón al inicio
        textWatcher = getTextWatcher();

        // Agrega un TextChangedListener a cada campo de entrada de texto
        emailEdittext.addTextChangedListener(textWatcher);
        passwordeditext.addTextChangedListener(textWatcher);

        registrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdittext.getText().toString();
                String password = passwordeditext.getText().toString();
                boolean isInserted = myDb.insertData(email, password);
                if(isInserted){
                    Toast.makeText(activity_registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_registro.this, menualumno.class);
                    startActivity(intent);
                    finish();


                } else {
                    Toast.makeText(activity_registro.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                }
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
                if (emailEdittext.getText().length() > 0 && passwordeditext.getText().length() > 0){
                    registrobtn.setEnabled(true);
                } else {
                    registrobtn.setEnabled(false);
                }
            }
        };
    }
}