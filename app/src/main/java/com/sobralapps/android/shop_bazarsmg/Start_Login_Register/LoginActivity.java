package com.sobralapps.android.shop_bazarsmg.Start_Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.SecurityActivities.PasswordResetEmail;
import com.sobralapps.android.shop_bazarsmg.HomeActivity;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout LLloginOptins;
    private LinearLayout LLloginEmail;
    private LinearLayout LLloginCel;
    private LinearLayout LLVerifyCel;
    private Button btn_loginCelOption;
    private Button btn_loginEmailOption;
    private Button btn_enter_no_cadastration;
    private Button btn_send_phone_code_verification;
    private Button btn_login_with_verificated_phone_code_manualmente;
    private ImageButton btn_backOptionLoginEmail;
    private ImageButton btn_backOptionLoginCel;
    private ImageButton btn_backNumberVerification;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_cel_number;
    private EditText editText_cel_code;
    private Button btn_loginWithEmail;
    private TextView btn_loginWithFacebook;
    private ProgressBar progressBar;
    private TextView textView_forgot_password;

    private String email;
    private String password;
    private String phone_number;
    private String verification_code_id = null;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Entre");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        LLloginOptins = findViewById(R.id.linerLayout_loginOption);
        LLloginEmail = findViewById(R.id.linearLayou_loginEmail);
        LLloginCel = findViewById(R.id.linearLayou_loginCel);
        LLVerifyCel = findViewById(R.id.linearLayou_cel_verification);
        btn_loginCelOption = findViewById(R.id.btn_continue_images);
        btn_loginEmailOption = findViewById(R.id.btn_registerFacebookOption);
        btn_enter_no_cadastration = findViewById(R.id.btn_enter_no_cadastration);
        btn_backOptionLoginEmail = findViewById(R.id.btn_back_options);
        btn_backOptionLoginCel = findViewById(R.id.btn_back_optionsCel);
        btn_backNumberVerification = findViewById(R.id.btn_back_number_verification);
        btn_send_phone_code_verification = findViewById(R.id.btn_send_phone_code_verification);
        btn_login_with_verificated_phone_code_manualmente = findViewById(R.id.btn_login_with_phone_code_verificated);
        editText_email = findViewById(R.id.editText_email_login);
        editText_password = findViewById(R.id.edit_text_password_register);
        editText_cel_number = findViewById(R.id.editTextCelNumber);
        editText_cel_code = findViewById(R.id.editTextPhoneCode);
        btn_loginWithEmail = findViewById(R.id.btn_login_with_email);
        btn_loginWithFacebook = findViewById(R.id.facebook_login);
        progressBar = findViewById(R.id.progressbar_login);
        textView_forgot_password = findViewById(R.id.forgot_password);

        btn_loginEmailOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLloginOptins.setVisibility(View.INVISIBLE);
                LLloginEmail.setVisibility(View.VISIBLE);
            }
        });

        btn_loginCelOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLloginOptins.setVisibility(View.INVISIBLE);
                LLloginCel.setVisibility(View.VISIBLE);
            }
        });

        btn_backOptionLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLloginOptins.setVisibility(View.VISIBLE);
                LLloginEmail.setVisibility(View.INVISIBLE);
            }
        });

        btn_backOptionLoginCel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLloginOptins.setVisibility(View.VISIBLE);
                LLloginCel.setVisibility(View.INVISIBLE);
            }
        });

        btn_backNumberVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLVerifyCel.setVisibility(View.INVISIBLE);
                LLloginCel.setVisibility(View.VISIBLE);
            }
        });

        btn_loginWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText_email.getText().toString().trim();
                password = editText_password.getText().toString();

                if(email.isEmpty()){
                    editText_email.setError("Digite o e-mail");
                    editText_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editText_email.setError("Digite um e-mail válido");
                    editText_email.requestFocus();
                    return;
                }
                if(password.isEmpty() || password.length() < 6){
                    editText_password.setError("Senha deve possuir no mínimo 6 caracteres");
                    editText_password.requestFocus();
                    return;
                }

                loginUser(email, password);
            }
        });

        btn_enter_no_cadastration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAnonimous();
            }
        });

        btn_loginWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginWithFacebook.class));
            }
        });

        btn_send_phone_code_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_number = editText_cel_number.getText().toString().trim();

                if(phone_number.isEmpty()){
                    editText_cel_number.setError("Digite o número do celular");
                    editText_cel_number.requestFocus();
                    return;
                }
                if(phone_number.length()<11){
                    editText_cel_number.setError("Digite um número válido");
                    editText_cel_number.requestFocus();
                    return;
                }

                if(!isOnline()){
                    Toast.makeText(LoginActivity.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phone_number_with_country_code = "+55" + phone_number;

                sendVerificationCode(phone_number_with_country_code);

            }
        });

        //Caso o código sms não seja detectado automaticamente.
        btn_login_with_verificated_phone_code_manualmente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText_cel_code.getText().toString().trim();

                if(code.isEmpty() || code.length()<6){
                    editText_cel_code.setError("Código inválido");
                    editText_cel_code.requestFocus();
                    return;
                }

                if(!isOnline()){
                    Toast.makeText(LoginActivity.this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyCode(code);
            }
        });

        textView_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, PasswordResetEmail.class);
                i.putExtra("PASSWORD_RESET_CODE", 2);
                startActivity(i);
            }
        });


    }

    private void loginUser(String email, String password){
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof FirebaseAuthException) {
                    String errorCode = ((FirebaseAuthException) e).getErrorCode();

                    switch (errorCode) {


                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(LoginActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(LoginActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;


                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(LoginActivity.this, "Senha incorreta ou este usuário faz login com um método que não requer senha.", Toast.LENGTH_SHORT).show();
                            editText_password.setError("Senha incorreta. ");
                            editText_password.requestFocus();
                            editText_password.setText("");
                            progressBar.setVisibility(View.INVISIBLE);
                            break;

                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(LoginActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(LoginActivity.this, "Uma conta já existe com esse mesmo endereço de e-mail mas com um diferente método de login.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;


                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(LoginActivity.this, "Essa credencial já está vinculada com um usuário diferente.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;

                        case "ERROR_USER_DISABLED":
                            Toast.makeText(LoginActivity.this, "Esse usuário foi desativado por um administrador.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;


                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(LoginActivity.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;


                        case "ERROR_WEAK_PASSWORD":
                            Toast.makeText(LoginActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                            editText_password.setError("The password is invalid it must 6 characters at least");
                            editText_password.requestFocus();
                            progressBar.setVisibility(View.INVISIBLE);
                            break;
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Sem conexão com a internet.",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void loginAnonimous(){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
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
            LLVerifyCel.setVisibility(View.VISIBLE);
            LLloginCel.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                editText_cel_code.setText(code);
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
                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if (task.isSuccessful()) {
                            //Isso retorna verdadeiro ou falso para verificação se o usuário é novo ou se já existe:
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                            if (isNew) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginActivity.this, CompleteProfileForPhoneLoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                            if (!isNew) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Código incorreto!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });




    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mUser!= null && !mUser.isAnonymous()){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
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
