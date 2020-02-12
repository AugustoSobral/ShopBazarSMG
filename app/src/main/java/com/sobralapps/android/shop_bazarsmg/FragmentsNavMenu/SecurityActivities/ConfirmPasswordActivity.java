package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.SecurityActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class ConfirmPasswordActivity extends AppCompatActivity {

    private EditText password;
    private Button btn_continue;
    private TextView forgot_password;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        getSupportActionBar().setTitle("Segurança");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        email = mUser.getEmail();

        password = findViewById(R.id.edit_text_password_confirmation);
        btn_continue = findViewById(R.id.btn_continue_password_change);
        forgot_password = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.progressbar_confirm_password);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().isEmpty() || password.length() < 6){
                    password.setError("Senha deve possuir no mínimo 6 caracteres");
                    password.requestFocus();
                    return;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(email, password.getText().toString());
                reauthenticate(credential);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfirmPasswordActivity.this,PasswordResetEmail.class);
                i.putExtra("PASSWORD_RESET_CODE", 1);
                startActivity(i);
            }
        });

    }

    private void reauthenticate(AuthCredential credential) {
        progressBar.setVisibility(View.VISIBLE);

        mUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(ConfirmPasswordActivity.this, ChangePasswordActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof FirebaseAuthException) {
                    String errorCode = ((FirebaseAuthException) e).getErrorCode();

                    switch (errorCode) {

                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(ConfirmPasswordActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(ConfirmPasswordActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;


                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(ConfirmPasswordActivity.this, "Senha incorreta.", Toast.LENGTH_SHORT).show();
                            password.setError("Senha incorreta. ");
                            password.requestFocus();
                            password.setText("");
                            progressBar.setVisibility(View.INVISIBLE);
                            break;

                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(ConfirmPasswordActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(ConfirmPasswordActivity.this, "Uma conta já existe com esse mesmo endereço de e-mail mas com um diferente método de login.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;


                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(ConfirmPasswordActivity.this, "Essa credencial já está vinculada com um usuário diferente.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;

                        case "ERROR_USER_DISABLED":
                            Toast.makeText(ConfirmPasswordActivity.this, "Esse usuário foi desativado por um administrador.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;


                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(ConfirmPasswordActivity.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;


                        case "ERROR_WEAK_PASSWORD":
                            Toast.makeText(ConfirmPasswordActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                            password.setError("The password is invalid it must 6 characters at least");
                            password.requestFocus();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;
                    }
                }else{
                    Toast.makeText(ConfirmPasswordActivity.this, "Sem conexão com a internet.",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}
