package com.example.proyectoii.Utils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.reactbutton.ReactButton;
import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewPostAdapter extends RecyclerView.Adapter<RecyclerViewPostAdapter.ViewHolder> {

    private ArrayList<PostWithUser> post = new ArrayList<>();
    private Context mContext;
    private OnPostListener onPostListener;

    public RecyclerViewPostAdapter(Context mContext, ArrayList<PostWithUser> post,OnPostListener onPostListener) {
        this.post = post;
        this.mContext = mContext;
        this.onPostListener = onPostListener;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        CircleImageView imgPerfil;

        ImageView imgPost;
        ImageView imgLike;
        TextView textNombreAutor;
        TextView textFechaPost;
        TextView textLike;
        TextView textPost;
        ReactButton reactButton;
        OnPostListener onPostListener;
        PostWithUser postWithUser;

        public ViewHolder(@NonNull View itemView,OnPostListener onPostListener) {
            super(itemView);
            imgPerfil = itemView.findViewById(R.id.img_post_perfil);
            imgPost = itemView.findViewById(R.id.img_post);
            textNombreAutor = itemView.findViewById(R.id.text_post_username);
            textFechaPost = itemView.findViewById(R.id.text_post_date);
            textPost = itemView.findViewById(R.id.text_post);
            reactButton = itemView.findViewById(R.id.likeButton);
            this.onPostListener = onPostListener;
            reactButton.setReactDismissListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.relativeLayout_post_content:
                    onPostListener.onPostClick(postWithUser);
                    break;
                case R.id.likeButton:
                    onPostListener.onLikeClick(postWithUser);
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
                onPostListener.onLikeLongClick(postWithUser);
            }
            return false;
        }
    }


    public interface  OnPostListener{
        void onPostClick(PostWithUser postWithUser);
        void onLikeClick(PostWithUser postWithUser);
        void onLikeLongClick(PostWithUser postWithUser);
        void onCommentClick(PostWithUser postWithUser);
        void onProfileClick(String idUser);

    }
}
