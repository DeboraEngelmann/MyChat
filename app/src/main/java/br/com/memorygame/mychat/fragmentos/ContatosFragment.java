package br.com.memorygame.mychat.fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.memorygame.mychat.MainActivity;
import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.adapters.ContatosAdapter;
import br.com.memorygame.mychat.models.Contato;

public class ContatosFragment extends Fragment {
    RecyclerView recyclerView;

    public ContatosFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.reciclerViewContatos);
        ContatosAdapter contatosAdapter = new ContatosAdapter(MainActivity.getContatosArrayList(),this.getContext());
        recyclerView.setAdapter(contatosAdapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
        return view;
    }


}
