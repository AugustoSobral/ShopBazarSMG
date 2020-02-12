package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.SecurityActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class SecurityActivity extends AppCompatActivity {

    private RelativeLayout password_change_layout;
    private RelativeLayout cel_change_layout;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        getSupportActionBar().setTitle("Segurança");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        password_change_layout = findViewById(R.id.password_change_layout);
        cel_change_layout = findViewById(R.id.cel_change_layout);

        password_change_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Para verificar como o usuário está logado, obtemos uma lista do tipo UserInfo com o método getProviderData() e percorremos todos os ProviderId dessa lista.
                for (UserInfo user : mUser.getProviderData()) {
                    if (user.getProviderId().equals("password")) {
                        startActivity(new Intent(SecurityActivity.this, ConfirmPasswordActivity.class));
                    }
                }
                for (UserInfo user : mUser.getProviderData()) {
                    if (user.getProviderId().equals("phone")) {
                        Toast.makeText(SecurityActivity.this, "Você está logado com um método que não necessita de senha.", Toast.LENGTH_SHORT).show();
                    }
                }

                for (UserInfo user : mUser.getProviderData()) {
                    if (user.getProviderId().equals("facebook.com")) {
                        Toast.makeText(SecurityActivity.this, "Você está logado com um método que não necessita de senha.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        cel_change_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (UserInfo user : mUser.getProviderData()) {
                    if (user.getProviderId().equals("phone")) {
                        startActivity(new Intent(SecurityActivity.this, ConfirmCelActivity.class));
                    }

                }
                for (UserInfo user : mUser.getProviderData()) {
                    if (user.getProviderId().equals("password")) {
                        Toast.makeText(SecurityActivity.this, "Você não está usando autenticação com número de celular.", Toast.LENGTH_SHORT).show();
                    }

                }
                for (UserInfo user : mUser.getProviderData()) {
                    if (user.getProviderId().equals("facebook.com")) {
                        Toast.makeText(SecurityActivity.this, "Você não está usando autenticação com número de celular.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    //Esse trecho de código faz com que o back button do action bar faça exatamente o mesmo que o back button do hardware
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
