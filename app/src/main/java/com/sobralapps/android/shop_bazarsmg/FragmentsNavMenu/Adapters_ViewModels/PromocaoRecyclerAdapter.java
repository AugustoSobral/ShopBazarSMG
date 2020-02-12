package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sobralapps.android.shop_bazarsmg.Data.Objects.PromocaoForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PromocaoRecyclerAdapter extends RecyclerView.Adapter<PromocaoRecyclerAdapter.Holder> {

    private List<PromocaoForFirebase> promocoesList = new ArrayList<>();
    private PromocaoRecyclerAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public PromocaoRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_promocao, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        PromocaoForFirebase currentPromocao = promocoesList.get(position);

        holder.titulo_text_view.setText(currentPromocao.getTitulo());
        holder.descricao_text_view.setText(currentPromocao.getDescricao());
        String desconto = currentPromocao.getDesconto() + "%";
        if(currentPromocao.getDesconto()!=null)
            holder.desconto_text_view.setText(desconto);
        else
            holder.desconto_text_view.setVisibility(View.GONE);

        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(Uri.parse(currentPromocao.getImagem())).fit().into(holder.image, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return promocoesList.size();
    }

    public void setPromocoesList(List<PromocaoForFirebase> promocoesList){
        this.promocoesList = promocoesList;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView titulo_text_view;
        private TextView descricao_text_view;
        private TextView desconto_text_view;
        private ProgressBar progressBar;

        public Holder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.list_promocao_image);
            titulo_text_view = itemView.findViewById(R.id.text_view_promocao_titulo);
            descricao_text_view = itemView.findViewById(R.id.text_view_promocao_informacao);
            desconto_text_view = itemView.findViewById(R.id.text_view_promocao_desconto);
            progressBar = itemView.findViewById(R.id.progressBar_list_item_promocao);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(promocoesList.get(position));
                    }
                }
            });

        }
    }

    //Codificando a interface OnItemClickListener manualmente:
    public interface OnItemClickListener{
        void OnItemClick(PromocaoForFirebase promocao);
    }

    public void setOnItemClickListener(PromocaoRecyclerAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

}
