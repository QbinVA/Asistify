package com.example.asistify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPerfil extends AppCompatActivity {

    EditText editname, editemail, editpassword;
    Button savebtn;
    String nameuser, emailuser, passworduser;
    DatabaseReference databaseReference;

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

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editEmail);
        editpassword = findViewById(R.id.editPassword);

        showData();

        Button saveButton = findViewById(R.id.savebtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged()){
                    Toast.makeText(EditPerfil.this, "Guardado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditPerfil.this, "Ups! Algo salio mal", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public boolean isNameChanged(){
        if (!nameuser.equals(editname.getText().toString())){
            databaseReference.child(nameuser).child("name").setValue(editname.getText().toString());
            nameuser = editname.getText().toString();
            return true;

        }else{
            return false;
        }
    }

    public boolean isEmailChanged(){
        if (!emailuser.equals(editemail.getText().toString())){
            databaseReference.child(nameuser).child("email").setValue(editemail.getText().toString());
            emailuser = editemail.getText().toString();
            return true;

        }else{
            return false;
        }
    }

    public boolean isPasswordChanged(){
        if (!passworduser.equals(editpassword.getText().toString())){
            databaseReference.child(nameuser).child("password").setValue(editpassword.getText().toString());
            passworduser = editpassword.getText().toString();
            return true;

        }else{
            return false;
        }
    }

    public void showData(){
        Intent intent = getIntent();

        nameuser = intent.getStringExtra("name");
        emailuser = intent.getStringExtra("email");
        passworduser = intent.getStringExtra("password");

        editname.setText(nameuser);
        editemail.setText(emailuser);
        editpassword.setText(passworduser);
    }

}