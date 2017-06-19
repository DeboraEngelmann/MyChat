package br.com.memorygame.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.memorygame.mychat.adapters.MensagemViewHolder;
import br.com.memorygame.mychat.models.Mensagem;

public class MensagemActivity extends AppCompatActivity {

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    EditText edtMensagem;

    private FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);
        Intent mIntent = getIntent();
        String uidConversa = mIntent.getStringExtra("uidConversa");

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference().child("conversas").child(uidConversa).child("mensagemArrayList");
        // [END create_database_reference]

        mRecycler = (RecyclerView) findViewById(R.id.mensagem_list);
        mRecycler.setHasFixedSize(true);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(false);
        mManager.setStackFromEnd(false);
        mRecycler.setLayoutManager(mManager);

        edtMensagem = (EditText)findViewById(R.id.mensagem);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = mDatabase;
        mAdapter = new FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder>(Mensagem.class, R.layout.item_message,
                MensagemViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final MensagemViewHolder viewHolder, final Mensagem model, final int position) {
                viewHolder.bindToMensagem(model);
            }
        };
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void addMensagem(View v){
        Mensagem mensagem = new Mensagem();
        mensagem.setNome(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        mensagem.setMensagem(edtMensagem.getText().toString());
        mensagem.setDataHora(new Date());
        mDatabase.push().setValue(mensagem.toMap());
        edtMensagem.setText("");

    }
}
