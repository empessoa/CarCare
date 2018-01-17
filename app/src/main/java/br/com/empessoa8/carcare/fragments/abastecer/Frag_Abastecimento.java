package br.com.empessoa8.carcare.fragments.abastecer;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import br.com.empessoa8.carcare.Act_Main;
import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.data_base.DataBase;
import br.com.empessoa8.carcare.entidade.Abastecer;
import br.com.empessoa8.carcare.persistencia.Pers_Abastecer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Abastecimento extends Fragment {

    private AlertDialog.Builder mensagem;
    View view;
    private DataBase dataBase;
    private SQLiteDatabase connBanco;
    private Abastecer abastecer;
    private Pers_Abastecer pers_abastecer;
    private ListView lst_abastecer_realizado_completo;
    private ArrayAdapter<Abastecer> adapterAbastecimento;

    public Frag_Abastecimento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__abastecimento, container, false);

        lst_abastecer_realizado_completo = (ListView) view.findViewById(R.id.lst_abastecer_realizado_completa);
        lst_abastecer_realizado_completo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogDelete(position);
                return false;
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

            adapterAbastecimento = pers_abastecer.buscarAbastecerRealizado(getActivity());
            lst_abastecer_realizado_completo.setAdapter(adapterAbastecimento);

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

    private void dialogDelete(int position) {
        try {
            abastecer = new Abastecer();
            abastecer = adapterAbastecimento.getItem(position);

            dataBase = new DataBase(getActivity());
            connBanco = dataBase.getReadableDatabase();
            pers_abastecer = new Pers_Abastecer(connBanco);

            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("DELETAR SERVIÇO");
            mensagem.setMessage("CONFIRMAR");
            mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pers_abastecer.excluirAbastecimento(abastecer.get_id());
                    //startActivityForResult(new Intent(getActivity(), getActivity().getClass()), 1);
                    startActivityForResult(new Intent(getActivity(), Act_Main.class), 0);
                    Toast.makeText(getActivity(), "DELETADO COM SUCESSO!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    //enviarDialogoSair();
                }
            });
            mensagem.setNeutralButton("NÂO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Cancelado!", Toast.LENGTH_SHORT).show();

                }
            });
            mensagem.create();
            mensagem.show();

        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Erro de conexão:" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            //mensagem.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapterAbastecimento = pers_abastecer.buscarAbastecerRealizado(getActivity());
        lst_abastecer_realizado_completo.setAdapter(adapterAbastecimento);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connBanco != null) {
            connBanco.close();
        }
    }
}
