package br.com.empessoa8.carcare.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.adapter.AdapterIgnicao;
import br.com.empessoa8.carcare.entidade.Ignicao;

/**
 * Created by elias on 14/06/2017.
 */

public class Pers_Ignicao {

    SQLiteDatabase conn;

    public Pers_Ignicao(SQLiteDatabase conn){
        this.conn = conn;
    }

    private ContentValues preencherTabelaIgnicao(Ignicao ignicao) {

        ContentValues values = new ContentValues();
        values.put("data_do_servico", ignicao.getData_do_servico());
        values.put("km_do_servico", ignicao.getKm_do_servico());
        values.put("valor_do_servico", ignicao.getValor_do_servico());
        values.put("data_proxima_revisao", ignicao.getData_proxima_revisao());
        values.put("km_proxima_revisao", ignicao.getKm_proxima_revisao());
        values.put("velas_ignicao", ignicao.getVelas_ignicao());
        return values;
    }

    public void inserirServicoIgnicao(Ignicao servico_ignicao) {
        ContentValues values = preencherTabelaIgnicao(servico_ignicao);
        conn.insertOrThrow("IGNICAO", null, values);
    }

    public void excluirServicoIgnicao(int _id) {
        conn.delete("IGNICAO", "_id = ?", new String[]{String.valueOf(_id)});
    }

    public AdapterIgnicao buscarServicoIgnicao(Context context){
        AdapterIgnicao adapterIgnicao = new AdapterIgnicao(
                context,
                R.layout.list_ignicao
        );

        Cursor cursor = conn.rawQuery("SELECT * FROM IGNICAO ORDER BY _id DESC", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela

            do {
                Ignicao servico_ignicao = new Ignicao();

                servico_ignicao.set_id(cursor.getInt(0));
                if (!cursor.isNull(6)) {
                    servico_ignicao.setVelas_ignicao(cursor.getString(6) + "\n");
                } else {
                    servico_ignicao.setVelas_ignicao("");
                }
                servico_ignicao.setData_do_servico(cursor.getString(1));
                servico_ignicao.setKm_do_servico(cursor.getInt(2));
                servico_ignicao.setValor_do_servico(cursor.getFloat(3));
                servico_ignicao.setData_proxima_revisao(cursor.getString(4));
                servico_ignicao.setKm_proxima_revisao(cursor.getInt(5));

                adapterIgnicao.add(servico_ignicao);

            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }

        return adapterIgnicao;
    }

    public String exibirDataProximarevisao() {
        String data = "";
        Cursor cursor = conn.rawQuery("SELECT * FROM IGNICAO ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o primeiro elemento da tabela
            do {
                Ignicao servico_ignicao = new Ignicao();
                data = servico_ignicao.setData_proxima_revisao(cursor.getString(4));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return data;
    }
    public Integer exibirKmProximaRevisao() {
        int km = 0;
        Cursor cursor = conn.rawQuery("SELECT * FROM IGNICAO ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o ultimo elemento da tabela
            do {
                Ignicao servico_ignicao = new Ignicao();
                km = servico_ignicao.setKm_proxima_revisao(cursor.getInt(5));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return km;
    }
}
