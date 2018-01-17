package br.com.empessoa8.carcare.fragments.abastecer;


import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.data_base.DataBase;
import br.com.empessoa8.carcare.entidade.Abastecer;
import br.com.empessoa8.carcare.persistencia.Pers_Abastecer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Abastecimento_Simples extends Fragment {

    private AlertDialog.Builder mensagem;
    View view;
    private DataBase dataBase;
    private SQLiteDatabase connBanco;
    private Abastecer abastecer;
    private Pers_Abastecer pers_abastecer;
    private ListView lst_abastecimento_simples;
    private ArrayAdapter<Abastecer> adapterAbastecimento;

    public Frag_Abastecimento_Simples() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__abastecimento__simples, container, false);

        lst_abastecimento_simples = (ListView) view.findViewById(R.id.lst_abastecimento_simples);
        lst_abastecimento_simples.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), Act_Abastecer.class));
            }
        });

        conectarBanco();

        return view;

    }
    private void conectarBanco() {
        try {
            dataBase = new DataBase(getActivity());
            connBanco = dataBase.getReadableDatabase();
            //consulta o banco
            pers_abastecer = new Pers_Abastecer(connBanco);

            adapterAbastecimento = pers_abastecer.buscaAbastecimentoSimples(getActivity());
            lst_abastecimento_simples.setAdapter(adapterAbastecimento);

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
            mensagem.show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapterAbastecimento = pers_abastecer.buscaAbastecimentoSimples(getActivity());
        lst_abastecimento_simples.setAdapter(adapterAbastecimento);
    }
}
