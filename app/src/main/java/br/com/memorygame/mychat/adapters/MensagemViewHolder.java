package br.com.memorygame.mychat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.memorygame.mychat.MainActivity;
import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.models.Mensagem;

/**
 * Created by debo_ on 18/06/2017.
 */

public class MensagemViewHolder extends RecyclerView.ViewHolder {
    TextView autorLeft;
    TextView autorRight;
    TextView mensagemLeft;
    TextView mensagemRight;
    TextView timeLeft;
    TextView timeRight;
    RelativeLayout containerLeft;
    RelativeLayout containerRight;

    //recupera os dados do layout
    public MensagemViewHolder(View itemView) {
        super(itemView);
        autorLeft = (TextView) itemView.findViewById(R.id.autor_left);
        autorRight = (TextView) itemView.findViewById(R.id.autor_right);
        mensagemLeft=(TextView) itemView.findViewById(R.id.mensagem_left);
        mensagemRight=(TextView) itemView.findViewById(R.id.mensagem_right);
        timeLeft=(TextView) itemView.findViewById(R.id.time_left);
        timeRight=(TextView) itemView.findViewById(R.id.time_right);
        containerLeft=(RelativeLayout) itemView.findViewById(R.id.container_left);
        containerRight=(RelativeLayout) itemView.findViewById(R.id.container_right);
    }

    //método responsável por colocar os dados na view
    public void bindToMensagem(Mensagem model) {
        if (model.getNome().equalsIgnoreCase(MainActivity.usuario.getNome())){
            containerRight.setVisibility(View.VISIBLE);
            containerLeft.setVisibility(View.GONE);
            autorRight.setText(model.getNome());
            timeRight.setText(model.getData_hora());
            mensagemRight.setText(model.getMensagem());
        }else {
            containerLeft.setVisibility(View.VISIBLE);
            containerRight.setVisibility(View.GONE);
            autorLeft.setText(model.getNome());
            timeLeft.setText(model.getData_hora());
            mensagemLeft.setText(model.getMensagem());
        }
    }
}
