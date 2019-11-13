package com.example.proyectoii.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.FriendRequest;
import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Post;
import com.example.proyectoii.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecyclerViewFriendRequestAdapter extends RecyclerView.Adapter<RecyclerViewFriendRequestAdapter.ViewHolderNotifications> {
    private ArrayList<FriendRequest> friendRequests = new ArrayList<>();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    private ArrayList<Usuario> usuarios =  new ArrayList<>();

    public RecyclerViewFriendRequestAdapter() {

        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.child("solicitudes").getChildren()){
                    FriendRequest friendRequest = singleSnapshot.getValue(FriendRequest.class);
                    assert friendRequest != null;
                    if (friendRequest.getTo().equals(MenuActivity.usuario.getId()))
                    {
                        friendRequests.add(friendRequest);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addValueEventListener(requestListener);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.child("usuarios").getChildren()){
                    Usuario usuario = singleSnapshot.getValue(Usuario.class);
                    assert usuario != null;
                    usuarios.add(usuario);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addValueEventListener(userListener);
    }

    @NonNull
    @Override
    public RecyclerViewFriendRequestAdapter.ViewHolderNotifications onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendrequest, null,  false);
        return new ViewHolderNotifications(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotifications holder, final int position) {
        FriendRequest friendRequest = friendRequests.get(position);
        StringBuilder request = new StringBuilder();
        for (Usuario usuario: usuarios
             ) {
            if (usuario.getId().equals(friendRequest.getFrom()))
            request.append(usuario.getNombre()).append(" ").append(usuario.getApellido()).append(" quiere ser tu amigo");
        }

        holder.asignarDatos(request.toString());

    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public class ViewHolderNotifications extends RecyclerView.ViewHolder {
        TextView noti;
        public ViewHolderNotifications(@NonNull View itemView) {
            super(itemView);
            noti = itemView.findViewById(R.id.messageTxt);
        }

        public void asignarDatos(String s) {
            noti.setText(s);
        }
    }
}
