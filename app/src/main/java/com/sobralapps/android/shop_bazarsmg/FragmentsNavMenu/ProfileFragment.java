package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private static int REQUEST_CODE_ADD_IMAGE = 2;

    private ProgressBar progressBar_pic;
    private Button btn_add_profile_pic;
    private CircleImageView circleImageView;
    private TextView textView_name;
    private TextView textView_email;
    private TextView textView_cel;
    private LinearLayout linearLayout_email;
    private LinearLayout linearLayout_cel;
    private LinearLayout linearLayout_nome;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private ProgressDialog progressDialog;

    public ProfileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar_pic = view.findViewById(R.id.progressBar_profile_pic_load);
        btn_add_profile_pic = view.findViewById(R.id.btn_add_profile_pic);
        circleImageView = view.findViewById(R.id.profile_circle_image_view);
        textView_email = view.findViewById(R.id.text_view_email_perfil);
        textView_name = view.findViewById(R.id.text_view_nome_e_sobrenome_perfil);
        textView_cel = view.findViewById(R.id.text_view_cel_perfil);
        linearLayout_cel = view.findViewById(R.id.linear_layout_numero_perfil);
        linearLayout_email = view.findViewById(R.id.linear_layout_email_perfil);
        linearLayout_nome = view.findViewById(R.id.linear_layout_nome_perfil);


        if(currentUser.getPhotoUrl()!=null)
            btn_add_profile_pic.setText("Alterar foto de Perfil");

        if (currentUser != null && !currentUser.isAnonymous()) {
            progressBar_pic.setVisibility(View.VISIBLE);
            Picasso.get().load(currentUser.getPhotoUrl()).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .error(R.drawable.com_facebook_profile_picture_blank_square).into(circleImageView, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar_pic.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
            textView_name.setText(currentUser.getDisplayName());

            if (currentUser.getEmail() == null || currentUser.getEmail().isEmpty()){
                linearLayout_email.setVisibility(View.GONE);
            }else
                textView_email.setText(currentUser.getEmail());

            if (currentUser.getPhoneNumber() == null || currentUser.getPhoneNumber().isEmpty()){
                linearLayout_cel.setVisibility(View.GONE);
            }else{

                //Removendo o código do país para mostrar na tela.
                StringBuilder phoneNoCountryCode = new StringBuilder(currentUser.getPhoneNumber());
                phoneNoCountryCode.deleteCharAt(0);
                phoneNoCountryCode.deleteCharAt(0);
                phoneNoCountryCode.deleteCharAt(0);

                textView_cel.setText(phoneNoCountryCode.toString());
            }
        }

        btn_add_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getActivity(), ImageCropperActivity.class);
                //CODE = 1 para imagem circular.
                i.putExtra("IMAGE_SHAPE_CODE", 1);
                startActivityForResult(i, REQUEST_CODE_ADD_IMAGE);
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Atualizando nome...");

        linearLayout_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());
                FrameLayout container = new FrameLayout(getActivity());
                FrameLayout.LayoutParams lp = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMarginStart(getResources().getDimensionPixelSize(R.dimen.size20dp));
                lp.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.size20dp));
                ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorBlack));
                ViewCompat.setBackgroundTintList(input, colorStateList);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input.setLayoutParams(lp);
                container.addView(input);
                final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Alterar nome de usuário")
                        .setMessage("Digite seu nome completo:")
                        .setView(container)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                String nome = input.getText().toString();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nome).build();
                                currentUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        textView_name.setText(currentUser.getDisplayName());
                                        Toast.makeText(getActivity(), "Nome atualizado com sucesso",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Falha ao atualizar nome",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD_IMAGE && resultCode == Activity.RESULT_OK && data!= null){

            Uri imageUri = data.getData();
            uploadImageAndSaveUri(imageUri);

        }
    }

    private void uploadImageAndSaveUri(final Uri imageUri) {
        progressBar_pic.setVisibility(View.VISIBLE);
        Picasso.get().load(R.drawable.com_facebook_profile_picture_blank_square).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square).into(circleImageView);

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
                                .setPhotoUri(uri).build();
                        if(currentUser!=null && !currentUser.isAnonymous()){
                            currentUser.updateProfile(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    updateUi(uri);

                                }
                            });
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Falha ao fazer Upload da foto de perfil.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUi(Uri profileImageUri){
        Picasso.get().load(profileImageUri).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square).into(circleImageView, new Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "Perfil Atualizado!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        textView_name.setText(currentUser.getDisplayName());
        textView_email.setText(currentUser.getEmail());
        progressBar_pic.setVisibility(View.INVISIBLE);

    }
}
