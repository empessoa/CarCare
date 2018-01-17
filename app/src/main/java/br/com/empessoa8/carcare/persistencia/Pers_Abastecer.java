package br.com.empessoa8.carcare.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.adapter.AdapterAbastecer;
import br.com.empessoa8.carcare.adapter.AdapterAbastecerSimples;
import br.com.empessoa8.carcare.entidade.Abastecer;

/**
 * Created by elias on 13/06/2017.
 */

public class Pers_Abastecer {

    SQLiteDatabase conn;

    public Pers_Abastecer(SQLiteDatabase conn) {

        this.conn = conn;
    }

    private ContentValues preencherTabelaAbastecer(Abastecer abastecer) {
        ContentValues values = new ContentValues();
        values.put("data_do_abastecimento", abastecer.getData_do_abastecimento());
        values.put("combustivel_tipo", abastecer.getCombustivel_tipo());
        values.put("km_do_abastecimento", abastecer.getKm_do_abastecimento());
        values.put("valor_total_abastecido", abastecer.getValor_total_abastecido());
        values.put("valor_do_litro", abastecer.getValor_do_litro());
        values.put("total_de_litro", abastecer.getTotal_de_litro());
        return values;
    }

    public void inserirAbastecimento(Abastecer abastecer) {
        ContentValues values = preencherTabelaAbastecer(abastecer);
        conn.insertOrThrow("ABASTECER", null, values);
    }

    public void excluirAbastecimento(int _id) {
        conn.delete("ABASTECER", "_id = ?", new String[]{String.valueOf(_id)});
    }

    public AdapterAbastecer buscarAbastecerRealizado(Context context) {
        AdapterAbastecer adpAbastecerRealizado = new AdapterAbastecer(
                context,
                R.layout.list_abastecer
        );

        //buscando a tabela.
        Cursor cursor = conn.rawQuery("SELECT * FROM ABASTECER ORDER BY _id DESC", null);

        //verificando se contem dados na tabela
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela

            do {
                Abastecer abastecer = new Abastecer();

                abastecer.set_id(cursor.getInt(0));
                abastecer.setData_do_abastecimento(cursor.getString(1));
                abastecer.setCombustivel_tipo(cursor.getString(2));
                abastecer.setKm_do_abastecimento(cursor.getInt(3));
                abastecer.setValor_total_abastecido(cursor.getFloat(4));
                abastecer.setValor_do_litro(cursor.getFloat(5));
                abastecer.setTotal_de_litro(cursor.getFloat(6));

                adpAbastecerRealizado.add(abastecer);

            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }

        return adpAbastecerRealizado;
    }

    public AdapterAbastecerSimples buscaAbastecimentoSimples(Context context) {

        AdapterAbastecerSimples adapterAbastecerSimples = new AdapterAbastecerSimples(
                context,
                R.layout.list_abastecer_simples
        );

        Cursor cursor = conn.rawQuery("SELECT * FROM ABASTECER ORDER BY _id DESC", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela

            do {
                Abastecer abastecer = new Abastecer();

                abastecer.set_id(cursor.getInt(0));

                abastecer.setData_do_abastecimento(cursor.getString(1));
                abastecer.setKm_do_abastecimento(cursor.getInt(3));
                abastecer.setTotal_de_litro(cursor.getFloat(6));

                adapterAbastecerSimples.add(abastecer);


            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }

        return adapterAbastecerSimples;
    }
    public Integer exibirKmAbastecido() {
        int km = 0;
        Cursor cursor = conn.rawQuery("SELECT * FROM ABASTECER ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o ultimo elemento da tabela
            do {
                Abastecer abastecer = new Abastecer();
                km =   abastecer.setKm_do_abastecimento(cursor.getInt(3));
            } while (cursor.moveToNext());//verificar se tem dado no proximo campo da tabela
        }
        return km;
    }
    public String exibirDataProximarevisao() {
        String data = "";
        Cursor cursor = conn.rawQuery("SELECT * FROM ABASTECER ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o primeiro elemento da tabela
            do {
                Abastecer abastecer = new Abastecer();
                data =  abastecer.setData_do_abastecimento(cursor.getString(1));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return data;
    }
}
























