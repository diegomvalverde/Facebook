package com.example.proyectoii.Utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.bumptech.glide.Glide;
import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.example.proyectoii.Post;
import com.example.proyectoii.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewPostAdapter extends RecyclerView.Adapter<RecyclerViewPostAdapter.ViewHolder> {

    private ArrayList<PostWithUser> post;
    private Context mContext;
    private OnPostListener onPostListener;
    public final static Reaction[] reactions   = new Reaction[7];
    private final String youtubeApiKey = "AIzaSyDeieVxJzbBFmU5NSge6Q2kVLJsXlDIMKI";
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    private final ThumbnailListener thumbnailListener;

    public RecyclerViewPostAdapter(Context mContext, ArrayList<PostWithUser> post,OnPostListener onPostListener) {
        this.post = post;
        this.mContext = mContext;
        this.onPostListener = onPostListener;
        thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
        thumbnailListener = new ThumbnailListener();

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

        if (!holder.postWithUser.getAuthorPhoto().isEmpty()){
            Glide.with(mContext).load(holder.postWithUser.getAuthorPhoto()).into(holder.imgPerfil);
        }
        else{
            holder.imgPerfil.setImageResource(R.drawable.ic_profile);
        }

        switch (post.get(position).getTipo()){
            case "IMAGE":
                holder.relativeLayoutVideo.setVisibility(View.GONE);
                Glide.with(mContext).load(holder.postWithUser.getImageURI()).into(holder.imgPost);
                holder.imgPost.setVisibility(View.VISIBLE);
                break;
            case "VIDEO":
                holder.imgPost.setVisibility(View.GONE);
                String tokens[] = holder.postWithUser.getVideoUrl().split("v=");
                if (tokens.length == 1) {
                    tokens = holder.postWithUser.getVideoUrl().split("/");
                }
                String idVideo = tokens[tokens.length - 1];
                holder.video.setTag(idVideo);
                holder.video.initialize(youtubeApiKey,thumbnailListener);
                holder.relativeLayoutVideo.setVisibility(View.VISIBLE);
                break;
            default:
                holder.relativeLayoutVideo.setVisibility(View.GONE);
                holder.imgPost.setVisibility(View.GONE);
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
        
        if(!holder.postWithUser.getComentarios().isEmpty()){
            if (holder.postWithUser.getComentarios().size() == 1){
                holder.textNumComentarios.setText("1 comentario");
                
            }
            else{
                holder.textNumComentarios.setText(holder.postWithUser.getComentarios().size() + " comentarios");
            }

            holder.textNumComentarios.setVisibility(View.VISIBLE);
        }

        else {
            holder.textNumComentarios.setVisibility(View.GONE);
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
        RelativeLayout profileLayout;
        YouTubeThumbnailView video;
        RelativeLayout relativeLayoutVideo;


        public ViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            view = itemView;
            imgPerfil = itemView.findViewById(R.id.img_post_perfil);
            imgPost = itemView.findViewById(R.id.img_post);
            textNombreAutor = itemView.findViewById(R.id.text_post_username);
            textFechaPost = itemView.findViewById(R.id.text_post_date);
            textPost = itemView.findViewById(R.id.text_post);
            reactButton = itemView.findViewById(R.id.likeButton);
            textNumComentarios = itemView.findViewById(R.id.text_numComentarios);
            textNumReacciones = itemView.findViewById(R.id.text_nunReacciones);
            reaccionTop1 = itemView.findViewById(R.id.img_reacciones_1);
            reaccionTop2 = itemView.findViewById(R.id.img_reacciones_2);
            reaccionTop3 = itemView.findViewById(R.id.img_reacciones_3);
            layoutReacciones = itemView.findViewById(R.id.relativeLayout_reacciones);
            contentLayout = itemView.findViewById(R.id.relativeLayout_post_content);
            video = itemView.findViewById(R.id.video_post);
            relativeLayoutVideo = itemView.findViewById(R.id.relativeLayout_post_video);
            this.onPostListener = onPostListener;

            RelativeLayout relativeLayout = itemView.findViewById(R.id.btn_comentar);
            
            
            reactButton.setReactDismissListener(this);
            reactButton.setOnClickListener(this);
            reactButton.setDefaultReaction(reactions[0]);
            reactButton.setReactions(reactions[1],reactions[2],reactions[3],reactions[4],reactions[5],reactions[6]);
            contentLayout.setOnClickListener(this);
            profileLayout = itemView.findViewById(R.id.relativeLayout_post_top);
            profileLayout.setOnClickListener(this);
            relativeLayoutVideo.setOnClickListener(this);
            textPost.setOnClickListener(this);
            relativeLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.relativeLayout_post_content:
                    onPostListener.onContentClick(postWithUser);

                    break;
                case R.id.likeButton:
                    onPostListener.onLikeClick(postWithUser,reactButton);
                    actualizarReacciones(this );
                    break;
                case R.id.relativeLayout_post_top:
                    onPostListener.onProfileClick(postWithUser.getAuthorId());
                    break;
                case R.id.btn_comentar:
                    onPostListener.onCommentClick(postWithUser);
                    break;
                case R.id.text_post:
                    onPostListener.onTextClick(postWithUser);
                    break;
                case R.id.relativeLayout_post_video:
                    onPostListener.onContentClick(postWithUser);
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
        void onContentClick(PostWithUser postWithUser);
        void onTextClick(PostWithUser postWithUser);
        void onLikeClick(PostWithUser postWithUser,ReactButton reactButton);
        void onLikeLongClick(PostWithUser postWithUser,Reaction reaction);
        void onCommentClick(PostWithUser postWithUser);
        void onProfileClick(String idUser);

    }

    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            thumbnailViewToLoaderMap.put(view, loader);

            view.setImageResource(R.drawable.loading_thumbnail);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.no_thumbnail);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {

        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.no_thumbnail);
        }
    }
}
