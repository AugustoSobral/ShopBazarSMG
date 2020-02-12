package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.EditAnuncioActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnuncioEditDescriptionActivity extends AppCompatActivity {

    private Button btn_continue;
    private EditText textDescription;
    private TextView help1;
    private TextView help2;

    private ProgressDialog progressDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        getSupportActionBar().setTitle("Descreva seu anúncio");
        getSupportActionBar().setElevation(0);

        help1 = findViewById(R.id.text_view_help1);
        help2 = findViewById(R.id.text_view_help2);

        btn_continue = findViewById(R.id.btn_continue_description);
        textDescription = findViewById(R.id.edit_text_description);
        btn_continue.setText("Salvar");

        Intent i = getIntent();
        final AnuncioForFirebase anuncio = (AnuncioForFirebase) i.getSerializableExtra("MEU_ANUNCIO_ATUAL");

        if (anuncio.getDescricao() != null && !anuncio.getDescricao().isEmpty())
            textDescription.setText(anuncio.getDescricao());

        if (anuncio.getAnuncio_tipo() == 1) {
            help1.setText("Inclua características que identifiquem o seu produto.");
            help2.setText("Se você vende algo usado, detalhe em quais condições ele está.");
            textDescription.setHint("Exemplo: Camisa sem uso, com etiqueta.");
        }
        if (anuncio.getAnuncio_tipo() == 2) {
            help1.setText("Descreva o seu veículo: Versão, Ano, Quantidade de Portas, Tipo de Combustível.");
            help2.setText("Direção, Transmissão, Cilindradas, Quilometragem, Estado da parte interna.");
            textDescription.setHint(null);
        }
        if (anuncio.getAnuncio_tipo() == 3) {
            help1.setText("Informe o estado geral do imóvel e como é o bairro/vizinhança.");
            help2.setText("Informe os detalhes que podem acrescentar valor ao seu imóvel, como presença de garagem, áreas etc.");
            textDescription.setHint("Ex: Excelente estado, área comercial");
        }
        if (anuncio.getAnuncio_tipo() == 4) {
            help1.setText("Informe o que inclui o seu serviço.");
            help2.setText("Descreva-o com detalhes.");
            textDescription.setHint("Ex: Aulas particulares de Inglês em domicílio.");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Modificando descrição...");

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                final DocumentReference anuncioAtualReference = anunciosRef.document(anuncio.getDocumentId());
                anuncioAtualReference.update("descricao", textDescription.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Se a atualização for bem sucedida retornamos o anuncio atualizado.
                                anuncioAtualReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        AnuncioForFirebase anuncioAtualizado = documentSnapshot.toObject(AnuncioForFirebase.class);
                                        anuncioAtualizado.setDocumentId(documentSnapshot.getId());
                                        Intent i = new Intent(AnuncioEditDescriptionActivity.this, AnuncioEditOptionsActivity.class);
                                        i.putExtra("MEU_ANUNCIO_ATUAL", anuncioAtualizado);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Toast.makeText(AnuncioEditDescriptionActivity.this, "Descrição atualizada.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        startActivity(i);
                                        finish();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent i = new Intent(AnuncioEditDescriptionActivity.this, AnuncioEditOptionsActivity.class);
                        i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Toast.makeText(AnuncioEditDescriptionActivity.this, "Erro ao atualizar descrição.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
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
