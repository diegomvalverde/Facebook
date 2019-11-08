package com.example.proyectoii.BeforeLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Profile;
import com.example.proyectoii.R;
import com.example.proyectoii.Utils.FirebaseConnector;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity {
    private EditText editTextCorreo, editTextContrasena;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])[A-Za-z0-9@$!%*#?&¿¡]{6,}$");
    private ProgressBar progressBar;
    private boolean connetionInProgress = false;


    @Override
    protected void onStart() {
        super.onStart();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editTextCorreo = findViewById(R.id.editText_LogIn_Correo);
        editTextContrasena = findViewById(R.id.editText_LogIn_Contrasena);
        progressBar = findViewById(R.id.progressbar_Login);

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean getFocus) {
                if(getFocus) {
                    TextInputLayout textInputLayout;
                    switch (view.getId()) {
                        case R.id.editText_LogIn_Correo:
                            textInputLayout = findViewById(R.id.textInput_Login_Correo);
                            ocultarErrorTextInput(editTextCorreo, textInputLayout);
                            break;
                        case R.id.editText_LogIn_Contrasena:
                            textInputLayout = findViewById(R.id.textInput_Login_Contrasena);
                            ocultarErrorTextInput(editTextContrasena, textInputLayout);
                            break;

                    }
                }
            }
        };

        editTextCorreo.setOnFocusChangeListener(focusChangeListener);
        editTextContrasena.setOnFocusChangeListener(focusChangeListener);
    }

    public void onClickCrearCuenta(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickIniciarSesion(View view) {
        if(!connetionInProgress) {

            if (isParametosValidos()) {
                String correo = editTextCorreo.getText().toString();
                String contrasena = editTextContrasena.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                connetionInProgress = true;

                iniciarSesion(correo, contrasena);


            } else {
                Toast.makeText(getApplicationContext(), "Parametos incorrectos", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void onClickRecuperarContrasena(View view) {

    }


    private boolean isParametosValidos() {
        boolean parametosValidos = true;

        if (!isCorreoValido()) {
            parametosValidos = false;
        }

        if (!isContrasenaValida()) {
            parametosValidos = false;
        }


        return parametosValidos;
    }

    private boolean isCorreoValido() {
        boolean isCorreoValido = true;
        String txtCorreo = editTextCorreo.getText().toString();
        if (txtCorreo.isEmpty()) {
            isCorreoValido = false;
            TextInputLayout textInputLayout = findViewById(R.id.textInput_Login_Correo);
            mostrarErrorTextInput(editTextCorreo, textInputLayout);

        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(txtCorreo).matches()) {
                isCorreoValido = false;
                TextInputLayout textInputLayout = findViewById(R.id.textInput_Login_Correo);
                mostrarErrorTextInput(editTextCorreo, textInputLayout);

            }
        }

        return isCorreoValido;
    }


    private void mostrarErrorTextInput(EditText editText, TextInputLayout textInputLayout) {
        editText.clearFocus();
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#F44336"));
        ViewCompat.setBackgroundTintList(editText, colorStateList);
        textInputLayout.setDefaultHintTextColor(colorStateList);

    }

    private void ocultarErrorTextInput(EditText editText, TextInputLayout textInputLayout) {
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#008B8B"));
        ViewCompat.setBackgroundTintList(editText, colorStateList);
        textInputLayout.setDefaultHintTextColor(colorStateList);

    }

    private boolean isContrasenaValida() {
        boolean isContrasenaValida = true;
        String txtContrasena = editTextContrasena.getText().toString();
        if (txtContrasena.isEmpty()) {
            isContrasenaValida = false;
            TextInputLayout textInputLayout = findViewById(R.id.textInput_Login_Contrasena);

            mostrarErrorTextInput(editTextContrasena, textInputLayout);

        } else {
            if (!PASSWORD_PATTERN.matcher(txtContrasena).matches()) {
                isContrasenaValida = false;
                TextInputLayout textInputLayout = findViewById(R.id.textInput_Login_Contrasena);

                mostrarErrorTextInput(editTextContrasena, textInputLayout);

            }
        }

        return isContrasenaValida;
    }


    private void iniciarSesion(String correo,String contrasena){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);

               }
               else{
                   Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
               }
                progressBar.setVisibility(View.INVISIBLE);
               connetionInProgress = false;
            }
        });
    }

}
