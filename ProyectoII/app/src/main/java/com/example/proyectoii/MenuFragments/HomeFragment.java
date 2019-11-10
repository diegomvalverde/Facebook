package com.example.proyectoii.MenuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Post;
import com.example.proyectoii.R;
import com.example.proyectoii.Utils.RecyclerViewPostAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecyclerViewPostAdapter.OnPostListener {
    private ArrayList<PostWithUser> posts;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        ArrayList<String> amigos = MenuActivity.usuario.getAmigos();
        amigos.add(MenuActivity.usuario.getId());
        posts = new ArrayList<>();
        obtenerPost(amigos);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Post.class);
                startActivity(intent);
            }
        });



        return view;
    }


    public void obtenerPost(final ArrayList<String> amigos){

        Query query = myRef
                .orderByPriority();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.child("posts").getChildren()){
                    PostObject post = singleSnapshot.getValue(PostObject.class);
                    String auhotID = post.getAuthorId();
                    if(amigos.contains(auhotID)){
                        Usuario usuario = dataSnapshot.child("usuarios").child(post.getAuthorId()).getValue(Usuario.class);

                        String username = usuario.getNombre() + " " + usuario.getApellido();

                        posts.add(new PostWithUser(usuario.getId(),post.getDescripcion(),post.getTipo(),username));

                    }
                }
                iniciarRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void iniciarRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_home);
        RecyclerViewPostAdapter adapter = new RecyclerViewPostAdapter(getContext(),posts,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public void onPostClick(PostWithUser postWithUser) {

    }

    @Override
    public void onLikeClick(PostWithUser postWithUser) {

    }

    @Override
    public void onLikeLongClick(PostWithUser postWithUser) {

    }

    @Override
    public void onCommentClick(PostWithUser postWithUser) {

    }

    @Override
    public void onProfileClick(String idUser) {

    }
}
