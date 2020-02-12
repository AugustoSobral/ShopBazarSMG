package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.SecurityActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText editText_password;
    private EditText editText_password_confirmation;
    private Button btn_change_password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Segurança");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        editText_password = findViewById(R.id.edit_text_new_password);
        editText_password_confirmation = findViewById(R.id.edit_text_new_password_confirmation);
        btn_change_password = findViewById(R.id.btn_continue_password_change);
        progressBar = findViewById(R.id.progressbar_confirm_password);
        
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText_password.getText().toString();
                String passwordConfirmation = editText_password_confirmation.getText().toString();

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
                
                updatePassowrd(password);
            }
        });
    }

    private void updatePassowrd(String password) {
        progressBar.setVisibility(View.VISIBLE);

        mUser.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ChangePasswordActivity.this, "Senha alterada com sucesso.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ChangePasswordActivity.this, SecurityActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangePasswordActivity.this, "Falha ao alterar senha.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}
