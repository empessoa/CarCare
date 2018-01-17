package br.com.empessoa8.carcare.fragments.correia_tensor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by elias on 26/07/2017.
 */

public class TabAdapterCorreiasTensor extends FragmentStatePagerAdapter {
    private String[] tituloAbas = {"Serviço", "Realizados"};
    public TabAdapterCorreiasTensor(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Frag_Correias_Tensor();
                break;
            case 1:
                fragment = new Frag_Correias_Tensor_Realizados();
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
