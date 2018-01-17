package br.com.empessoa8.carcare.fragments.oleos_filtros;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.helper.SlidingTabLayout;

public class Act_Oleo_Filtro extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__oleo__filtro);

        //montando as abas
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs_oleo_filtro);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina_oleo_filtro);
        //configurar aliding tabs
        slidingTabLayout.setDistributeEvenly(true);//distribui as tabs no espaço do layout
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_sliding));//configurando a cor
        //configurando o adapter Act_Servicos
        TabAdapterOleoFiltro tabAdapterOleoFiltro = new TabAdapterOleoFiltro(getSupportFragmentManager());//gerencia os fragmentos
        viewPager.setAdapter(tabAdapterOleoFiltro);
        slidingTabLayout.setViewPager(viewPager);
    }
}
