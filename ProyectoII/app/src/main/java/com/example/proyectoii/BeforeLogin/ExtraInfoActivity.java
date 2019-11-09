package com.example.proyectoii.BeforeLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ExtraInfoActivity extends AppCompatActivity {
    private GoogleSignInAccount account;
    private Spinner spinnerGenero;
    private DatePicker datePicker;
    private ProgressBar progressBar;
    private boolean connetionInProgress = false;

    private String userID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_info);

        account = getIntent().getParcelableExtra("Account");
        userID = getIntent().getStringExtra("ID");

        datePicker = findViewById(R.id.datePicker_ExtraInfo);
        spinnerGenero = findViewById(R.id.spinner_ExtraInfo);
        progressBar = findViewById(R.id.progressbar_ExtraInfo);


        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                TextView textView = findViewById(R.id.textError_ExtraInfo_Edad);
                ocultarErrorTexto(textView);
            }
        });


    }


    public void onClickBack(View view) {
        if(!connetionInProgress){
            finish();
        }
    }

    public void onClickCrearCuenta(View view) {
        if(!connetionInProgress) {
            if (isFechaNacimientoValida()) {
                String correo = account.getEmail();
                String nombre = account.getGivenName();
                String apellido = account.getFamilyName();
                String genero = spinnerGenero.getSelectedItem().toString();
                String fechaNac = datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear();

                Usuario nuevoUsuario = new Usuario(correo,nombre,apellido,fechaNac,genero);
                nuevoUsuario.setId(userID);
                connetionInProgress = true;
                progressBar.setVisibility(View.VISIBLE);
                registrarGoogle(nuevoUsuario);

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Parametros incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void registrarGoogle(Usuario usuario){
        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef  = mFirebaseDatabase.getReference();

        myRef.child("usuarios")
                .child(usuario.getId())
                .setValue(usuario);

    }


    private java.util.Date getDateFromDatePicker() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth()+1;
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    private boolean isFechaNacimientoValida() {

        Calendar calendarInDate = Calendar.getInstance();
        Calendar calendarToday = Calendar.getInstance();
        Date inDate = getDateFromDatePicker();
        Date today = new Date();

        calendarInDate.setTime(inDate);
        calendarToday.setTime(today);
        calendarToday.add(Calendar.YEAR, -calendarInDate.get(Calendar.YEAR));
        calendarToday.add(Calendar.MONTH, -calendarInDate.get(Calendar.MONTH));
        calendarToday.add(Calendar.DAY_OF_MONTH, -calendarInDate.get(Calendar.DAY_OF_MONTH));

        if (calendarToday.get(Calendar.YEAR) > 12) {
            return true;
        } else {
            TextView textView = findViewById(R.id.textError_ExtraInfo_Edad);
            mostarErrorTexto(textView, "Ingrese una fecha de nacimiento válida");
            return false;
        }

    }

    private void mostarErrorTexto(TextView textView, String textoError) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        textView.setText(textoError);
        params.height = 70;
        textView.setLayoutParams(params);
    }
    private void ocultarErrorTexto(TextView textView) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        params.height = 0;
        textView.setLayoutParams(params);
    }


}
