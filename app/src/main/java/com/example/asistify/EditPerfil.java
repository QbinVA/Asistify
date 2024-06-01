package com.example.asistify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class EditPerfil extends AppCompatActivity {

    EditText editname, editemail, editpassword;
    Button savebtn;
    String nameuser, emailuser, passworduser;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editEmail);
        editpassword = findViewById(R.id.editPassword);

        showData();

        savebtn = findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged()) {
                    Toast.makeText(EditPerfil.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                    // Llama al método para volver a menualumno
                    returnToMenuAlumno();
                } else {
                    Toast.makeText(EditPerfil.this, "Ups! Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            }
        });


        editname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No necesitamos esta parte
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No necesitamos esta parte
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Al detectar un cambio en el nombre, actualiza inmediatamente en Firebase
                updateNameInFirebase(s.toString());
            }
        });

        editemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                updateEmailInFirebase(s.toString());
            }
        });

        editpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Al detectar un cambio en la contraseña, actualiza inmediatamente en Firebase
                updatePasswordInFirebase(s.toString());
            }
        });

    }
    private void returnToMenuAlumno() {
        Intent intent = new Intent(EditPerfil.this, menualumno.class);
        // Puedes pasar datos actualizados si es necesario
        intent.putExtra("name", editname.getText().toString());
        intent.putExtra("email", editemail.getText().toString());
        intent.putExtra("password", editpassword.getText().toString());
        startActivity(intent);
        finish();
    }


    private void updateNameInFirebase(String newName) {
        databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("name").setValue(newName);
    }

    private void updateEmailInFirebase(String newEmail) {
        databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("email").setValue(newEmail);
    }

    private void updatePasswordInFirebase(String newPassword) {
        databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("password").setValue(newPassword);
    }


    public boolean isNameChanged() {
        if (!nameuser.equals(editname.getText().toString())) {
            databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("name").setValue(editname.getText().toString());
            nameuser = editname.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmailChanged() {
        if (!emailuser.equals(editemail.getText().toString())) {
            databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("email").setValue(editemail.getText().toString());
            emailuser = editemail.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isPasswordChanged() {
        if (!passworduser.equals(editpassword.getText().toString())) {
            databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("password").setValue(editpassword.getText().toString());
            passworduser = editpassword.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public void showData() {
        Intent intent = getIntent();

        nameuser = intent.getStringExtra("name");
        emailuser = intent.getStringExtra("email");
        passworduser = intent.getStringExtra("password");

        editname.setText(nameuser);
        editemail.setText(emailuser);
        editpassword.setText(passworduser);
    }
}

