package br.com.empessoa8.carcare.fragments.ignicao;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.fragments.abastecer.TabAdapterAbastecer;
import br.com.empessoa8.carcare.helper.SlidingTabLayout;

public class Act_Ignicao extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__ignicao);

        //montando as abas
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs_ignicao);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina_ignicao);
        //configurar aliding tabs
        slidingTabLayout.setDistributeEvenly(true);//distribui as tabs no espaço do layout
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_sliding));//configurando a cor
        //configurando o adapter Act_Servicos
        TabAdapterIgnicao tabAdapterIgnicao = new TabAdapterIgnicao(getSupportFragmentManager());//gerencia os fragmentos
        viewPager.setAdapter(tabAdapterIgnicao);
        slidingTabLayout.setViewPager(viewPager);
    }
}
