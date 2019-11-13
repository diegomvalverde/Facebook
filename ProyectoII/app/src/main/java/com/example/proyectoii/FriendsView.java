package com.example.proyectoii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.proyectoii.Objetos.UserPreview;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Utils.RecyclerViewUserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FriendsView extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    ArrayList<Usuario> usuarios = new ArrayList<>();
    Usuario usuario;
    String id;

    ArrayList<UserPreview> allFriends = new ArrayList<>();
    ArrayList<UserPreview> ourFriends = new ArrayList<>();

    public void cancelEdit(View view)
    {
        finish();
    }

    public void all(View view)
    {
        iniciarLista(allFriends);
    }

    public void our(View view)
    {
        iniciarLista(ourFriends);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_view);
        Intent intent = getIntent();
        this.id = intent.getStringExtra("userId");


        Query query = myRef;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario currentUsuario = dataSnapshot.child("usuarios").child(id).getValue(Usuario.class);
                for(DataSnapshot singleSnapshot: dataSnapshot.child("usuarios").getChildren()){
                    Usuario usuario = singleSnapshot.getValue(Usuario.class);
                    if(currentUsuario.getAmigos().contains(usuario.getId())){
                        UserPreview userPreview = new UserPreview(usuario.getNombre() + " " + usuario.getApellido(),usuario.getId(),usuario.getLinkImgPerfil());
                        allFriends.add(userPreview);
                        Toast.makeText(FriendsView.this, usuario.getNombre(), Toast.LENGTH_SHORT).show();
                    }
                }

                for(UserPreview userPreview: allFriends){
                    if(MenuActivity.usuario.getAmigos().contains(userPreview)){
                        ourFriends.add(userPreview);
                    }
                }
                iniciarLista(allFriends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void iniciarLista(ArrayList<UserPreview> friends)
    {
        RecyclerViewUserAdapter recyclerViewUserAdapter = new RecyclerViewUserAdapter(this, friends);
        RecyclerView recyclerView = findViewById(R.id.recyclerFriends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewUserAdapter);
    }



}
