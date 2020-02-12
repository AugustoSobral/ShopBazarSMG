package com.sobralapps.android.shop_bazarsmg.Start_Login_Register;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.HomeActivity;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.Sell.ImageCropperActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteProfileForPhoneLoginActivity extends AppCompatActivity {

    private static int REQUEST_CODE_ADD_IMAGE = 3;

    private ProgressBar progressBar_pic;
    private Button btn_add_profile_pic;
    private Button btn_continue;
    private CircleImageView circleImageView;
    private EditText editText_name;
    private EditText editText_email;

    private Uri imageUri = null;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile_for_phone_login);

        progressBar_pic = findViewById(R.id.progressBar_profile_pic_load);
        btn_add_profile_pic = findViewById(R.id.btn_add_profile_pic);
        circleImageView = findViewById(R.id.profile_circle_image_view);
        editText_email = findViewById(R.id.edit_text_email_register_cel_screen);
        editText_name = findViewById(R.id.edit_text_nome_e_sobrenome_register_cel_screen);
        btn_continue = findViewById(R.id.btn_confirm_profile_data_cel_register);


        btn_add_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CompleteProfileForPhoneLoginActivity.this, ImageCropperActivity.class);
                i.putExtra("IMAGE_SHAPE_CODE", 1);
                startActivityForResult(i, REQUEST_CODE_ADD_IMAGE);
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_name.getText().toString().trim().isEmpty()) {
                    editText_name.setError("Digite seu nome e sobrenome");
                    editText_name.requestFocus();
                    return;
                }

                if (!editText_email.getText().toString().trim().isEmpty()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(editText_email.getText().toString().trim()).matches()) {
                        editText_email.setError("Digite um email válido");
                        editText_email.requestFocus();
                        return;
                    }
                }

                String nome = editText_name.getText().toString();
                String email = editText_email.getText().toString().trim();

                uploadImageAndSaveUri(nome, email);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            imageUri = data.getData();
            Picasso.get().load(imageUri).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(circleImageView);

        }
    }

    private void uploadImageAndSaveUri(final String nome, final String email) {
        progressBar_pic.setVisibility(View.VISIBLE);

        if(imageUri==null){
            imageUri = getUriToDrawable(this, R.drawable.com_facebook_profile_picture_blank_square);
        }

        final StorageReference fileReference = FirebaseStorage.getInstance().getReference("Profile_Pictures")
                .child("profile_picture"
                        + FirebaseAuth.getInstance().getCurrentUser().getUid());

        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {

                        final UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri).setDisplayName(nome).build();
                        if (currentUser != null && !currentUser.isAnonymous()) {
                            currentUser.updateProfile(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    if(!email.isEmpty()) {
                                        currentUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar_pic.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(CompleteProfileForPhoneLoginActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CompleteProfileForPhoneLoginActivity.this,
                                                        "Falha ao adicinar e-mail.", Toast.LENGTH_SHORT).show();
                                                progressBar_pic.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(CompleteProfileForPhoneLoginActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                    }else{
                                        progressBar_pic.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(CompleteProfileForPhoneLoginActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CompleteProfileForPhoneLoginActivity.this, "Falha ao fazer Upload da foto de perfil.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * get uri to drawable or any other resource type if u wish
     * @param context - context
     * @param drawableId - drawable res id
     * @return - uri
     */
    private Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

}
