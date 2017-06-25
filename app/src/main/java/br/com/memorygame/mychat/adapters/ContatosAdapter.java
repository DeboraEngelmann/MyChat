package br.com.memorygame.mychat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.bumptech.glide.Glide;

import java.util.ArrayList;

import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.models.Contato;
//import br.com.memorygame.mychat.utilitarios.CircleTransform;

/**
 * Created by debo_ on 16/06/2017.
 */

public class ContatosAdapter extends RecyclerView.Adapter{
    private ArrayList<Contato> contatoArrayList;
    private Context contexto;
    //construtor da classe
    public ContatosAdapter(ArrayList contatos, Context contexto) {
        contatoArrayList=contatos;
        this.contexto=contexto;
    }

    @Override  //pega o layout que vai ser inflado
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.item_contato, parent, false);
        ContatosViewHolder holder = new ContatosViewHolder(view);
        return holder;
    }

    @Override //Responsável por passar os dados de cada contato para cada viewHolder visivel e vai colocando eles na tela
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContatosViewHolder viewHolder = (ContatosViewHolder) holder;
        Contato contato = contatoArrayList.get(position);
        viewHolder.contexto=this.contexto;
        viewHolder.uid=contato.getUid();
        viewHolder.nome.setText(contato.getNome());
        viewHolder.email.setText(contato.getEmail());
        //Parte que será necessária quando formos utilizar fotos nos perfis
//        if (contato.getUrlFoto()!=null) {
//            if (!contato.getUrlFoto().toString().isEmpty()) {
//                Glide.with(contexto).load(contato.getUrlFoto()).transform(new CircleTransform(contexto)).into(viewHolder.foto);
//            } else {
//                viewHolder.iniciais.setText("" + contato.getNome().charAt(0));
//            }
//        }else {
            viewHolder.iniciais.setText("" + contato.getNome().charAt(0));
//        }
    }

    @Override //pega o tamanho do array
    public int getItemCount() {
        return contatoArrayList.size();
    }
}
