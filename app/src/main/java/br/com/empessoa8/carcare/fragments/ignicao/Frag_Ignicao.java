package br.com.empessoa8.carcare.fragments.ignicao;


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
import br.com.empessoa8.carcare.entidade.Ignicao;
import br.com.empessoa8.carcare.mask.Mask;
import br.com.empessoa8.carcare.mask.MonetaryMask;
import br.com.empessoa8.carcare.persistencia.Pers_Abastecer;
import br.com.empessoa8.carcare.persistencia.Pers_Ignicao;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Ignicao extends Fragment implements View.OnClickListener {

    View view;
    AlertDialog.Builder mensagem;

    private DataBase carCare_dataBase;
    private SQLiteDatabase connBanco;
    private Ignicao servico_ignicao;
    private Pers_Ignicao pers_ignicao;

    private EditText edt_data_do_servico;
    private EditText edt_data_proxima_revisao;
    private EditText edt_km_do_servico;
    private EditText edt_valor_servico;
    private CheckBox cbx_ignicao;
    private Button btn_salvar_servicos;

    private Spinner spn_ignicao_km;
    private ArrayAdapter<String> adp_spn_km_ignicao;

    private Date dataSistema;
    String dataFormatadaAtual;
    String dataProximaRevisao;

    private Pers_Abastecer pers_abastecer;
    private Integer km_abastecimento;
    private int km_revisao;

    public Frag_Ignicao() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__ignicao, container, false);

        btn_salvar_servicos = (Button) view.findViewById(R.id.btn_salvar_servicos);
        btn_salvar_servicos.setOnClickListener(this);

        cbx_ignicao = (CheckBox) view.findViewById(R.id.cbx_ignicao);

        edt_data_do_servico = (EditText) view.findViewById(R.id.edt_data_servico);
        edt_data_do_servico.addTextChangedListener(Mask.insert(Mask.MASK_DATA_SERVICO, edt_data_do_servico));

        edt_data_proxima_revisao = (EditText) view.findViewById(R.id.edt_data_proxima_revisao);
        ExibirDataListener exibirDataListener = new ExibirDataListener();
        edt_data_proxima_revisao.setOnClickListener(exibirDataListener);

        edt_km_do_servico = (EditText) view.findViewById(R.id.edt_km_servico);
        edt_km_do_servico.addTextChangedListener(Mask.insert(Mask.MASK_KM, edt_km_do_servico));
        edt_km_do_servico.requestFocus();

        edt_valor_servico = (EditText) view.findViewById(R.id.edt_valor_servico);
        edt_valor_servico.addTextChangedListener(new MonetaryMask(edt_valor_servico) {
        });
//        edt_valor_servico.addTextChangedListener(Mask.insert(Mask.MASK_VALOR, edt_valor_servico));

        spn_ignicao_km = (Spinner) view.findViewById(R.id.spn_ignicao);

        conectarBanco();
        servico_ignicao = new Ignicao();
        spnKmIgnicao();
        verificarDataAtual();
//        verificarDataProximaRevisao();
        recuperarSharedPreferences();
        return view;
    }

    private void conectarBanco() {
        try {
            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            pers_ignicao = new Pers_Ignicao(connBanco);
            dataProximaRevisao = pers_ignicao.exibirDataProximarevisao();
            km_revisao = pers_ignicao.exibirKmProximaRevisao();

            pers_abastecer = new Pers_Abastecer(connBanco);
            km_abastecimento = pers_abastecer.exibirKmAbastecido();

            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Conectado com sucesso Serviços!...");
//            mensagem.setNeutralButton("Sair", null);
//            mensagem.show();


        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Erro de conexão:" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            //mensagem.show();
        }
    }

    private void spnKmIgnicao() {
        adp_spn_km_ignicao = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item
        );
        adp_spn_km_ignicao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp_spn_km_ignicao.add("40000");
        adp_spn_km_ignicao.add("50000");
        adp_spn_km_ignicao.add("60000");
        spn_ignicao_km.setAdapter(adp_spn_km_ignicao);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_salvar_servicos:
                salvarServicoIgnicao();
                break;
        }
    }

    private void salvarServicoIgnicao() {
        try {
            pers_ignicao = new Pers_Ignicao(connBanco);
            if ((!edt_data_do_servico.getText().toString().equals("")) &&
                    (!edt_km_do_servico.getText().toString().equals("")) &&
                    (!edt_valor_servico.getText().toString().equals("")) &&
                    (!edt_data_proxima_revisao.getText().toString().equals(""))) {
                if (cbx_ignicao.isChecked()) {
                    servico_ignicao.setVelas_ignicao(cbx_ignicao.getText().toString());
                    servico_ignicao.setKm_do_servico(Integer.parseInt(edt_km_do_servico.getText().toString()));

                    String valorDoServico_1, valorDoServico_2;
                    valorDoServico_1 = edt_valor_servico.getText().toString().replaceAll("[R$.]", "");
                    valorDoServico_2 = valorDoServico_1.replaceAll("[,]", ".");
                    float valorDoServico = Float.parseFloat(valorDoServico_2);
                    servico_ignicao.setValor_do_servico(valorDoServico);

                    int kmAtual = Integer.parseInt(edt_km_do_servico.getText().toString());
                    String kmRevisao = String.valueOf(spn_ignicao_km.getSelectedItem());
                    int kmProximaRevisao = kmAtual + Integer.parseInt(kmRevisao);
                    servico_ignicao.setKm_proxima_revisao((kmProximaRevisao));
                    pers_ignicao.inserirServicoIgnicao(servico_ignicao);
                    Toast.makeText(getActivity(), "Serviço Salvo com sucesso!!", Toast.LENGTH_SHORT).show();
                    enviarDialogoSair();
                } else {
                    //Toast.makeText(getActivity(), "Item sem checar!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Campos Data e/ou Km e/ou Valor em brancos!!", Toast.LENGTH_SHORT).show();
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

    private void verificarDataAtual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(ano, mes, dia);
            dataSistema = calendar.getTime();
            dataFormatadaAtual = dateFormat.format(dataSistema);
            edt_data_do_servico.setText(dataFormatadaAtual);
            servico_ignicao.setData_do_servico(dataFormatadaAtual);

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
        if ((dataFormatadaAtual.equals(dataProximaRevisao))||
                km_abastecimento.equals(km_revisao)) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("PRÓXIMA REVISÃO!");
            mensagem.setMessage("Data Proxima Revisão Ignição: ");
            mensagem.setNeutralButton("Sair", null);
//            mensagem.create();
//            mensagem.show();
        }
    }

    private void salvarSharedPreferences() {

        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("kmServico", edt_km_do_servico.getText().toString());
        edt.putString("valorServico", edt_valor_servico.getText().toString());//salvando kmAtualServico
        edt.commit();
        //Toast.makeText(getActivity(), "Km Incial salvo!", Toast.LENGTH_SHORT).show();
    }

    private void recuperarSharedPreferences() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if ((pref.contains("kmServico")) && (pref.contains("valorServico"))) {
            edt_km_do_servico.setText(pref.getString("kmServico", edt_km_do_servico.getText().toString()));
            edt_valor_servico.setText(pref.getString("valorServico", edt_valor_servico.getText().toString()));
        } else {
            //Toast.makeText(getActivity(), "Km Incial não definido!", Toast.LENGTH_SHORT).show();
        }
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

    private class SelecionarDataListener extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            dataSistema = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = dateFormat.format(dataSistema);
            edt_data_proxima_revisao.setText(dataFormatada);
            servico_ignicao.setData_proxima_revisao(dataFormatada);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

