package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PromocaoCreateFragment extends Fragment {

    private TextView email_contato;
    private TextView preço_por_dia;
    private String email;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PromocaoCreateFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promocao_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_send_email = view.findViewById(R.id.btn_send_email_to_support);
        email_contato = view.findViewById(R.id.text_view_email_contato);
        preço_por_dia = view.findViewById(R.id.text_view_preco_por_dia_promocao);


        btn_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto: " + email));
                //intent.putExtra(Intent.EXTRA_EMAIL, email);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(Intent.createChooser(intent, "Escolha um serviço de e-mails:"));
                else
                    Toast.makeText(getContext(), "Nenhum aplicativo de envio de e-mails está instalado.", Toast.LENGTH_SHORT).show();
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
                            email = documentSnapshot.getString("email");
                            email_contato.setText(email);
                        }
                    }
                });

        db.collection("suporte_contato").document("preco_por_dia")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }
                        if(documentSnapshot.exists())
                            preço_por_dia.setText(documentSnapshot.getString("preco_por_dia"));
                    }
                });
    }
}
