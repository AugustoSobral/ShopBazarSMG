package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.AnuncioHomeRecyclerAdapter;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AnuncioDetails.AnuncioDetailsFragment;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class AnuncioPorCategoriaFragment extends Fragment {

    private List<AnuncioForFirebase> anunciosList = new ArrayList<>();
    private AnuncioHomeRecyclerAdapter recyclerViewAdapter;
    private ProgressBar progressBar;
    private TextView text_no_anuncio;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");

    private String categoria_atual;

    public AnuncioPorCategoriaFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Vou usar o mesmo arquivo de layout, pq só preciso de um layout com um recyclerView.
        setHasOptionsMenu(true);
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

        categoria_atual = getArguments().getString("CATEGORIA_ATUAL");


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


    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        anunciosRef.whereEqualTo("categoria", categoria_atual).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                progressBar.setVisibility(View.INVISIBLE);
                anunciosList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                    anuncio.setDocumentId(documentSnapshot.getId());
                    if (anuncio.isAnuncio_ativo())
                        anunciosList.add(anuncio);

                }
                recyclerViewAdapter.setAnunciosList(anunciosList);
                if (anunciosList.isEmpty())
                    text_no_anuncio.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.pesquisa_button);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);                                       //Troca o botão de buscar para confirmar, já que a busca é em tempo real então não faz muito sentido ter o botão "pesquisar"

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Convertendo o texto de entrada para caracteres equivalentes sem acentuação, ç etc
                String texto;
                texto = Normalizer.normalize(newText, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                //Chamando o filter
                recyclerViewAdapter.getFilter().filter(texto);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
