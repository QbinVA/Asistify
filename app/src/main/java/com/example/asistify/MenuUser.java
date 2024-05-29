package com.example.asistify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuUser extends AppCompatActivity {

    TextView profileName, profileEmail, profilePassword;
    Button editbtn;

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



    public void showUserDB() {
        Intent intent = getIntent();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = preferences.getString("username", ""); // "" es el valor predeterminado si no se encuentra ninguna preferencia
        String userEmail = preferences.getString("email", "");
        String userPassword = preferences.getString("password", "");

        // Mostrar los datos del usuario en los TextViews correspondientes
        profileName.setText(userName);
        profileEmail.setText(userEmail);
        profilePassword.setText(userPassword);
    }

    public void passUserData() {
        String userName = profileName.getText().toString().trim();

        // Verificar si userName está vacío
        if (!userName.isEmpty()) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUserDatabase = reference.orderByChild("username").equalTo(userName);

            checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String namefromDB = snapshot.child(userName).child("name").getValue(String.class);
                        String emailfromDB = snapshot.child(userName).child("email").getValue(String.class);
                        String passwordfromDB = snapshot.child(userName).child("password").getValue(String.class);

                        Intent intent = new Intent(MenuUser.this, EditPerfil.class);
                        intent.putExtra("name", namefromDB);
                        intent.putExtra("email", emailfromDB);
                        intent.putExtra("password", passwordfromDB);

                        startActivity(intent);

                    } else {
                        String namefromDB = snapshot.child(userName).child("name").getValue(String.class);
                        String emailfromDB = snapshot.child(userName).child("email").getValue(String.class);
                        String passwordfromDB = snapshot.child(userName).child("password").getValue(String.class);

                        Intent intent = new Intent(MenuUser.this, EditPerfil.class);
                        intent.putExtra("name", namefromDB);
                        intent.putExtra("email", emailfromDB);
                        intent.putExtra("password", passwordfromDB);

                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar errores de Firebase si es necesario
                    Log.e("MenuUser", "Error en la consulta a Firebase: " + error.getMessage());
                }
            });
        } else {
            // Manejar el caso en que userName está vacío
            // Por ejemplo, mostrar un mensaje al usuario
            Toast.makeText(MenuUser.this, "Nombre de usuario vacío", Toast.LENGTH_SHORT).show();
        }
    }



}
