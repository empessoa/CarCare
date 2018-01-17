package br.com.empessoa8.carcare.fragments.viagem;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.data_base.DataBase;
import br.com.empessoa8.carcare.mask.Mask;
import br.com.empessoa8.carcare.mask.MonetaryMask;
import br.com.empessoa8.carcare.persistencia.Pers_Abastecer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Viagem extends Fragment implements View.OnClickListener{

    AlertDialog.Builder mensagem;
    View view;

    private DataBase carCare_dataBase;
    private SQLiteDatabase connBanco;

    private EditText edt_km_inicial;
    private EditText edt_km_final;
    private EditText edt_autonomia;
    private EditText edt_valor_combustivel;
    private EditText edt_valor_pedagio;
    private EditText edt_ocupantes;

    private Button btn_calcular;
    private Button btn_limpar;

    private Pers_Abastecer pers_abastecer;
    private Integer km_abastecimento;


    public Frag_Viagem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__viagem, container, false);

        edt_km_inicial = (EditText) view.findViewById(R.id.edt_km_inicial);
        edt_km_inicial.addTextChangedListener(Mask.insert(Mask.MASK_KM, edt_km_inicial));

        edt_km_final = (EditText) view.findViewById(R.id.edt_km_final);
        edt_km_final.addTextChangedListener(Mask.insert(Mask.MASK_KM, edt_km_final));
        edt_km_final.requestFocus();

        edt_autonomia = (EditText) view.findViewById(R.id.edt_autonomia);
        edt_autonomia.addTextChangedListener(Mask.insert(Mask.MASK_AUTONOMIA, edt_autonomia));

        edt_valor_combustivel = (EditText) view.findViewById(R.id.edt_combustivel);
        edt_valor_combustivel.addTextChangedListener(new MonetaryMask(edt_valor_combustivel) {
        });
//        edt_valor_combustivel.addTextChangedListener(Mask.insert(Mask.MASK_COMBUSTIVEL, edt_valor_combustivel));

        edt_valor_pedagio = (EditText) view.findViewById(R.id.edt_pedagio);
        edt_valor_pedagio.addTextChangedListener(new MonetaryMask(edt_valor_pedagio) {
        });
//        edt_valor_pedagio.addTextChangedListener(Mask.insert(Mask.MASK_PEDAGIO, edt_valor_pedagio));

        edt_ocupantes = (EditText) view.findViewById(R.id.edt_ocupantes);
        edt_ocupantes.addTextChangedListener(Mask.insert(Mask.MASK_OCUPANTE, edt_ocupantes));

        btn_calcular = (Button) view.findViewById(R.id.btn_calcular_viagem);
        btn_calcular.setOnClickListener(Frag_Viagem.this);
        btn_limpar = (Button) view.findViewById(R.id.btn_limpar_viagem);
        btn_limpar.setOnClickListener(Frag_Viagem.this);

        conectarBanco();
        recuperarSharedPreferences();
        return view;
    }

    @Override
    public void onClick(View botao) {
        if (botao == btn_calcular) {
            calcularViagem();
        } else if (botao == btn_limpar) {
            limparViagem();
        }
    }

    private void calcularViagem() {
        if ((!edt_km_inicial.getText().toString().equals("")) &&
                (!edt_km_final.getText().toString().equals("")) &&
                (!edt_autonomia.getText().toString().equals("")) &&
                (!edt_valor_combustivel.getText().toString().equals("")) &&
                (!edt_valor_pedagio.getText().toString().equals("")) &&
                (!edt_ocupantes.getText().toString().equals(""))) {

            if ((Integer.parseInt(edt_km_final.getText().toString()) > (Integer.parseInt(edt_km_inicial.getText().toString())))) {

                int km_rodado = (Integer.parseInt(edt_km_final.getText().toString()) -
                        (Integer.parseInt(edt_km_inicial.getText().toString())));
                float combustivel_litros = (km_rodado / Float.parseFloat(edt_autonomia.getText().toString()));
                String valorDoCombustivel_2 = edt_valor_combustivel.getText().toString().replaceAll("[R$.]", "");
                String valorDoCombustivel_1 = valorDoCombustivel_2.replaceAll("[,]", ".");
                float combustivel_valor = (Float.parseFloat(valorDoCombustivel_1) * combustivel_litros);
                String valorDoPedagio_2 = edt_valor_pedagio.getText().toString().replaceAll("[R$.]", "");
                String valorDoPedagio_1 = valorDoPedagio_2.replaceAll("[,]", ".");
                float gasto_total_viagem = (combustivel_valor + Float.parseFloat(valorDoPedagio_1));
                float gasto_total_ocupante = (gasto_total_viagem / Integer.parseInt(edt_ocupantes.getText().toString()));

                //Double valor_combustivel = Double.valueOf(String.format(Locale.US,"%2f",Math.ceil(combustivel_gasto)));//arredondando para cima (floor) para baixo
                Double litros_combustivel = Double.valueOf(String.format(Locale.US, "%.2f", combustivel_litros));
                Double valor_combustivel = Double.valueOf(String.format(Locale.US, "%.2f", combustivel_valor));
                Double total_viagem = Double.valueOf(String.format(Locale.US, "%.2f", gasto_total_viagem));
                Double valor_ocupante = Double.valueOf(String.format(Locale.US, "%.2f", gasto_total_ocupante));

                mensagem = new AlertDialog.Builder(getActivity());
                mensagem.setTitle("CÁLCULO DA VIAGEM");
                mensagem.setMessage("KM Rodado: " + km_rodado +
                        "\n\nLitros de  combustível: " + litros_combustivel + " litros" +
                        "\n\nValor  em  combustível: R$ " + valor_combustivel +
                        "\n\nValor  em  Pedágio: R$ " + edt_valor_pedagio.getText().toString() +
                        "\n\nTotal  da  viagem: R$ " + total_viagem +
                        "\n\nValor  por ocupante R$ : " + valor_ocupante);

                mensagem.setNeutralButton("SAIR", null);
                mensagem.create();
                mensagem.show();

                //salvarSharedPreferences();

            } else {
                Toast.makeText(getActivity(), "KM Inicial maior que KM Final!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), "Campos em branco!", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparViagem() {
        edt_km_inicial.setText("");
        edt_km_final.setText("");
        edt_autonomia.setText("");
        edt_valor_combustivel.setText("");
        edt_valor_pedagio.setText("");
        edt_ocupantes.setText("");
    }
    private void salvarSharedPreferences() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("km_inicial", edt_km_inicial.getText().toString());
        edt.putString("km_final", edt_km_final.getText().toString());
        edt.commit();
        //Toast.makeText(getActivity(), "Km Incial salvo!", Toast.LENGTH_SHORT).show();
    }
    private void recuperarSharedPreferences(){
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if ((pref.contains("km_inicial"))&&(pref.contains("km_final"))){
            String kmInicial = pref.getString("km_inicial", edt_km_inicial.getText().toString());
            String kmFinal = pref.getString("km_final", edt_km_final.getText().toString());
            edt_km_inicial.setText(kmInicial);
            edt_km_final.setText(kmFinal);
        }else {
            //Toast.makeText(getActivity(), "Km Incial não definido!", Toast.LENGTH_SHORT).show();
        }
    }
    private void conectarBanco() {
        try {
            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            pers_abastecer = new Pers_Abastecer(connBanco);
            km_abastecimento = pers_abastecer.exibirKmAbastecido();

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


    @Override
    public void onStart() {
        super.onStart();
        edt_km_inicial.setText(Integer.toString(km_abastecimento));
    }

    @Override
    public void onStop() {
        salvarSharedPreferences();
        super.onStop();
    }
}
