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
import com.sobralapps.android.shop_bazarsmg.Data.Objects.AvisoPerguntaRespostaForFirebase;
import com.sobralapps.android.shop_bazarsmg.Helpers.RoundedTransformation;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AvisoPerguntaRecyclerAdapter extends RecyclerView.Adapter<AvisoPerguntaRecyclerAdapter.Holder> {

    private OnItemClickListener listener;
    private List<AvisoPerguntaRespostaForFirebase> avisosList = new ArrayList<>();
    private CollectionReference anunciosRef = FirebaseFirestore.getInstance().collection("anuncios");

    @NonNull
    @Override
    public AvisoPerguntaRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_aviso_pergunta, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AvisoPerguntaRecyclerAdapter.Holder holder, int position) {
        final AvisoPerguntaRespostaForFirebase currentAviso = avisosList.get(position);
        holder.titulo_text_view.setText(currentAviso.getAviso_titulo());
        holder.pergunta_resposta_text_view.setText(currentAviso.getAviso_pergunta_resposta());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterHours = new SimpleDateFormat("HH:mm");
        String date = formatter.format(currentAviso.getAviso_timestamp());
        String hours = formatterHours.format((currentAviso.getAviso_timestamp()));
        holder.informacao_text_view.setText("" + date + " às " +hours);

        if(currentAviso.isAvisoRead())
            holder.unread.setVisibility(View.INVISIBLE);
        if(!currentAviso.isAvisoRead())
            holder.unread.setVisibility(View.VISIBLE);

        String anuncioId = currentAviso.getAnuncioId();
        holder.progressBar.setVisibility(View.VISIBLE);
        anunciosRef.document(anuncioId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    AnuncioForFirebase anuncio = documentSnapshot.toObject(AnuncioForFirebase.class);
                    if (anuncio.getImage0() != null) {
                        Picasso.get().load(Uri.parse(anuncio.getImage0()))
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
                    if (anuncio.getImage0() == null) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        Picasso.get().load(R.drawable.anuncio_sem_foto).placeholder(R.drawable.anuncio_sem_foto)
                                .error(R.drawable.anuncio_sem_foto)
                                .fit().centerCrop().into(holder.imagePrincipal);
                    }
                } else {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    Picasso.get().load(R.drawable.anuncio_sem_foto).placeholder(R.drawable.anuncio_sem_foto)
                            .error(R.drawable.anuncio_sem_foto)
                            .fit().centerCrop().into(holder.imagePrincipal);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return avisosList.size();
    }

    public void setAvisosList(List<AvisoPerguntaRespostaForFirebase> avisosList){
        this.avisosList = avisosList;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{

        private ImageView imagePrincipal;
        private TextView titulo_text_view;
        private TextView pergunta_resposta_text_view;
        private TextView informacao_text_view;
        private TextView unread;
        private ProgressBar progressBar;

        public Holder(@NonNull View itemView) {
            super(itemView);

            imagePrincipal = itemView.findViewById(R.id.list_aviso_pergunta_image);
            titulo_text_view = itemView.findViewById(R.id.list_aviso_pergunta_titulo);
            pergunta_resposta_text_view = itemView.findViewById(R.id.list_aviso_pergunta_pergunta);
            informacao_text_view = itemView.findViewById(R.id.list_aviso_pergunta_informacao);
            unread = itemView.findViewById(R.id.text_view_list_item_aviso_unread);
            progressBar = itemView.findViewById(R.id.progressBar_list_item_aviso_pergunta);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(avisosList.get(position));
                    }
                }
            });

        }

    }

    //Codificando a interface OnItemClickListener manualmente:
    public interface OnItemClickListener{
        void OnItemClick(AvisoPerguntaRespostaForFirebase aviso);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
