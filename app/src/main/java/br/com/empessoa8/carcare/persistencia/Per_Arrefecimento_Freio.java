package br.com.empessoa8.carcare.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.adapter.AdapterArrefecimentoFreio;
import br.com.empessoa8.carcare.entidade.Arrefecimento_Freio;

/**
 * Created by elias on 23/06/2017.
 */

public class Per_Arrefecimento_Freio {

    SQLiteDatabase conn;

    public Per_Arrefecimento_Freio(SQLiteDatabase conn){
        this.conn = conn;
    }

    private ContentValues preencherTabelaCorreiaTensor(Arrefecimento_Freio arrefecimento_freio) {

        ContentValues values = new ContentValues();
        values.put("data_do_servico", arrefecimento_freio.getData_do_servico());
        values.put("km_do_servico", arrefecimento_freio.getKm_do_servico());
        values.put("valor_do_servico", arrefecimento_freio.getValor_do_servico());
        values.put("data_proxima_revisao", arrefecimento_freio.getData_proxima_revisao());
        values.put("km_proxima_revisao", arrefecimento_freio.getKm_proxima_revisao());
        values.put("agua_arrefecimento", arrefecimento_freio.getAgua_arrefecimento());
        values.put("oleo_freio", arrefecimento_freio.getOleo_freio());
        values.put("pastilhas_freios", arrefecimento_freio.getPastilhas_freios());

        return values;
    }
    public void inserirServico(Arrefecimento_Freio arrefecimento_freio) {
        ContentValues values = preencherTabelaCorreiaTensor(arrefecimento_freio);
        conn.insertOrThrow("ARREFECIMENTOFREIO", null, values);
    }

    public void excluirServico(int _id) {
        conn.delete("ARREFECIMENTOFREIO", "_id = ?", new String[]{String.valueOf(_id)});
    }

    public AdapterArrefecimentoFreio buscarServico(Context context){
        AdapterArrefecimentoFreio adapter = new AdapterArrefecimentoFreio(
                context,
                R.layout.list_arrefecimento_freio
        );

        Cursor cursor = conn.rawQuery("SELECT * FROM ARREFECIMENTOFREIO ORDER BY _id DESC", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela

            do {
                Arrefecimento_Freio arrefecimento_freio = new Arrefecimento_Freio();
                arrefecimento_freio.set_id(cursor.getInt(0));
                arrefecimento_freio.setData_do_servico(cursor.getString(1));
                arrefecimento_freio.setKm_do_servico(cursor.getInt(2));
                arrefecimento_freio.setValor_do_servico(cursor.getFloat(3));
                arrefecimento_freio.setData_proxima_revisao(cursor.getString(4));
                arrefecimento_freio.setKm_proxima_revisao(cursor.getInt(5));
                if (!cursor.isNull(6)) {
                    arrefecimento_freio.setAgua_arrefecimento(cursor.getString(6) + "\n");
                } else {
                    arrefecimento_freio.setAgua_arrefecimento("");
                }
                if (!cursor.isNull(7)) {
                    arrefecimento_freio.setOleo_freio(cursor.getString(7) + "\n");
                } else {
                    arrefecimento_freio.setOleo_freio("");
                }
                if (!cursor.isNull(8)) {
                    arrefecimento_freio.setPastilhas_freios(cursor.getString(8) + "\n");
                } else {
                    arrefecimento_freio.setPastilhas_freios("");
                }

                adapter.add(arrefecimento_freio);

            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }

        return adapter;
    }

    public String exibirDataProximarevisao() {
        String data = "";
        Cursor cursor = conn.rawQuery("SELECT * FROM ARREFECIMENTOFREIO ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o ultimo elemento da tabela
            do {
                Arrefecimento_Freio arrefecimento_freio = new Arrefecimento_Freio();
                data = arrefecimento_freio.setData_proxima_revisao(cursor.getString(4));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return data;
    }

    public Integer exibirKmProximaRevisao() {
        int km = 0;
        Cursor cursor = conn.rawQuery("SELECT * FROM ARREFECIMENTOFREIO ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o ultimo elemento da tabela
            do {
                Arrefecimento_Freio arrefecimento_freio = new Arrefecimento_Freio();
                km = arrefecimento_freio.setKm_proxima_revisao(cursor.getInt(5));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return km;
    }

}
