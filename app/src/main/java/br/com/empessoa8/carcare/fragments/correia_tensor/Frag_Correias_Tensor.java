package br.com.empessoa8.carcare.fragments.correia_tensor;


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
import br.com.empessoa8.carcare.entidade.Correias_Tensor;
import br.com.empessoa8.carcare.mask.Mask;
import br.com.empessoa8.carcare.mask.MonetaryMask;
import br.com.empessoa8.carcare.persistencia.Per_Correia_Tensor;
import br.com.empessoa8.carcare.persistencia.Pers_Abastecer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Correias_Tensor extends Fragment implements View.OnClickListener {

    View view;
    AlertDialog.Builder mensagem;
    private DataBase dataBase;
    private SQLiteDatabase connBanco;
    private Correias_Tensor correias_tensor;
    private Per_Correia_Tensor per_correia_tensor;

    private Button btn_salvar_servicos;

    private EditText edt_data_do_servico, edt_km_do_servico, edt_valor_servico, edt_data_proxima_revisao;

    private CheckBox cbx_correia_dentada, cbx_correia_poly_v, cbx_tensionador, cbx_bomba_de_agua;

    private Spinner spn_correia_tensor;
    private ArrayAdapter<String> adp_spn_correia_tensor;

    private Date dataSistema;
    String dataAtualFormatada;
    String dataProximaRevisao;

    private Pers_Abastecer pers_abastecer;
    private Integer km_abastecimento;

    private int km_manutencao;

    public Frag_Correias_Tensor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__correias__tensor, container, false);
        spn_correia_tensor = (Spinner) view.findViewById(R.id.spn__correia_tensor);

        edt_km_do_servico = (EditText) view.findViewById(R.id.edt_km_servico_Correia_tensor);
        edt_km_do_servico.addTextChangedListener(Mask.insert(Mask.MASK_KM, edt_km_do_servico));
        edt_km_do_servico.requestFocus();

        edt_valor_servico = (EditText) view.findViewById(R.id.edt_valor_servico_Correia_tensor);
        edt_valor_servico.addTextChangedListener(new MonetaryMask(edt_valor_servico) {
        });
//        edt_valor_servico.addTextChangedListener(Mask.insert(Mask.MASK_VALOR, edt_valor_servico));

        edt_data_do_servico = (EditText) view.findViewById(R.id.edt_data_servico_Correia_tensor);
        edt_data_do_servico.addTextChangedListener(Mask.insert(Mask.MASK_DATA_SERVICO, edt_data_do_servico));

        edt_data_proxima_revisao = (EditText) view.findViewById(R.id.edt_next_revisao_correia_tensor);
        ExibirDataListener dataListener = new ExibirDataListener();
        edt_data_proxima_revisao.setOnClickListener(dataListener);

        cbx_correia_dentada = (CheckBox) view.findViewById(R.id.cbx_correia_dentada);
        cbx_correia_poly_v = (CheckBox) view.findViewById(R.id.cbx_correia_poly);
        cbx_tensionador = (CheckBox) view.findViewById(R.id.cbx_tensor);
        cbx_bomba_de_agua = (CheckBox) view.findViewById(R.id.cbx_bomba_de_agua);

        btn_salvar_servicos = (Button) view.findViewById(R.id.btn_salvar_servico_correia_tensor);
        btn_salvar_servicos.setOnClickListener(this);


        correias_tensor = new Correias_Tensor();
        conectarBanco();
        recuperarSharedPreferences();
        spnKmCorreiaTensor();
        exibirDataAtual();
//        verificarDataProximaRevisao();

        return view;
    }

    private void salvarServicoCorreiaTensor() {
        correias_tensor = new Correias_Tensor();
        try {
            if ((!edt_data_do_servico.getText().toString().equals("")) &&
                    (!edt_km_do_servico.getText().toString().equals("")) &&
                    (!edt_data_proxima_revisao.getText().toString().equals("")) &&
                    (!edt_valor_servico.getText().toString().equals(""))) {
                if ((cbx_correia_dentada.isChecked()) ||
                        (cbx_correia_poly_v.isChecked()) ||
                        (cbx_bomba_de_agua.isChecked()) ||
                        (cbx_tensionador.isChecked())) {
                    if (cbx_correia_dentada.isChecked()) {
                        correias_tensor.setCorreia_dentada(cbx_correia_dentada.getText().toString());
                    }
                    if (cbx_correia_poly_v.isChecked()) {
                        correias_tensor.setCorreia_poly_v(cbx_correia_poly_v.getText().toString());
                    }
                    if (cbx_tensionador.isChecked()) {
                        correias_tensor.setTensor_correia(cbx_tensionador.getText().toString());
                    }
                    if (cbx_bomba_de_agua.isChecked()) {
                        correias_tensor.setBomba_de_agua(cbx_bomba_de_agua.getText().toString());
                    }
                    correias_tensor.setKm_do_servico(Integer.parseInt(edt_km_do_servico.getText().toString()));

                    String valorDoServico_1, valorDoServico_2;
                    valorDoServico_1 = edt_valor_servico.getText().toString().replaceAll("[R$.]", "");
                    valorDoServico_2 = valorDoServico_1.replaceAll("[,]", ".");
                    float valorDoServico = Float.parseFloat(valorDoServico_2);
                    correias_tensor.setValor_do_servico(valorDoServico);

                    int kmAtual = Integer.parseInt(edt_km_do_servico.getText().toString());
                    String kmRevisao = String.valueOf(spn_correia_tensor.getSelectedItem());
                    int kmProximaRevisao = kmAtual + Integer.parseInt(kmRevisao);
                    correias_tensor.setKm_proxima_revisao((kmProximaRevisao));

                    correias_tensor.setData_do_servico(edt_data_do_servico.getText().toString());
                    correias_tensor.setData_proxima_revisao(edt_data_proxima_revisao.getText().toString());

                    per_correia_tensor.inserirServicoCorreiaTensor(correias_tensor);

                    Toast.makeText(getActivity(), "Serviço Salvo com Sucesso!!", Toast.LENGTH_SHORT).show();

                    enviarDialogoSair();

                } else {
                    Toast.makeText(getActivity(), "Itens sem checar!!", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), "Campos em brancos!!", Toast.LENGTH_SHORT).show();
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

    private void exibirDataAtual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(ano, mes, dia);
            dataSistema = calendar.getTime();
            dataAtualFormatada = dateFormat.format(dataSistema);
            edt_data_do_servico.setText(dataAtualFormatada);

        } catch (Exception e) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Erro " + e);
            mensagem.setNeutralButton("Sair", null);
            mensagem.create();
            mensagem.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_salvar_servico_correia_tensor:
                salvarServicoCorreiaTensor();
                break;
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

    private class SelecionarDataListener extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            dataSistema = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateNextFix = dateFormat.format(dataSistema);
            edt_data_proxima_revisao.setText(dateNextFix);
        }

    }

    private void conectarBanco() {
        try {
            dataBase = new DataBase(getActivity());
            connBanco = dataBase.getReadableDatabase();
            mensagem = new AlertDialog.Builder(getActivity());

            per_correia_tensor = new Per_Correia_Tensor(connBanco);
            dataProximaRevisao = per_correia_tensor.exibirDataProximarevisaoCorreiaTensor();
            km_manutencao = per_correia_tensor.exibirKmProximaRevisao();

            pers_abastecer = new Pers_Abastecer(connBanco);
            km_abastecimento = pers_abastecer.exibirKmAbastecido();

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

    private void verificarDataProximaRevisao() {
        
        if ((dataAtualFormatada.equals(dataProximaRevisao)) ||
                km_abastecimento.equals(km_manutencao)) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("ALERTA DE REVISÃO");
            mensagem.setMessage("\nMANUTENÇÃO PREVENTIVA IGNIÇÃO " +
                    "\nData Revisão: " + dataProximaRevisao +
                    "\nOU Km da revisão: " + km_manutencao);
            mensagem.setNeutralButton("Sair", null);
            mensagem.create();
            mensagem.show();
        }
    }

    private void spnKmCorreiaTensor() {
        adp_spn_correia_tensor = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item
        );
        adp_spn_correia_tensor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adp_spn_correia_tensor.add("40000");
        adp_spn_correia_tensor.add("50000");
        adp_spn_correia_tensor.add("60000");

        spn_correia_tensor.setAdapter(adp_spn_correia_tensor);
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


