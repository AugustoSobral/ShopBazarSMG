package com.sobralapps.android.shop_bazarsmg.Start_Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sobralapps.android.shop_bazarsmg.R;

public class LoginRegisterForAnonimousUser extends AppCompatActivity {

    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_for_anonimous_user);

        login = findViewById(R.id.btn_login_for_anonimous);
        register = findViewById(R.id.btn_register_for_anonimous);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginRegisterForAnonimousUser.this, LoginActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginRegisterForAnonimousUser.this, RegisterOptionsActivity.class));
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
