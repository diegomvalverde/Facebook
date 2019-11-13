package com.example.proyectoii.MenuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.bumptech.glide.Glide;
import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.Comentario;
import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Post;
import com.example.proyectoii.R;
import com.example.proyectoii.Utils.RecyclerViewPostAdapter;
import com.example.proyectoii.VerPublicacionActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewPostAdapter.OnPostListener {
    private ArrayList<PostWithUser> posts;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    private View view;
    private boolean cargando = false;
    public static int contadorPublicaciones = -1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean reiniciar = true;
    private RecyclerViewPostAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private boolean noMorePost = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);

        progressBar = view.findViewById(R.id.progressBar_Home);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciar = false;
                Intent intent = new Intent(getActivity(), Post.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_home);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<String> amigos = MenuActivity.usuario.getAmigos();
                amigos.add(MenuActivity.usuario.getId());
                contadorPublicaciones = -1;
                noMorePost = false;
                obtenerPost(amigos,false);

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reiniciar = true;
        if(!cargando) {
            if (contadorPublicaciones == -1) {
                cargando = true;
                ArrayList<String> amigos = MenuActivity.usuario.getAmigos();
                amigos.add(MenuActivity.usuario.getId());
                posts = new ArrayList<>();
                obtenerPost(amigos,true);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        reiniciar = true;
        if(!cargando) {
            if (contadorPublicaciones == -1 ) {
                cargando = true;
                ArrayList<String> amigos = MenuActivity.usuario.getAmigos();
                amigos.add(MenuActivity.usuario.getId());
                posts = new ArrayList<>();
                obtenerPost(amigos,true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contadorPublicaciones = -1;
        noMorePost = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(reiniciar){
            contadorPublicaciones = -1;
            noMorePost = false;
        }
    }

    public void obtenerPost(final ArrayList<String> amigos, final Boolean reiniciarTimeline){
        if (reiniciarTimeline || (!reiniciarTimeline && !noMorePost)) {
            progressBar.setVisibility(View.VISIBLE);
            Query query = myRef.orderByChild("posts");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<DataSnapshot> dataSnapshots = new ArrayList<>();
                    for (DataSnapshot singleSnapshot : dataSnapshot.child("posts").getChildren()) {
                        dataSnapshots.add(singleSnapshot);
                    }

                    if (contadorPublicaciones == -1) {
                        contadorPublicaciones = dataSnapshots.size();
                        posts = new ArrayList<>();
                    }

                    int postEncontrados = 0;
                    while (contadorPublicaciones > 0 && postEncontrados < 10) {
                        DataSnapshot singleSnapshot = dataSnapshots.get(contadorPublicaciones - 1);
                        PostObject postObject = singleSnapshot.getValue(PostObject.class);
                        PostWithUser postPrueba = singleSnapshot.getValue(PostWithUser.class);


                        String auhotID = postObject.getAuthorId();

                        if (amigos.contains(auhotID)) {
                            Usuario usuario = dataSnapshot.child("usuarios").child(postObject.getAuthorId()).getValue(Usuario.class);

                            String username = usuario.getNombre() + " " + usuario.getApellido();
                            PostWithUser post = new PostWithUser(usuario.getId(), postObject.getDescripcion(), postObject.getTipo(), postObject.getIdPost(), username, usuario.getLinkImgPerfil());

                            for (DataSnapshot reactionSnapshot : singleSnapshot.child("reacciones").getChildren()) {
                                //String autorReaccion = reactionSnapshot.child("idAutor").getValue(String.class);
                                //int tipoReaccion = reactionSnapshot.child("tipoReaccion").getValue(Integer.class);
                                Reaccion reaccion = reactionSnapshot.getValue(Reaccion.class);

                                post.getReacciones().add(reaccion);
                            }

                            for (DataSnapshot commentSnapshot : singleSnapshot.child("comentarios").getChildren()) {
                                post.getComentarios().add(commentSnapshot.getValue(Comentario.class));
                            }

                            switch (post.getTipo()) {
                                case "IMAGE":
                                    post.setImageURI(singleSnapshot.child("imageURI").getValue(String.class));
                                    break;
                                case "VIDEO":
                                    post.setVideoUrl(singleSnapshot.child("videoUrl").getValue(String.class));
                                    break;
                            }

                            post.setFecha(singleSnapshot.child("fecha").getValue(String.class));
                            posts.add(post);

                            postEncontrados++;

                        }

                        contadorPublicaciones--;

                    }
                    if (postEncontrados < 10)
                        noMorePost = true;

                    iniciarRecyclerView(reiniciarTimeline);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


    public void iniciarRecyclerView(Boolean reiniciarTimeline){
        if(reiniciarTimeline) {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView_home);
            adapter = new RecyclerViewPostAdapter(getContext(), posts, this);
            recyclerView.setAdapter(adapter);
            linearLayoutManager = new LinearLayoutManager(this.getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        ArrayList<String> amigos = MenuActivity.usuario.getAmigos();
                        amigos.add(MenuActivity.usuario.getId());
                        obtenerPost(amigos,false);
                        }

                }
            });
            cargando = false;
        }
        else{
            adapter.notifyDataSetChanged();
        }

        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onContentClick(PostWithUser postWithUser) {
        reiniciar = false;
        Intent intent = new Intent(getContext(), VerPublicacionActivity.class);
        intent.putExtra("Post",postWithUser);
        startActivity(intent);
    }

    @Override
    public void onTextClick(PostWithUser postWithUser) {
        reiniciar = false;
        Intent intent = new Intent(getContext(), VerPublicacionActivity.class);
        intent.putExtra("Post",postWithUser);
        startActivity(intent);
    }

    @Override
    public void onLikeClick(PostWithUser postWithUser, ReactButton reactButton) {

        Reaccion reaccion = new Reaccion(MenuActivity.usuario.getId(),0);
        if(postWithUser.getReacciones().contains(reaccion)){
            agregarReaccion(reaccion,postWithUser);
            reactButton.setCurrentReaction(reactButton.getDefaultReaction());
            postWithUser.getReacciones().remove(reaccion);
        }
        else{
            reactButton.setCurrentReaction(RecyclerViewPostAdapter.reactions[1]);
            reaccion.setTipoReaccion(1);
            postWithUser.getReacciones().add(reaccion);
            agregarReaccion(reaccion,postWithUser);
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
            if (tipoReaccion == 0){
                postWithUser.getReacciones().remove(reactionIndex);
            }
            else{
                postWithUser.getReacciones().set(reactionIndex,reaccion);
            }



            agregarReaccion(reaccion,postWithUser);



        }

        else{
            postWithUser.getReacciones().add(reaccion);
            agregarReaccion(reaccion,postWithUser);
        }
    }

    @Override
    public void onCommentClick(PostWithUser postWithUser) {
        reiniciar = false;
        Intent intent = new Intent(getContext(), VerPublicacionActivity.class);
        intent.putExtra("Post",postWithUser);
        intent.putExtra("Comentario",true);
        startActivity(intent);
    }

    @Override
    public void onProfileClick(String idUser) {
        Toast.makeText(getContext(), "Abrir perfil de " + idUser, Toast.LENGTH_SHORT).show();
    }


    public void agregarReaccion(final Reaccion reaccion,final PostWithUser post){
        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef  = mFirebaseDatabase.getReference();
        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Reaccion> arrayList  = new ArrayList<>();
                for(DataSnapshot singledataSnapshot : dataSnapshot.child("posts").child(post.getIdPost()).child("reacciones").getChildren()) {
                    Reaccion reaccionTemp = singledataSnapshot.getValue(Reaccion.class);
                    arrayList.add(reaccionTemp);
                }

                long reaccionIndex = arrayList.indexOf(reaccion);

                if (reaccionIndex == -1){
                    reaccionIndex = arrayList.size();
                }
                if (reaccion.getTipoReaccion() != 0) {
                    myRef.child("posts")
                            .child(post.getIdPost())
                            .child("reacciones")
                            .child(String.valueOf(reaccionIndex))
                            .setValue(reaccion);
                }
                else{
                    arrayList.remove(reaccion);
                    myRef.child("posts")
                            .child(post.getIdPost())
                            .child("reacciones")
                            .setValue(arrayList);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
