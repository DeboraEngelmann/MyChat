package br.com.memorygame.mychat.fragmentos;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.memorygame.mychat.R;
import br.com.memorygame.mychat.adapters.TabsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    public  static TabLayout mTabLayout;
    public  static ViewPager viewPager;
    public  static int int_items= 2;

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab,null);

        mTabLayout=(TabLayout)v.findViewById(R.id.tabs);

        viewPager=(ViewPager)v.findViewById(R.id.viewpager);
        //set an adpater;
        viewPager.setAdapter(new TabsAdapter( getChildFragmentManager()));
        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //Pra trocar a pagina da tab é necessário executar uma thread para não dar a sensação de travamento para o usuario
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setupWithViewPager(viewPager);
            }
        });
        return v;
    }
}
