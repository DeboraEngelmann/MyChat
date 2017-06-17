package br.com.memorygame.mychat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.models.Conversa;

/**
 * Created by debo_ on 16/06/2017.
 */

public class ConversasViewHolder extends RecyclerView.ViewHolder {
    final TextView iniciais;
    final TextView tituloConversa;
    final TextView ultimaMsg;
    final ImageView foto;
    private Context context;

    public ConversasViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        iniciais = (TextView) itemView.findViewById(R.id.txtIniciais);
        tituloConversa = (TextView) itemView.findViewById(R.id.txtTitulo);
        ultimaMsg = (TextView) itemView.findViewById(R.id.txtMsg);
        foto = (ImageView) itemView.findViewById(R.id.imgFoto);
    }

    public void bindToConversa(Conversa conversa, boolean flag) {
        if (flag) {
            tituloConversa.setText(conversa.getContatoArrayList().get(1).getNome());
            iniciais.setText("" + tituloConversa.getText().charAt(0));
        }else{
            itemView.setVisibility(View.GONE);
        }
    }
}

