package br.com.memorygame.mychat.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.adapters.AdapterConversa;
import br.com.memorygame.mychat.models.Contato;
import br.com.memorygame.mychat.models.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private AdapterConversa mAdapter;
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
        if (FirebaseAuth.getInstance()!=null){
            if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
                meuEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new AdapterConversa(getActivity());
        mDatabaseReference.child("conversas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Conversa conversa = dataSnapshot.getValue(Conversa.class);
                conversa.setUid(dataSnapshot.getKey());
                boolean flag=false;
                for (Contato contato: conversa.getContatoArrayList()) {
                    if (contato.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        flag=true;
                    }
                }
                if (flag) {
                    mAdapter.addConversa(conversa);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
