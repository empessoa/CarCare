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
 * Created by elias on 14/06/2017.
 */

public class AdapterAbastecerSimples extends ArrayAdapter<Abastecer> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public AdapterAbastecerSimples(Context context, int resource) {
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

            viewHolder.txt_data_abastecimento = (TextView) view.findViewById(R.id.dataAtualAbastecimentoSimples);
            viewHolder.txt_km_abastecimento = (TextView) view.findViewById(R.id.kmAtualAbastecimentoSimples);
            viewHolder.txt_litros_abastecido = (TextView) view.findViewById(R.id.litrosAbastecimentoSimples);

            view.setTag(viewHolder);//associando a view, armazenando objeto

            convertView = view;

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Abastecer abastecer = getItem(position);

        viewHolder.txt_data_abastecimento.setText(abastecer.getData_do_abastecimento());
        viewHolder.txt_km_abastecimento.setText(Integer.toString(abastecer.getKm_do_abastecimento()));
        viewHolder.txt_litros_abastecido.setText(Float.toString(abastecer.getTotal_de_litro()));

        return view;
    }

    static class ViewHolder {
        TextView txt_data_abastecimento;
        TextView txt_km_abastecimento;
        TextView txt_litros_abastecido;
    }
}
