package br.com.empessoa8.carcare.fragments.correia_tensor;


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
import br.com.empessoa8.carcare.entidade.Correias_Tensor;
import br.com.empessoa8.carcare.persistencia.Per_Correia_Tensor;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Correias_Tensor_Realizados extends Fragment {
    View view;
    AlertDialog.Builder mensagem;

    private DataBase carCare_dataBase;
    private SQLiteDatabase connBanco;
    private Correias_Tensor correias_tensor;
    private Per_Correia_Tensor per_correia_tensor;
    private ArrayAdapter<Correias_Tensor> adp_ArrayAdapter;
    private ListView lst_correia_tensor;


    public Frag_Correias_Tensor_Realizados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.frag__correias__tensor__realizados, container, false);

        lst_correia_tensor = (ListView) view.findViewById(R.id.lst_servico_correia_tensor);
        lst_correia_tensor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogDelete(position);
                return false;
            }
        });

        correias_tensor = new Correias_Tensor();
        conectarBanco();
        return view;
    }

    private void conectarBanco() {

        try {

            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            //consulta o banco
            per_correia_tensor= new Per_Correia_Tensor(connBanco);
            adp_ArrayAdapter = per_correia_tensor.buscarServicoCorreiaTensor(getActivity());
            lst_correia_tensor.setAdapter(adp_ArrayAdapter);

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
            correias_tensor = new Correias_Tensor();
            correias_tensor = adp_ArrayAdapter.getItem(position);
            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            per_correia_tensor = new Per_Correia_Tensor(connBanco);
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("DELETAR SERVIÇO");
            mensagem.setMessage("CONFIRMAR!");
            mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    per_correia_tensor.excluirServicoCorreiaTensor(correias_tensor.get_id());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adp_ArrayAdapter = per_correia_tensor.buscarServicoCorreiaTensor(getActivity());
        lst_correia_tensor.setAdapter(adp_ArrayAdapter);
    }

}
