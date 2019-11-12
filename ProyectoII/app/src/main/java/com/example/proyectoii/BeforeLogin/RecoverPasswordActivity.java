package com.example.proyectoii.BeforeLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoii.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPasswordActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private TextView textError;
    private TextInputLayout textInputCorreo;
    private EditText editTextCorreo;
    private RelativeLayout relativeLayoutCharging;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        mAuth = FirebaseAuth.getInstance();

        textError = findViewById(R.id.textError_RecoverPass_Correo);
        textInputCorreo = findViewById(R.id.textInput_RecoverPass_Correo);
        editTextCorreo = findViewById(R.id.editText_RecoverPass_Correo);
        relativeLayoutCharging = findViewById(R.id.relativeLayout_RecoverPass_charging);

        editTextCorreo.setOnFocusChangeListener(this);


    }


    public void onClickBack(View view){
        finish();
    }

    public void onClickRecuperarContrasena(View view){
        if (isCorreoValido()){
            relativeLayoutCharging.setVisibility(View.VISIBLE);
            enviarCorreo(editTextCorreo.getText().toString());
        }
    }

    private void enviarCorreo(String correo) {
        mAuth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                relativeLayoutCharging.setVisibility(View.GONE);

                if (task.isSuccessful()){
                    Toast.makeText(RecoverPasswordActivity.this, "Correo enviado con éxito", Toast.LENGTH_SHORT).show();
                    editTextCorreo.setText("");
                }
                else{
                    Toast.makeText(RecoverPasswordActivity.this, "Se ha detectado un error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.getId() == R.id.editText_RecoverPass_Correo){
            ocultarErrorTextInput(editTextCorreo, textInputCorreo);
            ocultarErrorTexto(textError);
        }
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

    private boolean isCorreoValido(){
        boolean isCorreoValido = true;
        String txtCorreo = editTextCorreo.getText().toString();
        if (txtCorreo.isEmpty()) {
            isCorreoValido = false;
            mostrarErrorTextInput(editTextCorreo, textInputCorreo);

            mostarErrorTexto(textError, "Ingresa una dirección de correo electrónico válida");
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(txtCorreo).matches()) {
                isCorreoValido = false;

                mostrarErrorTextInput(editTextCorreo, textInputCorreo);

                mostarErrorTexto(textError, "Ingresa una dirección de correo electrónico válida");
            }
        }

        return  isCorreoValido;
    }
}
