package com.example.proyectoii.Objetos;

import android.app.Application;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.MenuFragments.HomeFragment;
import com.example.proyectoii.Post;
import com.example.proyectoii.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendRequest
{
    private String from, to, idRequest;
    private boolean pending, accepted;

    public FriendRequest() {
    }

    public static void sendRequest(FriendRequest friendRequest)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("solicitudes").push().getKey();
        friendRequest.setIdRequest(key);
        mDatabase.child("solicitudes").child(key).setValue(friendRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }
    public FriendRequest(String to) {
        this.from = MenuActivity.usuario.getId();
        this.to = to;
        this.pending = true;
        this.accepted = false;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isAcccepted() {
        return accepted;
    }

    public void setAcccepted(boolean acccepted) {
        this.accepted = acccepted;
    }
}
