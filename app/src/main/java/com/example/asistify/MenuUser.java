package com.example.asistify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asistify.EditPerfil;
import com.example.asistify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuUser extends AppCompatActivity {

    TextView profileName, profileEmail, profilePassword;
    Button editbtn;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        profileName = findViewById(R.id.profilename);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        editbtn = findViewById(R.id.editbtn);

        showUserDB();

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passUserData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserDB();
    }

    public void showUserDB() {
        String userId = mAuth.getCurrentUser().getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);

                    profileName.setText(name);
                    profileEmail.setText(email);
                    profilePassword.setText(password);

                    // Guardar datos en SharedPreferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuUser.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", name);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();
                } else {
                    Toast.makeText(MenuUser.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenuUser.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void passUserData() {
        String userId = mAuth.getCurrentUser().getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);

                    Intent intent = new Intent(MenuUser.this, EditPerfil.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                } else {
                    Toast.makeText(MenuUser.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenuUser.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
