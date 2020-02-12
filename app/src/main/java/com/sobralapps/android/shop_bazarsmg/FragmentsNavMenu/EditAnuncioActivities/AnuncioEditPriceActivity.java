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
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.Constants;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.TextWatchers.MoneyTextWatcher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnuncioEditPriceActivity extends AppCompatActivity {

    private EditText editTextPrice;
    private Button btn_continue;
    private ProgressDialog progressDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        getSupportActionBar().setTitle(null);
        getSupportActionBar().setElevation(0);

        editTextPrice = findViewById(R.id.edit_text_price);
        editTextPrice.addTextChangedListener(new MoneyTextWatcher(editTextPrice));
        btn_continue = findViewById(R.id.btn_continue_description);
        btn_continue.setText("Salvar");

        Intent i = getIntent();
        final AnuncioForFirebase anuncio = (AnuncioForFirebase) i.getSerializableExtra("MEU_ANUNCIO_ATUAL");

        if (anuncio.getPreco() != null && !anuncio.getPreco().isEmpty())
            editTextPrice.setText(anuncio.getPreco());

        if (anuncio.getImovel_aluguel_venda() == Constants.IMOVEL_ALUGUEL_CODE)
            editTextPrice.setHint("Preço do aluguel (R$)");
        else if (anuncio.getImovel_aluguel_venda() == Constants.IMOVEL_TEMPORADA_CODE)
            editTextPrice.setHint("Preço da temporada (R$)");
        else
            editTextPrice.setHint("Preço de venda (R$)");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Modificando preço...");

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPrice.getText().toString().isEmpty()) {
                    editTextPrice.setError("Digite um preço.");
                    editTextPrice.requestFocus();
                    return;
                }

                String str = editTextPrice.getText().toString();
                String str2 = str.replaceAll("[^\\d]", "");
                long price = Long.parseLong(str2);
                if (price <= 0) {
                    editTextPrice.setError("Digite um preço maior que 0");
                    editTextPrice.requestFocus();
                    return;
                }

                progressDialog.show();
                final DocumentReference anuncioAtualReference = anunciosRef.document(anuncio.getDocumentId());
                anuncioAtualReference.update("preco", editTextPrice.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Se a atualização for bem sucedida retornamos o anuncio atualizado.
                                anuncioAtualReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        AnuncioForFirebase anuncioAtualizado = documentSnapshot.toObject(AnuncioForFirebase.class);
                                        anuncioAtualizado.setDocumentId(documentSnapshot.getId());
                                        Intent i = new Intent(AnuncioEditPriceActivity.this, AnuncioEditOptionsActivity.class);
                                        i.putExtra("MEU_ANUNCIO_ATUAL", anuncioAtualizado);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Toast.makeText(AnuncioEditPriceActivity.this, "Preço atualizado.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        startActivity(i);
                                        finish();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent i = new Intent(AnuncioEditPriceActivity.this, AnuncioEditOptionsActivity.class);
                        i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Toast.makeText(AnuncioEditPriceActivity.this, "Erro ao atualizar preço.", Toast.LENGTH_SHORT).show();
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
