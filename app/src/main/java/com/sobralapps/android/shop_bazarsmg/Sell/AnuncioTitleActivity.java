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
import android.widget.TextView;

import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;
import com.sobralapps.android.shop_bazarsmg.Data.ViewModels.AnuncioOperationsViewModel;
import com.sobralapps.android.shop_bazarsmg.R;

public class AnuncioTitleActivity extends AppCompatActivity {

    private Button btn_continue;
    private EditText textTitle;
    private TextView help1;
    private TextView help2;
    private TextView pageTitle;

    private AnuncioOperationsViewModel anuncioOperationsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_title);

        getSupportActionBar().setTitle("Título do seu anúncio");
        getSupportActionBar().setElevation(0);

        anuncioOperationsViewModel = ViewModelProviders.of(this).get(AnuncioOperationsViewModel.class);

        btn_continue = findViewById(R.id.btn_continue_description);
        textTitle = findViewById(R.id.edit_text_description);
        help1 = findViewById(R.id.text_view_help1);
        help2 = findViewById(R.id.text_view_help2);
        pageTitle = findViewById(R.id.text_view_page_title_category);

        final LiveData<AnuncioEntity> getAnuncioLiveData = anuncioOperationsViewModel.getAnuncioById(1);

        getAnuncioLiveData.observe(this, new Observer<AnuncioEntity>() {
            @Override
            public void onChanged(AnuncioEntity anuncio) {

                if(anuncio.getAnuncioTitle()!=null && !anuncio.getAnuncioTitle().isEmpty())
                    textTitle.setText(anuncio.getAnuncioTitle());

                if(anuncio.getAnuncioType()==1) {
                    help1.setText("Informe o nome do produto, marca e modelo!");
                    help2.setText("Seja claro: separe as palavras com espaços e evite o uso de símbolos.");
                    textTitle.setHint("Exemplo: Camisa regata Mizuno");
                }
                if(anuncio.getAnuncioType()==2){
                    pageTitle.setText("Qual veículo quer vender?");
                    help1.setText(null);
                    help2.setText(null);
                    textTitle.setHint("Exemplo: Volkswagen Gol 2018");
                }
                if(anuncio.getAnuncioType()==3){
                    help1.setText("Informe qual é o tipo do imóvel.");
                    help2.setText("Seja claro: separe as palavras com espaços e evite o uso de símbolos.");
                    textTitle.setHint("Ex: Casa de 3 quartos e 2 banheiros");
                }
                if(anuncio.getAnuncioType()==4){
                    help1.setText("Informe o nome e o tipo do serviço que você oferece.");
                    help2.setText("Seja claro: separe as palavras com espaços e evite o uso de símbolos.");
                    textTitle.setHint("Ex: Manicure e Pedicure");
                }
                getAnuncioLiveData.removeObserver(this);

            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textTitle.getText().toString().trim().isEmpty()){
                    textTitle.setError("Digite o título do anúncio");
                    textTitle.requestFocus();
                    return;
                }

                final LiveData<AnuncioEntity> getAnuncioLiveData2 = anuncioOperationsViewModel.getAnuncioById(1);

                getAnuncioLiveData2.observe(AnuncioTitleActivity.this, new Observer<AnuncioEntity>() {
                    @Override
                    public void onChanged(AnuncioEntity anuncio) {
                        String title = textTitle.getText().toString();

                        anuncio.setAnuncioTitle(title);
                        anuncioOperationsViewModel.update(anuncio);
                        getAnuncioLiveData2.removeObserver(this);
                    }
                });
                startActivity(new Intent(AnuncioTitleActivity.this, AnuncioDescriptionActivity.class));

            }
        });
    }
}
