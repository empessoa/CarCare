package br.com.empessoa8.carcare.fragments.arrefecimento_freio;


import android.content.DialogInterface;
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
import android.widget.Toast;

import br.com.empessoa8.carcare.Act_Main;
import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.data_base.DataBase;
import br.com.empessoa8.carcare.entidade.Arrefecimento_Freio;
import br.com.empessoa8.carcare.persistencia.Per_Arrefecimento_Freio;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Arrefecimento_Freio_Realizados extends Fragment {

    View view;
    AlertDialog.Builder mensagem;

    private DataBase carCare_dataBase;
    private SQLiteDatabase connBanco;
    private Arrefecimento_Freio arrefecimento_freio;
    private Per_Arrefecimento_Freio per_arrefecimento_freio;
    private ArrayAdapter<Arrefecimento_Freio> adp_ArrayAdapter;
    private ListView lst_arrefecimento_freio;


    public Frag_Arrefecimento_Freio_Realizados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__arrefecimento__freio__realizados, container, false);

        lst_arrefecimento_freio = (ListView) view.findViewById(R.id.lst_servico_arrefecimento_freio);
        lst_arrefecimento_freio.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               dialogDelete(position);
                return false;
            }
        });

        arrefecimento_freio = new Arrefecimento_Freio();
        conectarBanco();
        return view;
    }

    private void conectarBanco() {
        try {

            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            //consulta o banco
            per_arrefecimento_freio= new Per_Arrefecimento_Freio(connBanco);
            adp_ArrayAdapter = per_arrefecimento_freio.buscarServico(getActivity());
            lst_arrefecimento_freio.setAdapter(adp_ArrayAdapter);

            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Ignicao conectada ao banco");
            mensagem.setNeutralButton("Sair", null);
            mensagem.create();
            //mensagem.show();

        } catch (SQLException ex) {
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("Verificando conexão com o Banco");
            mensagem.setMessage("Erro de conexão:" + ex.getMessage());
            mensagem.setNeutralButton("Sair", null);
            mensagem.create();
            mensagem.show();
        }
    }

    private void dialogDelete(int position) {
        try {
            arrefecimento_freio = new Arrefecimento_Freio();
            arrefecimento_freio = adp_ArrayAdapter.getItem(position);
            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            per_arrefecimento_freio = new Per_Arrefecimento_Freio(connBanco);
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("DELETAR SERVIÇO");
            mensagem.setMessage("CONFIRMAR!");
            mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    per_arrefecimento_freio.excluirServico(arrefecimento_freio.get_id());
                    Intent intent = new Intent(getActivity(), Act_Main.class);
                    startActivityForResult(intent, 0);
                    getActivity().finish();

                    Toast.makeText(getActivity(), "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
            mensagem.setNegativeButton("NÂO", new DialogInterface.OnClickListener() {
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
            mensagem.create();
            mensagem.show();
        }
    }


}
