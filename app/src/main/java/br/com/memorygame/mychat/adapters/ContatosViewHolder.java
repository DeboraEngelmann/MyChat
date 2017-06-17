package br.com.memorygame.mychat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.models.Contato;
import br.com.memorygame.mychat.models.Conversa;

/**
 * Created by debo_ on 16/06/2017.
 */

public class ContatosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    String uid;
    final TextView nome;
    final TextView email;
    final TextView iniciais;
    final ImageView foto;
    DatabaseReference mDatabaseReference;
    Context contexto;

    public ContatosViewHolder(View itemView) {
        super(itemView);
        this.nome = (TextView) itemView.findViewById(R.id.txtNome);
        this.email = (TextView) itemView.findViewById(R.id.txtEmail);
        this.iniciais = (TextView) itemView.findViewById(R.id.txtIniciais);
        this.foto = (ImageView) itemView.findViewById(R.id.imgFoto);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        criarConversa();
    }

    private void criarConversa(){

        mDatabaseReference=FirebaseDatabase.getInstance().getReference();
        String key = mDatabaseReference.push().getKey();
        String uid = "";
        Conversa conversa = new Conversa();
        List<Contato> contatoArrayList = new ArrayList<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            Contato meuContato = new Contato();
            meuContato.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            meuContato.setNome(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            meuContato.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            meuContato.setUrlFoto(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
            contatoArrayList.add(meuContato);
        }
        Contato contato = new Contato();
        contato.setUid(uid);
        contato.setNome(nome.getText().toString());
        contato.setEmail(email.getText().toString());
        contatoArrayList.add(contato);
        conversa.setContatoArrayList(contatoArrayList);
        Map<String,Object>mapValues=conversa.toMap();

        Map<String,Object>childUpdates=new HashMap<>();
        childUpdates.put("/conversas/"+key,mapValues);
        mDatabaseReference.updateChildren(childUpdates);
        Toast.makeText(contexto, "deu certo", Toast.LENGTH_SHORT).show();
    }
}
