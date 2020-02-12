package com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.Adapters_ViewModels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.PerguntaRespostaForFirebase;
import com.sobralapps.android.shop_bazarsmg.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PerguntasRecyclerAdapter extends RecyclerView.Adapter<PerguntasRecyclerAdapter.Holder> {

    private List<PerguntaRespostaForFirebase> pergunta_respostaList = new ArrayList<>();

    @NonNull
    @Override
    public PerguntasRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pergunta, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PerguntasRecyclerAdapter.Holder holder, int position) {
        PerguntaRespostaForFirebase currentPergunta_Resposta = pergunta_respostaList.get(position);
        holder.pergunta.setText(currentPergunta_Resposta.getPergunta());
        if(currentPergunta_Resposta.getResposta()!=null && !currentPergunta_Resposta.getResposta().isEmpty())
            holder.resposta.setText(currentPergunta_Resposta.getResposta());
        else {
            holder.resposta.setVisibility(View.GONE);
            holder.ic_resposta.setVisibility(View.GONE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterHours = new SimpleDateFormat("HH:mm");
        String date = formatter.format(currentPergunta_Resposta.getTimestamp());
        String hours = formatterHours.format((currentPergunta_Resposta.getTimestamp()));
        holder.data.setText("" + date + " às " +hours);

    }

    @Override
    public int getItemCount() {
        return pergunta_respostaList.size();
    }

    public void setPerguntas_RespostasList(List<PerguntaRespostaForFirebase> pergunta_respostaList) {
        this.pergunta_respostaList = pergunta_respostaList;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{

        private TextView pergunta;
        private TextView resposta;
        private TextView data;
        private ImageView ic_pergunta;
        private ImageView ic_resposta;

        public Holder(@NonNull View itemView) {
            super(itemView);

            pergunta = itemView.findViewById(R.id.list_item_text_view_pergunta);
            resposta = itemView.findViewById(R.id.list_item_text_view_resposta);
            data = itemView.findViewById(R.id.text_view_tempo_pergunta);
            ic_pergunta = itemView.findViewById(R.id.imagge_view_list_item_pergunta);
            ic_resposta = itemView.findViewById(R.id.imagge_view_list_item_resposta);

        }
    }
}
