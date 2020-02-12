package com.sobralapps.android.shop_bazarsmg.Sell;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;
import com.sobralapps.android.shop_bazarsmg.Data.ViewModels.AnuncioOperationsViewModel;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.ImagesChosenViewModel;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.RecyclerViewImagesAdapter;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.SliderImagesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagesChosenActivity extends AppCompatActivity {

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

    private AnuncioOperationsViewModel anuncioOperationsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_chosen);

        getSupportActionBar().setTitle("Adicione fotos");

        btn_add_images = findViewById(R.id.btn_add_images);
        btn_continue = findViewById(R.id.btn_continue_images);
        recyclerView = findViewById(R.id.recycler_view_images);
        viewPager = findViewById(R.id.view_pager_images);
        dotLayout = findViewById(R.id.dots_layout);

        imageViewModel = ViewModelProviders.of(ImagesChosenActivity.this).get(ImagesChosenViewModel.class);
        anuncioOperationsViewModel= ViewModelProviders.of(ImagesChosenActivity.this).get(AnuncioOperationsViewModel.class);

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
            //Se o anuncio já estiver sendo criado e o usuário voltar na tela de imagens, mostramos as imagens já adicionadas:
            final LiveData<AnuncioEntity> getAnuncioLiveData = anuncioOperationsViewModel.getAnuncioById(1);
            getAnuncioLiveData.observe(this, new Observer<AnuncioEntity>() {
                @Override
                public void onChanged(AnuncioEntity anuncio) {

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
                    //Usamos o removeObserver pois se não esses observers continuam ativos executando ações mesmo após a atividade ser finalizada.
                    getAnuncioLiveData.removeObserver(this);
                }
            });

        }

        btn_add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewModel.getImagesList().size()>=6){
                    Toast.makeText(ImagesChosenActivity.this, "Máximo de 6 fotos por anúncio.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i =  new Intent(ImagesChosenActivity.this, ImageCropperActivity.class);
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

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LiveData<AnuncioEntity> getAnuncioLiveData2 = anuncioOperationsViewModel.getAnuncioById(1);
                getAnuncioLiveData2.observe(ImagesChosenActivity.this, new Observer<AnuncioEntity>() {
                    @Override
                    public void onChanged(AnuncioEntity anuncio) {
                        String image1 = null, image2 = null, image3 = null, image4 = null, image5 = null , image6 = null, image7 = null, image8 = null;
                        if(imageViewModel.getImagesList().size() > 0 && imageViewModel.getImagesList().get(0)!= null)
                            image1 = imageViewModel.getImagesList().get(0).toString();
                        if(imageViewModel.getImagesList().size() > 1 && imageViewModel.getImagesList().get(1)!=null)
                            image2 = imageViewModel.getImagesList().get(1).toString();
                        if(imageViewModel.getImagesList().size() > 2 && imageViewModel.getImagesList().get(2)!=null)
                            image3 = imageViewModel.getImagesList().get(2).toString();
                        if(imageViewModel.getImagesList().size() > 3 && imageViewModel.getImagesList().get(3)!=null)
                            image4 = imageViewModel.getImagesList().get(3).toString();
                        if(imageViewModel.getImagesList().size() > 4 && imageViewModel.getImagesList().get(4)!=null)
                            image5 = imageViewModel.getImagesList().get(4).toString();
                        if(imageViewModel.getImagesList().size() > 5 && imageViewModel.getImagesList().get(5)!=null)
                            image6 = imageViewModel.getImagesList().get(5).toString();
                        if(imageViewModel.getImagesList().size() > 6 && imageViewModel.getImagesList().get(6)!=null)
                            image7 = imageViewModel.getImagesList().get(6).toString();
                        if(imageViewModel.getImagesList().size() > 7 && imageViewModel.getImagesList().get(7)!=null)
                            image8 = imageViewModel.getImagesList().get(7).toString();

                        AnuncioEntity anuncioCurrent = new AnuncioEntity();
                        anuncioCurrent.setId(1);
                        anuncioCurrent.setAnuncioType(anuncio.getAnuncioType());
                        anuncioCurrent.setImage0(image1);
                        anuncioCurrent.setImage1(image2);
                        anuncioCurrent.setImage2(image3);
                        anuncioCurrent.setImage3(image4);
                        anuncioCurrent.setImage4(image5);
                        anuncioCurrent.setImage5(image6);
                        anuncioCurrent.setImage6(image7);
                        anuncioCurrent.setImage7(image8);
                        anuncioOperationsViewModel.update(anuncioCurrent);
                        getAnuncioLiveData2.removeObserver(this);
                    }
                });

                Intent i = new Intent(ImagesChosenActivity.this, AnuncioTitleActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD_IMAGE && resultCode == RESULT_OK && data!= null){

            Uri imageUri = data.getData();
            imageViewModel.addImageToList(imageUri);
            sliderImagesAdapter.setImagesList(imageViewModel.getImagesList());
            Toast.makeText(this, "Imagem Adicionada!", Toast.LENGTH_SHORT).show();
            recyclerViewAdapter.setImagesList(imageViewModel.getImagesList());
            viewPager.setCurrentItem(0);
            if(imageViewModel.getImagesList().size()!=1)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (R.id.delete_image):
                deleteImage();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteImage() {
        if(!imageViewModel.getImagesList().isEmpty()) {
            int position = viewPager.getCurrentItem();
            imageViewModel.removeImageToList(position);
            recyclerViewAdapter.setImagesList(imageViewModel.getImagesList());
            sliderImagesAdapter.setImagesList(imageViewModel.getImagesList());
            viewPager.setAdapter(sliderImagesAdapter);

            if(imageViewModel.getImagesList().size() != 0) {

                if(imageViewModel.getImagesList().size()!=1 && position!=0){
                    addDotsIndicator(position - 1);
                    viewPager.setCurrentItem(position -1);
                }

                if(imageViewModel.getImagesList().size()!=1 && position==0) {
                    addDotsIndicator(0);
                    viewPager.setCurrentItem(0);
                }
                if(imageViewModel.getImagesList().size()==1) {
                    dotLayout.removeAllViews();

                }
            }
        }
    }
}
