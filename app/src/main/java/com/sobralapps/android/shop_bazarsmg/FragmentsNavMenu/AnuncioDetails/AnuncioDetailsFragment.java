package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AnuncioDetails;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.sobralapps.android.shop_bazarsmg.Constants;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.AnuncioForHorizontalContainerRecyclerViewAdapter;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.AnuncioHomeRecyclerAdapter;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnuncioDetailsFragment extends Fragment {

    private List<String> anunciosIdList = new ArrayList<>();
    private List<AnuncioForFirebase> anunciosList = new ArrayList<>();
    private List<AnuncioForFirebase> meusAnunciosList = new ArrayList<>();

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
    private LinearLayout linearLayout_wpp_celular;
    private LinearLayout linearLayout_tel;
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
    private CollectionReference anunciosRef = db.collection("anuncios");
    private CollectionReference meusAnunciosRef = db.collection("users_anuncios");
    private CollectionReference perguntas_respostasRef = db.collection("anuncios_perguntas_respostas");

    private AnuncioForHorizontalContainerRecyclerViewAdapter recyclerViewAdapter;

    private AnuncioForFirebase anuncio_atual;

    public AnuncioDetailsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anuncio_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        titulo_text_view = view.findViewById(R.id.text_view_details_titulo);
        preco_text_view = view.findViewById(R.id.text_view_details_preco);
        descricao_text_view = view.findViewById(R.id.text_view_details_descricao);
        caracteristica1_titulo = view.findViewById(R.id.text_view_details_caracteristica1);
        caracteristica1_content = view.findViewById(R.id.caracteristica1_content);
        caracteristica2_titulo = view.findViewById(R.id.text_view_details_caracteristica2);
        caracteristica2_content = view.findViewById(R.id.caracteristica2_content);
        contato1_titulo = view.findViewById(R.id.text_view_details_contato1);
        contato1_content = view.findViewById(R.id.contato1_content);
        contato2_titulo = view.findViewById(R.id.text_view_details_contato2);
        contato2_content = view.findViewById(R.id.contato2_content);
        contato3_titulo = view.findViewById(R.id.text_view_details_contato3);
        contato3_content = view.findViewById(R.id.contato3_content);
        viewPager = view.findViewById(R.id.view_pager_anuncio_details);
        dotLayout = view.findViewById(R.id.dots_layout_anuncio_details);
        text_view_n_fotos = view.findViewById(R.id.text_view_qnt_fotos_anuncio_details);
        text_view_fotos = view.findViewById(R.id.text_view_fotos_anuncio_details);
        linear_layout_qnt_fotos = view.findViewById(R.id.linear_layout_qnt_fotos);
        btn_favorite = view.findViewById(R.id.favorite_button);
        linearLayout_mais_anuncios = view.findViewById(R.id.linear_layout_anuncios_in_details);
        linear_layout_perguntas = view.findViewById(R.id.linear_layout_perguntas);
        linearLayout_wpp_celular = view.findViewById(R.id.linear_layout_anuncio_details_wpp_celular);
        linearLayout_tel = view.findViewById(R.id.linear_layout_anuncio_details_tel);
        text_qnt_perguntas = view.findViewById(R.id.text_view_qnt_perguntas_details);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_anuncios_in_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new AnuncioForHorizontalContainerRecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        imageViewModel = ViewModelProviders.of(AnuncioDetailsFragment.this).get(ImagesChosenViewModel.class);

        anuncio_atual = (AnuncioForFirebase) getArguments().getSerializable("ANUNCIO_ATUAL");

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

        if(anuncio_atual.getContato_wpp() == null && anuncio_atual.getContato_phone_call() ==null)
            linearLayout_wpp_celular.setVisibility(View.GONE);

        if (anuncio_atual.getContato_wpp() != null) {
            contato1_titulo.setText("Whatsapp:");
            contato1_content.setText(anuncio_atual.getContato_wpp());
        }
        if (anuncio_atual.getContato_wpp() == null && anuncio_atual.getContato_phone_call()!=null){
            contato1_titulo.setVisibility(View.GONE);
            contato1_content.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 15, 0, 0);
            contato3_titulo.setLayoutParams(layoutParams);
        }
        if (anuncio_atual.getContato_phone_call() != null) {
            contato3_titulo.setText("Celular para chamadas:");
            contato3_content.setText(anuncio_atual.getContato_phone_call());
        } else {
            contato3_titulo.setVisibility(View.GONE);
            contato3_content.setVisibility(View.GONE);
        }
        if (anuncio_atual.getContato_tel_call() != null) {
            contato2_titulo.setText("Tel Fixo para chamadas:");
            contato2_content.setText(anuncio_atual.getContato_tel_call());
        } else {
            linearLayout_tel.setVisibility(View.GONE);
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
        sliderImagesAdapter = new SliderImagesAdapter(imageViewModel.getImagesList(), getContext());
        viewPager.setAdapter(sliderImagesAdapter);
        //Se o anuncio não tiver imagens exibimos uma imagem padrão
        if (imageViewModel.getImagesList().isEmpty()) {
            List<Uri> imagesListUriForEmpty = new ArrayList<>();
            Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getContext().getResources().getResourcePackageName(R.drawable.anuncio_sem_foto)
                    + '/' + getContext().getResources().getResourceTypeName(R.drawable.anuncio_sem_foto)
                    + '/' + getContext().getResources().getResourceEntryName(R.drawable.anuncio_sem_foto));
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

        if (currentUser.isAnonymous())
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

                if (!currentUser.isAnonymous() && currentUser != null) {
                    currentUserFavoritoReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {
                                Map<String, String> data = new HashMap<>();
                                data.put("favorito" + anuncio_atual.getDocumentId(), anuncio_atual.getDocumentId());
                                currentUserFavoritoReference.set(data, SetOptions.merge());
                                //Adicionado aos favoritos

                            } else {
                                Map<String, String> data = new HashMap<>();
                                data.put("favorito" + anuncio_atual.getDocumentId(), anuncio_atual.getDocumentId());
                                currentUserFavoritoReference.set(data);
                                //Removido dos favoritos
                            }

                        }
                    });
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                if (!currentUser.isAnonymous() && currentUser != null) {
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
                                        Toast.makeText(getContext(), "Removido dos favoritos.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });
                }
            }
        });

        recyclerViewAdapter.setOnItemClickListener(new AnuncioHomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(AnuncioForFirebase anuncio) {
                AnuncioDetailsFragment fragment = new AnuncioDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ANUNCIO_ATUAL", anuncio);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).addToBackStack(null).commit();
            }
        });

        linear_layout_perguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PerguntasActivity.class);
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

                mDots[i] = new TextView(getContext());
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

    @Override
    public void onStart() {
        super.onStart();
        meusAnunciosRef.document(anuncio_atual.getUserId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                //Obtendo a lista de Strings que referenciam os anuncios do usuário dono do anuncio aberto.
                anunciosIdList.clear();
                if (documentSnapshot.exists()) {
                    Map<String, Object> map = documentSnapshot.getData();
                    if (map != null) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if (entry.getValue() != null)
                                anunciosIdList.add(entry.getValue().toString());
                        }
                    }
                }
                //Obtendo todos os anuncios
                anunciosRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        anunciosList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                            anuncio.setDocumentId(documentSnapshot.getId());
                            anunciosList.add(anuncio);
                        }
                        //Obtendo os anuncios que estão na lista do usuário dono do anuncio visualizado atualmente, exceto o que já está aberto nos detalhes.
                        meusAnunciosList.clear();
                        for (int i = 0; i < anunciosList.size(); i++) {
                            if (anunciosIdList.contains(anunciosList.get(i).getDocumentId()) && !anunciosList.get(i).getDocumentId().equals(anuncio_atual.getDocumentId()))
                                meusAnunciosList.add(anunciosList.get(i));
                        }

                        recyclerViewAdapter.setAnunciosList(meusAnunciosList);
                        if (meusAnunciosList.isEmpty()) {
                            linearLayout_mais_anuncios.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}
