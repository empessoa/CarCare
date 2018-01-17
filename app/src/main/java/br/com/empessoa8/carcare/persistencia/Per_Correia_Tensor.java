package br.com.empessoa8.carcare.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.empessoa8.carcare.R;
import br.com.empessoa8.carcare.adapter.AdapterCorreiaTensor;
import br.com.empessoa8.carcare.entidade.Correias_Tensor;

/**
 * Created by elias on 23/06/2017.
 */

public class Per_Correia_Tensor {

    SQLiteDatabase conn;

    public Per_Correia_Tensor(SQLiteDatabase conn){
        this.conn = conn;
    }

    private ContentValues preencherTabelaCorreiaTensor(Correias_Tensor correias_tensor) {

        ContentValues values = new ContentValues();
        values.put("data_do_servico", correias_tensor.getData_do_servico());
        values.put("km_do_servico", correias_tensor.getKm_do_servico());
        values.put("valor_do_servico", correias_tensor.getValor_do_servico());
        values.put("data_proxima_revisao", correias_tensor.getData_proxima_revisao());
        values.put("km_proxima_revisao", correias_tensor.getKm_proxima_revisao());
        values.put("correia_dentada", correias_tensor.getCorreia_dentada());
        values.put("correia_poly_v", correias_tensor.getCorreia_poly_v());
        values.put("tensor_correia", correias_tensor.getTensor_correia());
        values.put("bomba_de_agua", correias_tensor.getBomba_de_agua());
        return values;
    }
    public void inserirServicoCorreiaTensor(Correias_Tensor correias_tensor) {
        ContentValues values = preencherTabelaCorreiaTensor(correias_tensor);
        conn.insertOrThrow("CORREIATENSOR", null, values);
    }

    public void excluirServicoCorreiaTensor(int _id) {
        conn.delete("CORREIATENSOR", "_id = ?", new String[]{String.valueOf(_id)});
    }

    public AdapterCorreiaTensor buscarServicoCorreiaTensor(Context context){
        AdapterCorreiaTensor adapter = new AdapterCorreiaTensor(
                context,
                R.layout.list_correia_tensor
        );

        Cursor cursor = conn.rawQuery("SELECT * FROM CORREIATENSOR ORDER BY _id DESC", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();//movendo o cursor para o primeiro elemento da tabela

            do {
                Correias_Tensor correias_tensor = new Correias_Tensor();

                correias_tensor.set_id(cursor.getInt(0));
                correias_tensor.setData_do_servico(cursor.getString(1));
                correias_tensor.setKm_do_servico(cursor.getInt(2));
                correias_tensor.setValor_do_servico(cursor.getFloat(3));
                correias_tensor.setData_proxima_revisao(cursor.getString(4));
                correias_tensor.setKm_proxima_revisao(cursor.getInt(5));
                if (!cursor.isNull(6)) {
                    correias_tensor.setCorreia_dentada(cursor.getString(6) + "\n");
                } else {
                    correias_tensor.setCorreia_dentada("");
                }
                if (!cursor.isNull(7)) {
                    correias_tensor.setCorreia_poly_v(cursor.getString(7) + "\n");
                } else {
                    correias_tensor.setCorreia_poly_v("");
                }
                if (!cursor.isNull(8)) {
                    correias_tensor.setTensor_correia(cursor.getString(8) + "\n");
                } else {
                    correias_tensor.setTensor_correia("");
                }if(!cursor.isNull(9)){
                    correias_tensor.setBomba_de_agua(cursor.getString(9));
                }else {
                    correias_tensor.setBomba_de_agua("");
                }

                adapter.add(correias_tensor);

            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }

        return adapter;
    }

    public String exibirDataProximarevisaoCorreiaTensor() {
        String data = "";
        Cursor cursor = conn.rawQuery("SELECT * FROM CORREIATENSOR ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o primeiro elemento da tabela
            do {
                Correias_Tensor correias_tensor = new Correias_Tensor();
                data = correias_tensor.setData_proxima_revisao(cursor.getString(4));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return data;
    }
    public Integer exibirKmProximaRevisao() {
        int km = 0;
        Cursor cursor = conn.rawQuery("SELECT * FROM CORREIATENSOR ORDER BY _id ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();//movendo o cursor para o ultimo elemento da tabela
            do {
                Correias_Tensor correias_tensor = new Correias_Tensor();
                km = correias_tensor.setKm_proxima_revisao(cursor.getInt(5));
            } while (cursor.moveToNext());//verifica se tem dado no proximo campo da tabela
        }
        return km;
    }

}
