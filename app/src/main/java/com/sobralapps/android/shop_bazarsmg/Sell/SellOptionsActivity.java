package com.sobralapps.android.shop_bazarsmg.Sell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sobralapps.android.shop_bazarsmg.Constants;
import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;
import com.sobralapps.android.shop_bazarsmg.Data.ViewModels.AnuncioOperationsViewModel;
import com.sobralapps.android.shop_bazarsmg.R;

public class SellOptionsActivity extends AppCompatActivity {

    private ImageButton btn_sell_produtos;
    private ImageButton btn_sell_veiculos;
    private ImageButton btn_sell_imoveis;
    private ImageButton btn_sell_servicos;

    private TextView produtos;
    private TextView veiculos;
    private TextView servicos;
    private TextView imoveis;

    private AnuncioOperationsViewModel anuncioOperationsViewModel;
    private AnuncioEntity anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_options);

        getSupportActionBar().setTitle(null);
        //Removendo a sombra embaixo do actionBar.
        getSupportActionBar().setElevation(0);

        btn_sell_produtos = findViewById(R.id.btn_sell_produtos);
        btn_sell_servicos = findViewById(R.id.btn_sell_servicos);
        btn_sell_imoveis = findViewById(R.id.btn_sell_casas);
        btn_sell_veiculos = findViewById(R.id.btn_sell_veiculos);
        produtos = findViewById(R.id.produtos);
        servicos = findViewById(R.id.servicos);
        imoveis = findViewById(R.id.imoveis);
        veiculos = findViewById(R.id.veiculos);

        anuncioOperationsViewModel = ViewModelProviders.of(this).get(AnuncioOperationsViewModel.class);

        anuncio = new AnuncioEntity();
        anuncio.setId(1);

        btn_sell_produtos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellOptionsActivity.this, ImagesChosenActivity.class);
                anuncio.setAnuncioType(Constants.PRODUTO_TYPE_CODE);
                anuncioOperationsViewModel.insert(anuncio);
                startActivity(i);
            }
        });
        btn_sell_veiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellOptionsActivity.this, ImagesChosenActivity.class);
                anuncio.setAnuncioType(Constants.VEICULO_TYPE_CODE);
                anuncioOperationsViewModel.insert(anuncio);
                startActivity(i);
            }
        });
        btn_sell_imoveis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellOptionsActivity.this, ImagesChosenActivity.class);
                anuncio.setAnuncioType(Constants.IMOVEL_TYPE_CODE);
                anuncioOperationsViewModel.insert(anuncio);
                startActivity(i);
            }
        });
        btn_sell_servicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellOptionsActivity.this, ImagesChosenActivity.class);
                anuncio.setAnuncioType(Constants.SERVICO_TYPE_CODE);
                anuncioOperationsViewModel.insert(anuncio);
                startActivity(i);
            }
        });



        produtos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellOptionsActivity.this, ImagesChosenActivity.class);
                anuncio.setAnuncioType(Constants.PRODUTO_TYPE_CODE);
                anuncioOperationsViewModel.insert(anuncio);
                startActivity(i);
            }
        });
        veiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellOptionsActivity.this, ImagesChosenActivity.class);
                anuncio.setAnuncioType(Constants.VEICULO_TYPE_CODE);
                anuncioOperationsViewModel.insert(anuncio);
                startActivity(i);
            }
        });
        servicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellOptionsActivity.this, ImagesChosenActivity.class);
                anuncio.setAnuncioType(Constants.SERVICO_TYPE_CODE);
                anuncioOperationsViewModel.insert(anuncio);
                startActivity(i);
            }
        });
        imoveis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SellOptionsActivity.this, ImagesChosenActivity.class);
                anuncio.setAnuncioType(Constants.IMOVEL_TYPE_CODE);
                anuncioOperationsViewModel.insert(anuncio);
                startActivity(i);
            }
        });
    }
}
