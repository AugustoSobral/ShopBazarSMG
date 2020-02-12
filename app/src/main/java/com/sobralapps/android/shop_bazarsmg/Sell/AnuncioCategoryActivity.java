package com.sobralapps.android.shop_bazarsmg.Sell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sobralapps.android.shop_bazarsmg.Constants;
import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;
import com.sobralapps.android.shop_bazarsmg.Data.ViewModels.AnuncioOperationsViewModel;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.RecyclerViewCategoryAdapter;

public class AnuncioCategoryActivity extends AppCompatActivity {

    private TextView pageTitle;
    private AnuncioOperationsViewModel anuncioOperationsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_category);

        getSupportActionBar().setTitle(null);
        getSupportActionBar().setElevation(0);

        pageTitle = findViewById(R.id.text_view_page_title_category);

        anuncioOperationsViewModel = ViewModelProviders.of(this).get(AnuncioOperationsViewModel.class);

        RecyclerView recyclerViewCategory =  findViewById(R.id.recycler_view_category);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));
        //recyclerViewCategory.setHasFixedSize(true);

        final RecyclerViewCategoryAdapter mAdapter = new RecyclerViewCategoryAdapter();

        recyclerViewCategory.setAdapter(mAdapter);

        final LiveData<AnuncioEntity> getAnuncioLiveData = anuncioOperationsViewModel.getAnuncioById(1);

        getAnuncioLiveData.observe(this, new Observer<AnuncioEntity>() {
            @Override
            public void onChanged(AnuncioEntity anuncio) {

                if(anuncio.getAnuncioType()==1) {
                    mAdapter.setCategoryNameList(Constants.produtosCategory);
                    pageTitle.setText("Qual opção define o seu produto?");
                }

                if(anuncio.getAnuncioType()==2) {
                    mAdapter.setCategoryNameList(Constants.veiculosCategory);
                    pageTitle.setText("Qual opção define o seu veículo?");
                }

                if(anuncio.getAnuncioType()==3) {
                    mAdapter.setCategoryNameList(Constants.imoveisCategory);
                    pageTitle.setText("Qual opção define o seu imóvel?");
                }
                if(anuncio.getAnuncioType()==4) {
                    mAdapter.setCategoryNameList(Constants.servicosCategory);
                    pageTitle.setText("Qual opção define o seu serviço?");
                }

                getAnuncioLiveData.removeObserver(this);

            }
        });

        mAdapter.setOnItemClickListener(new RecyclerViewCategoryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(final String categoryName, int position) {
                final LiveData<AnuncioEntity> getAnuncioLiveData2 = anuncioOperationsViewModel.getAnuncioById(1);

                getAnuncioLiveData2.observe(AnuncioCategoryActivity.this, new Observer<AnuncioEntity>() {
                    @Override
                    public void onChanged(AnuncioEntity anuncio) {

                        anuncio.setAnuncioCategory(categoryName);
                        anuncioOperationsViewModel.update(anuncio);
                        getAnuncioLiveData2.removeObserver(this);
                    }
                });
                startActivity(new Intent(AnuncioCategoryActivity.this, NovoUsadoActivity.class));
            }
        });
    }
}
