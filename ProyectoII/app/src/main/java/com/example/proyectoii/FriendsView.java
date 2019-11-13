package com.example.proyectoii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.proyectoii.Objetos.UserPreview;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Utils.RecyclerViewUserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    public void all(View view)
    {
        RecyclerViewUserAdapter recyclerViewUserAdapter = new RecyclerViewUserAdapter(this, allFriends);
        RecyclerView recyclerView = findViewById(R.id.recyclerFriends);
        recyclerView.setAdapter(recyclerViewUserAdapter);
    }

    public void our(View view)
    {
        RecyclerViewUserAdapter recyclerViewUserAdapter = new RecyclerViewUserAdapter(this, ourFriends);
        RecyclerView recyclerView = findViewById(R.id.recyclerFriends);
        recyclerView.setAdapter(recyclerViewUserAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_view);
        Intent intent = getIntent();
        this.id = intent.getStringExtra("userId");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.child("usuarios").getChildren()){
                    Usuario usuario = singleSnapshot.getValue(Usuario.class);
                    assert usuario != null;
                    usuarios.add(usuario);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addValueEventListener(userListener);

        for (Usuario usuariotmp: usuarios
             ) {
            if (usuariotmp.getId().equals(id))
            {
                this.usuario = usuariotmp;
            }
        }

        // All users
        for (Usuario usr: usuarios
             ) {
            if (usuario.getAmigos().contains(usr.getId()))
            {
                allFriends.add(new UserPreview(usr.getNombre(), usr.getId(), usr.getLinkImgPerfil()));
            }
        }

        // Our friends
        for (Usuario usr: usuarios
             ) {
            if (usuario.getAmigos().contains(usr.getId()) && MenuActivity.usuario.getAmigos().contains(usr.getId()))
            {
                ourFriends.add(new UserPreview(usr.getNombre(), usr.getId(), usr.getLinkImgPerfil()));
            }
        }

        RecyclerViewUserAdapter recyclerViewUserAdapter = new RecyclerViewUserAdapter(this, allFriends);
        RecyclerView recyclerView = findViewById(R.id.recyclerFriends);
        recyclerView.setAdapter(recyclerViewUserAdapter);


    }

}
