package br.com.memorygame.mychat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.models.Conversa;

/**
 * Created by debo_ on 18/06/2017.
 *
 */

public class AdapterConversa extends RecyclerView.Adapter{
    Context context;
    List<Conversa> conversaList = new ArrayList<>();
    //Recebe o contexto
    public AdapterConversa(Context context) {
        this.context = context;
    }
    //Responsável por adicionar uma conversa ao array
    public void addConversa(Conversa conversa) {
        conversaList.add(conversa);
        notifyItemInserted(conversaList.size());
    }

    @Override //pega o layout que vai ser inflado
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversa, parent, false);
        ConversasViewHolder holder = new ConversasViewHolder(view);
        return holder;
    }

    @Override //coloca os dados dentro do layout
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConversasViewHolder viewHolder = (ConversasViewHolder) holder;
        viewHolder.bindToConversa(conversaList.get(position));
    }

    @Override  //pega o tamanho do array
    public int getItemCount() {
        return conversaList.size();
    }

}
