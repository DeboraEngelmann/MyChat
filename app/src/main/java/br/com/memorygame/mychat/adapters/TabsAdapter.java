package br.com.memorygame.mychat.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.memorygame.mychat.fragmentos.ContatosFragment;
import br.com.memorygame.mychat.fragmentos.ConversasFragment;

import static br.com.memorygame.mychat.fragmentos.TabFragment.int_items;

//Gerencia as tabs na mainActivity
public class TabsAdapter extends FragmentPagerAdapter {

    public TabsAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new ConversasFragment();
            case 1:
                return new ContatosFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return int_items;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Conversas";
            case 1:
                return "Contatos";
        }
        return null;
    }
}
