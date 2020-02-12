package com.sobralapps.android.shop_bazarsmg.Sell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;
import com.sobralapps.android.shop_bazarsmg.Data.ViewModels.AnuncioOperationsViewModel;
import com.sobralapps.android.shop_bazarsmg.Helpers.ConvertAnuncioForFirebaseHelper;
import com.sobralapps.android.shop_bazarsmg.HomeActivity;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.TextWatchers.CEP_TEL_DATA;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DadosParaContatoActivity extends AppCompatActivity {

    private EditText call_number_phone;
    private EditText wpp_number;
    private EditText call_number_tel;
    private Button btn_continue;

    private AnuncioOperationsViewModel anuncioOperationsViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");
    private CollectionReference user_anuncios_ref = db.collection("users_anuncios");

    private StorageReference imagesAnunciosReference = FirebaseStorage.getInstance().getReference("Anuncios_Images");

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_para_contato);

        getSupportActionBar().setTitle("Dados para contato");
        getSupportActionBar().setElevation(0);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Criando Anúncio...");

        anuncioOperationsViewModel = ViewModelProviders.of(this).get(AnuncioOperationsViewModel.class);

        call_number_phone = findViewById(R.id.edit_text_call_phone);
        call_number_phone.addTextChangedListener(CEP_TEL_DATA.mask(call_number_phone, CEP_TEL_DATA.FORMAT_CELL));
        wpp_number = findViewById(R.id.edit_text_wpp_phone);
        wpp_number.addTextChangedListener(CEP_TEL_DATA.mask(wpp_number, CEP_TEL_DATA.FORMAT_CELL));
        call_number_tel = findViewById(R.id.edit_text_call_tel);
        call_number_tel.addTextChangedListener(CEP_TEL_DATA.mask(call_number_tel, CEP_TEL_DATA.FORMAT_TEL));

        btn_continue = findViewById(R.id.btn_continue_contato);

        final LiveData<AnuncioEntity> getAnuncioLiveData = anuncioOperationsViewModel.getAnuncioById(1);
        getAnuncioLiveData.observe(this, new Observer<AnuncioEntity>() {
            @Override
            public void onChanged(AnuncioEntity anuncioEntity) {
                if (anuncioEntity.getAnuncioContactCell() != null && !anuncioEntity.getAnuncioContactCell().isEmpty()) {
                    call_number_phone.setText(anuncioEntity.getAnuncioContactCell());
                }
                if (anuncioEntity.getAnuncioContactTel() != null && !anuncioEntity.getAnuncioContactTel().isEmpty()) {
                    call_number_tel.setText(anuncioEntity.getAnuncioContactTel());
                }
                if (anuncioEntity.getAnuncioContactWpp() != null && !anuncioEntity.getAnuncioContactWpp().isEmpty()) {
                    wpp_number.setText(anuncioEntity.getAnuncioContactWpp());
                }
                getAnuncioLiveData.removeObserver(this);
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2687105338472411/2063264206");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Intent intent = new Intent(DadosParaContatoActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(DadosParaContatoActivity.this, "Criando anúncio..", Toast.LENGTH_LONG).show();
            }

        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call_number_phone.getText().toString().isEmpty() && wpp_number.getText().toString().isEmpty()
                        && call_number_tel.getText().toString().isEmpty()) {
                    Toast.makeText(DadosParaContatoActivity.this, "Adicione um dos dados para contato",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (call_number_phone.getText().toString().replaceAll("[()]", "").length() < 11 && !call_number_phone.getText().toString().isEmpty()) {
                    call_number_phone.setError("Número inválido");
                    call_number_phone.requestFocus();
                    return;
                }
                if (call_number_tel.getText().toString().replaceAll("[()]", "").length() < 10 && !call_number_tel.getText().toString().isEmpty()) {
                    call_number_tel.setError("Número inválido");
                    call_number_tel.requestFocus();
                    return;
                }
                if (wpp_number.getText().toString().replaceAll("[()]", "").length() < 11 && !wpp_number.getText().toString().isEmpty()) {
                    wpp_number.setError("Número inválido");
                    wpp_number.requestFocus();
                    return;
                }

                if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                    mInterstitialAd.show();

                final LiveData<AnuncioEntity> getAnuncioLiveData2 = anuncioOperationsViewModel.getAnuncioById(1);
                getAnuncioLiveData2.observe(DadosParaContatoActivity.this, new Observer<AnuncioEntity>() {
                    @Override
                    public void onChanged(AnuncioEntity anuncioEntity) {
                        if (call_number_phone.getText().toString().isEmpty())
                            anuncioEntity.setAnuncioContactCell(null);
                        else
                            anuncioEntity.setAnuncioContactCell(call_number_phone.getText().toString());
                        if (call_number_tel.getText().toString().isEmpty())
                            anuncioEntity.setAnuncioContactTel(null);
                        else
                            anuncioEntity.setAnuncioContactTel(call_number_tel.getText().toString());
                        if (wpp_number.getText().toString().isEmpty())
                            anuncioEntity.setAnuncioContactWpp(null);
                        else
                            anuncioEntity.setAnuncioContactWpp(wpp_number.getText().toString());

                        AnuncioForFirebase anuncioForFirebase = ConvertAnuncioForFirebaseHelper.convertAnuncioForFirebase(anuncioEntity);

                        anuncioForFirebase.setTimestamp(Calendar.getInstance().getTime());
                        anuncioForFirebase.setUserId(currentUser.getUid());
                        anuncioForFirebase.setAnuncio_ativo(true);

                        uploadAnuncio(anuncioForFirebase);

                        anuncioOperationsViewModel.update(anuncioEntity);
                        getAnuncioLiveData2.removeObserver(this);

                    }
                });

            }
        });



    }

    private void uploadAnuncio(final AnuncioForFirebase anuncioForFirebase) {

        //progressDialog.show();
        //Primeiro adicionamos o objeto ao Banco de dados com as uri-url das imagens locais, depois que essa task estiver concluída
        //verificamos se as imagens existem nas posições, executamos uma task de upload e dentro da de upload uma task de pegar a Url.
        Task<DocumentReference> task = anunciosRef.add(anuncioForFirebase);
        task.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {

                if (anuncioForFirebase.getImage0() == null) {

                    //Adicionando o anuncio aos dados do usuário
                    addAnuncioAoUserData(documentReference);
                    if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                        finishAnuncioCreation();
                    }
                }

                if (anuncioForFirebase.getImage0() != null) {
                    Uri imageUri = Uri.parse(anuncioForFirebase.getImage0());
                    byte [] imageCompressed = compressImage(imageUri);
                    final StorageReference fileReference = imagesAnunciosReference.child(imageUri.getLastPathSegment());
                    fileReference.putBytes(imageCompressed)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrlString = uri.toString();
                                            documentReference.update("image0", downloadUrlString);

                                            if (anuncioForFirebase.getImage1() == null) {
                                                addAnuncioAoUserData(documentReference);
                                                if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                                                    finishAnuncioCreation();
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DadosParaContatoActivity.this, "Falha no Upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                if (anuncioForFirebase.getImage1() != null) {
                    Uri imageUri = Uri.parse(anuncioForFirebase.getImage1());
                    byte [] imageCompressed = compressImage(imageUri);
                    final StorageReference fileReference = imagesAnunciosReference.child(imageUri.getLastPathSegment());
                    fileReference.putBytes(imageCompressed)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrlString = uri.toString();
                                            documentReference.update("image1", downloadUrlString);

                                            if (anuncioForFirebase.getImage2() == null) {
                                                addAnuncioAoUserData(documentReference);
                                                if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                                                    finishAnuncioCreation();
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DadosParaContatoActivity.this, "Falha no Upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                if (anuncioForFirebase.getImage2() != null) {
                    Uri imageUri = Uri.parse(anuncioForFirebase.getImage2());
                    byte [] imageCompressed = compressImage(imageUri);
                    final StorageReference fileReference = imagesAnunciosReference.child(imageUri.getLastPathSegment());
                    fileReference.putBytes(imageCompressed)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrlString = uri.toString();
                                            documentReference.update("image2", downloadUrlString);

                                            if (anuncioForFirebase.getImage3() == null) {
                                                addAnuncioAoUserData(documentReference);
                                                if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                                                    finishAnuncioCreation();
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DadosParaContatoActivity.this, "Falha no Upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                if (anuncioForFirebase.getImage3() != null) {
                    Uri imageUri = Uri.parse(anuncioForFirebase.getImage3());
                    byte [] imageCompressed = compressImage(imageUri);
                    final StorageReference fileReference = imagesAnunciosReference.child(imageUri.getLastPathSegment());
                    fileReference.putBytes(imageCompressed)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrlString = uri.toString();
                                            documentReference.update("image3", downloadUrlString);

                                            if (anuncioForFirebase.getImage4() == null) {
                                                addAnuncioAoUserData(documentReference);
                                                if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                                                    finishAnuncioCreation();
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DadosParaContatoActivity.this, "Falha no Upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                if (anuncioForFirebase.getImage4() != null) {
                    Uri imageUri = Uri.parse(anuncioForFirebase.getImage4());
                    byte [] imageCompressed = compressImage(imageUri);
                    final StorageReference fileReference = imagesAnunciosReference.child(imageUri.getLastPathSegment());
                    fileReference.putBytes(imageCompressed)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrlString = uri.toString();
                                            documentReference.update("image4", downloadUrlString);

                                            if (anuncioForFirebase.getImage5() == null) {
                                                addAnuncioAoUserData(documentReference);
                                                if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                                                    finishAnuncioCreation();
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DadosParaContatoActivity.this, "Falha no Upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                if (anuncioForFirebase.getImage5() != null) {
                    Uri imageUri = Uri.parse(anuncioForFirebase.getImage5());
                    byte [] imageCompressed = compressImage(imageUri);
                    final StorageReference fileReference = imagesAnunciosReference.child(imageUri.getLastPathSegment());
                    fileReference.putBytes(imageCompressed)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrlString = uri.toString();
                                            documentReference.update("image5", downloadUrlString);

                                            if (anuncioForFirebase.getImage6() == null) {
                                                addAnuncioAoUserData(documentReference);
                                                if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                                                    finishAnuncioCreation();
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DadosParaContatoActivity.this, "Falha no Upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if (anuncioForFirebase.getImage6() != null) {
                    Uri imageUri = Uri.parse(anuncioForFirebase.getImage6());
                    byte [] imageCompressed = compressImage(imageUri);
                    final StorageReference fileReference = imagesAnunciosReference.child(imageUri.getLastPathSegment());
                    fileReference.putBytes(imageCompressed)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrlString = uri.toString();
                                            documentReference.update("image6", downloadUrlString);

                                            if (anuncioForFirebase.getImage7() == null) {
                                                addAnuncioAoUserData(documentReference);
                                                if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                                                    finishAnuncioCreation();
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DadosParaContatoActivity.this, "Falha no Upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if (anuncioForFirebase.getImage7() != null) {
                    Uri imageUri = Uri.parse(anuncioForFirebase.getImage7());
                    byte [] imageCompressed = compressImage(imageUri);
                    final StorageReference fileReference = imagesAnunciosReference.child(imageUri.getLastPathSegment());
                    fileReference.putBytes(imageCompressed)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrlString = uri.toString();
                                            documentReference.update("image7", downloadUrlString);
                                            addAnuncioAoUserData(documentReference);
                                            if(!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                                                finishAnuncioCreation();
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DadosParaContatoActivity.this, "Falha no Upload.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    //Para os "meus anuncios" criamos um documento cujo nome é o Uid do usuário (único), e os campos são os ids dos anuncios criados por ele.
    private void addAnuncioAoUserData(final DocumentReference anuncioReference){
            final DocumentReference currentUserAnuncios = user_anuncios_ref.document(currentUser.getUid());
            currentUserAnuncios.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Map<String, String> data = new HashMap<>();
                        data.put("meu_anuncio" + anuncioReference.getId(), anuncioReference.getId());
                        currentUserAnuncios.set(data, SetOptions.merge());

                    } else {
                        Map<String, String> data = new HashMap<>();
                        data.put("meu_anuncio" + anuncioReference.getId(), anuncioReference.getId());
                        currentUserAnuncios.set(data);
                    }

                }
            });
    }

    private void finishAnuncioCreation(){
        //progressDialog.dismiss();
        Intent intent = new Intent(DadosParaContatoActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private byte[] compressImage(Uri imageUri){
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        return baos.toByteArray();
    }
}
