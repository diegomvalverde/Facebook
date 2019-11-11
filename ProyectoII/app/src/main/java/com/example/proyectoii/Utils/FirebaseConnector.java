package com.example.proyectoii.Utils;



import androidx.annotation.NonNull;

import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseConnector {

    public static boolean resultado;
    public static boolean procesando;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    ArrayList<PostObject> post  = new ArrayList<>();


    public static boolean isUsuarioConectado(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean registrarUsuario(String correo,String contrasena){
        procesando = true;
        mAuth.createUserWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    resultado = true;

                }
                else{
                    resultado = false;
                }
            }
        });



        return  resultado;
    }

    public static boolean iniciarSesion(String correo,String contrasena){
        mAuth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    resultado = true;
                }
                else{
                    resultado = false;
                }
            }
        });

        return  resultado;
    }
    public void obtenerPost(final ArrayList<String> amigos){
        myRef.child("post");
        Query query = myRef
                .orderByPriority();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    PostObject  post = singleSnapshot.getValue(PostObject.class);
                    if(amigos.contains(post.getAuthorId())){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
