package com.sobralapps.android.shop_bazarsmg.Start_Login_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sobralapps.android.shop_bazarsmg.R;

public class RegisterOptionsActivity extends AppCompatActivity {

    private Button btn_emailOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_options);

        getSupportActionBar().setTitle("Opções de Cadastro");

        btn_emailOption = findViewById(R.id.btn_continue_images);

        btn_emailOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterOptionsActivity.this, RegisterEmailActivity.class);
                startActivity(intent);
            }
        });
    }
}
