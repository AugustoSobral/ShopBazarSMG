package com.sobralapps.android.shop_bazarsmg.Sell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sobralapps.android.shop_bazarsmg.Constants;
import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;
import com.sobralapps.android.shop_bazarsmg.Data.ViewModels.AnuncioOperationsViewModel;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.TextWatchers.MoneyTextWatcher;

public class PriceActivity extends AppCompatActivity {

    private AnuncioOperationsViewModel anuncioOperationsViewModel;
    private EditText editTextPrice;
    private Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        getSupportActionBar().setTitle(null);
        getSupportActionBar().setElevation(0);

        editTextPrice = findViewById(R.id.edit_text_price);
        editTextPrice.addTextChangedListener(new MoneyTextWatcher(editTextPrice));
        btn_continue = findViewById(R.id.btn_continue_description);

        anuncioOperationsViewModel = ViewModelProviders.of(this).get(AnuncioOperationsViewModel.class);

        final LiveData<AnuncioEntity> getAnuncioLiveData = anuncioOperationsViewModel.getAnuncioById(1);

        getAnuncioLiveData.observe(this, new Observer<AnuncioEntity>() {
            @Override
            public void onChanged(AnuncioEntity anuncio) {

                if (anuncio.getAnuncioPrice() != null && !anuncio.getAnuncioPrice().isEmpty())
                    editTextPrice.setText(anuncio.getAnuncioPrice());

                if(anuncio.getAluguel_venda()== Constants.IMOVEL_ALUGUEL_CODE)
                    editTextPrice.setHint("Preço do aluguel (R$)");
                else
                    if(anuncio.getAluguel_venda()== Constants.IMOVEL_TEMPORADA_CODE)
                        editTextPrice.setHint("Preço da temporada (R$)");
                    else
                        editTextPrice.setHint("Preço de venda (R$)");

                getAnuncioLiveData.removeObserver(this);

            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextPrice.getText().toString().isEmpty()){
                    editTextPrice.setError("Digite um preço.");
                    editTextPrice.requestFocus();
                    return;
                }

                String str = editTextPrice.getText().toString();
                String str2 = str.replaceAll("[^\\d]", "");
                long price = Long.parseLong(str2);
                if(price<=0){
                    editTextPrice.setError("Digite um preço maior que 0");
                    editTextPrice.requestFocus();
                    return;
                }

                final LiveData<AnuncioEntity> getAnuncioLiveData2 = anuncioOperationsViewModel.getAnuncioById(1);
                getAnuncioLiveData2.observe(PriceActivity.this, new Observer<AnuncioEntity>() {
                    @Override
                    public void onChanged(AnuncioEntity anuncio) {
                        anuncio.setAnuncioPrice(editTextPrice.getText().toString());
                        anuncioOperationsViewModel.update(anuncio);
                        getAnuncioLiveData2.removeObserver(this);
                    }
                });
                startActivity(new Intent(PriceActivity.this, DadosParaContatoActivity.class));

            }
        });
    }
}
