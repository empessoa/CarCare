package br.com.empessoa8.carcare.fragments.oleos_filtros;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.data_base.DataBase;
import br.com.empessoa8.carcare.entidade.Oleo_Filtros;
import br.com.empessoa8.carcare.mask.Mask;
import br.com.empessoa8.carcare.mask.MonetaryMask;
import br.com.empessoa8.carcare.persistencia.Per_Oleo_Filtro;
import br.com.empessoa8.carcare.persistencia.Pers_Abastecer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Oleo_Filtro extends Fragment implements View.OnClickListener {

    View view;
    AlertDialog.Builder mensagem;
    private DataBase dataBase;
    private SQLiteDatabase connBanco;
    private Oleo_Filtros oleo_filtros;
    private Per_Oleo_Filtro perOleoFreio;
    private Button btn_salvar_servicos;
    private EditText edt_data_do_servico;
    private EditText edt_km_do_servico;
    private EditText edt_valor_servico;
    private CheckBox cbx_oleo_do_motor;
    private CheckBox cbx_oleo_da_direcao;
    private CheckBox cbx_filtro_do_oleo;
    private CheckBox cbx_filtro_do_combustivel;
    private CheckBox cbx_filtro_do_ar;
    private CheckBox cbx_filtro_do_ar_condicionado;
    private EditText edt_lubrificante_tipo;
    private EditText edt_data_proxima_revisao;
    private Spinner spn_oleo_km;
    private ArrayAdapter<String> adp_spn_km_oleo;
    private Date dataSistema;
    String dataFormatadaOleo;
    String dataProximaRevisaoOleo;
    private Pers_Abastecer pers_abastecer;
    private Integer km_abastecimento;


    public Frag_Oleo_Filtro() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__oleo__filtro, container, false);
        btn_salvar_servicos = (Button) view.findViewById(R.id.btn_salvar_oleo_freio);
        btn_salvar_servicos.setOnClickListener(this);

        cbx_oleo_do_motor = (CheckBox) view.findViewById(R.id.cbx_oleo_motor);
        cbx_oleo_da_direcao = (CheckBox) view.findViewById(R.id.cbx_oleo_direcao);
        cbx_filtro_do_oleo = (CheckBox) view.findViewById(R.id.cbx_filtro_oleo);
        cbx_filtro_do_combustivel = (CheckBox) view.findViewById(R.id.cbx_filtro_combustivel);
        cbx_filtro_do_ar = (CheckBox) view.findViewById(R.id.cbx_filtro_ar);
        cbx_filtro_do_ar_condicionado = (CheckBox) view.findViewById(R.id.cbx_filtro_ar_condicionado);

        edt_lubrificante_tipo = (EditText) view.findViewById(R.id.edt_lubrificante_tipo);
        edt_lubrificante_tipo.addTextChangedListener(Mask.insert(Mask.MASK_TEXTO, edt_lubrificante_tipo));

        edt_km_do_servico = (EditText) view.findViewById(R.id.edt_km_servico);
        edt_km_do_servico.addTextChangedListener(Mask.insert(Mask.MASK_KM, edt_km_do_servico));
        edt_km_do_servico.requestFocus();

        edt_valor_servico = (EditText) view.findViewById(R.id.edt_valor_servico);
        edt_valor_servico.addTextChangedListener(new MonetaryMask(edt_valor_servico) {
        });
//        edt_valor_servico.addTextChangedListener(Mask.insert(Mask.MASK_VALOR, edt_valor_servico));

        edt_data_do_servico = (EditText) view.findViewById(R.id.edt_data_servico);
        edt_data_do_servico.addTextChangedListener(Mask.insert(Mask.MASK_DATA_SERVICO, edt_data_do_servico));

        edt_data_proxima_revisao = (EditText) view.findViewById(R.id.edt_next_revisao_oleo_e_filtro);
        ExibirDataListener dataListener = new ExibirDataListener();
        edt_data_proxima_revisao.setOnClickListener(dataListener);

        spn_oleo_km = (Spinner) view.findViewById(R.id.spn_oleo_freio);

        oleo_filtros = new Oleo_Filtros();
        conectarBanco();
        spnKmOleo();
        recuperarSharedPreferences();
        verificarDataAtual();
//        verificarDataProximaRevisao();
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_salvar_oleo_freio:
                salvarServicoOleoFiltro();
                break;
        }

    }

    private void salvarServicoOleoFiltro() {
        try {
            perOleoFreio = new Per_Oleo_Filtro(connBanco);
            if ((!edt_data_do_servico.getText().toString().equals("")) &&
                    (!edt_km_do_servico.getText().toString().equals("")) &&
                    (!edt_valor_servico.getText().toString().equals("")) &&
                    (!edt_data_proxima_revisao.getText().toString().equals("")) &&
                    (!edt_lubrificante_tipo.getText().toString().equals(""))) {
                if (cbx_oleo_do_motor.isChecked() ||
                        (cbx_oleo_da_direcao.isChecked()) ||
                        (cbx_filtro_do_oleo.isChecked()) ||
                        (cbx_filtro_do_combustivel.isChecked()) ||
                        (cbx_filtro_do_ar.isChecked()) ||
                        (cbx_filtro_do_ar_condicionado.isChecked())) {
                    if (cbx_oleo_do_motor.isChecked()) {
                        oleo_filtros.setOleo_do_motor(cbx_oleo_do_motor.getText().toString());
                    }
                    if (cbx_oleo_da_direcao.isChecked()) {
                        oleo_filtros.setOleo_da_direcao(cbx_oleo_da_direcao.getText().toString());
                    }
                    if (cbx_filtro_do_oleo.isChecked()) {
                        oleo_filtros.setFiltro_do_oleo(cbx_filtro_do_oleo.getText().toString());
                    }
                    if (cbx_filtro_do_combustivel.isChecked()) {
                        oleo_filtros.setFiltro_do_combustivel(cbx_filtro_do_combustivel.getText().toString());
                    }
                    if (cbx_filtro_do_ar.isChecked()) {
                        oleo_filtros.setFiltro_do_ar(cbx_filtro_do_ar.getText().toString());
                    }
                    if (cbx_filtro_do_ar_condicionado.isChecked()) {
                        oleo_filtros.setFiltro_do_ar_condicionado(cbx_filtro_do_ar_condicionado.getText().toString());
                    }

                    oleo_filtros.setKm_do_servico(Integer.parseInt(edt_km_do_servico.getText().toString()));

                    String valorDoServico_1, valorDoServico_2;
                    valorDoServico_1 = edt_valor_servico.getText().toString().replaceAll("[R$.]", "");
                    valorDoServico_2 = valorDoServico_1.replaceAll("[,]", ".");
                    float valorDoServico = Float.parseFloat(valorDoServico_2);
                    oleo_filtros.setValor_do_servico(valorDoServico);

                    int kmAtual = Integer.parseInt(edt_km_do_servico.getText().toString());
                    String kmRevisao = String.valueOf(spn_oleo_km.getSelectedItem());
                    int kmProximaRevisao = kmAtual + Integer.parseInt(kmRevisao);
                    oleo_filtros.setKm_proxima_revisao((kmProximaRevisao));
                    perOleoFreio.inserirServicoOleoFiltro(oleo_filtros);
                    Toast.makeText(getActivity(), "Serviço Salvo com sucesso!!", Toast.LENGTH_SHORT).show();
                    enviarDialogoSair();
                } else {
                    Toast.makeText(getActivity(), "Item sem checar!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Campos  em brancos!!", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Salvar Serviços!");
            mensagem.setMessage("Erro método salvarServico() :" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            mensagem.show();
        }
    }

    private void enviarDialogoSair() {
        mensagem = new AlertDialog.Builder(getActivity());
        mensagem.setTitle("SERVIÇOS SALVO");
        mensagem.setMessage("ENCERRAR SERVIÇOS \n\n Confirmar:");
        mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                salvarSharedPreferences();
                //System.exit(0);
                getActivity().finish();
            }
        });
        mensagem.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                salvarSharedPreferences();
                getActivity().finish();
                startActivityForResult(new Intent(getActivity(), getActivity().getClass()), 0);
            }
        });
        mensagem.create();
        mensagem.show();
    }

    private void conectarBanco() {
        try {
            dataBase = new DataBase(getActivity());
            connBanco = dataBase.getReadableDatabase();
            perOleoFreio = new Per_Oleo_Filtro(connBanco);
            dataProximaRevisaoOleo = perOleoFreio.exibirDataProximarevisaoOleoFiltro();

            pers_abastecer = new Pers_Abastecer(connBanco);
            km_abastecimento = pers_abastecer.exibirKmAbastecido();

            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Conectado com sucesso Serviços!");
            mensagem.setNeutralButton("Sair", null);
            //mensagem.show();


        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Erro de conexão:" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            //mensagem.show();
        }
    }

    private void spnKmOleo() {
        adp_spn_km_oleo = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item
        );
        adp_spn_km_oleo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp_spn_km_oleo.add("10000");
        adp_spn_km_oleo.add("5000");
        adp_spn_km_oleo.add("3000");
        spn_oleo_km.setAdapter(adp_spn_km_oleo);
    }

    private void verificarDataAtual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(ano, mes, dia);
            dataSistema = calendar.getTime();
            dataFormatadaOleo = dateFormat.format(dataSistema);
            edt_data_do_servico.setText(dataFormatadaOleo);
            oleo_filtros.setData_do_servico(dataFormatadaOleo);

        } catch (Exception e) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Erro " + e);
            mensagem.setNeutralButton("Sair", null);
            mensagem.create();
            mensagem.show();
        }
    }

    private void verificarDataProximaRevisao() {

        if (dataFormatadaOleo.equals(dataProximaRevisaoOleo)) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("PRÓXIMA REVISÃO!");
            mensagem.setMessage("Data Proxima Revisão: " + dataProximaRevisaoOleo);
            mensagem.setNeutralButton("Sair", null);
            mensagem.create();
            mensagem.show();
        }
    }

    private void salvarSharedPreferences() {

        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("lubrificanteTipo", edt_lubrificante_tipo.getText().toString());//salvando kmAtualServico
        edt.putString("kmServico", edt_km_do_servico.getText().toString());
        edt.putString("valorServico", edt_valor_servico.getText().toString());//salvando kmAtualServico
        edt.commit();
        //Toast.makeText(getActivity(), "Km Incial salvo!", Toast.LENGTH_SHORT).show();

    }

    private void recuperarSharedPreferences() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if ((pref.contains("lubrificanteTipo")) && (pref.contains("kmServico")) && (pref.contains("valorServico"))) {
            edt_lubrificante_tipo.setText(pref.getString("lubrificanteTipo", edt_lubrificante_tipo.getText().toString()));
            edt_km_do_servico.setText(pref.getString("kmServico", edt_km_do_servico.getText().toString()));
            edt_valor_servico.setText(pref.getString("valorServico", edt_valor_servico.getText().toString()));
        } else {
            //Toast.makeText(getActivity(), "Km Incial não definido!", Toast.LENGTH_SHORT).show();
        }
    }

    //metodo destinado a exibir data do sistema
    private void exibirDataSistema() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                new SelecionarDataListener(), ano, mes, dia);
        dialog.show();
    }

    private class ExibirDataListener implements View.OnClickListener, View.OnFocusChangeListener {
        @Override
        public void onClick(View v) {
            exibirDataSistema();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            exibirDataSistema();
        }

    }

    private   class SelecionarDataListener extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            dataSistema = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateNextFix = dateFormat.format(dataSistema);
            edt_data_proxima_revisao.setText(dateNextFix);
            oleo_filtros.setData_proxima_revisao(dateNextFix);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        edt_km_do_servico.setText(Integer.toString(km_abastecimento));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connBanco != null) {
            connBanco.close();
        }
        salvarSharedPreferences();
    }
}
