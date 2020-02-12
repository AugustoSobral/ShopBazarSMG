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

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.Helpers.RoundedTransformation;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AnuncioForHorizontalContainerRecyclerViewAdapter extends RecyclerView.Adapter<AnuncioForHorizontalContainerRecyclerViewAdapter.AnuncioHolder> {

    private List<AnuncioForFirebase> anunciosList = new ArrayList<>();
    private AnuncioHomeRecyclerAdapter.OnItemClickListener listener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull
    @Override
    public AnuncioForHorizontalContainerRecyclerViewAdapter.AnuncioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_anuncio_for_horizontal_container, parent, false);
        return new AnuncioForHorizontalContainerRecyclerViewAdapter.AnuncioHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnuncioForHorizontalContainerRecyclerViewAdapter.AnuncioHolder holder, int position) {
        final AnuncioForFirebase currentAnuncio = anunciosList.get(position);
        holder.titulo_text_view.setText(currentAnuncio.getTitulo());
        if(currentAnuncio.getPreco()==null)
            holder.preco_text_view.setText("Preço à combinar.");
        else
            holder.preco_text_view.setText(currentAnuncio.getPreco());
        if(currentAnuncio.getImage0()!=null){
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(Uri.parse(currentAnuncio.getImage0()))
                    .error(R.drawable.ic_image_sample_50dp_grey)
                    .transform(new RoundedTransformation(20, 2))
                    //.resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                    .fit().centerCrop().into(holder.imagePrincipal, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        if(currentAnuncio.getImage0()==null)
            Picasso.get().load(R.drawable.anuncio_sem_foto).placeholder(R.drawable.anuncio_sem_foto)
                    .error(R.drawable.anuncio_sem_foto)
                    .fit().centerCrop().into(holder.imagePrincipal);
    }

    @Override
    public int getItemCount() {
        return anunciosList.size();
    }

    public void setAnunciosList(List<AnuncioForFirebase> anunciosList){
        this.anunciosList = anunciosList;
        notifyDataSetChanged();
    }

    class AnuncioHolder extends RecyclerView.ViewHolder{

        private ImageView imagePrincipal;
        private TextView titulo_text_view;
        private TextView preco_text_view;
        private TextView informacao_text_view;
        private ProgressBar progressBar;

        public AnuncioHolder(@NonNull View itemView) {
            super(itemView);

            imagePrincipal = itemView.findViewById(R.id.list_anuncio_image);
            titulo_text_view = itemView.findViewById(R.id.list_anuncio_titulo);
            preco_text_view = itemView.findViewById(R.id.list_anuncio_preco);
            informacao_text_view = itemView.findViewById(R.id.list_anuncio_informacao);
            progressBar = itemView.findViewById(R.id.progressBar_list_item_anuncio);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(anunciosList.get(position));
                    }
                }
            });

        }
    }

    //Codificando a interface OnItemClickListener manualmente:
    public interface OnItemClickListener{
        void OnItemClick(AnuncioForFirebase anuncio);
    }

    public void setOnItemClickListener(AnuncioHomeRecyclerAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

}
