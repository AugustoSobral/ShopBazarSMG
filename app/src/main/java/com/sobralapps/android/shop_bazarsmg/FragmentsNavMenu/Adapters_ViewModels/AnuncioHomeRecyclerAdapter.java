package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.Helpers.RoundedTransformation;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnuncioHomeRecyclerAdapter extends RecyclerView.Adapter<AnuncioHomeRecyclerAdapter.AnuncioHolder> implements Filterable {

    private List<AnuncioForFirebase> anunciosList = new ArrayList<>();
    private List<AnuncioForFirebase> anunciosBusca = new ArrayList<>();
    private OnItemClickListener listener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference favoriteRef = db.collection("users_favoritos");

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @NonNull
    @Override
    public AnuncioHomeRecyclerAdapter.AnuncioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_anuncio, parent, false);
        return new AnuncioHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnuncioHomeRecyclerAdapter.AnuncioHolder holder, int position) {
        final AnuncioForFirebase currentAnuncio = anunciosBusca.get(position);
        holder.titulo_text_view.setText(currentAnuncio.getTitulo());
        if(currentAnuncio.getPreco()==null)
            holder.preco_text_view.setText("Preço à combinar.");
        else
            holder.preco_text_view.setText(currentAnuncio.getPreco());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterHours = new SimpleDateFormat("HH:mm");
        String date = formatter.format(currentAnuncio.getTimestamp());
        String hours = formatterHours.format((currentAnuncio.getTimestamp()));
        holder.informacao_text_view.setText("Anunciado: " + date + " às " +hours);

        if(currentAnuncio.getImage0()!=null){
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(Uri.parse(currentAnuncio.getImage0()))
                    .error(R.drawable.anuncio_sem_foto)
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

        if(!currentUser.isAnonymous() && currentUser!=null) {
            final DocumentReference currentUserFavoritoReference = favoriteRef.document(currentUser.getUid());
            currentUserFavoritoReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.getData().containsKey("favorito" + currentAnuncio.getDocumentId()))
                            holder.btn_favorite.setLiked(true);
                        else
                            holder.btn_favorite.setLiked(false);
                    }
                }
            });
        }
        if(currentUser.isAnonymous())
            holder.btn_favorite.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return anunciosBusca.size();
    }

    public void setAnunciosList(List<AnuncioForFirebase> anunciosList){
        this.anunciosList = anunciosList;
        anunciosBusca.clear();
        anunciosBusca.addAll(anunciosList);
        notifyDataSetChanged();
    }

    class AnuncioHolder extends RecyclerView.ViewHolder{

        private ImageView imagePrincipal;
        private TextView titulo_text_view;
        private TextView preco_text_view;
        private TextView informacao_text_view;
        private ProgressBar progressBar;
        private LikeButton btn_favorite;

        public AnuncioHolder(@NonNull View itemView) {
            super(itemView);

            imagePrincipal = itemView.findViewById(R.id.list_anuncio_image);
            titulo_text_view = itemView.findViewById(R.id.list_anuncio_titulo);
            preco_text_view = itemView.findViewById(R.id.list_anuncio_preco);
            informacao_text_view = itemView.findViewById(R.id.list_anuncio_informacao);
            progressBar = itemView.findViewById(R.id.progressBar_list_item_anuncio);
            btn_favorite = itemView.findViewById(R.id.favorite_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(anunciosList.get(position));
                    }
                }
            });

            btn_favorite.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                    if(currentUser.isAnonymous()){
                        btn_favorite.setLiked(false);
                        return;
                    }

                    final int position = getAdapterPosition();
                    final DocumentReference currentUserFavoritoReference = favoriteRef.document(currentUser.getUid());
                    currentUserFavoritoReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {

                                Map<String, String> data = new HashMap<>();
                                data.put("favorito" + anunciosList.get(position).getDocumentId(), anunciosList.get(position).getDocumentId());
                                currentUserFavoritoReference.set(data, SetOptions.merge());

                            } else {
                                Map<String, String> data = new HashMap<>();
                                data.put("favorito" + anunciosList.get(position).getDocumentId(), anunciosList.get(position).getDocumentId());
                                currentUserFavoritoReference.set(data);
                            }
                        }
                    });
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    final int position = getAdapterPosition();
                    final DocumentReference currentUserFavoritoReference = favoriteRef.document(currentUser.getUid());
                    currentUserFavoritoReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {

                                //Apagando o campo no docuemnto do firestore.
                                Map<String, Object> data = new HashMap<>();
                                data.put("favorito" + anunciosList.get(position).getDocumentId(), FieldValue.delete());
                                currentUserFavoritoReference.update(data);

                            }
                        }
                    });
                }
            });
        }
    }

    //Codificando a interface OnItemClickListener manualmente:
    public interface OnItemClickListener{
        void OnItemClick(AnuncioForFirebase anuncio);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    //Métodos para implementar a pesquisa em anunciosList pelo título.
    @Override
    public Filter getFilter() {
        return namesFilter;
    }

    private Filter namesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AnuncioForFirebase> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(anunciosList);                                             //Quando não há nada digitado na pesquisa, mostramos a lista completa (padrão).
            }else{
                //Trasforma a CharSequence em uma String, coloca tudo minúsculo e remove espaços.
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(AnuncioForFirebase anuncio : anunciosList){                                     //Percorre anuncio por anuncio da lista de anuncios.
                    String tituloNoASCII;
                    tituloNoASCII = Normalizer.normalize(anuncio.getTitulo(), Normalizer.Form.NFD)
                            .replaceAll("[^\\p{ASCII}]", "");
                    if(tituloNoASCII.toLowerCase().contains(filterPattern)){                          //Se a CharSequence digitada estiver contida no anuncio.getTitulo(), adicionamos esse anuncio ao resultado da busca.
                        filteredList.add(anuncio);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            anunciosBusca.clear();
            anunciosBusca.addAll((List)results.values);                                             //A variável anunciosBusca só contém os itens que condizem com a busca agora.
            notifyDataSetChanged();
        }
    };
}
