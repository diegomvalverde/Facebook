package com.example.proyectoii.ProfileUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.Educacion;
import com.example.proyectoii.R;

import java.util.ArrayList;

public class ProfileEditor extends AppCompatActivity {
    EditText nameTxt;
    EditText lastnameTxt;
    EditText contactTxt;
    EditText cityTxt;
    EditText mailTxt;
    EditText educationTxt;

    public  void editProfile(View view)
    {
        MenuActivity.usuario.setApellido(lastnameTxt.getText().toString());
        MenuActivity.usuario.setNombre(nameTxt.getText().toString());
        MenuActivity.usuario.setCiudad(cityTxt.getText().toString());
        MenuActivity.usuario.setTelefono(contactTxt.getText().toString());
        MenuActivity.usuario.setCorreo(mailTxt.getText().toString());

        ArrayList<Educacion> educacions = new ArrayList<>();
        String education = educationTxt.getText().toString();
        String[] lines = education.split("\n");
        for (String line: lines
             ) {
            if (line.contains(":")) {
                String[] edu = line.split(":");
                educacions.add(new Educacion(edu[1], edu[0]));
                Log.d("Education", line);
            }
        }
        MenuActivity.usuario.setEducacicion(educacions);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        nameTxt = findViewById(R.id.nameTxt);
        lastnameTxt = findViewById(R.id.lastNameTxt);
        contactTxt = findViewById(R.id.contactTxt);
        cityTxt = findViewById(R.id.cityTxt);
        mailTxt = findViewById(R.id.mailTxt);
        educationTxt = findViewById(R.id.educationTxt);

        nameTxt.setText(MenuActivity.usuario.getNombre());
        lastnameTxt.setText(MenuActivity.usuario.getApellido());
        contactTxt.setText(MenuActivity.usuario.getTelefono());
        cityTxt.setText(MenuActivity.usuario.getCiudad());

    }
}
