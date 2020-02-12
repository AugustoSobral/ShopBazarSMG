package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sobralapps.android.shop_bazarsmg.Constants;
import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.CategoriaFragmentRecyclerAdapter;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CategoriasFragment extends Fragment {


    private CategoriaFragmentRecyclerAdapter recyclerAdapter;
    private List<String> categoriaList = new ArrayList<>();
    private List<Integer> numbersList = new ArrayList<>();


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");

    public CategoriasFragment() {
        // Required menu_empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categorias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_categoria_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        recyclerAdapter = new CategoriaFragmentRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);

        categoriaList.clear();
        categoriaList.addAll(Constants.produtosCategory);
        categoriaList.addAll(Constants.veiculosCategory);
        categoriaList.addAll(Constants.imoveisCategory);
        categoriaList.addAll(Constants.servicosCategory);

        sortListAlphabetically(categoriaList);

        recyclerAdapter.setOnItemClickListener(new CategoriaFragmentRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String categoria) {
                AnuncioPorCategoriaFragment fragment = new AnuncioPorCategoriaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("CATEGORIA_ATUAL", categoria);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        anunciosRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                numbersList = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);

                    for(int i=0; i<numbersList.size(); i++){
                        if(anuncio.getCategoria().equals(categoriaList.get(i)) && anuncio.isAnuncio_ativo())
                            numbersList.set(i, numbersList.get(i)+1);
                    }

                }
                recyclerAdapter.setCategoryList(categoriaList, numbersList);

            }
        });

    }

    private void sortListAlphabetically(List<String> categoriasNoAlphabetically){
        Collections.sort(categoriasNoAlphabetically, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String nome1, nome2;
                nome1 = Normalizer.normalize(o1, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                nome2 = Normalizer.normalize(o2, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                return nome1.compareTo(nome2);
            }
        });
        recyclerAdapter.notifyDataSetChanged();
    }
}
