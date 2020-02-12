package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AvisoPerguntaRespostaForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels.AvisoPerguntaRecyclerAdapter;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AnuncioDetails.ChatRespostaActivity;
import com.sobralapps.android.shop_bazarsmg.HomeActivity;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AvisosFragment extends Fragment {

    private TextView textView_no_avisos;
    private ProgressBar progressBar;

    private AvisoPerguntaRecyclerAdapter recyclerViewAdapter;

    private List<AvisoPerguntaRespostaForFirebase> avisosList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users_avisos_ref = db.collection("users_avisos");
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public AvisosFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //O fragment_categorias é só um recyclerview com progressBar então podemos usá-lo como layout
        return inflater.inflate(R.layout.fragment_categorias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Avisos");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        textView_no_avisos = view.findViewById(R.id.text_view_no_anuncios);
        progressBar = view.findViewById(R.id.progressBar_categorias_fragment);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_categoria_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerViewAdapter = new AvisoPerguntaRecyclerAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(new AvisoPerguntaRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(AvisoPerguntaRespostaForFirebase aviso) {
                Intent i = new Intent(getActivity(), ChatRespostaActivity.class);
                i.putExtra("PERGUNTA", aviso);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        users_avisos_ref.document(currentUser.getUid()).collection("avisos")
                .orderBy("aviso_timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                progressBar.setVisibility(View.INVISIBLE);
                if (e != null) {
                    return;
                }
                avisosList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    AvisoPerguntaRespostaForFirebase aviso = documentSnapshot.toObject(AvisoPerguntaRespostaForFirebase.class);
                    aviso.setAvisoId(documentSnapshot.getId());
                    //Pegando os avisos que tem menos de 7 dias desde sua criação.
                    DateTime timestamp = new DateTime(aviso.getAviso_timestamp());
                    DateTime today = new DateTime(Calendar.getInstance().getTime());
                    if(Days.daysBetween(timestamp, today).getDays()<7)
                        avisosList.add(aviso);
                }
                recyclerViewAdapter.setAvisosList(avisosList);
                if(avisosList.isEmpty()) {
                    textView_no_avisos.setText("Não há avisos no momento.");
                    textView_no_avisos.setVisibility(View.VISIBLE);
                }else
                    textView_no_avisos.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_config, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (R.id.config_notification):
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", getActivity().getPackageName());
                    intent.putExtra("app_uid", getActivity().getApplicationInfo().uid);
                } else {
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                }
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
