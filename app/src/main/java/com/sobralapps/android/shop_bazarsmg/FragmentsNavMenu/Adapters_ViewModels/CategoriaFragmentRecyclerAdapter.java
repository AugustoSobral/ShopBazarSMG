package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sobralapps.android.shop_bazarsmg.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriaFragmentRecyclerAdapter extends RecyclerView.Adapter<CategoriaFragmentRecyclerAdapter.Holder> {

    private List<String> categoryList = new ArrayList<>();
    private List<Integer> numbersList = new ArrayList<>();
    private CategoriaFragmentRecyclerAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public CategoriaFragmentRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_categoria_fragment, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaFragmentRecyclerAdapter.Holder holder, int position) {
        String currentCategoryName = categoryList.get(position);
        int number = numbersList.get(position);
        holder.categoria.setText(currentCategoryName);
        holder.numero.setText("("+number+")");

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategoryList(List<String> categoryList, List<Integer> numbersList) {
        this.categoryList = categoryList;
        this.numbersList = numbersList;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder{

        private TextView categoria;
        private TextView numero;

        public Holder(@NonNull View itemView) {
            super(itemView);

            categoria = itemView.findViewById(R.id.list_item_categoria_text_fragment);
            numero = itemView.findViewById(R.id.list_item_categoria_number_fragment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(categoryList.get(position));
                    }
                }
            });
        }
    }

    //Codificando a interface OnItemClickListener manualmente:
    public interface OnItemClickListener{
        void OnItemClick(String categoria);
    }

    public void setOnItemClickListener(CategoriaFragmentRecyclerAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
