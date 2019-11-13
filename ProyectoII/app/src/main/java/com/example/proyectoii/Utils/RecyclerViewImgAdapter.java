package com.example.proyectoii.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectoii.Objetos.UserPreview;
import com.example.proyectoii.ProfileView;
import com.example.proyectoii.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewImgAdapter extends RecyclerView.Adapter<RecyclerViewImgAdapter.ViewHolder> {

    private ArrayList<String> usersImgs;
    private Context mContext;

    public RecyclerViewImgAdapter(Context context, ArrayList<String> imgs) {
        mContext = context;
        usersImgs = imgs;
    }

    @NonNull
    @Override
    public RecyclerViewImgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_img_gallery,parent,false);
        RecyclerViewImgAdapter.ViewHolder holder = new RecyclerViewImgAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewImgAdapter.ViewHolder holder, final int position) {
        String imageLink = usersImgs.get(position);

        if (!imageLink.isEmpty()) {
            Uri muri = Uri.parse(imageLink);
            Glide.with(mContext).load(muri).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return usersImgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgview);
        }
    }
}
