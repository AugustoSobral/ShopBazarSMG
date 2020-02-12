package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.EditAnuncioActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.ImagesChosenViewModel;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.RecyclerViewImagesAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnuncioEditOptionsActivity extends AppCompatActivity {

    private LinearLayout linearLayout_title;
    private TextView textView_title;
    private LinearLayout linearLayout_price;
    private TextView textView_price;
    private LinearLayout linearLayout_description;
    private TextView textView_description;
    private TextView textView_ver_anuncio;
    private TextView textView_excluir;
    private TextView textView_ativo_title;
    private TextView textView_ativo_information;
    private Button btn_ativar_desativar_anuncio;
    private ProgressDialog progressDialog;
    private ImageButton btn_add_images;

    private RecyclerViewImagesAdapter recyclerViewAdapter;
    private ImagesChosenViewModel imageViewModel;
    private RecyclerView recyclerViewImages;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");
    private CollectionReference meusAnunciosRef = db.collection("users_anuncios");
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private AnuncioForFirebase anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_edit_options);

        linearLayout_title = findViewById(R.id.linear_layout_anuncio_edit_option_title);
        textView_title = findViewById(R.id.text_view_anuncio_edit_option_title);
        linearLayout_price = findViewById(R.id.linear_layout_anuncio_edit_option_price);
        textView_price = findViewById(R.id.text_view_anuncio_edit_option_price);
        linearLayout_description = findViewById(R.id.linear_layout_anuncio_edit_option_description);
        textView_description = findViewById(R.id.text_view_anuncio_edit_option_description);
        recyclerViewImages = findViewById(R.id.recycler_view_images);
        textView_excluir = findViewById(R.id.text_view_anuncio_edit_option_excluir);
        textView_ativo_title = findViewById(R.id.text_view_anuncio_edit_ativo_inativo);
        textView_ativo_information = findViewById(R.id.text_view_anuncio_edit_ativo_inativo_information);
        btn_ativar_desativar_anuncio = findViewById(R.id.btn_ativar_desativar_anuncio);
        textView_ver_anuncio = findViewById(R.id.text_view_anuncio_edit_option_ver_anuncio);
        btn_add_images = findViewById(R.id.imageButton_add_images_anuncio_edit);

        imageViewModel = ViewModelProviders.of(AnuncioEditOptionsActivity.this).get(ImagesChosenViewModel.class);

        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerViewImagesAdapter();
        recyclerViewImages.setAdapter(recyclerViewAdapter);


        Intent i = getIntent();
        anuncio = (AnuncioForFirebase) i.getSerializableExtra("MEU_ANUNCIO_ATUAL");

        textView_title.setText(anuncio.getTitulo());
        textView_price.setText(anuncio.getPreco());
        textView_description.setText(anuncio.getDescricao());

        if (anuncio.isAnuncio_ativo()) {
            textView_ativo_title.setText("O anúncio está ativo");
            textView_ativo_information.setText("Desativá-lo fará com que ele não apareça nos resultados de busca. Você poderá ativá-lo novamente.");
            btn_ativar_desativar_anuncio.setText("Desativar");
        }

        getAnuncioImages(anuncio);

        recyclerViewAdapter.setImagesList(imageViewModel.getImagesList());

        if(imageViewModel.getImagesList().isEmpty()){
            recyclerViewImages.setVisibility(View.GONE);
            btn_add_images.setVisibility(View.VISIBLE);
        }

        linearLayout_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnuncioEditOptionsActivity.this, AnuncioEditTitleActivity.class);
                i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                startActivity(i);
            }
        });

        linearLayout_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnuncioEditOptionsActivity.this, AnuncioEditDescriptionActivity.class);
                i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                startActivity(i);
            }
        });

        linearLayout_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnuncioEditOptionsActivity.this, AnuncioEditPriceActivity.class);
                i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                startActivity(i);
            }
        });

        textView_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnuncioEditOptionsActivity.this);
                builder.setTitle("Excluir anúncio.").setMessage("Tem certeza que deseja excluir esse anúncio?")
                        .setNegativeButton("Cancelar", dialogClickListener)
                        .setPositiveButton("Sim", dialogClickListener).show();
            }

            //Configura um ClickListener pra uma dialog box de confirmação para exclusão do anúncio.
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            deleteAnuncio();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
        });

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewImagesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent i = new Intent(AnuncioEditOptionsActivity.this, AnuncioEditImagesActivity.class);
                i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                startActivity(i);
            }
        });

        btn_add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnuncioEditOptionsActivity.this, AnuncioEditImagesActivity.class);
                i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                startActivity(i);
            }
        });

        progressDialog = new ProgressDialog(this);
        if (anuncio.isAnuncio_ativo())
            progressDialog.setMessage("Desativando anúncio...");
        if (!anuncio.isAnuncio_ativo())
            progressDialog.setMessage("Ativando anúncio...");

        btn_ativar_desativar_anuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                DocumentReference anuncioAtualReference = anunciosRef.document(anuncio.getDocumentId());
                if (anuncio.isAnuncio_ativo())
                    anuncioAtualReference.update("anuncio_ativo", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            anuncio.setAnuncio_ativo(false);
                            progressDialog.dismiss();
                            recreate();
                        }
                    });

                if (!anuncio.isAnuncio_ativo())
                    anuncioAtualReference.update("anuncio_ativo", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            anuncio.setAnuncio_ativo(true);
                            progressDialog.dismiss();
                            recreate();
                        }
                    });
            }
        });

        textView_ver_anuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnuncioEditOptionsActivity.this, AnuncioDetailsActivity.class);
                i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                startActivity(i);
            }
        });
    }

    private void getAnuncioImages(AnuncioForFirebase anuncio) {
        String image1, image2, image3, image4, image5, image6, image7, image8;
        image1 = anuncio.getImage0();
        image2 = anuncio.getImage1();
        image3 = anuncio.getImage2();
        image4 = anuncio.getImage3();
        image5 = anuncio.getImage4();
        image6 = anuncio.getImage5();
        image7 = anuncio.getImage6();
        image8 = anuncio.getImage7();

        Uri imageUri1 = null, imageUri2 = null, imageUri3 = null, imageUri4 = null, imageUri5 = null, imageUri6 = null, imageUri7 = null, imageUri8 = null;
        if (image1 != null)
            imageUri1 = Uri.parse(image1);
        if (image2 != null)
            imageUri2 = Uri.parse(image2);
        if (image3 != null)
            imageUri3 = Uri.parse(image3);
        if (image4 != null)
            imageUri4 = Uri.parse(image4);
        if (image5 != null)
            imageUri5 = Uri.parse(image5);
        if (image6 != null)
            imageUri6 = Uri.parse(image6);
        if (image7 != null)
            imageUri7 = Uri.parse(image7);
        if (image8 != null)
            imageUri8 = Uri.parse(image8);

        List<Uri> imagesListUri = new ArrayList<>();
        if (imageUri1 != null)
            imagesListUri.add(0, imageUri1);
        if (imageUri2 != null)
            imagesListUri.add(1, imageUri2);
        if (imageUri3 != null)
            imagesListUri.add(2, imageUri3);
        if (imageUri4 != null)
            imagesListUri.add(3, imageUri4);
        if (imageUri5 != null)
            imagesListUri.add(4, imageUri5);
        if (imageUri6 != null)
            imagesListUri.add(5, imageUri6);
        if (imageUri7 != null)
            imagesListUri.add(6, imageUri7);
        if (imageUri8 != null)
            imagesListUri.add(7, imageUri8);

        imageViewModel.setImagesList(imagesListUri);
    }

    public void deleteAnuncio(){
        progressDialog.setMessage("Excluindo anúncio...");
        progressDialog.show();
        DocumentReference anuncioAtualReference = anunciosRef.document(anuncio.getDocumentId());
        anuncioAtualReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Removendo o anuncio dos "meus_anúncios": apagando o campo no documento
                Map<String, Object> updates = new HashMap<>();
                updates.put("meu_anuncio" + anuncio.getDocumentId(), FieldValue.delete());
                meusAnunciosRef.document(currentUser.getUid()).update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Excluindo todos os documentos dentro da sub coleção onde ficam as perguntas.
                                CollectionReference anuncioPerguntasSubCollectionRef = db.collection("anuncios_perguntas_respostas")
                                        .document(anuncio.getDocumentId()).collection("perguntas_respostas");
                                anuncioPerguntasSubCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            progressDialog.dismiss();
                                            return;
                                        }
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            documentSnapshot.getReference().delete();
                                        }
                                        //Depois de deletada toda a subcoleção, deletemos o documento raiz.
                                        db.collection("anuncios_perguntas_respostas")
                                                .document(anuncio.getDocumentId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Deletando as imagens
                                                deleteStorageFiles();
                                                progressDialog.dismiss();
                                                Toast.makeText(AnuncioEditOptionsActivity.this, "Anúncio excluído.", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AnuncioEditOptionsActivity.this, "Falha ao excluir anúncio.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void deleteStorageFiles(){
        if(anuncio.getImage0()!=null){
            mStorage.getReferenceFromUrl(anuncio.getImage0()).delete();
        }
        if(anuncio.getImage1()!=null){
            mStorage.getReferenceFromUrl(anuncio.getImage1()).delete();
        }
        if(anuncio.getImage2()!=null){
            mStorage.getReferenceFromUrl(anuncio.getImage2()).delete();
        }
        if(anuncio.getImage3()!=null){
            mStorage.getReferenceFromUrl(anuncio.getImage3()).delete();
        }
        if(anuncio.getImage4()!=null){
            mStorage.getReferenceFromUrl(anuncio.getImage4()).delete();
        }
        if(anuncio.getImage5()!=null){
            mStorage.getReferenceFromUrl(anuncio.getImage5()).delete();
        }
        if(anuncio.getImage6()!=null){
            mStorage.getReferenceFromUrl(anuncio.getImage6()).delete();
        }
        if(anuncio.getImage7()!=null){
            mStorage.getReferenceFromUrl(anuncio.getImage7()).delete();
        }
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
