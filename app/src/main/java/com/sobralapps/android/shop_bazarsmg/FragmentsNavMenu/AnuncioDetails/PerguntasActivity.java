package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AnuncioDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.AvisoPerguntaRespostaForFirebase;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.Notification;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.PerguntaRespostaForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.PerguntasRecyclerAdapter;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PerguntasActivity extends AppCompatActivity {

    private EditText editText_pergunta;
    private ImageButton btn_send_pergunta;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private TextView no_perguntas;
    private LinearLayout linearLayout_fazer_perguntas;

    private PerguntasRecyclerAdapter recyclerViewAdapter;

    private AnuncioForFirebase anuncioAtual;
    private List<PerguntaRespostaForFirebase> perguntasList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference perguntas_respostasRef = db.collection("anuncios_perguntas_respostas");
    private CollectionReference users_avisos_ref = db.collection("users_avisos");
    private CollectionReference users_tokens_ref = db.collection("users_tokens");
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perguntas);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        editText_pergunta = findViewById(R.id.edit_text_pergunta);
        btn_send_pergunta = findViewById(R.id.imageButton_send_pergunta);
        progressBar = findViewById(R.id.progressBar_categorias_fragment);
        no_perguntas = findViewById(R.id.text_view_no_anuncios);
        linearLayout_fazer_perguntas =findViewById(R.id.linear_layout_fazer_perguntas);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando pergunta...");

        RecyclerView recyclerView = findViewById(R.id.recycler_view_perguntas);
        recyclerView.setLayoutManager(new LinearLayoutManager(PerguntasActivity.this));

        recyclerViewAdapter = new PerguntasRecyclerAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        anuncioAtual = (AnuncioForFirebase) getIntent().getSerializableExtra("ANUNCIO_ATUAL");

        if(anuncioAtual.getUserId().equals(currentUser.getUid())){
            linearLayout_fazer_perguntas.setVisibility(View.GONE);
        }

        btn_send_pergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_pergunta.getText().toString().isEmpty()){
                    editText_pergunta.setError("Digite sua pergunta.");
                    editText_pergunta.requestFocus();
                    return;
                }
                if(anuncioAtual.getUserId().equals(currentUser.getUid())){
                    Toast.makeText(PerguntasActivity.this, "Você não pode fazer perguntas no seu próprio anúncio.", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String pergunta = editText_pergunta.getText().toString();

                PerguntaRespostaForFirebase perguntaRespostaForFirebase = new PerguntaRespostaForFirebase();
                perguntaRespostaForFirebase.setPergunta(pergunta);
                perguntaRespostaForFirebase.setTimestamp(Calendar.getInstance().getTime());
                perguntaRespostaForFirebase.setUserIdPergunta(currentUser.getUid());
                perguntaRespostaForFirebase.setAnuncioId(anuncioAtual.getDocumentId());
                perguntaRespostaForFirebase.setUserPerguntaName(currentUser.getDisplayName());

                progressDialog.show();

                perguntas_respostasRef.document(anuncioAtual.getDocumentId()).collection("perguntas_respostas")
                        .add(perguntaRespostaForFirebase).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        AvisoPerguntaRespostaForFirebase aviso = new AvisoPerguntaRespostaForFirebase();
                        aviso.setAnuncioId(anuncioAtual.getDocumentId());
                        aviso.setAviso_pergunta_resposta(editText_pergunta.getText().toString());
                        aviso.setAviso_timestamp(Calendar.getInstance().getTime());
                        aviso.setId_user_que_perguntou(currentUser.getUid());
                        aviso.setAviso_titulo("Nova pergunta em: " + anuncioAtual.getTitulo());
                        aviso.setAvisoRead(false);
                        //Salvamos o id do documento pergunta no aviso que o dono do anuncio receberá
                        aviso.setIdDocumentPergunta(documentReference.getId());
                        //Mandamos o aviso para o dono do anúncio
                        users_avisos_ref.document(anuncioAtual.getUserId()).collection("avisos").add(aviso);

                        createNotification(anuncioAtual.getTitulo(), pergunta);

                        editText_pergunta.getText().clear();
                        hideSoftKeyboard(PerguntasActivity.this);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);

        perguntas_respostasRef.document(anuncioAtual.getDocumentId()).collection("perguntas_respostas")
                .orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                progressBar.setVisibility(View.INVISIBLE);
                if (e != null) {
                    return;
                }
                perguntasList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    PerguntaRespostaForFirebase perguntaRespostaForFirebase = documentSnapshot.toObject(PerguntaRespostaForFirebase.class);
                    perguntasList.add(perguntaRespostaForFirebase);
                }
                recyclerViewAdapter.setPerguntas_RespostasList(perguntasList);
                if(perguntasList.isEmpty()) {
                    no_perguntas.setVisibility(View.VISIBLE);
                    if(anuncioAtual.getUserId().equals(currentUser.getUid()))
                        no_perguntas.setText("Não há perguntas no seu anúncio.");
                }
                else
                    no_perguntas.setVisibility(View.INVISIBLE);
            }
        });

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void createNotification(String titulo, String pergunta_resposta){
        final Notification notification = new Notification();
        notification.setNotification_titulo(titulo);
        notification.setTimestamp(Calendar.getInstance().getTime());
        notification.setNotification_pergunta_resposta(pergunta_resposta);

        users_tokens_ref.document(anuncioAtual.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String token =  (String) documentSnapshot.get("token");

                db.collection("notifications").document(token).set(notification);
            }
        });


    }
}
