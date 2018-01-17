package br.com.empessoa8.carcare.fragments.ignicao;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.empessoa8.carcare.fragments.abastecer.Frag_Abastecer;
import br.com.empessoa8.carcare.fragments.abastecer.Frag_Abastecimento;

/**
 * Created by elias on 14/06/2017.
 */

public class TabAdapterIgnicao extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"Serviço", "Realizados"};

    public TabAdapterIgnicao(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Frag_Ignicao();
                break;
            case 1:
                fragment = new Frag_Ignicao_Realizado();
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
