package br.com.memorygame.mychat.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.adapters.ConversasViewHolder;
import br.com.memorygame.mychat.models.Contato;
import br.com.memorygame.mychat.models.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Conversa,ConversasViewHolder> mAdapter;
    private DatabaseReference mDatabaseReference;
    private String meuEmail;

    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.reciclerViewConversas);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        meuEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        Query query = mDatabaseReference.child("conversas");
        mAdapter = new FirebaseRecyclerAdapter<Conversa, ConversasViewHolder>(Conversa.class,R.layout.item_conversa,ConversasViewHolder.class,query) {
            @Override
            protected void populateViewHolder(ConversasViewHolder viewHolder, Conversa model, int position) {
                boolean flag = false;
                for (Contato item:model.getContatoArrayList()) {
                    if (item.getEmail().equalsIgnoreCase(meuEmail)){
                        flag=true;
                    }
                }
                viewHolder.bindToConversa(model,flag);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }
}
