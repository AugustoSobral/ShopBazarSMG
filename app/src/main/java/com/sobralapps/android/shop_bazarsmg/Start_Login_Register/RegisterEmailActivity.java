package com.sobralapps.android.shop_bazarsmg.Start_Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.HomeActivity;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText editText_nome_sobrenome;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_password_confirmation;
    private ProgressBar progressBar;
    private Button button_register;
    private String email;
    private String password;
    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

        getSupportActionBar().setTitle("Cadastre-se");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        editText_nome_sobrenome = findViewById(R.id.edit_text_nome_e_sobrenome_register_email_screen);
        editText_email = findViewById(R.id.edit_text_email_register);
        editText_password = findViewById(R.id.edit_text_password_register);
        editText_password_confirmation = findViewById(R.id.edit_text_password_confirmation);
        button_register = findViewById(R.id.btn_continue_images);
        progressBar = findViewById(R.id.progressbar_register_email);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText_email.getText().toString().trim();
                password = editText_password.getText().toString();
                nome = editText_nome_sobrenome.getText().toString();
                String passwordConfirmation = editText_password_confirmation.getText().toString();

                if(nome.isEmpty()){
                    editText_email.setError("Digite seu nome e sobrenome");
                    editText_email.requestFocus();
                    return;
                }

                if(email.isEmpty()){
                    editText_email.setError("Digite o email");
                    editText_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editText_email.setError("Digite um email válido");
                    editText_email.requestFocus();
                    return;
                }
                if(password.isEmpty() || password.length() < 6){
                    editText_password.setError("Senha deve possuir no mínimo 6 caracteres");
                    editText_password.requestFocus();
                    return;
                }
                if(!password.equals(passwordConfirmation)){
                    editText_password.setError("Senhas digitadas estão diferentes");
                    editText_password_confirmation.setError("Senhas digitadas estão diferentes");
                    editText_password.requestFocus();
                    return;
                }


                registerUser(email, password);
            }
        });

    }

    private void registerUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){

                            //Adicionando o nome do usuário.
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nome).build();
                            user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterEmailActivity.this, "Conta registrada com sucesso!",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterEmailActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
                        else{
                            try {
                                throw task.getException();
                            }  catch(FirebaseAuthInvalidCredentialsException e) {
                                editText_email.setError("E-mail inválido.");
                                editText_email.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                editText_email.setError("E-mail já está em uso.");
                                editText_email.requestFocus();
                            } catch(Exception e) {
                                Log.e("Erro ao registrar user", e.getMessage());
                            }
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mUser!= null && !mUser.isAnonymous()){
            Intent intent = new Intent(RegisterEmailActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
