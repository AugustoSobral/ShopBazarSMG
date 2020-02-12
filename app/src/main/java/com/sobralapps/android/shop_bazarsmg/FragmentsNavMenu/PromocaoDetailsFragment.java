package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.PromocaoForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PromocaoDetailsFragment extends Fragment {

    private ProgressBar progressBar;

    public PromocaoDetailsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_promocao_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView descricao;
        final TextView endereco;
        TextView texto;
        ImageView image;
        ImageButton btn_maps;
        TextView maps;

        descricao = view.findViewById(R.id.text_view_descricao_promocao_details);
        endereco = view.findViewById(R.id.text_view_endereco_promocao_details);
        texto = view.findViewById(R.id.text_view_texto_promocao_details);
        image = view.findViewById(R.id.image_view_promocao_details);
        progressBar = view.findViewById(R.id.progress_bar_promocao_details);
        btn_maps = view.findViewById(R.id.imageButton_maps);
        maps = view.findViewById(R.id.textView_maps);

        final PromocaoForFirebase promocao = (PromocaoForFirebase) getArguments().getSerializable("PROMOCAO_ATUAL");

        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(Uri.parse(promocao.getImagem())).fit().into(image, new Callback() {
            @Override
            public void onSuccess() {
            progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        descricao.setText(promocao.getDescricao());
        endereco.setText(promocao.getEndereco());
        texto.setText(promocao.getTexto());

        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+promocao.getEndereco()+"&travelmode=driving");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
                //mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                //Verificando se há pelo menos um aplicativo que pode lidar com o intent.
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(mapIntent, "Selecione um aplicativo"));
                }else
                    Toast.makeText(getContext(), "Nenhum aplicativo capaz de lidar com essa intenção está instalado.", Toast.LENGTH_SHORT).show();
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+promocao.getEndereco()+"&travelmode=driving");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
                //mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                //Verificando se há pelo menos um aplicativo que pode lidar com o intent.
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(mapIntent, "Selecione um aplicativo"));
                }else
                    Toast.makeText(getContext(), "Nenhum aplicativo capaz de lidar com essa intenção está instalado.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
