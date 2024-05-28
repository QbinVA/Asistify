package com.example.asistify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<InfoClases> dataList;
    FirebaseAuth mAuth;

    public MyAdapter(Context context, List<InfoClases> dataList) {
        this.context = context;
        this.dataList = dataList;
        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth aquí
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_itrem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.recMateria.setText(dataList.get(position).getMateria());
        holder.recDocente.setText(dataList.get(position).getDocente());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String currentUser = user.getUid(); // Obtener el UID del usuario actual

                    // Obtener el userId del creador de la clase desde dataList
                    String claseCodigo = dataList.get(position).getCodigo(); // Suponiendo que tienes un método getCodigo()
                    DatabaseReference claseRef = FirebaseDatabase.getInstance().getReference("clases").child(claseCodigo);

                    claseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                InfoClases infoClases = snapshot.getValue(InfoClases.class);
                                if (infoClases != null) {
                                    String userId = infoClases.getUserId(); // Obtener el userId del creador de la clase

                                    // Comparar userId con el UID del usuario actual
                                    if (currentUser.equals(userId)) {
                                        Intent intent = new Intent(context, ListAsistencia.class);
                                        context.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(context, DetallesClase.class);
                                        context.startActivity(intent);
                                    }
                                }
                            } else {
                                // Manejar el caso en el que la clase no existe
                                Toast.makeText(context, "Clase no encontrada", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Manejar error de la base de datos
                            Toast.makeText(context, "Error al obtener datos de la clase", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Manejar el caso donde el usuario no está autenticado
                    Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView recMateria, recDocente;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recMateria = itemView.findViewById(R.id.recMateria);
        recDocente = itemView.findViewById(R.id.recDocente);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
