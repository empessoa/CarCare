package br.com.empessoa8.carcare.fragments.abastecer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by elias on 13/06/2017.
 */

public class TabAdapterAbastecer extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"Abastecer", "Realizados"};

    public TabAdapterAbastecer(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Frag_Abastecer();
                break;
            case 1:
                fragment = new Frag_Abastecimento();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;//retornando o tamanho das abas
    }
    //recupera o titulo das abas
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}