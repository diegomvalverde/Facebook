package com.example.proyectoii.Utils;


import android.content.Context;
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
import com.example.proyectoii.Objetos.Comentario;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.R;

import java.util.ArrayList;

public class RecyclerViewCommentAdapter  extends RecyclerView.Adapter<RecyclerViewCommentAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Comentario> comentarios;
    private OnCommentClickListener commentClickListener;

    public RecyclerViewCommentAdapter(Context mContext, ArrayList<Comentario> comentarios, OnCommentClickListener commentClickListener) {
        this.mContext = mContext;
        this.comentarios = comentarios;
        this.commentClickListener = commentClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comentario_item,parent,false);
        RecyclerViewCommentAdapter.ViewHolder holder = new RecyclerViewCommentAdapter.ViewHolder(view,commentClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comentario comentario = comentarios.get(position);

        holder.idAutor = comentario.getIdAutor();
        holder.textNombreAutor.setText(comentario.getNombreAutor());
        holder.textFecha.setText(comentario.obtenerTimestampDifference());
        holder.textComentario.setText(comentario.getComentario());

        if(!comentario.getImgUrlAutor().isEmpty()){
            Glide.with(mContext).load(comentario.getImgUrlAutor()).into(holder.imagenAutor);
        }
        else{
            holder.imagenAutor.setImageResource(R.drawable.ic_profile);
        }

    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imagenAutor;
        TextView textComentario;
        TextView textNombreAutor;
        TextView textFecha;
        String idAutor;
        OnCommentClickListener onCommentClickListener;

        public ViewHolder(@NonNull View itemView,OnCommentClickListener onCommentClickListener) {
            super(itemView);

            imagenAutor = itemView.findViewById(R.id.img_comentario);
            textComentario = itemView.findViewById(R.id.text_comentario);
            textNombreAutor = itemView.findViewById(R.id.text_comentario_autor);
            textFecha = itemView.findViewById(R.id.text_comentario_fecha);
            this.onCommentClickListener = onCommentClickListener;

            imagenAutor.setOnClickListener(this);
            textNombreAutor.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCommentClickListener.onCommentClick(idAutor);
        }
    }


    public interface  OnCommentClickListener{
        void onCommentClick(String idAutor);

    }
}
