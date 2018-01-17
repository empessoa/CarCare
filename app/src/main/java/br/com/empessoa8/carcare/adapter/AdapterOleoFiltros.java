package br.com.empessoa8.carcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.entidade.Oleo_Filtros;

/**
 * Created by elias on 21/06/2017.
 */

public class AdapterOleoFiltros extends ArrayAdapter<Oleo_Filtros> {
    private int resource = 0;
    private LayoutInflater inflater;

    public AdapterOleoFiltros(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);//criando a linha a ser preenchida no ListView
            viewHolder.txt_data_servico_oleo_filtro = (TextView) view.findViewById(R.id.data_atual_oleo_filtro);
            viewHolder.txt_valor_servico_oleo_filtro = (TextView) view.findViewById(R.id.valor_servico_oleo_filtro);
            viewHolder.txt_km_servico_oleo_filtro = (TextView) view.findViewById(R.id.km_atual_oleo_filtro);
            viewHolder.txt_data_proxima_revisao_oleo_filtro = (TextView) view.findViewById(R.id.data_proxima_revisao_oleo_filtro);
            viewHolder.txt_km_proxima_manutencao_oleo_filtro = (TextView) view.findViewById(R.id.km_proxima_revisao_oleo_filtro);
            viewHolder.txt_mensagem_servicos_realizados_oleo_filtro = (TextView) view.findViewById(R.id.servicos_realizados_oleo_filtro);

            view.setTag(viewHolder);//associando a view, armazenando objeto

            convertView = view;

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Oleo_Filtros oleo_filtros = getItem(position);

        String mensagem = "";
        mensagem += oleo_filtros.getOleo_do_motor() + oleo_filtros.getOleo_da_direcao()+
        oleo_filtros.getFiltro_do_oleo() + oleo_filtros.getFiltro_do_combustivel() +
        oleo_filtros.getFiltro_do_ar() + oleo_filtros.getFiltro_do_ar_condicionado();

        viewHolder.txt_data_servico_oleo_filtro.setText(oleo_filtros.getData_do_servico());
        viewHolder.txt_km_servico_oleo_filtro.setText(Integer.toString(oleo_filtros.getKm_do_servico()));
        viewHolder.txt_valor_servico_oleo_filtro.setText(Float.toString(oleo_filtros.getValor_do_servico()));
        viewHolder.txt_data_proxima_revisao_oleo_filtro.setText(oleo_filtros.getData_proxima_revisao());
        viewHolder.txt_km_proxima_manutencao_oleo_filtro.setText((Integer.toString(oleo_filtros.getKm_proxima_revisao())));
        viewHolder.txt_mensagem_servicos_realizados_oleo_filtro.setText(mensagem);
        return view;
    }

    static class ViewHolder {

        TextView txt_data_servico_oleo_filtro;
        TextView txt_data_proxima_revisao_oleo_filtro;
        TextView txt_km_servico_oleo_filtro;
        TextView txt_valor_servico_oleo_filtro;
        TextView txt_km_proxima_manutencao_oleo_filtro;
        TextView txt_mensagem_servicos_realizados_oleo_filtro;

    }
}
