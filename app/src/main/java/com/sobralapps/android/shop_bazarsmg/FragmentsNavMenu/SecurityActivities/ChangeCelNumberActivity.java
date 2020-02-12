package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.SecurityActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ChangeCelNumberActivity extends AppCompatActivity {

    private EditText cell_number;
    private EditText editText_code;
    private Button btn_continue_cel;
    private Button btn_continue_code;
    private ProgressBar progressBar;
    private LinearLayout ll_cel_number;
    private LinearLayout ll_code;
    private TextView textView_help;
    private TextView textView_title;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String verification_code_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_cel_number);

        getSupportActionBar().setTitle("Segurança");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        cell_number = findViewById(R.id.edit_text_password_confirmation);
        editText_code = findViewById(R.id.edit_text_code_confirmation);
        btn_continue_cel = findViewById(R.id.btn_continue_cel_change);
        btn_continue_code = findViewById(R.id.btn_continue_code_change);
        progressBar = findViewById(R.id.progressbar_confirm_password);
        ll_cel_number = findViewById(R.id.linerLayout_confirm_cel_number);
        ll_code = findViewById(R.id.linerLayout_confirm_code);
        textView_help = findViewById(R.id.text_view_help_confirm_cel_number);
        textView_title = findViewById(R.id.text_view_change_cel_title);

        btn_continue_cel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(cell_number.getText().toString().isEmpty() || cell_number.getText().toString().length()<11){
                    cell_number.setError("Digite um número válido.");
                    cell_number.requestFocus();
                    return;
                }

                String phone_number_with_country_code = "+55" + cell_number.getText().toString();

                if(!isOnline()){
                    Toast.makeText(ChangeCelNumberActivity.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    return;
                }


                sendVerificationCode(phone_number_with_country_code);

            }
        });

        btn_continue_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText_code.getText().toString().trim();

                if(code.isEmpty() || code.length()<6){
                    editText_code.setError("Código inválido");
                    editText_code.requestFocus();
                    return;
                }
                if(!isOnline()){
                    Toast.makeText(ChangeCelNumberActivity.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyCode(code);
            }
        });

    }

    //Esse método envia código de verificação e o callBack a seguir verifica o recebimento do SMS para detecção automática do código.
    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verification_code_id = s;
            ll_cel_number.setVisibility(View.GONE);
            ll_code.setVisibility(View.VISIBLE);
            textView_help.setText("Aguarde, você receberá um código via SMS para verificação que será detectado automaticamente. " +
                    "Caso o código não seja detectado você pode digitá-lo abaixo.");
            textView_title.setText("Código de confirmação do novo número.");

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                editText_code.setText(code);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

    };


    private void verifyCode(String code){
        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code_id, code);
        mUser.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    Toast.makeText(ChangeCelNumberActivity.this, "Número de celular atualizado!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ChangeCelNumberActivity.this, SecurityActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Toast.makeText(ChangeCelNumberActivity.this, "Código incorreto ou este número já está em uso!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}
