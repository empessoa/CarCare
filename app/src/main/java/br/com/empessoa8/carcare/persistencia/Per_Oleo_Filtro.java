package br.com.empessoa8.carcare.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.adapter.AdapterOleoFiltros;
import br.com.empessoa8.carcare.entidade.Oleo_Filtros;

/**
 * Created by elias on 21/06/2017.
 */

public class Per_Oleo_Filtro {
    SQLiteDatabase conn;

    public Per_Oleo_Filtro(SQLiteDatabase conn){
        this.conn = conn;
    }

    private ContentValues preencherTabelaIgnicao(Oleo_Filtros oleo_filtros) {

        ContentValues values = new ContentValues();
        values.put("data_do_servico", oleo_filtros.getData_do_servico());
        values.put("km_do_servico", oleo_filtros.getKm_do_servico());
        values.put("valor_do_servico", oleo_filtros.getValor_do_servico());
        values.put("data_proxima_revisao", oleo_filtros.getData_proxima_revisao());
        values.put("km_proxima_revisao", oleo_filtros.getKm_proxima_revisao());
        values.put("oleo_do_motor", oleo_filtros.getOleo_do_motor());
        values.put("oleo_da_direcao", oleo_filtros.getOleo_da_direcao());
        values.put("filtro_do_oleo", oleo_filtros.getFiltro_do_oleo());
        values.put("filtro_do_combustivel ", oleo_filtros.getFiltro_do_combustivel());
        values.put("filtro_do_ar", oleo_filtros.getFiltro_do_ar());
        values.put("filtro_do_ar_condicionado", oleo_filtros.getFiltro_do_ar_condicionado());
        values.put("lubrificante_tipo ", oleo_filtros.getLubrificante_tipo());
        return values;
    }
    public void inserirServicoOleoFiltro(Oleo_Filtros oleo_filtros) {
        ContentValues values = preencherTabelaIgnicao(oleo_filtros);
        conn.insertOrThrow("OLEOFREIO", null, values);
    }

    public void excluirServicoOleoFiltro(int _id) {
        conn.delete("OLEOFREIO", "_id = ?", new String[]{String.valueOf(_id)});
    }

    public AdapterOleoFiltros buscarServicoOleoFiltro(Context context){
        AdapterOleoFiltros adapter = new AdapterOleoFiltros(
                context,
                R.layout.list_oleo_filtro
        );

        Cursor cursor = conn.rawQuery("SELECT * FROM OLEOFREIO ORDER BY _id DESC", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela

            do {
                Oleo_Filtros servico = new Oleo_Filtros();

                servico.set_id(cursor.getInt(0));
                servico.setData_do_servico(cursor.getString(1));
                servico.setKm_do_servico(cursor.getInt(2));
                servico.setValor_do_servico(cursor.getFloat(3));
                servico.setData_proxima_revisao(cursor.getString(4));
                servico.setKm_proxima_revisao(cursor.getInt(5));
                if (!cursor.isNull(6)) {
                    servico.setOleo_do_motor(cursor.getString(6) + "\n");
                } else {
                    servico.setOleo_do_motor("");
                }
                if (!cursor.isNull(7)) {
                    servico.setOleo_da_direcao(cursor.getString(7) + "\n");
                } else {
                    servico.setOleo_da_direcao("");
                }
                if (!cursor.isNull(8)) {
                    servico.setFiltro_do_oleo(cursor.getString(8) + "\n");
                } else {
                    servico.setFiltro_do_oleo("");
                }
                if (!cursor.isNull(9)) {
                    servico.setFiltro_do_combustivel(cursor.getString(9) + "\n");
                } else {
                    servico.setFiltro_do_combustivel("");
                }
                if (!cursor.isNull(10)) {
                    servico.setFiltro_do_ar(cursor.getString(10) + "\n");
                } else {
                    servico.setFiltro_do_ar("");
                }
                if (!cursor.isNull(11)) {
                    servico.setFiltro_do_ar_condicionado(cursor.getString(11) + "\n");
                } else {
                    servico.setFiltro_do_ar_condicionado("");
                }
                servico.setLubrificante_tipo(cursor.getString(12));

                adapter.add(servico);

            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }

        return adapter;
    }

    public String exibirDataProximarevisaoOleoFiltro() {
        String data = "";
        Cursor cursor = conn.rawQuery("SELECT * FROM OLEOFREIO ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o primeiro elemento da tabela
            do {
                Oleo_Filtros servico = new Oleo_Filtros();
                data = servico.setData_proxima_revisao(cursor.getString(4));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return data;
    }

    public Integer exibirKmProximaRevisao() {
        int km = 0;
        Cursor cursor = conn.rawQuery("SELECT * FROM OLEOFREIO ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o ultimo elemento da tabela
            do {
                Oleo_Filtros servico = new Oleo_Filtros();
                km = servico.setKm_proxima_revisao(cursor.getInt(5));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return km;
    }

}
