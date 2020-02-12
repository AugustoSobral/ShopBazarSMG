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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NovoUsadoActivity extends AppCompatActivity {

    private List<String> produto_novo_usado = new ArrayList<>(Arrays.asList("Novo", "Usado"));
    private List<String> servico_preco_combinar = new ArrayList<>(Arrays.asList("Com preço definido", "Com preço a combinar"));
    private List<String> imovel_venda_aluguel = new ArrayList<>(Arrays.asList("Aluguel", "Venda", "Temporada"));
    private TextView pageTitle;
    private AnuncioOperationsViewModel anuncioOperationsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usado);

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
                    mAdapter.setCategoryNameList(produto_novo_usado);
                    pageTitle.setText("O seu produto é...");
                }

                if(anuncio.getAnuncioType()==2) {
                    mAdapter.setCategoryNameList(Constants.ano_veiculo);
                    pageTitle.setText("Qual o ano do veículo?");
                }

                if(anuncio.getAnuncioType()==3) {
                    mAdapter.setCategoryNameList(imovel_venda_aluguel);
                    pageTitle.setText("Anúncio de imóvel para:");
                }
                if(anuncio.getAnuncioType()==4) {
                    mAdapter.setCategoryNameList(servico_preco_combinar);
                    pageTitle.setText("Como você quer anunciar o seu serviço?");
                }
                getAnuncioLiveData.removeObserver(this);
            }
        });

        mAdapter.setOnItemClickListener(new RecyclerViewCategoryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(final String categoryName, final int position) {

                final LiveData<AnuncioEntity> getAnuncioLiveData2 = anuncioOperationsViewModel.getAnuncioById(1);
                getAnuncioLiveData2.observe(NovoUsadoActivity.this, new Observer<AnuncioEntity>() {
                    @Override
                    public void onChanged(AnuncioEntity anuncio) {

                        if(anuncio.getAnuncioType()==1){
                            anuncio.setAluguel_venda(Constants.SEM_ESPECIFICACAO_CODE);
                            anuncio.setAno_veiculo(null);
                            anuncio.setServico_preco_definido_combinar(Constants.SEM_ESPECIFICACAO_CODE);
                            if(position==0)
                                anuncio.setNovo_usado(Constants.NOVO_CODE);
                            if(position==1)
                                anuncio.setNovo_usado(Constants.USADO_CODE);
                        }

                        if(anuncio.getAnuncioType()==2){
                            anuncio.setAluguel_venda(Constants.SEM_ESPECIFICACAO_CODE);
                            anuncio.setNovo_usado(Constants.SEM_ESPECIFICACAO_CODE);
                            anuncio.setServico_preco_definido_combinar(Constants.SEM_ESPECIFICACAO_CODE);
                                anuncio.setAno_veiculo(categoryName);
                        }

                        if(anuncio.getAnuncioType()==3){
                            anuncio.setNovo_usado(Constants.SEM_ESPECIFICACAO_CODE);
                            anuncio.setAno_veiculo(null);
                            anuncio.setServico_preco_definido_combinar(Constants.SEM_ESPECIFICACAO_CODE);
                            if(position==0)
                                anuncio.setAluguel_venda(Constants.IMOVEL_ALUGUEL_CODE);
                            if(position==1)
                                anuncio.setAluguel_venda(Constants.IMOVEL_VENDA_CODE);
                            if(position==2)
                                anuncio.setAluguel_venda(Constants.IMOVEL_TEMPORADA_CODE);
                        }

                        if(anuncio.getAnuncioType()==4){
                            anuncio.setAluguel_venda(Constants.SEM_ESPECIFICACAO_CODE);
                            anuncio.setAno_veiculo(null);
                            anuncio.setNovo_usado(Constants.SEM_ESPECIFICACAO_CODE);
                            if(position==0)
                                anuncio.setServico_preco_definido_combinar(Constants.SERVICO_PRECO_DEFINIDO_CODE);
                            if(position==1) {
                                anuncio.setServico_preco_definido_combinar(Constants.SERVICO_PRECO_COMBINAR_CODE);
                                anuncio.setAnuncioPrice(null);
                            }
                        }

                        anuncioOperationsViewModel.update(anuncio);
                        getAnuncioLiveData2.removeObserver(this);
                    }
                });

                if(categoryName.equals("Com preço a combinar")){
                    startActivity(new Intent(NovoUsadoActivity.this, DadosParaContatoActivity.class));
                    return;
                }

               startActivity(new Intent(NovoUsadoActivity.this, PriceActivity.class));

            }
        });


    }
}
