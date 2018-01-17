package br.com.empessoa8.carcare.fragments.arrefecimento_freio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by elias on 23/06/2017.
 */

public class TabAdapterArrefecimentoFreio extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"Serviço", "Realizados"};

    public TabAdapterArrefecimentoFreio(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Frag_Arrefecimento_Freio();
                break;
            case 1:
                fragment = new Frag_Arrefecimento_Freio_Realizados();
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
