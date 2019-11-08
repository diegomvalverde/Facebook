package com.example.proyectoii.BeforeLogin;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.proyectoii.MenuActivity;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity {
    private EditText editTextCorreo, editTextContrasena;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])[A-Za-z0-9@$!%*#?&¿¡]{6,}$");
    private ProgressBar progressBar;
    private boolean connetionInProgress = false;
    private SignInButton btnGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth ;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth= FirebaseAuth.getInstance();
      if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
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
        btnGoogle = findViewById(R.id.btn_LogIn_Google);

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean getFocus) {
                if (getFocus) {
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

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                onClickSignIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
       mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    public void onClickCrearCuenta(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickIniciarSesion(View view) {
        if (!connetionInProgress) {

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


    private void iniciarSesion(String correo, String contrasena) {

        mAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
                connetionInProgress = false;
            }
        });
    }


    private void onClickSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    Toast.makeText(LogInActivity.this, "Se ha detectado un error", Toast.LENGTH_SHORT).show();


                }
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            getCurrentUser(account);

                        } else {
                            Toast.makeText(LogInActivity.this, "Se ha detectado un error", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    private void getCurrentUser(final GoogleSignInAccount account){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("usuarios/"+mAuth.getCurrentUser().getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Usuario usuario =  dataSnapshot.getValue(Usuario.class);
               if(usuario == null){
                   Log.i("Resultados",mAuth.getCurrentUser().getUid());
                   mGoogleSignInClient.signOut();
                   Intent intent = new Intent(getApplicationContext(), ExtraInfoActivity.class);
                   intent.putExtra("Account",account);
                   intent.putExtra("ID",mAuth.getCurrentUser().getUid());
                   startActivity(intent);
               }
               else{
                   mGoogleSignInClient.signOut();
                   Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
