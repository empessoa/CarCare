package br.com.empessoa8.carcare.fragments.abastecer;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.empessoa8.carcare.Act_Main;
import br.com.empessoa8.carcare.notificacoes.NotificatioReceiverArrefecimentoFreio;
import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.data_base.DataBase;
import br.com.empessoa8.carcare.entidade.Abastecer;
import br.com.empessoa8.carcare.mask.Mask;
import br.com.empessoa8.carcare.mask.MonetaryMask;
import br.com.empessoa8.carcare.notificacoes.NotificatioReceiverCorreiasTensor;
import br.com.empessoa8.carcare.notificacoes.NotificatioReceiverIgnicao;
import br.com.empessoa8.carcare.notificacoes.NotificatioReceiverOleoFiltro;
import br.com.empessoa8.carcare.persistencia.Per_Arrefecimento_Freio;
import br.com.empessoa8.carcare.persistencia.Per_Correia_Tensor;
import br.com.empessoa8.carcare.persistencia.Per_Oleo_Filtro;
import br.com.empessoa8.carcare.persistencia.Pers_Abastecer;
import br.com.empessoa8.carcare.persistencia.Pers_Ignicao;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Abastecer extends Fragment implements View.OnClickListener {
    View view;
    AlertDialog.Builder mensagem;

    private DataBase dataBase;
    private SQLiteDatabase connBanco;
    private Abastecer abastecer;
    private Pers_Abastecer pers_abastecer;
    private ArrayAdapter<Abastecer> adpAbastecer;

    private Per_Arrefecimento_Freio per_arrefecimento_freio;
    private Per_Correia_Tensor per_correia_tensor;
    private Per_Oleo_Filtro per_oleo_filtro;
    private Pers_Ignicao pers_ignicao;

    //atributos campos calculo do combustivel
    private EditText edt_alcool;
    private EditText edt_gasolina;

    //atributos campos do abastecimento
    private RadioGroup radio_group_combustivel_tipo;
    private RadioButton radio_button_escolhido;

    private EditText edt_data_abastecimento;
    private EditText edt_km_abastecimento;
    private EditText edt_valor_abastecimento;
    private EditText edt_valor_por_litro;

    private Button btn_calcular;
    private Button btn_salvar;

    private String dataAtualAbastecimentoFormatada;
    private String dataProximaRevisaoArrefecimentoFreio;
    private String dataProximaRevisaoCorreiaTensor;
    private String dataProximaRevisaoIgnicao;
    private String dataProximaRevisaoOleoFiltros;
    private int kmProximaRevisaoArrefecimentoFreio;
    private int kmProximaRevisaoCorreiaTensor;
    private int kmProximaRevisaoIgnicao;
    private int kmProximaRevisaoOleoFiltro;
    private int km_ultimo_abastecimento_realizado;

    private boolean status = false;


    public Frag_Abastecer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__abastecer, container, false);

        edt_alcool = (EditText) view.findViewById(R.id.edt_alcool);
        edt_alcool.addTextChangedListener(new MonetaryMask(edt_alcool) {
        });
//        edt_alcool.addTextChangedListener(Mask.insert(Mask.MASK_COMBUSTIVEL, edt_alcool));
        edt_gasolina = (EditText) view.findViewById(R.id.edt_gasolina);
        edt_gasolina.addTextChangedListener(new MonetaryMask(edt_gasolina) {
        });
//        edt_gasolina.addTextChangedListener(Mask.insert(Mask.MASK_COMBUSTIVEL, edt_gasolina));

        radio_group_combustivel_tipo = (RadioGroup) view.findViewById(R.id.radio_group_combustivel);

        edt_data_abastecimento = (EditText) view.findViewById(R.id.edt_data_abastecimento);
        edt_data_abastecimento.addTextChangedListener(Mask.insert(Mask.MASK_DATA_SERVICO, edt_data_abastecimento));

        edt_km_abastecimento = (EditText) view.findViewById(R.id.edt_km_atual);
        edt_km_abastecimento.addTextChangedListener(Mask.insert(Mask.MASK_KM, edt_km_abastecimento));
        edt_km_abastecimento.requestFocus();
//        edt_km_abastecimento.setText("0");

        edt_valor_abastecimento = (EditText) view.findViewById(R.id.edt_valor_abastecido);
        edt_valor_abastecimento.addTextChangedListener(new MonetaryMask(edt_valor_abastecimento) {
        });
//        edt_valor_abastecimento.addTextChangedListener(Mask.insert(Mask.MASK_ABASTECER, edt_valor_abastecimento));

        edt_valor_por_litro = (EditText) view.findViewById(R.id.edt_valor_litro);
        edt_valor_por_litro.addTextChangedListener(new MonetaryMask(edt_valor_por_litro) {
        });
//        edt_valor_por_litro.addTextChangedListener(Mask.insert(Mask.MASK_COMBUSTIVEL, edt_valor_por_litro));

        btn_calcular = (Button) view.findViewById(R.id.btn_calcular_abastecer);
        btn_calcular.setOnClickListener(this);
        btn_salvar = (Button) view.findViewById(R.id.btn_salvar_abastecer);
        btn_salvar.setOnClickListener(this);


        abastecer = new Abastecer();

        conectarBanco();
        recuperarSharedPreferences();
        verificarDataAtual();
//        verificarProximaRevisao();
        setAlarm();

        return view;
    }

    private void verificarDataAtual() {
        try {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            String mesAtual;
            String diaAtual;
            mes += 1;
            if (mes < 10) {
                mesAtual = "0" + mes;
            } else {
                mesAtual = String.valueOf(mes);
            }
            if (dia < 10) {
                diaAtual = "0" + dia;
            } else {
                diaAtual = String.valueOf(dia);
            }

            dataAtualAbastecimentoFormatada = diaAtual + "/" + mesAtual + "/" + ano;
            edt_data_abastecimento.setText(dataAtualAbastecimentoFormatada);
            abastecer.setData_do_abastecimento(dataAtualAbastecimentoFormatada);

        } catch (Exception e) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando data do sistema...");
            mensagem.setMessage("Error: " + e);
            mensagem.setNeutralButton("Sair", null);
            mensagem.create();
            mensagem.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_calcular_abastecer:
                calcularAbastecer();
                break;
            case R.id.btn_salvar_abastecer:
                salvarAbastecer();
                break;
        }
    }

    private void verificarProximaRevisao() {
        if (dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoArrefecimentoFreio) && (!dataProximaRevisaoArrefecimentoFreio.equals(""))
                || (km_ultimo_abastecimento_realizado >= kmProximaRevisaoArrefecimentoFreio) && (kmProximaRevisaoArrefecimentoFreio > 1)) {
            enviarDialogo("ALERTA PARA A REVISÃO",
                    "MANUTENÇÃO PREVENTIVA ARREFECIMENTO E FREIO \n\n VERIFICAR DATA OU KM \n\nDATA PRÓXIMA REVISÃO: \n" + dataProximaRevisaoArrefecimentoFreio +
                            "\n\nKM DA PRÓXIMA REVISÃO: \n" + kmProximaRevisaoArrefecimentoFreio, "OK", "SAIR", "");
        }
        if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoCorreiaTensor)) && (!dataProximaRevisaoCorreiaTensor.equals(""))
                || (km_ultimo_abastecimento_realizado >= kmProximaRevisaoCorreiaTensor) && (kmProximaRevisaoCorreiaTensor > 1)) {
            enviarDialogo("ALERTA PARA A REVISÃO",
                    "\nMANUTENÇÃO PREVENTIVA CORREIAS E TENSOR \n\n VERIFICAR DATA OU KM \n\nDATA PRÓXIMA REVISÃO: \n" + dataProximaRevisaoCorreiaTensor +
                            "\n\nKM DA PRÓXIMA REVISÃO: \n" + kmProximaRevisaoCorreiaTensor, "OK", "SAIR", "");
        }
        if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoIgnicao)) && (!dataProximaRevisaoIgnicao.equals(""))
                || (km_ultimo_abastecimento_realizado >= kmProximaRevisaoIgnicao) && (kmProximaRevisaoIgnicao > 1)) {
            enviarDialogo("ALERTA PARA A REVISÃO",
                    "\nMANUTENÇÃO PREVENTIVA IGNIÇÃO \n\n VERIFICAR DATA OU KM \n\nDATA PRÓXIMA REVISÃO: \n" + dataProximaRevisaoCorreiaTensor +
                            "\n\nKM DA PRÓXIMA REVISÃO: \n" + kmProximaRevisaoCorreiaTensor, "OK", "SAIR", "");
        }
        if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoOleoFiltros)) && (!dataProximaRevisaoOleoFiltros.equals(""))
                || (km_ultimo_abastecimento_realizado >= kmProximaRevisaoOleoFiltro) && (kmProximaRevisaoOleoFiltro > 1)) {
            enviarDialogo("ALERTA PARA A REVISÃO",
                    "\nMANUTENÇÃO PREVENTIVA OLEOS E FILTROS \n\n VERIFICAR DATA OU KM \n\nDATA PRÓXIMA REVISÃO: \n" + dataProximaRevisaoOleoFiltros +
                            "\n\nKM DA PRÓXIMA REVISÃO: \n" + kmProximaRevisaoOleoFiltro, "OK", "SAIR", "");
        }
    }

    private void verificarProximaRevisao_e_salvar() {
        int kmAbastecimentoAtual = Integer.parseInt(edt_km_abastecimento.getText().toString());
        if ((kmProximaRevisaoArrefecimentoFreio == 0 || kmAbastecimentoAtual < kmProximaRevisaoArrefecimentoFreio && !dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoArrefecimentoFreio)) &&
                (kmProximaRevisaoCorreiaTensor == 0 || kmAbastecimentoAtual < kmProximaRevisaoCorreiaTensor && !dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoCorreiaTensor)) &&
                (kmProximaRevisaoIgnicao == 0 || kmAbastecimentoAtual < kmProximaRevisaoIgnicao && !dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoIgnicao)) &&
                (kmProximaRevisaoOleoFiltro == 0 || kmAbastecimentoAtual < kmProximaRevisaoOleoFiltro && !dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoOleoFiltros))) {
            enviarDialogo("ENCERRAR SERVIÇOS", "CONFIRMAR: ", "", "SIM", "NÃO");
        } else {
            if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoArrefecimentoFreio) && (!dataProximaRevisaoArrefecimentoFreio.equals(""))
                    || (kmAbastecimentoAtual >= kmProximaRevisaoArrefecimentoFreio) && (kmProximaRevisaoArrefecimentoFreio > 1))) {

                enviarDialogo("ALERTA PARA A REVISÃO",
                        "\n\nMANUTENÇÃO PREVENTIVA \n\nARREFECIMENTO E FREIO \n\n VERIFICAR DATA OU KM \n\nDATA PRÓXIMA REVISÃO: " + dataProximaRevisaoArrefecimentoFreio +
                                "\n\nKM DA PRÓXIMA REVISÃO: \n" + kmProximaRevisaoArrefecimentoFreio, "", "SAIR", "OK");
            }
            if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoCorreiaTensor)) && (!dataProximaRevisaoCorreiaTensor.equals("")) ||
                    (kmAbastecimentoAtual >= kmProximaRevisaoCorreiaTensor) && (kmProximaRevisaoCorreiaTensor > 1)) {
                enviarDialogo("ALERTA PARA A REVISÃO",
                        "\n\nMANUTENÇÃO PREVENTIVA \n\nCORREIAS E TENSOR \n\n VERIFICAR DATA OU KM \n\nDATA PRÓXIMA REVISÃO: " + dataProximaRevisaoCorreiaTensor +
                                "\n\nKM DA PRÓXIMA REVISÃO: \n" + kmProximaRevisaoCorreiaTensor, "", "SAIR", "OK");
            }
            if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoIgnicao)) && (!dataProximaRevisaoIgnicao.equals("")) ||
                    (kmAbastecimentoAtual >= kmProximaRevisaoIgnicao) && (kmProximaRevisaoIgnicao > 1)) {
                enviarDialogo("ALERTA PARA A REVISÃO",
                        "\n\nMANUTENÇÃO PREVENTIVA \n\nIGNIÇÃO \n\n VERIFICAR DATA OU KM \n\n DATA PRÓXIMA REVISÃO: " + dataProximaRevisaoIgnicao +
                                "\n\nKM DA PRÓXIMA REVISÃO: \n" + kmProximaRevisaoIgnicao, "", "SAIR", "OK");
            }
            if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoOleoFiltros)) && (!dataProximaRevisaoOleoFiltros.equals("")) ||
                    (kmAbastecimentoAtual >= kmProximaRevisaoOleoFiltro) && (kmProximaRevisaoOleoFiltro > 1)) {
                enviarDialogo("ALERTA PARA A REVISÃO",
                        "\n\nMANUTENÇÃO PREVENTIVA \n\nÓLEOS E FILTROS \n\n VERIFICAR DATA OU KM \n\n DATA PRÓXIMA REVISÃO: " + dataProximaRevisaoOleoFiltros +
                                "\n\nKM DA PRÓXIMA REVISÃO: \n" + kmProximaRevisaoOleoFiltro, "", "SAIR", "OK");
            }
        }
    }

    private void enviarDialogo(String t, String m, String sair, String sim, String nao) {
        mensagem = new AlertDialog.Builder(getActivity());
        mensagem.setTitle(t);
        mensagem.setMessage(m);
        mensagem.setNeutralButton(sair, null);
        mensagem.setPositiveButton(sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(getActivity(), Act_Main.class), 0);
                getActivity().finish();
            }
        });
        mensagem.setNegativeButton(nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(getActivity(), getActivity().getClass()), 0);
                getActivity().finish();
            }
        });
        mensagem.create();
        mensagem.show();
        Toast.makeText(getActivity(), "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void salvarAbastecer() {

        try {
            if (radio_group_combustivel_tipo.getCheckedRadioButtonId() > 0) {
                if ((!edt_data_abastecimento.getText().toString().equals("")) &&
                        (!edt_km_abastecimento.getText().toString().equals("")) &&
                        (!edt_valor_abastecimento.getText().toString().equals("")) &&
                        (!edt_valor_por_litro.getText().toString().equals(""))) {
                    int kmAbastecimentoAtual = Integer.parseInt(edt_km_abastecimento.getText().toString());
                    if (kmAbastecimentoAtual >= km_ultimo_abastecimento_realizado) {
                        radio_button_escolhido = (RadioButton) view.findViewById(radio_group_combustivel_tipo.getCheckedRadioButtonId());
                        String combustivel_escolhido = radio_button_escolhido.getText().toString();
                        abastecer.setCombustivel_tipo(combustivel_escolhido);

                        String valorAbastecidoReplace_1, valorAbastecidoReplace_2, valorLitroReplace_1, valorLitroReplace_2;
                        valorAbastecidoReplace_1 = edt_valor_abastecimento.getText().toString().replaceAll("[R$.]", "");
                        valorAbastecidoReplace_2 = valorAbastecidoReplace_1.replaceAll("[,]", ".");
                        valorLitroReplace_1 = edt_valor_por_litro.getText().toString().replaceAll("[R$.]", "");
                        valorLitroReplace_2 = valorLitroReplace_1.replaceAll("[,]", ".");

                        float valorAbastecidoClean, valorLitroClean, litros_abastecidos;
                        valorAbastecidoClean = Float.parseFloat(valorAbastecidoReplace_2);
                        valorLitroClean = Float.parseFloat(valorLitroReplace_2);
                        litros_abastecidos = (valorAbastecidoClean) / (valorLitroClean);

                        abastecer.setTotal_de_litro(Float.valueOf(String.format(Locale.US, "%.2f", litros_abastecidos)));
                        abastecer.setKm_do_abastecimento(Integer.parseInt(edt_km_abastecimento.getText().toString()));
                        abastecer.setValor_total_abastecido(valorAbastecidoClean);
                        abastecer.setValor_do_litro(valorLitroClean);

                        pers_abastecer.inserirAbastecimento(abastecer);

                        verificarProximaRevisao_e_salvar();
                        salvarSharedPreferences();
                        setAlarm();
                    } else {
                        Toast.makeText(getActivity(), "KM atual menor que o anterior", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Campos em Branco!", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), "Selecionar tipo de combustível!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            enviarDialogo("ERROR", "", "Error método salvarAbastecer !!!\n" + ex.getMessage(), "", "");
        }
    }

    private void calcularAbastecer() {
        if ((!edt_alcool.getText().toString().equals("")) &&
                !edt_gasolina.getText().toString().equals("")) {

            String alcoolValor_1, alcoolValor_2, gasolinaValor_1, gasolinaValor_2;
            alcoolValor_1 = edt_alcool.getText().toString().replaceAll("[R$.]", "");
            alcoolValor_2 = alcoolValor_1.replaceAll("[,]", ".");
            gasolinaValor_1 = edt_gasolina.getText().toString().replaceAll("[R$.]", "");
            gasolinaValor_2 = gasolinaValor_1.replaceAll("[,]", ".");

            float alcoolValor, gasolinaValor, resultado;
            alcoolValor = (Float.parseFloat(alcoolValor_2));
            gasolinaValor = (Float.parseFloat(gasolinaValor_2));

            resultado = (alcoolValor) / (gasolinaValor);
            if (resultado >= 0.7) {
                enviarDialogo("ABASTECER", "Melhor opção de combustível: GASOLINA", "SAIR", "", "");
            } else {
                enviarDialogo("ABASTECER", "Melhor opção de combustível: ÁLCOOL", "SAIR", "", "");
            }
        } else {
            Toast.makeText(getActivity(), "Campos Combustível em branco!", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarSharedPreferences() {

        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("kmAtualAbastecido", edt_km_abastecimento.getText().toString());//salvando kmAtualServico
        edt.putString("valorPorLitro", edt_valor_por_litro.getText().toString());//salvando kmAtualServico
        edt.putString("valorAbastecido", edt_valor_abastecimento.getText().toString());//salvando kmAtualServico
        edt.commit();
        //Toast.makeText(getActivity(), "Km Incial salvo!", Toast.LENGTH_SHORT).show();

    }

    private void recuperarSharedPreferences() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if ((pref.contains("kmAtualAbastecido")) && (pref.contains("valorPorLitro")) && (pref.contains("valorAbastecido"))) {
            edt_km_abastecimento.setText(pref.getString("kmAtualAbastecido", edt_km_abastecimento.getText().toString()));
            edt_valor_por_litro.setText(pref.getString("valorPorLitro", edt_valor_por_litro.getText().toString()));
            edt_valor_abastecimento.setText(pref.getString("valorAbastecido", edt_valor_abastecimento.getText().toString()));
        } else {
            //Toast.makeText(getActivity(), "Km Incial não definido!", Toast.LENGTH_SHORT).show();
        }
    }

    private void conectarBanco() {
        try {
            dataBase = new DataBase(getActivity());
            connBanco = dataBase.getReadableDatabase();
            //consulta o banco
            pers_abastecer = new Pers_Abastecer(connBanco);
            adpAbastecer = pers_abastecer.buscarAbastecerRealizado(getActivity());
            km_ultimo_abastecimento_realizado = pers_abastecer.exibirKmAbastecido();

            //Pegando a data e km proxima revisao do Arrefecimento e Freios
            per_arrefecimento_freio = new Per_Arrefecimento_Freio(connBanco);
            dataProximaRevisaoArrefecimentoFreio = per_arrefecimento_freio.exibirDataProximarevisao();
            kmProximaRevisaoArrefecimentoFreio = per_arrefecimento_freio.exibirKmProximaRevisao();

            per_correia_tensor = new Per_Correia_Tensor(connBanco);
            dataProximaRevisaoCorreiaTensor = per_correia_tensor.exibirDataProximarevisaoCorreiaTensor();
            kmProximaRevisaoCorreiaTensor = per_correia_tensor.exibirKmProximaRevisao();

            per_oleo_filtro = new Per_Oleo_Filtro(connBanco);
            dataProximaRevisaoOleoFiltros = per_oleo_filtro.exibirDataProximarevisaoOleoFiltro();
            kmProximaRevisaoOleoFiltro = per_oleo_filtro.exibirKmProximaRevisao();


            pers_ignicao = new Pers_Ignicao(connBanco);
            dataProximaRevisaoIgnicao = pers_ignicao.exibirDataProximarevisao();
            kmProximaRevisaoIgnicao = pers_ignicao.exibirKmProximaRevisao();

            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Conectado com sucesso!");
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

    @Override
    public void onStop() {
        super.onStop();
        recuperarSharedPreferences();
        conectarBanco();
        setAlarm();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connBanco != null) {
            connBanco.close();
        }
    }

    public void setAlarm() {
        AlarmManager alarmManager_1 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager_2 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager_3 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager_4 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Long alertTime = new GregorianCalendar().getTimeInMillis();

        Intent alertIntentArrefecimentoFreio = new Intent(getActivity(), NotificatioReceiverArrefecimentoFreio.class);
        Intent alertIntentCorreiasTensor = new Intent(getActivity(), NotificatioReceiverCorreiasTensor.class);
        Intent alertIntentIgnicao = new Intent(getActivity(), NotificatioReceiverIgnicao.class);
        Intent alertIntentOleoFiltro = new Intent(getActivity(), NotificatioReceiverOleoFiltro.class);

        PendingIntent pendingIntentArrefecimentoFreio = PendingIntent.getBroadcast(getActivity(), 1, alertIntentArrefecimentoFreio,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentCorreiasTensor = PendingIntent.getBroadcast(getActivity(), 1, alertIntentCorreiasTensor ,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentIgnicao = PendingIntent.getBroadcast(getActivity(), 1, alertIntentIgnicao ,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentOleoFiltro = PendingIntent.getBroadcast(getActivity(), 1, alertIntentOleoFiltro ,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoArrefecimentoFreio) && (!dataProximaRevisaoArrefecimentoFreio.equals(""))
                || (km_ultimo_abastecimento_realizado >= kmProximaRevisaoArrefecimentoFreio) && (kmProximaRevisaoArrefecimentoFreio > 1)) {
            alarmManager_1.set(AlarmManager.RTC_WAKEUP,alertTime, pendingIntentArrefecimentoFreio);
        }
        if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoCorreiaTensor)) && (!dataProximaRevisaoCorreiaTensor.equals(""))
                || (km_ultimo_abastecimento_realizado >= kmProximaRevisaoCorreiaTensor) && (kmProximaRevisaoCorreiaTensor > 1)) {
            alarmManager_2.set(AlarmManager.RTC_WAKEUP,alertTime, pendingIntentCorreiasTensor);
        }
        if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoIgnicao)) && (!dataProximaRevisaoIgnicao.equals(""))
                || (km_ultimo_abastecimento_realizado >= kmProximaRevisaoIgnicao) && (kmProximaRevisaoIgnicao > 1)) {
            alarmManager_3.set(AlarmManager.RTC_WAKEUP,alertTime, pendingIntentIgnicao);
        }
        if ((dataAtualAbastecimentoFormatada.equals(dataProximaRevisaoOleoFiltros)) && (!dataProximaRevisaoOleoFiltros.equals(""))
                || (km_ultimo_abastecimento_realizado >= kmProximaRevisaoOleoFiltro) && (kmProximaRevisaoOleoFiltro > 1)) {
            alarmManager_4.set(AlarmManager.RTC_WAKEUP,alertTime, pendingIntentOleoFiltro);
        }
    }
}
