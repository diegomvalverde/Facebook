package com.example.proyectoii.MenuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.example.proyectoii.ImageAdapter;
import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.Comentario;
import com.example.proyectoii.Objetos.Educacion;
import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Post;
import com.example.proyectoii.R;
import com.example.proyectoii.Session;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.example.proyectoii.Objetos.Educacion;
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

import static com.example.proyectoii.MenuActivity.usuario;

public class ProfileFragment extends Fragment implements RecyclerViewPostAdapter.OnPostListener {
    private ArrayList<PostWithUser> posts;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    private View view;
    public static int contadorPublicaciones ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile,container,false);
        ArrayList<String> user = new ArrayList<>();
        user.add(usuario.getId());
        posts = new ArrayList<>();
        obtenerPost(user);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Post.class);
                startActivity(intent);
            }
        });


        TextView textView = view.findViewById(R.id.nameTxt);
        textView.setText(String.format("%s %s", usuario.getNombre(), usuario.getApellido()));

        StringBuilder userInfo = new StringBuilder(String.format("%s %s", "Fecha de nacimiento:", usuario.getFechaNac()));
        userInfo.append("\n");
        userInfo.append(String.format("%s %s", "Ciudad:", usuario.getCiudad()));
        userInfo.append("\n");
        userInfo.append(String.format("%s %s", "Contacto:", usuario.getTelefono()));
        userInfo.append("\n");
        userInfo.append(String.format("%s %s", "Correo  de contacto:", usuario.getCorreo()));

        if (usuario.getEducacicion() != null && usuario.getEducacicion().size() > 0)
        for (Educacion education: usuario.getEducacicion())
        {
            userInfo.append("\n");
            userInfo.append(String.format("%s %s", String.format("%s:", education.getTipoInstitucion()), education.getNombreInstitucion()));
        }
        TextView infoTxt = view.findViewById(R.id.infoTxt);

        infoTxt.setText(userInfo.toString());


        // Diplay the user photos in gallery scrollable
        ViewPager viewPager =  view.findViewById(R.id.view_pager);
        String[] images = {"https://images.unsplash.com/photo-1535498730771-e735b998cd64?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80",
                "https://images.unsplash.com/photo-1535498730771-e735b998cd64?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"};
        ImageAdapter adapter = new ImageAdapter(this.getContext(), images);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount()-1);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        contadorPublicaciones = -1;
    }

    public void obtenerPost(final ArrayList<String> amigos){

        Query query = myRef.orderByChild("posts");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DataSnapshot> dataSnapshots = new ArrayList<>();
                for(DataSnapshot singleSnapshot: dataSnapshot.child("posts").getChildren()){
                    dataSnapshots.add(singleSnapshot);
                }

                if(contadorPublicaciones == -1){
                    contadorPublicaciones = dataSnapshots.size();
                    posts = new ArrayList<>();
                }

                int postEncontrados = 0;
                while(contadorPublicaciones > 0 && postEncontrados < 10){
                    DataSnapshot singleSnapshot = dataSnapshots.get(contadorPublicaciones-1);
                    PostObject postObject = singleSnapshot.getValue(PostObject.class);

                    String auhotID = postObject.getAuthorId();

                    if(amigos.contains(auhotID)){
                        Usuario usuario = dataSnapshot.child("usuarios").child(postObject.getAuthorId()).getValue(Usuario.class);

                        String username = usuario.getNombre() + " " + usuario.getApellido();
                        PostWithUser post = new PostWithUser(usuario.getId(),postObject.getDescripcion(),postObject.getTipo(),postObject.getIdPost(),username,usuario.getLinkImgPerfil());

                        for(DataSnapshot reactionSnapshot: singleSnapshot.child("reacciones").getChildren()){
                            String autorReaccion = reactionSnapshot.child("idAutor").getValue(String.class);
                            int tipoReaccion = reactionSnapshot.child("tipoReaccion").getValue(Integer.class);

                            post.getReacciones().add(new Reaccion(autorReaccion,tipoReaccion));
                        }

                        for(DataSnapshot commentSnapshot: singleSnapshot.child("comentarios").getChildren()){
                            post.getComentarios().add(commentSnapshot.getValue(Comentario.class));
                        }

                        switch (post.getTipo()){
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

                iniciarRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onPostClick(PostWithUser postWithUser) {
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
        Intent intent = new Intent(getContext(), VerPublicacionActivity.class);
        intent.putExtra("Post",postWithUser);
        intent.putExtra("Comentario",true);
        startActivity(intent);
    }

    @Override
    public void onProfileClick(String idUser) {
        Toast.makeText(getContext(), "Abrir perfil de " + idUser, Toast.LENGTH_SHORT).show();
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
            myRef.child("posts")
                    .child(post.getIdPost())
                    .child("reacciones")
                    .child(String.valueOf(reaccionIndex))
                    .setValue(null);
            post.getReacciones().remove(reaccionIndex);
        }


    }

    public void iniciarRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerViewPostAdapter adapter = new RecyclerViewPostAdapter(getContext(),posts,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

}
