package com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sobralapps.android.shop_bazarsmg.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.CategoryHolder> {

    private List<String> categoryListName = new ArrayList<>();
    private RecyclerViewCategoryAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public RecyclerViewCategoryAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategoryAdapter.CategoryHolder holder, int position) {
        String currentCategoryName = categoryListName.get(position);
        holder.text_view_categoryName.setText(currentCategoryName);

    }

    @Override
    public int getItemCount() {
        return categoryListName.size();
    }

    public void setCategoryNameList(List<String> categoryListName) {
        this.categoryListName = categoryListName;
        notifyDataSetChanged();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView text_view_categoryName;

        public CategoryHolder(View itemView) {
            super(itemView);
            text_view_categoryName = itemView.findViewById(R.id.list_item_text_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(categoryListName.get(position), position);
                    }
                }
            });
        }
    }

    //Codificando a interface OnItemClickListener manualmente:
    public interface OnItemClickListener{
        void OnItemClick(String categoryName, int position);
    }

    public void setOnItemClickListener(RecyclerViewCategoryAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

}
