package com.example.proyectoii.BeforeLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.R;
import com.example.proyectoii.Utils.FirebaseConnector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])[A-Za-z0-9@$!%*#?&¿¡]{6,}$");

    private EditText editTextNombre, editTextApellido, editTextCorreo, editTextContrasena;
    private Spinner spinnerGenero;
    private DatePicker datePicker;
    private ProgressBar progressBar;
    private boolean connetionInProgress = false;
    private Usuario nuevoUsuario;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inicializa las variables
        editTextNombre = findViewById(R.id.editText_Register_Nombre);
        editTextApellido = findViewById(R.id.editText_Register_Apellido);
        datePicker = findViewById(R.id.datePicker_Register);
        editTextCorreo = findViewById(R.id.editText_Register_Correo);
        editTextContrasena = findViewById(R.id.editText_Register_Contrasena);
        progressBar = findViewById(R.id.progressbar_Register);
        spinnerGenero = findViewById(R.id.spinner_Register);
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    nuevoUsuario.setId(mAuth.getCurrentUser().getUid());
                    agregarUsuario();
                    Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };


        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean getFocus) {
                if (getFocus) {
                    TextInputLayout textInputLayout;
                    TextView textView;
                    switch (view.getId()) {
                        case R.id.editText_Register_Nombre:
                            textInputLayout = findViewById(R.id.textInput_Register_Nombre);
                            textView = findViewById(R.id.textError_Register_Nombre);
                            ocultarErrorTextInput(editTextNombre, textInputLayout);
                            ocultarErrorTexto(textView);
                            break;

                        case R.id.editText_Register_Apellido:
                            textInputLayout = findViewById(R.id.textInput_Register_Apellido);
                            textView = findViewById(R.id.textError_Register_Nombre);
                            ocultarErrorTextInput(editTextApellido, textInputLayout);
                            ocultarErrorTexto(textView);
                            break;

                        case R.id.editText_Register_Correo:
                            textInputLayout = findViewById(R.id.textInput_Register_Correo);
                            textView = findViewById(R.id.textError_Register_Correo);
                            ocultarErrorTextInput(editTextCorreo, textInputLayout);
                            ocultarErrorTexto(textView);
                            break;

                        case R.id.editText_Register_Contrasena:
                            textInputLayout = findViewById(R.id.textInput_Register_Contrasena);
                            textView = findViewById(R.id.textError_Register_Contrasena);
                            ocultarErrorTextInput(editTextContrasena, textInputLayout);
                            ocultarErrorTexto(textView);
                            break;
                    }
                }
            }
        };

        //Asigna un focusChange a todos los editText para cambiar el estilo cuando se selecciona
        editTextNombre.setOnFocusChangeListener(focusChangeListener);
        editTextApellido.setOnFocusChangeListener(focusChangeListener);
        editTextCorreo.setOnFocusChangeListener(focusChangeListener);
        editTextContrasena.setOnFocusChangeListener(focusChangeListener);

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                TextView textView = findViewById(R.id.textError_Register_Edad);
                ocultarErrorTexto(textView);
            }
        });

        //Configura el datePicker
        Date today = new Date();
        datePicker.setMaxDate(today.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.YEAR, -120);
        datePicker.setMinDate(c.getTime().getTime());


    }

    public void onClickBack(View view) {
        if(!connetionInProgress){
            finish();
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

    public void onClickCrearCuenta(View view) {
        if(!connetionInProgress) {
            if (parametosCorrectos()) {
                DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                String correo = editTextCorreo.getText().toString();
                String contrasena = editTextContrasena.getText().toString();
                String nombre = editTextNombre.getText().toString();
                String apellido = editTextApellido.getText().toString();
                String genero = spinnerGenero.getSelectedItem().toString();
                String fechaNac = datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear();

                nuevoUsuario = new Usuario(correo,nombre,apellido,fechaNac,genero);
               connetionInProgress = true;
               progressBar.setVisibility(View.VISIBLE);
                registrarCorreo(contrasena);
            } else {
                Toast.makeText(getApplicationContext(), "Parametros incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean parametosCorrectos() {
        boolean parametrosCorrectos = true;


        if(!isNombreValido()){
            parametrosCorrectos = false;
        }

        if (!isFechaNacimientoValida()) {
            parametrosCorrectos = false;

        }

        if(!isCorreoValido()){
            parametrosCorrectos = false;
        }

        if(!isContrasenaValida()){
            parametrosCorrectos = false;
        }



        return parametrosCorrectos;
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
            TextView textView = findViewById(R.id.textError_Register_Edad);
            mostarErrorTexto(textView, "Ingrese una fecha de nacimiento válida");
            return false;
        }

    }

    private boolean isNombreValido(){
        Boolean isNombreValido = true;
        if (editTextNombre.getText().toString().isEmpty() && editTextApellido.getText().toString().isEmpty()) {
            isNombreValido = false;
            TextView textView = findViewById(R.id.textError_Register_Nombre);
            TextInputLayout textInputLayoutNombre = findViewById(R.id.textInput_Register_Nombre);
            TextInputLayout textInputLayoutApellido = findViewById(R.id.textInput_Register_Apellido);

            mostrarErrorTextInput(editTextNombre, textInputLayoutNombre);
            mostrarErrorTextInput(editTextApellido, textInputLayoutApellido);

            mostarErrorTexto(textView, "Ingresa tu nombre y apellido");
        } else {
            if (editTextNombre.getText().toString().isEmpty()) {
                isNombreValido = false;
                TextInputLayout textInputLayoutNombre = findViewById(R.id.textInput_Register_Nombre);
                TextView textView = findViewById(R.id.textError_Register_Nombre);

                mostrarErrorTextInput(editTextNombre, textInputLayoutNombre);

                mostarErrorTexto(textView, "Ingresa tu nombre");
            } else {
                if (editTextApellido.getText().toString().isEmpty()) {
                    isNombreValido = false;
                    TextView textView = findViewById(R.id.textError_Register_Nombre);
                    TextInputLayout textInputLayoutApellido = findViewById(R.id.textInput_Register_Apellido);

                    mostrarErrorTextInput(editTextApellido, textInputLayoutApellido);

                    mostarErrorTexto(textView, "Ingresa tu apellido");
                }
            }
        }

        return isNombreValido;
    }

    private boolean isCorreoValido(){
        boolean isCorreoValido = true;
        String txtCorreo = editTextCorreo.getText().toString();
        if (txtCorreo.isEmpty()) {
            isCorreoValido = false;
            TextView textView = findViewById(R.id.textError_Register_Correo);
            TextInputLayout textInputLayout = findViewById(R.id.textInput_Register_Correo);

            mostrarErrorTextInput(editTextCorreo, textInputLayout);

            mostarErrorTexto(textView, "Ingresa una dirección de correo electrónico válida");
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(txtCorreo).matches()) {
                isCorreoValido = false;
                TextView textView = findViewById(R.id.textError_Register_Correo);
                TextInputLayout textInputLayout = findViewById(R.id.textInput_Register_Correo);

                mostrarErrorTextInput(editTextCorreo, textInputLayout);

                mostarErrorTexto(textView, "Ingresa una dirección de correo electrónico válida");
            }
        }

        return  isCorreoValido;
    }

    private boolean isContrasenaValida(){
        boolean isContrasenaValida = true;
        String txtContrasena = editTextContrasena.getText().toString();
        if (txtContrasena.isEmpty()) {
            isContrasenaValida = false;
            TextView textView = findViewById(R.id.textError_Register_Contrasena);
            TextInputLayout textInputLayout = findViewById(R.id.textInput_Register_Contrasena);

            mostrarErrorTextInput(editTextContrasena, textInputLayout);

            mostarErrorTexto(textView, "Tu contraseña debe tener como mínimo 6 letras");
        } else {
            if (!PASSWORD_PATTERN.matcher(txtContrasena).matches()) {
                isContrasenaValida = false;
                TextView textView = findViewById(R.id.textError_Register_Contrasena);
                TextInputLayout textInputLayout = findViewById(R.id.textInput_Register_Contrasena);

                mostrarErrorTextInput(editTextContrasena, textInputLayout);

                mostarErrorTexto(textView, "Tu contraseña debe tener como mínimo 6 letras");

            }
        }

        return isContrasenaValida;

    }



    private void registrarCorreo(String contrasena){

        mAuth.createUserWithEmailAndPassword(nuevoUsuario.getCorreo(),contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
                connetionInProgress = false;
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void agregarUsuario() {
        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef  = mFirebaseDatabase.getReference();

        myRef.child("usuarios")
                .child(nuevoUsuario.getId())
                .setValue(nuevoUsuario);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
