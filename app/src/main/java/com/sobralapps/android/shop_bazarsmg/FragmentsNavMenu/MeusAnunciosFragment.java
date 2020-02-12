package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.content.Intent;
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
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.AnuncioEditRecyclerViewAdapter;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.EditAnuncioActivities.AnuncioEditOptionsActivity;
import com.sobralapps.android.shop_bazarsmg.HomeActivity;
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

public class MeusAnunciosFragment extends Fragment {

    private TextView text_no_anuncio;

    private ProgressBar progressBar;
    private AnuncioEditRecyclerViewAdapter recyclerViewAdapter;
    private List<String> anunciosIdList = new ArrayList<>();
    private List<AnuncioForFirebase> anunciosList = new ArrayList<>();
    private List<AnuncioForFirebase> meusAnunciosList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference meusAnunciosRef = db.collection("users_anuncios");
    private CollectionReference anunciosRef = db.collection("anuncios");

    public MeusAnunciosFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_categorias, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Meus anúncios");

        progressBar = view.findViewById(R.id.progressBar_categorias_fragment);
        text_no_anuncio = view.findViewById(R.id.text_view_no_anuncios);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_categoria_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        recyclerViewAdapter = new AnuncioEditRecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);


        //Passando o anuncio clicado para o fragment details
        recyclerViewAdapter.setOnItemClickListener(new AnuncioEditRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(AnuncioForFirebase anuncio) {
                Intent i = new Intent(getActivity(), AnuncioEditOptionsActivity.class);
                i.putExtra("MEU_ANUNCIO_ATUAL", anuncio);
                startActivity(i);
            }
        });


    }

    //Quando um anuncio é adicionado, os listeners atualizam a lista em tempo real
    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);

        meusAnunciosRef.document(currentUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }

                //Obtendo a lista de Strings que referenciam os anuncios do usuário
                anunciosIdList.clear();
                if (documentSnapshot.exists()) {
                    Map<String, Object> map = documentSnapshot.getData();
                    if (map != null) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if(entry.getValue()!=null)
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
                            anunciosList.add(anuncio);
                        }

                        //Obtendo os anuncios que estão na lista do usuáio
                        meusAnunciosList.clear();
                        for (int i = 0; i < anunciosList.size(); i++) {
                            if (anunciosIdList.contains(anunciosList.get(i).getDocumentId()))
                                meusAnunciosList.add(anunciosList.get(i));
                        }

                        recyclerViewAdapter.setAnunciosList(meusAnunciosList);

                        if(meusAnunciosList.isEmpty()) {
                            text_no_anuncio.setVisibility(View.VISIBLE);
                            text_no_anuncio.setText("Você ainda não possui anúncios publicados.");
                        }
                    }
                });

            }
        });

    }



}
