package com.example.proyectoii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.bumptech.glide.Glide;
import com.example.proyectoii.BeforeLogin.LogInActivity;
import com.example.proyectoii.Objetos.Comentario;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Utils.RecyclerViewCommentAdapter;
import com.example.proyectoii.Utils.RecyclerViewPostAdapter;
import com.example.proyectoii.Utils.YouTubeFailureRecoveryActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.proyectoii.Utils.RecyclerViewPostAdapter.reactions;

public class VerPublicacionActivity extends YouTubeFailureRecoveryActivity implements RecyclerViewCommentAdapter.OnCommentClickListener {
    private PostWithUser post;
    private CircleImageView imgPerfilAutor, imgCurrentUser;
    private ImageView imgPost;
    private TextView textNombreAutor;
    private TextView textFechaPost;
    private TextView textPost;
    private ReactButton reactButton;
    private TextView textNumComentarios;
    private TextView textNumReacciones;
    private ImageView reaccionTop1, reaccionTop2, reaccionTop3;
    private RelativeLayout layoutReacciones;
    private RelativeLayout profileLayout;
    private RecyclerView recyclerViewComentarios;
    private EditText editTextComentario;
    YouTubePlayerView video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_publicacion);

        Intent intent = getIntent();

        post = intent.getParcelableExtra("Post");
        boolean quiereComentar = intent.getBooleanExtra("Comentario", false);


        imgPerfilAutor = findViewById(R.id.img_post_perfil);
        imgPost = findViewById(R.id.img_vPublicacion_post);
        textNombreAutor = findViewById(R.id.text_post_username);
        textFechaPost = findViewById(R.id.text_post_date);
        textPost = findViewById(R.id.text_vPublicacion);
        reactButton = findViewById(R.id.likeButton);
        textNumComentarios = findViewById(R.id.text_numComentarios);
        textNumReacciones = findViewById(R.id.text_nunReacciones);
        reaccionTop1 = findViewById(R.id.img_reacciones_1);
        reaccionTop2 = findViewById(R.id.img_reacciones_2);
        reaccionTop3 = findViewById(R.id.img_reacciones_3);
        layoutReacciones = findViewById(R.id.relativeLayout_reacciones);
        imgCurrentUser = findViewById(R.id.img_vPublicacion_miComentario);
        recyclerViewComentarios = findViewById(R.id.recyclerView_vPublicacion_comentarios);
        video = findViewById(R.id.video_vPublicacion);
        editTextComentario = findViewById(R.id.editText_vPublicacion_Cometario);
        profileLayout = findViewById(R.id.relativeLayout_vPublicacion_top);

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileView.class);
                intent.putExtra("USERID",post.getAuthorId());
                startActivity(intent);
            }
        });

        reactButton.setDefaultReaction(reactions[0]);
        reactButton.setReactions(reactions[1], reactions[2], reactions[3], reactions[4], reactions[5], reactions[6]);
        Reaccion reaccion = new Reaccion(MenuActivity.usuario.getId(), 0);
        if (post.getReacciones().contains(reaccion)) {
            Reaccion reaccionUsuario = post.getReacciones().get(post.getReacciones().indexOf(reaccion));

            reactButton.setCurrentReaction(reactions[reaccionUsuario.getTipoReaccion()]);
        } else {
            reactButton.setCurrentReaction(reactButton.getDefaultReaction());
        }


        if (!MenuActivity.usuario.getLinkImgPerfil().equals("")) {
            Glide.with(getApplicationContext()).load(MenuActivity.usuario.getLinkImgPerfil()).into(imgCurrentUser);
        } else {
            imgCurrentUser.setImageResource(R.drawable.ic_profile);
        }

        if (!post.getAuthorPhoto().equals("")) {
            Glide.with(getApplicationContext()).load(post.getAuthorPhoto()).into(imgPerfilAutor);
        } else {
            imgPerfilAutor.setImageResource(R.drawable.ic_profile);
        }

        switch (post.getTipo()) {
            case "IMAGE":
                Glide.with(getApplicationContext()).load(post.getImageURI()).into(imgPost);
                imgPost.setVisibility(View.VISIBLE);
                break;
            case "VIDEO":
                video.initialize("AIzaSyDeieVxJzbBFmU5NSge6Q2kVLJsXlDIMKI", this);
                video.setVisibility(View.VISIBLE);
                break;
            default:
                video.setVisibility(View.GONE);
                imgPost.setVisibility(View.GONE);
        }

        textPost.setText(post.getDescripcion());
        textNombreAutor.setText(post.getAuthorName());
        textFechaPost.setText(post.obtenerTimestampDifference());


        obtenerComentarios();
        obtenerReacciones();

        reactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLikeClick();
            }
        });

        reactButton.setReactDismissListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onLikeLongClick();
                return true;
            }
        });

        if (quiereComentar) {
            editTextComentario.requestFocus();
        }

    }

    public void onClickBack(View view) {
        finish();

    }

    public void onClickPublicar(View view) {
        if (!editTextComentario.getText().toString().isEmpty()){
            String textoComentario = editTextComentario.getText().toString();
            String idUsuario = MenuActivity.usuario.getId();
            Comentario comentario = new Comentario(idUsuario,textoComentario);
            subirComentario(comentario);
            hideKeyboard();
        }
        else
            Toast.makeText(this, "No se permiten comentarios vacios", Toast.LENGTH_SHORT).show();
    }

    private void subirComentario(final Comentario comentario) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference();
        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numComentarios = dataSnapshot.child("posts").child(post.getIdPost()).child("comentarios").getChildrenCount();

                myRef.child("posts")
                        .child(post.getIdPost())
                        .child("comentarios")
                        .child(String.valueOf(numComentarios))
                        .setValue(comentario);
                editTextComentario.clearFocus();
                editTextComentario.setText("");
                obtenerComentarios();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void actualizarReacciones() {
        int[] topReacciones = post.getTopReacciones();

        if (topReacciones[0] != 0) {
            textNumReacciones.setText(String.valueOf(post.getReacciones().size()));
            textNumReacciones.setVisibility(View.VISIBLE);
            reaccionTop1.setImageResource(topReacciones[0]);
            reaccionTop1.setVisibility(View.VISIBLE);
            layoutReacciones.setVisibility(View.VISIBLE);


            if (topReacciones[1] != 0) {
                reaccionTop2.setImageResource(topReacciones[1]);
                reaccionTop2.setVisibility(View.VISIBLE);

                if (topReacciones[2] != 0) {
                    reaccionTop3.setImageResource(topReacciones[2]);
                    reaccionTop3.setVisibility(View.VISIBLE);
                }
            }
        } else {
            textNumReacciones.setVisibility(View.GONE);
            reaccionTop1.setVisibility(View.GONE);
            layoutReacciones.setVisibility(View.GONE);
            reaccionTop2.setVisibility(View.GONE);
            reaccionTop3.setVisibility(View.GONE);
        }

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            String tokens[] = post.getVideoUrl().split("v=");
            if (tokens.length == 1) {
                tokens = post.getVideoUrl().split("/");
            }
            String idVideo = tokens[tokens.length - 1];
            player.cueVideo(idVideo);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return video;
    }

    public void obtenerComentarios() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Comentario> comentarios = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.child("posts").child(post.getIdPost()).child("comentarios").getChildren()) {
                    Comentario comentario = singleSnapshot.getValue(Comentario.class);
                    Usuario usuario = dataSnapshot.child("usuarios").child(comentario.getIdAutor()).getValue(Usuario.class);

                    comentario.setNombreAutor(usuario.getNombre() + " " + usuario.getApellido());
                    comentario.setImgUrlAutor(usuario.getLinkImgPerfil());
                    comentarios.add(comentario);
                }
                post.setComentarios(comentarios);
                if (!post.getComentarios().isEmpty()) {
                    if (post.getComentarios().size() == 1){
                        textNumComentarios.setText("1 comentario");
                    }
                    else{
                        textNumComentarios.setText(post.getComentarios().size() + " comentarios");
                    }

                    textNumComentarios.setVisibility(View.VISIBLE);
                }
                else {
                    textNumComentarios.setVisibility(View.GONE);
                }


                iniciarRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void iniciarRecyclerView() {
        RecyclerViewCommentAdapter adapter = new RecyclerViewCommentAdapter(getApplicationContext(), post.getComentarios(), this);
        recyclerViewComentarios.setAdapter(adapter);
        recyclerViewComentarios.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onCommentClick(String idAutor) {
        Intent intent = new Intent(getApplicationContext(), ProfileView.class);
        intent.putExtra("USERID",idAutor);
        startActivity(intent);
    }

    public  void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = this.getCurrentFocus();

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void onLikeClick() {

        Reaccion reaccion = new Reaccion(MenuActivity.usuario.getId(),0);
        if(!post.getReacciones().contains(reaccion)){
            reaccion.setTipoReaccion(1);
            reactButton.setCurrentReaction(reactions[1]);
        }
        else{
            reactButton.setCurrentReaction(reactions[0]);
        }
        agregarReaccion(reaccion);


    }

    public void onLikeLongClick() {
        int tipoReaccion;
        switch (reactButton.getCurrentReaction().getReactType()){
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
        agregarReaccion(reaccion);
    }

    public void agregarReaccion(final Reaccion reaccion){
        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef  = mFirebaseDatabase.getReference();
        final String idPost = post.getIdPost();


        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long index;
                ArrayList<Reaccion> arrayList  = new ArrayList<>();
                for(DataSnapshot singledataSnapshot : dataSnapshot.child("posts").child(idPost).child("reacciones").getChildren()){
                    Reaccion reaccionTemp = singledataSnapshot.getValue(Reaccion.class);
                    arrayList.add(reaccionTemp);
                }
                index = arrayList.indexOf(reaccion);
                if (index == -1){
                    index = arrayList.size();
                    arrayList.add(reaccion);
                }
                else{

                    arrayList.set(Math.toIntExact(index),reaccion);
                }

                if (reaccion.getTipoReaccion() != 0) {
                    myRef.child("posts")
                            .child(idPost)
                            .child("reacciones")
                            .child(String.valueOf(index))
                            .setValue(reaccion);
                }
                else{
                    arrayList.remove(reaccion);
                    myRef.child("posts")
                            .child(idPost)
                            .child("reacciones")
                            .setValue(arrayList);
                }
                post.setReacciones(arrayList);
                actualizarReacciones();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void obtenerReacciones(){
        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef  = mFirebaseDatabase.getReference();
        final String idPost = post.getIdPost();

        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Reaccion> arrayList  = new ArrayList<>();
                for(DataSnapshot singledataSnapshot : dataSnapshot.child("posts").child(idPost).child("reacciones").getChildren()){
                    Reaccion reaccionTemp = singledataSnapshot.getValue(Reaccion.class);
                    arrayList.add(reaccionTemp);
                }

                post.setReacciones(arrayList);

                actualizarReacciones();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
