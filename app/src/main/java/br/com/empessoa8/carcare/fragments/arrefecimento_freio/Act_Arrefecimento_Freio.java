package br.com.empessoa8.carcare.fragments.arrefecimento_freio;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.fragments.ignicao.TabAdapterIgnicao;
import br.com.empessoa8.carcare.helper.SlidingTabLayout;

public class Act_Arrefecimento_Freio extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__arrefecimento__freio);
        //montando as abas
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs_arrefecimento_freio);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina_arrefecimento_freio);
        //configurar aliding tabs
        slidingTabLayout.setDistributeEvenly(true);//distribui as tabs no espaço do layout
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_sliding));//configurando a cor
        //configurando o adapter Act_Servicos
        TabAdapterArrefecimentoFreio tabAdapterArrefecimentoFreio = new TabAdapterArrefecimentoFreio(getSupportFragmentManager());//gerencia os fragmentos
        viewPager.setAdapter(tabAdapterArrefecimentoFreio);
        slidingTabLayout.setViewPager(viewPager);
    }
}
