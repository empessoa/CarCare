package br.com.empessoa8.carcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.entidade.Correias_Tensor;

/**
 * Created by elias on 23/06/2017.
 */

public class AdapterCorreiaTensor extends ArrayAdapter<Correias_Tensor> {

    private int resource = 0;
    private LayoutInflater inflater;

    public AdapterCorreiaTensor(Context context, int resource) {
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
            viewHolder.txt_data_servico_correia_tensor = (TextView) view.findViewById(R.id.data_atual_correia_tensor);
            viewHolder.txt_valor_servico_correia_tensor = (TextView) view.findViewById(R.id.valor_servico_correia_tensor);
            viewHolder.txt_km_servico_correia_tensor= (TextView) view.findViewById(R.id.km_servico_correia_tensor);
            viewHolder.txt_data_proxima_revisao_correia_tensor = (TextView) view.findViewById(R.id.data_proxima_revisao_correia_tensor);
            viewHolder.txt_km_proxima_manutencao_correia_tensor= (TextView) view.findViewById(R.id.km_proxima_revisao_correia_tensor);
            viewHolder.txt_mensagem_servicos_realizados_correia_tensor = (TextView) view.findViewById(R.id.servicos_realizados_correia_tensor);

            view.setTag(viewHolder);//associando a view, armazenando objeto

            convertView = view;

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Correias_Tensor correias_tensor = getItem(position);

        String mensagem = "";
        mensagem +=  correias_tensor.getCorreia_dentada() + correias_tensor.getCorreia_poly_v()
                + correias_tensor.getTensor_correia() + correias_tensor.getBomba_de_agua();

        viewHolder.txt_data_servico_correia_tensor.setText(correias_tensor.getData_do_servico());
        viewHolder.txt_km_servico_correia_tensor.setText(Integer.toString(correias_tensor.getKm_do_servico()));
        viewHolder.txt_valor_servico_correia_tensor.setText(Float.toString(correias_tensor.getValor_do_servico()));
        viewHolder.txt_data_proxima_revisao_correia_tensor.setText(correias_tensor.getData_proxima_revisao());
        viewHolder.txt_km_proxima_manutencao_correia_tensor.setText((Integer.toString(correias_tensor.getKm_proxima_revisao())));
        viewHolder.txt_mensagem_servicos_realizados_correia_tensor.setText(mensagem);
        return view;
    }

    static class ViewHolder {

        TextView txt_data_servico_correia_tensor;
        TextView txt_data_proxima_revisao_correia_tensor;
        TextView txt_km_servico_correia_tensor;
        TextView txt_valor_servico_correia_tensor;
        TextView txt_km_proxima_manutencao_correia_tensor;
        TextView txt_mensagem_servicos_realizados_correia_tensor;

    }
}
