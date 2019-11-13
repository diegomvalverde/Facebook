package com.example.proyectoii.ProfileUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.Educacion;
import com.example.proyectoii.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProfileEditor extends AppCompatActivity {
    EditText nameTxt;
    EditText lastnameTxt;
    EditText contactTxt;
    EditText cityTxt;
    EditText mailTxt;
    EditText educationTxt;
    DatePicker datePicker;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();

    public void cancelEdit(View view)
    {
        finish();
    }


    public  void editProfile(View view)
    {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();


        MenuActivity.usuario.setFechaNac(day+"/"+month+"/"+year);
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

//        Saving info in firebase
        myRef.child("usuarios").child(MenuActivity.usuario.getId()).child("ciudad").setValue(MenuActivity.usuario.getCiudad());
        myRef.child("usuarios").child(MenuActivity.usuario.getId()).child("nombre").setValue(MenuActivity.usuario.getNombre());
        myRef.child("usuarios").child(MenuActivity.usuario.getId()).child("apellido").setValue(MenuActivity.usuario.getApellido());
        myRef.child("usuarios").child(MenuActivity.usuario.getId()).child("telefono").setValue(MenuActivity.usuario.getTelefono());
        myRef.child("usuarios").child(MenuActivity.usuario.getId()).child("correo").setValue(MenuActivity.usuario.getCorreo());
        myRef.child("usuarios").child(MenuActivity.usuario.getId()).child("fechaNac").setValue(MenuActivity.usuario.getFechaNac());

        finish();
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
        datePicker = findViewById(R.id.datePicker);

        nameTxt.setText(MenuActivity.usuario.getNombre());
        lastnameTxt.setText(MenuActivity.usuario.getApellido());
        contactTxt.setText(MenuActivity.usuario.getTelefono());
        cityTxt.setText(MenuActivity.usuario.getCiudad());
        mailTxt.setText(MenuActivity.usuario.getCorreo());
        String[] dateList = MenuActivity.usuario.getFechaNac().split("/");
        datePicker.init(Integer.parseInt(dateList[2]), Integer.parseInt(dateList[1])-1, Integer.parseInt(dateList[0]), null);

    }
}
