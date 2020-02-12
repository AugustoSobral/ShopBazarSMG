package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sobralapps.android.shop_bazarsmg.HomeActivity;
import com.sobralapps.android.shop_bazarsmg.R;

public class ConfigAvisosNotificationsFragment extends Fragment {

    private CheckBox checkBox_perguntas;
    private CheckBox checkBox_promocoes;

    public ConfigAvisosNotificationsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Notificações");

        checkBox_perguntas = view.findViewById(R.id.checkBox_perguntas);
        checkBox_perguntas = view.findViewById(R.id.checkBox_promocoes);

    }
}
