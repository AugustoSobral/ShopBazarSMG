package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.EditAnuncioActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.ImagesChosenViewModel;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.RecyclerViewImagesAdapter;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.SliderImagesAdapter;
import com.sobralapps.android.shop_bazarsmg.Sell.ImageCropperActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AnuncioEditImagesActivity extends AppCompatActivity {

    private static int REQUEST_CODE_ADD_IMAGE = 1;

    private Button btn_add_images;
    private Button btn_continue;
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private TextView[] mDots;
    private SliderImagesAdapter sliderImagesAdapter;
    private RecyclerViewImagesAdapter recyclerViewAdapter;
    private LinearLayout dotLayout;

    private ImagesChosenViewModel imageViewModel;
    private Uri image1 = null, image2 = null, image3 = null, image4 = null, image5 = null, image6 = null, image7 = null, image8 = null;

    private ProgressDialog progressDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");
    private StorageReference imagesAnunciosReference = FirebaseStorage.getInstance().getReference("Anuncios_Images");
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_chosen);

        getSupportActionBar().setTitle("Altere as fotos do anúncio");

        btn_add_images = findViewById(R.id.btn_add_images);
        btn_continue = findViewById(R.id.btn_continue_images);
        recyclerView = findViewById(R.id.recycler_view_images);
        viewPager = findViewById(R.id.view_pager_images);
        dotLayout = findViewById(R.id.dots_layout);

        btn_continue.setText("Salvar");

        Intent i = getIntent();
        final AnuncioForFirebase anuncio = (AnuncioForFirebase) i.getSerializableExtra("MEU_ANUNCIO_ATUAL");

        imageViewModel = ViewModelProviders.of(AnuncioEditImagesActivity.this).get(ImagesChosenViewModel.class);

        sliderImagesAdapter = new SliderImagesAdapter(imageViewModel.getImagesList(), this);
        viewPager.setAdapter(sliderImagesAdapter);
        sliderImagesAdapter.setImagesList(imageViewModel.getImagesList());

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(viewPagerListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerViewImagesAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setImagesList(imageViewModel.getImagesList());

        //Adicionamos a verificação para não mudar a lista em mudanças de config
        if (savedInstanceState == null) {

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
            sliderImagesAdapter.setImagesList(imageViewModel.getImagesList());
            recyclerViewAdapter.setImagesList(imageViewModel.getImagesList());
            viewPager.setCurrentItem(0);
            if (imageViewModel.getImagesList().size() != 1)
                addDotsIndicator(0);
        }

        btn_add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewModel.getImagesList().size() >= 6) {
                    Toast.makeText(AnuncioEditImagesActivity.this, "Máximo de 6 fotos por anúncio.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(AnuncioEditImagesActivity.this, ImageCropperActivity.class);
                startActivityForResult(i, REQUEST_CODE_ADD_IMAGE);
            }
        });

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewImagesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                viewPager.setCurrentItem(position);
                addDotsIndicator(position);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Atualizando imagens...");

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                if (imageViewModel.getImagesList().size() > 0 && imageViewModel.getImagesList().get(0) != null)
                    image1 = imageViewModel.getImagesList().get(0);
                if (imageViewModel.getImagesList().size() > 1 && imageViewModel.getImagesList().get(1) != null)
                    image2 = imageViewModel.getImagesList().get(1);
                if (imageViewModel.getImagesList().size() > 2 && imageViewModel.getImagesList().get(2) != null)
                    image3 = imageViewModel.getImagesList().get(2);
                if (imageViewModel.getImagesList().size() > 3 && imageViewModel.getImagesList().get(3) != null)
                    image4 = imageViewModel.getImagesList().get(3);
                if (imageViewModel.getImagesList().size() > 4 && imageViewModel.getImagesList().get(4) != null)
                    image5 = imageViewModel.getImagesList().get(4);
                if (imageViewModel.getImagesList().size() > 5 && imageViewModel.getImagesList().get(5) != null)
                    image6 = imageViewModel.getImagesList().get(5);
                if (imageViewModel.getImagesList().size() > 6 && imageViewModel.getImagesList().get(6) != null)
                    image7 = imageViewModel.getImagesList().get(6);
                if (imageViewModel.getImagesList().size() > 7 && imageViewModel.getImagesList().get(7) != null)
                    image8 = imageViewModel.getImagesList().get(7);

                //(Lançar imagens para o Storage e salvar as urls no DB)

                final DocumentReference anuncioAtualReference = anunciosRef.document(anuncio.getDocumentId());
                if (image1 == null) {
                    finishEditAndRetrieveAnuncio(anuncioAtualReference);
                }

                if (image1 != null) {
                    final StorageReference fileReference = imagesAnunciosReference.child(image1.getLastPathSegment());
                    fileReference.putFile(image1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrlString = uri.toString();
                                    anuncioAtualReference.update("image0", downloadUrlString);

                                    if(image2 == null){
                                        anuncioAtualReference.update("image1", null, "image2", null,
                                                "image3", null, "image4", null, "image5", null, "image6", null, "image7", null)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finishEditAndRetrieveAnuncio(anuncioAtualReference);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }

                if (image2 != null) {
                    final StorageReference fileReference = imagesAnunciosReference.child(image2.getLastPathSegment());
                    fileReference.putFile(image2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrlString = uri.toString();
                                    anuncioAtualReference.update("image1", downloadUrlString);
                                    if(image3 == null){
                                        anuncioAtualReference.update("image2", null, "image3", null,
                                                "image4", null, "image5", null, "image6", null, "image7", null)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        finishEditAndRetrieveAnuncio(anuncioAtualReference);
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    });
                }

                if (image3 != null) {
                    final StorageReference fileReference = imagesAnunciosReference.child(image3.getLastPathSegment());
                    fileReference.putFile(image3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrlString = uri.toString();
                                    anuncioAtualReference.update("image2", downloadUrlString);
                                    if(image4 == null){
                                        anuncioAtualReference.update("image3", null, "image4", null,
                                                "image5", null, "image6", null, "image7", null)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        finishEditAndRetrieveAnuncio(anuncioAtualReference);
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    });
                }

                if (image4 != null) {
                    final StorageReference fileReference = imagesAnunciosReference.child(image4.getLastPathSegment());
                    fileReference.putFile(image4).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrlString = uri.toString();
                                    anuncioAtualReference.update("image3", downloadUrlString);
                                    if(image5 == null){
                                        anuncioAtualReference.update("image4", null, "image5", null,
                                                "image6", null, "image7", null)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        finishEditAndRetrieveAnuncio(anuncioAtualReference);
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    });
                }

                if (image5 != null) {
                    final StorageReference fileReference = imagesAnunciosReference.child(image5.getLastPathSegment());
                    fileReference.putFile(image5).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrlString = uri.toString();
                                    anuncioAtualReference.update("image4", downloadUrlString);
                                    if(image6 == null){
                                        anuncioAtualReference.update("image5", null, "image6", null,
                                                "image7", null)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        finishEditAndRetrieveAnuncio(anuncioAtualReference);
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    });
                }

                if (image6 != null) {
                    final StorageReference fileReference = imagesAnunciosReference.child(image6.getLastPathSegment());
                    fileReference.putFile(image6).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrlString = uri.toString();
                                    anuncioAtualReference.update("image5", downloadUrlString);
                                    if(image7 == null){
                                        anuncioAtualReference.update("image6", null, "image7", null)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        finishEditAndRetrieveAnuncio(anuncioAtualReference);
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    });
                }

                if (image7 != null) {
                    final StorageReference fileReference = imagesAnunciosReference.child(image7.getLastPathSegment());
                    fileReference.putFile(image7).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrlString = uri.toString();
                                    anuncioAtualReference.update("image6", downloadUrlString);
                                    if(image8 == null){
                                        anuncioAtualReference.update("image7", null)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        finishEditAndRetrieveAnuncio(anuncioAtualReference);
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    });
                }

                if (image8 != null) {
                    final StorageReference fileReference = imagesAnunciosReference.child(image8.getLastPathSegment());
                    fileReference.putFile(image8).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrlString = uri.toString();
                                    anuncioAtualReference.update("image7", downloadUrlString);
                                    finishEditAndRetrieveAnuncio(anuncioAtualReference);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            imageViewModel.addImageToList(imageUri);
            sliderImagesAdapter.setImagesList(imageViewModel.getImagesList());
            Toast.makeText(this, "Imagem Adicionada!", Toast.LENGTH_SHORT).show();
            recyclerViewAdapter.setImagesList(imageViewModel.getImagesList());
            viewPager.setCurrentItem(0);
            if (imageViewModel.getImagesList().size() != 1)
                addDotsIndicator(0);
        }
    }

    //Adiciona os pontos embaixo do slide
    public void addDotsIndicator(int position) {

        if (imageViewModel.getImagesList().size() != 0) {
            mDots = new TextView[imageViewModel.getImagesList().size()];
            dotLayout.removeAllViews();

            for (int i = 0; i < mDots.length; i++) {

                mDots[i] = new TextView(this);
                mDots[i].setText(Html.fromHtml("&#8226;"));
                mDots[i].setTextSize(30);
                mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                dotLayout.addView(mDots[i]);
            }
            //Na posição atual a cor do ponto correspondente é setada para ser diferente dos demais.
            if (mDots.length > 0)
                mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //Esse trecho de código faz com que o back button do action bar faça exatamente o mesmo que o back button do hardware
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        /*if (item.getItemId() == R.id.delete_image) {
            deleteImage();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /*private void deleteImage() {
        if (!imageViewModel.getImagesList().isEmpty()) {
            int position = viewPager.getCurrentItem();
            removeImageFromStorage(imageViewModel.getImagesList().get(position));
            imageViewModel.removeImageToList(position);
            recyclerViewAdapter.setImagesList(imageViewModel.getImagesList());
            sliderImagesAdapter.setImagesList(imageViewModel.getImagesList());
            viewPager.setAdapter(sliderImagesAdapter);

            if (imageViewModel.getImagesList().size() != 0) {

                if (imageViewModel.getImagesList().size() != 1 && position != 0) {
                    addDotsIndicator(position - 1);
                    viewPager.setCurrentItem(position - 1);
                }
                if (imageViewModel.getImagesList().size() != 1 && position == 0) {
                    addDotsIndicator(0);
                    viewPager.setCurrentItem(0);
                }
                if (imageViewModel.getImagesList().size() == 1) {
                    dotLayout.removeAllViews();
                }
            }
        }
    }

    private void removeImageFromStorage(Uri imageUri){
        final String imageUrl = imageUri.toString();
        //Verificando se a url existe no storage (porque se a imagem foi adicionada nesta activity ela só está no imageList do viewModel e não no storage).
        mStorage.getReferenceFromUrl(imageUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mStorage.getReferenceFromUrl(imageUrl).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }*/

    private void finishEditAndRetrieveAnuncio(DocumentReference anuncioAtualReference){
        //Se a atualização for bem sucedida retornamos o anuncio atualizado.
        anuncioAtualReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AnuncioForFirebase anuncioAtualizado = documentSnapshot.toObject(AnuncioForFirebase.class);
                anuncioAtualizado.setDocumentId(documentSnapshot.getId());
                Intent i = new Intent(AnuncioEditImagesActivity.this, AnuncioEditOptionsActivity.class);
                i.putExtra("MEU_ANUNCIO_ATUAL", anuncioAtualizado);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(AnuncioEditImagesActivity.this, "Imagens atualizadas.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                startActivity(i);
                finish();
            }
        });
    }
}
