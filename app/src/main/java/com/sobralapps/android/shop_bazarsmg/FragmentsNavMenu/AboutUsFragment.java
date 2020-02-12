package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AboutUsFragment extends Fragment {

    private TextView textView_email;
    private TextView versao_app;
    private Button btn_politica;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AboutUsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView_email = view.findViewById(R.id.text_view_email_contato);
        btn_politica = view.findViewById(R.id.btn_politica_privacidade);
        versao_app = view.findViewById(R.id.text_view_versao_app);

        btn_politica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PoliticaPrivacidadeActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        db.collection("suporte_contato").document("email_para_contato")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        if(documentSnapshot.exists()) {
                            String email = documentSnapshot.getString("email");
                            textView_email.setText(email);
                        }
                    }
                });
        db.collection("suporte_contato").document("versao")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        if(documentSnapshot.exists()) {
                            String versao = documentSnapshot.getString("versao");
                            versao_app.setText(versao);
                        }
                    }
                });


    }
}
