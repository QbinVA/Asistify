package com.example.asistify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class clases_menu extends Fragment {

    RecyclerView recyclerView;
    List<InfoClases> datalist;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clases_menu, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        int numberOfColumns = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), numberOfColumns);
        recyclerView.setLayoutManager(gridLayoutManager);

        datalist = new ArrayList<>();

        MyAdapter adapter = new MyAdapter(requireContext(), datalist);
        recyclerView.setAdapter(adapter);

        // Inicializar FirebaseAuth y obtener el usuario actual
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("clases");

            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    datalist.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        InfoClases infoClases = itemSnapshot.getValue(InfoClases.class);
                        datalist.add(infoClases);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejo de errores
                }
            });
        } else {
            // Manejo del caso donde el usuario no está autenticado
        }

        return view;
    }
}
