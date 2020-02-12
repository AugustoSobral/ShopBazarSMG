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
import com.sobralapps.android.shop_bazarsmg.Data.Objects.PromocaoForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.PromocaoRecyclerAdapter;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PromocoesFragment extends Fragment {

    private TextView textView_no_promocoes;
    private ProgressBar progressBar;
    private FloatingActionButton add_promocao;

    private PromocaoRecyclerAdapter recyclerViewAdapter;

    private List<PromocaoForFirebase> promocoesList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference promocoes_ref = db.collection("promocoes");

    public PromocoesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //O fragment_categorias é só um recyclerview com progressBar então podemos usá-lo como layout
        return inflater.inflate(R.layout.fragment_promocoes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView_no_promocoes = view.findViewById(R.id.text_view_no_anuncios);
        progressBar = view.findViewById(R.id.progressBar_categorias_fragment);
        add_promocao = view.findViewById(R.id.fab_generico);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_categoria_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setBackgroundResource(R.color.colorTransparentWhite);
        recyclerViewAdapter = new PromocaoRecyclerAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        add_promocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragments_container, new PromocaoCreateFragment())
                        .addToBackStack(null).commit();
            }
        });

        recyclerViewAdapter.setOnItemClickListener(new PromocaoRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(PromocaoForFirebase promocao) {
                PromocaoDetailsFragment fragment = new PromocaoDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("PROMOCAO_ATUAL", promocao);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        promocoes_ref.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                progressBar.setVisibility(View.INVISIBLE);
                promocoesList.clear();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    PromocaoForFirebase promocao = documentSnapshot.toObject(PromocaoForFirebase.class);
                    //promocao.setDocumentId(documentSnapshot.getId());
                    promocoesList.add(promocao);
                }
                recyclerViewAdapter.setPromocoesList(promocoesList);
                if(promocoesList.isEmpty()) {
                    textView_no_promocoes.setText("Não há promoções no momento.");
                    textView_no_promocoes.setVisibility(View.VISIBLE);
                }else
                    textView_no_promocoes.setVisibility(View.INVISIBLE);
            }
        });
    }
}
