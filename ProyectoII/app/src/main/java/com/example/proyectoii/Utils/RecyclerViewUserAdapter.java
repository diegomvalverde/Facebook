package com.example.proyectoii.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectoii.Objetos.UserPreview;
import com.example.proyectoii.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewUserAdapter extends RecyclerView.Adapter<RecyclerViewUserAdapter.ViewHolder> {

    private ArrayList<UserPreview> userslist;
    private Context mContext;

    public RecyclerViewUserAdapter(Context context, ArrayList<UserPreview> users) {
        mContext = context;
        userslist = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String profilePicLink = userslist.get(position).getProfilepic();
        if (!profilePicLink.isEmpty()) {
            Uri muri = Uri.parse(profilePicLink);
            Glide.with(mContext).load(muri).into(holder.profilepic);
        }
        holder.nombre.setText(userslist.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return userslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilepic;
        TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilepic = itemView.findViewById(R.id.profilepic);
            nombre = itemView.findViewById(R.id.nombreuser);
        }

    }
}
