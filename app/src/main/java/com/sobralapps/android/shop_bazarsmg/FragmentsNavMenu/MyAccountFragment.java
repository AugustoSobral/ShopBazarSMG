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

import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.SecurityActivities.SecurityActivity;
import com.sobralapps.android.shop_bazarsmg.HomeActivity;
import com.sobralapps.android.shop_bazarsmg.R;
import com.sobralapps.android.shop_bazarsmg.Sell.SellOptionsActivity;
import com.sobralapps.android.shop_bazarsmg.Start_Login_Register.LoginRegisterForAnonimousUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyAccountFragment extends Fragment {

    private Button btn_anunciar;
    private TextView textView_meus_anuncios;
    private TextView textView_profile;
    private TextView textView_security;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public MyAccountFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Minha conta");

        btn_anunciar = view.findViewById(R.id.btn_anunciar);
        textView_profile = view.findViewById(R.id.text_view_my_account_profile);
        textView_security = view.findViewById(R.id.text_view_my_account_security);
        textView_meus_anuncios = view.findViewById(R.id.text_view_meus_anuncios);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btn_anunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null && !mUser.isAnonymous())
                    startActivity(new Intent(getActivity(), SellOptionsActivity.class));
                if (mUser == null || mUser.isAnonymous())
                    startActivity(new Intent(getActivity(), LoginRegisterForAnonimousUser.class));
            }
        });

        textView_meus_anuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null && !mUser.isAnonymous()) {
                    getFragmentManager().beginTransaction().replace(R.id.fragments_container, new MeusAnunciosFragment()).addToBackStack(null)
                            .commit();
                }
                if (mUser == null || mUser.isAnonymous())
                    startActivity(new Intent(getActivity(), LoginRegisterForAnonimousUser.class));
            }
        });

        textView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null && !mUser.isAnonymous()) {
                    getFragmentManager().beginTransaction().replace(R.id.fragments_container, new ProfileFragment()).addToBackStack(null)
                            .commit();
                }
                if (mUser == null || mUser.isAnonymous())
                    startActivity(new Intent(getActivity(), LoginRegisterForAnonimousUser.class));
            }
        });

        textView_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUser != null && !mUser.isAnonymous()) {
                    startActivity(new Intent(getActivity(), SecurityActivity.class));
                }
                if (mUser == null || mUser.isAnonymous())
                    startActivity(new Intent(getActivity(), LoginRegisterForAnonimousUser.class));
            }
        });

    }
}
