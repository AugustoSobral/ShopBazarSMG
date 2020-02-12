package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private int CATEGORIA_SELECIONADA = 0;

    private ImageButton btn_produtos;
    private ImageButton btn_veiculos;
    private ImageButton btn_imoveis;
    private ImageButton btn_servicos;
    private TextView text_produtos;
    private TextView text_veiculos;
    private TextView text_imoveis;
    private TextView text_servicos;
    private ProgressBar progressBar;
    private SearchView searchView;

    private AnuncioHomeRecyclerAdapter recyclerViewAdapter;

    private List<AnuncioForFirebase> anunciosList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference anunciosRef = db.collection("anuncios");

    private String queryText = "";

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_produtos = view.findViewById(R.id.imageButton);
        btn_imoveis = view.findViewById(R.id.imageButtonImoveis);
        btn_servicos = view.findViewById(R.id.imageButtonServicos);
        btn_veiculos = view.findViewById(R.id.imageButtonVeiculos);
        text_produtos = view.findViewById(R.id.textViewProdutosHome);
        text_imoveis = view.findViewById(R.id.textViewImoveisHome);
        text_servicos = view.findViewById(R.id.textViewServicosHome);
        text_veiculos = view.findViewById(R.id.textViewVeiculosHome);
        progressBar = view.findViewById(R.id.progressBar);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_home_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        recyclerViewAdapter = new AnuncioHomeRecyclerAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        btn_produtos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAnuncios(1);
                CATEGORIA_SELECIONADA = 1;
            }
        });
        text_produtos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAnuncios(1);
                CATEGORIA_SELECIONADA = 1;
            }
        });
        btn_veiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAnuncios(2);
                CATEGORIA_SELECIONADA = 2;
            }
        });
        text_veiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAnuncios(2);
                CATEGORIA_SELECIONADA = 2;
            }
        });
        btn_imoveis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAnuncios(3);
                CATEGORIA_SELECIONADA = 3;
            }
        });
        text_imoveis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAnuncios(3);
                CATEGORIA_SELECIONADA = 3;
            }
        });
        btn_servicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAnuncios(4);
                CATEGORIA_SELECIONADA = 4;
            }
        });
        text_servicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAnuncios(4);
                CATEGORIA_SELECIONADA = 4;
            }
        });

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

    //Quando um anuncio é adicionado, os listeners atualizam a lista em tempo real, a lista começa mostrando todos os anuncios
    //depois é filtrada quando as categorias superiores são clicadas.
    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        if (CATEGORIA_SELECIONADA == 0) {
            anunciosRef.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (CATEGORIA_SELECIONADA != 0)
                        return;
                    progressBar.setVisibility(View.INVISIBLE);
                    anunciosList.clear();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                        anuncio.setDocumentId(documentSnapshot.getId());
                        if (anuncio.isAnuncio_ativo())
                            anunciosList.add(anuncio);
                    }
                    recyclerViewAdapter.setAnunciosList(anunciosList);
                    //Setamos a query do searchView para não limparmos a pesquisa quando o snapshotListener for chamado (por mudança na lista de anuncios);
                    setQuery();
                }
            });
        }
        if (CATEGORIA_SELECIONADA == 1) {
            anunciosRef.whereEqualTo("anuncio_tipo", 1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (CATEGORIA_SELECIONADA != 1)
                        return;
                    progressBar.setVisibility(View.INVISIBLE);
                    anunciosList.clear();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                        anuncio.setDocumentId(documentSnapshot.getId());
                        if (anuncio.isAnuncio_ativo())
                            anunciosList.add(anuncio);
                    }
                    sortAnunciosListForDate(anunciosList);
                    recyclerViewAdapter.setAnunciosList(anunciosList);
                    //Setamos a query do searchView para não limparmos a pesquisa quando o snapshotListener for chamado (por mudança na lista de anuncios);
                    setQuery();
                }
            });
        }
        if (CATEGORIA_SELECIONADA == 2) {
            anunciosRef.whereEqualTo("anuncio_tipo", 2).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (CATEGORIA_SELECIONADA != 2)
                        return;
                    progressBar.setVisibility(View.INVISIBLE);
                    anunciosList.clear();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                        anuncio.setDocumentId(documentSnapshot.getId());
                        if(anuncio.isAnuncio_ativo())
                            anunciosList.add(anuncio);
                    }
                    sortAnunciosListForDate(anunciosList);
                    recyclerViewAdapter.setAnunciosList(anunciosList);
                    //Setamos a query do searchView para não limparmos a pesquisa quando o snapshotListener for chamado (por mudança na lista de anuncios);
                    setQuery();
                }
            });
        }
        if (CATEGORIA_SELECIONADA == 3) {
            anunciosRef.whereEqualTo("anuncio_tipo", 3).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (CATEGORIA_SELECIONADA != 3)
                        return;
                    progressBar.setVisibility(View.INVISIBLE);
                    anunciosList.clear();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                        anuncio.setDocumentId(documentSnapshot.getId());
                        if(anuncio.isAnuncio_ativo())
                            anunciosList.add(anuncio);
                    }
                    sortAnunciosListForDate(anunciosList);
                    recyclerViewAdapter.setAnunciosList(anunciosList);
                    //Setamos a query do searchView para não limparmos a pesquisa quando o snapshotListener for chamado (por mudança na lista de anuncios);
                    setQuery();
                }
            });
        }
        if (CATEGORIA_SELECIONADA == 4) {
            anunciosRef.whereEqualTo("anuncio_tipo", 4).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (CATEGORIA_SELECIONADA != 4)
                        return;
                    progressBar.setVisibility(View.INVISIBLE);
                    anunciosList.clear();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                        anuncio.setDocumentId(documentSnapshot.getId());
                        if(anuncio.isAnuncio_ativo())
                            anunciosList.add(anuncio);
                    }
                    sortAnunciosListForDate(anunciosList);
                    recyclerViewAdapter.setAnunciosList(anunciosList);
                    //Setamos a query do searchView para não limparmos a pesquisa quando o snapshotListener for chamado (por mudança na lista de anuncios);
                    setQuery();
                }
            });
        }
    }

    private void loadAnuncios(final int tipo) {
        //Quando clicamos nos filtros por tipo de anuncio, limpamos o searchview.
        searchView.setQuery("", false);
        searchView.clearFocus();
        progressBar.setVisibility(View.VISIBLE);
        anunciosRef.whereEqualTo("anuncio_tipo", tipo).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (CATEGORIA_SELECIONADA != tipo)
                    return;

                progressBar.setVisibility(View.INVISIBLE);
                anunciosList.clear();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                    anuncio.setDocumentId(documentSnapshot.getId());
                    if(anuncio.isAnuncio_ativo())
                        anunciosList.add(anuncio);
                }
                sortAnunciosListForDate(anunciosList);
                recyclerViewAdapter.setAnunciosList(anunciosList);
                //Setamos a query do searchView para não limparmos a pesquisa quando o snapshotListener for chamado (por mudança na lista de anuncios);
                setQuery();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.pesquisa_button);
        searchView = (SearchView) searchItem.getActionView();


        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);                                       //Troca o botão de buscar para confirmar, já que a busca é em tempo real então não faz muito sentido ter o botão "pesquisar"

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                //Convertendo o texto de entrada para caracteres equivalentes sem acentuação, ç etc
                String texto;
                queryText = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                //Chamando o filter
                recyclerViewAdapter.getFilter().filter(queryText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Convertendo o texto de entrada para caracteres equivalentes sem acentuação, ç etc
                String texto;
                queryText = Normalizer.normalize(newText, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                //Chamando o filter
                recyclerViewAdapter.getFilter().filter(queryText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void sortAnunciosListForDate(List<AnuncioForFirebase> list) {
        Collections.sort(list, new Comparator<AnuncioForFirebase>() {
            @Override
            public int compare(AnuncioForFirebase o1, AnuncioForFirebase o2) {
                Date data1, data2;
                data1 = o1.getTimestamp();
                data2 = o2.getTimestamp();
                return data2.compareTo(data1);
            }
        });
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void setQuery() {
        //Setamos a query do searchView para não limparmos a pesquisa quando o snapshotListener for chamado (por mudança na lista de anuncios);
        if (searchView != null)
            searchView.post(new Runnable() {
                @Override
                public void run() {
                    searchView.setQuery(queryText, true);
                }
            });
    }

}
