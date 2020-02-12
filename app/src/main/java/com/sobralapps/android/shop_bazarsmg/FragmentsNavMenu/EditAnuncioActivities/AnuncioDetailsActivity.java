package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.EditAnuncioActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sobralapps.android.shop_bazarsmg.Constants;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AnuncioDetails.PerguntasActivity;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.ImagesChosenViewModel;
import com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels.SliderImagesAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnuncioDetailsActivity extends AppCompatActivity {

    private TextView titulo_text_view;
    private TextView preco_text_view;
    private TextView descricao_text_view;
    private TextView caracteristica1_titulo, caracteristica1_content, caracteristica2_titulo, caracteristica2_content;
    private TextView contato1_titulo, contato1_content, contato2_titulo, contato2_content, contato3_titulo,
            contato3_content;
    private TextView[] mDots;
    private TextView text_qnt_perguntas;
    private LinearLayout dotLayout;
    private LinearLayout linear_layout_qnt_fotos;
    private TextView text_view_n_fotos, text_view_fotos;
    private ViewPager viewPager;
    private SliderImagesAdapter sliderImagesAdapter;
    //Podemos usar o mesmo viewModel da ImagesChosenActivity pois ele só guarda uma lista de Uris.
    private ImagesChosenViewModel imageViewModel;
    private LikeButton btn_favorite;
    private LinearLayout linearLayout_mais_anuncios;
    private LinearLayout linear_layout_perguntas;


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference favoriteRef = db.collection("users_favoritos");
    private CollectionReference perguntas_respostasRef = db.collection("anuncios_perguntas_respostas");

    private AnuncioForFirebase anuncio_atual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_anuncio_details);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        titulo_text_view = findViewById(R.id.text_view_details_titulo);
        preco_text_view = findViewById(R.id.text_view_details_preco);
        descricao_text_view = findViewById(R.id.text_view_details_descricao);
        caracteristica1_titulo = findViewById(R.id.text_view_details_caracteristica1);
        caracteristica1_content = findViewById(R.id.caracteristica1_content);
        caracteristica2_titulo = findViewById(R.id.text_view_details_caracteristica2);
        caracteristica2_content = findViewById(R.id.caracteristica2_content);
        contato1_titulo = findViewById(R.id.text_view_details_contato1);
        contato1_content = findViewById(R.id.contato1_content);
        contato2_titulo = findViewById(R.id.text_view_details_contato2);
        contato2_content = findViewById(R.id.contato2_content);
        contato3_titulo = findViewById(R.id.text_view_details_contato3);
        contato3_content = findViewById(R.id.contato3_content);
        viewPager = findViewById(R.id.view_pager_anuncio_details);
        dotLayout = findViewById(R.id.dots_layout_anuncio_details);
        text_view_n_fotos = findViewById(R.id.text_view_qnt_fotos_anuncio_details);
        text_view_fotos = findViewById(R.id.text_view_fotos_anuncio_details);
        linear_layout_qnt_fotos = findViewById(R.id.linear_layout_qnt_fotos);
        btn_favorite = findViewById(R.id.favorite_button);
        linearLayout_mais_anuncios = findViewById(R.id.linear_layout_anuncios_in_details);
        linear_layout_perguntas = findViewById(R.id.linear_layout_perguntas);
        text_qnt_perguntas = findViewById(R.id.text_view_qnt_perguntas_details);
        linearLayout_mais_anuncios.setVisibility(View.GONE);

        imageViewModel = ViewModelProviders.of(AnuncioDetailsActivity.this).get(ImagesChosenViewModel.class);

        anuncio_atual = (AnuncioForFirebase) getIntent().getSerializableExtra("MEU_ANUNCIO_ATUAL");

        titulo_text_view.setText(anuncio_atual.getTitulo());
        if (anuncio_atual.getPreco() == null) {
            preco_text_view.setText("Preço à combinar");
            preco_text_view.setTextSize(24);
        } else
            preco_text_view.setText(anuncio_atual.getPreco());
        descricao_text_view.setText(anuncio_atual.getDescricao());

        caracteristica2_titulo.setText("Categoria:");
        caracteristica2_content.setText(anuncio_atual.getCategoria());

        if (anuncio_atual.getAnuncio_tipo() == 1) {
            caracteristica1_titulo.setText("Estado:");
            if (anuncio_atual.getProduto_novo_usado() == Constants.NOVO_CODE)
                caracteristica1_content.setText("Novo");
            if (anuncio_atual.getProduto_novo_usado() == Constants.USADO_CODE)
                caracteristica1_content.setText("Usado");
        }

        if (anuncio_atual.getAnuncio_tipo() == 2) {
            caracteristica1_titulo.setText("Ano:");
            caracteristica1_content.setText(anuncio_atual.getAno_veiculo());
        }

        if (anuncio_atual.getAnuncio_tipo() == 3) {
            caracteristica1_titulo.setText("Imóvel para:");
            if (anuncio_atual.getImovel_aluguel_venda() == Constants.IMOVEL_ALUGUEL_CODE)
                caracteristica1_content.setText("Aluguel");
            if (anuncio_atual.getImovel_aluguel_venda() == Constants.IMOVEL_VENDA_CODE)
                caracteristica1_content.setText("Venda");
            if (anuncio_atual.getImovel_aluguel_venda() == Constants.IMOVEL_TEMPORADA_CODE)
                caracteristica1_content.setText("Temporada");
        }

        if (anuncio_atual.getAnuncio_tipo() == 4) {
            caracteristica1_titulo.setText("Categoria:");
            caracteristica1_content.setText(anuncio_atual.getCategoria());
            caracteristica2_titulo.setVisibility(View.INVISIBLE);
            caracteristica2_content.setVisibility(View.INVISIBLE);
        }

        if (anuncio_atual.getContato_wpp() != null) {
            contato1_titulo.setText("Whatsapp:");
            contato1_content.setText(anuncio_atual.getContato_wpp());
        } else {
            contato1_titulo.setVisibility(View.GONE);
            contato1_content.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 15, 0, 0);
            contato3_titulo.setLayoutParams(layoutParams);
        }
        if (anuncio_atual.getContato_phone_call() != null) {
            contato2_titulo.setText("Celular para chamadas:");
            contato2_content.setText(anuncio_atual.getContato_phone_call());
        } else {
            contato2_titulo.setVisibility(View.GONE);
            contato2_content.setVisibility(View.GONE);
        }
        if (anuncio_atual.getContato_tel_call() != null) {
            contato3_titulo.setText("Tel Fixo para chamadas:");
            contato3_content.setText(anuncio_atual.getContato_tel_call());
        } else {
            contato3_titulo.setVisibility(View.GONE);
            contato3_content.setVisibility(View.GONE);
        }

        List<Uri> imagesListUri = new ArrayList<>();

        if (anuncio_atual.getImage0() != null)
            imagesListUri.add(0, Uri.parse(anuncio_atual.getImage0()));
        if (anuncio_atual.getImage1() != null)
            imagesListUri.add(1, Uri.parse(anuncio_atual.getImage1()));
        if (anuncio_atual.getImage2() != null)
            imagesListUri.add(2, Uri.parse(anuncio_atual.getImage2()));
        if (anuncio_atual.getImage3() != null)
            imagesListUri.add(3, Uri.parse(anuncio_atual.getImage3()));
        if (anuncio_atual.getImage4() != null)
            imagesListUri.add(4, Uri.parse(anuncio_atual.getImage4()));
        if (anuncio_atual.getImage5() != null)
            imagesListUri.add(5, Uri.parse(anuncio_atual.getImage5()));
        if (anuncio_atual.getImage6() != null)
            imagesListUri.add(6, Uri.parse(anuncio_atual.getImage6()));
        if (anuncio_atual.getImage7() != null)
            imagesListUri.add(7, Uri.parse(anuncio_atual.getImage7()));

        imageViewModel.setImagesList(imagesListUri);
        sliderImagesAdapter = new SliderImagesAdapter(imageViewModel.getImagesList(), AnuncioDetailsActivity.this);
        viewPager.setAdapter(sliderImagesAdapter);
        if (imageViewModel.getImagesList().isEmpty()) {
            List<Uri> imagesListUriForEmpty = new ArrayList<>();
            Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + this.getResources().getResourcePackageName(R.drawable.anuncio_sem_foto)
                    + '/' + this.getResources().getResourceTypeName(R.drawable.anuncio_sem_foto)
                    + '/' + this.getResources().getResourceEntryName(R.drawable.anuncio_sem_foto));
            imagesListUriForEmpty.add(imageUri);
            sliderImagesAdapter.setImagesList(imagesListUriForEmpty);
        } else
            sliderImagesAdapter.setImagesList(imageViewModel.getImagesList());

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewPagerListener);

        if (imageViewModel.getImagesList().size() > 1) {
            text_view_n_fotos.setText("" + imageViewModel.getImagesList().size());
            text_view_fotos.setText("Fotos");
        } else {
            text_view_fotos.setVisibility(View.GONE);
            text_view_n_fotos.setVisibility(View.GONE);
            linear_layout_qnt_fotos.setVisibility(View.GONE);
        }

        final DocumentReference currentUserFavoritoReference = favoriteRef.document(currentUser.getUid());

        currentUserFavoritoReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    if (documentSnapshot.getData().containsKey("favorito" + anuncio_atual.getDocumentId()))
                        btn_favorite.setLiked(true);

                }

            }
        });

        if(currentUser.isAnonymous())
            btn_favorite.setVisibility(View.GONE);

        perguntas_respostasRef.document(anuncio_atual.getDocumentId()).collection("perguntas_respostas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        int qnt_perguntas = queryDocumentSnapshots.size();
                        text_qnt_perguntas.setText(""+qnt_perguntas);

                    }
                });

        btn_favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                if(!currentUser.isAnonymous() && currentUser !=null) {
                    currentUserFavoritoReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {

                                Map<String, String> data = new HashMap<>();
                                data.put("favorito" + anuncio_atual.getDocumentId(), anuncio_atual.getDocumentId());
                                currentUserFavoritoReference.set(data, SetOptions.merge());
                                Toast.makeText(AnuncioDetailsActivity.this, "Adicionado aos favoritos.", Toast.LENGTH_SHORT).show();

                            } else {
                                Map<String, String> data = new HashMap<>();
                                data.put("favorito" + anuncio_atual.getDocumentId(), anuncio_atual.getDocumentId());
                                currentUserFavoritoReference.set(data);
                                Toast.makeText(AnuncioDetailsActivity.this, "Adicionado aos favoritos.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                if(!currentUser.isAnonymous() && currentUser !=null) {
                    currentUserFavoritoReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {

                                //Apagando o campo no docuemnto do firestore.
                                Map<String, Object> data = new HashMap<>();
                                data.put("favorito" + anuncio_atual.getDocumentId(), FieldValue.delete());
                                currentUserFavoritoReference.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AnuncioDetailsActivity.this, "Removido dos favoritos.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });
                }
            }
        });

        linear_layout_perguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnuncioDetailsActivity.this, PerguntasActivity.class);
                i.putExtra("ANUNCIO_ATUAL", anuncio_atual);
                startActivity(i);
            }
        });

    }

    //Adiciona os pontos embaixo do slide
    public void addDotsIndicator(int position) {

        if (imageViewModel.getImagesList().size() != 0 && imageViewModel.getImagesList().size() != 1) {
            mDots = new TextView[imageViewModel.getImagesList().size()];
            dotLayout.removeAllViews();

            for (int i = 0; i < mDots.length; i++) {

                mDots[i] = new TextView(AnuncioDetailsActivity.this);
                mDots[i].setText(Html.fromHtml("&#8226;"));
                mDots[i].setTextSize(40);
                mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                dotLayout.addView(mDots[i]);
            }

            //Na posição atual a cor do ponto correspondente é setada para ser diferente dos demais.
            if (mDots.length > 0)
                mDots[position].setTextColor(getResources().getColor(R.color.colorDotGrey));
        }
    }

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
