package com.example.proyectoii.Utils;



import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseConnector {

    public static boolean resultado;
    public static boolean procesando;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();


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

}
