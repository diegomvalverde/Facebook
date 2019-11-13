package com.example.proyectoii.MenuFragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectoii.BeforeLogin.LogInActivity;
import com.example.proyectoii.EliminarActivity;
import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.Comentario;
import com.example.proyectoii.Objetos.PostWithUser;
import com.example.proyectoii.Objetos.Reaccion;
import com.example.proyectoii.R;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.regex.Pattern;


public class OptionsFragment extends Fragment {
    private View myView;
    private RelativeLayout rLPeril,rLSalir,rLEliminar;
    private TextView textName;
    private ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_options,container,false);


        textName = myView.findViewById(R.id.textView_Options_name);
        rLPeril = myView.findViewById(R.id.relativeLayout_Options_Profile);
        rLSalir = myView.findViewById(R.id.relativeLayout_Options_Salir);
        rLEliminar = myView.findViewById(R.id.relativeLayout_Options_Eliminar);
        imageView = myView.findViewById(R.id.imageView_Options);



        if (!MenuActivity.usuario.getLinkImgPerfil().isEmpty()){
            Glide.with(getContext()).load(MenuActivity.usuario.getLinkImgPerfil()).into(imageView);
        }
        else{
            imageView.setImageResource(R.drawable.ic_profile);
        }

        rLPeril.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.setTabSelected(1);
            }
        });
        rLEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),EliminarActivity.class);
                startActivity(intent);
            }
        });
        rLSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });


        textName.setText(MenuActivity.usuario.getNombre()+ " "+MenuActivity.usuario.getApellido());

        return myView;
    }





    public void cerrarSesion(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        MenuActivity.usuario = null;
        Intent intent = new Intent(getContext(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }








}
