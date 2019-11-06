package com.example.proyectoii.Utils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewPostAdapter extends RecyclerView.Adapter<RecyclerViewPostAdapter.ViewHolder> {

    private ArrayList<PostWithUser> post = new ArrayList<>();
    private Context mContext;

    public RecyclerViewPostAdapter(Context mContext, ArrayList<PostWithUser> post) {
        this.post = post;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (post.get(position).getTipo()){
            case "IMAGE":
                holder.imgPost.setMaxHeight(325);
                break;
            case "VIDEO":
                break;
        }
        holder.textPost.setText(post.get(position).getDescripcion());
        holder.textNombreAutor.setText(post.get(position).getAuthorName());

    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgPerfil;
        ImageView imgPost;
        ImageView imgLike;
        TextView textNombreAutor;
        TextView textFechaPost;
        TextView textLike;
        TextView textPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPerfil = itemView.findViewById(R.id.img_post_perfil);
            imgPost = itemView.findViewById(R.id.img_post);
            imgLike = itemView.findViewById(R.id.img_post_like);
            textNombreAutor = itemView.findViewById(R.id.text_post_username);
            textFechaPost = itemView.findViewById(R.id.text_post_date);
            textLike = itemView.findViewById(R.id.text_post_like);
            textPost = itemView.findViewById(R.id.text_post);
        }
    }
}
