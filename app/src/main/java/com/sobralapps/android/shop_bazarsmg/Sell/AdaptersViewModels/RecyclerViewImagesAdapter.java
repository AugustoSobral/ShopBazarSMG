package com.sobralapps.android.shop_bazarsmg.Sell.AdaptersViewModels;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sobralapps.android.shop_bazarsmg.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewImagesAdapter extends RecyclerView.Adapter<RecyclerViewImagesAdapter.ImagesHolder> {

    private List<Uri> imagesList = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_images, parent, false);
        return new ImagesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesHolder holder, int position) {
        Uri currentImageUri = imagesList.get(position);
        if(currentImageUri != null)
            Picasso.get().load(currentImageUri).fit().centerCrop().into(holder.imagePhoto);

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public void setImagesList(List<Uri> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    class ImagesHolder extends RecyclerView.ViewHolder {
        private ImageView imagePhoto;

        public ImagesHolder(View itemView) {
            super(itemView);
            imagePhoto = itemView.findViewById(R.id.list_item_images_chooser);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(position);
                    }
                }
            });
        }
    }

    //Codificando a interface OnItemClickListener manualmente:
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(RecyclerViewImagesAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

}
