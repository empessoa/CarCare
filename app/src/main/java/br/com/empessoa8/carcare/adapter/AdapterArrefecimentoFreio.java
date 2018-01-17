package br.com.empessoa8.carcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.entidade.Arrefecimento_Freio;

/**
 * Created by elias on 23/06/2017.
 */

public class AdapterArrefecimentoFreio extends ArrayAdapter<Arrefecimento_Freio> {

    private int resource = 0;
    private LayoutInflater inflater;

    public AdapterArrefecimentoFreio(Context context, int resource) {
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
            viewHolder.txt_data_servico_arrefecimento_freio = (TextView) view.findViewById(R.id.data_arrefecimento_freio);
            viewHolder.txt_valor_servico_arrefecimento_freio = (TextView) view.findViewById(R.id.valor_servico_arrefecimento_freio);
            viewHolder.txt_km_servico_arrefecimento_freio = (TextView) view.findViewById(R.id.km_atual_arrefecimento_freio);
            viewHolder.txt_data_proxima_revisao_arrefecimento_freio = (TextView) view.findViewById(R.id.data_proxima_revisao_arrefecimento_freio);
            viewHolder.txt_km_proxima_manutencao_arrefecimento_freio = (TextView) view.findViewById(R.id.km_proxima_revisao_arrefecimento_freio);
            viewHolder.txt_mensagem_servicos_realizados_arrefecimento_freio = (TextView) view.findViewById(R.id.servicos_realizados_arrefecimento_freio);

            view.setTag(viewHolder);//associando a view, armazenando objeto

            convertView = view;

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Arrefecimento_Freio arrefecimento_freio = getItem(position);

        String mensagem = "";
        mensagem +=  arrefecimento_freio.getAgua_arrefecimento() + arrefecimento_freio.getOleo_freio() +
        arrefecimento_freio.getPastilhas_freios();

        viewHolder.txt_data_servico_arrefecimento_freio.setText(arrefecimento_freio.getData_do_servico());
        viewHolder.txt_km_servico_arrefecimento_freio.setText(Integer.toString(arrefecimento_freio.getKm_do_servico()));
        viewHolder.txt_valor_servico_arrefecimento_freio.setText(Float.toString(arrefecimento_freio.getValor_do_servico()));
        viewHolder.txt_data_proxima_revisao_arrefecimento_freio.setText(arrefecimento_freio.getData_proxima_revisao());
        viewHolder.txt_km_proxima_manutencao_arrefecimento_freio.setText(Integer.toString(arrefecimento_freio.getKm_proxima_revisao()));
        viewHolder.txt_mensagem_servicos_realizados_arrefecimento_freio.setText(mensagem);
        return view;
    }

    static class ViewHolder {

        TextView txt_data_servico_arrefecimento_freio;
        TextView txt_data_proxima_revisao_arrefecimento_freio;
        TextView txt_km_servico_arrefecimento_freio;
        TextView txt_valor_servico_arrefecimento_freio;
        TextView txt_km_proxima_manutencao_arrefecimento_freio;
        TextView txt_mensagem_servicos_realizados_arrefecimento_freio;

    }
}
