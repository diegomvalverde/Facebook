package com.example.proyectoii.ProfileUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.R;

public class ProfileEditor extends AppCompatActivity {
    EditText nameTxt;
    EditText lastnameTxt;
    EditText contactTxt;
    EditText cityTxt;

    public  void editProfileInfo(View view)
    {
        MenuActivity.usuario.setApellido(lastnameTxt.getText().toString());
        MenuActivity.usuario.setNombre(nameTxt.getText().toString());
        MenuActivity.usuario.setCiudad(cityTxt.getText().toString());
        MenuActivity.usuario.setTelefono(contactTxt.getText().toString());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        nameTxt = findViewById(R.id.nameTxt);
        lastnameTxt = findViewById(R.id.lastNameTxt);
        contactTxt = findViewById(R.id.contactTxt);
        cityTxt = findViewById(R.id.cityTxt);

        nameTxt.setText(MenuActivity.usuario.getNombre());
        lastnameTxt.setText(MenuActivity.usuario.getApellido());
        contactTxt.setText(MenuActivity.usuario.getTelefono());
        cityTxt.setText(MenuActivity.usuario.getCiudad());

    }
}
