package com.example.proyectoii.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.FriendRequest;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        Usuario usr = new Usuario();
        for (Usuario usuario: usuarios
             ) {
            if (usuario.getId().equals(friendRequest.getFrom())) {
                request.append(usuario.getNombre()).append(" ").append(usuario.getApellido()).append(" quiere ser tu amigo");
                usr = usuario;
            }
        }

        holder.asignarDatos(request.toString(),friendRequest, usr, MenuActivity.usuario );

    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public class ViewHolderNotifications extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noti;
        FriendRequest friendRequest;
        Button accept, cancel;
        Usuario from, me;

        public ViewHolderNotifications(@NonNull View itemView) {
            super(itemView);
            noti = itemView.findViewById(R.id.messageTxt);
            accept = itemView.findViewById(R.id.acceptButton);
            accept.setOnClickListener(this);
            cancel = itemView.findViewById(R.id.cancelButton);
            cancel.setOnClickListener(this);
        }

        public void asignarDatos(String s,FriendRequest friendRequest, Usuario from, Usuario me) {
            noti.setText(s);
            this.friendRequest = friendRequest;
            this.from = from;
            this.me = me;

            if (!friendRequest.isPending())
            {
                accept.setEnabled(false);
                cancel.setEnabled(false);
            }
        }




        @Override
        public void onClick(View v) {
            accept.setEnabled(false);
            cancel.setEnabled(false);
            switch (v.getId())
            {
                case R.id.acceptButton:
                    myRef.child("solicitudes").child(friendRequest.getIdRequest()).child("accepted").setValue(true);
                    myRef.child("solicitudes").child(friendRequest.getIdRequest()).child("pending").setValue(false);

//                    Update Friends
                    ArrayList<String> theirFriends = from.getAmigos();
                    theirFriends.add(me.getId());

                    ArrayList<String> myFriends = me.getAmigos();
                    myFriends.add(from.getId());

                    myRef.child("usuarios").child(from.getId()).child("amigos").setValue(theirFriends);
                    myRef.child("usuarios").child(me.getId()).child("amigos").setValue(myFriends);

                    break;

                case R.id.layout:
                    myRef.child("solicitudes").child(friendRequest.getIdRequest()).child("accepted").setValue(false);
                    myRef.child("solicitudes").child(friendRequest.getIdRequest()).child("pending").setValue(false);
                    break;
            }
        }
    }

}
