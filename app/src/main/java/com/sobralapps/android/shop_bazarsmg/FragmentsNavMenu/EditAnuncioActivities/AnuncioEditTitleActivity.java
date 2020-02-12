package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.EditAnuncioActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnuncioEditTitleActivity extends AppCompatActivity {

    private Button btn_continue;
    private EditText textTitle;
    private TextView help1;
    private TextView help2;
    private TextView pageTitle;
    private ProgressDialog progressDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_title);

        getSupportActionBar().setTitle("Título do seu anúncio");
        getSupportActionBar().setElevation(0);


        btn_continue = findViewById(R.id.btn_continue_description);
        textTitle = findViewById(R.id.edit_text_description);
        help1 = findViewById(R.id.text_view_help1);
        help2 = findViewById(R.id.text_view_help2);
        pageTitle = findViewById(R.id.text_view_page_title_category);

        btn_continue.setText("Salvar");

        Intent i = getIntent();
        final AnuncioForFirebase anuncio = (AnuncioForFirebase) i.getSerializableExtra("MEU_ANUNCIO_ATUAL");

        if (anuncio.getTitulo() != null && !anuncio.getTitulo().isEmpty())
            textTitle.setText(anuncio.getTitulo());

        if (anuncio.getAnuncio_tipo() == 1) {
            help1.setText("Informe o nome do produto, marca e modelo!");
            help2.setText("Seja claro: separe as palavras com espaços e evite o uso de símbolos.");
            textTitle.setHint("Exemplo: Camisa regata Mizuno");
        }
        if (anuncio.getAnuncio_tipo() == 2) {
            pageTitle.setText("Qual veículo quer vender?");
            help1.setText(null);
            help2.setText(null);
            textTitle.setHint("Exemplo: Volkswagen Gol 2018");
        }
        if (anuncio.getAnuncio_tipo() == 3) {
            help1.setText("Informe qual é o tipo do imóvel.");
            help2.setText("Seja claro: separe as palavras com espaços e evite o uso de símbolos.");
            textTitle.setHint("Ex: Casa de 3 quartos e 2 banheiros");
        }
        if (anuncio.getAnuncio_tipo() == 4) {
            help1.setText("Informe o nome e o tipo do serviço que você oferece.");
            help2.setText("Seja claro: separe as palavras com espaços e evite o uso de símbolos.");
            textTitle.setHint("Ex: Manicure e Pedicure");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Modificando título...");


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textTitle.getText().toString().trim().isEmpty()) {
                    textTitle.setError("Digite o título do anúncio");
                    textTitle.requestFocus();
                    return;
                }

                progressDialog.show();

                final DocumentReference anuncioAtualReference = anunciosRef.document(anuncio.getDocumentId());
                anuncioAtualReference.update("titulo", textTitle.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Se a atualização for bem sucedida retornamos o anuncio atualizado.
                                anuncioAtualReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        AnuncioForFirebase anuncioAtualizado = documentSnapshot.toObject(AnuncioForFirebase.class);
                                        anuncioAtualizado.setDocumentId(documentSnapshot.getId());
                                        Intent i = new Intent(AnuncioEditTitleActivity.this, AnuncioEditOptionsActivity.class);
                                        i.putExtra("MEU_ANUNCIO_ATUAL", anuncioAtualizado);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Toast.makeText(AnuncioEditTitleActivity.this, "Título atualizado.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        startActivity(i);
                                        finish();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent i = new Intent(AnuncioEditTitleActivity.this, AnuncioEditOptionsActivity.class);
                        i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Toast.makeText(AnuncioEditTitleActivity.this, "Erro ao atualizar título.", Toast.LENGTH_SHORT).show();
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
