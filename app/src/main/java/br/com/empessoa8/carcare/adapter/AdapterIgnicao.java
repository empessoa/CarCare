package br.com.empessoa8.carcare.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.entidade.Ignicao;

/**
 * Created by elias on 14/06/2017.
 */

public class AdapterIgnicao extends ArrayAdapter<Ignicao> {
    private int resource = 0;
    private LayoutInflater inflater;

    public AdapterIgnicao(Context context, int resource) {
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
            viewHolder.txt_data_servico_troca_ignicao = (TextView) view.findViewById(R.id.data_atual_ignicao);
            viewHolder.txt_valor_servico_troca_ignicao = (TextView) view.findViewById(R.id.valor_servico_ignicao);
            viewHolder.txt_km_servico_troca_ignicao = (TextView) view.findViewById(R.id.km_atual_ignicao);
            viewHolder.txt_data_proxima_revisao = (TextView) view.findViewById(R.id.data_proxima_revisao_ignicao);
            viewHolder.txt_km_proxima_manutencao = (TextView) view.findViewById(R.id.km_proxima_revisao_ignicao);
            viewHolder.txt_mensagem_servicos_realizados_ignicao = (TextView) view.findViewById(R.id.servicos_realizados);

            view.setTag(viewHolder);//associando a view, armazenando objeto

            convertView = view;

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Ignicao servico_ignicao = getItem(position);

        String mensagem = "";
        mensagem += servico_ignicao.getVelas_ignicao();

        viewHolder.txt_data_servico_troca_ignicao.setText(servico_ignicao.getData_do_servico());
        viewHolder.txt_km_servico_troca_ignicao.setText(Integer.toString(servico_ignicao.getKm_do_servico()));
        viewHolder.txt_valor_servico_troca_ignicao.setText(Float.toString(servico_ignicao.getValor_do_servico()));
        viewHolder.txt_data_proxima_revisao.setText(servico_ignicao.getData_proxima_revisao());
        viewHolder.txt_km_proxima_manutencao.setText(Integer.toString(servico_ignicao.getKm_proxima_revisao()));
        viewHolder.txt_mensagem_servicos_realizados_ignicao.setText(mensagem);
        return view;
    }

    static class ViewHolder {

        TextView txt_data_servico_troca_ignicao;
        TextView txt_data_proxima_revisao;
        TextView txt_km_servico_troca_ignicao;
        TextView txt_valor_servico_troca_ignicao;
        TextView txt_mensagem_servicos_realizados_ignicao;
        TextView txt_km_proxima_manutencao;

    }
}
