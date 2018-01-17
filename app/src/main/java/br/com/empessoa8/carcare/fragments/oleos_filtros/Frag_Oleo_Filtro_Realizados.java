package br.com.empessoa8.carcare.fragments.oleos_filtros;


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
import br.com.empessoa8.carcare.entidade.Oleo_Filtros;
import br.com.empessoa8.carcare.persistencia.Per_Oleo_Filtro;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Oleo_Filtro_Realizados extends Fragment {

    View view;
    AlertDialog.Builder mensagem;

    private DataBase carCare_dataBase;
    private SQLiteDatabase connBanco;
    private Oleo_Filtros oleo_filtros;
    private Per_Oleo_Filtro per_oleo_filtro;
    private ArrayAdapter<Oleo_Filtros> adp_ArrayAdapter;
    private ListView lst_oleo_filtro;

    public Frag_Oleo_Filtro_Realizados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag__oleo__filtro__realizados, container, false);

        lst_oleo_filtro = (ListView) view.findViewById(R.id.lst_servico_oleo_freio);
        lst_oleo_filtro.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogDelete(position);
                return false;
            }
        });

        oleo_filtros = new Oleo_Filtros();
        conectarBanco();
        return view;
    }

    private void conectarBanco() {

        try {

            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            //consulta o banco
            per_oleo_filtro = new Per_Oleo_Filtro(connBanco);
            adp_ArrayAdapter = per_oleo_filtro.buscarServicoOleoFiltro(getActivity());
            lst_oleo_filtro.setAdapter(adp_ArrayAdapter);

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
            oleo_filtros = new Oleo_Filtros();
            oleo_filtros = adp_ArrayAdapter.getItem(position);
            carCare_dataBase = new DataBase(getActivity());
            connBanco = carCare_dataBase.getReadableDatabase();
            per_oleo_filtro = new Per_Oleo_Filtro(connBanco);
            mensagem = new AlertDialog.Builder(getActivity());
            mensagem.setTitle("DELETAR SERVIÇO");
            mensagem.setMessage("CONFIRMAR!");
            mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    per_oleo_filtro.excluirServicoOleoFiltro(oleo_filtros.get_id());
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
        adp_ArrayAdapter = per_oleo_filtro.buscarServicoOleoFiltro(getActivity());
        lst_oleo_filtro.setAdapter(adp_ArrayAdapter);
    }

}
