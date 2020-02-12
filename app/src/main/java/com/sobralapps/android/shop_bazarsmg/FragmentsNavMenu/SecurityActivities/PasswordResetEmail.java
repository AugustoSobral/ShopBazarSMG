package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.SecurityActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.Start_Login_Register.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordResetEmail extends AppCompatActivity {

    private EditText editText_email;
    private Button btn_send_email_for_password_change;
    private ProgressBar progressBar;

    private static int ORIGEM_CODE = 0;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_email);

        getSupportActionBar().setTitle("Segurança");

        editText_email = findViewById(R.id.edit_text_email_confirmation);
        btn_send_email_for_password_change = findViewById(R.id.btn_continue_password_change);
        progressBar = findViewById(R.id.progressbar_confirm_password);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        ORIGEM_CODE = getIntent().getIntExtra("PASSWORD_RESET_CODE", 0);

        btn_send_email_for_password_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editText_email.getText().toString().trim();
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
                if(mUser!= null && mUser.getEmail()!= null && !email.equals(mUser.getEmail())){
                    editText_email.setError("Este não é o e-mail desta conta.");
                    editText_email.requestFocus();
                    return;
                }

                sendPasswordResetEmail(email);

            }
        });
    }

    private void sendPasswordResetEmail(String email){

        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PasswordResetEmail.this, "Você receberá o e-mail em alguns instantes.", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(PasswordResetEmail.this, "Erro de conexão.", Toast.LENGTH_SHORT).show();

                if(ORIGEM_CODE==1) {
                    startActivity(new Intent(PasswordResetEmail.this, ConfirmPasswordActivity.class));
                    finish();
                }
                if(ORIGEM_CODE==2) {
                    startActivity(new Intent(PasswordResetEmail.this, LoginActivity.class));
                    finish();
                }
            }
        });

    }
}
