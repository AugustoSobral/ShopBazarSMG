package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AnuncioDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.AvisoPerguntaRespostaForFirebase;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.Notification;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.PerguntaRespostaForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatRespostaActivity extends AppCompatActivity {

    private EditText editText_resposta;
    private TextView textView_resposta;
    private TextView textView_name;
    private TextView textView_pergunta;
    private TextView textView_timestamp;
    private TextView msg_anuncio_excluido;
    private TextView msg_sistema;
    private ImageButton btn_send_resposta;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout_responder;
    private LinearLayout linearLayout_anuncio_desativado;
    private RelativeLayout relativeLayout_resposta;

    private AvisoPerguntaRespostaForFirebase avisoAtual;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference perguntas_respostasRef = db.collection("anuncios_perguntas_respostas");
    private CollectionReference users_avisos_ref = db.collection("users_avisos");
    private CollectionReference users_tokens_ref = db.collection("users_tokens");
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_resposta);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        editText_resposta = findViewById(R.id.edit_text_pergunta);
        textView_name = findViewById(R.id.text_view_chat_name);
        textView_pergunta = findViewById(R.id.text_view_chat_pergunta);
        textView_resposta = findViewById(R.id.text_view_chat_resposta);
        textView_timestamp = findViewById(R.id.text_view_chat_timestamp);
        msg_anuncio_excluido = findViewById(R.id.textView_anuncio_excluido);
        msg_sistema = findViewById(R.id.textView_mensagem_sistema);
        btn_send_resposta = findViewById(R.id.imageButton_send_pergunta);
        progressBar = findViewById(R.id.progressBar_categorias_fragment);
        linearLayout_responder = findViewById(R.id.linear_layout_fazer_perguntas);
        linearLayout_anuncio_desativado = findViewById(R.id.linear_layout_anuncio_desativado_chat);
        relativeLayout_resposta = findViewById(R.id.relative_layout_chat_resposta);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando resposta...");
        editText_resposta.setHint("Escreva sua resposta");

        avisoAtual = (AvisoPerguntaRespostaForFirebase) getIntent().getSerializableExtra("PERGUNTA");

        //Colocando o estado de lido no aviso aberto:
        users_avisos_ref.document(currentUser.getUid()).collection("avisos").document(avisoAtual.getAvisoId())
                .update("avisoRead", true);

        if(avisoAtual.getIdDocumentPergunta()!=null) {
            perguntas_respostasRef.document(avisoAtual.getAnuncioId()).collection("perguntas_respostas")
                    .document(avisoAtual.getIdDocumentPergunta()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        linearLayout_responder.setVisibility(View.VISIBLE);
                        PerguntaRespostaForFirebase perguntaRespostaForFirebase = documentSnapshot.toObject(PerguntaRespostaForFirebase.class);
                        updateUi(perguntaRespostaForFirebase);
                    } else {
                        linearLayout_responder.setVisibility(View.GONE);
                        linearLayout_anuncio_desativado.setVisibility(View.VISIBLE);
                        //Excluindo o aviso, já que o anúncio não existe mais.
                        users_avisos_ref.document(currentUser.getUid()).collection("avisos")
                                .document(avisoAtual.getAvisoId()).delete();
                    }
                }
            });
        }
        if(avisoAtual.getIdDocumentPergunta()==null){
            linearLayout_responder.setVisibility(View.GONE);
            linearLayout_anuncio_desativado.setVisibility(View.VISIBLE);
            msg_anuncio_excluido.setVisibility(View.GONE);
            msg_sistema.setVisibility(View.VISIBLE);
            textView_name.setText("Sistema");
            textView_pergunta.setText(avisoAtual.getAviso_pergunta_resposta());
        }

        btn_send_resposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(editText_resposta.getText().toString().isEmpty()){
                    editText_resposta.setError("Digite sua resposta.");
                    editText_resposta.requestFocus();
                    return;
                }
                final String resposta = editText_resposta.getText().toString();
                perguntas_respostasRef.document(avisoAtual.getAnuncioId()).collection("perguntas_respostas")
                        .document(avisoAtual.getIdDocumentPergunta()).update("resposta", resposta)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        relativeLayout_resposta.setVisibility(View.VISIBLE);
                        textView_resposta.setText(resposta);
                        linearLayout_responder.setVisibility(View.GONE);
                        createAviso(resposta);
                        createNotification(resposta);
                        hideSoftKeyboard(ChatRespostaActivity.this);
                    }
                });
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void createAviso(final String resposta){
        CollectionReference anunciosRef = db.collection("anuncios");
        anunciosRef.document(avisoAtual.getAnuncioId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);

                AvisoPerguntaRespostaForFirebase aviso = new AvisoPerguntaRespostaForFirebase();
                aviso.setAnuncioId(avisoAtual.getAnuncioId());
                aviso.setAviso_pergunta_resposta(resposta);
                aviso.setAviso_timestamp(Calendar.getInstance().getTime());
                //No caso agora é o usuário que respondeu!
                aviso.setId_user_que_perguntou(currentUser.getUid());
                aviso.setAviso_titulo("Resposta do vendedor em: " + anuncio.getTitulo());
                aviso.setAvisoRead(false);
                aviso.setIdDocumentPergunta(avisoAtual.getIdDocumentPergunta());
                //Este aviso pertence ao usuário que fez a pergunta:
                users_avisos_ref.document(avisoAtual.getId_user_que_perguntou()).collection("avisos").add(aviso);
            }
        });
    }

    public void createNotification(final String resposta){
        CollectionReference anunciosRef = db.collection("anuncios");
        anunciosRef.document(avisoAtual.getAnuncioId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                final Notification notification = new Notification();
                notification.setNotification_titulo("Nova resposta em: " + anuncio.getTitulo());
                notification.setTimestamp(Calendar.getInstance().getTime());
                notification.setNotification_pergunta_resposta(resposta);
                users_tokens_ref.document(avisoAtual.getId_user_que_perguntou()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String token =  (String) documentSnapshot.get("token");
                        db.collection("notifications").document(token).set(notification);
                    }
                });
            }
        });
    }

    private void updateUi(PerguntaRespostaForFirebase perguntaRespostaForFirebase){
        if(perguntaRespostaForFirebase.getResposta()!=null && !perguntaRespostaForFirebase.getResposta().isEmpty()){
            relativeLayout_resposta.setVisibility(View.VISIBLE);
            textView_resposta.setText(perguntaRespostaForFirebase.getResposta());
            linearLayout_responder.setVisibility(View.GONE);
        }
        textView_name.setText(perguntaRespostaForFirebase.getUserPerguntaName());
        textView_pergunta.setText(perguntaRespostaForFirebase.getPergunta());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterHours = new SimpleDateFormat("HH:mm");
        String date = formatter.format(perguntaRespostaForFirebase.getTimestamp());
        String hours = formatterHours.format((perguntaRespostaForFirebase.getTimestamp()));
        textView_timestamp.setText("" + date + " às " +hours);

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
