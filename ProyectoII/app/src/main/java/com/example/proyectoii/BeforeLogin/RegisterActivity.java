package com.example.proyectoii.BeforeLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoii.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])[A-Za-z0-9@$!%*#?&¿¡]{6,}$");

    private EditText editTextNombre, editTextApellido, editTextCorreo, editTextContrasena;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inicializa los layouts
        editTextNombre = findViewById(R.id.editText_Register_Nombre);
        editTextApellido = findViewById(R.id.editText_Register_Apellido);
        datePicker = findViewById(R.id.datePicker_Register);
        editTextCorreo = findViewById(R.id.editText_Register_Correo);
        editTextContrasena = findViewById(R.id.editText_Register_Contrasena);

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
        finish();
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
        if(parametosCorrectos()){
            Toast.makeText(getApplicationContext(),"Parametros correctos",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Parametros incorrectos",Toast.LENGTH_SHORT).show();
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
        int month = datePicker.getMonth();
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




}
