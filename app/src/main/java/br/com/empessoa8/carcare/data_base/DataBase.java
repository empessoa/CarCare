package br.com.empessoa8.carcare.data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by elias on 13/06/2017.
 */

public class DataBase extends SQLiteOpenHelper {
    public DataBase(Context context) {
        super(context, "CarCare", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptSQL.getCreateTableAbastecer());
        db.execSQL(ScriptSQL.getCreateTableIgnicao());
        db.execSQL(ScriptSQL.getCreateTableOleoFreio());
        db.execSQL(ScriptSQL.getCreateTableCorreiaTensor());
        db.execSQL(ScriptSQL.getCreateTableArrefecimentoFreio());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

