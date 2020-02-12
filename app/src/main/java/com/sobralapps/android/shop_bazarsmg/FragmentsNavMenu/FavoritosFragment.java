package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.AnuncioHomeRecyclerAdapter;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AnuncioDetails.AnuncioDetailsFragment;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoritosFragment extends Fragment {

    private TextView text_no_anuncio;

    private ProgressBar progressBar;
    private AnuncioHomeRecyclerAdapter recyclerViewAdapter;
    private List<String> anunciosIdList = new ArrayList<>();
    private List<AnuncioForFirebase> anunciosList = new ArrayList<>();
    private List<AnuncioForFirebase> anunciosFavoritosList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference favoriteRef = db.collection("users_favoritos");
    private CollectionReference anunciosRef = db.collection("anuncios");

    public FavoritosFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_categorias, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar_categorias_fragment);
        text_no_anuncio = view.findViewById(R.id.text_view_no_anuncios);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_categoria_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        recyclerViewAdapter = new AnuncioHomeRecyclerAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);


        //Passando o anuncio clicado para o fragment details
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


    }

    //Quando um anuncio é adicionado, os listeners atualizam a lista em tempo real
    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);

        favoriteRef.document(currentUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }

                //Obtendo a lista de Strings que referencia os anuncios favoritos do usuário
                anunciosIdList.clear();
                if (documentSnapshot.exists()) {
                    Map<String, Object> map = documentSnapshot.getData();
                    if (map != null) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            anunciosIdList.add(entry.getValue().toString());
                        }
                    }
                }

                //Obtendo todos os anuncios
                anunciosRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (e != null) {
                            return;
                        }
                        anunciosList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                            anuncio.setDocumentId(documentSnapshot.getId());
                            if(anuncio.isAnuncio_ativo())
                                anunciosList.add(anuncio);
                        }

                        //Obtendo os anuncios que estão na lista de favoritos do usuáio
                        anunciosFavoritosList.clear();
                        for (int i = 0; i < anunciosList.size(); i++) {
                            if (anunciosIdList.contains(anunciosList.get(i).getDocumentId()))
                                anunciosFavoritosList.add(anunciosList.get(i));
                        }
                        recyclerViewAdapter.setAnunciosList(anunciosFavoritosList);
                        if(anunciosFavoritosList.isEmpty()) {
                            text_no_anuncio.setVisibility(View.VISIBLE);
                            text_no_anuncio.setText("Você ainda não adicionou anúncios aos seus favoritos.");
                        }
                    }
                });

            }
        });

    }

}
