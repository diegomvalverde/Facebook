package com.example.proyectoii.MenuFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.proyectoii.BeforeLogin.LogInActivity;
import com.example.proyectoii.R;
import com.google.firebase.auth.FirebaseAuth;


public class OptionsFragment extends Fragment {
    private View myView;
    private RelativeLayout rLPeril,rLSalir;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_options,container,false);

        rLPeril = myView.findViewById(R.id.relativeLayout_Options_Profile);
        rLSalir = myView.findViewById(R.id.relativeLayout_Options_Salir);

        rLSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.signOut();
                Intent intent = new Intent(getContext(), LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        return myView;
    }



}
