package com.example.proyectoii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amrdeveloper.reactbutton.ReactButton;
import com.bumptech.glide.Glide;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.example.proyectoii.Utils.RecyclerViewPostAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.proyectoii.Utils.RecyclerViewPostAdapter.reactions;

public class VerPublicacionActivity extends AppCompatActivity {
    private PostWithUser post;
    private CircleImageView imgPerfilAutor,imgCurrentUser;
    private ImageView imgPost;
    private TextView textNombreAutor;
    private TextView textFechaPost;
    private TextView textPost;
    private ReactButton reactButton;
    private RecyclerViewPostAdapter.OnPostListener onPostListener;
    private TextView textNumComentarios;
    private TextView textNumReacciones;
    private ImageView reaccionTop1,reaccionTop2,reaccionTop3;
    private View view;
    private RelativeLayout layoutReacciones;
    private RelativeLayout contentLayout;
    private RelativeLayout profileLayout;
    private RecyclerView comentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_publicacion);

        Intent intent = getIntent();

        post = intent.getParcelableExtra("Post");
        boolean quiereComentar = intent.getBooleanExtra("Comentario",false);


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
        contentLayout = findViewById(R.id.relativeLayout_vPublicacion_content);
        imgCurrentUser = findViewById(R.id.img_vPublicacion_miComentario);
        comentarios = findViewById(R.id.recyclerView_vPublicacion_comentarios);



        if (!post.getAuthorPhoto().equals("")){
            Glide.with(getApplicationContext()).load(post.getAuthorPhoto()).into(imgPerfilAutor);
        }
        else{
            imgPerfilAutor.setImageResource(R.drawable.ic_profile);
        }

        switch (post.getTipo()){
            case "IMAGE":
                Glide.with(getApplicationContext()).load(post.getImageURI()).into(imgPost);
                ViewGroup.LayoutParams params= imgPost.getLayoutParams();
                params.height = 600;
                imgPost.setLayoutParams(params);
                break;
            case "VIDEO":
                break;
        }

        textPost.setText(post.getDescripcion());
        textNombreAutor.setText(post.getAuthorName());
        textFechaPost.setText(post.obtenerTimestampDifference());
        Reaccion reaccion = new Reaccion(MenuActivity.usuario.getId(),0);
        if(post.getReacciones().contains(reaccion)){
            Reaccion reaccionUsuario = post.getReacciones().get(post.getReacciones().indexOf(reaccion));

            reactButton.setCurrentReaction(reactions[reaccionUsuario.getTipoReaccion()]);
        }
        else{
            reactButton.setCurrentReaction(reactButton.getDefaultReaction());
        }



        actualizarReacciones();



        if(quiereComentar){

        }

    }

    public void onClickBack(View view) {
        finish();

    }


    private void actualizarReacciones() {
        int[] topReacciones = post.getTopReacciones();

        if(topReacciones[0] != 0){
            textNumReacciones.setText(String.valueOf(post.getReacciones().size()));
            textNumReacciones.setVisibility(View.VISIBLE);
            reaccionTop1.setImageResource(topReacciones[0]);
            reaccionTop1.setVisibility(View.VISIBLE);
            layoutReacciones.setVisibility(View.VISIBLE);


            if(topReacciones[1] != 0){
                reaccionTop2.setImageResource(topReacciones[1]);
                reaccionTop2.setVisibility(View.VISIBLE);

                if(topReacciones[2] != 0){
                    reaccionTop3.setImageResource(topReacciones[2]);
                    reaccionTop3.setVisibility(View.VISIBLE);
                }
            }
        }

        else{
            textNumReacciones.setVisibility(View.GONE);
            reaccionTop1.setVisibility(View.GONE);
            layoutReacciones.setVisibility(View.GONE);
            reaccionTop2.setVisibility(View.GONE);
            reaccionTop3.setVisibility(View.GONE);
        }

    }
}