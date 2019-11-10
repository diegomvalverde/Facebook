package com.example.proyectoii.Utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.bumptech.glide.Glide;
import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.example.proyectoii.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewPostAdapter extends RecyclerView.Adapter<RecyclerViewPostAdapter.ViewHolder> {

    private ArrayList<PostWithUser> post;
    private Context mContext;
    private OnPostListener onPostListener;
    public final static Reaction[] reactions   = new Reaction[7];

    public RecyclerViewPostAdapter(Context mContext, ArrayList<PostWithUser> post,OnPostListener onPostListener) {
        this.post = post;
        this.mContext = mContext;
        this.onPostListener = onPostListener;

        reactions[0] = new Reaction("Me gusta", "Default","#616770", R.drawable.ic_gray_like);
        reactions[1] = new Reaction("Me gusta","#0366d6",R.drawable.ic_like);
        reactions[2] = new Reaction("Me encanta","#f0716b",R.drawable.ic_heart);
        reactions[3] = new Reaction("Me divierte","#f0ba15",R.drawable.ic_happy);
        reactions[4] = new Reaction("Me asombra","#f0ba15",R.drawable.ic_surprise);
        reactions[5] =  new Reaction("No me gusta","#0366d6",R.mipmap.ic_dislike);
        reactions[6] = new Reaction("Me enoja","#f15268",R.drawable.ic_angry);



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post,parent,false);
        ViewHolder holder = new ViewHolder(view,onPostListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.postWithUser = post.get(position);


        switch (post.get(position).getTipo()){
            case "IMAGE":
                Glide.with(mContext).load(holder.postWithUser.getImageURI()).into(holder.imgPost);
                ViewGroup.LayoutParams params= holder.imgPost.getLayoutParams();
                params.height = 600;
                holder.imgPost.setLayoutParams(params);
                break;
            case "VIDEO":
                break;
        }

        holder.textPost.setText(post.get(position).getDescripcion());
        holder.textNombreAutor.setText(post.get(position).getAuthorName());
        holder.textFechaPost.setText(holder.postWithUser.obtenerTimestampDifference());
        Reaccion reaccion = new Reaccion(MenuActivity.usuario.getId(),0);
        if(holder.postWithUser.getReacciones().contains(reaccion)){
            Reaccion reaccionUsuario = holder.postWithUser.getReacciones().get(holder.postWithUser.getReacciones().indexOf(reaccion));

            holder.reactButton.setCurrentReaction(reactions[reaccionUsuario.getTipoReaccion()]);
        }
        else{
            holder.reactButton.setCurrentReaction(holder.reactButton.getDefaultReaction());
        }



      actualizarReacciones(holder);

    }

    private void actualizarReacciones(ViewHolder holder) {
        int[] topReacciones = holder.postWithUser.getTopReacciones();

        if(topReacciones[0] != 0){
            holder.textNumReacciones.setText(String.valueOf(holder.postWithUser.getReacciones().size()));
            holder.textNumReacciones.setVisibility(View.VISIBLE);
            holder.reaccionTop1.setImageResource(topReacciones[0]);
            holder.reaccionTop1.setVisibility(View.VISIBLE);
            holder.layoutReacciones.setVisibility(View.VISIBLE);


            if(topReacciones[1] != 0){
                holder.reaccionTop2.setImageResource(topReacciones[1]);
                holder.reaccionTop2.setVisibility(View.VISIBLE);

                if(topReacciones[2] != 0){
                    holder.reaccionTop3.setImageResource(topReacciones[2]);
                    holder.reaccionTop3.setVisibility(View.VISIBLE);
                }
            }
        }

        else{
            holder.textNumReacciones.setVisibility(View.GONE);
            holder.reaccionTop1.setVisibility(View.GONE);
            holder.layoutReacciones.setVisibility(View.GONE);
            holder.reaccionTop2.setVisibility(View.GONE);
            holder.reaccionTop3.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        CircleImageView imgPerfil;
        ImageView imgPost;
        TextView textNombreAutor;
        TextView textFechaPost;
        TextView textPost;
        ReactButton reactButton;
        OnPostListener onPostListener;
        PostWithUser postWithUser;
        TextView textNumComentarios;
        TextView textNumReacciones;
        ImageView reaccionTop1,reaccionTop2,reaccionTop3;
        View view;
        RelativeLayout layoutReacciones;
        RelativeLayout contentLayout;

        public ViewHolder(@NonNull View itemView,OnPostListener onPostListener) {
            super(itemView);
            view = itemView;
            contentLayout = itemView.findViewById(R.id.relativeLayout_post_content);
            layoutReacciones = itemView.findViewById(R.id.relativeLayout_reacciones);
            textNumComentarios = itemView.findViewById(R.id.text_numComentarios);
            textNumReacciones = itemView.findViewById(R.id.text_nunReacciones);
            reaccionTop1 = itemView.findViewById(R.id.img_reacciones_1);
            reaccionTop2 = itemView.findViewById(R.id.img_reacciones_2);
            reaccionTop3 = itemView.findViewById(R.id.img_reacciones_3);
            imgPerfil = itemView.findViewById(R.id.img_post_perfil);
            imgPost = itemView.findViewById(R.id.img_post);
            textNombreAutor = itemView.findViewById(R.id.text_post_username);
            textFechaPost = itemView.findViewById(R.id.text_post_date);
            textPost = itemView.findViewById(R.id.text_post);
            reactButton = itemView.findViewById(R.id.likeButton);
            this.onPostListener = onPostListener;
            reactButton.setReactDismissListener(this);
            reactButton.setOnClickListener(this);
            reactButton.setDefaultReaction(reactions[0]);
            reactButton.setReactions(reactions[1],reactions[2],reactions[3],reactions[4],reactions[5],reactions[6]);
            contentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.relativeLayout_post_content:
                    onPostListener.onPostClick(postWithUser);
                    break;
                case R.id.likeButton:
                    onPostListener.onLikeClick(postWithUser,reactButton);
                    actualizarReacciones(this );
                    break;
                case R.id.relativeLayout_post_top:
                    onPostListener.onProfileClick(postWithUser.getAuthorId());
                    break;
                case R.id.linearLayout_comentar:
                    onPostListener.onCommentClick(postWithUser);
                    break;

            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(view.getId() == R.id.likeButton){
                Reaction reaction = reactButton.getCurrentReaction();
                onPostListener.onLikeLongClick(postWithUser,reaction);
                actualizarReacciones(this );

                return true;
            }
            return false;
        }
    }


    public interface  OnPostListener{
        void onPostClick(PostWithUser postWithUser);
        void onLikeClick(PostWithUser postWithUser,ReactButton reactButton);
        void onLikeLongClick(PostWithUser postWithUser,Reaction reaction);
        void onCommentClick(PostWithUser postWithUser);
        void onProfileClick(String idUser);

    }
}
