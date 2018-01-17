package br.com.empessoa8.carcare.fragments.oleos_filtros;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by elias on 21/06/2017.
 */

public class TabAdapterOleoFiltro extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"Serviços", "Realizados"};

    public TabAdapterOleoFiltro(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Frag_Oleo_Filtro();
                break;
            case 1:
                fragment = new Frag_Oleo_Filtro_Realizados();
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
