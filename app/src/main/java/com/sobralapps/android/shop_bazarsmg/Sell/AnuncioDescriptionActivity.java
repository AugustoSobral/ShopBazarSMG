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

public class AnuncioDescriptionActivity extends AppCompatActivity {

    private Button btn_continue;
    private EditText textDescription;
    private TextView help1;
    private TextView help2;
    private AnuncioOperationsViewModel anuncioOperationsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        getSupportActionBar().setTitle("Descreva seu anúncio");
        getSupportActionBar().setElevation(0);

        help1 = findViewById(R.id.text_view_help1);
        help2 = findViewById(R.id.text_view_help2);

        anuncioOperationsViewModel = ViewModelProviders.of(this).get(AnuncioOperationsViewModel.class);

        btn_continue = findViewById(R.id.btn_continue_description);
        textDescription = findViewById(R.id.edit_text_description);

        final LiveData<AnuncioEntity> getAnuncioLiveData = anuncioOperationsViewModel.getAnuncioById(1);

        getAnuncioLiveData.observe(this, new Observer<AnuncioEntity>() {
            @Override
            public void onChanged(AnuncioEntity anuncio) {

                if(anuncio.getAnuncioDescription()!=null && !anuncio.getAnuncioDescription().isEmpty())
                    textDescription.setText(anuncio.getAnuncioDescription());

                if(anuncio.getAnuncioType()==1) {
                    help1.setText("Inclua características que identifiquem o seu produto.");
                    help2.setText("Se você vende algo usado, detalhe em quais condições ele está.");
                    textDescription.setHint("Exemplo: Camisa sem uso, com etiqueta.");
                }
                if(anuncio.getAnuncioType()==2){
                    help1.setText("Descreva o seu veículo: Versão, Ano, Quantidade de Portas, Tipo de Combustível.");
                    help2.setText("Direção, Transmissão, Cilindradas, Quilometragem, Estado da parte interna.");
                    textDescription.setHint(null);
                }
                if(anuncio.getAnuncioType()==3){
                    help1.setText("Informe o estado geral do imóvel e como é o bairro/vizinhança.");
                    help2.setText("Informe os detalhes que podem acrescentar valor ao seu imóvel, como presença de garagem, áreas etc.");
                    textDescription.setHint("Ex: Excelente estado, área comercial");
                }
                if(anuncio.getAnuncioType()==4){
                    help1.setText("Informe o que inclui o seu serviço.");
                    help2.setText("Descreva-o com detalhes.");
                    textDescription.setHint("Ex: Aulas particulares de Inglês em domicílio.");
                }
                getAnuncioLiveData.removeObserver(this);
            }
        });



        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LiveData<AnuncioEntity> getAnuncioLiveData2 = anuncioOperationsViewModel.getAnuncioById(1);

                getAnuncioLiveData2.observe(AnuncioDescriptionActivity.this, new Observer<AnuncioEntity>() {
                    @Override
                    public void onChanged(AnuncioEntity anuncio) {
                        String description = textDescription.getText().toString();
                        anuncio.setAnuncioDescription(description);
                        anuncioOperationsViewModel.update(anuncio);
                        getAnuncioLiveData2.removeObserver(this);
                    }
                });
                startActivity(new Intent(AnuncioDescriptionActivity.this, AnuncioCategoryActivity.class));
            }
        });
    }
}
