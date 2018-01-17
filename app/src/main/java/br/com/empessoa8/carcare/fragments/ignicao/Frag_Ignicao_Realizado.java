package br.com.empessoa8.carcare.fragments.ignicao;


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
import br.com.empessoa8.carcare.entidade.Ignicao;
import br.com.empessoa8.carcare.persistencia.Pers_Ignicao;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Ignicao_Realizado extends Fragment {
    View view;
    AlertDialog.Builder mensagem;

    private DataBase carCare_dataBase;
    private SQLiteDatabase connBanco;
    private Ignicao servico_ignicao;
    private Pers_Ignicao repor_servico_ignicao;
    private ArrayAdapter<Ignicao> adp_servico_ignicao;
    private ListView lst_servico_ignicao;

    public Frag_Ignicao_Realizado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.frag__ignicao__realizado, container, false);

        lst_servico_ignicao = (ListView) view.findViewById(R.id.lst_servico_ignicao);
        lst_servico_ignicao.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogDelete(position);
                return false;
            }
        });

        conectarBanco();
        servico_ignicao = new Ignicao();

        return view;
    }
    private void conectarBanco() {

        try {

            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            //consulta o banco
            repor_servico_ignicao = new Pers_Ignicao(connBanco);
            adp_servico_ignicao = repor_servico_ignicao.buscarServicoIgnicao(getActivity());
            lst_servico_ignicao.setAdapter(adp_servico_ignicao);

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
            servico_ignicao = new Ignicao();
            servico_ignicao = adp_servico_ignicao.getItem(position);
            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            repor_servico_ignicao = new Pers_Ignicao(connBanco);
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("DELETAR SERVIÇO");
            mensagem.setMessage("CONFIRMAR!");
            mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    repor_servico_ignicao.excluirServicoIgnicao(servico_ignicao.get_id());

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
        adp_servico_ignicao = repor_servico_ignicao.buscarServicoIgnicao(getActivity());
        lst_servico_ignicao.setAdapter(adp_servico_ignicao);
    }

}
