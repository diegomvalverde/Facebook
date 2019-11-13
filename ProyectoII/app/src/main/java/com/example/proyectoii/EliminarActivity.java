package com.example.proyectoii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoii.BeforeLogin.LogInActivity;
import com.example.proyectoii.Objetos.Comentario;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EliminarActivity extends AppCompatActivity {
    private EditText editTextContrasena;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])[A-Za-z0-9@$!%*#?&¿¡]{6,}$");

    private RelativeLayout rLCargando;

    private static boolean isEliminando;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        rLCargando = findViewById(R.id.relativeLayout_Eliminar_Cargando);
        editTextContrasena = findViewById(R.id.editText_Eliminar_Contrasena);
        isEliminando = false;

        editTextContrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ocultarErrorTexto();
                ocultarErrorTextInput();
            }
        });
    }


    public void onClickBack(View view) {
        if(!isEliminando){
            finish();
        }
    }

    public void onClickEliminar(View view) {
        if(!isEliminando){
            comprobarContrasena();
        }
    }



    private void comprobarContrasena() {
        if(isContrasenaValida()){
            rLCargando.setVisibility(View.VISIBLE);
            isEliminando = true;
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), editTextContrasena.getText().toString());

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        auth.signOut();
                                        eliminarCuenta();
                                    }
                                    else {
                                        isEliminando = false;
                                        rLCargando.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Se ha detactado un error", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });
                        }

                    });

        }
    }

    public void eliminarCuenta(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final DatabaseReference myRef = db.getReference();
        Query query = myRef;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> misAmigos = MenuActivity.usuario.getAmigos();
                final String miID = MenuActivity.usuario.getId();

                //Borrar el usuario de la lista de amigos de sus amigos
                for(String amigo: misAmigos){
                    ArrayList<String> amigos = new ArrayList<>();
                    for(DataSnapshot singleDataSnapshot: dataSnapshot.child("usuarios").child(amigo).child("amigos").getChildren()){
                        amigos.add(singleDataSnapshot.getValue(String.class));
                    }
                    amigos.remove(miID);
                    myRef.child("usuarios").child(amigo).child("amigos").setValue(amigos);
                }
                //Borrar las publicaciones, reacciones y comentarios
                for(DataSnapshot singleDataSnapshot: dataSnapshot.child("posts").getChildren()){
                    PostWithUser post = singleDataSnapshot.getValue(PostWithUser.class);
                    if(post.getAuthorId() == miID){
                        myRef.child("posts").child(post.getIdPost()).setValue(null);
                    }
                    else{
                        Reaccion reaccion = new Reaccion(miID,0);
                        if(post.getReacciones().contains(reaccion)){
                            post.getReacciones().remove(reaccion);
                            myRef.child("posts").child(post.getIdPost()).child("reacciones").setValue(post.getReacciones());
                        }
                        Comentario comentario = new Comentario(miID,"");
                        if(post.getComentarios().contains(comentario)){
                            while (post.getComentarios().contains(comentario)){
                                post.getComentarios().remove(comentario);
                            }
                            myRef.child("posts").child(post.getIdPost()).child("comentarios").setValue(post.getComentarios());
                        }

                    }
                }

                myRef.child("usuarios").child(miID).setValue(null);

                //Falta eliminar solicitudes

                if(!isEliminando){
                    cerrarSesion();
                }
                else {
                    isEliminando=false;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        StorageReference publicacionesRef = storageRef.child("/FotosPublicaciones/"+MenuActivity.usuario.getId()+"/");

        publicacionesRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(!isEliminando){
                    cerrarSesion();
                }
                else {
                    isEliminando=false;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if(!isEliminando){
                    cerrarSesion();
                }
                else {
                    isEliminando=false;
                }
            }
        });

    }

    private boolean isContrasenaValida(){
        boolean isContrasenaValida = true;
        String txtContrasena = editTextContrasena.getText().toString();
        if (txtContrasena.isEmpty()) {
            isContrasenaValida = false;


            mostrarErrorTextInput();

            mostarErrorTexto("Tu contraseña debe tener como mínimo 6 letras");
        } else {
            if (!PASSWORD_PATTERN.matcher(txtContrasena).matches()) {
                isContrasenaValida = false;

                mostrarErrorTextInput();

                mostarErrorTexto("Tu contraseña debe tener como mínimo 6 letras");

            }
        }

        return isContrasenaValida;

    }

    private void mostrarErrorTextInput() {
        TextInputLayout textInputLayout = findViewById(R.id.textInput_Eliminar_Contrasena);
        editTextContrasena.clearFocus();
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#F44336"));
        ViewCompat.setBackgroundTintList(editTextContrasena, colorStateList);
        textInputLayout.setDefaultHintTextColor(colorStateList);

    }

    private void ocultarErrorTextInput() {
        TextInputLayout textInputLayout = findViewById(R.id.textInput_Eliminar_Contrasena);
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#008B8B"));
        ViewCompat.setBackgroundTintList(editTextContrasena, colorStateList);
        textInputLayout.setDefaultHintTextColor(colorStateList);

    }

    private void mostarErrorTexto(String textoError) {
        TextView textView =findViewById(R.id.textError_Eliminar_Contrasena);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        textView.setText(textoError);
        params.height = 70;
        textView.setLayoutParams(params);
    }

    private void ocultarErrorTexto() {
        TextView textView = findViewById(R.id.textError_Eliminar_Contrasena);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        params.height = 0;
        textView.setLayoutParams(params);
    }

    public void cerrarSesion(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        MenuActivity.usuario = null;
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
