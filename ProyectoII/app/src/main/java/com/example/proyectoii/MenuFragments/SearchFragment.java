package com.example.proyectoii.MenuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.example.proyectoii.Objetos.UserPreview;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.R;
import com.example.proyectoii.Utils.RecyclerViewPostAdapter;
import com.example.proyectoii.Utils.RecyclerViewUserAdapter;
import com.example.proyectoii.VerPublicacionActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements RecyclerViewPostAdapter.OnPostListener{

    private CheckBox users;
    private CheckBox posts;
    private EditText search;
    private ArrayList<UserPreview> usersList;
    private View view;
    private ArrayList<PostWithUser> postsList;
    private String nombre;
    private Button buscar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search,container,false);

        users = view.findViewById(R.id.userson);
        users.setChecked(true);
        posts = view.findViewById(R.id.postson);
        search = view.findViewById(R.id.criteriobusqueda);
        usersList = new ArrayList<UserPreview>();
        postsList = new ArrayList<>();
        nombre = "";
        buscar = view.findViewById(R.id.buscarbtn);

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleSelection(0);
            }
        });

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleSelection(1);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (users.isChecked()) {
                    if (!s.toString().isEmpty()) {
                        searchUser(s.toString());
                    } else {
                        searchUser("");
                    }
                    setAdapter();
                } else if (posts.isChecked()) {
                    if (!s.toString().isEmpty()) {
                        searchPost(s.toString());
                    }
                    iniciarRecyclerView();
                }

            }
        });

        return view;
    }



    public void singleSelection(int chkbox) {
        if (chkbox==0) {
            users.setChecked(true);
            posts.setChecked(false);
        } else {
            users.setChecked(false);
            posts.setChecked(true);
        }
    }

    public boolean userInLista(String newid) {
        boolean isIn = false;
        for (int i=0; i<usersList.size(); i++) {
            String id = usersList.get(i).getId();
            if (newid.equals(id)) {
                isIn = true;
            }
        }
        return isIn;
    }

    public void searchUser(String s) {
        usersList.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("usuarios").orderByChild("nombre")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        String nombre = dss.child("nombre").getValue().toString() + " " + dss.child("apellido").getValue().toString();
                        final UserPreview userPreview = new UserPreview(nombre, dss.child("id").getValue().toString());
                        userPreview.setProfilepic(dss.child("linkImgPerfil").getValue().toString());
                        usersList.add(userPreview);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        Query query1 = databaseReference1.child("usuarios").orderByChild("apellido")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        if (!userInLista(dss.child("id").getValue().toString())) {
                            String nombre = dss.child("nombre").getValue().toString() + " " + dss.child("apellido").getValue().toString();
                            final UserPreview userPreview = new UserPreview(nombre, dss.child("id").getValue().toString());
                            userPreview.setProfilepic(dss.child("linkImgPerfil").getValue().toString());
                            usersList.add(userPreview);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void searchPost(final String s) {
        postsList.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.orderByPriority();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean found;
                for (DataSnapshot dss: dataSnapshot.child("posts").getChildren()) {
                    String descrp = dss.child("descripcion").getValue().toString();
                    found = descrp.contains(s);
                    if (found) {
                        PostObject post = dss.getValue(PostObject.class);
                        String nombre = dataSnapshot.child("usuarios").child(post.getAuthorId()).child("nombre").getValue().toString();
                        nombre += " " + dataSnapshot.child("usuarios").child(post.getAuthorId()).child("apellido").getValue().toString();
                        String imgPostAuthor = dataSnapshot.child("usuarios").child(post.getAuthorId()).child("linkImgPerfil").getValue().toString();
                        PostWithUser postWithUser = new PostWithUser(post.getAuthorId(), post.getDescripcion(), post.getTipo(),post.getIdPost(), nombre,imgPostAuthor);
                        postWithUser.setFecha(post.getFecha());
                        switch (post.getTipo()) {
                            case "IMAGE" :
                                postWithUser.setImageURI(post.getImageURI());
                                break;
                            case "VIDEO":
                                postWithUser.setVideoUrl(post.getVideoUrl());
                                break;
                        }
                        //TODO add reacciones y comentarios
                        postsList.add(postWithUser);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void setAdapter() {
        RecyclerView recyclerView = view.findViewById(R.id.resultados);
        RecyclerViewUserAdapter userAdapter = new RecyclerViewUserAdapter(getContext(), usersList);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    public void iniciarRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.resultados);
        RecyclerViewPostAdapter adapter = new RecyclerViewPostAdapter(getContext(),postsList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }


    @Override
    public void onContentClick(PostWithUser postWithUser) {
        Intent intent = new Intent(getContext(), VerPublicacionActivity.class);
        intent.putExtra("Post",postWithUser);
        startActivity(intent);
    }

    @Override
    public void onTextClick(PostWithUser postWithUser) {
        Intent intent = new Intent(getContext(), VerPublicacionActivity.class);
        intent.putExtra("Post",postWithUser);
        startActivity(intent);
    }

    @Override
    public void onLikeClick(PostWithUser postWithUser, ReactButton reactButton) {

        Reaccion reaccion = new Reaccion(MenuActivity.usuario.getId(),0);
        if(postWithUser.getReacciones().contains(reaccion)){
            int reactionIndex = postWithUser.getReacciones().indexOf(reaccion);
            agregarReaccion(reaccion,reactionIndex,postWithUser);
            reactButton.setCurrentReaction(reactButton.getDefaultReaction());
        }
        else{
            reactButton.setCurrentReaction(RecyclerViewPostAdapter.reactions[1]);
            reaccion.setTipoReaccion(1);
            postWithUser.getReacciones().add(reaccion);
            agregarReaccion(reaccion,postWithUser.getReacciones().size()-1,postWithUser);
        }

    }

    @Override
    public void onLikeLongClick(PostWithUser postWithUser, Reaction reaction) {
        int tipoReaccion;
        switch (reaction.getReactType()){
            case "Me gusta":
                tipoReaccion = 1;
                break;
            case "Me encanta":
                tipoReaccion = 2;
                break;
            case "Me divierte":
                tipoReaccion = 3;
                break;
            case "Me asombra":
                tipoReaccion = 4;
                break;
            case "No me gusta":
                tipoReaccion = 5;
                break;
            case "Me enoja":
                tipoReaccion = 6;
                break;
            default:
                tipoReaccion = 0;
                break;
        }
        Reaccion reaccion = new Reaccion(MenuActivity.usuario.getId(),tipoReaccion);
        if(postWithUser.getReacciones().contains(reaccion)){
            int reactionIndex = postWithUser.getReacciones().indexOf(reaccion);
            postWithUser.getReacciones().set(reactionIndex,reaccion);
            agregarReaccion(reaccion,reactionIndex,postWithUser);


        }

        else{
            postWithUser.getReacciones().add(reaccion);
            agregarReaccion(reaccion,postWithUser.getReacciones().size()-1,postWithUser);

        }
    }

    @Override
    public void onCommentClick(PostWithUser postWithUser) {

    }

    @Override
    public void onProfileClick(String idUser) {

    }


    public void agregarReaccion(Reaccion reaccion,int reaccionIndex,PostWithUser post){
        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef  = mFirebaseDatabase.getReference();
        if (reaccion.getTipoReaccion() != 0) {
            myRef.child("posts")
                    .child(post.getIdPost())
                    .child("reacciones")
                    .child(String.valueOf(reaccionIndex))
                    .setValue(reaccion);
        }
        else{
            Log.i("Resultados","Entre");
            myRef.child("posts")
                    .child(post.getIdPost())
                    .child("reacciones")
                    .child(String.valueOf(reaccionIndex))
                    .setValue(null);
            post.getReacciones().remove(reaccionIndex);
        }


    }
}

