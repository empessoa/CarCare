package br.com.empessoa8.carcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.entidade.Abastecer;

/**
 * Created by elias on 13/06/2017.
 */

public class AdapterAbastecer extends ArrayAdapter<Abastecer> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public AdapterAbastecer(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);//criando a linha a ser preenchida no ListView

            viewHolder.txt_data_abastecimento = (TextView) view.findViewById(R.id.dataAtualAbastecimento);
            viewHolder.txt_km_abastecimento = (TextView) view.findViewById(R.id.kmAtualAbastecimento);
            viewHolder.txt_valor_abastecido = (TextView) view.findViewById(R.id.valorTotalAbastecido);
            viewHolder.txt_valor_litro = (TextView) view.findViewById(R.id.valorDoLitroAbastecido);
            viewHolder.txt_litros_abastecido = (TextView) view.findViewById(R.id.totalLitrosAbastecido);
            viewHolder.txt_tipo_combustivel = (TextView) view.findViewById(R.id.combustivelTipoAbastecido);


            view.setTag(viewHolder);//associando a view, armazenando objeto

            convertView = view;

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Abastecer abastecer = getItem(position);



        viewHolder.txt_data_abastecimento.setText(abastecer.getData_do_abastecimento());
        viewHolder.txt_km_abastecimento.setText(Integer.toString(abastecer.getKm_do_abastecimento()));
        viewHolder.txt_valor_abastecido.setText(Float.toString(abastecer.getValor_total_abastecido()));
        viewHolder.txt_valor_litro.setText(Float.toString(abastecer.getValor_do_litro()));
        viewHolder.txt_litros_abastecido.setText(Float.toString(abastecer.getTotal_de_litro()));
        viewHolder.txt_tipo_combustivel.setText(abastecer.getCombustivel_tipo());

        return view;
    }

    static class ViewHolder {

        TextView txt_data_abastecimento;
        TextView txt_km_abastecimento;
        TextView txt_valor_litro;
        TextView txt_valor_abastecido;
        TextView txt_litros_abastecido;
        TextView txt_tipo_combustivel;

    }
}
